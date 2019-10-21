package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.CommonRPCComponent;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.enums.ParamTypeEnum;
import com.rxh.anew.service.shortcut.NewIntoPiecesOfInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}
