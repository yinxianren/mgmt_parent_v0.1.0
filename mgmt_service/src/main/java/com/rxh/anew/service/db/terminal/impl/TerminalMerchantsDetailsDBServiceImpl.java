package com.rxh.anew.service.db.terminal.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.mapper.anew.terminal.AnewTerminalMerchantDetailsMapper;
import org.springframework.stereotype.Service;

@Service
public class TerminalMerchantsDetailsDBServiceImpl extends ServiceImpl<AnewTerminalMerchantDetailsMapper, TerminalMerchantsDetailsTable> implements TerminalMerchantsDetailsDBService {
}
