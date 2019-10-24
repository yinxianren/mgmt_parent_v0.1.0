package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.RegisterCollectDbService;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiRegisterCollectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:44
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiRegisterCollectServiceImpl implements ApiRegisterCollectService, NewPayAssert {

    private final RegisterCollectDbService registerCollectDbService;

    @Override
    public RegisterCollectTable getOne(RegisterCollectTable rct) {
        if(isNull(rct)) return null;
        LambdaQueryWrapper<RegisterCollectTable> lambdaQueryWrapper = new QueryWrapper().lambda();
        if( !isBlank(rct.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerchantId,rct.getMerchantId());
        if( !isBlank(rct.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getTerminalMerId,rct.getTerminalMerId());
        if( !isBlank(rct.getMerOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerOrderId,rct.getMerOrderId());
        if( !isBlank(rct.getPlatformOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getPlatformOrderId,rct.getPlatformOrderId());
        if( !isNull(rct.getStatus()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getStatus,rct.getStatus());
        if( !isBlank(rct.getBussType()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getBussType,rct.getBussType());
        return registerCollectDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<RegisterCollectTable> getList(RegisterCollectTable rct) {
        if(isNull(rct)) return null;
        LambdaQueryWrapper<RegisterCollectTable> lambdaQueryWrapper = new QueryWrapper().lambda();
        if( !isBlank(rct.getMerchantId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerchantId,rct.getMerchantId());
        if( !isBlank(rct.getTerminalMerId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getTerminalMerId,rct.getTerminalMerId());
        if( !isBlank(rct.getMerOrderId()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getMerOrderId,rct.getMerOrderId());
        if( !isBlank(rct.getBussType()) ) lambdaQueryWrapper.eq(RegisterCollectTable::getBussType,rct.getBussType());
        if( !isNull(rct.getStatus()) )  lambdaQueryWrapper.eq(RegisterCollectTable::getStatus,rct.getStatus());
        return registerCollectDbService.list(lambdaQueryWrapper);
    }
    @Override
    public boolean save(RegisterCollectTable rct) {
        if(isNull(rct))  return false;
        return registerCollectDbService.save(rct);
    }

    @Override
    public boolean updateByPrimaryKey(RegisterCollectTable rct) {
        if(isNull(rct))  return false;
        return registerCollectDbService.updateById(rct);
    }

}
