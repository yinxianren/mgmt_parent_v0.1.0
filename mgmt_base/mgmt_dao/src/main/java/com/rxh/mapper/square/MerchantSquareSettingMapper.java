package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantSetting;
import com.rxh.square.pojo.MerchantSettingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantSquareSettingMapper {
    int countByExample(MerchantSettingExample example);

    int deleteByExample(MerchantSettingExample example);

    int deleteByPrimaryKey(String id);
    int deleteByMerId(String merId);

    int insert(MerchantSetting record);

    int insertSelective(MerchantSetting record);

    List<MerchantSetting> selectByExample(MerchantSettingExample example);

    MerchantSetting selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MerchantSetting record, @Param("example") MerchantSettingExample example);

    int updateByExample(@Param("record") MerchantSetting record, @Param("example") MerchantSettingExample example);

    int updateByPrimaryKeySelective(MerchantSetting record);

    int updateByMerId(MerchantSetting record);

    MerchantSetting selectByMerId(String merId);

    MerchantSetting selectMerByMerId(String merId);



}
