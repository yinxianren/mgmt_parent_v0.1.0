package com.rxh.service.base;

import com.rxh.pojo.Result;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/17
 * Time: 16:15
 * Project: Management
 * Package: com.rxh.service.base
 */
public interface BaseWithBLOBsService<T, TWB, PK> {
    Result<TWB> deleteByPrimaryKey(PK id);

    Result<TWB> insert(TWB record);

    Result<TWB> insertSelective(TWB record);

    Result<TWB> selectByPrimaryKey(PK id);

    Result<TWB> updateByPrimaryKeySelective(TWB record);

    Result<TWB> updateByPrimaryKeyWithBLOBs(TWB record);

    Result<TWB> updateByPrimaryKey(T record);
}
