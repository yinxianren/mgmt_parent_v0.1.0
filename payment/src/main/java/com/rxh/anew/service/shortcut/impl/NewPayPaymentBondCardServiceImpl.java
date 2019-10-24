package com.rxh.anew.service.shortcut.impl;

import com.rxh.anew.inner.InnerPrintLogObject;
import com.rxh.anew.inner.ParamRule;
import com.rxh.anew.service.CommonServiceAbstract;
import com.rxh.anew.service.shortcut.NewPayPaymentBondCardService;
import com.rxh.enums.ParamTypeEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午9:52
 * Description:
 */
@Service
public class NewPayPaymentBondCardServiceImpl extends CommonServiceAbstract implements NewPayPaymentBondCardService {















    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) {

    }


    @Override
    public Map<String, ParamRule> getParamMapByBC() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 商户订单号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 125));//签名字符串
            }
        };
    }


}
