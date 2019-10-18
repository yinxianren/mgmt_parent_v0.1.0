package com.rxh.pojo.merchant;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/6/21
 * Time: 9:31
 * Project: Management
 * Package: com.rxh.pojo.merchant
 * 商户相关信息
 */
public class Merchant implements Serializable {
    // 商户信息
    private MerchantInfo merchantInfo;
    // 商户设置
    private MerchantSetting merchantSetting;
    // 商户支付信息
    private MerchantPay merchantPay;

    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

    public MerchantSetting getMerchantSetting() {
        return merchantSetting;
    }

    public void setMerchantSetting(MerchantSetting merchantSetting) {
        this.merchantSetting = merchantSetting;
    }

    public MerchantPay getMerchantPay() {
        return merchantPay;
    }

    public void setMerchantPay(MerchantPay merchantPay) {
        this.merchantPay = merchantPay;
    }
}
