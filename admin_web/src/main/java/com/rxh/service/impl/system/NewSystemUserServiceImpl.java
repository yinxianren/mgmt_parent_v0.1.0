package com.rxh.service.impl.system;

import com.internal.playment.api.db.system.ApiSysPrivilegesService;
import com.internal.playment.api.db.system.ApiSysRoleService;
import com.internal.playment.api.db.system.ApiSysUserServie;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.internal.playment.common.table.system.SysRoleTable;
import com.internal.playment.common.table.system.SysUserTable;
import com.rxh.service.system.NewSystemUserService;
import com.rxh.util.UserInfoUtils;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewSystemUserServiceImpl implements NewSystemUserService {

    @Autowired
    private ApiSysUserServie apiSysUserServie;
    @Autowired
    private ApiSysRoleService apiSysRoleService;
    @Autowired
    private ApiSysPrivilegesService apiSysPrivilegesService;

    @Override
    public ResponseVO saveOrUpdate(SysUserTable sysUserTable) {
        if (sysUserTable.getId() == null){
            sysUserTable.setCreateTime(new Date());
            sysUserTable.setCreator(UserInfoUtils.getName());
            sysUserTable.setStatus(StatusEnum._0.getStatus());
            sysUserTable.setAvailable(true);
        }
        if (sysUserTable.getAvailable()){
            sysUserTable.setStatus(StatusEnum._0.getStatus());
        }else {
            sysUserTable.setStatus(StatusEnum._1.getStatus());
        }
        sysUserTable.setUpdateTime(new Date());
        boolean b = apiSysUserServie.savaOrUpdate(sysUserTable);
        ResponseVO responseVO = new ResponseVO();
        if (b){
            responseVO.setMessage(StatusEnum._0.getRemark());
            responseVO.setCode(StatusEnum._0.getStatus());
        }else {
            responseVO.setMessage(StatusEnum._1.getRemark());
            responseVO.setCode(StatusEnum._1.getStatus());
        }
        return responseVO;
    }

    @Override
    public ResponseVO getList(SysUserTable sysUserTable) {
        ResponseVO responseVO = new ResponseVO();
        responseVO.setCode(StatusEnum._0.getStatus());
        responseVO.setMessage(StatusEnum._0.getRemark());
        List<SysUserTable> list = apiSysUserServie.getList(sysUserTable);
        for (SysUserTable sysUserTable1 : list){
            if (sysUserTable1.getStatus() == StatusEnum._0.getStatus()){
                sysUserTable1.setAvailable(true);
            }else {
                sysUserTable1.setAvailable(false);
            }
        }
        responseVO.setData(list);
        return responseVO;
    }

    @Override
    public ResponseVO delByIds(List<Long> ids) {
        ResponseVO responseVO = new ResponseVO();
        boolean b = apiSysUserServie.delByIds(ids);
        if (b){
            responseVO.setCode(StatusEnum._0.getStatus());
            responseVO.setMessage(StatusEnum._0.getRemark());
        }else {
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
        }
        return responseVO;
    }

    public SysUserTable getUserAndRoleAndPrivilege(String username) {
        SysUserTable user = new SysUserTable();
        user.setUserName(username);
        user = apiSysUserServie.getList(user).get(0);
        if (user == null) {
            return null;
        }
        SysRoleTable role = new SysRoleTable();
        role.setId(user.getRoleId());
        role = apiSysRoleService.getList(role).get(0);
        if (null != (role.getStatus())) {
            List<Long> roleIdList = new ArrayList<>();
            Pattern r = Pattern.compile("\\d+");
            Matcher m = r.matcher(role.getPrivilegesId());
            while (m.find()) {
                roleIdList.add(Long.valueOf(m.group()));
            }
            SysPrivilegesTable sysPrivilegesTable = new SysPrivilegesTable();
            sysPrivilegesTable.setIds(roleIdList);
            List<SysPrivilegesTable> sysPrivileges = apiSysPrivilegesService.getList(sysPrivilegesTable);

            role.setPrivileges(sysPrivileges);
        }
        user.setRole(role);
        return user;
    }
}
