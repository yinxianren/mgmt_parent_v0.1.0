package com.rxh.service.impl;

import com.rxh.mapper.square.ExtraChannelInfoMapper;
import com.rxh.pojo.Result;
import com.rxh.service.square.ExtraChannelInfoService;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.ExtraChannelInfoExample;
import com.rxh.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExtraChannelInfoServiceImpl implements ExtraChannelInfoService {

    @Autowired
    ExtraChannelInfoMapper extraChannelInfoMapper;

    @RedisCacheDeleteByHashKey(hashKey = "extra_channel_info")
    @Override
    public Result insert(ExtraChannelInfo record) {
        Result result = new Result();
        record.setId(UUID.createKey("extra_channel_info",""));
        record.setCrateTime(new Date());
        String id = UUID.createNumber("EX",extraChannelInfoMapper.getMaxExtraChannelId());
        record.setExtraChannelId(id);
        ExtraChannelInfo extraChannelInfo = checkExist(record);
        if (extraChannelInfo != null){
            result.setMsg("附属通道类型已经存在！");
            result.setCode(Result.FAIL);
            return result;
        }
        int a = extraChannelInfoMapper.insert(record);
        result.setMsg("附属通道新增成功！");
        result.setCode(Result.SUCCESS);
        result.setData(a);
        return result;
    }

    /**
     * 验证附属通道类型是否存在
     * @param record
     * @return
     */
    private ExtraChannelInfo checkExist(ExtraChannelInfo record) {
        List<ExtraChannelInfo> extraChannelInfos = getAll();
        return  extraChannelInfos
                .stream()
                .filter(extraChannelInfo -> (extraChannelInfo.getType().equals(record.getType()) && extraChannelInfo.getOrganizationId().equals(record.getOrganizationId())) )
                .findFirst()
                .orElse(null);
    }

    //    @RedisCacheUpdate(hashKey = "extra_channel_info",keyName = "#extraChannelId")
    @RedisCacheDeleteByHashKey(hashKey = "extra_channel_info")
    @Override
    public int insertSelective(ExtraChannelInfo record) {
        return extraChannelInfoMapper.insertSelective(record);
    }

    //    @RedisCacheDelete(hashKey = "extra_channel_info",keyName = "#extraChannelId")
    @RedisCacheDeleteByHashKey(hashKey = "extra_channel_info")
    @Override
    public Result update(ExtraChannelInfo record) {
        Result result = new Result();
        ExtraChannelInfo extraChannelInfo = checkExist(record);
        if (extraChannelInfo!= null && !extraChannelInfo.getId().equals(record.getId())){
            result.setMsg("修改失败：附属通道类型已经存在！");
            result.setCode(Result.FAIL);
            return result;
        }
        int  a = extraChannelInfoMapper.update(record);
        result.setMsg("附属通道修改成功！");
        result.setCode(Result.SUCCESS);
        result.setData(a);
        return result;
    }

    @RedisCacheDeleteByHashKey(hashKey = "extra_channel_info")
    @Override
    public int delete(List<String> ids) {
        int i=0;
        for (String id : ids) {
            extraChannelInfoMapper.delete(id);
            i++;
        }
        return i==ids.size()?i:0;
    }

    @Override
    public ExtraChannelInfo search(ExtraChannelInfo record) {
        return extraChannelInfoMapper.search(record);
    }

    @Override
    public List<ExtraChannelInfo> select(ExtraChannelInfo record) {
        return extraChannelInfoMapper.select(record);
    }

    @Override
    public List<ExtraChannelInfo> getAll() {
        ExtraChannelInfoExample example=new ExtraChannelInfoExample();
        List<ExtraChannelInfo> extraChannelInfos = extraChannelInfoMapper.selectByExample(example);
        return extraChannelInfos;
    }

    @RedisCacheDeleteByHashKey(hashKey = "extra_channel_info")
    @Override
    public boolean deleteByPrimaryKey(List<String> ids) {
        int a=0;
        for (String id : ids) {
            extraChannelInfoMapper.deleteByPrimaryKey(id);
            a++;
        }
        return a==ids.size();
    }


    @Override
    public List<ExtraChannelInfo> getChannelInfosByPayType(Integer bizType) {
        return extraChannelInfoMapper.getChannelInfosByPayType(bizType);
    }
}
