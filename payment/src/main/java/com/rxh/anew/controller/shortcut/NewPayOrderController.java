package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantPayOrderDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        MerchantPayOrderDTO merchantPayOrderDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merchantPayOrderDTO = JSON.parse(sotTable.getRequestMsg(),MerchantPayOrderDTO.class);
            sotTable.setMerId(merchantPayOrderDTO.getMerId()).setMerOrderId(merchantPayOrderDTO.getMerOrderId()).setReturnUrl(merchantPayOrderDTO.getReturnUrl()).setNoticeUrl(merchantPayOrderDTO.getNoticeUrl());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB7();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merchantPayOrderDTO.getMerId(),merchantPayOrderDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,merchantPayOrderDTO, MerchantPayOrderDTO.class,ipo);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newPayOrderService.multipleOrder(merchantPayOrderDTO.getMerOrderId(),ipo);
            //查询通道使用记录
            ChannelHistoryTable channelHistoryTable = newPayOrderService.getChannelHistoryInfo(merchantPayOrderDTO,ipo);
            //没有通道使用记录判断是否有进行进件和帮卡操作
            if(isNull(channelHistoryTable)) {
                //获取成功进件记录
                List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(merchantPayOrderDTO, ipo);
                //根据进件信息获取邦卡记录
                List<MerchantCardTable> merchantCardTableList = newPayOrderService.getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                //判断该卡是否进行绑卡操作
                newPayOrderService.checkBondCardByCardNum(merchantCardTableList,merchantPayOrderDTO,ipo);
                //清空垃圾
                paramRuleMap = null;
                registerCollectTableList = null;
                merchantCardTableList = null;
            }
            //执行平台风控
            //获取商户风控表
            MerchantQuotaRiskTable merchantQuotaRiskTable = newPayOrderService.getMerchantQuotaRiskByMerId(merInfoTable.getMerchantId(),ipo);
            //执行单笔风控
            newPayOrderService.checkSingleAmountRisk(merchantPayOrderDTO,merchantQuotaRiskTable,ipo);
            //获取风控交易量统计数据
            Tuple2<RiskQuotaTable,RiskQuotaTable> MerRiskQuota = newPayOrderService.getRiskQuotaInfoByMer(merInfoTable,ipo);
            //执行风控控制
            newPayOrderService.executePlatformRisk(merchantPayOrderDTO,merchantQuotaRiskTable,MerRiskQuota,ipo);
            //执行通道风控
            //最终使用的通道
            ChannelInfoTable channelInfoTable = null;
            if( !isNull(channelHistoryTable) ){
                //获取通道信息
                channelInfoTable = newPayOrderService.getChannelInfoByChannelHistory(channelHistoryTable,ipo);
                //获取该通道历史统计交易量
                Tuple2<RiskQuotaTable,RiskQuotaTable> channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                //执行通道风控
                channelInfoTable = newPayOrderService.executeChannelRisk(merchantPayOrderDTO,channelInfoTable,channelRiskQuota,ipo);
            }
            if(isNull(channelInfoTable)){//通道切换
                //获取商户可用支付的所有通道
                List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannel(merchantPayOrderDTO,channelHistoryTable,ipo);
                //获取一个可用通
                channelInfoTable = newPayOrderService.getFeasibleChannel(merchantPayOrderDTO,channelInfoTableList,ipo);
            }
            //保存订单信息
            PayOrderInfoTable payOrderInfoTable = newPayOrderService.savePayOrderInfo(merInfoTable,merchantPayOrderDTO,channelInfoTable,ipo);

            //获取进件成功的附属表
//            RegisterCollectTable registerCollectTable = newPayPaymentBondCardService.getSuccessRegisterCollectInfo(merchantPayOrderDTO,ipo);
            //根据平台流水号获取进件成功的附属表
//            RegisterCollectTable registerCollectTable = newPayOrderService.getRegisterInfoTableByPlatformOrderId(merchantPayOrderDTO.getPlatformOrderId(),ipo);
//            //获取进件主表
//            RegisterInfoTable registerInfoTable = newPayOrderService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
//            //获取通道信息
//            ChannelInfoTable channelInfoTable = newPayOrderService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
//            //获取通道附属信息
//            ChannelExtraInfoTable channelExtraInfoTable = newPayOrderService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
//            //保存绑卡申请记录
//            MerchantCardTable merchantCardTable = newPayOrderService.saveCardInfoByB4(merchantPayOrderDTO,channelInfoTable,registerCollectTable,ipo);
//            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
//            //封装请求cross必要参数
//            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
//            //发生cross请求
//            String crossResponseMsg = newPayOrderService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
//            //将请求结果转为对象
//            crossResponseMsgDTO = newPayOrderService.jsonToPojo(crossResponseMsg,ipo);
//            //更新进件信息
//            newPayOrderService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
//            //封装放回结果
//            respResult = newPayOrderService.responseMsg(merchantPayOrderDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
//            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode())).setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
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
//            respResult = newBondCardService.responseMsg(null != merchantPayOrderDTO ? merchantPayOrderDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
//            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
