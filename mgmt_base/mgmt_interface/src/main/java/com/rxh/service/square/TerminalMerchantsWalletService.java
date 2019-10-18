package com.rxh.service.square;

import com.rxh.square.pojo.TerminalMerchantsWallet;

import java.util.List;

public interface TerminalMerchantsWalletService {
    TerminalMerchantsWallet search(String merId,String terminalMerId);

    int update(TerminalMerchantsWallet record);

    int insert(TerminalMerchantsWallet record);

    List<TerminalMerchantsWallet> getWalletByParam(TerminalMerchantsWallet terminalMerchantsWallet);

    List<TerminalMerchantsWallet> getIds(String merId);

    int updateByPrimaryKeySelective(TerminalMerchantsWallet record);
    // List<TerminalMerchantsWallet> getIdsAndName();
}
