package com.rxh.anew.service.shortcut.impl.merchant;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.merchant.MerchantInfoDbService;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午8:59
 * Description:
 */
@Service
public class ApiMerchantInfoServiceImpl implements ApiMerchantInfoService, NewPayAssert {

    @Autowired
    private MerchantInfoDbService merchantInfoDbService;

    @Override
    public MerchantInfoTable getOne(MerchantInfoTable mit) {
        if(isNull(mit)) return null;
        LambdaQueryWrapper<MerchantInfoTable>  lambdaQueryWrapper=new QueryWrapper<MerchantInfoTable>().lambda();
        if( !isBlank(mit.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,mit.getMerchantId());
        return merchantInfoDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<MerchantInfoTable> getMerchant(MerchantInfoTable merchantInfoTable) {
        if (isNull(merchantInfoTable)) return merchantInfoDbService.list();
        LambdaQueryWrapper<MerchantInfoTable>  lambdaQueryWrapper=new QueryWrapper<MerchantInfoTable>().lambda();
        if( !isBlank(merchantInfoTable.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,merchantInfoTable.getMerchantId());
        if( !isBlank(merchantInfoTable.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,merchantInfoTable.getMerchantId());
        if( !isBlank(merchantInfoTable.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,merchantInfoTable.getMerchantId());
        if( !isBlank(merchantInfoTable.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantInfoTable::getMerchantId,merchantInfoTable.getMerchantId());
        return null;
    }

    @Override
    public Boolean saveOrUpdate(MerchantInfoTable merchantInfoTable) {
        return null;
    }

    @Override
    public Boolean delByIds(List<String> ids) {
        return null;
    }


}
