package com.rxh.service.anew.business;

import com.rxh.anew.table.business.RegisterCollectTable;

import java.util.List;

public interface ApiRegisterCollectService {

    RegisterCollectTable  getOne(RegisterCollectTable rct);

    List<RegisterCollectTable> getList(RegisterCollectTable rct);
}
