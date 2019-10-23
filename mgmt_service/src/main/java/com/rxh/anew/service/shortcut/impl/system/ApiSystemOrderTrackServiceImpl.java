package com.rxh.anew.service.shortcut.impl.system;

import com.rxh.anew.service.db.system.SystemOrderTrackDbService;
import com.rxh.anew.table.system.SystemOrderTrackTable;
import com.rxh.payInterface.NewPayAssert;
import com.rxh.service.anew.system.ApiSystemOrderTrackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午1:56
 * Description:
 */
@AllArgsConstructor
@Service
public class ApiSystemOrderTrackServiceImpl implements ApiSystemOrderTrackService, NewPayAssert {

    private final SystemOrderTrackDbService systemOrderTrackDbService;

    @Override
    public boolean save(SystemOrderTrackTable sot) {
        if(isNull(sot)) return false;
        return systemOrderTrackDbService.save(sot);
    }


}
