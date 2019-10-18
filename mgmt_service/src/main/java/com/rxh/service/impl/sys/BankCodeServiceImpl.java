package com.rxh.service.impl.sys;

import com.rxh.mapper.square.BankCodeServiceMapper;
import com.rxh.service.square.BankCodeService;
import com.rxh.square.pojo.BankCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BankCodeServiceImpl implements BankCodeService {
    @Resource
    private BankCodeServiceMapper bankCodeServiceMapper;

    @Override
    public String getBankName(String bankCode) {
        return bankCodeServiceMapper.getBankName(bankCode);
    }

    @Override
    public String getShortBankName(String bankCode) {
        return bankCodeServiceMapper.getShortBankName(bankCode);
    }

    @Override
    public BankCode getBankInfo(String bankCode) {
        return bankCodeServiceMapper.getBankInfo(bankCode);
    }
}
