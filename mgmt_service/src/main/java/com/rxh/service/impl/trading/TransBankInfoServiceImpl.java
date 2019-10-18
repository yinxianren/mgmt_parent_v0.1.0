package com.rxh.service.impl.trading;

import com.rxh.mapper.square.TransBankInfoMapper;
import com.rxh.service.trading.TransBankInfoService;
import com.rxh.square.pojo.TransBankInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName : TransBankInfoServiceImpl
 * @Author : zoe
 * @Date : 2019/5/19 16:22
 */
@Service
public class TransBankInfoServiceImpl implements TransBankInfoService {

    @Resource
    private TransBankInfoMapper transBankInfoMapper;

    @Override
    public TransBankInfo selectByPrimaryKey(String transId) {
        return transBankInfoMapper.selectByPrimaryKey(transId);
    }
}
