package com.rxh.utils;

import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.square.pojo.MerchantInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  描述：为每一个用户创建一把锁，解决多个用户抢占同一把锁
 * @author panda
 * @date 20190716
 */

@Component
public class MerchantsLockUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //存放锁的并发容器
    private static Map<String, Lock>  merchantsLock= Collections.synchronizedMap(new HashMap<>());
    @Autowired
    private RecordSquareService recordSquareService;

    /**
     *   根据商户id，获取商户特点的一把锁
     * @param merchantId
     * @return
     */
    public  synchronized Lock  getLock(String merchantId) throws Exception {
        try{
            //商户号为空，抛出异常
            Assert.notNull(merchantId,"获取商户对象锁时，商户号为 null");
            Lock lock= merchantsLock.get(merchantId);
            if(null == lock){
                // 查询商户信息
                MerchantInfo merchantInfo = recordSquareService.getMerchantInfoByMerId(merchantId);
                //如果没该用户，则抛出异常
                Assert.notNull(merchantInfo,"获取商户对象锁时，查询到平台未有该商户["+merchantId+"]");
                //如果没用锁，则手动创建一把锁，然后放到集合
                lock=new ReentrantLock();
                merchantsLock.put(merchantId,lock);
                logger.info("【商户锁管理】 [{}]新创建了一把锁！",merchantId);
            }
            return lock;
        }catch (Exception exception){
            throw  exception;
        }
    }

}
