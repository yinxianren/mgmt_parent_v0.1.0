package com.rxh.controller.trading;


import com.rxh.pojo.Result;
import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.internal.playment.common.page.SearchInfo;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.internal.playment.common.enums.SystemConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName : TransOrderController
 * @Author : zoe
 * @Date : 2019/5/19 16:15
 */

@RestController
@RequestMapping("/transOrder")
public class TransOrderController {

    @Resource
    private TransOrderService transOrderService;
    @Resource
    private ConstantService constantService;
    @Resource
    private ChannelWalletService channelWalletService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;

    @Resource
    private MerchantInfoService merchantInfoService;
//    @Resource
//    private MerchantInfoService merchantInfoService;
@SystemLogInfo(description = "下发交易查询")
    @RequestMapping("/findPayOrderPage")
    @ResponseBody
    public PageResult findPayOrderPage(@RequestBody Page page ) {
        String agentMerchantId = UserInfoUtils.getMerchantId();
        page.getSearchInfo().setParentId(agentMerchantId);
        PageResult  pageResult = transOrderService.findTransOrder(page);
        return pageResult;
    }
    @RequestMapping("/getTransBankInfo")
    @ResponseBody
    public Result getTransBankInfo(@RequestBody  String transId) {
        Result result = transOrderService.getTransBankInfo(transId);
        return result;
    }
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public Result getProductInfo(@RequestBody String transId) {
        Result  result = transOrderService.getProductInfo(transId);
        return result;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationService .getIdsAndName());
        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
        init.put("bankcardType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
        init.put("merchants", merchantInfoService.getIdsAndName());
//        init.put("merchants", merchantInfoService.getIdsAndName());
//        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
//        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
//        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
//        init.put("merchants", merchantInfoService.getIdsAndName());
        return init;
    }
}
