package com.rxh.wallet;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.service.square.PaymentRecordSquareService;
import com.rxh.service.wallet.KuaiJiePayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  描述： 钱包公共父类
 * @author panda
 * @date 20190721
 */
public abstract class AbstractWalletComponent implements PayAssert, PayUtil {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RedisCacheCommonCompoment redisCacheCommonCompoment;
    @Autowired
    protected MerchantWalletComponet merchantWalletComponet;
    @Autowired
    protected TerminalMerchantsWalletComponet terminalMerchantsWalletComponet;
    @Autowired
    protected AgentWalletComponet agentWalletComponet;
    @Autowired
    protected ChannelWalletComponet channelWalletComponet;
    @Autowired
    protected KuaiJiePayService kuaiJiePayService;
    @Autowired
    protected PaymentRecordSquareService paymentRecordSquareService;
}
