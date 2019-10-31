package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.merchant.MerchantRateDBService;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantRateService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if( !isBlank(mr.getMerchantId())) lambdaQueryWrapper.eq(MerchantRateTable::getMerchantId,mr.getMerchantId());
        if( !isBlank(mr.getProductId())) lambdaQueryWrapper.eq(MerchantRateTable::getProductId,mr.getProductId());
        if( !isBlank(mr.getChannelId())) lambdaQueryWrapper.eq(MerchantRateTable::getChannelId,mr.getChannelId());
        if(!isNull(mr.getStatus())) lambdaQueryWrapper.eq(MerchantRateTable::getStatus,mr.getStatus());
        return merchantRateDBService.getOne(lambdaQueryWrapper);
    }


    @Override
    public boolean save(MerchantRateTable mr) {
        if(isNull(mr)) return false;
        return merchantRateDBService.save(mr);
    }

    @Override
    public Boolean batchSaveOrUpdate(List<MerchantRateTable> rateTables) {
        if(isHasNotElement(rateTables)) return false;
        return merchantRateDBService.saveOrUpdateBatch(rateTables);
    }

    @Override
    public List<MerchantRateTable> getList(MerchantRateTable mer) {
        if (isNull(mer)) return merchantRateDBService.list();
        LambdaQueryWrapper<MerchantRateTable> queryWrapper = new QueryWrapper<MerchantRateTable>().lambda();
        if (StringUtils.isNotEmpty(mer.getMerchantId())) queryWrapper.eq(MerchantRateTable::getMerchantId,mer.getMerchantId());
        return merchantRateDBService.list(queryWrapper);
    }
}
