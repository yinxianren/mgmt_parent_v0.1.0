package com.rxh.mapper.square;

import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.square.pojo.MerchantRegisterInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchantRegisterInfoMapper {

    int countByExample(MerchantRegisterInfoExample example);

    int deleteByExample(MerchantRegisterInfoExample example);

    int deleteByMerIdAndTerminalMerId(@Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

    int deleteByPrimaryKey(String id);

    int insert(MerchantRegisterInfo record);

    int insertSelective(MerchantRegisterInfo record);

    List<MerchantRegisterInfo> selectByExample(MerchantRegisterInfoExample example);

    List<MerchantRegisterInfo> selectAll();

    MerchantRegisterInfo selectByPrimaryKey(String id);


    int updateByExampleSelective(@Param("record") MerchantRegisterInfo record, @Param("example") MerchantRegisterInfoExample example);

    int updateByExample(@Param("record") MerchantRegisterInfo record, @Param("example") MerchantRegisterInfoExample example);

    int updateByPrimaryKeySelective(MerchantRegisterInfo record);

    int updateByMerId(MerchantRegisterInfo record);

    int updateByPrimaryKey(MerchantRegisterInfo record);

    MerchantRegisterInfo getMerchantRegisterInfoByMerIdAndTerminalMerId(@Param("merId")String merId, @Param("terminalMerId")String terminalMerId);

}