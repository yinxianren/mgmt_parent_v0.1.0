package com.rxh.anew.service;

import com.alibaba.fastjson.JSON;
import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.dto.ResponseEntity;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.OrganizationInfoTable;
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
import java.util.Date;
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
        List<MerchantSettingTable> list =null;
        try {
            list = commonRPCComponent.apiMerchantSettingService.getList(
                    new MerchantSettingTable().setMerchantId(ipo.getMerId())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取商户通道配置信息发生异常,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(list,
                ResponseCodeEnum.RXH00019.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00019.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00019.getMsg()));
        return list;
    }


    public OrganizationInfoTable getOrganizationInfo(String organizationId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="OrganizationInfoTable";
        OrganizationInfoTable organizationInfoTable = null;
        try{
            organizationInfoTable = commonRPCComponent.apiOrganizationInfoService.getOne(new OrganizationInfoTable().setOrganizationId(organizationId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取组织机构信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(organizationInfoTable,
                ResponseCodeEnum.RXH99996.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：根据机构ID(%s),获取组织信息为null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH99996.getMsg(),localPoint,organizationId),
                format(" %s",ResponseCodeEnum.RXH99996.getMsg()));


        isNull(organizationInfoTable.getApplicationClassObj(),
                ResponseCodeEnum.RXH99996.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：组织机构中有字段未配置：ApplicationClassObj==null",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), ResponseCodeEnum.RXH99996.getMsg(),localPoint,organizationId),
                format(" %s",ResponseCodeEnum.RXH99996.getMsg()));

        return organizationInfoTable;
    }

    public MerchantInfoTable getOneMerInfo(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getOneMerInfo";
        MerchantInfoTable merchantInfoTable =  null;
        try {
            merchantInfoTable = commonRPCComponent.apiMerchantInfoService.getOne(new MerchantInfoTable().setMerchantId(ipo.getMerId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取商户信息发生异常,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(merchantInfoTable,
                ResponseCodeEnum.RXH00017.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00017.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00017.getMsg()));
        return merchantInfoTable;
    }



    public String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelExtraInfoTable extraInfoTable, InnerPrintLogObject ipo)";
        String result = null;
        try {

//            result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), extraInfoTable.getRequestUrl(), JsonUtils.objectToJsonNonNull(requestCrossMsgDTO));

            //测试模块

            {
                CrossResponseMsgDTO crm = new CrossResponseMsgDTO();
                crm.setCrossStatusCode(StatusEnum._0.getStatus());
                crm.setCrossResponseMsg(StatusEnum._0.getRemark());
                crm.setChannelOrderId("ORDER_ID-"+System.currentTimeMillis());
                crm.setChannelResponseTime(new Date());
                crm.setChannelResponseMsg(StatusEnum._0.getRemark());
                result = JSON.toJSONString(crm);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：请求cross工程失败,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return result;
    }


    public  String doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="doPostJson(RequestCrossMsgDTO requestCrossMsgDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo)";
        String result = null;
        try {
//            result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), channelInfoTable.getRequestUrl(), JsonUtils.objectToJsonNonNull(requestCrossMsgDTO));
            //测试模块
            {
                CrossResponseMsgDTO crm = new CrossResponseMsgDTO();
                crm.setCrossStatusCode(StatusEnum._0.getStatus());
                crm.setCrossResponseMsg(StatusEnum._0.getRemark());
                crm.setChannelOrderId("ORDER_ID-"+System.currentTimeMillis());
                crm.setChannelResponseTime(new Date());
                crm.setChannelResponseMsg(StatusEnum._0.getRemark());
                result = JSON.toJSONString(crm);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：请求cross工程失败,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return result;
    }


    public CrossResponseMsgDTO jsonToPojo(String crossResponseMsg, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="jsonToPojo";
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        try {
            crossResponseMsgDTO = JsonUtils.jsonToPojo(crossResponseMsg, CrossResponseMsgDTO.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：将cross返回结果转CrossResponseMsgDTO对象发生异常，异常信息：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(crossResponseMsgDTO,
                ResponseCodeEnum.RXH99997.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：将cross返回结果转CrossResponseMsgDTO对象为null ",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99997.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH99997.getMsg()));
        return crossResponseMsgDTO;
    }


    public boolean saveSysLog(SystemOrderTrackTable systemOrderTrackTable){
        boolean how=false;
        try{
            how =  commonRPCComponent.apiSystemOrderTrackService.save(systemOrderTrackTable);
        } catch (Exception e){
            e.printStackTrace();
        }
        return how;
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
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息发生异常，异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );

        }
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
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
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00026.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00026.getMsg())
        );
        return channelExtraInfoTable;
    }


    public String responseMsg(MerchantInfoTable merInfoTable,InnerPrintLogObject ipo,CrossResponseMsgDTO crossResponseMsgDTO,String ...args) throws NewPayException, IllegalAccessException {
        final String localPoint="responseMsg";
        String responseMsg = null;
        try {
            ResponseEntity responseEntity = new ResponseEntity()
                    .setMerId( null !=merInfoTable ? merInfoTable.getMerchantId() : null)
                    .setStatus( null != crossResponseMsgDTO ? crossResponseMsgDTO.getCrossStatusCode() :  StatusEnum._1.getStatus() )
                    .setMsg( null != crossResponseMsgDTO ? crossResponseMsgDTO.getCrossStatusMsg() : StatusEnum._1.getRemark())
                    .setMerOrderId( args[0] )
                    .setPlatformOrderId( args[1] )
                    .setAmount(args[2])
                    .setErrorCode(args[3])
                    .setErrorMsg(args[4]);

            if(args.length == 6)//支付成功才传通道标识
                responseEntity.setChannelTab(args[5]);

            Field[] fields = responseEntity.getClass().getDeclaredFields();
            PayTreeMap<String,Object> map = new PayTreeMap<>();
            for (Field field : fields) {
                field.setAccessible(true);
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
        }
        return responseMsg;
    }

    public void isSuccess(CrossResponseMsgDTO crossResponseMsgDTO, InnerPrintLogObject ipo) throws NewPayException {
        state(crossResponseMsgDTO.getCrossStatusCode() != StatusEnum._0.getStatus(),
                crossResponseMsgDTO.getErrorCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(), crossResponseMsgDTO.getErrorMsg()),
                crossResponseMsgDTO.getErrorMsg());
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
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00027.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00027.getMsg()));

        return  registerInfoTable;
    }


    public MerchantCardTable getMerchantCardInfoByPlatformOrderId(String platformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerchantCardInfoByPlatformOrderId";
        MerchantCardTable merchantCardTable=null;
        try {
            merchantCardTable = commonRPCComponent.apiMerchantCardService.getOne(new MerchantCardTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setPlatformOrderId(platformOrderId)
                    .setBussType(busiType)
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据平台流水号获取绑卡申请记录发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(merchantCardTable,
                ResponseCodeEnum.RXH00032.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00032.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00032.getMsg()));
        return merchantCardTable;
    }

    public RegisterCollectTable getRegCollectInfo(String regPlatformOrderId, String busiType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegCollectInfo";
        RegisterCollectTable rct = null;
        try{
            rct = commonRPCComponent.apiRegisterCollectService.getOne(new RegisterCollectTable()
                    .setPlatformOrderId(regPlatformOrderId)
                    .setBussType(busiType)
                    .setStatus(StatusEnum._0.getStatus())
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取成功进件信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(rct,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));

        return rct;
    }
}
