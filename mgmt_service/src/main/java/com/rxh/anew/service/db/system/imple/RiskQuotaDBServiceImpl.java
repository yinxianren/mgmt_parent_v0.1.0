package com.rxh.anew.service.db.system.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.system.RiskQuotaDBService;
import com.rxh.anew.table.system.RiskQuotaTable;
import com.rxh.mapper.anew.system.AnewRiskQuotaMapper;
import org.springframework.stereotype.Service;

@Service
public class RiskQuotaDBServiceImpl extends ServiceImpl<AnewRiskQuotaMapper, RiskQuotaTable> implements RiskQuotaDBService {
}
