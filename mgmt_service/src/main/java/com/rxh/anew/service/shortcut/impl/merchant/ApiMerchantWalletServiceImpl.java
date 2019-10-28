package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.merchant.MerchantWalletDBService;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantWalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
