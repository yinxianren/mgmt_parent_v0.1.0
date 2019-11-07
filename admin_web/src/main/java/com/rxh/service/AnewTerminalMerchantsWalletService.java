package com.rxh.service;

import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.pojo.base.Page;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewTerminalMerchantsWalletService {

    ResponseVO search(TerminalMerchantsWalletTable terminalMerchantsWalletTable);

    ResponseVO pageByDetail(Page page);

    List<TerminalMerchantsDetailsTable> listByTerminalId();
}
