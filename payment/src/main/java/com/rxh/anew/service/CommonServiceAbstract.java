package com.rxh.anew.service;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.dto.ResponseEntity;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.NewPayException;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.PayTreeMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午9:15
 * Description:
 */

public abstract class CommonServiceAbstract implements NewPayAssert, PayUtil {

    @Autowired
    protected CommonRPCComponent commonRPCComponent;
    @Autowired
    protected Md5Component md5Component;



    public List<MerchantSettingTable> getMerchantSetting(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerchantSetting";
        List<MerchantSettingTable> list = commonRPCComponent.apiMerchantSettingService.getList(
                new MerchantSettingTable().setMerchantId(ipo.getMerId())
        );
        isHasNotElement(list,
                ResponseCodeEnum.RXH00019.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00019.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00019.getMsg()));
        return list;
    }


    public MerchantInfoTable getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getOneMerInfo";
        MerchantInfoTable merchantInfoTable = new MerchantInfoTable();
        merchantInfoTable.setMerchantId(ipo.getMerId());
        merchantInfoTable = commonRPCComponent.apiMerchantInfoService.getOne(merchantInfoTable);
        isNull(merchantInfoTable,
                ResponseCodeEnum.RXH00017.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00017.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00017.getMsg()));
        return merchantInfoTable;
    }



    public String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo) {
        final String localPoint="doPostJson";
        String result = null;
        try {
            result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraInfoTable.getRequestUrl(), JsonUtils.objectToJsonNonNull(requestCrossMsgDTO));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：请求cross工程失败,异常信息：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return result;
        }
    }


    public CrossResponseMsgDTO jsonToPojo(String crossResponseMsg, InnerPrintLogObject ipo) {
        final String localPoint="jsonToPojo";
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        try {
            crossResponseMsgDTO = JsonUtils.jsonToPojo(crossResponseMsg, CrossResponseMsgDTO.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：将cross返回结果转BankResult对象发生异常，异常信息：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return crossResponseMsgDTO;
        }
    }


    public boolean saveSysLog(SystemOrderTrackTable systemOrderTrackTable){
        return commonRPCComponent.apiSystemOrderTrackService.save(systemOrderTrackTable);
    }

    public ChannelInfoTable getChannelInfoByChannelId(String channelId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByChannelId";
        ChannelInfoTable channelInfoTable =null;
        try{
            channelInfoTable = commonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息发生异常，异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );

        }
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg())
        );
        return channelInfoTable;

    }


    public ChannelExtraInfoTable getChannelExtraInfoByOrgId(String organizationId, String bussType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelExtraInfoByOrgId";
        ChannelExtraInfoTable channelExtraInfoTable = null;
        try{
            channelExtraInfoTable = commonRPCComponent.apiChannelExtraInfoService.getOne(new ChannelExtraInfoTable()
                    .setOrganizationId(organizationId)
                    .setBussType(bussType));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道组织ID获取附属进件通道信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(channelExtraInfoTable,
                ResponseCodeEnum.RXH00026.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00026.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00026.getMsg())
        );
        return channelExtraInfoTable;
    }



    public String responseMsg(String merOrderId,MerchantInfoTable merInfoTable, RequestCrossMsgDTO  requestCrossMsgDTO, CrossResponseMsgDTO crossResponseMsgDTO,String errorCode,String errorMsg,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="responseMsg";
        String responseMsg = null;
        try {
            ResponseEntity responseEntity = new ResponseEntity()
                    .setMerId( null !=merInfoTable ? merInfoTable.getMerchantId() : null)
                    .setStatus( null != crossResponseMsgDTO ? crossResponseMsgDTO.getCrossStatusCode() :  StatusEnum._1.getStatus() )
                    .setMsg( null != crossResponseMsgDTO ? StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()) : StatusEnum._1.getRemark())
                    .setMerOrderId( null != merOrderId ? merOrderId : null )
                    .setPlatformOrderId( null != requestCrossMsgDTO ? requestCrossMsgDTO.getRegisterCollectTable().getPlatformOrderId() : null)
                    .setErrorCode(errorCode)
                    .setErrorMsg(errorMsg)
                    ;

            Field[] fields = responseEntity.getClass().getDeclaredFields();
            PayTreeMap<String,Object> map = new PayTreeMap<>();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object object = field.get(responseEntity);
                if(null != object) map.lput(fieldName,object);
            }
            map.lput("signMsg", null != merInfoTable ? md5Component.getMd5SignWithKey(map,merInfoTable.getSecretKey()) : "" );
            responseMsg = JSON.toJSONString(map);
        }catch (Exception e){
            e.printStackTrace();
            if(null != ipo) {
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：封装响应报文发生异常，异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            } else
                throw  e;
        }finally {
            return  null == responseMsg ? "系统内部错误！" : responseMsg;
        }
    }


    public RegisterInfoTable getRegisterInfoTable(Long ritId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveOnRegisterInfo";
        RegisterInfoTable registerInfoTable = null;
        try{
            registerInfoTable = commonRPCComponent.apiRegisterInfoService.getOne(new RegisterInfoTable().setId(ritId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B3：%s,异常根源：获取进件信息主表信息发生异常，异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(registerInfoTable,
                ResponseCodeEnum.RXH00027.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00027.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00027.getMsg()));

        return  registerInfoTable;
    }
}
