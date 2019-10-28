package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.anew.PaySession;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantPayOrderApplyDTO;
import com.rxh.anew.dto.MerchantPayOrderConfirmDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.shortcut.NewPayOrderService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelHistoryTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午5:28
 * Description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewPayOrderController extends NewAbstractCommonController {

    private  final Md5Component md5Component;
    private  final NewPayOrderService newPayOrderService;
    private  final TransOrderMQ transOrderMQ;
    /**
     *  支付下单申请
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/payApply", produces = "text/html;charset=UTF-8")
    public String bondCardApply(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【支付下单申请】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantPayOrderApplyDTO merchantPayOrderApplyDTO =null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merchantPayOrderApplyDTO = JSON.parse(sotTable.getRequestMsg(), MerchantPayOrderApplyDTO.class);
            sotTable.setMerId(merchantPayOrderApplyDTO.getMerId()).setMerOrderId(merchantPayOrderApplyDTO.getMerOrderId()).setReturnUrl(merchantPayOrderApplyDTO.getReturnUrl()).setNoticeUrl(merchantPayOrderApplyDTO.getNoticeUrl());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB7();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merchantPayOrderApplyDTO.getMerId(), merchantPayOrderApplyDTO.getTerminalMerId(),bussType);
            //参数校验
            this.verify(paramRuleMap, merchantPayOrderApplyDTO, MerchantPayOrderApplyDTO.class,ipo);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newPayOrderService.multipleOrder(merchantPayOrderApplyDTO.getMerOrderId(),ipo);
            //查询通道使用记录
            ChannelHistoryTable channelHistoryTable = newPayOrderService.getChannelHistoryInfo(merchantPayOrderApplyDTO,ipo);
            //没有通道使用记录判断是否有进行进件和帮卡操作
            if(isNull(channelHistoryTable)) {
                //获取成功进件记录
                List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(merchantPayOrderApplyDTO, ipo);
                //根据进件信息获取邦卡记录
                List<MerchantCardTable> merchantCardTableList = newPayOrderService.getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                //判断该卡是否进行绑卡操作
                newPayOrderService.checkBondCardByCardNum(merchantCardTableList, merchantPayOrderApplyDTO,ipo);
                //清空垃圾
                paramRuleMap = null;
                registerCollectTableList = null;
                merchantCardTableList = null;
            }
            //执行平台风控
            //获取商户风控表
            MerchantQuotaRiskTable merchantQuotaRiskTable = newPayOrderService.getMerchantQuotaRiskByMerId(merInfoTable.getMerchantId(),ipo);
            //执行单笔风控
            newPayOrderService.checkSingleAmountRisk(merchantPayOrderApplyDTO,merchantQuotaRiskTable,ipo);
            //获取风控交易量统计数据
            Tuple2<RiskQuotaTable,RiskQuotaTable> MerRiskQuota = newPayOrderService.getRiskQuotaInfoByMer(merInfoTable,ipo);
            //执行风控控制
            newPayOrderService.executePlatformRisk(merchantPayOrderApplyDTO,merchantQuotaRiskTable,MerRiskQuota,ipo);
            //执行通道风控
            //最终使用的通道
            ChannelInfoTable channelInfoTable = null;
            //获取该通道历史统计交易量
            Tuple2<RiskQuotaTable,RiskQuotaTable> channelRiskQuota = null;
            if( !isNull(channelHistoryTable) ){
                //获取通道信息
                channelInfoTable = newPayOrderService.getChannelInfoByChannelHistory(channelHistoryTable,ipo);
                //获取该通道历史统计交易量
                 channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                //执行通道风控
                channelInfoTable = newPayOrderService.executeChannelRisk(merchantPayOrderApplyDTO,channelInfoTable,channelRiskQuota,ipo);
            }
            if(isNull(channelInfoTable)){//通道切换
                //获取商户可用支付的所有通道
                List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannel(merchantPayOrderApplyDTO,channelHistoryTable,ipo);
                //获取一个可用通
                channelInfoTable = newPayOrderService.getFeasibleChannel(merchantPayOrderApplyDTO,channelInfoTableList,ipo);
            }
            //保存订单信息
            PayOrderInfoTable payOrderInfoTable = newPayOrderService.savePayOrderInfo(merInfoTable, merchantPayOrderApplyDTO,channelInfoTable,ipo);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple2(channelInfoTable,payOrderInfoTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfo(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //非失败的情况下，缓存
            if( !crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._1.getStatus())){
                ChannelHistoryTable  cht = newPayOrderService.updateByChannelHistoryInfo(channelHistoryTable,payOrderInfoTable);
                Set<RiskQuotaTable>   rqtSet = newPayOrderService.updateByRiskQuotaInfo(payOrderInfoTable,MerRiskQuota,channelRiskQuota);
                //存放内部缓存
                PaySession.put(payOrderInfoTable.getPlatformOrderId(),new Tuple2<>(cht,rqtSet),System.currentTimeMillis());
            }
            //封装放回结果
            respResult = newPayOrderService.responseMsg(merchantPayOrderApplyDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode())).setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            respResult = newPayOrderService.responseMsg(null != merchantPayOrderApplyDTO ? merchantPayOrderApplyDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }

    /**
     * 支付交易确认
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/confirmPay", produces = "text/html;charset=UTF-8")
    public String confirmPay(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【支付交易确认】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantPayOrderConfirmDTO merchantPayOrderConfirmDTO = null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merchantPayOrderConfirmDTO = JSON.parse(sotTable.getRequestMsg(), MerchantPayOrderConfirmDTO.class);
            sotTable.setMerId(merchantPayOrderConfirmDTO.getMerId()).setPlatformOrderId(merchantPayOrderConfirmDTO.getPlatformOrderId());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB9();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merchantPayOrderConfirmDTO.getMerId(), merchantPayOrderConfirmDTO.getTerminalMerId(),bussType);
            //参数校验
            this.verify(paramRuleMap, merchantPayOrderConfirmDTO, MerchantPayOrderApplyDTO.class,ipo);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //判断平台订单号是否存在
            payOrderInfoTable = newPayOrderService.getPayOrderInfoByPlatformOrderId(merchantPayOrderConfirmDTO.getPlatformOrderId(),ipo);
            //取缓存数据
            Tuple2<ChannelHistoryTable,Set<RiskQuotaTable>>  tuple2= (Tuple2<ChannelHistoryTable, Set<RiskQuotaTable>>) PaySession.getAndRemove(payOrderInfoTable.getPlatformOrderId());
            //判断缓存数据是否存在
            assertIsNull(tuple2,ResponseCodeEnum.RXH99998,ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newPayOrderService.getChannelInfoByChannelId(payOrderInfoTable.getChannelId(),ipo);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple2(channelInfoTable,payOrderInfoTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfo(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //状态为成功是才执行以下操作
            if(crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._0.getStatus())){
                //更新通道历史使用记录 8_channel_history_table
                ChannelHistoryTable  cht = tuple2._;
                //更新 商户和通道使用汇总情况 8_risk_quota_table
                Set<RiskQuotaTable>   rqtSet = tuple2._2;
                //执行事务更新操作
                newPayOrderService.batchUpdatePayOderCorrelationInfo(payOrderInfoTable,cht,rqtSet,ipo);
                //发送到队列
                transOrderMQ.sendObjectMessageToPayOderMsgMQ(payOrderInfoTable);
            }
            //发送异步查找队列
            if( !crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._3.getStatus()) ){

            }
            //封装放回结果
            respResult = newPayOrderService.responseMsg(payOrderInfoTable.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode())).setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            respResult = newPayOrderService.responseMsg(null != payOrderInfoTable ? payOrderInfoTable.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }


    /**
     *
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/confirmCode", produces = "text/html;charset=UTF-8")
    public String confirmCode(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【支付交易确认】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantPayOrderConfirmDTO merchantPayOrderConfirmDTO = null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merchantPayOrderConfirmDTO = JSON.parse(sotTable.getRequestMsg(), MerchantPayOrderConfirmDTO.class);
            sotTable.setMerId(merchantPayOrderConfirmDTO.getMerId()).setPlatformOrderId(merchantPayOrderConfirmDTO.getPlatformOrderId());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB8();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merchantPayOrderConfirmDTO.getMerId(), merchantPayOrderConfirmDTO.getTerminalMerId(),bussType);
            //参数校验
            this.verify(paramRuleMap, merchantPayOrderConfirmDTO, MerchantPayOrderApplyDTO.class,ipo);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //判断平台订单号是否存在
            payOrderInfoTable = newPayOrderService.getPayOrderInfoByPlatformOrderId(merchantPayOrderConfirmDTO.getPlatformOrderId(),ipo);
            //取缓存数据
            Tuple2<ChannelHistoryTable,Set<RiskQuotaTable>>  tuple2= (Tuple2<ChannelHistoryTable, Set<RiskQuotaTable>>) PaySession.getAndRemove(payOrderInfoTable.getPlatformOrderId());
            //判断缓存数据是否存在
            assertIsNull(tuple2,ResponseCodeEnum.RXH99998,ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newPayOrderService.getChannelInfoByChannelId(payOrderInfoTable.getChannelId(),ipo);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple2(channelInfoTable,payOrderInfoTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfo(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //封装放回结果
            respResult = newPayOrderService.responseMsg(payOrderInfoTable.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode())).setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            respResult = newPayOrderService.responseMsg(null != payOrderInfoTable ? payOrderInfoTable.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }



}
