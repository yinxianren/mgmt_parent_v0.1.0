package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantBondCardApplyDTO;
import com.rxh.anew.dto.MerchantPayOrderDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.shortcut.NewPayOrderService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.BusinessTypeEnum;
import com.rxh.enums.BussTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple5;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
            //根据平台订单号获取绑卡记录,只有绑卡成功才能进行下一步操作
            MerchantCardTable merchantCardTable = newPayOrderService.getMerchantCardInfoByPlatformOrderId(merchantPayOrderDTO.getPlatformOrderId(), BusinessTypeEnum.b6.getBusiType(),ipo);
            //*********************执行平台风控*********************
            //获取商户风控表
            MerchantQuotaRiskTable merchantQuotaRiskTable = newPayOrderService.getMerchantQuotaRiskByMerId(merInfoTable.getMerchantId(),ipo);

             //*********************执行通道风控*********************
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
            respResult = newBondCardService.responseMsg(null != merchantPayOrderDTO ? merchantPayOrderDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
