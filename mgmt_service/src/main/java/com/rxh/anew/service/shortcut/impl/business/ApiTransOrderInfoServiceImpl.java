package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.TransOrderInfoDBService;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiTransOrderInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午8:04
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiTransOrderInfoServiceImpl implements ApiTransOrderInfoService, NewPayAssert {

    private final TransOrderInfoDBService transOrderInfoDBService;

    @Override
    public TransOrderInfoTable getOne(TransOrderInfoTable tit) {
        if(isNull(tit)) return null;
        LambdaQueryWrapper<TransOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<TransOrderInfoTable>()
                .lambda();
        if( !isBlank(tit.getMerOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerOrderId,tit.getMerOrderId());
        if( !isBlank(tit.getMerchantId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerchantId,tit.getMerchantId());
        if( !isBlank(tit.getTerminalMerId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getTerminalMerId,tit.getTerminalMerId());
        if( !isBlank(tit.getBusiType()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getBusiType,tit.getBusiType());
        if( !isNull(tit.getStatus()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getStatus,tit.getStatus());
        return transOrderInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<TransOrderInfoTable> getList(TransOrderInfoTable tit) {
        if(isNull(tit)) return null;
        LambdaQueryWrapper<TransOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<TransOrderInfoTable>()
                .lambda();
        if( !isBlank(tit.getMerOrderId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerOrderId,tit.getMerOrderId());
        if( !isBlank(tit.getMerchantId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getMerchantId,tit.getMerchantId());
        if( !isBlank(tit.getTerminalMerId()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getTerminalMerId,tit.getTerminalMerId());
        if( !isBlank(tit.getBusiType()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getBusiType,tit.getBusiType());
        if( !isNull(tit.getStatus()) ) lambdaQueryWrapper.eq(TransOrderInfoTable::getStatus,tit.getStatus());
        return transOrderInfoDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(TransOrderInfoTable tit) {
        if(isNull(tit)) return false;
        return transOrderInfoDBService.save(tit);
    }

    @Override
    public boolean updateById(TransOrderInfoTable tit) {
        if(isNull(tit)) return false;
        return transOrderInfoDBService.updateById(tit);
    }
}
