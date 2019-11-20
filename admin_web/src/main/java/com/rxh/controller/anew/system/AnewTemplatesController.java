package com.rxh.controller.anew.system;

import com.rxh.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AnewTemplatesController {
    private static final String USER_ERROR = "1";
    private static final String SESSION_EXPIRED = "2";
    private static final String SESSION_INVALID = "3";

    @RequestMapping(value = "/")
    public String showIndex() {
        return "index";
    }

    @RequestMapping(value = "/user/login")
    public String loginPage(Model model, String error) {
        if (StringUtils.isNotBlank(error)) {
            switch (error) {
                case USER_ERROR:
                    model.addAttribute("warning_msg", "您填写的帐号或密码不正确，请再次尝试。");
                    break;
                case SESSION_EXPIRED:
                    model.addAttribute("warning_msg", "您的账号已在其他地方登陆，请重新登陆。");
                    break;
                case SESSION_INVALID:
                    model.addAttribute("warning_msg", "登录超时，请重新登陆。");
                    break;
                default:
                    break;
            }
        }
        return "/views/login";
    }



    @RequestMapping(value = "/views/{page}")
    public String showViews_Tools(@PathVariable String page) {
        return "views/" + page;
    }
    
    /**
     * @author xuzm
     * @date 20180314
     * @param fileName
     * @param page
     * @return
     */
    @RequestMapping(value = "/views/{fileName}/{page}")
    public String showSecondPage(@PathVariable String fileName,@PathVariable String page) {
        return "views/" + fileName + "/" + page;
    }
}
