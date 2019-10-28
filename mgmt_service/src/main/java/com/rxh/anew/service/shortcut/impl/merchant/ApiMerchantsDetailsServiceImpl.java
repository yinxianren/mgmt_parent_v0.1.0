package com.rxh.anew.service.shortcut.impl.merchant;

import com.rxh.anew.service.db.merchant.MerchantsDetailsDBService;
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
public class ApiMerchantsDetailsServiceImpl implements ApiMerchantsDetailsService , NewPayAssert {

    private final MerchantsDetailsDBService merchantsDetailsDBService;

    @Override
    public boolean save(MerchantsDetailsTable mdt) {
      if(isNull(mdt)) return false;

        return merchantsDetailsDBService.save(mdt);
    }
}
