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
import java.util.List;
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
        MerBondCardApplyDTO mbcaDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        String crossResponseMsg = null;
        MerchantCardTable merchantCardTable = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mbcaDTO = JSON.parse(sotTable.getRequestMsg(), MerBondCardApplyDTO.class);
            sotTable.setMerId(mbcaDTO.getMerId()).setMerOrderId(mbcaDTO.getMerOrderId()).setReturnUrl(mbcaDTO.getReturnUrl()).setNoticeUrl(mbcaDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mbcaDTO.getMerId(),mbcaDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByB4();
            //参数校验
            this.verify(paramRuleMap,mbcaDTO, MerBondCardApplyDTO.class,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newBondCardService.multipleOrder(mbcaDTO.getMerOrderId(),ipo);
            //获取绑卡进件信息
            List<RegisterCollectTable> registerCollectTableList = newBondCardService.getRegCollectBySuccess(mbcaDTO,ipo);
            //获取所有已经绑卡成功的卡信息
            List<MerchantCardTable> merchantCardTableList = newBondCardService.getMerCartInfoBySuccess(mbcaDTO,ipo);
            //排除已经成功绑卡的通道
            registerCollectTableList = newBondCardService.filterRegCollectByBondCardSuccess(registerCollectTableList,merchantCardTableList,ipo);
            //获取等级最高的通道
            ChannelInfoTable channelInfoTable = newBondCardService.getChannelInfoByRegCollect(registerCollectTableList,ipo);
            //根据通道ID筛选出最终的进件附表信息
            RegisterCollectTable registerCollectTable =newBondCardService.filterRegCollectByChannelId(registerCollectTableList,channelInfoTable.getChannelId(),ipo);
            //获取进件主表
            RegisterInfoTable registerInfoTable = newBondCardService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道附属信息
            ChannelExtraInfoTable channelExtraInfoTable = newBondCardService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.BONDCARD.getBussType(),ipo);
            //保存绑卡申请记录
            merchantCardTable = newBondCardService.saveCardInfoByB4(mbcaDTO,channelInfoTable,registerCollectTable,ipo);
            sotTable.setPlatformOrderId(merchantCardTable.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newBondCardService.getRequestCrossMsgDTO(new Tuple5(registerInfoTable,registerCollectTable,channelInfoTable,channelExtraInfoTable,merchantCardTable));
            //请求cross请求
            crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(mbcaDTO.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
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
            if( !isNull(merchantCardTable) )
                newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
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
        MerReGetBondCodeDTO mrgbcDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        MerchantCardTable merchantCardTable =null;
        String crossResponseMsg = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mrgbcDTO = JSON.parse(sotTable.getRequestMsg(), MerReGetBondCodeDTO.class);
            sotTable.setMerId(mrgbcDTO.getMerId()).setReturnUrl(mrgbcDTO.getReturnUrl()).setNoticeUrl(mrgbcDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mrgbcDTO.getMerId(),mrgbcDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByB5();
            //参数校验
            this.verify(paramRuleMap,mrgbcDTO, MerReGetBondCodeDTO.class,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //根据平台订单号获取B4操作记录
            merchantCardTable = newBondCardService.getMerchantCardInfoByPlatformOrderId(mrgbcDTO.getPlatformOrderId(),BusinessTypeEnum.b4.getBusiType(),ipo);
            sotTable.setMerOrderId(merchantCardTable.getMerOrderId());
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
            //请求cross请求
            crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(merchantCardTable.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
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
            if( !isNull(merchantCardTable) &&  merchantCardTable.getBussType().equalsIgnoreCase(BusinessTypeEnum.b5.getBusiType()))
                newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            respResult = newBondCardService.responseMsg(null != merchantCardTable ? merchantCardTable.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
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
        MerConfirmBondCardDTO mcbcDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        MerchantCardTable merchantCardTable = null;
        String crossResponseMsg =null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mcbcDTO = JSON.parse(sotTable.getRequestMsg(), MerConfirmBondCardDTO.class);
            sotTable.setMerId(mcbcDTO.getMerId()).setReturnUrl(mcbcDTO.getReturnUrl()).setNoticeUrl(mcbcDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mcbcDTO.getMerId(),mcbcDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newBondCardService.getOneMerInfo(ipo);
            //更加平台订单号获取B4或B5操作记录
            merchantCardTable = newBondCardService.getMerchantCardInfoByPlatformOrderId(mcbcDTO.getPlatformOrderId(),null,ipo);
            sotTable.setMerOrderId(merchantCardTable.getMerOrderId());
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newBondCardService.getParamMapByB6();
            //参数校验
            this.verify(paramRuleMap,mcbcDTO, MerConfirmBondCardDTO.class,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
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
            //请求cross请求
            crossResponseMsg = newBondCardService.doPostJson(requestCrossMsgDTO,channelExtraInfoTable,ipo);
            //将请求结果转为对象
            crossResponseMsgDTO = newBondCardService.jsonToPojo(crossResponseMsg,ipo);
            //更新进件信息
            newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            //封装放回结果
            respResult = newBondCardService.responseMsg(merchantCardTable.getMerOrderId(),merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,null,null,ipo);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
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
            if( !isNull(merchantCardTable) &&  merchantCardTable.getBussType().equalsIgnoreCase(BusinessTypeEnum.b6.getBusiType()))
                newBondCardService.updateByBondCardInfo(crossResponseMsgDTO,crossResponseMsg,merchantCardTable,ipo);
            respResult = newBondCardService.responseMsg(null != merchantCardTable ? merchantCardTable.getMerOrderId() : null ,merInfoTable,requestCrossMsgDTO,crossResponseMsgDTO,errorCode,errorMsg,ipo);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
