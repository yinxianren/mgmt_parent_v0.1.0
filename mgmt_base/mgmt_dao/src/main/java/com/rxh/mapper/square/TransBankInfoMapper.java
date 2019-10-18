package com.rxh.mapper.square;

import com.rxh.square.pojo.TransBankInfo;
import com.rxh.square.pojo.TransBankInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransBankInfoMapper {
    int countByExample(TransBankInfoExample example);

    int deleteByExample(TransBankInfoExample example);

    int deleteByPrimaryKey(String transId);

    int insert(TransBankInfo record);

    int insertSelective(TransBankInfo record);

    List<TransBankInfo> selectByExample(TransBankInfoExample example);

    TransBankInfo selectByPrimaryKey(String transId);

    int updateByExampleSelective(@Param("record") TransBankInfo record, @Param("example") TransBankInfoExample example);

    int updateByExample(@Param("record") TransBankInfo record, @Param("example") TransBankInfoExample example);

    int updateByPrimaryKeySelective(TransBankInfo record);

    int updateByPrimaryKey(TransBankInfo record);

    TransBankInfo getTransBankInfo(String transId);
}