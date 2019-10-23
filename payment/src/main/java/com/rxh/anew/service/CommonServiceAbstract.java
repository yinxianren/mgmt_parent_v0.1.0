package com.rxh.anew.service;

import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.component.Md5Component;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.exception.NewPayException;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    public boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException{
        final String localPoint="multipleOrder";
        RegisterCollectTable rct = new RegisterCollectTable();
        rct.setMerchantId(ipo.getMerId());
        rct.setTerminalMerId(ipo.getTerMerId());
        rct.setMerOrderId(merOrderId);
        rct = commonRPCComponent.apiRegisterCollectService.getOne(rct);
        isNotNull(rct,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
        return false;
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
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：请求cross工程失败",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
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
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：将cross返回结果转BankResult对象",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
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
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道ID获取通道信息", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint),
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
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据通道组织ID获取附属进件通道信息", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint),
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

}
