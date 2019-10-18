package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.square.pojo.AgentMerchantInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.square.vo.VoAgentMerchantInfo;

import java.util.List;

public interface AgentMerchantInfoService {

    int insertAgentMerchantInfo(AgentMerchantInfo agentMerchantInfo,String username);

    int deleteAgentMerchantInfo(String id);

    int update(AgentMerchantInfo agentMerchantInfo);

    List<AgentMerchantInfo> getAll();

    Boolean isAgentMerchantIdExist(String agentMerchantId);

    List<AgentMerchantInfo> getAllByVoAgentMerchantInfo(VoAgentMerchantInfo voAgentMerchantInfo);

    Result deleteByIdArray(List<String> idArray);

    String getAgentMerchantIdIncre();
    String getMaxAgentMerchantId();

    List<AgentMerchantInfo> getAllIdAndName();

    List<MerchantInfo> getAllMerchantIdAndName(String merchantId);

    AgentMerchantInfo getMerchantInfo(String merchantId);
}
