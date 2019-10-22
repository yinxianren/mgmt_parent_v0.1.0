package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantInfoDbService;
import com.rxh.anew.table.merchant.MerchantInfoTable;
import com.rxh.mapper.anew.merchant.AnewMerchantInfoMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 上午10:06
 * Description:
 */
@Service
public class MerchantInfoDbServiceImpl  extends ServiceImpl<AnewMerchantInfoMapper, MerchantInfoTable>  implements MerchantInfoDbService {
}
