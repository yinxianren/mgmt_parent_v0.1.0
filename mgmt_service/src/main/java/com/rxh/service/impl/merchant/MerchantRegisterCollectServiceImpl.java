package com.rxh.service.impl.merchant;

import com.rxh.mapper.square.MerchantRegisterCollectMapper;
import com.rxh.service.merchant.MerchantRegisterCollectService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.square.pojo.MerchantRegisterCollectExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
@Service
public class MerchantRegisterCollectServiceImpl implements MerchantRegisterCollectService {

    @Autowired
    private MerchantRegisterCollectMapper merchantRegisterCollectMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_collect")
    @Override
    public int insert(MerchantRegisterCollect record) {
        return merchantRegisterCollectMapper.insert(record);
    }

    @Override
    public List<MerchantRegisterCollect> selectByExample(MerchantRegisterCollect merchantRegisterCollect) {
        MerchantRegisterCollectExample example=new MerchantRegisterCollectExample();

        //商户号
        if(!StringUtils.isEmpty(merchantRegisterCollect.getMerId())){
            example.createCriteria().andMerdEqualTo(merchantRegisterCollect.getMerId());
        }
        //商户订单号
        if( !StringUtils.isEmpty(merchantRegisterCollect.getMerOrderId())){
            example.createCriteria().andMerOrderIdEqualTo(merchantRegisterCollect.getMerOrderId());
        }
        //终端id
        if( !StringUtils.isEmpty(merchantRegisterCollect.getTerminalMerId())){
            example.createCriteria().andTerminalMerIdEqualTo(merchantRegisterCollect.getTerminalMerId());
        }
        //状态
        if( !StringUtils.isEmpty(merchantRegisterCollect.getStatus())){
            example.createCriteria().andStatusEqualTo(merchantRegisterCollect.getStatus());
        }
        return merchantRegisterCollectMapper.selectByExample(example);
    }


    @Override
    public List<MerchantRegisterCollect> selectByMeridAndterminalMerIdAndStatus(MerchantRegisterCollect merchantRegisterCollect) {
        return merchantRegisterCollectMapper.selectByMeridAndterminalMerIdAndStatus(merchantRegisterCollect.getMerId(), merchantRegisterCollect.getTerminalMerId(), merchantRegisterCollect.getStatus());
    }

    @Override
    public List<MerchantRegisterCollect> selectByMerOrderIdAndMerId(MerchantRegisterCollect merchantRegisterCollect) {
        return merchantRegisterCollectMapper.selectByMerOrderIdAndMerId(merchantRegisterCollect.getMerId(),merchantRegisterCollect.getMerOrderId());
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_collect")
    @Override
    public int updateByMerOrderIdAndMerId(MerchantRegisterCollect record) {
        return merchantRegisterCollectMapper.updateByMerOrderIdAndMerId(record);
    }

    @Override
    public List<MerchantRegisterCollect> selectByWhereCondition(MerchantRegisterCollect record) {
        return merchantRegisterCollectMapper.selectByWhereCondition(record);
    }


}
