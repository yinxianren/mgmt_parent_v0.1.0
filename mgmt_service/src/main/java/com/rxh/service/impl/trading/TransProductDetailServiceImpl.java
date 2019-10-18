package com.rxh.service.impl.trading;

import com.rxh.mapper.square.TransProductDetailMapper;
import com.rxh.service.trading.TransProductDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : TransProductDetailServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 16:34
 */
@Service
public class TransProductDetailServiceImpl implements TransProductDetailService {

    @Resource
    private TransProductDetailMapper transProductDetailMapper;

    @Override
    public com.rxh.square.pojo.TransProductDetail selectByPrimaryKey(String transId) {
        return transProductDetailMapper.selectByPrimaryKey(transId);
    }
}
