package com.rxh.controller.trading;


import com.rxh.pojo.Excel;
import com.rxh.pojo.Result;
import com.internal.playment.common.page.Page;
import com.rxh.pojo.base.PageResult;
import com.rxh.service.ConstantService;
import com.rxh.service.square.AgentMerchantInfoService;
import com.rxh.service.square.ChannelWalletService;
import com.rxh.service.square.MerchantInfoService;
import com.rxh.service.square.OrganizationService;
import com.rxh.service.trading.TransOrderService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.BatchData;
import com.rxh.util.ExcelUtils;
import com.rxh.util.UserInfoUtils;
import com.rxh.utils.BatchOperationUtils;
import com.rxh.utils.SystemConstant;
import org.apache.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
        page.getSearchInfo().setMerId(String.valueOf(UserInfoUtils.getMerchantId()));
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
        //String merchantId = UserInfoUtils.getMerchantId();
        Map<String, Object> init = new HashMap<>();
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationService .getIdsAndName());
        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
        init.put("bankcardType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
//        init.put("merchants", agentMerchantInfoService.getAllMerchantIdAndName(merchantId));
//        init.put("agentMerchants", agentMerchantInfoService.getAllMerchantIdAndName(merchantId));
//        init.put("merchants", merchantInfoService.getIdsAndName());
//        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
//        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
//        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
//        init.put("merchants", merchantInfoService.getIdsAndName());
        return init;
    }


    /**
     * 获取交易信息
     *
     * @param page 分页对象
     * @return 查询结果
     */
    @SystemLogInfo(description = "批量代付查询")
    @RequestMapping(value = "/getBatchRepayOrderList")
    @ResponseBody
    public PageResult getItemList(@RequestBody Page page) {
        page.getSearchInfo().setMerId(UserInfoUtils.getMerchantId().toString());
        PageResult orderList = transOrderService.getBatchRepayList(page);
        return orderList;
    }

    @RequestMapping("/getBatchRepayInit")
    @ResponseBody
    public Map<String,Object> getBatchRepayInit() {
        String merId=UserInfoUtils.getMerchantId().toString();
        return transOrderService.getBatchRepayInit(merId);
    }

    /**
     * 批量操作
     *
     * @param multipartFiles 上传文件
     * @return 批量操作结果
     */
    @SystemLogInfo(description = "订单批量操作")
    @RequestMapping(value = "/batchOperation")
    @ResponseBody
    public int batchOperation(@RequestParam("file") MultipartFile multipartFiles) throws IOException {
        String merId = UserInfoUtils.getMerchantId();
        Excel excel = ExcelUtils.getWorkbooks(multipartFiles);


        if (excel != null) {
            List<BatchData> orderChanges =BatchOperationUtils.getOperation(excel, SecurityContextHolder.getContext().getAuthentication().getName());
            return transOrderService.batchRepay(orderChanges, excel.getType(),merId);
        }
        return 1;
    }


}
