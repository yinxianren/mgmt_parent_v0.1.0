package com.rxh.controller.kuaijie;


import com.rxh.controller.AbstractPayController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.AlliancePayService;
import com.rxh.service.kuaijie.AlliancePayinwardService;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.MerchantsLockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("/kuaijie")
public class AlliancePayController extends AbstractPayController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private  AlliancePayinwardService alliancePayinwardService;
    @Autowired
    private  RecordSquareService recordSquareService;
    @Autowired
    private HaiYiPayService haiYiPayService;
    @Autowired
    private AlliancePayService alliancePayService;
    @Autowired
    private MerchantsLockUtils merchantsLockUtils;
    /**
     * 代付
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/getTransOrder", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getOrder(HttpServletRequest req, @RequestBody String tradeInfo)  throws  Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result = null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        TradeObjectSquare tradeObjectSquare =null;
        Lock lock=null;
        String merId=null;
        try {
            systemOrderTrack =super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = alliancePayService.alliancePay(systemOrderTrack);
            merId=systemOrderTrack.getMerId();
            lock=merchantsLockUtils.getLock(merId);
            isNull(lock,format("【代付】商户号：%s,获取到商户锁失败",merId));
            lock.lock();
            squareTrade=alliancePayService.getSquareTrade(systemOrderTrack, tradeObjectSquare);
            bankResult=alliancePayService.toPay(squareTrade);
            result =alliancePayService.kuaiJieToUpdataTransInfoAndPayWallet_MQ(squareTrade,bankResult);
//            result = alliancePayinwardService.toSaveOrder(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException) {
                result = alliancePayService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage(),squareTrade,((PayException) e).getResultCode());
                logger.warn("【代付】 提示：{}",e.getMessage());
            }
            else {
                result = alliancePayService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage(),squareTrade);
                e.printStackTrace();
            }
        } finally {
            if (systemOrderTrack != null) {
                recordSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            if(isNotNull(lock)) lock.unlock();
            return result;
        }
    }




    @RequestMapping(value = "/toPay")
    @ResponseBody
    public String toPay(@RequestBody String map) throws Exception {
        String result = alliancePayinwardService.goPay(map);
        return  result;
    }






}
