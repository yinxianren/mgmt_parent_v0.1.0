//package com.rxh.controller.trading;
//
//
//import com.rxh.pojo.Result;
//import com.internal.playment.common.page.Page;
//import com.rxh.pojo.base.PageResult;
//import com.rxh.service.ConstantService;
//import com.rxh.service.square.AgentMerchantInfoService;
//import com.rxh.service.square.ChannelWalletService;
//import com.rxh.service.square.MerchantInfoService;
//import com.rxh.service.square.OrganizationService;
//import com.rxh.service.trading.TransAuditService;
//import com.rxh.util.UserInfoUtils;
//import com.rxh.utils.HttpClientUtils;
//import com.rxh.utils.JsonUtils;
//import com.rxh.utils.StringUtils;
//import com.internal.playment.common.enums.SystemConstant;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.net.MalformedURLException;
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * @ClassName : TransAuditController
// * @Author : zoe
// * @Date : 2019/5/19 16:15
// */
//
//@RestController
//@RequestMapping("/transAudit")
//public class TransAuditController {
//
//    @Resource
//    private TransAuditService transAuditService;
//    @Resource
//    private ConstantService constantService;
//    @Resource
//    private ChannelWalletService channelWalletService;
//    @Resource
//    private OrganizationService organizationService;
//    @Resource
//    private AgentMerchantInfoService agentMerchantInfoService;
//    @Resource
//    private MerchantInfoService merchantInfoService;
//
//    @RequestMapping("/findPayOrderPage")
//    @ResponseBody
//    public PageResult findPayOrderPage(@RequestBody Page page ) {
//        PageResult  pageResult = transAuditService.findTransAudit(page);
//        return pageResult;
//    }
//
//    @RequestMapping("/init")
//    public Map<String, Object> init() {
//        Map<String, Object> init = new HashMap<>();
//        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
//        init.put("channels", channelWalletService.getIdsAndName());
//        init.put("organizations",organizationService .getIdsAndName());
//        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
//        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
//        init.put("bankcardType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
//        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
//        init.put("agentMerchants", agentMerchantInfoService.getAllIdAndName());
//        init.put("merchants", merchantInfoService.getIdsAndName());
////        init.put("merchantType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.MERCHANTTYPE));
////        init.put("identityType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
////        init.put("status", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.availableStatus));
////        init.put("merchants", merchantInfoService.getIdsAndName());
//        return init;
//    }
//    @RequestMapping("/shTransAudit")
//    public Result shTransAudit(HttpServletRequest request, @RequestBody String transId) throws MalformedURLException {
//        String transferer = UserInfoUtils.getName();
//        Map<String,String> map = new HashMap<>();
//        map.put("transId",transId);
//        map.put("transferer",transferer);
//         String jsonResult = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "http://inward.zrtcnet.com/toPay", JsonUtils.objectToJsonNonNull(map));
////        String jsonResult = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), "http://localhost:8070/toPay", JsonUtils.objectToJsonNonNull(map));
//        // HttpClientUtils.doPost(HttpClientUtils.getHttpClient(), siteUrl,map );
//        Result<String> result = new Result<>();
//        if(StringUtils.isNotEmpty(jsonResult)){
//            result.setCode(Result.SUCCESS);
//            result.setMsg(jsonResult);
//        }else {
//            result.setCode(Result.FAIL);
//            result.setMsg("审核失败");
//        }
//        return result;
//    }
//}
