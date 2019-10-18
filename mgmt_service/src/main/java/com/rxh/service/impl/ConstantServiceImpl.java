package com.rxh.service.impl;


import com.rxh.jedis.JedisClientPool;
import com.rxh.mapper.sys.SysConstantMapper;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConstantServiceImpl implements ConstantService {

    @Resource
    private SysConstantMapper constantMapper;

    @Override
    public SysConstant getStateByProvince(String province) {
        return constantMapper.getStateByProvince(province);
    }

    @Override
    public SysConstant selectByConstant(SysConstant constant) {
        // TODO Auto-generated method stub
        return null;
    }


    @CacheEvict(value = "sys_constant", key = "#entity.getGroupCode()")
    @Override
    public Map<String, Object> save(SysConstant entity) {

        Map<String, Object> map = new HashMap<String, Object>();
        int affectCount = constantMapper.insertSelective(entity);
        if (affectCount > 0) {

            map.put("success", 1);

        } else {

            map.put("success", 0);

        }
        return map;

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

    @CacheEvict(value = "sys_constant", key = "#entity.getGroupCode()")
    @Override
    public Map<String, Object> update(SysConstant entity) {

        Map<String, Object> map = new HashMap<String, Object>();
        int affectCount = constantMapper.updateByPrimaryKeySelective(entity);
        if (affectCount > 0) {

            map.put("success", 1);

        } else {

            map.put("success", 0);

        }
        return map;

    }

    @CacheEvict(value = "sys_constant", key = "#entity.getGroupCode()")
    @Override
    public int updateByIdSelective(SysConstant entity) {

        return constantMapper.updateByPrimaryKeySelective(entity);

    }

    @Override
    public SysConstant selectById(String id) {

        return constantMapper.selectByPrimaryKey(id);

    }

    @Override
    public List<SysConstant> queryList(Map<String, Object> param) {
        // TODO Auto-generated method stub
        return constantMapper.queryList(param);
    }


    @Override
    public Map<String, Object> getConstantInfo(Map<String, Object> paramMap) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> searchInfoMap = (Map<String, Object>) paramMap.get("searchInfo");
        if (null != searchInfoMap) {

            paramMap.put("name", searchInfoMap.get("name"));
            paramMap.put("firstValue", searchInfoMap.get("firstValue"));
            paramMap.put("secondValue", searchInfoMap.get("secondValue"));
            paramMap.put("groupCode", searchInfoMap.get("groupCode"));
            paramMap.put("sortValue", searchInfoMap.get("sortValue"));

        }

        paramMap.put("sortFieldName", paramMap.get("sortFieldName"));
        paramMap.put("sortType", paramMap.get("sortType"));

        Integer pageFirst = Integer.parseInt(paramMap.get("pageFirst") + "");
        Integer pageSize = Integer.parseInt(paramMap.get("pageSize") + "");
        pageFirst = pageFirst * pageSize;
        paramMap.put("pageFirst", pageFirst);
        paramMap.put("pageSize", pageSize);

        List<SysConstant> constantList = constantMapper.queryList(paramMap);
        int resultTotal = constantMapper.getConstantAllResultCount(paramMap);
        resultMap.put("resultTotal", resultTotal);

        resultMap.put("constantList", constantList);


        return resultMap;

    }

    @CacheEvict(value = "sys_constant", allEntries = true)
    @Override
    public Map<String, Object> deleteByIds(Map<String, Object> paramMap) {

        Map<String, Object> map = new HashMap<String, Object>();

        String ids = paramMap.get("ids") + "";
        String[] idArray = null;
        int result = 0;

        if (StringUtils.isNotBlank(ids)) {

            idArray = ids.split(",");


        }

        for (int i = 0; i < idArray.length; i++) {

            String id = idArray[i];
            result = constantMapper.deleteByPrimaryKey(id);

        }


        if (result >= 1) {

            map.put("result", "1");

        } else {

            map.put("result", "0");
        }

        return map;

    }

    @Cacheable(value = "sys_constant", key = "#groupName")
    @Override
    public List<SysConstant> getConstantByGroupName(String groupName) {
        return constantMapper.selectByGroupName(groupName);
    }

    @Override
    public List<SysConstant> getConstantByGroupNameAndSortValueIsNotNULL(String groupName) {
        return getConstantByGroupName(groupName)
                .stream()
                .filter(sysConstant -> sysConstant.getSortValue() != 0)
                .collect(Collectors.toList());
        //getConstantByGroupName(groupName).stream().filter(sysConstant ->sysConstant.getGroupCode()=="QuickTest")
    }

    @Override
    public List<SysConstant> getConstantByGroupNameAndSortValueIsNULL(String groupName) {
        return getConstantByGroupName(groupName)
                .stream()
                .filter(sysConstant -> sysConstant.getSortValue()  == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysConstant> selectAll() {
        return constantMapper.selectAll();
    }

    /**
     * 根据系统常量组别获取系统常量
     *
     * @param groupNames 系统常量组别数组
     * @return 系统常量以组别为Key，Value以FirstValue为Key的Map对象的Map对象
     */
    @Override
    public Map<String, Map<String, SysConstant>> getConstantsMapByGroupNames(List<String> groupNames) {
        Map<String, Map<String, SysConstant>> constants = new HashMap<>();
        for (String groupName :
                groupNames) {
            constants.put(groupName, constantMapper.selectByGroupNameForMapKeyFirstValue(groupName));
        }
        return constants;
    }

    /**
     * 获取以firstValue为主键的Map对象
     *
     * @param groupName 组别
     * @return 以firstValue为主键的Map对象
     */
    @Override
    public Map<String, SysConstant> getConstantsMapByGroupName(String groupName) {
        return constantMapper.selectByGroupNameForMapKeyFirstValue(groupName);
    }
}