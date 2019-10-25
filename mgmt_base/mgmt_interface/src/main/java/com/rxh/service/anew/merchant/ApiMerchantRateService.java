package com.rxh.service.anew.merchant;

import com.rxh.anew.table.merchant.MerchantRateTable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/25
 * Time: 下午7:41
 * Description:
 */
public interface ApiMerchantRateService {

    MerchantRateTable getOne(MerchantRateTable mr);

    boolean save(MerchantRateTable mr);

}
