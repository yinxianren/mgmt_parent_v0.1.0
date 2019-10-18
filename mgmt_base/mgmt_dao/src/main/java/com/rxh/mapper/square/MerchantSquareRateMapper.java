package com.rxh.mapper.square;

import com.rxh.square.pojo.MerchantRate;
import com.rxh.square.pojo.MerchantRateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantSquareRateMapper {
    int countByExample(MerchantRateExample example);

    int deleteByExample(MerchantRateExample example);

    int deleteByPrimaryKey(String id);
    int deleteByMerId(String merId);

    int insert(MerchantRate record);

    int insertSelective(MerchantRate record);

    List<MerchantRate> selectByExample(MerchantRateExample example);

    MerchantRate selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") MerchantRate record, @Param("example") MerchantRateExample example);

    int updateByExample(@Param("record") MerchantRate record, @Param("example") MerchantRateExample example);

    int updateByPrimaryKeySelective(MerchantRate record);

    int updateByPrimaryKey(MerchantRate record);

    List<MerchantRate> search(String merId);

    List<MerchantRate> merSearch(String merId);

    List<MerchantRate> getAll();

    MerchantRate getMerchantRate(String merId);

    MerchantRate getMerchantRateByIdAndPayType(@Param("merId") String merId,@Param("payType") String payType);

}
