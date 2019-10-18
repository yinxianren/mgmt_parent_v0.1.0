package com.rxh.controller.kuaijie;


import com.rxh.exception.PayException;
import com.rxh.pojo.Result;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.InwardSquareService;
import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.GlobalConfiguration;
import com.rxh.utils.IpUtils;
import com.rxh.utils.ParseTradeInfoSquare;
import com.rxh.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Date;


@Controller
public class PaymentSquareController {

    private final GlobalConfiguration globalConfiguration;

    private final RecordSquareService recordSquareService;

    private final InwardSquareService inwardSquareService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PaymentSquareController(InwardSquareService inwardSquareService, GlobalConfiguration globalConfiguration, RecordSquareService recordSquareService) {
        this.globalConfiguration = globalConfiguration;
        this.inwardSquareService = inwardSquareService;
        this.recordSquareService = recordSquareService;
    }
    /**
     * 代付入口
     */
    @RequestMapping(value = "/transEntry")
    @ResponseBody
    public Result transEntry(HttpServletRequest req, @RequestBody String tradeInfo) throws PayException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try{
            // 系统前置对象
            systemOrderTrack = getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            // 报文参数校验
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSquare.parse(systemOrderTrack);
            // 表操作
            inwardSquareService.payment(systemOrderTrack,tradeObjectSquare);
            systemOrderTrack.setTradeResult("成功");
            return  new Result(Result.SUCCESS,"成功");
        } catch (PayException e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getPayExceptionMsg());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }

    @RequestMapping(value = "/toPay")
    @ResponseBody
    public String toPay(@RequestBody String map) throws Exception {
        String result = inwardSquareService.goPay(map);
        return  result;
    }

    /**
     * 获取风控前置表数据
     * @param req
     * @param tradeInfo
     * @return
     */
    private SystemOrderTrack getSystemOrderTrack(HttpServletRequest req, String tradeInfo,SystemOrderTrack systemOrderTrack) throws PayException{
        // if (globalConfiguration.isProduction() && req.getRemotePort() != 80) {
        //     logger.error("请求端口非法：" + req.getServerPort());
        //     throw new PayException("请求端口非80端口！当前端口：" + req.getServerPort(), 1100);
        // }
        String reqUrl = req.getHeader(HttpHeaders.REFERER) == null ?
                req.getRequestURL().toString() :
                req.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(tradeInfo)) {
            logger.error("提交参数为空！");
            logger.info("来源站点：" + reqUrl + "，IP：" + IpUtils.getReallyIpForRequest(req));
            throw new PayException("提交参数为空！", 1000);
        }
        String tradeInfoDecode = new String(Base64.getDecoder().decode(tradeInfo.getBytes()));
        try {
            tradeInfo = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new PayException("提交参数无法解析", 1001);
        }
        systemOrderTrack.setTradeInfo(tradeInfo);
        systemOrderTrack.setRefer(reqUrl);
        systemOrderTrack.setTradeTime(new Date());
        return systemOrderTrack;
    }

}