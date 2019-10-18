package com.rxh.mapper.square;

import com.rxh.square.pojo.BankCode;

public interface BankCodeServiceMapper {
    String getBankName(String bankCode);
    String getShortBankName(String bankCode);
    BankCode getBankInfo(String bankCode);
}
