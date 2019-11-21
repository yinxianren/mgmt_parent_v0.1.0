package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSysConstantService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysConstantTable;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/18
 * Time: 上午9:30
 * Description:
 */

@Service
public class NewSystemConstantServiceImpl implements NewSystemConstantService {

    @Autowired
    private ApiSysConstantService apiSysConstantService;

    @Override
    public ResponseVO getConstantByGroupName(String GroupName) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.hashCode());
        responseVO.setMessage(StatusEnum._0.getRemark());
        SysConstantTable sysConstantTable = new SysConstantTable();
        sysConstantTable.setGroupCode(GroupName);
        responseVO.setData(apiSysConstantService.getList(sysConstantTable));
        return responseVO;
    }

    @Override
    public Map getConstantsMapByGroupName(String GroupName) {
        SysConstantTable sysConstantTable = new SysConstantTable();
        sysConstantTable.setGroupCode(GroupName);
        List<SysConstantTable> list = (apiSysConstantService.getList(sysConstantTable));
        Map map = new HashMap();
        for (SysConstantTable sysConstantTable1 : list){
            map.put(sysConstantTable1.getFirstValue(),sysConstantTable1);
        }
        return map;
    }
}
