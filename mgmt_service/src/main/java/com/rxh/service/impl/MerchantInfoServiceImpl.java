package com.rxh.service.impl;

import com.rxh.mapper.square.*;
import com.rxh.pojo.Result;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.MerchantUserService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.MerchantAcount;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.pojo.MerchantSetting;
import com.rxh.square.pojo.MerchantWallet;
import com.rxh.utils.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {
    @Resource
    MerchantSquareInfoMapper merchantSquareInfoMapper;
    @Resource
    MerchantAcountMapper merchantAcountMapper;
    @Resource
    MerchantQuotaRiskMapper merchantQuotaRiskMapper;
    @Resource
    MerchantSquareSettingMapper merchantSquareSettingMapper;
    @Resource
    MerchantUserService merchantUserService;
    @Resource
    MerchantWalletMapper merchantWalletMapper;
    @Resource
    MerchantSquareRateMapper merchantSquareRateMapper;

    @Resource
    PayOrderMapper payOrderMapper;
    @Resource
    TransOrderMapper transOrderMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_info")
    @Override
    public synchronized Result insert(MerchantInfo record ,String username) {
        Result<String> result = new Result<>();
        String maxMerId = merchantWalletMapper.getMaxMerId();
        String merId = UUID.createNumber("M",maxMerId);
        record.setMerId(merId);
        record.setCreateTime(new Date());
        int i = merchantSquareInfoMapper.insertSelective(record);
        saveAgentMerchantSetting(record,username);
        if (i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("新增商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("新增商户失败");
        }
        return result;
    }

    private void saveAgentMerchantSetting(MerchantInfo merchantInfo,String username) {
        String merId = merchantInfo.getMerId();
        MerchantAcount merchantAcount = new MerchantAcount();
//        MerchantQuotaRisk merchantQuotaRisk = new MerchantQuotaRisk();
        MerchantSetting merchantSetting = new MerchantSetting();
        MerchantWallet merchantWallet=new MerchantWallet();
        Long settingId = UUID.createKey("merchan_setting");
        merchantAcount.setMerId(merId);
//        merchantQuotaRisk.setMerId(merId);
        merchantSetting.setMerId(merId);
        merchantSetting.setId(settingId.toString());

        merchantWallet.setMerId(merchantInfo.getMerId());
        merchantWallet.setUpdateTime(new Date());

//        merchantAcountMapper.insert(merchantAcount);
//        merchantQuotaRiskMapper.insert(merchantQuotaRisk);
        merchantSquareSettingMapper.insert(merchantSetting);
//        merchantWalletMapper.insert(merchantWallet);

        MerchantUser merchantUser=new MerchantUser();
        merchantUser.setUserName(merchantInfo.getLoginName());
        merchantUser.setPassword(merchantInfo.getPassword());

        merchantUserService.addAdminRole(merchantInfo.getMerId(),username,merchantUser);

    }

    //    @RedisCacheDeleteByBatch(hashKey = {"merchant_info"},keyType=1)
    @RedisCacheDeleteByHashKey(hashKey = "merchant_info")
    @Override
    @Transactional
    public Result deleteByPrimaryKey(String[] array) {
        List<String> merIds=Arrays.asList(array);
        int i=0;
        Result<String> result = new Result<>();
        for (String merId : merIds) {
            Integer payNum=payOrderMapper.countByMerId(merId);
            Integer transNum=transOrderMapper.countByMerId(merId);
            if (payNum>0||transNum>0){
                result.setCode(Result.FAIL);
                result.setMsg("删除商户失败:"+merId+" 存在订单");
                return result;
            }
        }
        for (String merId : merIds) {
            // 商户表
            merchantSquareInfoMapper.deleteByPrimaryKey(merId);
            // 商户结算账号信息表
            merchantAcountMapper.deleteByPrimaryKey(merId);
            // 商户钱包
            merchantWalletMapper.deleteByPrimaryKey(merId);
            // 商户风控
            merchantQuotaRiskMapper.deleteByPrimaryKey(merId);
            // 商户配置
            merchantSquareSettingMapper.deleteByMerId(merId);
            // 商户费率
            merchantSquareRateMapper.deleteByMerId(merId);
            i++;
        }
        if(i==merIds.size()){
            result.setCode(Result.SUCCESS);
            result.setMsg("删除商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("删除商户失败");
        }
        return result;
    }

    //    @RedisCacheUpdate(hashKey = "merchant_info",keyName = "#merId",updateObjectIndex = 0)
    @RedisCacheDeleteByHashKey(hashKey = "merchant_info")
    @Override
    public Result update(MerchantInfo record, String name) {
        Result<String> result = new Result<>();
        int i = merchantSquareInfoMapper.updateByPrimaryKey(record);
        if (i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("修改商户成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("修改商户失败");
        }

        return result;
    }


    @Override
    public Result search(MerchantInfo merchantInfo) {
        Result<List> result = new Result<>();
        List<MerchantInfo> list = merchantSquareInfoMapper.selectByParam(merchantInfo);
        if (list!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(list);
        }else {
            result.setCode(Result.SUCCESS);
            result.setMsg("查询失败");
        }
        return result;
    }

    @Override
    public Result getAll() {
        Result<List> result = new Result<>();
        List<MerchantInfo> list = merchantSquareInfoMapper.getAll();
        if (list!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(list);
        }else {
            result.setCode(Result.SUCCESS);
            result.setMsg("查询失败");
        }

        return result;
    }

    @Override
    public Result getAllByMerchantId(String merchantId) {
        Result<List> result = new Result<>();
        List<MerchantInfo> list = merchantSquareInfoMapper.getAllByMerchantId(merchantId);
        if (list!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(list);
        }else {
            result.setCode(Result.SUCCESS);
            result.setMsg("查询失败");
        }

        return result;
    }
    @Override
    public MerchantInfo selectByMerId(String merId){
        return merchantSquareInfoMapper.getMerchantName(merId);
    }

    @Override
    public Result getMerchants(String merchantId) {
        Result<List> result = new Result<>();
        List<MerchantInfo> list= merchantSquareInfoMapper.getMerchantsByParentId(merchantId.toString());
        if (list!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(list);
        }else {
            result.setCode(Result.SUCCESS);
            result.setMsg("查询失败");
        }
        return result;
    }

    @Override
    public List<MerchantInfo> getIdsAndName() {
        return merchantSquareInfoMapper.getIdsAndName();
    }

    @Override
    public MerchantInfo getMerchantById(String merchantId) {
        return merchantSquareInfoMapper.selectByPrimaryKey(merchantId);
    }


    @Override
    public Result getAllByAgentMerchantId(String agentMerchantId) {
        Result<List> result = new Result<>();
        List<MerchantInfo> list= merchantSquareInfoMapper.getMerchantsByParentId(agentMerchantId);
        if (list!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("查询成功");
            result.setData(list);
        }else {
            result.setCode(Result.SUCCESS);
            result.setMsg("查询失败");
        }
        return result;
    }

    @Override
    public List<MerchantInfo>  getAllmerId() {
        return merchantSquareInfoMapper.getAllmerId();
    }

}
