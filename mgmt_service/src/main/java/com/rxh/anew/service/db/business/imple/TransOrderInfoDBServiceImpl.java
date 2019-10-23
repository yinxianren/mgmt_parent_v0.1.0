package com.rxh.anew.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.business.TransOrderInfoDBService;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.mapper.anew.business.AnewTransOrderInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class TransOrderInfoDBServiceImpl extends ServiceImpl<AnewTransOrderInfoMapper, TransOrderInfoTable> implements TransOrderInfoDBService {
}
