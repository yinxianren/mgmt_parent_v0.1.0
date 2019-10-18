package com.rxh.spring.aop;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.spring.annotation.RedisCacheDelete;
import com.rxh.utils.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;

/**
 * 删除redis 中的缓存
 * @author panda
 * @date 20190806
 *
 */
public class RedisCacheDeleteAspect implements PayAssert {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void after(JoinPoint joinPoint, RedisCacheDelete redisCacheDelete){
        try {
            String hashKey = redisCacheDelete.hashKey();
            if (isNull(hashKey))
                throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数，hashKey不存在"));
            int keyindex = redisCacheDelete.keyIndex();
            String keyName=redisCacheDelete.keyName();
            String[] keyNameArray=redisCacheDelete.keyNameArry();
            Object[] objArr = joinPoint.getArgs();

            if (isHasElement(objArr)) {
                if(!isHasElement(keyNameArray)) {
                    if (isBlank(keyName)) {
                        String key = (String) objArr[keyindex];
                        redisTemplate.opsForHash().delete(hashKey, key);
                    } else {
                        if (!keyName.matches("^#.+"))
                            throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数，keyName 设置有误，原数据：%s", keyName));
                        keyName = keyName.substring(1, keyName.length()).trim();
                        Object obj = objArr[keyindex];
                        Field[] fields = obj.getClass().getDeclaredFields();
                        Object key = null;
                        if (isHasElement(fields)) {
                            for (Field filed : fields) {
                                if (filed.getName().equals(keyName)) {
                                    filed.setAccessible(true);
                                    key = filed.get(obj);
                                    break;
                                }
                            }
                        }
                        if (isNull(key))
                            throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数,给定key不存在，key:%s", keyName));
                        redisTemplate.opsForHash().delete(hashKey, key);
                    }
                }else {
                    if(!isHasElement(keyNameArray))
                        throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数,给定keyNameArray不存在"));

                    String[] keyArry= new String[keyNameArray.length];
                    for(int i=0;i<keyNameArray.length;i++){
                        if(! keyNameArray[i].matches("^#.+"))
                            throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数，keyNameArray 设置有误，原数据：%s",
                                    JsonUtils.objectToJson(keyNameArray)));
                        keyArry[i]=keyNameArray[i].substring(1, keyNameArray[i].length()).trim();
                    }

                    Object obj=objArr[keyindex];
                    Field[] fields=obj.getClass().getDeclaredFields();
                    String key="";
                    for(Field field : fields){
                        for(String fieldName : keyArry){
                            if(field.getName().equals(fieldName)){
                                field.setAccessible(true);
                                key =key + field.get(obj)+"-";
                            }
                        }
                    }
                    key=key.substring(0,key.length()-1);
                    redisTemplate.opsForHash().delete(hashKey, key);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
