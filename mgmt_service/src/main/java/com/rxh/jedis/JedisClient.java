package com.rxh.jedis;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/14
 * Time: 21:45
 * Project: mgmt_parent
 */
public interface JedisClient {

    String set(String key, String value);

    String get(String key);

    Boolean exists(String key);

    Long persist(String key);

    Long expire(String key, int seconds);

    Long ttl(String key);

    Long incr(String key);

    Long hset(String key, String field, String value);

    String hmset(String key, Map<String, String> hash);

    String hget(String key, String field);

    List<String> hmget(String key, String... fields);

    List<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    Long hdel(String key, String... field);

    Boolean hexists(String key, String field);

    String delAll();

}
