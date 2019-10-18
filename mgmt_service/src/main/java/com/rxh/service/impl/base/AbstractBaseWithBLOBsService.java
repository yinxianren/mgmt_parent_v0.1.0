package com.rxh.service.impl.base;

import com.rxh.mapper.base.BaseWithBLOBsMapper;
import com.rxh.pojo.Result;
import com.rxh.service.base.BaseWithBLOBsService;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/17
 * Time: 16:37
 * Project: Management
 * Package: com.rxh.service.base
 */
public abstract class AbstractBaseWithBLOBsService<T, TWB extends T, PK> implements BaseWithBLOBsService<T, TWB, PK> {
    private BaseWithBLOBsMapper<T, TWB, PK> mapper;

    @Override
    public Result<TWB> deleteByPrimaryKey(PK id) {
        return getResult(mapper.deleteByPrimaryKey(id));
    }

    @Override
    public Result<TWB> insert(TWB record) {
        return getResult(mapper.insert(record));
    }

    @Override
    public Result<TWB> insertSelective(TWB record) {
        return getResult(mapper.insertSelective(record));
    }

    @Override
    public Result<TWB> selectByPrimaryKey(PK id) {
        return getResult(mapper.selectByPrimaryKey(id));
    }

    @Override
    public Result<TWB> updateByPrimaryKeySelective(TWB record) {
        return getResult(mapper.updateByPrimaryKeySelective(record));
    }

    @Override
    public Result<TWB> updateByPrimaryKeyWithBLOBs(TWB record) {
        return getResult(mapper.updateByPrimaryKeyWithBLOBs(record));
    }

    @Override
    public Result<TWB> updateByPrimaryKey(T record) {
        return getResult(mapper.updateByPrimaryKey(record));
    }

    private Result<TWB> getResult(int changeNum) {
        if (changeNum == 1) {
            return new Result<>(Result.SUCCESS);
        }
        return new Result<>(Result.FAIL);
    }

    private Result<TWB> getResult(TWB ojb) {
        if (ojb != null) {
            return new Result<>(Result.SUCCESS, ojb);
        }
        return new Result<>(Result.FAIL);
    }

    public BaseWithBLOBsMapper<T, TWB, PK> getMapper() {
        return mapper;
    }

    public void setMapper(BaseWithBLOBsMapper<T, TWB, PK> mapper) {
        this.mapper = mapper;
    }
}
