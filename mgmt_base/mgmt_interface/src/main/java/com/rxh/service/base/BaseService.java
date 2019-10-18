package com.rxh.service.base;

import com.rxh.pojo.Result;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/16
 * Time: 10:20
 * Project: Management
 * Package: com.rxh.service.core
 */
public interface BaseService<T, PK> {

    Result<T> insert(T record);

    Result<T> insertSelective(T record);

    Result<T> deleteByPrimaryKey(PK id);

    Result<T> updateByPrimaryKeySelective(T record);

    Result<T> updateByPrimaryKey(T record);

    Result<T> selectByPrimaryKey(PK id);

}