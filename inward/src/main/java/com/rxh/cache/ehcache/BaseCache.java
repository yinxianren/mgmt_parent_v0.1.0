package com.rxh.cache.ehcache;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/24
 * Time: 14:41
 * Project: Management
 * Package: com.rxh.cache
 */
public interface BaseCache<T, E> {
    T getCache();

    T refreshCache();

    T updateCache(E e);

    void cleanCache();
}
