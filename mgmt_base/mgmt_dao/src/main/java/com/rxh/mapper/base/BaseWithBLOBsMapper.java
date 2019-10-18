package com.rxh.mapper.base;

import com.rxh.pojo.merchant.MerchantQuestion;
import com.rxh.pojo.merchant.MerchantQuestionWithBLOBs;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/10/17
 * Time: 16:05
 * Project: Management
 * Package: com.rxh.mapper.base
 */
public interface BaseWithBLOBsMapper<T, TWB extends T , PK> {
    int deleteByPrimaryKey(PK id);

    int insert(TWB record);

    int insertSelective(TWB record);

    TWB selectByPrimaryKey(PK id);

    int updateByPrimaryKeySelective(TWB record);

    int updateByPrimaryKeyWithBLOBs(TWB record);

    int updateByPrimaryKey(T record);
}
