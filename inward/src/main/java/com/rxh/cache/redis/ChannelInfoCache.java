package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.square.pojo.ChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class ChannelInfoCache extends AbstractPayCache {

    private final String tableMapKeyName="channel_info";
    @Autowired
    private ChannelInfoService channelInfoService;

    /**
     *
     * @param channelId
     * @return
     */
    public ChannelInfo getOne(String channelId){
        ChannelInfo  channelInfo= (ChannelInfo) redisTemplate.opsForHash().get(tableMapKeyName,channelId);
        if(isNull(channelInfo)){
            List<ChannelInfo> channelInfoList= channelInfoService.getAll();
            if(isHasElement(channelInfoList)){
                Map<Object,Object> map= new HashMap<>(channelInfoList.size());
                for(ChannelInfo  ci:channelInfoList){
                    if(ci.getChannelId().equals(channelId)){
                        channelInfo=ci;
                    }
                    map.put(ci.getChannelId(),ci);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return channelInfo;
    }

    /**
     *
     * @return
     *  new ArrayList(redisTemplate.opsForHash().multiGet(tableMapKeyName,redisTemplate.opsForHash().keys(tableMapKeyName)));
     */
    public List<ChannelInfo> getAll(){
        List<ChannelInfo> channelInfoList=redisTemplate.opsForHash()
                .values(tableMapKeyName)
                .stream()
                .map(obj->(ChannelInfo)obj)
                .distinct()
                .collect(Collectors.toList());

        if(!isHasElement(channelInfoList)){
            channelInfoList= channelInfoService.getAll();
            if(isHasElement(channelInfoList)){
                Map<Object,Object> map= new HashMap<>(channelInfoList.size());
                for(ChannelInfo  ci:channelInfoList){
                    map.put(ci.getChannelId(),ci);
                }
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return channelInfoList;
    }

}
