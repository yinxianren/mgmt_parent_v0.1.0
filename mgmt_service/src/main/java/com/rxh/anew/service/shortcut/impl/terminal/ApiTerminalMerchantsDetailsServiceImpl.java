package com.rxh.anew.service.shortcut.impl.terminal;

import com.rxh.anew.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 上午9:28
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiTerminalMerchantsDetailsServiceImpl implements ApiTerminalMerchantsDetailsService, NewPayAssert {

    private final TerminalMerchantsDetailsDBService  terminalMerchantsDetailsDBService;

    @Override
    public boolean save(TerminalMerchantsDetailsTable tmd) {
        if(isNull(tmd)) return false;
        return terminalMerchantsDetailsDBService.save(tmd);
    }
}
