package com.rxh.cache.ehcache;

import com.rxh.exception.PayException;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 */

//@CacheConfig(cacheNames = "channel_info_square")
@Component
public class ChannelInfoSquareCache implements BaseCache<List<ChannelInfo>, ChannelInfo> {


    @Resource
    ChannelInfoService channelInfoService;



    @Override
//    @Cacheable(key = "'all'")
    public List<ChannelInfo> getCache() {
        return channelInfoService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<ChannelInfo> refreshCache() {
        return channelInfoService.getAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<ChannelInfo> updateCache(ChannelInfo channelInfo) {
        return getCache();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {

    }


    public List<ChannelInfo> getChannelInfosByChannelId(String channelId) throws  PayException{
        List<ChannelInfo> channelInfos = new ArrayList<>();
        String[] strings = channelId.split(",");
        for (String s : strings){
            ChannelInfo channelInfo =
                    getCache()
                    .stream()
                    .filter(channelInfo1 -> channelInfo1.getChannelId().equals(s) && SystemConstant.SQUARE_ENABLE.equals(channelInfo1.getStatus()))
                    .findFirst()
                    .orElse(new ChannelInfo());
            channelInfos.add(channelInfo);
        }
        // 根据通道等级进行排序
        channelInfos.stream()
                .sorted(Comparator.comparing(u -> u.getChannelLevel()));
        return channelInfos;
    }

    public ChannelInfo getChannelInfoByOrganizationInfoId(String organizationId) throws  PayException {
        return getCache()
                .stream()
                .filter(channelInfo -> channelInfo.getOrganizationId().equals(organizationId) && SystemConstant.SQUARE_ENABLE.equals(channelInfo.getStatus()))
                .findFirst()
                .orElseThrow(() -> new PayException("通道不存在或未启用",2005));
    }
}