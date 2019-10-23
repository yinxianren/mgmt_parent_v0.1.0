package com.rxh.anew.service.shortcut.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rxh.anew.service.db.system.MerchantSettingDbService;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.enums.StatusEnum;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiMerchantSettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:55
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantSettingServiceImpl implements ApiMerchantSettingService, NewPayAssert {


    private final MerchantSettingDbService  merchantSettingDbService;


    @Override
    public List<MerchantSettingTable> getList(MerchantSettingTable mst) {
        if(isNull(mst)) return null;
        LambdaQueryWrapper<MerchantSettingTable> lambdaQueryWrapper = new QueryWrapper<MerchantSettingTable>()
                .lambda()
                .eq(MerchantSettingTable::getStatus, StatusEnum._0.getStatus()); //默认取可用的

        if( !isBlank(mst.getMerchantId()) ) lambdaQueryWrapper.eq(MerchantSettingTable::getMerchantId,mst.getMerchantId());
        return merchantSettingDbService.list(lambdaQueryWrapper);
    }


}
