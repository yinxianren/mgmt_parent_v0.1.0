package com.rxh.controller.anew.order;

import com.internal.playment.common.enums.StatusEnum;
import com.rxh.pojo.Result;
import com.rxh.pojo.base.Page;
import com.rxh.service.AnewMerchantInfoService;
import com.rxh.service.AnewPayOrderService;
import com.rxh.service.ConstantService;
import com.rxh.service.OrganizationInfoService;
import com.rxh.service.square.*;
import com.rxh.service.trading.PayOrderService;
import com.rxh.spring.annotation.SystemLogInfo;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.MerchantInfo;
import com.rxh.utils.SystemConstant;
import com.rxh.vo.ResponseVO;
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
    private PayOrderService payOrderService;
    @Resource
    private ConstantService constantService;
    @Resource
    private ChannelInfoService channelInfoService;
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
        init.put("payType", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PAYTYPE));
        init.put("orderStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.ORDERSTATUS));
        init.put("settleStatus", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.SETTLESTATUS));
//        init.put("channels", channelWalletService.getIdsAndName());
        init.put("organizations",organizationInfoService .getAll(null).getData());
        init.put("merchants", anewMerchantInfoService.getMerchants(null));
//        init.put("identityTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.IDENTITYTYPE));
//        init.put("bankcardTypes", constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.BANKCARDTYPE));
//        init.put("agents",agentMerchantInfoService.getAllIdAndName());
        init.put("productTypes",constantService.getConstantByGroupNameAndSortValueIsNotNULL(SystemConstant.PRODUCTTYPE));

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
    @RequestMapping(value = "/getProductInfo",method = RequestMethod.POST)
    public Result getProductInfo(@RequestBody String payId) {
        Result  result = payOrderService.getProductInfo(payId);
        return result;
    }

    @RequestMapping(value = "/getChannels")
    public Result getChannels(@RequestBody MerchantInfo merId) {
        List<ChannelInfo> channelInfos = channelInfoService.selectByMerId(merId.getMerId());
        Result result = new Result();
        result.setData(channelInfos);
        return result;
    }
}
