package com.rxh.service.anew.system;

import com.rxh.anew.table.system.BankCodeTable;

import java.util.List;

public interface ApiBankCodeService {

    BankCodeTable getOne(BankCodeTable bct);

    List<BankCodeTable> getList(BankCodeTable bct);

    boolean save(BankCodeTable bct);
}
