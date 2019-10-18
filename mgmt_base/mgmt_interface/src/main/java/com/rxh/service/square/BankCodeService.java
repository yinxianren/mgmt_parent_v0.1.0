package com.rxh.service.square;

import com.rxh.square.pojo.BankCode;

public interface BankCodeService {
    String getBankName(String bankCode);
    String getShortBankName(String bankCode);
    BankCode getBankInfo(String bankCode);
}
