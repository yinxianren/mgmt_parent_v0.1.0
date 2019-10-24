package com.rxh.anew.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.controller.NewAbstractCommonController;
import com.rxh.anew.dto.*;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.shortcut.NewBondCardService;
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
 * Date: 2019/10/23
 * Time: 下午9:51
 * Description:
 */
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewBondCardController extends NewAbstractCommonController {

    private  final  Md5Component md5Component;
    private final NewBondCardService newBondCardService;


    /**
     *  绑卡申请接口
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/bondCardApply", produces = "text/html;charset=UTF-8")
    public String bondCardApply(HttpServletRequest request, @RequestBody(required = false) String param){
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
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByBC();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mbcaDTO.getMerId(),mbcaDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,mbcaDTO,MerchantBondCardApplyDTO.class,ipo);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newBondCardService.multipleOrder(mbcaDTO.getMerOrderId(),ipo);
            //获取进件成功的附属表
//            RegisterCollectTable registerCollectTable = newPayPaymentBondCardService.getSuccessRegisterCollectInfo(mbcaDTO,ipo);
            //根据平台流水号获取进件成功的附属表
            RegisterCollectTable registerCollectTable = newBondCardService.getRegisterInfoTableByPlatformOrderId(mbcaDTO.getPlatformOrderId(),ipo);
            //获取进件主表
            RegisterInfoTable registerInfoTable = newBondCardService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newBondCardService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
            //获取通道附属信息
            ChannelExtraInfoTable channelExtraInfoTable = newBondCardService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
            //保存绑卡申请记录
            MerchantCardTable merchantCardTable = newBondCardService.saveCardInfoByB4(mbcaDTO,channelInfoTable,registerCollectTable,ipo);
            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newBondCardService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
            //发生cross请求
            String crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(mbcaDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
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
            respResult = newBondCardService.responseMsg(null != mbcaDTO ? mbcaDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }

    /**
     *  重新获取绑卡验证码
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value = "/reGetBondCode", produces = "text/html;charset=UTF-8")
    public String reGetBondCode(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【重新获取绑卡验证码】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantReGetBondCodeDTO mrgbcDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mrgbcDTO = JSON.parse(sotTable.getRequestMsg(),MerchantReGetBondCodeDTO.class);
            sotTable.setMerId(mrgbcDTO.getMerId()).setMerOrderId(mrgbcDTO.getMerOrderId()).setReturnUrl(mrgbcDTO.getReturnUrl()).setNoticeUrl(mrgbcDTO.getNoticeUrl());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByMRGBC();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mrgbcDTO.getMerId(),mrgbcDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,mrgbcDTO,MerchantReGetBondCodeDTO.class,ipo);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newBondCardService.multipleOrder(mrgbcDTO.getMerOrderId(),ipo);
            //更加平台订单号获取B4操作记录
            MerchantCardTable merchantCardTable = newBondCardService.getMerchantCardInfoByPlatformOrderId(mrgbcDTO.getPlatformOrderId(),BusinessTypeEnum.b4.getBusiType(),ipo);
            //获取进件成功的附属表
            RegisterCollectTable registerCollectTable = newBondCardService.getRegisterInfoTableByPlatformOrderId(merchantCardTable.getRegisterCollectPlatformOrderId(),ipo);
            //获取进件主表
            RegisterInfoTable registerInfoTable = newBondCardService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newBondCardService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
            //获取通道附属信息
            ChannelExtraInfoTable channelExtraInfoTable = newBondCardService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
            //保存绑卡申请记录
            merchantCardTable = newBondCardService.saveCardInfoByB5(merchantCardTable,mrgbcDTO,ipo);
            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newBondCardService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
            //发生cross请求
            String crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(mrgbcDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
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
            respResult = newBondCardService.responseMsg(null != mrgbcDTO ? mrgbcDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }


    /**
     * 确定绑定银行卡
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/confirmBondCard", produces = "text/html;charset=UTF-8")
    public String confirmBondCard(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【确定绑定银行卡】";
        String errorMsg = null,errorCode = null,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerchantConfirmBondCardDTO mcbcDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mcbcDTO = JSON.parse(sotTable.getRequestMsg(),MerchantConfirmBondCardDTO.class);
            sotTable.setMerId(mcbcDTO.getMerId()).setMerOrderId(mcbcDTO.getMerOrderId()).setReturnUrl(mcbcDTO.getReturnUrl()).setNoticeUrl(mcbcDTO.getNoticeUrl());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByCBC();
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mcbcDTO.getMerId(),mcbcDTO.getMerOrderId(),bussType);
            //参数校验
            this.verify(paramRuleMap,mcbcDTO,MerchantConfirmBondCardDTO.class,ipo);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newBondCardService.multipleOrder(mcbcDTO.getMerOrderId(),ipo);
            //更加平台订单号获取B4或B5操作记录
            MerchantCardTable merchantCardTable = newBondCardService.getMerchantCardInfoByPlatformOrderId(mcbcDTO.getPlatformOrderId(),null,ipo);
            //获取进件成功的附属表
            RegisterCollectTable registerCollectTable = newBondCardService.getRegisterInfoTableByPlatformOrderId(merchantCardTable.getRegisterCollectPlatformOrderId(),ipo);
            //获取进件主表
            RegisterInfoTable registerInfoTable = newBondCardService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newBondCardService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
            //获取通道附属信息
            ChannelExtraInfoTable channelExtraInfoTable = newBondCardService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
            //保存绑卡申请记录
            merchantCardTable = newBondCardService.saveCardInfoByB6(merchantCardTable,mcbcDTO,ipo);
            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newBondCardService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
            //发生cross请求
            String crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(mcbcDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
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
            respResult = newBondCardService.responseMsg(null != mcbcDTO ? mcbcDTO.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
