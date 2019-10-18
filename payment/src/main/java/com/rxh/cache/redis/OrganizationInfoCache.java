package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.service.square.OrganizationService;
import com.rxh.square.pojo.OrganizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrganizationInfoCache  extends AbstractPayCache {

    private final String tableMapKeyName="organization_info";

    @Autowired
    protected OrganizationService organizationService;

    public List<OrganizationInfo>  getAll(){
        List<OrganizationInfo>  organizationInfoList= redisTemplate.opsForHash().values(tableMapKeyName)
                .stream()
                .map(t->(OrganizationInfo)t)
                .distinct()
                .collect(Collectors.toList());

        if(!isHasElement(organizationInfoList)){
            organizationInfoList=organizationService.getAll(new OrganizationInfo());
            if(isHasElement(organizationInfoList)){
                Map<Object,Object> map= new HashMap<>(organizationInfoList.size());
                organizationInfoList.forEach(t->{
                    map.put(t.getOrganizationId(),t);
                });
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }

        return organizationInfoList;
    }

    /**
     *
     * @param organizetionId
     * @return
     */
    public OrganizationInfo getOne(String organizetionId){
        OrganizationInfo organizationInfo= (OrganizationInfo) redisTemplate.opsForHash().get(tableMapKeyName,organizetionId);
        if(isNull(organizationInfo)){
            List<OrganizationInfo> organizationInfoList=organizationService.getAll(new OrganizationInfo());
            if(isHasElement(organizationInfoList)){
                Map<Object,Object> map= new HashMap<>(organizationInfoList.size());
                organizationInfoList.forEach(t->{
                    map.put(t.getOrganizationId(),t);
                });
                organizationInfo= (OrganizationInfo) map.get(organizetionId);
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return organizationInfo;
    }
}
