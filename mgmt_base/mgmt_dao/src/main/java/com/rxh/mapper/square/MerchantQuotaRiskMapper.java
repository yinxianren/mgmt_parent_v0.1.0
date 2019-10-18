package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantQuotaRisk;
import com.rxh.square.pojo.MerchantQuotaRiskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantQuotaRiskMapper {
    int countByExample(MerchantQuotaRiskExample example);

    int deleteByExample(MerchantQuotaRiskExample example);

    int insert(MerchantQuotaRisk record);

    int insertSelective(MerchantQuotaRisk record);

    List<MerchantQuotaRisk> selectByExample(MerchantQuotaRiskExample example);

    int updateByExampleSelective(@Param("record") MerchantQuotaRisk record, @Param("example") MerchantQuotaRiskExample example);

    int updateByExample(@Param("record") MerchantQuotaRisk record, @Param("example") MerchantQuotaRiskExample example);

    int update(MerchantQuotaRisk param);

    int deleteByPrimaryKey(String merId);

    MerchantQuotaRisk search(String merId);

    List<MerchantQuotaRisk> getAll();
}
