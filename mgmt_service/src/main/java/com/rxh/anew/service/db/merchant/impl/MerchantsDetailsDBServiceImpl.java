package com.rxh.anew.service.db.merchant.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.merchant.MerchantsDetailsDBService;
import com.rxh.anew.table.merchant.MerchantsDetailsTable;
import com.rxh.mapper.anew.merchant.AnewMerchantsDetailsMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantsDetailsDBServiceImpl extends ServiceImpl<AnewMerchantsDetailsMapper, MerchantsDetailsTable> implements MerchantsDetailsDBService {
}
