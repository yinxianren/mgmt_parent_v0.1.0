package com.rxh.service;

import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.page.Page;
import com.rxh.vo.ResponseVO;

import java.util.List;

public interface AnewTerminalMerchantsWalletService {

    ResponseVO search(TerminalMerchantsWalletTable terminalMerchantsWalletTable);

    ResponseVO pageByDetail(Page page);

    List<TerminalMerchantsDetailsTable> listByTerminalId();
}
