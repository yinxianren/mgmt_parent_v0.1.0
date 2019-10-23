package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantSettingDBService;
import com.rxh.anew.table.merchant.MerchantSettingTable;
import com.rxh.mapper.anew.merchant.AnewsMerchantSettingMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantSettingDBServiceImpl extends ServiceImpl<AnewsMerchantSettingMapper, MerchantSettingTable> implements MerchantSettingDBService {
}
