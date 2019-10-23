package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.RegisterInfoDBService;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiRegisterInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午9:37
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiRegisterInfoServiceImpl implements ApiRegisterInfoService, NewPayAssert {

    private final RegisterInfoDBService registerInfoDBService;


    @Override
    public RegisterInfoTable getOne(RegisterInfoTable rit) {
        if(isNull(rit)) return null;
        LambdaQueryWrapper<RegisterInfoTable> lambdaQueryWrapper = new QueryWrapper<RegisterInfoTable>()
                .lambda().eq(RegisterInfoTable::getStatus,0);
        if( !isBlank(rit.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getMerchantId,rit.getMerchantId());
        if( !isBlank(rit.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getTerminalMerId,rit.getTerminalMerId());
        if( isNull(rit.getIdentityType()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getIdentityType,rit.getIdentityType());
        if( !isBlank(rit.getUserName()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getUserName,rit.getUserName());
        if( !isBlank(rit.getIdentityNum()) ) lambdaQueryWrapper.eq(RegisterInfoTable::getIdentityNum,rit.getIdentityNum());
        return registerInfoDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(RegisterInfoTable rit) {
        if(isNull(rit)) return false;
        return registerInfoDBService.save(rit);
    }
}
