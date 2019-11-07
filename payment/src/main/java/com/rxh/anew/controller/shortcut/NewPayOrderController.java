package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.channel.CommonChannelHandlePortComponent;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerPayOrderApplyDTO;
import com.rxh.anew.dto.MerPayOrderConfirmDTO;
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
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.OrganizationInfoTable;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.anew.tools.SpringContextUtil;
import com.rxh.enums.BusinessTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple2;
import com.rxh.tuple.Tuple4;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 收单
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 下午7:40
 * Description:
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewPayOrderController extends NewAbstractCommonController {
    private  final Md5Component md5Component;
    private  final NewPayOrderService newPayOrderService;
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
        MerPayOrderApplyDTO merPayOrderApplyDTO =null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //0.解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merPayOrderApplyDTO = JSON.parse(sotTable.getRequestMsg(), MerPayOrderApplyDTO.class);
            sotTable.setMerId(merPayOrderApplyDTO.getMerId()).setMerOrderId(merPayOrderApplyDTO.getMerOrderId()).setReturnUrl(merPayOrderApplyDTO.getReturnUrl()).setNoticeUrl(merPayOrderApplyDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merPayOrderApplyDTO.getMerId(), merPayOrderApplyDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB7();
            //参数校验
            this.verify(paramRuleMap, merPayOrderApplyDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newPayOrderService.multipleOrder(merPayOrderApplyDTO.getMerOrderId(),ipo);
            //1.执行平台风控
            //获取商户风控表
            MerchantQuotaRiskTable merchantQuotaRiskTable = newPayOrderService.getMerchantQuotaRiskByMerId(merInfoTable.getMerchantId(),ipo);
            //执行单笔风控
            newPayOrderService.checkSingleAmountRisk(merPayOrderApplyDTO.getAmount(),merchantQuotaRiskTable,ipo);
            //获取风控交易量统计数据
            Tuple2<RiskQuotaTable,RiskQuotaTable> merRiskQuota = newPayOrderService.getRiskQuotaInfoByMer(merInfoTable,ipo);
            //执行风控控制
            newPayOrderService.executePlatformRisk(merPayOrderApplyDTO.getAmount(),merchantQuotaRiskTable,merRiskQuota,ipo);
            //2.查询通道使用记录
            ChannelHistoryTable channelHistoryTable = newPayOrderService.getChannelHistoryInfo(ipo,merPayOrderApplyDTO.getMerId(),merPayOrderApplyDTO.getTerMerId(),merPayOrderApplyDTO.getProductType());
            //通道信息
            ChannelInfoTable channelInfoTable = null;
            //进件信息
            RegisterCollectTable registerCollectTable = null;
            //绑卡信息
            MerchantCardTable merchantCardTable = null;
            //没有通道使用记录
            if(isNull(channelHistoryTable)){
                //获取成功进件记录
                List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(ipo,merPayOrderApplyDTO.getMerId(),merPayOrderApplyDTO.getTerMerId(),merPayOrderApplyDTO.getProductType());
                //根据商户配置信息
                List<MerchantSettingTable> merchantSettingTableList = newPayOrderService.getMerchantSetting(ipo);
                //根据配置通道信息过滤可用的进件信息
                registerCollectTableList = newPayOrderService.filterRegCollectByMerSet(registerCollectTableList,merchantSettingTableList,ipo);
                //根据进件信息获取邦卡记录
                List<MerchantCardTable> merchantCardTableList = newPayOrderService.getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                //根据收单的信息过滤出绑卡信息
                merchantCardTableList= newPayOrderService.filterMerCardByPaymentMsg(merchantCardTableList,ipo,merPayOrderApplyDTO.getBankCardNum(),merPayOrderApplyDTO.getBankCardPhone());
                //根据进件信息和绑卡信息过滤进件信息,  merchantCardTableList,registerCollectTableList 有共同的组织机构
                registerCollectTableList  = newPayOrderService.filterRegCollectInfoByMerCard(registerCollectTableList,merchantCardTableList,ipo);
                //获取可行的通道
                List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannelList(registerCollectTableList,ipo,merPayOrderApplyDTO.getProductType(),BusinessTypeEnum.PAY.getBusiType());
                //获取最终可用的通道
                channelInfoTable = newPayOrderService.getFeasibleChannel(channelInfoTableList,ipo,merPayOrderApplyDTO.getAmount());
                //确定进件信息
                registerCollectTable = newPayOrderService.finallyFilterRegCollect(channelInfoTable,registerCollectTableList,ipo);
                //确定绑卡信息
                merchantCardTable = newPayOrderService.finallyFilterMerCard(merchantCardTableList,ipo,merPayOrderApplyDTO.getBankCardNum() ,merPayOrderApplyDTO.getBankCardPhone());
            }else{
                //获取通道信息
                channelInfoTable = newPayOrderService.getChannelInfoByChannelHistory(channelHistoryTable,ipo);
                //根据商户配置信息
                List<MerchantSettingTable> merchantSettingTableList = newPayOrderService.getMerchantSetting(ipo);
                //判断商户是否该通道,如果该channelInfoTable没在merchantSettingTableList列表中，则制空
                channelInfoTable = newPayOrderService.judgeThisChannelUsable(channelInfoTable,merchantSettingTableList);
                //备份一个通道信息
                ChannelInfoTable channelInfoTable_back = channelInfoTable;
                //执行通道风控
                if(!isNull(channelInfoTable)){
                    //获取该通道历史统计交易量
                    Tuple2<RiskQuotaTable,RiskQuotaTable> channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                    //执行通道风控
                    channelInfoTable = newPayOrderService.executeChannelRisk(channelInfoTable,channelRiskQuota,ipo,merPayOrderApplyDTO.getAmount());
                }
                //如果该通道从商户配置中删除，则该通道为null,需要重新获取一次
                if(isNull(channelInfoTable)){
                    //获取成功进件记录
                    List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(ipo,merPayOrderApplyDTO.getMerId(),merPayOrderApplyDTO.getTerMerId(),merPayOrderApplyDTO.getProductType());
                    //根据配置通道信息过滤可用的进件信息
                    registerCollectTableList = newPayOrderService.filterRegCollectByMerSet(registerCollectTableList,merchantSettingTableList,ipo);
                    //根据进件信息获取邦卡记录
                    List<MerchantCardTable> merchantCardTableList = newPayOrderService.getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                    //根据收单的信息过滤出绑卡信息
                    merchantCardTableList= newPayOrderService.filterMerCardByPaymentMsg(merchantCardTableList,ipo,merPayOrderApplyDTO.getBankCardNum(),merPayOrderApplyDTO.getBankCardPhone());
                    //根据进件信息和绑卡信息过滤进件信息,  merchantCardTableList,registerCollectTableList 有共同的组织机构
                    registerCollectTableList  = newPayOrderService.filterRegCollectInfoByMerCard(registerCollectTableList,merchantCardTableList,ipo);
                    //获取可行的通道
                    List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannelList(registerCollectTableList,ipo,merPayOrderApplyDTO.getProductType(),BusinessTypeEnum.PAY.getBusiType());
                    //去除前面备份的通道
                    channelInfoTableList = newPayOrderService.subtractUnableChanInfo(channelInfoTableList,channelInfoTable_back,ipo);
                    //获取最终可用的通道
                    channelInfoTable = newPayOrderService.getFeasibleChannel(channelInfoTableList,ipo,merPayOrderApplyDTO.getAmount());
                    //确定进件信息
                    registerCollectTable = newPayOrderService.finallyFilterRegCollect(channelInfoTable,registerCollectTableList,ipo);
                    //确定绑卡信息
                    merchantCardTable = newPayOrderService.finallyFilterMerCard(merchantCardTableList,ipo,merPayOrderApplyDTO.getBankCardNum() ,merPayOrderApplyDTO.getBankCardPhone());
                }else {
                    //获取进件信息
                    registerCollectTable = newPayOrderService.getSuccessRegInfoByChanInfo(channelInfoTable, ipo);
                    //获取绑卡信息
                    merchantCardTable = newPayOrderService.getMerCardByChanAndReg(channelInfoTable, registerCollectTable, ipo, merPayOrderApplyDTO.getBankCardNum(),merPayOrderApplyDTO.getBankCardPhone());
                }
            }
            //3.保存订单信息
             payOrderInfoTable = newPayOrderService.savePayOrder(merInfoTable, merPayOrderApplyDTO,channelInfoTable,registerCollectTable,merchantCardTable,ipo);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,payOrderInfoTable,registerCollectTable,merchantCardTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfo(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,merPayOrderApplyDTO.getMerOrderId(),payOrderInfoTable.getPlatformOrderId(),merPayOrderApplyDTO.getAmount(),null,null);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
                log.info(printErrorMsg);
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != merPayOrderApplyDTO ? merPayOrderApplyDTO.getMerOrderId() : null, null != payOrderInfoTable ? payOrderInfoTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }


    /**
     * 支付短信验证码获取
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/getPayCode", produces = "text/html;charset=UTF-8")
    public String getPayCode(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【支付短信验证码获取】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerPayOrderConfirmDTO merPayOrderConfirmDTO = null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merPayOrderConfirmDTO = JSON.parse(sotTable.getRequestMsg(), MerPayOrderConfirmDTO.class);
            sotTable.setMerId(merPayOrderConfirmDTO.getMerId()).setPlatformOrderId(merPayOrderConfirmDTO.getPlatformOrderId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merPayOrderConfirmDTO.getMerId(), merPayOrderConfirmDTO.getTerminalMerId(),bussType);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB8();
            //参数校验
            this.verify(paramRuleMap, merPayOrderConfirmDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //判断平台订单号是否存在
            payOrderInfoTable = newPayOrderService.getPayOrderInfoByPlatformOrderId(merPayOrderConfirmDTO.getPlatformOrderId(), BusinessTypeEnum.b7.getBusiType(),ipo);
            sotTable.setPlatformOrderId(payOrderInfoTable.getPlatformOrderId()).setMerOrderId(payOrderInfoTable.getMerOrderId());
            //获取进件信息
            RegisterCollectTable registerCollectTable = newPayOrderService.getRegCollectInfo(payOrderInfoTable.getRegPlatformOrderId(),BusinessTypeEnum.b3.getBusiType(),ipo);
            //获取绑卡信息
            MerchantCardTable merchantCardTable =newPayOrderService.getMerchantCardInfoByPlatformOrderId(payOrderInfoTable.getCardPlatformOrderId(),BusinessTypeEnum.b6.getBusiType(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newPayOrderService.getChannelInfoByChannelId(payOrderInfoTable.getChannelId(),ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfoByB9Before(payOrderInfoTable,BusinessTypeEnum.b8.getBusiType(),ipo);
            //封装请求cross必要参数  channelInfoTable
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,payOrderInfoTable,registerCollectTable,merchantCardTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfo(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,payOrderInfoTable.getMerOrderId(),payOrderInfoTable.getPlatformOrderId(),payOrderInfoTable.getAmount().toString(),null,null);
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
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != payOrderInfoTable ? payOrderInfoTable.getMerOrderId() : null, null != payOrderInfoTable ? payOrderInfoTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
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
        MerPayOrderConfirmDTO merPayOrderConfirmDTO = null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merPayOrderConfirmDTO = JSON.parse(sotTable.getRequestMsg(), MerPayOrderConfirmDTO.class);
            sotTable.setMerId(merPayOrderConfirmDTO.getMerId()).setPlatformOrderId(merPayOrderConfirmDTO.getPlatformOrderId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merPayOrderConfirmDTO.getMerId(), merPayOrderConfirmDTO.getTerminalMerId(),bussType);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB9();
            //参数校验
            this.verify(paramRuleMap, merPayOrderConfirmDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //判断平台订单号是否存在
            payOrderInfoTable = newPayOrderService.getPayOrderInfoByPlatformOrderId(merPayOrderConfirmDTO.getPlatformOrderId(),null,ipo);
            //判断该订单是否已经使用过 ,正式环境必须打开
            newPayOrderService.checkPayOrderInfoTableByB9(payOrderInfoTable,ipo);
            //获取进件信息
            RegisterCollectTable registerCollectTable = newPayOrderService.getRegCollectInfo(payOrderInfoTable.getRegPlatformOrderId(),BusinessTypeEnum.b3.getBusiType(),ipo);
            //获取绑卡信息
            MerchantCardTable merchantCardTable =newPayOrderService.getMerchantCardInfoByPlatformOrderId(payOrderInfoTable.getCardPlatformOrderId(),BusinessTypeEnum.b6.getBusiType(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newPayOrderService.getChannelInfoByChannelId(payOrderInfoTable.getChannelId(),ipo);
            //获取组织机构信息
            OrganizationInfoTable organizationInfoTable = newPayOrderService.getOrganizationInfo(channelInfoTable.getOrganizationId(),ipo);
            Class  clz=Class.forName(organizationInfoTable.getApplicationClassObj().trim());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,payOrderInfoTable,registerCollectTable,merchantCardTable));
            //请求cross请求
            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelInfoTable,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfoByB9Before(payOrderInfoTable,BusinessTypeEnum.b9.getBusiType(),ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfoByB9After(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //状态为成功是才执行以下操作
            if(crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._0.getStatus())){
                /**
                 * 事务处理
                 */
                MerPayOrderApplyDTO  merPayOrderApplyDTO = newPayOrderService.getMerPayOrderApplyDTO(payOrderInfoTable);
                //查询通道使用记录  MerchantId  TerminalMerId ProductId
                ChannelHistoryTable channelHistoryTable = newPayOrderService.getChannelHistoryInfo(ipo,merPayOrderApplyDTO.getMerId(),merPayOrderApplyDTO.getTerMerId(),merPayOrderApplyDTO.getProductType());
                //更新通道历史使用记录 8_channel_history_table
                ChannelHistoryTable  cht = newPayOrderService.updateByChannelHistoryInfo(channelHistoryTable,payOrderInfoTable);
                //获取风控交易量统计数据
                Tuple2<RiskQuotaTable,RiskQuotaTable> merRiskQuota = newPayOrderService.getRiskQuotaInfoByMer(merInfoTable,ipo);
                //获取该通道历史统计交易量
                Tuple2<RiskQuotaTable,RiskQuotaTable> channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                //更新 商户和通道使用汇总情况 8_risk_quota_table
                Set<RiskQuotaTable>   rqtSet = newPayOrderService.updateByRiskQuotaInfo(payOrderInfoTable,merRiskQuota,channelRiskQuota);
                //执行事务更新操作
                newPayOrderService.batchUpdatePayOderCorrelationInfo(payOrderInfoTable,cht,rqtSet,ipo);
            }
            //通道差异化处理
            commonChannelHandlePortComponent.channelDifferBusinessHandleByPayOrder(requestCrossMsgDTO,crossResponseMsgDTO);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg，channelTab
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,payOrderInfoTable.getMerOrderId(),payOrderInfoTable.getPlatformOrderId(),payOrderInfoTable.getAmount().toString(),null,null,channelInfoTable.getChannelId());
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
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != payOrderInfoTable ? payOrderInfoTable.getMerOrderId() : null, null != payOrderInfoTable ? payOrderInfoTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }


}






