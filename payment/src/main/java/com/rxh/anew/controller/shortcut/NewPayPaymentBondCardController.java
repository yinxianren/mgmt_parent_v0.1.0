package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.MerchantBasicInformationRegistrationDTO;
import com.rxh.anew.dto.MerchantBondCardApplyDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.shortcut.NewPayPaymentBondCardService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.BusinessTypeEnum;
import com.rxh.enums.BussTypeEnum;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.tuple.Tuple4;
import com.rxh.tuple.Tuple5;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午9:51
 * Description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewPayPaymentBondCardController  extends NewAbstractCommonController {

    private  final  Md5Component md5Component;
    private final   NewPayPaymentBondCardService newPayPaymentBondCardService;


    /**
     *  绑卡申请接口
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/bondCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String bondCard(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【绑卡申请接口】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantBondCardApplyDTO mbcaDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mbcaDTO = JSON.parse(sotTable.getRequestMsg(),MerchantBondCardApplyDTO.class);
            sotTable.setMerId(mbcaDTO.getMerId()).setMerOrderId(mbcaDTO.getMerOrderId()).setReturnUrl(mbcaDTO.getReturnUrl()).setNoticeUrl(mbcaDTO.getNoticeUrl());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap =newPayPaymentBondCardService.getParamMapByBC();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mbcaDTO.getMerId(),mbcaDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,mbcaDTO,MerchantBasicInformationRegistrationDTO.class,ipo);
            //获取商户信息
            merInfoTable = newPayPaymentBondCardService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newPayPaymentBondCardService.multipleOrder(mbcaDTO.getMerOrderId(),ipo);
            //获取进件成功的附属表
            RegisterCollectTable registerCollectTable = newPayPaymentBondCardService.getSuccessRegisterCollectInfo(mbcaDTO,ipo);
            //获取进件主表
            RegisterInfoTable registerInfoTable = newPayPaymentBondCardService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newPayPaymentBondCardService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
            //获取通道附属信息
            ChannelExtraInfoTable channelExtraInfoTable = newPayPaymentBondCardService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
            //保存绑卡申请记录
            MerchantCardTable merchantCardTable = newPayPaymentBondCardService.saveCardInfo(mbcaDTO,channelInfoTable,ipo);
            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayPaymentBondCardService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
            //发生cross请求
            String crossResponseMsg = newPayPaymentBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newPayPaymentBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newPayPaymentBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newPayPaymentBondCardService.responseMsg(mbcaDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
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
            respResult = newPayPaymentBondCardService.responseMsg(null != mbcaDTO ? mbcaDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayPaymentBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }







}
