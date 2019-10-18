package com.rxh.service.impl.merchant;

import com.rxh.mapper.square.MerchantRegisterInfoMapper;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.service.merchant.MerchantRegisterInfoService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.MerchantRegisterInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MerchantRegisterInfoServiceImpl implements MerchantRegisterInfoService {

    @Autowired
    private MerchantRegisterInfoMapper merchantRegisterInfoMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_info")
    @Override
    public int deleteByExample(MerchantRegisterInfo example) {
        MerchantRegisterInfoExample merchantRegisterInfoExample=new MerchantRegisterInfoExample();

        if(!StringUtils.isEmpty(example.getMerId())){
            merchantRegisterInfoExample.createCriteria().andMerIdEqualTo(example.getMerId());
        }
        if(!StringUtils.isEmpty(example.getTerminalMerId())){
            merchantRegisterInfoExample.createCriteria().andTerminalMerIdEqualTo(example.getTerminalMerId());
        }
        return merchantRegisterInfoMapper.deleteByExample(merchantRegisterInfoExample);
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_info")
    @Override
    public int deleteByMerIdAndTerminalMerId(MerchantRegisterInfo record) {
        return merchantRegisterInfoMapper.deleteByMerIdAndTerminalMerId(record.getMerId(),record.getTerminalMerId());
    }
    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_info")
    @Override
    public int insert(MerchantRegisterInfo record) {
        return merchantRegisterInfoMapper.insert(record);
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_register_info")
    @Override
    public int updateByMerId(MerchantRegisterInfo record) {
        return merchantRegisterInfoMapper.updateByMerId(record);
    }

    @Override
    public List<MerchantRegisterInfo> selectByExample(MerchantRegisterInfo example) {
        MerchantRegisterInfoExample merchantRegisterInfoExample=new MerchantRegisterInfoExample();

        if(!StringUtils.isEmpty(example.getTerminalMerId())){
            merchantRegisterInfoExample.createCriteria().andTerminalMerIdEqualTo(example.getTerminalMerId());
        }

        if(!StringUtils.isEmpty(example.getMerId())){
            merchantRegisterInfoExample.createCriteria().andMerIdEqualTo(example.getMerId());
        }

        return merchantRegisterInfoMapper.selectByExample(merchantRegisterInfoExample);
    }

    @Override
    public MerchantRegisterInfo selectByMerIdAndTerminalMerId(MerchantRegisterInfo example) {
        return merchantRegisterInfoMapper.getMerchantRegisterInfoByMerIdAndTerminalMerId(example.getMerId(),example.getTerminalMerId());
    }

    @Override
    public  List<MerchantRegisterInfo> selecAll(){
        return merchantRegisterInfoMapper.selectAll();
    }
}
