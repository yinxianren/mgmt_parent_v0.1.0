package com.rxh.mapper.square;

import com.rxh.pojo.merchant.Merchant;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.MerchantInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantSquareInfoMapper {
    int countByExample(MerchantInfoExample example);

    int deleteByExample(MerchantInfoExample example);

    int deleteByPrimaryKey(String merId);

    int insert(MerchantInfo record);

    int insertSelective(MerchantInfo record);

    List<MerchantInfo> selectByExample(MerchantInfoExample example);

    MerchantInfo selectByPrimaryKey(String merId);

    int updateByExampleSelective(@Param("record") MerchantInfo record, @Param("example") MerchantInfoExample example);

    int updateByExample(@Param("record") MerchantInfo record, @Param("example") MerchantInfoExample example);

    int updateByPrimaryKeySelective(MerchantInfo record);

    int updateByPrimaryKey(MerchantInfo record);

    List<MerchantInfo> selectByParam(MerchantInfo merchantInfo);

    List<MerchantInfo> getAll();

    List<MerchantInfo> getIdsAndName();

    List<MerchantInfo> getAllByMerchantId(String merchantId);

    List<MerchantInfo> getMerchantsByParentId(String merchantId);

    MerchantInfo getMerchantName(String merId);

    List<MerchantInfo> getAllmerId();

}