package com.rxh.spring.aop;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.spring.annotation.RedisCacheDeleteByHashKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheDeleteByHashKeyAspect implements PayAssert {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void after(RedisCacheDeleteByHashKey redisCacheDeleteByHashKey) throws PayException {
        try {
            String hashKey = redisCacheDeleteByHashKey.hashKey();
            isBlank(hashKey, String.format("错误提示：错误使用：@RedisCacheDeleteByHashKey，请查看配置参数:hashKey的值"));
            Object[] objStr= redisTemplate.opsForHash().keys(hashKey).toArray();
            if(objStr.length>0)
                redisTemplate.opsForHash().delete(hashKey,objStr);
        }catch (Exception e){
            throw  new PayException(e.getMessage());
        }
    }

}
