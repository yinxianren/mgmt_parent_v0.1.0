package com.rxh.mapper.base;


import java.util.List;
import java.util.Map;

public interface BaseMapper<T, PK> {

    int insert(T record);

    int insertSelective(T record);

    int deleteByPrimaryKey(PK id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    T selectByPrimaryKey(PK id);

    /**
     * 根据条件查询结果集,包括传入分页参数
     *
     * @param param
     * @return
     */
     List<T> queryList(Map<String, Object> param);
}
