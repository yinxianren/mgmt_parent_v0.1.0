package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantQuotaRiskDBService;
import com.rxh.anew.table.merchant.MerchantQuotaRiskTable;
import com.rxh.mapper.anew.merchant.AnewMerchantQuotaRiskMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantQuotaRiskDBServiceImpl extends ServiceImpl<AnewMerchantQuotaRiskMapper, MerchantQuotaRiskTable> implements MerchantQuotaRiskDBService {
}
