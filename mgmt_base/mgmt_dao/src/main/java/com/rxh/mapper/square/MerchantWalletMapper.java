package com.rxh.mapper.square;

import com.rxh.square.pojo.*;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MerchantWalletMapper {
    int countByExample(MerchantWalletExample example);

    int deleteByExample(MerchantWalletExample example);

    int deleteByPrimaryKey(String merId);

    int insert(MerchantWallet record);

    int insertSelective(MerchantWallet record);

    List<MerchantWallet> selectByExample(MerchantWalletExample example);

    MerchantWallet selectByPrimaryKey(String merId);

    int updateByExampleSelective(@Param("record") MerchantWallet record, @Param("example") MerchantWalletExample example);

    int updateByExample(@Param("record") MerchantWallet record, @Param("example") MerchantWalletExample example);

    int updateByPrimaryKeySelective(MerchantWallet record);

    int updateByPrimaryKey(MerchantWallet record);


    List<MerchantWallet> searchByParam(MerchantInfo param);

    String getMerchantIdIncre();

    String getMaxMerId();

    List<MerchantWallet> search(@Param("merId") String merId);
}