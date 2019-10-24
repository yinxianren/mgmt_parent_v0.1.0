package com.rxh.anew.service.shortcut.impl.merchant;

import com.rxh.anew.service.db.merchant.MerchantQuotaRiskDBService;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.merchant.ApiMerchantQuotaRiskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/24
 * Time: 下午8:48
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantQuotaRiskServiceImpl implements ApiMerchantQuotaRiskService, NewPayAssert {

    private final MerchantQuotaRiskDBService merchantQuotaRiskDBService;


    @Override
    public MerchantQuotaRiskTable getOne(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return null;
        return null;
    }

    @Override
    public List<MerchantQuotaRiskTable> getList(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return null;
        return null;
    }

    @Override
    public boolean save(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return false;
        return false;
    }

    @Override
    public boolean updateByPrimaryKey(MerchantQuotaRiskTable mqr) {
        if(isNull(mqr)) return false;
        return false;
    }
}
