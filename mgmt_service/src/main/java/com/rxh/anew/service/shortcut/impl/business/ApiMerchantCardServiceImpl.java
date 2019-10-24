package com.rxh.anew.service.shortcut.impl.business;

import com.alibaba.druid.sql.visitor.functions.Isnull;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.business.MerchantCardDBService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.business.ApiMerchantCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.ImageConsumer;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 上午9:18
 * Description:
 */

@AllArgsConstructor
@Service
public class ApiMerchantCardServiceImpl implements ApiMerchantCardService , NewPayAssert {

    private final MerchantCardDBService merchantCardDBService;

    @Override
    public MerchantCardTable getOne(MerchantCardTable mct) {
        if(isNull(mct)) return null;
        LambdaQueryWrapper<MerchantCardTable> lambdaQueryWrapper = new QueryWrapper<MerchantCardTable>() .lambda();
        if( !isNull(mct.getStatus()) )  lambdaQueryWrapper.eq(MerchantCardTable::getStatus,mct.getStatus());
        if( !isNull(mct.getMerOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerOrderId,mct.getMerOrderId());
        return merchantCardDBService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<MerchantCardTable> getList(MerchantCardTable mct) {
        if(isNull(mct)) return null;
        LambdaQueryWrapper<MerchantCardTable> lambdaQueryWrapper = new QueryWrapper<MerchantCardTable>() .lambda();
        if( !isNull(mct.getStatus()) )  lambdaQueryWrapper.eq(MerchantCardTable::getStatus,mct.getStatus());
        if( !isNull(mct.getMerOrderId()) )  lambdaQueryWrapper.eq(MerchantCardTable::getMerOrderId,mct.getMerOrderId());
        return  merchantCardDBService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean updateById(MerchantCardTable mct) {
        if(isNull(mct)) return false;
        return merchantCardDBService.updateById(mct);
    }

    @Override
    public boolean save(MerchantCardTable mct) {
        if(isNull(mct)) return false;
        return merchantCardDBService.save(mct);
    }
}
