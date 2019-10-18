package com.rxh.service.impl.trading;

import com.rxh.mapper.square.PayProductDetailMapper;
import com.rxh.service.trading.PayProductDetailService;
import com.rxh.square.pojo.PayProductDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : PayProductDetailServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 15:06
 */
@Service
public class PayProductDetailServiceImpl implements PayProductDetailService {
    @Resource
    private PayProductDetailMapper payProductDetailMapper;

    @Override
    public PayProductDetail selectByPrimaryKey(String payId) {

        return payProductDetailMapper.selectByPrimaryKey(payId);
    }
}
