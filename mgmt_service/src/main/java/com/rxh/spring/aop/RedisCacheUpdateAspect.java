package com.rxh.spring.aop;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.spring.annotation.RedisCacheUpdate;
import com.rxh.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;

public class RedisCacheUpdateAspect implements PayAssert {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void after(JoinPoint joinPoint, RedisCacheUpdate redisCacheUpdate) throws PayException {
        try {
            String hashKey = redisCacheUpdate.hashKey();
            String keyName = redisCacheUpdate.keyName();
            if(isNull(keyName) || isNull(hashKey) || !keyName.matches("^#.+"))
                throw new PayException(String.format("错误提示：错误使用：@RedisCacheUpdate，请查看配置参数，\n原数据：%s; \n 注释内容：%s",
                        JsonUtils.objectToJson(joinPoint),JsonUtils.objectToJson(redisCacheUpdate)));

            keyName=keyName.substring(1,keyName.length()).trim();
            int updateObjectIndex = redisCacheUpdate.updateObjectIndex();
            Object[] objects = joinPoint.getArgs();
            if(isHasElement(objects)){
                Object obj= objects[updateObjectIndex];
                Field[] fields = obj.getClass().getDeclaredFields();
                String  key=null;
                if(isHasElement(fields)){
                    for(Field filed : fields){
                        if(filed.getName().equals(keyName)){
                            filed.setAccessible(true);
                            key= (String) filed.get(obj);
                            break;
                        }
                    }
                }
                if(isNull(key)) throw new PayException(String.format("错误提示：错误使用：@RedisCacheUpdate，请查看配置参数,给定key不存在，key:%s",keyName));
                redisTemplate.opsForHash().put(hashKey,key,obj);
            }
        }catch ( Exception e){
             throw new PayException(e.getMessage());
        }
    }

}
