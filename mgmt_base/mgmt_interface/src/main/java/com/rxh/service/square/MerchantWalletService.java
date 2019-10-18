package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.AgentWallet;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.square.pojo.MerchantsDetails;

import java.math.BigDecimal;
import java.util.List;

public interface MerchantWalletService {

    MerchantWallet searchById(String merId);
    List<MerchantWallet> searchByParam(MerchantInfo param);
    Result deleteByPrimaryKey(List<String> merIds);

    String getMerchantIdIncre();
    String getMaxMerId();

    List<MerchantWallet> search(MerchantWallet merchantWallet);

   boolean insert(MerchantWallet merchantWallet);
   int invest(Object investInfo);
   int updateByPrimaryKeySelective(MerchantWallet record);

}
