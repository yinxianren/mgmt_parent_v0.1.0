package com.rxh.anew.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.business.MerchantCardDBService;
import com.rxh.anew.table.business.MerchantCardTable;
import com.rxh.mapper.anew.business.AnewMerchantCardMapper;
import org.springframework.stereotype.Service;

@Service
public class MerchantCardDBServiceImpl extends ServiceImpl<AnewMerchantCardMapper, MerchantCardTable> implements MerchantCardDBService {
}
