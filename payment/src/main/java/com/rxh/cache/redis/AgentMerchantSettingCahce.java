package com.rxh.cache.redis;

import com.rxh.cache.AbstractPayCache;
import com.rxh.exception.PayException;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.square.pojo.AgentMerchantSetting;
import com.rxh.square.pojo.MerchantInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AgentMerchantSettingCahce extends AbstractPayCache {

    private final String tableMapKeyName="agent_merchant_setting";

    @Autowired
    protected PaymentRecordSquareService paymentRecordSquareService;
    @Autowired
    private MerchantInfoCache merchantInfoCache;

    /**
     *
     * @param agentMerchantId
     * @param payType
     * @return
     */
    public AgentMerchantSetting getOne(String agentMerchantId, String payType){
        String key=agentMerchantId+"-"+payType;
        AgentMerchantSetting agentMerchantSetting= (AgentMerchantSetting) redisTemplate.opsForHash().get(tableMapKeyName,key);
        if(isNull(agentMerchantSetting)){
            agentMerchantSetting=paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(agentMerchantId,payType);
            if(isNotNull(agentMerchantSetting)){
                final AgentMerchantSetting ams=agentMerchantSetting;
                pool.execute(()->put(tableMapKeyName,ams,key));
            }
        }
        return agentMerchantSetting;
    }

    /**
     *
     * @return
     */
    public List<AgentMerchantSetting> getAll(){
       List<AgentMerchantSetting>  agentMerchantSettingList=redisTemplate.opsForHash().values(tableMapKeyName)
               .stream()
               .map(t->(AgentMerchantSetting)t)
               .distinct()
               .collect(Collectors.toList());
       if(!isHasElement(agentMerchantSettingList)){
           agentMerchantSettingList= paymentRecordSquareService.getAgentMerchantSetting(new AgentMerchantSetting());
           if(isHasElement(agentMerchantSettingList)){
               Map<Object,Object> map=new HashMap<>(agentMerchantSettingList.size());
               agentMerchantSettingList.forEach(t->{
                   map.put(t.getAgentMerchantId()+"-"+t.getPayType(),t);
               });
               pool.execute(()->putAll(tableMapKeyName,map));
           }
       }
       return agentMerchantSettingList;
    }

    /**
     *
     * @param merId
     * @param payType
     * @return
     */
    public AgentMerchantSetting getOneByMerId(String merId, String payType) throws PayException {
        MerchantInfo merchantInfo = merchantInfoCache.getOne(merId);
        String agentMerchantId = merchantInfo.getParentId();
        String key=agentMerchantId+"-"+payType;
        AgentMerchantSetting agentMerchantSetting= (AgentMerchantSetting) redisTemplate.opsForHash().get(tableMapKeyName,key);
        if(isNull(agentMerchantSetting)){
            agentMerchantSetting=paymentRecordSquareService.getAgentMerchantSettingByParentIdAndPayType(agentMerchantId,payType);
            if(isNotNull(agentMerchantSetting)){
                final AgentMerchantSetting ams=agentMerchantSetting;
                pool.execute(()->put(tableMapKeyName,ams,key));
            }
        }
        return agentMerchantSetting;
    }

}
