package com.rxh.controller.anew.merchant;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.rxh.service.*;
import com.rxh.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchantSetting")
public class AnewMerchantSettingController {

    @Autowired
    private AnewMerchantSettingService anewMerchantSettingService;
    @Autowired
    private OrganizationInfoService organizationInfoService;
    @Autowired
    private AnewChannelService anewChannelService;

    @RequestMapping("/search")
    public ResponseVO search(@RequestBody MerchantSettingTable merchantSetting){
        try {
            return anewMerchantSettingService.getList(merchantSetting);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    @RequestMapping("/update")
    public ResponseVO update(@RequestBody Map<String,Object> merchantSetting){
        try {
            return anewMerchantSettingService.batchUpdate(merchantSetting);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
//        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
//        init.put("channelLevel", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.channelLevel));
//        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
//        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
        OrganizationInfoTable o = new OrganizationInfoTable();
        o.setStatus(1);
        List<OrganizationInfoTable> infoTables = (List<OrganizationInfoTable>)organizationInfoService.getAll(o).getData();
        Map organMap = new HashMap();
        for (OrganizationInfoTable infoTable : infoTables){
            organMap.put(infoTable.getOrganizationId(),infoTable.getOrganizationName());
        }
        ChannelInfoTable channelInfoTable = new ChannelInfoTable();
        channelInfoTable.setStatus(1);
        List<ChannelInfoTable> channelInfoTables = (List<ChannelInfoTable>)anewChannelService.getAll(channelInfoTable).getData();
        Map proMap = new HashMap();
        for (ChannelInfoTable channelInfoTable1 : channelInfoTables){
            proMap.put(channelInfoTable1.getOrganizationId(),channelInfoTable1.getChannelId());
        }
        init.put("organizations", infoTables);
        init.put("channels",channelInfoTables);
//        init.put("organizations", organizationService.getIdsAndName());
//        init.put("channels", channelWalletService.getIdsAndName());
        return init;
    }

    @RequestMapping("/getChannels")
    public ResponseVO getChannels(@RequestParam(value = "organizationIds",required = false) String organizationIds){
        try {
            return anewMerchantSettingService.getChannels(organizationIds);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }

}
