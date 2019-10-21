package com.rxh.anew;

import com.rxh.service.anew.merchant.AnewMerchantInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/21
 * Time: 上午9:06
 * Description:
 */

@Component
@AllArgsConstructor
public  class CommonRPCComponent {

    public final AnewMerchantInfoService anewMerchantInfoService;

}
