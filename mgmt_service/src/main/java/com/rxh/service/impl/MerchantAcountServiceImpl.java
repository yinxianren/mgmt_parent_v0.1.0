package com.rxh.service.impl;

import com.rxh.mapper.square.MerchantAcountMapper;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantAcountService;
import com.rxh.square.pojo.MerchantAcount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantAcountServiceImpl implements MerchantAcountService {

    @Autowired
    private MerchantAcountMapper merchantAcountMapper;
    @Override
    public Result insert(MerchantAcount record) {
        Result<String> result = new Result<>();
        int i = merchantAcountMapper.insert(record);
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("增加结算账户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("增加结算账户失败");
        }
        return result;
    }

    @Override
    public Result update(MerchantAcount record) {
        Result<String> result = new Result<>();
        MerchantAcount merchantAcount = merchantAcountMapper.selectByPrimaryKey(record.getMerId());
        int i =0;
        if (merchantAcount==null){
            i = merchantAcountMapper.insertSelective(record);
        }
        else {
         i = merchantAcountMapper.updateByPrimaryKey(record);
        }
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("修改结算账户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("修改结算账户失败");
        }
        return result;
    }

    @Override
    public Result delete(String merId) {
        return null;
    }

    @Override
    public Result search(String merId) {
        Result<MerchantAcount> result = new Result<>();
        MerchantAcount merchantAcount = merchantAcountMapper.selectByPrimaryKey(merId);
        if (merchantAcount==null){
            merchantAcount=new MerchantAcount();
            merchantAcount.setMerId(merId);
        }
        if(merchantAcount!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取结算账户成功");
            result.setData(merchantAcount);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取结算账户失败");
        }
        return  result;
    }
}
