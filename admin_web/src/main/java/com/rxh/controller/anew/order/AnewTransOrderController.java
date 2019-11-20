package com.rxh.controller.anew.order;


import com.internal.playment.common.enums.StatusEnum;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.AnewTransOrderService;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transOrder")
@Slf4j
public class AnewTransOrderController {

    @Resource
    private TransOrderService transOrderService;
    @Resource
    private ConstantService constantService;
    @Autowired
    private AnewTransOrderService anewTransOrderService;
    @Autowired
    private AnewMerchantInfoService anewMerchantInfoService;
    @Autowired
    private OrganizationInfoService organizationInfoService;

    @SystemLogInfo(description = "下发交易查询")
    @RequestMapping("/findPayOrderPage")
    @ResponseBody
    public ResponseVO findPayOrderPage(@RequestBody Page page ) {
       try {
           return anewTransOrderService.page(page);
       }catch (Exception e){
           e.printStackTrace();
           return new ResponseVO(StatusEnum._1.getStatus(),StatusEnum._1.getRemark());
       }
    }

    @RequestMapping("/getTransBankInfo")
    @ResponseBody
    public ResponseVO getTransBankInfo(@RequestParam("platformOrderId") String platformOrderId) {
        return anewTransOrderService.getTransBankInfo(platformOrderId);
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
        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
        init.put("organizations",organizationInfoService .getAll(null).getData());
        init.put("merchants", anewMerchantInfoService.getMerchants(null));
        init.put("productTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));
        return init;
    }
}
