package com.rxh.utils.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  综合操作redis 封装类
 * @author  panda
 * @date 20190806
 * @param <T>
 */
public class RedisPosting<T> {
    //以下是指定redis操作核心部件
    private String hashKey;
    private String key;
    private Integer outTime;
    /**
     * 操作类型    typeIndex 和 typeName 只要使用其中一个可以，同时使用增强类型操作的准确性
     *    1:ADD\2:DELETE\3:SELECT\4:UPDATE
     */
    private Integer typeIndex;
    /**
     *  操作类型   typeIndex 和 typeName 只要使用其中一个可以，同时使用增强类型操作的准确性
     *     ADD\DELETE\SELECT\UPDATE
      */
    private String typeName;
    //以下为封装数据对象模型,选其一使用
    private T obj;
    private List<T>  objList;
    private Set<T>   objSet;
    private Map<String,T> objMap;


    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getOutTime() {
        return outTime;
    }

    public void setOutTime(Integer outTime) {
        this.outTime = outTime;
    }

    public Integer getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(Integer typeIndex) {
        this.typeIndex = typeIndex;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getObjList() {
        return objList;
    }

    public void setObjList(List<T> objList) {
        this.objList = objList;
    }

    public Set<T> getObjSet() {
        return objSet;
    }

    public void setObjSet(Set<T> objSet) {
        this.objSet = objSet;
    }

    public Map<String, T> getObjMap() {
        return objMap;
    }

    public void setObjMap(Map<String, T> objMap) {
        this.objMap = objMap;
    }
}
