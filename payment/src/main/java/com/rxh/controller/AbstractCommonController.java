package com.rxh.controller;

import com.rxh.activeMQ.TransOrderMQ;
import com.rxh.exception.PayException;
import com.rxh.payInterface.PayAssert;
import com.rxh.payInterface.PayUtil;
import com.rxh.service.kuaijie.KuaijiePayQueryService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.payLock.MerchantsLock;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

public abstract class AbstractCommonController implements PayAssert, PayUtil {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RecordPaymentSquareService recordPaymentSquareService;
    @Autowired
    protected MerchantsLock merchantsLock;
    @Resource
    protected KuaijiePayQueryService kuaijiePayQueryService;
    @Autowired
    protected TransOrderMQ transOrderMQ;

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

        try {
            isNull(tradeInfo,"提交参数为空！");
//            logger.info(String.format("商户请求原始报文%s",tradeInfo));
            String tradeInfoDecode = new String(Base64.decodeBase64(tradeInfo.getBytes()));
            tradeInfo = URLDecoder.decode(tradeInfoDecode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("提交参数无法解析，打印原始报文:"+tradeInfo);
            throw new PayException("提交参数无法解析");
        }finally {
            systemOrderTrack.setTradeInfo(tradeInfo);
            systemOrderTrack.setRefer(reqUrl);
            systemOrderTrack.setTradeTime(new Date());
            return systemOrderTrack;
        }
    }

}
