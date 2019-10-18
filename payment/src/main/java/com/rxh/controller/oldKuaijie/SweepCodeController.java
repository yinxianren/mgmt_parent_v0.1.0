package com.rxh.controller.oldKuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class SweepCodeController {


    private final GlobalConfiguration gobalConfiguration;
    private final  SweepCodePaymentService sweepCodePaymentService;
    private final RecordPaymentSquareService recordPaymentSquareService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    public SweepCodeController(GlobalConfiguration gobalConfiguration, SweepCodePaymentService sweepCodePaymentService, RecordPaymentSquareService recordPaymentSquareService) {
        this.gobalConfiguration = gobalConfiguration;
        this.sweepCodePaymentService = sweepCodePaymentService;
        this.recordPaymentSquareService = recordPaymentSquareService;
    }


    /**
     * 扫码支付
     *
     * @param req       http请求
     * @param tradeInfo 交易信息
     * @return 交易结果
     */
    @RequestMapping(value = "/SweepCodepay", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String SweepCodepay(HttpServletRequest req, @RequestBody String tradeInfo)  throws  PayException {
       SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.sweepCodepay(systemOrderTrack,ParseTradeInfoSweepCode.SweepCode);

            String result = sweepCodePaymentService.payment(systemOrderTrack, tradeObjectSquare);
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



    /**
     * 获取风控前置表数据
     * @param req
     * @param tradeInfo
     * @return
     */
    private SystemOrderTrack getSystemOrderTrack(HttpServletRequest req, String tradeInfo, SystemOrderTrack systemOrderTrack) throws PayException {
        if (gobalConfiguration.isProduction() && req.getRemotePort() != 80) {
            logger.error("请求端口非法：" + req.getServerPort());
            throw new PayException("请求端口非80端口！当前端口：" + req.getServerPort(), 1100);
        }
        String reqUrl = req.getHeader(HttpHeaders.REFERER) == null ?
                req.getRequestURL().toString() :
                req.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(tradeInfo)) {
            logger.error("提交参数为空！");
            logger.info("来源站点：" + reqUrl + "，IP：" + IpUtils.getReallyIpForRequest(req));
            throw new PayException("提交参数为空！", 1000);
        }
        systemOrderTrack.setTradeInfo(tradeInfo);
        systemOrderTrack.setRefer(reqUrl);
        systemOrderTrack.setTradeTime(new Date());
        return systemOrderTrack;
    }




}
