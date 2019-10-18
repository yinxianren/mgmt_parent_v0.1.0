package com.rxh.controller.kuaijie;


import com.rxh.controller.AbstractCommonController;
import com.rxh.pojo.merchant.MerchantPayOrderShortMessage;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import com.rxh.service.oldKuaijie.MerchantPayOrderShortMessageServiceCommonService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.ParseTradeInfoSweepCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/kuaijie/payment")
public class MerchantPayOrderShortMessageController extends AbstractCommonController {


    @Autowired
    private MerchantPayOrderShortMessageServiceCommonService merchantPayOrderShortMessageService;
    @Autowired
    private SweepCodePaymentService sweepCodePaymentService;
    @Autowired
    private RecordPaymentSquareService recordPaymentSquareService;
    /**
     *  通用的短信确认
     * @return
     *  version 0.0.1
     *    所需参数：商户号（merId）、终端商户号（TerminalMerId）、平台订单号（merOrderId）
     *
     */
    @RequestMapping(value="/confirmSMS", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmSMS(HttpServletRequest req, @RequestBody String tradeInfo) throws Exception {
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        try{
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            MerchantPayOrderShortMessage merchantPayOrderShortMessage = ParseTradeInfoSweepCode.merchantPayOrderShortMessageVerify(systemOrderTrack);
            String result = merchantPayOrderShortMessageService.confirmMerchantPayOrderShortMessage(systemOrderTrack, merchantPayOrderShortMessage);
            return  result;
        }catch (Exception e){
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            logger.error(tradeInfo, e);
            e.printStackTrace();
            throw e;
        }finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }




}
