package com.rxh.service.impl;

import com.rxh.mapper.square.TerminalMerchantsWalletMapper;
import com.rxh.service.square.TerminalMerchantsWalletService;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TerminalMerchantsWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerminalMerchantsWalletServiceImpl implements TerminalMerchantsWalletService{

    @Autowired
    private TerminalMerchantsWalletMapper terminalMerchantsWalletMapper;

    @Override
    public TerminalMerchantsWallet search(String merId, String terminalMerId) {
        return terminalMerchantsWalletMapper.search(merId,terminalMerId);
    }

    @Override
    public int update(TerminalMerchantsWallet record) {
        return terminalMerchantsWalletMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insert(TerminalMerchantsWallet record) {
        return terminalMerchantsWalletMapper.insert(record);
    }


    @Override
    public List<TerminalMerchantsWallet> getWalletByParam(TerminalMerchantsWallet terminalMerchantsWallet) {
        return terminalMerchantsWalletMapper.getWalletByParam(terminalMerchantsWallet);
    }

    @Override
    public List<TerminalMerchantsWallet> getIds(String merId) {
        return terminalMerchantsWalletMapper.getIds(merId);
    }
    // @Override
    // public List<TerminalMerchantsWallet> getIdsAndName() {
    //     return terminalMerchantsWalletMapper.selectAllIdAndName();
    // }

    @Override
    @Transactional
    public int updateByPrimaryKeySelective(TerminalMerchantsWallet record){
        return terminalMerchantsWalletMapper.updateByPrimaryKeySelective(record);
    }
}
