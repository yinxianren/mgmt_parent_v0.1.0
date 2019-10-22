package com.rxh.anew.service.shortcut.impl.system;

import com.rxh.anew.service.db.system.MerchantSettingDbService;
import com.rxh.service.anew.system.ApiMerchantSettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:55
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiMerchantSettingServiceImpl implements ApiMerchantSettingService {

    private final MerchantSettingDbService  merchantSettingDbService;

}
