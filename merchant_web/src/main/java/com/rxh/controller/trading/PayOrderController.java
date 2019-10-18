package com.rxh.controller.trading;

import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.trading.PayOrderService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.SystemConstant;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : PayOrderController
 * @Author : zoe
 * @Date : 2019/5/19 14:42
 */
@RestController
@RequestMapping(value = "/payOrder")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private ConstantService constantService;
    @Resource
    private ChannelWalletService channelWalletService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private MerchantInfoService merchantInfoService;
    @Resource
    private AgentMerchantInfoService agentMerchantInfoService;

    @SystemLogInfo(description = "支付交易查询")
    @RequestMapping(value="/findPayOrderPage")
    @ResponseBody
    public PageResult  findPayOrderPage(@RequestBody Page page ) {
        page.getSearchInfo().setMerId(String.valueOf(UserInfoUtils.getMerchantId()));
        PageResult pageResult = payOrderService.findPayOrder(page);
        return pageResult;
    }

    @RequestMapping("/init")
    public Map<String, Object> init() {
        Map<String, Object> init = new HashMap<>();
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationService .getIdsAndName());
        init.put("identityTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
        init.put("bankcardTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
        init.put("remark", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.remark));
        init.put("agents",agentMerchantInfoService.getAllIdAndName());
        return init;
    }

    @RequestMapping("/getCardHolderInfo")
    public Result getCardHolderInfo(@RequestBody  String payId) {
        Result result = payOrderService.getCardHolderInfo(payId);
        return result;
    }
    @RequestMapping(value = "/getProductInfo",method = RequestMethod.POST)
    public Result getProductInfo(@RequestBody String payId) {
        Result  result = payOrderService.getProductInfo(payId);
        return result;
    }
}
