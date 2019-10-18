package com.rxh.service.impl.base;

import com.rxh.mapper.base.BaseMapper;
import com.rxh.pojo.Result;
import com.rxh.service.base.BaseService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/16
 * Time: 10:36
 * Project: Management
 * Package: com.rxh.service.base
 */

public abstract class AbstractBaseService<T, PK> implements BaseService<T, PK> {

    private BaseMapper<T, PK> mapper;

    @Override
    public Result<T> insert(T record) {
        return getResult(mapper.insert(record));
    }

    @Override
    public Result<T> insertSelective(T record) {
        return getResult(mapper.insertSelective(record));
    }

    @Override
    public Result<T> deleteByPrimaryKey(PK id) {
        return getResult(mapper.deleteByPrimaryKey(id));
    }

    @Override
    public Result<T> updateByPrimaryKeySelective(T record) {
        return getResult(mapper.updateByPrimaryKeySelective(record));
    }

    @Override
    public Result<T> updateByPrimaryKey(T record) {
        return getResult(mapper.updateByPrimaryKey(record));
    }

    @Override
    public Result<T> selectByPrimaryKey(PK id) {
        return getResult(mapper.selectByPrimaryKey(id));
    }

    public Result<T> getResult(int changeNum) {
        if (changeNum == 1) {
            return new Result<>(Result.SUCCESS);
        }
        return new Result<>(Result.FAIL);
    }

    public Result<T> getResult(T obj) {
        if (obj != null) {
            return new Result<>(Result.SUCCESS, obj);
        }
        return new Result<>(Result.FAIL);
    }

    public BaseMapper<T, PK> getMapper() {
        return mapper;
    }

    public void setMapper(BaseMapper<T, PK> mapper) {
        this.mapper = mapper;
    }
}