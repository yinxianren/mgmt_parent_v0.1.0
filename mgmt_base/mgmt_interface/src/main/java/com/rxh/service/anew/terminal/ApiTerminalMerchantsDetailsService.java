package com.rxh.service.anew.terminal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;

import java.util.List;

public interface ApiTerminalMerchantsDetailsService {

    boolean save(TerminalMerchantsDetailsTable tmd);

    IPage page(TerminalMerchantsDetailsTable terminalMerchantsDetailsTable);

    List<TerminalMerchantsDetailsTable> listGroupByTerminalId();

}
