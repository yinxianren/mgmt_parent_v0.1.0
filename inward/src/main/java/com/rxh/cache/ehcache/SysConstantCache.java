package com.rxh.cache.ehcache;

import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.ConstantService;
import com.rxh.utils.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/8/28
 * Time: 15:51
 * Project: Management
 * Package: com.rxh.cache
 */
//@CacheConfig(cacheNames = "sys_constant")
@Component
public class SysConstantCache implements BaseCache<List<SysConstant>, SysConstant> {
    @Resource
    private ConstantService constantService;

//    @Cacheable(key = "'all'")
    @Override
    public List<SysConstant> getCache() {
        return constantService.selectAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<SysConstant> refreshCache() {
        return constantService.selectAll();
    }

//    @CachePut(key = "'all'")
    @Override
    public List<SysConstant> updateCache(SysConstant sysConstant) {
        return constantService.selectAll();
    }

//    @CacheEvict(allEntries = true)
    @Override
    public void cleanCache() {
    }

//    @Cacheable(key = "#groupname")
    public List<SysConstant> getConstantByGroupName(String groupname) {
        return constantService.getConstantByGroupName(groupname);
    }

//    @Cacheable(key = "#id")
    public SysConstant getSysConstantByid(String id) {
        return getCache()
                .stream()
                .filter(sysConstant -> StringUtils.equals(id,sysConstant.getId()))
                .findFirst().orElse(null);
    }

//    @Cacheable(key = "#currcy")
    public SysConstant getSysConstantByFirstValue(String currcy) {
        return getCache()
                .stream()
                .filter(sysConstant -> StringUtils.equals(currcy,sysConstant.getFirstValue()))
                .findFirst().orElse(null);
    }
//    @Cacheable(key = "#currcy")
    public SysConstant getcurrencyNumByFirstValue(String currcy) {
        return getCache()
                .stream()
                .filter(sysConstant -> StringUtils.equals(currcy,sysConstant.getFirstValue())
                && StringUtils.equals("Currency",sysConstant.getGroupCode())
                )
                .findFirst().orElse(null);
    }


//    @Cacheable(key = "#groupname")
    public List<SysConstant> getConstantByGroupNameAndSortValueIsNotNULL(String groupname) {
        return constantService.getConstantByGroupNameAndSortValueIsNotNULL(groupname);
    }


//    @Cacheable(key = "#province")
    public SysConstant getStateByProvince(String province) {
        return constantService.getStateByProvince(province);
    }




}
