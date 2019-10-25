package com.rxh.controller.anew.channel;

import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.anew.table.system.ProductSettingTable;
import com.rxh.cache.RedisCacheCommonCompoment;
import com.rxh.service.AnewChannelService;
import com.rxh.service.ConstantService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.square.ChannelInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ChannelInfo")
@Slf4j
public class AnewChannelInfoController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private AnewChannelService anewChannelService;
    @Autowired
    private ProductTypeSettingService productTypeSettingService;

    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/getAll")
    public ResponseVO getAll(){
        return anewChannelService.getAll(new ChannelInfoTable());
    }
    @SystemLogInfo(description = "支付通道增加")
    @RequestMapping("/insert")
    public ResponseVO insert(@RequestBody ChannelInfoTable channelInfo){
        channelInfo.setCreateTime(new Date());
        channelInfo.setUpdateTime(new Date());
        channelInfo.setChannelId("CH"+System.currentTimeMillis());
        return anewChannelService.saveOrUpdate(channelInfo);
    }
    @SystemLogInfo(description = "支付通道删除")
    @RequestMapping("/delete")
    public ResponseVO delete(@RequestBody List<String> ids){
        return anewChannelService.delByIds(ids);
    }

    @SystemLogInfo(description = "支付通道修改")
    @RequestMapping("/update")
    public ResponseVO update(@RequestBody ChannelInfoTable channelInfo){
        channelInfo.setUpdateTime(new Date());
        return anewChannelService.saveOrUpdate(channelInfo);
    }
    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody ChannelInfoTable channelInfo){
        return anewChannelService.getAll(channelInfo);
    }

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getChannelInfoInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("organizations",organizationService.getAll(new OrganizationInfo()));
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
        init.put("busiTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BUSITYPE));
        init.put("products", productTypeSettingService.selectByOrganizationId(new ProductSettingTable()).getData());
        init.put("productTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        return init;
    }

}
