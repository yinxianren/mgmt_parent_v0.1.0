package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.PayOrderInfoDBService;
import com.rxh.anew.table.business.PayOrderInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiPayOrderInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午7:44
 * Description:
 */

@AllArgsConstructor
@Service
public class ApiPayOrderInfoServiceImpl implements ApiPayOrderInfoService, NewPayAssert {
    private final  PayOrderInfoDBService payOrderInfoDBService;

    @Override
    public PayOrderInfoTable getOne(PayOrderInfoTable pit) {
        if(isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        return payOrderInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<PayOrderInfoTable> getList(PayOrderInfoTable pit) {
        if(isNull(pit)) return null;
        LambdaQueryWrapper<PayOrderInfoTable> lambdaQueryWrapper = new QueryWrapper<PayOrderInfoTable>() .lambda();
        if( !isNull(pit.getStatus())) lambdaQueryWrapper.eq(PayOrderInfoTable::getStatus,pit.getStatus());
        if( !isBlank(pit.getPlatformOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getPlatformOrderId,pit.getPlatformOrderId());
        if( !isBlank(pit.getMerOrderId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerOrderId,pit.getMerOrderId());
        if( !isBlank(pit.getMerchantId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getMerchantId,pit.getMerchantId());
        if( !isBlank(pit.getTerminalMerId())) lambdaQueryWrapper.eq(PayOrderInfoTable::getTerminalMerId,pit.getTerminalMerId());
        if( !isBlank(pit.getBussType())) lambdaQueryWrapper.eq(PayOrderInfoTable::getBussType,pit.getBussType());
        return payOrderInfoDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.save(pit);
    }

    @Override
    public boolean updateByPrimaryKey(PayOrderInfoTable pit) {
        if(isNull(pit)) return false;
        return payOrderInfoDBService.updateById(pit);
    }
}
