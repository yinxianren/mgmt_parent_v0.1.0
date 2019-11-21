package com.rxh.controller.anew.order;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.page.Page;
import com.rxh.service.merchant.AnewMerchantInfoService;
import com.rxh.service.AnewPayOrderService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.system.NewSystemConstantService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.internal.playment.common.enums.SystemConstant;
import com.internal.playment.common.page.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payOrder")
@Slf4j
public class AnewPayOrderController {

    @Resource
    private NewSystemConstantService constantService;
    @Autowired
    private AnewPayOrderService anewPayOrderService;
    @Autowired
    private AnewMerchantInfoService anewMerchantInfoService;
    @Autowired
    private OrganizationInfoService organizationInfoService;

    @SystemLogInfo(description = "支付交易查询")
    @RequestMapping(value="/findPayOrderPage")
    @ResponseBody
    public ResponseVO findPayOrderPage(@RequestBody Page page ) {
        try {
            return anewPayOrderService.getList(page);
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
        init.put("payType", constantService.getConstantByGroupName(SystemConstant.PAYTYPE).getData());
        init.put("orderStatus", constantService.getConstantByGroupName(SystemConstant.ORDERSTATUS).getData());
        init.put("settleStatus", constantService.getConstantByGroupName(SystemConstant.SETTLESTATUS).getData());
//        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationInfoService .getAll(null).getData());
        init.put("merchants", anewMerchantInfoService.getMerchants(null));
//        init.put("identityTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
//        init.put("bankcardTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
//        init.put("agents",agentMerchantInfoService.getAllIdAndName());
        init.put("productTypes",constantService.getConstantByGroupName(SystemConstant.PRODUCTTYPE).getData());

        return init;
    }

    @RequestMapping("/getCardHolderInfo")
    public ResponseVO getCardHolderInfo(@RequestParam(value = "payId") String payId) {
        try {
            return anewPayOrderService.getCardHolderInfo(payId);
        }catch (Exception e){
            e.printStackTrace();
            ResponseVO responseVO = new ResponseVO();
            responseVO.setCode(StatusEnum._1.getStatus());
            responseVO.setMessage("失败");
            return responseVO;
        }

    }
}
