package com.rxh.service.impl.trading;

import com.rxh.mapper.square.PayCardholderInfoMapper;
import com.rxh.service.trading.PayCardholderInfoService;
import com.rxh.square.pojo.PayCardholderInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : PayCardholderInfoServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 15:43
 */
@Service
public class PayCardholderInfoServiceImpl implements PayCardholderInfoService {

    @Resource
    private PayCardholderInfoMapper payCardholderInfoMapper;

    @Override
    public PayCardholderInfo selectByPrimaryKey(String payId) {
        return payCardholderInfoMapper.selectByPrimaryKey(payId);
    }
}
