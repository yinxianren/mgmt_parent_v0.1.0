package com.rxh.service.anew.terminal;

import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;

import java.util.List;

public interface ApiTerminalMerchantsWalletService {

    TerminalMerchantsWalletTable  getOne(TerminalMerchantsWalletTable tmw);

    boolean updateOrSave(TerminalMerchantsWalletTable tmw);

    List<TerminalMerchantsWalletTable> getList(TerminalMerchantsWalletTable terminalMerchantsWalletTable);

}
