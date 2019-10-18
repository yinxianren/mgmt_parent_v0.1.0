package com.rxh.controller;

import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.IpUtils;
import com.rxh.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;

/**
 * 描述：支付 controller共有部分
 * @author  panda
 * @date 20190710
 */
public abstract class AbstractPayController implements PayAssert, PayUtil {


    private Logger logger = LoggerFactory.getLogger(AbstractPayController.class);
    /**
     * 获取风控前置表数据
     * @param req
     * @param tradeInfo
     * @return
     */
    protected SystemOrderTrack getSystemOrderTrack(HttpServletRequest req, String tradeInfo, SystemOrderTrack systemOrderTrack) throws PayException {

        String reqUrl = req.getHeader(HttpHeaders.REFERER) == null ?
                req.getRequestURL().toString() :
                req.getHeader(HttpHeaders.REFERER);
        if (StringUtils.isBlank(tradeInfo)) {
            logger.error("提交参数为空！");
            logger.info("来源站点：" + reqUrl + "，IP：" + IpUtils.getReallyIpForRequest(req));
            throw new PayException("提交参数为空！", 1000);
        }
        try {
            String tradeInfoDecode = new String(Base64.decodeBase64(tradeInfo.getBytes()));
            tradeInfo = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        } catch (Exception e) {
            logger.error("解析失败，打印原始报文tradeInfo："+tradeInfo);
            throw new PayException("提交参数无法解析");
        }
        systemOrderTrack.setTradeInfo(tradeInfo);
        systemOrderTrack.setRefer(reqUrl);
        systemOrderTrack.setTradeTime(new Date());
        return systemOrderTrack;
    }

}
