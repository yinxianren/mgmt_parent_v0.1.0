package com.rxh.anew.service.db.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rxh.anew.table.business.RegisterInfoTable;

public interface RegisterInfoDBService extends IService<RegisterInfoTable> {

    boolean replaceSave(RegisterInfoTable registerInfoTable);
}
