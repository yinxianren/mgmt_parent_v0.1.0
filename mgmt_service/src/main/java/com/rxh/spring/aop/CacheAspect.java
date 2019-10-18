package com.rxh.spring.aop;

import com.rxh.jedis.JedisClientPool;
import com.rxh.spring.annotation.redisCache;
import com.rxh.spring.annotation.redisEvict;
import com.rxh.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 王德明
 * Date: 2018/7/2
 * Time: 11:07
 * Project: Management_new
 * Package: com.rxh.spring.aop
 */
@Component
@Aspect
public class CacheAspect {

    @Resource
    private JedisClientPool jedisClientPool;

    @Resource
    private JsonUtils jsonUtils;


    @Before(value = "@annotation(redisEvict)")
    public void beforecache(JoinPoint jp,redisEvict redisEvict) {
        try{
            jedisClientPool.hdel(redisEvict.key(),redisEvict.field());
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    //环绕通知，先获取缓存数据若为空则执行方法并存入redis数据库中.可返回list,pojo,string类型.@Cacheable(key,field,xxx.class)
    @Around(value = "@annotation(rediscache)")
    public Object cache(ProceedingJoinPoint pjp,redisCache rediscache) throws Throwable {
        Object result = null;
        String value = jedisClientPool.hget(rediscache.key(), rediscache.field());
        if (value == null) {
            result = pjp.proceed();
            try {
                String resultJson = JsonUtils.objectToJson(result);
                jedisClientPool.hset(rediscache.key(),rediscache.field(),resultJson);
                if (rediscache.expireTime() > 0) {
                    jedisClientPool.expire(rediscache.key(),rediscache.expireTime());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            try {
                //获取代理方法的返回类型
                Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();
                result = deserialize(value, returnType,rediscache.type());
            } catch (Exception e) {
                result = pjp.proceed();
                e.printStackTrace();
            }
        }
        return result;
    }

    protected Object deserialize(String value,Class returnType,Class type) {
        if (returnType.isAssignableFrom(List.class)) {
            return JsonUtils.jsonToList(value,type);  //将json字符串转换成pojo对象list
        }
        return JsonUtils.jsonToPojo(value,type);   //将json字符串转换成pojo对象
    }

}
