package com.rxh.service.anew.business;

import com.rxh.anew.table.business.RegisterInfoTable;

public interface ApiRegisterInfoService {

    RegisterInfoTable  getOne(RegisterInfoTable  rit);

    boolean replaceSave(RegisterInfoTable  rit);
}
