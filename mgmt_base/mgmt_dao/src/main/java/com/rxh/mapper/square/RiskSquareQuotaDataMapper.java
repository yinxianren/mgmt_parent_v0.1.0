package com.rxh.mapper.square;

import com.rxh.square.pojo.RiskQuotaData;
import com.rxh.square.pojo.RiskQuotaDataExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiskSquareQuotaDataMapper {
    int countByExample(RiskQuotaDataExample example);

    int deleteByExample(RiskQuotaDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RiskQuotaData record);

    int insertSelective(RiskQuotaData record);

    List<RiskQuotaData> selectByExample(RiskQuotaDataExample example);

    List<RiskQuotaData> selectRiskQuotaDataByWhereCondition(RiskQuotaData riskQuotaData);

    Integer selectMaxId();

    RiskQuotaData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RiskQuotaData record, @Param("example") RiskQuotaDataExample example);

    int updateByExample(@Param("record") RiskQuotaData record, @Param("example") RiskQuotaDataExample example);

    int updateByPrimaryKeySelective(RiskQuotaData record);

    int updateByPrimaryKey(RiskQuotaData record);

    Integer insertOrUpdateRiskQuotaData(List<RiskQuotaData> quotaDataList);

    Integer insertRiskQuotaData(List<RiskQuotaData> quotaDataList);
}