package com.rxh.service.anew.terminal;

import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;

public interface ApiTerminalMerchantsWalletService {

    TerminalMerchantsWalletTable  getOne(TerminalMerchantsWalletTable tmw);

    boolean updateOrSave(TerminalMerchantsWalletTable tmw);

}
