package com.rxh.service.impl.sys;

import com.rxh.mapper.sys.SysPrivilegesMapper;
import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.service.impl.base.AbstractBaseService;
import com.rxh.service.sys.SysPrivilegesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SysPrivilegesServiceImpl extends AbstractBaseService<SysPrivileges, Long> implements SysPrivilegesService {
    @Resource
    private  SysPrivilegesMapper sysPrivilegesMapper;

//    @Autowired
//    public SysPrivilegesServiceImpl(SysPrivilegesMapper sysPrivilegesMapper) {
//        this.sysPrivilegesMapper = sysPrivilegesMapper;
//        setMapper(sysPrivilegesMapper);
//    }

    @Override
    public boolean addPrivielges(SysPrivileges privileges) {
        int success = sysPrivilegesMapper.insert(privileges);

        return success>0;
    }

    @Override
    public boolean delPrivielgesById(SysPrivileges privileges) {
        int success = sysPrivilegesMapper.deleteByPrimaryKey(privileges.getId());
        return success>0;
    }

    @Override
    public boolean bathDelete(SysPrivileges privileges) {

        return false;
    }

    @Override
    public boolean updatePrivielges(SysPrivileges privileges) {
        int success = sysPrivilegesMapper.updateByPrimaryKey(privileges);
        return success>0;
    }
    @Override
    public   Map<String, Object> getAllPrivielges( Map<String, Object> paramMap ) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> searchInfoMap = (Map<String, Object>) paramMap.get("searchInfo");
        if (null != searchInfoMap) {
            paramMap.put("description", searchInfoMap.get("description"));
            paramMap.put("name", searchInfoMap.get("name"));
            paramMap.put("stateName", searchInfoMap.get("stateName"));
        }


        Integer pageFirst = Integer.parseInt(paramMap.get("pageFirst") + "");
        Integer pageSize = Integer.parseInt(paramMap.get("pageSize") + "");

        pageFirst = pageFirst * pageSize;
        paramMap.put("pageFirst", pageFirst);
        paramMap.put("pageSize", pageSize);
        List<SysPrivileges> sysPrivilegesList= sysPrivilegesMapper.queryList(paramMap);//.queryList(paramMap);
        resultMap.put("resultTotal", sysPrivilegesList.size());

        resultMap.put("sysPrivilegesList", sysPrivilegesList);
        return resultMap;

    }

    @Override
    public boolean batchDelete(Long[] ids) {
        for (Long id : ids) {
            try {
                sysPrivilegesMapper.deleteByPrimaryKey(id);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}