package com.rxh.payLock;

import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  描述： payLock的父类，锁级别有：商户锁，通道锁，平台锁，代理锁；对应着四个钱包，四个钱包明细，以及各自的相关信息
 * @author panda
 * @date 20190802
 *
 */
public abstract class AbstractPayLock implements PayAssert, PayUtil {

    protected Logger logger = LoggerFactory.getLogger(getClass());

}
