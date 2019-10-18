package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantQuotaRiskMapper;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantQuotaRiskService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.MerchantQuotaRisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MerchantQuotaRiskServiceImpl implements MerchantQuotaRiskService {
    @Autowired
    private MerchantQuotaRiskMapper merchantQuotaRiskMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_quota_risk")
    @Override
    public Result insert(MerchantQuotaRisk record) {
        Result result=new Result<String>();
//        record.setMerId();
//        merchantQuotaRiskMapper
        int i = merchantQuotaRiskMapper.insert(record);
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("插入商户风控成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("插入商户风控失败");
        }
        return null;
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_quota_risk")
    @Override
    public Result delete(MerchantQuotaRisk record) {
        return null;
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_quota_risk")
    @Override
    public Result update(MerchantQuotaRisk record) {
        Result result=new Result<String>();
        MerchantQuotaRisk risk= merchantQuotaRiskMapper.search(record.getMerId());
        int i=0;
        if (risk==null){
            i=  merchantQuotaRiskMapper.insertSelective(record);
        }else {
            i = merchantQuotaRiskMapper.update(record);
        }
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("更新商户风控成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("更新商户风控失败");
        }
        return result;
    }

    @Override
    public Result search(String merId) {
        Result<MerchantQuotaRisk> result = new Result<>();
        MerchantQuotaRisk risk= merchantQuotaRiskMapper.search(merId);
        if(risk==null){
            risk=new MerchantQuotaRisk();
            risk.setMerId(merId);
        }
        if (risk!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取商户风控信息成功");
            result.setData(risk);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取商户风控信息失败");
        }
        return result;
    }

    @Override
    public List<MerchantQuotaRisk> getAll() {
        return merchantQuotaRiskMapper.getAll();
    }
}
