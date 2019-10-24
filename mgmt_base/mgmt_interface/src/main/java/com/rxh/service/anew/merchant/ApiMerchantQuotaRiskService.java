package com.rxh.service.anew.merchant;

import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;

import java.util.List;

public interface ApiMerchantQuotaRiskService {

    MerchantQuotaRiskTable getOne(MerchantQuotaRiskTable mqr);

    List<MerchantQuotaRiskTable> getList(MerchantQuotaRiskTable mqr);

    boolean save(MerchantQuotaRiskTable mqr);

    boolean updateByPrimaryKey(MerchantQuotaRiskTable mqr);

}
