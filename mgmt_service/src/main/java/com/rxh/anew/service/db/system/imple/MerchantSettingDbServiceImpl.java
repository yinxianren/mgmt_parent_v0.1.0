package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.MerchantSettingDbService;
import com.rxh.anew.table.system.MerchantSettingTable;
import com.rxh.mapper.anew.system.AnewMerchantSettingMapper;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:54
 * Description:
 */
@Service
public class MerchantSettingDbServiceImpl extends ServiceImpl<AnewMerchantSettingMapper, MerchantSettingTable> implements MerchantSettingDbService {
}
