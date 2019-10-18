package com.rxh.spring.aop;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayThreadPool;
import com.rxh.spring.annotation.RedisCacheDeleteByBatch;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.util.Collection;

public class RedisCacheDeleteByBatchAspect implements PayAssert, PayThreadPool {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void after(JoinPoint joinPoint, RedisCacheDeleteByBatch redisCacheDeleteByBatch) throws PayException {
        try {
            String[] hashKeyArray = redisCacheDeleteByBatch.hashKey();
            if (!isHasElement(hashKeyArray)) throw new PayException(String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数,hashKey不存在"));
            int keyType=redisCacheDeleteByBatch.keyType();
            int delObjectIndex=redisCacheDeleteByBatch.del0bjectIndex();
            Object[]  objects=joinPoint.getArgs();
            String keyName=redisCacheDeleteByBatch.keyName();
            switch (keyType){
                case 0:
                    this.keyTypeByString(hashKeyArray,delObjectIndex,objects);
                    break;
                case 1:
                    this.keyTypeByArray(hashKeyArray,delObjectIndex,objects);
                    break;
                case 2:
                case 3:
                    this.keyTypeByCollection(hashKeyArray,delObjectIndex,objects,keyName);
                    break;
                case 4:
                    throw new PayException(String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数,keyTyp目前不支持Map类型"));
//                    break;
                default:
                    throw new PayException(String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数,keyTyp目前不支持其他类型"));
            }

        }catch (Exception e){
            throw  new PayException(e.getMessage());
        }

    }

    /**
     *
     * @param hashKeyArray
     * @param delObjectIndex
     * @param objects
     * @throws PayException
     */
    private void keyTypeByString( String[] hashKeyArray,int delObjectIndex,Object[]  objects) throws PayException {
        try {
            String key = (String) objects[delObjectIndex];
            isNotNull(key,String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数，keyType不存在"));
            for(String hashKey : hashKeyArray){
                pool.execute(()-> redisTemplate.opsForHash().delete(hashKey,key));
            }
        }catch (Exception e){
            throw new PayException(e.getMessage());
        }
    }

    /**
     *
     * @param hashKeyArray
     * @param delObjectIndex
     * @param objects
     * @throws PayException
     */
    private void keyTypeByArray(String[] hashKeyArray,int delObjectIndex,Object[]  objects) throws PayException {
        Object[] keyTypeArray= (Object[]) objects[delObjectIndex];
        if(!isHasElement(keyTypeArray))
            throw new PayException(String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数，keyType不存在"));
        for(String hashKey : hashKeyArray){
            for(Object key : keyTypeArray){
                pool.execute(()-> redisTemplate.opsForHash().delete(hashKey,key));
            }
        }
    }

    /**
     *
     * @param hashKeyArray
     * @param delObjectIndex
     * @param objects
     * @throws PayException
     */
    private void keyTypeByCollection(String[] hashKeyArray,int delObjectIndex,Object[]  objects,String keyName) throws Exception {
        Collection<Object>   objList= (Collection<Object>) objects[delObjectIndex];
        isNotElement(objList,String.format("错误提示：错误使用：@RedisCacheDeleteByBatch，请查看配置参数，keyType不存在"));
        if(isBlank(keyName)||!keyName.matches("^#.+"))
            throw new PayException(String.format("错误提示：错误使用：@RedisCacheDelete，请查看配置参数，keyName 设置有误，原数据：%s",keyName));
        keyName=keyName.substring(1,keyName.length()).trim();
        for(String hashKey : hashKeyArray){
            for (Object obj : objList) {
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals(keyName)) {
                        field.setAccessible(true);
                        Object key = field.get(obj);
                        if(!isNull(key)){
                            pool.execute(()-> redisTemplate.opsForHash().delete(hashKey,key));
                        }
                        break;
                    }
                }
            }
        }
    }

}
