package com.rxh.controller.anew.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.rxh.pojo.Result;
import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.AnewChannelService;
import com.rxh.service.AnewChannelWalletService;
import com.rxh.service.ConstantService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelWallet;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channelWallet")
public class AnewChannelWalletController {

    @Resource
    private NewSystemConstantService constantService;
    @Autowired
    private AnewChannelWalletService anewChannelWalletService;
    @Autowired
    private AnewChannelService anewChannelService;

    @SystemLogInfo(description = "平台钱包查询")
    @RequestMapping("/search")
    public ResponseVO search(@RequestBody ChannelWalletTable channelInfo){
        try {
            return anewChannelWalletService.search(channelInfo);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }
    }


    @RequestMapping("/idsInit")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("paytype", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("channels", anewChannelService.getAll(null).getData());
//        init.put("organizations", organizationService.getIdsAndName());
        return init;
    }

//    @RequestMapping("/batchDel")
//    public Result delete(@RequestBody List<String> ids){
//        return channelWalletService.deleteByPrimaryKey(ids);
//    }

    @RequestMapping("/init")
    public Map<String, Object> init1() {
        Map<String, Object> init = new HashMap<>();
        init.put("productTypes", constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());
//        init.put("detailsTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.DETAILSTYPE));
        init.put("channels", anewChannelService.getAll(null).getData());
        return init;
    }

    @RequestMapping(value="/findChannelWallteDetailsPage")
    public ResponseVO search(@RequestBody Page page ){
        try {
            return  anewChannelWalletService.pageByDetails(page);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage(StatusEnum._1.getRemark());
            return responseVO;
        }

    }

}
