package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantSquareRateMapper;
import com.rxh.mapper.sys.SysConstantMapper;
import com.rxh.pojo.Result;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.square.MerchantSquareRateService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.MerchantRate;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MerchantSquareRateServiceImpl implements MerchantSquareRateService {

    @Autowired
    private MerchantSquareRateMapper merchantSquareRateMapper;
    @Autowired
    private SysConstantMapper sysConstantMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_rate")
    @Override
    public Result insert(MerchantRate record) {
        Result<String> result = new Result<>();
        int i = merchantSquareRateMapper.insert(record);
        if (i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("新增商户费率成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("新增商户费率失败");
        }
        return result;
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_rate")
    @Override
    public Result update(List<MerchantRate> param) {
        //3个对象
        Result<String> result = new Result<String>();
        try {
            for (MerchantRate merchantRate : param) {
                if(merchantRate.getId().length()>0&&merchantRate.getId()!=null){
                    //id不为空代表update
                    merchantSquareRateMapper.updateByPrimaryKey(merchantRate);
                }else if (merchantRate.getId().length()==0&&merchantRate.getStatus()==0){
                    //id为'temp'代表不存在记录 插入状态为通过的数据
                    Long id = UUID.createKey("merchant_rate");
                    merchantRate.setId(id.toString());
                    merchantSquareRateMapper.insert(merchantRate);
                }
            }
            result.setCode(Result.SUCCESS);
            result.setMsg("更新商户费率成功");
        } catch (Exception e) {
            result.setCode(Result.FAIL);
            result.setMsg("更新商户费率失败");
            e.printStackTrace();
        } finally {
            return result;
        }


    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_rate")
    @Override
    public Result delete(String id) {
        return null;
    }

    @Override
    public List<MerchantRate> search(String merId) {
        List<MerchantRate> list=new ArrayList<>();
        List<MerchantRate> result = merchantSquareRateMapper.search(merId);
        List<SysConstant> payTypes = sysConstantMapper.selectByGroupName(SystemConstant.PAYTYPE);
        List<String> resultStr=new ArrayList<>();
        if(result.size()==0){
            for (int i = 0; i < payTypes.size(); i++) {
                MerchantRate merchantRate = new MerchantRate();
                merchantRate.setId("");
                merchantRate.setPayType(payTypes.get(i).getFirstValue());
                merchantRate.setMerId(merId);
                merchantRate.setStatus(1);
                list.add(merchantRate);
            }
            return list;
        }
        for (MerchantRate rate : result) {
            resultStr.add(rate.getPayType());
        }
        for (SysConstant rate : payTypes) {
            if (!resultStr.contains(rate.getFirstValue())){
                MerchantRate merchantRate=new MerchantRate();
                merchantRate.setId("");
                merchantRate.setStatus(1);
                merchantRate.setMerId(merId);
                merchantRate.setPayType(rate.getFirstValue());
                result.add(merchantRate);
            };

        }
        return result;
    }

    @Override
    public List<MerchantRate> merSearch(String merId) {
        List<MerchantRate> result = merchantSquareRateMapper.merSearch(merId);
        System.out.println(result);
        return result;
    }

    @Override
    public List<MerchantRate> getAll() {
        return merchantSquareRateMapper.getAll();
    }
    @Override
    public MerchantRate getMerchantRateByIdAndPayType(String merId,String payType){
        return merchantSquareRateMapper.getMerchantRateByIdAndPayType(merId,payType);
    }
}
