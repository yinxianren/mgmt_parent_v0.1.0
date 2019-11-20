package com.rxh.controller.anew.system;

import com.rxh.cache.RedisCackeKeys;
import com.rxh.pojo.sys.SysPrivileges;
import com.rxh.service.UserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/3/8
 * Time: 9:54
 * Project: MySSM
 * Package: com.chan.controller
 */
@Controller
@RequestMapping(value = "/user")
public class AnewUserController {

    @Resource
    private UserService userService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 获取用户菜单
     *
     * @return 用户菜单
     */
    @RequestMapping(value = "/getMenuList")
    @ResponseBody
    public List<SysPrivileges> getMenuList() {
        return userService.getMenu(UserInfoUtils.getName());
    }

    @SystemLogInfo(description = "刷新缓存")
    @RequestMapping(value = "/refreshCache")
    @ResponseBody
    public  String refreshCache(){
        try{
            List<String>  list = Arrays.stream(RedisCackeKeys.values())
                    .map(RedisCackeKeys::getKey)
                    .distinct()
                    .collect(Collectors.toList());
            redisTemplate.delete(list);
            return "SUCCESS";
        }catch (Exception e){
            e.printStackTrace();
            return "FAIL";
        }
    }
}