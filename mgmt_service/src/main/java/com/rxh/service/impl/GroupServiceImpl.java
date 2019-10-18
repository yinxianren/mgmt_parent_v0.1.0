package com.rxh.service.impl;


import com.rxh.mapper.sys.SysGroupMapper;
import com.rxh.pojo.sys.SysGroup;
import com.rxh.service.GroupService;
import com.rxh.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupServiceImpl implements GroupService
{

	@Resource
	private SysGroupMapper groupMapper;
    

	@Override
	public Map<String, Object> save(SysGroup entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteById(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByParam(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String,Object> update(SysGroup entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByIdSelective(SysGroup entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SysGroup selectById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysGroup> queryList(Map<String, Object> param) {
		
		return groupMapper.queryList(null);
		
	}

	@Override
	public List<SysGroup> selectAll() {
		return groupMapper.selectAll();
	}

	@Override
	public Map<String, Object> findSysGroup(Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> searchInfoMap = (Map<String, Object>) paramMap.get("searchInfo");
		if (null != searchInfoMap) {
			paramMap.put("name", searchInfoMap.get("name"));
			paramMap.put("code", searchInfoMap.get("code"));
            String model;
			if(searchInfoMap.get("model")!=null){
                model= searchInfoMap.get("model").toString();
                switch (model){

                    case "系统": paramMap.put("model",1);break;
                    case "商户": paramMap.put("model",2);break;
                    case "代理商": paramMap.put("model",3);break;
                    default:paramMap.put("model",model);break;
                }
            }
            String system;
            if(searchInfoMap.get("system")!=null){
                system= searchInfoMap.get("system").toString();
                switch (system){
                    case "是": paramMap.put("system",1);break;
                    case "否": paramMap.put("system",0);break;
                    default:paramMap.put("system",system);break;
                }
            }




		}
		Integer pageFirst = Integer.parseInt(paramMap.get("pageFirst") + "");
		Integer pageSize = Integer.parseInt(paramMap.get("pageSize") + "");
		pageFirst = pageFirst * pageSize;
		paramMap.put("pageFirst", pageFirst);
		paramMap.put("pageSize", pageSize);
		List<SysGroup> constantList = groupMapper.queryList(paramMap);
		int resultTotal = groupMapper.getConstantAllResultCount(paramMap);
		resultMap.put("resultTotal", resultTotal);

		resultMap.put("sysGroupList", constantList);
		return resultMap;
	}

	@Override
	public Map<String, Object> addSysGroup(SysGroup sysGroup) {
		Map<String, Object> map = new HashMap<String, Object>();
		int affectCount = groupMapper.insertSelective(sysGroup);
		if (affectCount > 0) {
			map.put("success", 1);
		} else {
			map.put("success", 0);
		}
		return map;
	}

	@Override
	public Map<String, Object> updateSysGroup(SysGroup sysGroup) {
		Map<String, Object> map = new HashMap<String, Object>();
		int affectCount = groupMapper.updateByPrimaryKeySelective(sysGroup);
		if (affectCount > 0) {

			map.put("success", 1);

		} else {

			map.put("success", 0);

		}
		return map;
	}

	@Override
	public Map<String, Object> deleteByIds(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = paramMap.get("codes") + "";
		String[] idArray = null;
		int result = 0;
		if (StringUtils.isNotBlank(ids)) {
			idArray = ids.split(",");
		}
		for (int i = 0; i < idArray.length; i++) {
			String id = idArray[i];
			result = groupMapper.deleteByPrimaryKey(id);
		}
		if (result >= 1) {
			map.put("result", "1");
		} else {
			map.put("result", "0");
		}
		return map;
	}

	@Override
	public SysGroup selectUserByGroupName(String groupName) {
	    if(groupName.equals("")||groupName==null){
	        return null;
        }

		return groupMapper.selectUserByGroupName(groupName);
	}

    @Override
    public SysGroup selectUserByGroupCode(String groupCode) {
        if(groupCode.equals("")||groupCode==null){
            return null;
        }

        return groupMapper.selectUserByGroupCode(groupCode);
    }
}
