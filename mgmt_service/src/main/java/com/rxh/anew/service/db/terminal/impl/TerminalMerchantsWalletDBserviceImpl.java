package com.rxh.anew.service.db.terminal.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.terminal.TerminalMerchantsWalletDBservice;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.mapper.anew.terminal.AnewTerminalMerchantsWalletMapper;
import org.springframework.stereotype.Service;

@Service
public class TerminalMerchantsWalletDBserviceImpl extends ServiceImpl<AnewTerminalMerchantsWalletMapper, TerminalMerchantsWalletTable> implements TerminalMerchantsWalletDBservice {
}
