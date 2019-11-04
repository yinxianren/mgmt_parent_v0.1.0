package com.rxh.service.anew.system;

import com.rxh.anew.table.system.BankRateTable;

import java.util.List;

public interface ApiBankRateService {

    BankRateTable getOne(BankRateTable brt);

    List<BankRateTable>  getList(BankRateTable brt);

    boolean save(BankRateTable brt );
}
