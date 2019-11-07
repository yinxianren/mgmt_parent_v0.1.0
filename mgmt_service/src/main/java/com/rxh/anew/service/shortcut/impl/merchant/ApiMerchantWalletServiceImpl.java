package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.merchant.MerchantWalletDBService;
import com.rxh.anew.service.db.merchant.MerchantsDetailsDBService;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午7:14
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantWalletServiceImpl implements ApiMerchantWalletService, NewPayAssert {

   private final MerchantWalletDBService merchantWalletDBService;

   private final MerchantsDetailsDBService merchantsDetailsDBService;

    @Override
    public MerchantWalletTable getOne(MerchantWalletTable mwt) {
        if(isNull(mwt)) return null;
        LambdaQueryWrapper<MerchantWalletTable> lambdaQueryWrapper = new QueryWrapper<MerchantWalletTable>()
                .lambda();

        if( !isBlank(mwt.getMerchantId())) lambdaQueryWrapper.eq(MerchantWalletTable::getMerchantId,mwt.getMerchantId());
        if( !isNull(mwt.getStatus())) lambdaQueryWrapper.eq(MerchantWalletTable::getStatus,mwt.getStatus());

        return merchantWalletDBService.getOne(lambdaQueryWrapper);
    }


    @Override
    public boolean updateOrSave(MerchantWalletTable mwt) {
        if(isNull(mwt)) return false;
        return merchantWalletDBService.saveOrUpdate(mwt);
    }

    @Override
    public List<MerchantWalletTable> getList(MerchantWalletTable merchantWalletTable) {
        if (isNull(merchantWalletTable)) return merchantWalletDBService.list();
        LambdaQueryWrapper<MerchantWalletTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(merchantWalletTable.getMerchantId())) queryWrapper.eq(MerchantWalletTable::getMerchantId,merchantWalletTable.getMerchantId());
        if (isHasElement(merchantWalletTable.getMerchantIds())) queryWrapper.in(MerchantWalletTable::getMerchantId,merchantWalletTable.getMerchantIds());
        return merchantWalletDBService.list(queryWrapper);
    }

    @Override
    public IPage page(MerchantsDetailsTable merchantsDetailsTable) {
        if (isNull(merchantsDetailsTable)) return new Page();
        LambdaQueryWrapper<MerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper();
        if (!isBlank(merchantsDetailsTable.getMerchantId())) queryWrapper.eq(MerchantsDetailsTable::getMerchantId,merchantsDetailsTable.getMerchantId());
        if (!isBlank(merchantsDetailsTable.getMerOrderId())) queryWrapper.eq(MerchantsDetailsTable::getMerOrderId,merchantsDetailsTable.getMerOrderId());
        if (!isBlank(merchantsDetailsTable.getPlatformOrderId())) queryWrapper.eq(MerchantsDetailsTable::getPlatformOrderId,merchantsDetailsTable.getPlatformOrderId());
        if (!isBlank(merchantsDetailsTable.getProductId())) queryWrapper.eq(MerchantsDetailsTable::getProductId,merchantsDetailsTable.getProductId());
        if (!isNull(merchantsDetailsTable.getBeginTime())) queryWrapper.gt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getBeginTime());
        if (!isNull(merchantsDetailsTable.getEndTime())) queryWrapper.lt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getEndTime());
        IPage iPage = new Page(merchantsDetailsTable.getPageNum(),merchantsDetailsTable.getPageSize());
        return merchantsDetailsDBService.page(iPage,queryWrapper);
    }
}
