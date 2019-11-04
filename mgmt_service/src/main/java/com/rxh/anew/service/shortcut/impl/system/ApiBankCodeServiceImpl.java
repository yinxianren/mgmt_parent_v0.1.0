package com.rxh.anew.service.shortcut.impl.system;

import com.rxh.anew.service.db.system.BankCodeDbService;
import com.rxh.anew.table.system.BankCodeTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiBankCodeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/1
 * Time: 下午3:47
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiBankCodeServiceImpl implements ApiBankCodeService, NewPayAssert {

    private final BankCodeDbService bankCodeDbService;

    @Override
    public BankCodeTable getOne(BankCodeTable bct) {
        return null;
    }

    @Override
    public List<BankCodeTable> getList(BankCodeTable bct) {
        return null;
    }

    @Override
    public boolean save(BankCodeTable bct) {
        return false;
    }
}
