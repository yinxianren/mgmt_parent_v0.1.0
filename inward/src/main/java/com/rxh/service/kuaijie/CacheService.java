package com.rxh.service.kuaijie;

import com.rxh.cache.ehcache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/21
 * Time: 14:33
 * Project: Management
 * Package: com.rxh.service
 */
@Service
public class CacheService {

    private final ApplicationContext applicationContext;

    @Autowired
    public CacheService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void refresh(String beanName) {
        BaseCache baseCache = applicationContext.getBean(beanName, BaseCache.class);
//        System.out.println(baseCache.refreshCache());
    }
}
