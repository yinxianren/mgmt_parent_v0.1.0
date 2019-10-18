package com.rxh.cache.redis;

import com.rxh.service.square.AgentMerchantSettingService;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.cache.AbstractPayCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AgentMerchantSettingCache extends AbstractPayCache {

    private final String tableMapKeyName="agent_merchant_setting";

    @Autowired
    private AgentMerchantSettingService agentMerchantSettingService;

    /**
     *
     * @param  agentMerchantId
     * @param  payType
     * @return
     */
    public AgentMerchantSetting getOne(String agentMerchantId,String payType){
        String key=agentMerchantId+"-"+payType;
        AgentMerchantSetting  agentMerchantSetting= (AgentMerchantSetting) redisTemplate.opsForHash().get(tableMapKeyName,key);
        if(isNull(agentMerchantSetting)){
            agentMerchantSetting = agentMerchantSettingService.getAgentMerchantSettingByMerIdAndPayType(agentMerchantId,payType);
            if(isNotNull(agentMerchantSetting)){
                final AgentMerchantSetting ams=agentMerchantSetting;
                pool.execute(()->put(tableMapKeyName,ams,key));
            }
        }
        return agentMerchantSetting;
    }

    /**
     *
     * @param
     * @return
     */
    public List<AgentMerchantSetting> getAll(){
        List<AgentMerchantSetting>  agentMerchantSettingList=redisTemplate.opsForHash().values(tableMapKeyName)
                .stream()
                .map(t->(AgentMerchantSetting)t)
                .distinct()
                .collect(Collectors.toList());
        if(!isHasElement(agentMerchantSettingList)){
            agentMerchantSettingList= agentMerchantSettingService.getAll();
            if(isHasElement(agentMerchantSettingList)){
                Map<Object,Object> map=new HashMap<>();
                agentMerchantSettingList.forEach(t->{
                    map.put(t.getAgentMerchantId()+"-"+t.getPayType(),t);
                });
                pool.execute(()->putAll(tableMapKeyName,map));
            }
        }
        return agentMerchantSettingList;
    }

    private void put(AgentMerchantSetting agentMerchantSetting, String objectKey) {
        redisTemplate.opsForHash().put(tableMapKeyName,objectKey,agentMerchantSetting);
    }
}
