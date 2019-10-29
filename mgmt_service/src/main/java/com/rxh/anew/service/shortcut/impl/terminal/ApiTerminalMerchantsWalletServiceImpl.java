package com.rxh.anew.service.shortcut.impl.terminal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.rxh.anew.service.db.terminal.TerminalMerchantsWalletDBservice;
import com.rxh.anew.table.terminal.TerminalMerchantsWalletTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/29
 * Time: 上午9:21
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiTerminalMerchantsWalletServiceImpl implements ApiTerminalMerchantsWalletService , NewPayAssert {

    private final TerminalMerchantsWalletDBservice terminalMerchantsWalletDBservice;

    @Override
    public TerminalMerchantsWalletTable getOne(TerminalMerchantsWalletTable tmw) {
        if(isNull(tmw)) return null;
        LambdaQueryWrapper<TerminalMerchantsWalletTable> lambdaQueryWrapper =new QueryWrapper<TerminalMerchantsWalletTable>().lambda();
        if( !isBlank(tmw.getMerchantId()))  lambdaQueryWrapper.eq(TerminalMerchantsWalletTable::getMerchantId,tmw.getMerchantId());
        if( !isNull(tmw.getStatus()))  lambdaQueryWrapper.eq(TerminalMerchantsWalletTable::getStatus,tmw.getStatus());
        return terminalMerchantsWalletDBservice.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean updateOrSave(TerminalMerchantsWalletTable tmw) {
        if(isNull(tmw)) return false;
        return terminalMerchantsWalletDBservice.saveOrUpdate(tmw);
    }
}
