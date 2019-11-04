package com.rxh.anew.service.shortcut.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.BankRateDbService;
import com.rxh.anew.table.system.BankRateTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiBankRateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:48
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiBankRateServiceImpl implements ApiBankRateService, NewPayAssert {

    private final BankRateDbService bankRateDbService;

    @Override
    public BankRateTable getOne(BankRateTable brt) {
        if(isNull(brt)) return null;
        LambdaQueryWrapper<BankRateTable> lambdaQueryWrapper =new QueryWrapper<BankRateTable>().lambda();
        if( !isBlank(brt.getBankCode()) ) lambdaQueryWrapper.eq(BankRateTable::getBankCode,brt.getBankCode());
        if( !isBlank(brt.getOrganizationId()) ) lambdaQueryWrapper.eq(BankRateTable::getOrganizationId,brt.getOrganizationId());
        if( !isBlank(brt.getProductId()) ) lambdaQueryWrapper.eq(BankRateTable::getProductId,brt.getProductId());
        return bankRateDbService.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<BankRateTable> getList(BankRateTable brt) {
        if(isNull(brt)) return null;
        LambdaQueryWrapper<BankRateTable> lambdaQueryWrapper =new QueryWrapper<BankRateTable>().lambda();
        if( !isBlank(brt.getBankCode()) ) lambdaQueryWrapper.eq(BankRateTable::getBankCode,brt.getBankCode());
        if( !isBlank(brt.getOrganizationId()) ) lambdaQueryWrapper.eq(BankRateTable::getOrganizationId,brt.getOrganizationId());
        if( !isBlank(brt.getProductId()) ) lambdaQueryWrapper.eq(BankRateTable::getProductId,brt.getProductId());
        return bankRateDbService.list(lambdaQueryWrapper);
    }

    @Override
    public boolean save(BankRateTable brt) {
        if(isNull(brt)) return false;
        return bankRateDbService.save(brt);
    }
}
