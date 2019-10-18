package com.rxh.service.merchant;

import com.rxh.pojo.merchant.MerchantPay;
import com.rxh.service.base.BaseService;

public interface MerchantPayService extends BaseService<MerchantPay, Integer> {

    MerchantPay getMerchantPayByMerId(Integer merId);

}