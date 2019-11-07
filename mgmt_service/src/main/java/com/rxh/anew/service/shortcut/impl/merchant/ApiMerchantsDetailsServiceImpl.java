package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rxh.anew.service.db.merchant.MerchantsDetailsDBService;
import com.rxh.anew.table.channel.ChannelDetailsTable;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantsDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午7:21
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantsDetailsServiceImpl implements ApiMerchantsDetailsService, NewPayAssert {

    private final MerchantsDetailsDBService merchantsDetailsDBService;

    @Override
    public boolean save(MerchantsDetailsTable mdt) {
      if(isNull(mdt)) return false;

        return merchantsDetailsDBService.save(mdt);
    }

    @Override
    public IPage page(MerchantsDetailsTable merchantsDetailsTable) {
        if (isNull(merchantsDetailsTable)) return new Page();
        LambdaQueryWrapper<MerchantsDetailsTable> queryWrapper = new LambdaQueryWrapper();
        IPage iPage = new Page(merchantsDetailsTable.getPageNum(),merchantsDetailsTable.getPageSize());
        if (!isBlank(merchantsDetailsTable.getMerchantId())) queryWrapper.eq(MerchantsDetailsTable::getMerchantId,merchantsDetailsTable.getMerchantId());
        if (!isBlank(merchantsDetailsTable.getMerOrderId())) queryWrapper.eq(MerchantsDetailsTable::getMerOrderId,merchantsDetailsTable.getMerOrderId());
        if (!isBlank(merchantsDetailsTable.getPlatformOrderId())) queryWrapper.eq(MerchantsDetailsTable::getPlatformOrderId,merchantsDetailsTable.getPlatformOrderId());
        if (!isBlank(merchantsDetailsTable.getProductId())) queryWrapper.eq(MerchantsDetailsTable::getProductId,merchantsDetailsTable.getProductId());
        if (!isNull(merchantsDetailsTable.getBeginTime())) queryWrapper.gt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getBeginTime());
        if (!isNull(merchantsDetailsTable.getEndTime())) queryWrapper.lt(MerchantsDetailsTable::getUpdateTime,merchantsDetailsTable.getEndTime());
        return merchantsDetailsDBService.page(iPage,queryWrapper);
    }
}
