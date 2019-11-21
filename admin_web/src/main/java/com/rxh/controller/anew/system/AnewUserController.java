package com.rxh.controller.anew.system;

import com.internal.playment.common.table.system.SysPrivilegesTable;
import com.rxh.service.system.NewSystemUserService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

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
    private NewSystemUserService userService;

    /**
     * 获取用户菜单
     *
     * @return 用户菜单
     */
    @RequestMapping(value = "/getMenuList")
    @ResponseBody
    public List<SysPrivilegesTable> getMenuList() {
        return userService.getMenu(UserInfoUtils.getName());
    }

    @SystemLogInfo(description = "刷新缓存")
    @RequestMapping(value = "/refreshCache")
    @ResponseBody
    public  String refreshCache(){
        try{
            return "SUCCESS";
        }catch (Exception e){
            e.printStackTrace();
            return "FAIL";
        }
    }
}