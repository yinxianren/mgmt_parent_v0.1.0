package com.rxh.controller.oldKuaijie;

//import com.rxh.cache.ehcache.BaseCache;
import com.rxh.cache.ehcache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/28
 * Time: 11:00
 * Project: Management
 * Package: com.rxh.controller
 */
@Controller
@RequestMapping(value = "/cache")
public class CacheController {
    private final ApplicationContext applicationContext;


    private List<String> getCacheName(){
        List<String> list=new ArrayList<>();

        list.add("channerInfoSquareCache");
        list.add("merchantSquareRateCache");
        list.add("merchantSquareSettingCache");
        list.add("organizationInfoSquareCache");
        list.add("sysConstantCache");

        return list;
    }

    @Autowired
    public CacheController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/refresh")
    @ResponseBody
    public String refresh() {
        List<String> list = getCacheName();
        for (String CacheName : list) {
            BaseCache baseCache = applicationContext.getBean(CacheName, BaseCache.class);
            baseCache.cleanCache();
            System.out.println(CacheName+"执行了刷新缓存操作");
//            baseCache.refreshCache();
        }
        return "Ok";
    }
}