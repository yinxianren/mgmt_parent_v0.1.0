package com.rxh.service.impl;

import com.rxh.mapper.square.ChannelInfoMapper;
import com.rxh.mapper.square.MerchantSquareSettingMapper;
import com.rxh.pojo.Result;
import com.rxh.service.square.MerchantSquareSettingService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class MerchantSquareSettingServiceImpl implements MerchantSquareSettingService {

    @Autowired
    private MerchantSquareSettingMapper merchantSquareSettingMapper;

    @Resource
    private ChannelInfoMapper channelInfoMapper;

    @RedisCacheDeleteByHashKey(hashKey = "merchant_setting")
    @Override
    public Result insert(MerchantSetting record) {
        Result<String> result = new Result<>();
        int i = merchantSquareSettingMapper.insert(record);
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("新增商户配置成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("新增商户配置失败");
        }
        return result;

    }

    @Override
    public Result delete(String id) {
        return null;
    }

    @RedisCacheDeleteByHashKey(hashKey = "merchant_setting")
    @Override
    public Result update(MerchantSetting record) {
        Result<String> result = new Result<>();
        int i = merchantSquareSettingMapper.updateByPrimaryKeySelective(record);
        if(i>0){
            result.setCode(Result.SUCCESS);
            result.setMsg("修改商户配置成功");
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("修改商户配置失败");
        }
        return result;
    }

    @Override
    public Result search(String merId) {
        Result<MerchantSetting> result = new Result<>();

        MerchantSetting merchantSetting = merchantSquareSettingMapper.selectByMerId(merId);
        if(merchantSetting!=null){
            result.setCode(Result.SUCCESS);
            result.setMsg("获取商户配置成功");
            result.setData(merchantSetting);
        }else {
            result.setCode(Result.FAIL);
            result.setMsg("获取商户配置失败");
        }
        return result;
    }


    @Override
    public Result merSearch(String merId) {
        HashMap<String, Object> map = new HashMap<>();
        Result< HashMap<String, Object>> result = new Result<>();
        MerchantSetting merchantSetting = merchantSquareSettingMapper.selectMerByMerId(merId);
        map.put("merchantSetting",merchantSetting);
        String channelId = merchantSetting.getChannelId();
        List<String> ids = new ArrayList<>();
        if (channelId.contains(",")){
            String[] idarr = channelId.split(",");
            ids.addAll(Arrays.asList(idarr));
        }else {
            ids.add(channelId);
        }
        List<ChannelInfo>  channelInfos= channelInfoMapper.getChannelById(ids);
        map.put("channels",channelInfos);
        List<ChannelInfo>  payType = channelInfoMapper.getType(ids);
        map.put("payType",payType);
        result.setCode(Result.SUCCESS);
        result.setMsg("获取商户配置成功");
        result.setData(map);
        return result;
    }


    @Override
    public List<MerchantSetting> getAll() {
        return merchantSquareSettingMapper.selectByExample(null);
    }


}
