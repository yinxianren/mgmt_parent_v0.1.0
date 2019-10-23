package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantWalletDBService;
import com.rxh.anew.table.merchant.MerchantWalletTable;
import com.rxh.mapper.anew.merchant.AnewMerchantWalletMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantWalletDBServiceImpl extends ServiceImpl<AnewMerchantWalletMapper, MerchantWalletTable> implements MerchantWalletDBService {
}
