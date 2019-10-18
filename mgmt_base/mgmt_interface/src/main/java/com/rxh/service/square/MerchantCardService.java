package com.rxh.service.square;

import com.rxh.square.pojo.MerchantCard;

import java.util.List;

public interface MerchantCardService {

    int insert(MerchantCard record);

    int insertSelective(MerchantCard record);

    int update(MerchantCard record);

    MerchantCard search(MerchantCard record);

    List<MerchantCard> select(MerchantCard record);

    MerchantCard selectById(String id);


}
