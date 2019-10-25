package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.merchant.MerchantRateDBService;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantRateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 下午7:42
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantRateServiceImpl implements ApiMerchantRateService, NewPayAssert {

    private final   MerchantRateDBService merchantRateDBService;

    @Override
    public MerchantRateTable getOne(MerchantRateTable mr) {
        if(isNull(mr)) return null;
        LambdaQueryWrapper<MerchantRateTable> lambdaQueryWrapper = new QueryWrapper<MerchantRateTable>().lambda();
        if(!isBlank(mr.getMerchantId())) lambdaQueryWrapper.eq(MerchantRateTable::getMerchantId,mr.getMerchantId());
        if(!isNull(mr.getStatus())) lambdaQueryWrapper.eq(MerchantRateTable::getStatus,mr.getStatus());
        return merchantRateDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public boolean save(MerchantRateTable mr) {
        if(isNull(mr)) return false;
        return merchantRateDBService.save(mr);
    }
}
