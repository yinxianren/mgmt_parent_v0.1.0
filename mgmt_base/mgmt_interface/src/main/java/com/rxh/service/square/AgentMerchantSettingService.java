package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.AgentMerchantSetting;

import java.util.List;

public interface AgentMerchantSettingService {
    Result insert(AgentMerchantSetting record);
    Result update(List<AgentMerchantSetting> param);
    Result delete(String id);
    List<AgentMerchantSetting> search(String merId);
    List<AgentMerchantSetting> merSearch(String merId);
    List<AgentMerchantSetting> getAll();
    //add by gjm 添加redis  at 20190807 start
    /**
     * 根据代理商商户编号和类型,获取代理商费率信息
     * @param merId
     * @param payType
     * @return AgentMerchantSetting
     * @author gjm
     */
    AgentMerchantSetting getAgentMerchantSettingByMerIdAndPayType(String merId,String payType);
    //add by gjm 添加redis  at 20190807 end
}
