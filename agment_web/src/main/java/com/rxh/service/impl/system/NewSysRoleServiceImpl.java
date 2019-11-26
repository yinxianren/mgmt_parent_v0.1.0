package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSysRoleService;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.ResponseVO;
import com.internal.playment.common.table.system.SysRoleTable;
import com.rxh.service.system.NewSysRoleService;
import com.rxh.util.UserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewSysRoleServiceImpl implements NewSysRoleService {

    @Autowired
    private ApiSysRoleService apiSysRoleService;

    @Override
    public ResponseVO saveOrUpdate(SysRoleTable sysRoleTable) {
        ResponseVO responseVO = new ResponseVO();
        if (sysRoleTable.getId()== null){
            sysRoleTable.setCreateTime(new Date());
            sysRoleTable.setCreator(UserInfoUtils.getName());
            sysRoleTable.setStatus(StatusEnum._0.getStatus());
            sysRoleTable.setAvailable(true);
        }
        if (null != sysRoleTable.getAvailable() && sysRoleTable.getAvailable()){
            sysRoleTable.setStatus(StatusEnum._0.getStatus());
        }else {
            sysRoleTable.setStatus(StatusEnum._1.getStatus());
        }
        sysRoleTable.setUpdateTime(new Date());
        boolean b = apiSysRoleService.saveOrUpdate(sysRoleTable);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO delByids(List<String> ids) {
        ResponseVO responseVO = new ResponseVO();
        List<Long> longList = new ArrayList<>();
        for (String id : ids){
            longList.add(Long.valueOf(id));
        }
        boolean b = apiSysRoleService.delByIds(longList);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(SysRoleTable sysRoleTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        List<SysRoleTable> list = apiSysRoleService.getList(sysRoleTable);
        for (SysRoleTable sysRoleTable1 : list){
            if (sysRoleTable1.getStatus() == StatusEnum._0.getStatus()){
                sysRoleTable1.setAvailable(true);
            }else {
                sysRoleTable1.setAvailable(false);
            }
        }
        responseVO.setData(list);
        return responseVO;
    }
}
