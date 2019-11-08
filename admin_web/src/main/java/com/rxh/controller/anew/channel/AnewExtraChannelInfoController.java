package com.rxh.controller.anew.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.rxh.service.AnewExtraChannelInfoService;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.OrganizationService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.OrganizationInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
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
@RequestMapping("/ExtraChannelInfo")
public class AnewExtraChannelInfoController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ConstantService constantService;
    @Autowired
    private ChannelWalletService channelWalletService;
    @Autowired
    private AnewExtraChannelInfoService anewExtraChannelInfoService;

    @SystemLogInfo(description = "支付通道查询")
    @RequestMapping("/getAll")
    public ResponseVO getAll(){
        try {
            ChannelExtraInfoTable channelExtraInfoTable = new ChannelExtraInfoTable();
            return anewExtraChannelInfoService.getAll(channelExtraInfoTable);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }
    }

    @SystemLogInfo(description = "支付通道增加")
    @RequestMapping("/insert")
    public ResponseVO insert(@RequestBody ChannelExtraInfoTable extraChannelInfo){
        try {
            extraChannelInfo.setCreator(UserInfoUtils.getName());
            extraChannelInfo.setCreateTime(new Date());
            extraChannelInfo.setUpdateTime(new Date());
            extraChannelInfo.setExtraChannelId("EX"+System.currentTimeMillis());
            return anewExtraChannelInfoService.saveOrUpdate(extraChannelInfo);
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
            return anewExtraChannelInfoService.removeByIds(ids);
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
    public ResponseVO update(@RequestBody ChannelExtraInfoTable extraChannelInfo){
        try {
            extraChannelInfo.setUpdateTime(new Date());
            return  anewExtraChannelInfoService.saveOrUpdate(extraChannelInfo);
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
    public ResponseVO search(@RequestBody ChannelExtraInfoTable extraChannelInfo){
        try {
            return anewExtraChannelInfoService.getAll(extraChannelInfo);
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
        init.put("organizations",organizationService.getAll(new OrganizationInfo()));
        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
        init.put("paytype", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("extraTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BUSSTYPE));
        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
        init.put("channelInfoList", channelWalletService.getIdsAndName());
        return init;
    }


}
