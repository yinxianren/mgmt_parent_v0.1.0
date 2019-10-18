package com.rxh.controller.nonVerifyKuaijie;

import com.rxh.controller.AbstractPayController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.service.nonVerifyKuaijie.NonVerifyKuaijieInwardService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.MerchantsLockUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
@RestController
@RequestMapping("/nonVerifyKuaijie")
public class NonVerifyKuaijieInwardController extends AbstractPayController {

    private final Logger logger = LoggerFactory.getLogger(NonVerifyKuaijieInwardController.class);
    @Resource
    private NonVerifyKuaijieInwardService nonVerifyKuaijieInwardService;
    @Autowired
    private RecordSquareService recordSquareService;
    @Autowired
    private MerchantsLockUtils merchantsLockUtils;

    private static final Lock lock=new ReentrantLock(true);

    @RequestMapping(value = "/inwardForAnother",produces = "text/html;charset=UTF-8")
    public String NonVerifyKuaijieForAnother(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare =null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        Lock lock=null;
        String merId=null;
        try{
            systemOrderTrack =super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = nonVerifyKuaijieInwardService.verifyMustParamOnHaiYiPayForAnother(systemOrderTrack);
            merId=systemOrderTrack.getMerId();
            lock=merchantsLockUtils.getLock(merId);
            isNull(lock,format("【代付】商户号：%s,获取到商户锁失败",merId));
            lock.lock();
            squareTrade=nonVerifyKuaijieInwardService.nonVerifyPayForAnotherToSaveOrder(systemOrderTrack, tradeObjectSquare);
            bankResult=nonVerifyKuaijieInwardService.nonVerifyPayForAnotherToPay(squareTrade);
            result =nonVerifyKuaijieInwardService.nonVerifyPayForAnotherToUpdataPayAndPayWallet(squareTrade,bankResult);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        }catch (Exception e){
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(null == result) result = nonVerifyKuaijieInwardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage());
            if(e instanceof PayException) logger.warn("【无验证快捷绑卡】 提示：{}",e.getMessage());
            else e.printStackTrace();
        } finally {
            lock.unlock();
            if (systemOrderTrack != null)  recordSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }
}
