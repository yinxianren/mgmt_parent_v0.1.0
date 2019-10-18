package com.rxh.cache;

import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayThreadPool;
import com.rxh.payLock.MerchantsLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public abstract class AbstractPayCache implements PayAssert, PayThreadPool {
    @Autowired
    protected MerchantsLock merchantsLock;
    @Autowired
    protected RedisTemplate<String,Object> redisTemplate;



    protected void put(String tableMapKeyName,Object  object, String objectKey) {
        redisTemplate.opsForHash().put(tableMapKeyName,objectKey,object);
    }

    protected void putAll(String tableMapKeyName,Map<Object,Object> map){
        redisTemplate.opsForHash().putAll(tableMapKeyName,map);
    }
}
