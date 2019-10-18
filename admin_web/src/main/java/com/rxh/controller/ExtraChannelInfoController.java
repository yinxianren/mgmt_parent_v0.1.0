package com.rxh.controller;

import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.pojo.Result;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.ExtraChannelInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ExtraChannelInfo")
public class ExtraChannelInfoController {

    @Autowired
    private ExtraChannelInfoService extraChannelInfoService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private ChannelWalletService channelWalletService;
    @Autowired
    private RedisCacheCommonCompoment redisCacheCommonCompoment;

    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/getAll")
    public List<ExtraChannelInfo> getAll(){
        //modify by gjm 添加redis  at 20190809 start
        List<ExtraChannelInfo> list = redisCacheCommonCompoment.extraChannelInfoCache.getAll();
        if(null == list || list.isEmpty()){
            list = extraChannelInfoService.getAll();
        }
        //modify by gjm 添加redis  at 20190809 end
        return list;
    }
    @SystemLogInfo(description = "支付通道增加")
    @RequestMapping("/insert")
    public Result insert(@RequestBody ExtraChannelInfo extraChannelInfo){
        extraChannelInfo.setOperator(UserInfoUtils.getName());
        Result result = extraChannelInfoService.insert(extraChannelInfo);
        return result;
    }
    @SystemLogInfo(description = "支付通道删除")
    @RequestMapping("/delete")
    public boolean delete(@RequestBody List<String> ids){
        boolean flag = extraChannelInfoService.deleteByPrimaryKey(ids);

        return flag;
    }

    @SystemLogInfo(description = "支付通道修改")
    @RequestMapping("/update")
    public Result update(@RequestBody ExtraChannelInfo extraChannelInfo){
        extraChannelInfo.setOperator(UserInfoUtils.getName());
        Result result = extraChannelInfoService.update(extraChannelInfo);
        return result;
    }
    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/search")
    public List<ExtraChannelInfo> search(@RequestBody ExtraChannelInfo extraChannelInfo){
        //modify by gjm 添加redis  at 20190809 start
        List<ExtraChannelInfo> list = redisCacheCommonCompoment.extraChannelInfoCache.getAll();
        if(null == list || list.isEmpty()){
            list = extraChannelInfoService.select(extraChannelInfo);
        }
        //modify by gjm 添加redis  at 20190809 end
        return list;
    }

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getChannelInfoInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("organizations",organizationService.getAll(new OrganizationInfo()));
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("extraTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.EXTRATYPE));
        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
        init.put("channelInfoList", channelWalletService.getIdsAndName());
        return init;
    }


}
