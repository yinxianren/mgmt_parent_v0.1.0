package com.rxh.controller.oldKuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.oldKuaijie.GetOrderPayService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.GlobalConfiguration;
import com.rxh.utils.ParseTradeInfoSweepCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GetOrderPayController {




    private final GlobalConfiguration gobalConfiguration;
    private final SweepCodePaymentService sweepCodePaymentService;
    private final GetOrderPayService getOrderPayService;
    private final RecordPaymentSquareService recordPaymentSquareService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public GetOrderPayController(GlobalConfiguration gobalConfiguration, SweepCodePaymentService sweepCodePaymentService, GetOrderPayService getOrderPayService, RecordPaymentSquareService recordPaymentSquareService) {
        this.gobalConfiguration = gobalConfiguration;
        this.sweepCodePaymentService = sweepCodePaymentService;
        this.getOrderPayService = getOrderPayService;
        this.recordPaymentSquareService = recordPaymentSquareService;
    }

    @RequestMapping(value = "/getOrderPay", produces = "text/html;charset=UTF-8")
    public String getOrderPay(HttpServletRequest req, @RequestBody String tradeInfo)  throws PayException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.getOrderPay(systemOrderTrack);
            String result= getOrderPayService.payment(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result);
            return result;
        } catch (PayException e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }
}
