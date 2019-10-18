package com.rxh.mapper.base;

import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/5/25
 * Time: 16:46
 * Project: Management
 * Package: com.rxh.mapper.base
 */
public interface IdMapper {
    String selectLastIdByDate(@Param("tableName") String tableName);
    String selectLastId(@Param("tableName") String tableName);
}
