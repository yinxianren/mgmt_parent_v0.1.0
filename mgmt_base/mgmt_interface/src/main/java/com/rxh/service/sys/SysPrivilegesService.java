package com.rxh.service.sys;

import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.service.base.BaseService;

import java.util.Map;

public interface SysPrivilegesService extends BaseService<SysPrivileges, Long> {
    boolean addPrivielges(SysPrivileges privileges);
    boolean delPrivielgesById(SysPrivileges privileges);
    boolean bathDelete(SysPrivileges privileges);
    boolean updatePrivielges(SysPrivileges privileges);


    Map<String, Object> getAllPrivielges(Map<String, Object>  paramMap);

    boolean batchDelete(Long[] ids);
}