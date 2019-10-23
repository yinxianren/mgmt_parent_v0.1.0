package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantRateDBService;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.mapper.anew.merchant.AnewMerchantRateMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantRateDBServiceImpl extends ServiceImpl<AnewMerchantRateMapper, MerchantRateTable> implements MerchantRateDBService {
}
