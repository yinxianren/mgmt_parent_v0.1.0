package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.enums.ParamTypeEnum;
import com.rxh.anew.service.shortcut.NewIntoPiecesOfInformationService;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.exception.NewPayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午3:49
 * Description:
 */
@Service
public class NewIntoPiecesOfInformationServiceImp extends CommonServiceAbstract implements NewIntoPiecesOfInformationService {






    @Override
    public Map<String, ParamRule> getParamMapByIPOI() {
        return new HashMap<String, ParamRule>() {
            {
                put("productType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//产品类型
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 32, 64));// 商户订单号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//终端客户名称
                put("userShortName", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 32));//  商户简称
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("phone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));// 手机号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 10));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 10));// 城市
                put("address", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 128));// 详细地址
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 32));// 银行名称
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));// 卡号类型
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 24));//银行卡号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 12));//银行卡持卡人
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11,11));// 银行卡手机号
                put("category", new ParamRule(ParamTypeEnum.STRING.getType(), 2,10));// 经营项目
                put("payFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,5));//
                put("backFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,5));//
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 1024));//签名字符串
            }
        };
    }


    @Override
    public List<ChannelInfoTable> getChannelInfoByMerSetting(List<MerchantSettingTable> list, InnerPrintLogObject ipo) throws NewPayException {
        Set<String>  channelIdSet = list.stream().map(MerchantSettingTable::getChannelId).collect(Collectors.toSet());
        List<ChannelInfoTable>   channelInfoTableList = commonRPCComponent.apiChannelInfoService.batchGetByChannelId(channelIdSet);
        isHasNotElement(channelInfoTableList,
                ResponseCodeEnum.RXH00020.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg()),
                format(" %s",ResponseCodeEnum.RXH00020.getMsg()));
        return channelInfoTableList;
    }

    @Override
    public List<ChannelInfoTable> filtrationChannelInfoByProductType(List<ChannelInfoTable> list, String productType, InnerPrintLogObject ipo) throws NewPayException {

        list.stream().filter(t->t.getProductId().equalsIgnoreCase())

        return null;
    }


}
