package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantAcount;
import com.rxh.square.pojo.MerchantAcountExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantAcountMapper {
    int countByExample(MerchantAcountExample example);

    int deleteByExample(MerchantAcountExample example);

    int deleteByPrimaryKey(String merId);

    int insert(MerchantAcount record);

    int insertSelective(MerchantAcount record);

    List<MerchantAcount> selectByExample(MerchantAcountExample example);

    MerchantAcount selectByPrimaryKey(String merId);

    int updateByExampleSelective(@Param("record") MerchantAcount record, @Param("example") MerchantAcountExample example);

    int updateByExample(@Param("record") MerchantAcount record, @Param("example") MerchantAcountExample example);

    int updateByPrimaryKeySelective(MerchantAcount record);

    int updateByPrimaryKey(MerchantAcount record);
}