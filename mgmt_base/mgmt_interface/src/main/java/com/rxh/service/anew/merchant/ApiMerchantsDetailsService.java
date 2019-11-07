package com.rxh.service.anew.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;

public interface ApiMerchantsDetailsService {

    boolean save(MerchantsDetailsTable mdt);

    IPage page(MerchantsDetailsTable merchantsDetailsTable);

}
