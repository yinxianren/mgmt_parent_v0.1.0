package com.rxh.controller.oldKuaijie;


import com.rxh.exception.PayException;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.NoCardPayService;
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
public class NoCardPayController {

    private final GlobalConfiguration globalConfiguration;

    private final RecordPaymentSquareService recordPaymentSquareService;
    private final SweepCodePaymentService sweepCodePaymentService;

    private final NoCardPayService noCardPayService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public NoCardPayController(NoCardPayService noCardPayService, GlobalConfiguration globalConfiguration, RecordPaymentSquareService recordPaymentSquareService, SweepCodePaymentService sweepCodePaymentService) {
        this.globalConfiguration = globalConfiguration;
        this.noCardPayService =  noCardPayService;
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.sweepCodePaymentService = sweepCodePaymentService;
    }


    /**
     * 快钱快捷支付入口
     */
    @RequestMapping(value = "/noCardPayEntry")
    @ResponseBody
    public String noCardPayEntry(HttpServletRequest req, @RequestBody String tradeInfo) throws  PayException{
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try{
            systemOrderTrack =  sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            // 报文参数校验
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.parseNoCardInfo(systemOrderTrack);
            // 表操作
            String result  = noCardPayService.payment(systemOrderTrack,tradeObjectSquare);
            systemOrderTrack.setTradeResult(result);
            return  result;
        } catch (PayException e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getPayExceptionMsg());
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
    private SystemOrderTrack getSystemOrderTrack(HttpServletRequest req, String tradeInfo,SystemOrderTrack systemOrderTrack) throws PayException{
        if (globalConfiguration.isProduction() && req.getRemotePort() != 80) {
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
        // String tradeInfoDecode = new String(Base64.getDecoder().decode(tradeInfo.getBytes()));
        // try {
        //     tradeInfoDecode = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        //     logger.error(tradeInfo, e);
        //     throw new PayException("请求报文无法解析！" + e.getMessage(), 1100);
        // }
        systemOrderTrack.setTradeInfo(tradeInfo);
        systemOrderTrack.setRefer(reqUrl);
        systemOrderTrack.setTradeTime(new Date());
        return systemOrderTrack;
    }

}