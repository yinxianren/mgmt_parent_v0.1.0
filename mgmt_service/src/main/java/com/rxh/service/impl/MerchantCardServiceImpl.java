package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantCardMapper;
import com.rxh.service.square.MerchantCardService;
import com.rxh.spring.annotation.RedisCacheDelete;
import com.rxh.square.pojo.MerchantCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantCardServiceImpl implements MerchantCardService {

    @Autowired
    MerchantCardMapper merchantCardMapper;

    @RedisCacheDelete(hashKey="merchant_card",keyNameArry={"#merId","#terminalMerId"},keyIndex = 0)
    @Override
    public int insert(MerchantCard record) {
        return merchantCardMapper.insert(record);
    }

    @RedisCacheDelete(hashKey="merchant_card",keyNameArry={"#merId","#terminalMerId"},keyIndex = 0)
    @Override
    public int insertSelective(MerchantCard record) {
        return merchantCardMapper.insertSelective(record);
    }

    @RedisCacheDelete(hashKey="merchant_card",keyNameArry={"#merId","#terminalMerId"},keyIndex = 0)
    @Override
    public int update(MerchantCard record) {
        return merchantCardMapper.update(record);
    }


    @Override
    public MerchantCard search(MerchantCard record) {
        return merchantCardMapper.search(record);
    }

    @Override
    public List<MerchantCard> select(MerchantCard record) {
        return merchantCardMapper.select(record);
    }

    @Override
    public MerchantCard selectById(String id) {
        return merchantCardMapper.selectById(id);
    }
}
