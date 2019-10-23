package com.rxh.anew.service.db.business.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rxh.anew.service.db.business.RegisterInfoDBService;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.mapper.anew.business.AnewRegisterInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class RegisterInfoDBServiceImpl extends ServiceImpl<AnewRegisterInfoMapper, RegisterInfoTable> implements RegisterInfoDBService {


    @Override
    public boolean replaceSave(RegisterInfoTable registerInfoTable) {
        return this.baseMapper.replaceSave(registerInfoTable);
    }
}
