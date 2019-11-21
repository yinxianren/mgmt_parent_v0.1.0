package com.rxh.controller.anew.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.rxh.service.AnewChannelService;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.ProductTypeSettingService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.sys.SysConstantService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
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
    private OrganizationInfoService organizationInfoService;
    @Autowired
    private NewSystemConstantService constantService;
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
        try {
            channelInfo.setCreateTime(new Date());
            channelInfo.setUpdateTime(new Date());
            channelInfo.setChannelId("CH"+System.currentTimeMillis());
            return anewChannelService.saveOrUpdate(channelInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
    @SystemLogInfo(description = "支付通道删除")
    @RequestMapping("/delete")
    public ResponseVO delete(@RequestBody List<String> ids){
        try {
            return anewChannelService.delByIds(ids);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "支付通道修改")
    @RequestMapping("/update")
    public ResponseVO update(@RequestBody ChannelInfoTable channelInfo){
        try {
            channelInfo.setUpdateTime(new Date());
            return anewChannelService.saveOrUpdate(channelInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody ChannelInfoTable channelInfo){
        try {
            return anewChannelService.getAll(channelInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @RequestMapping(value = "/getInit")
    @ResponseBody
    public Map<String, Object> getChannelInfoInit() {
        Map<String, Object> init = new HashMap<>();
        init.put("organizations",organizationInfoService.getAll(new OrganizationInfoTable()).getData());
        init.put("status", constantService.getConstantByGroupName(SystemConstant.availableStatus).getData());
        init.put("paytype", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("channelLevel", constantService.getConstantByGroupName(SystemConstant.channelLevel).getData());
        init.put("busiTypes", constantService.getConstantByGroupName(SystemConstant.BUSITYPE).getData());
        ProductSettingTable productSettingTable = new ProductSettingTable();
        productSettingTable.setStatus(StatusEnum._0.getStatus());
        init.put("products", productTypeSettingService.selectByOrganizationId(productSettingTable).getData());
        init.put("productTypes", constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
        return init;
    }

}
