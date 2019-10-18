package com.rxh.cache.ehcache;

import com.rxh.exception.PayException;
import com.rxh.service.square.OrganizationService;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.SystemConstant;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hul
 * Date: 2019/5/24
 */

//@CacheConfig(cacheNames = "organization_info_square")
@Component
public class OrganizationInfoSquareCache implements BaseCache<List<OrganizationInfo>, OrganizationInfo> {


    @Resource
    OrganizationService organizationService;

    @Override
//    @Cacheable(key = "'all'")
    public List<OrganizationInfo> getCache() {
        return organizationService.selectAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<OrganizationInfo> refreshCache() {
        return organizationService.selectAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<OrganizationInfo> updateCache(OrganizationInfo channelInfo) {
        return getCache();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {

    }

    public List<OrganizationInfo> getOrganizationInfosByOrganizationId(String organizationId) throws  PayException{
        List<OrganizationInfo> organizationInfos = new ArrayList<>();
        String[] strings = organizationId.split(",");
        for (String s : strings){
            OrganizationInfo organizationInfo =
                    getCache()
                    .stream()
                    .filter(organizationInfo1 -> organizationInfo1.getOrganizationId().equals(s) && SystemConstant.SQUARE_ENABLE.equals(organizationInfo1.getStatus()))
                    .findFirst()
                    .orElseThrow(()-> new PayException("机构不存在或未启用",2006));
            organizationInfos.add(organizationInfo);
        }
        return organizationInfos;
    }
}