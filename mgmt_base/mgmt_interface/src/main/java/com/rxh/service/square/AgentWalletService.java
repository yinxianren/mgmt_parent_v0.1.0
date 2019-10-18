package com.rxh.service.square;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.square.pojo.AgentMerchantsDetails;
import com.rxh.square.pojo.AgentWallet;

import java.math.BigDecimal;
import java.util.List;

public interface AgentWalletService {

     List<AgentWallet> search(AgentWallet agentWallet);
     boolean insert(AgentWallet agentWallet);
     Result deleteByPrimaryKey(List<String> agentMerchantIds);
     PageResult findAgentWallteDetails(Page page);
     AgentWallet selectByPrimaryKey(String agentMerchantId);
     int updateByPrimaryKeySelective(AgentWallet record);
}
