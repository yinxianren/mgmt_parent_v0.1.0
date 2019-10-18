package com.rxh.service.impl;

import com.rxh.mapper.square.*;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.*;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelInfoServiceImpl implements ChannelInfoService {

    @Resource
    private ChannelInfoMapper channelInfoMapper;
    @Resource
    private OrganizationInfoMapper organizationInfoMapper;
    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private TransOrderMapper transOrderMapper;
    @Resource
    private ConstantService constantService;
    @Autowired
    private MerchantSquareSettingMapper MerchantSquareSettingMapper;


    //    @RedisCacheDeleteByBatch(hashKey = "channel_info" , keyType=2, del0bjectIndex = 0)
    @RedisCacheDeleteByHashKey(hashKey = "channel_info")
    @Override
    public boolean deleteByPrimaryKey(String[] arrayStr) {
        List<String> idList= Arrays.asList(arrayStr);
        for (String id : idList) {
            List<PayOrder> payOrders=  payOrderMapper.countByChannelId(id);
            if (payOrders.size()>0){
                return false;
            }
            List<TransOrder> transOrders=transOrderMapper.countByChannelId(id);
            if (transOrders.size()>0){
                return false;
            }
        }

        int a=0;
        for (String id : idList) {
            channelInfoMapper.deleteByPrimaryKey(id);
            a++;
        }
        return a==idList.size();
    }


    //    @RedisCacheDelete(hashKey = "channel_info",keyName = "#channelId",keyIndex = 0 )
    @RedisCacheDeleteByHashKey(hashKey = "channel_info")
    @Override
    public int insert(ChannelInfo record) {

        initChannelName(record);
        /* int id = UUID.createIntegerKey(channelInfoMapper.getMaxId());*/
        String id = UUID.createNumber("CH",channelInfoMapper.getMaxId());
        record.setChannelId(id);
        record.setCreateTime(new Date());
        int i = channelInfoMapper.insert(record);
        return i;
    }

    void initChannelName(ChannelInfo record) {
        String organizationId = record.getOrganizationId();
        OrganizationInfo organizationInfo = organizationInfoMapper.selectByPrimaryKey(organizationId);
        String channelTransCode = record.getChannelTransCode() != null ? "("+record.getChannelTransCode()+")" : "";
        String type = record.getType().toString();
        List<SysConstant> payTypes = constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE);
        SysConstant payType = payTypes.stream().filter(sysConstant -> sysConstant.getFirstValue().equals(type)).findFirst().get();
        String channelName=organizationInfo.getOrganizationName()+payType.getName()+channelTransCode;
        record.setChannelName(channelName);
    }

    @Override
    public List<ChannelInfo> selectByExample(ChannelInfo record) {
        ChannelInfoExample example=new ChannelInfoExample();
        ChannelInfoExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(record.getChannelId())){
            criteria.andChannelIdEqualTo(record.getChannelId());
        }
        if (record.getChannelName()!=null){
            criteria.andChannelNameLike(record.getChannelName());
        }
        if (record.getCreateTime()!=null){
            criteria.andCreateTimeGreaterThanOrEqualTo(record.getCreateTime());
        }
        if (record.getOrganizationId()!=null){
            criteria.andOrganizationIdEqualTo(record.getOrganizationId());
        }
        if (record.getChannelLevel()!=null){
            criteria.andChannelLevelEqualTo(record.getChannelLevel());
        }
        if (record.getType()!=null){
            criteria.andTypeEqualTo(record.getType());
        }

        List<ChannelInfo> channelInfos = channelInfoMapper.selectByExample(example);
        return channelInfos;
    }


    //    @RedisCacheDelete(hashKey = "channel_info",keyName = "#channelId",keyIndex = 0 )
    @RedisCacheDeleteByHashKey(hashKey = "channel_info")
    @Override
    public int updateByPrimaryKeySelective(ChannelInfo record) {
        initChannelName(record);
        // 1 支付方式是否从代付 变成 非代付
        ChannelInfo info = channelInfoMapper.selectByPrimaryKey(record.getChannelId());
        if (4 == info.getType() && 4 != record.getType()){
            // 1.1更新支付通道下，支付方式为代付的通道。
            record.setOutChannelId("");
            List<ChannelInfo> channelInfos = channelInfoMapper.selectByExample(null);
            updateChannelInfoByType(channelInfos,record);
        }
        int i  = channelInfoMapper.updateByPrimaryKeySelective(record);
        return i;
    }

    @Override
    public List<ChannelInfo> selectByGroupOrganizationId() {
        return channelInfoMapper.selectByGroupOrganizationId();
    }

    @RedisCacheDeleteByHashKey(hashKey = "channel_info")
    @Override
    public int updateOthersInfo(String Others, String channelId) {
        return channelInfoMapper.updateOthersInfo(Others,channelId);
    }

    @Override
    public ChannelInfo selectByChannelId(String channelId) {
        return channelInfoMapper.selectByChannelId(channelId);
    }

    @Override
    public List<ChannelInfo> selectByWhereCondition(ChannelInfo record){
        return channelInfoMapper.selectByWhereCondition(record);
    }


    @Override
    public List<ChannelInfo> selectByMerId(String merId) {
        MerchantSetting result = MerchantSquareSettingMapper.selectByMerId(merId);
        List<ChannelInfo> channelInfos = getAll();
        List<ChannelInfo> channelInfoList = new ArrayList<>();
        if (result == null || result.getChannelId() == null){
            for (ChannelInfo channelInfo:channelInfos){
                ChannelInfo channelInfo1 = new ChannelInfo();
                channelInfo1.setChannelId(channelInfo.getChannelId());
                channelInfo1.setChannelName(channelInfo.getChannelName());
                channelInfo1.setStatus(channelInfo.getStatus());
                channelInfoList.add(channelInfo1);
            }
        }else {
            for (ChannelInfo channelInfo:channelInfos){
                if(result.getChannelId().contains(channelInfo.getChannelId())){
                    ChannelInfo channelInfo1 = new ChannelInfo();
                    channelInfo1.setChannelId(channelInfo.getChannelId());
                    channelInfo1.setChannelName(channelInfo.getChannelName());
                    channelInfo1.setStatus(channelInfo.getStatus());
                    channelInfoList.add(channelInfo1);
                }
            }
        }
        return channelInfoList;
    }

    private int updateChannelInfoByType(List<ChannelInfo> channelInfos,ChannelInfo record) {
        List<ChannelInfo> channelInfoList = channelInfos.stream()
                .filter(channelInfo -> record.getChannelId().equals(channelInfo.getOutChannelId()))
                .collect(Collectors.toList());
        if (channelInfoList.size()>0 && channelInfoList != null){
            int i =0;
            for (ChannelInfo channelInfo : channelInfoList){
                channelInfo.setOutChannelId("");
                channelInfoMapper.updateByPrimaryKeySelective(channelInfo);
                i++;
            }
            return i;
        }
        return  0;
    }


    @Override
    public List<ChannelInfo> getAll() {
        ChannelInfoExample example=new ChannelInfoExample();
        List<ChannelInfo> channelInfos = channelInfoMapper.selectByExample(example);
        return channelInfos;
    }

}
