package com.rxh.cache.redis;


import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.ExtraChannelInfoService;
import com.rxh.square.pojo.ExtraChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class ExtraChannelInfoCache extends AbstractPayCache {

    private final String tableMapKeyName="extra_channel_info";

    @Autowired
    protected ExtraChannelInfoService extraChannelInfoService;

    /**
     *
     * @param ExtraChannelId
     * @return
     */
    public ExtraChannelInfo getOne(String ExtraChannelId){
        ExtraChannelInfo  extraChannelInfo= (ExtraChannelInfo) redisTemplate.opsForHash().get(tableMapKeyName,ExtraChannelId);
        if(isNull(extraChannelInfo)){
            List<ExtraChannelInfo>  extraChannelInfoList= extraChannelInfoService.getAll();
            if(isHasElement(extraChannelInfoList)){
                Map<Object,Object> map= new HashMap<>();
                for(ExtraChannelInfo exhil : extraChannelInfoList){
                    if(exhil.getExtraChannelId().equals(ExtraChannelId)){
                        extraChannelInfo=exhil;
                    }
                    map.put(exhil.getExtraChannelId(),exhil);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return extraChannelInfo;
    }

    /**
     *
     * @return
     */
    public List<ExtraChannelInfo> getAll(){
        List<ExtraChannelInfo>  extraChannelInfoList=redisTemplate.opsForHash()
                .values(tableMapKeyName)
                .stream()
                .map(obj->(ExtraChannelInfo)obj)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(extraChannelInfoList)){
            extraChannelInfoList= extraChannelInfoService.getAll();
            if(isHasElement(extraChannelInfoList)){
                Map<Object,Object> map= new HashMap<>(extraChannelInfoList.size());
                for(ExtraChannelInfo exhil : extraChannelInfoList){
                    map.put(exhil.getExtraChannelId(),exhil);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return extraChannelInfoList;
    }




}
