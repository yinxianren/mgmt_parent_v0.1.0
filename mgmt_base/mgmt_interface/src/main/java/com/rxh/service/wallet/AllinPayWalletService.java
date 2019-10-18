package com.rxh.service.wallet;

import com.rxh.square.pojo.*;

public interface AllinPayWalletService {

    /**
     * 代付更新钱包
     * @param agentWallet
     * @param merchantWallet
     * @param channelWallet
     * @param terminalMerchantsWallet
     * @param merchantsDetails
     * @param terminalMerchantsDetails
     * @param agentMerchantsDetails
     * @param channelDetails
     */
    public void updateAllinPayWallet(AgentWallet agentWallet, MerchantWallet merchantWallet, ChannelWallet channelWallet, TerminalMerchantsWallet terminalMerchantsWallet,TransOrder transOrder,
                                    MerchantsDetails merchantsDetails,TerminalMerchantsDetails terminalMerchantsDetails,AgentMerchantsDetails agentMerchantsDetails,ChannelDetails channelDetails);
}
