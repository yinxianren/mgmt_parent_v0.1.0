package com.rxh.anew.service.shortcut.impl.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.RegisterCollectDbService;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiRegisterCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午11:44
 * Description:
 */
@Service
public class ApiRegisterCollectServiceImpl implements ApiRegisterCollectService, NewPayAssert {

    @Autowired
    private RegisterCollectDbService registerCollectDbService;

    @Override
    public RegisterCollectTable getOne(RegisterCollectTable rct) {
        if(isNull(rct)) return null;
        LambdaQueryWrapper<RegisterCollectTable> lambdaQueryWrapper = new QueryWrapper().lambda();
        if( !isBlank(rct.getMerchantId())) lambdaQueryWrapper.eq(RegisterCollectTable::getMerchantId,rct.getMerchantId());
        if( !isBlank(rct.getTerminalMerId())) lambdaQueryWrapper.eq(RegisterCollectTable::getTerminalMerId,rct.getTerminalMerId());
        if( !isBlank(rct.getMerOrderId())) lambdaQueryWrapper.eq(RegisterCollectTable::getMerOrderId,rct.getMerOrderId());
        return registerCollectDbService.getOne(lambdaQueryWrapper);
    }

}
