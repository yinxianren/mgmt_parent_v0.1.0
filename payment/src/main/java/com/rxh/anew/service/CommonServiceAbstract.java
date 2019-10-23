package com.rxh.anew.service;

import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.exception.NewPayException;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.pojo.cross.BankResult;
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
            throw  new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：请求cross工程失败",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }finally {
            return result;
        }
    }


    public BankResult jsonToPojo(String result, InnerPrintLogObject ipo) {
        return null;
    }
}
