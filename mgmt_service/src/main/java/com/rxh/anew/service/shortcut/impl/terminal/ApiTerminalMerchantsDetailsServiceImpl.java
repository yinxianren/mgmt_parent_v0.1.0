package com.rxh.anew.service.shortcut.impl.terminal;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.terminal.TerminalMerchantsDetailsDBService;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.anew.table.terminal.TerminalMerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.terminal.ApiTerminalMerchantsDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public IPage page(TerminalMerchantsDetailsTable terminalMerchantsDetailsTable) {
        if (isNull(terminalMerchantsDetailsTable)) return new Page();
        LambdaQueryWrapper<TerminalMerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper();
        IPage iPage = new Page(terminalMerchantsDetailsTable.getPageNum(),terminalMerchantsDetailsTable.getPageSize());
        if (!isBlank(terminalMerchantsDetailsTable.getTerminalMerId())) queryWrapper.eq(TerminalMerchantsDetailsTable::getTerminalMerId,terminalMerchantsDetailsTable.getTerminalMerId());
        if (!isBlank(terminalMerchantsDetailsTable.getMerchantId())) queryWrapper.eq(TerminalMerchantsDetailsTable::getMerchantId,terminalMerchantsDetailsTable.getMerchantId());
        if (!isBlank(terminalMerchantsDetailsTable.getMerOrderId())) queryWrapper.eq(TerminalMerchantsDetailsTable::getMerOrderId,terminalMerchantsDetailsTable.getMerOrderId());
        if (!isBlank(terminalMerchantsDetailsTable.getPlatformOrderId())) queryWrapper.eq(TerminalMerchantsDetailsTable::getPlatformOrderId,terminalMerchantsDetailsTable.getPlatformOrderId());
        if (!isBlank(terminalMerchantsDetailsTable.getProductId())) queryWrapper.eq(TerminalMerchantsDetailsTable::getProductId,terminalMerchantsDetailsTable.getProductId());
        if (!isNull(terminalMerchantsDetailsTable.getBeginTime())) queryWrapper.gt(TerminalMerchantsDetailsTable::getUpdateTime,terminalMerchantsDetailsTable.getBeginTime());
        if (!isNull(terminalMerchantsDetailsTable.getEndTime())) queryWrapper.lt(TerminalMerchantsDetailsTable::getUpdateTime,terminalMerchantsDetailsTable.getEndTime());
        return terminalMerchantsDetailsDBService.page(iPage,queryWrapper);
    }

    @Override
    public List<TerminalMerchantsDetailsTable> listGroupByTerminalId() {
        LambdaQueryWrapper<TerminalMerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.groupBy(TerminalMerchantsDetailsTable::getTerminalMerId);
        return terminalMerchantsDetailsDBService.list(queryWrapper);
    }
}
