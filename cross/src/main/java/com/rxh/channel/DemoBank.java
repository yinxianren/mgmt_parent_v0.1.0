package com.rxh.channel;

import com.rxh.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class DemoBank {

    @RequestMapping("/demoBank")
    @ResponseBody
    public String getMsg(@RequestBody String msg){
        System.out.println(msg);
        HashMap<String, Object> map = new HashMap<>();
        map.put("responseCode","00");
        map.put("payMoney","212");
        String dataJson = JsonUtils.objectToJson(map);

        return dataJson;
    }
}
