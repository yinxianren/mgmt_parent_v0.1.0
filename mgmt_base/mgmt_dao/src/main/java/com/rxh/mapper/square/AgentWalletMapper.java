package com.rxh.mapper.square;

import com.rxh.square.pojo.AgentWallet;
import com.rxh.square.pojo.AgentWalletExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AgentWalletMapper {
    int countByExample(AgentWalletExample example);

    int deleteByExample(AgentWalletExample example);

    int deleteByPrimaryKey(String agentMerchantId);

    int insert(AgentWallet record);

    int insertSelective(AgentWallet record);

    List<AgentWallet> selectByExample(AgentWalletExample example);

    AgentWallet selectByPrimaryKey(String agentMerchantId);

    int updateByExampleSelective(@Param("record") AgentWallet record, @Param("example") AgentWalletExample example);

    int updateByExample(@Param("record") AgentWallet record, @Param("example") AgentWalletExample example);

    int updateByPrimaryKeySelective(AgentWallet record);

    int updateByPrimaryKey(AgentWallet record);

    List<AgentWallet> search(@Param("agentMerchantId") String agentMerchantId);
}