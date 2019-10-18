package com.rxh.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/14
 * Time: 21:47
 * Project: mgmt_parent
 */
public class JedisClientPool implements JedisClient {
    private final static Logger logger = LoggerFactory.getLogger(JedisClientPool.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * Set String
     *
     * @param key   key
     * @param value value
     * @return
     */
    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    /**
     * Get String
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    /**
     * Exists
     *
     * @param key
     * @return
     */
    @Override
    public Boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.exists(key);
        jedis.close();
        return result;
    }

    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    /**
     * 如果一个key设置了过期时间，则取消其过期时间，使其永久存在
     *
     * @param key key
     * @return 1 成功，0失败
     */
    @Override
    public Long persist(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.persist(key);
        jedis.close();
        return result;
    }

    /**
     * 设置存活时间（秒）
     *
     * @param key     key
     * @param seconds 存活时间
     * @return 1 成功，0失败
     */
    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, seconds);
        jedis.close();
        return result;
    }

    /**
     * 以秒为单位返回 key 的剩余过期时间
     *
     * @param key key
     * @return 剩余过去秒数
     */
    @Override
    public Long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.ttl(key);
        jedis.close();
        return result;
    }


    /**
     * 类似计数器
     *
     * @param key
     * @return
     */
    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incr(key);
        jedis.close();
        return result;
    }

    /**
     * Hash set
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hset(key, field, value);
        jedis.close();
        return result;
    }


    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hmset(key, hash);
        return null;
    }

    /**
     * Hash get
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public String hget(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key, field);
        jedis.close();
        return result;
    }

    /**
     * 在hash中获取多个字段的值，若字段不存在，则其值为nil。
     *
     * @param key
     * @param fields
     * @return 按顺序返回多个字段的值
     */
    @Override
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hmget(key, fields);
        jedis.close();
        return result;
    }

    /**
     * 返回 key 指定的哈希集中所有字段的值。
     *
     * @param key
     * @return 哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表。
     */
    @Override
    public List<String> hvals(String key) {
        Jedis jedis = jedisPool.getResource();
        List<String> result = jedis.hvals(key);
        jedis.close();
        return result;
    }

    /**
     * 返回 key 指定的哈希集中所有的字段和值
     *
     * @param key
     * @return 返回 key 指定的哈希集中所有的字段和值,若key不存在返回空map。
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> result = jedis.hgetAll(key);
        jedis.close();
        return result;
    }

    /**
     * Hash del
     *
     * @param key
     * @param field
     * @return
     */
    @Override
    public Long hdel(String key, String... field) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.hdel(key, field);
        jedis.close();
        return result;
    }

    /**
     * 判断hash中指定字段是否存在
     *
     * @param key
     * @param field
     * @return 若存在返回1，若不存在返回0
     */
    @Override
    public Boolean hexists(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        Boolean result = jedis.hexists(key, field);
        jedis.close();
        return result;
    }

    @Override
    public String delAll() {
        Jedis jedis = jedisPool.getResource();
        logger.info("清空Redis所有缓存，RedisDB：" + jedis.getDB());
        String s = jedis.flushDB();
        jedis.close();
        return s;
    }
}
