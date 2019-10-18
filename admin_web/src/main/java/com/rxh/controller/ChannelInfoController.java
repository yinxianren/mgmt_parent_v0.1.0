package com.rxh.controller;

import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.SystemConstant;
import com.rxh.cache.RedisCacheCommonCompoment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/ChannelInfo")
public class ChannelInfoController {

    @Autowired
    private ChannelInfoService channelInfoService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/getAll")
    public List<ChannelInfo> getAll(){
        //modify by gjm 添加redis  at 20190808 start
        List<ChannelInfo> list = redisCacheCommonCompoment.channelInfoCache.getAll();
        if(null == list || list.isEmpty()){
            list = channelInfoService.getAll();
        }
        //modify by gjm 添加redis  at 20190808 end
        return list;
    }
    @SystemLogInfo(description = "支付通道增加")
    @RequestMapping("/insert")
    public int insert(@RequestBody ChannelInfo channelInfo){
        int i = channelInfoService.insert(channelInfo);
        return i;
    }
    @SystemLogInfo(description = "支付通道删除")
    @RequestMapping("/delete")
    public boolean delete(@RequestBody List<String> ids){
        String[] strArr=new String[ids.size()];
        for(int i=0;i<ids.size();i++){
            strArr[i]=ids.get(i);
        }
        boolean flag = channelInfoService.deleteByPrimaryKey(strArr);
        return flag;
    }

    @SystemLogInfo(description = "支付通道修改")
    @RequestMapping("/update")
    public int update(@RequestBody ChannelInfo channelInfo){
        int i = channelInfoService.updateByPrimaryKeySelective(channelInfo);
        return i;
    }
    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/search")
    public List<ChannelInfo> search(@RequestBody ChannelInfo channelInfo){
        //modify by gjm 添加redis  at 20190808 start
        List<ChannelInfo> list = null;
        if (channelInfo == null){
            list = redisCacheCommonCompoment.channelInfoCache.getAll();
        }
        if(null == list || list.isEmpty()){
            list = channelInfoService.selectByExample(channelInfo);
        }
        //modify by gjm 添加redis  at 20190808 end
        return list;
    }

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getChannelInfoInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("organizations",organizationService.getAll(new OrganizationInfo()));
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
        return init;
    }


}
