package com.rxh.controller.nonVerifyKuaijie;

import com.rxh.controller.AbstractCommonController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.nonVerifyKuaijie.NonVerifyKuaijiePayService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.payLock.MerchantsLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;

@RestController
@RequestMapping("/nvkuaijie")
public class NonVerifyKuaijiePayController extends AbstractCommonController {

    @Autowired
    private NonVerifyKuaijiePayService nonVerifyKuaijiePayService;

    @Autowired
    private MerchantsLock merchantsLock;
    @Autowired
    private RecordPaymentSquareService recordPaymentSquareService;
    @RequestMapping(value ="/pay",produces = "text/html;charset=UTF-8")
    public String nonVerifyKuaijiePay(@RequestBody String tradeInfo, HttpServletRequest req){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare = null;
        SquareTrade trade=null;
        String merId = null;
        Lock lock=null;
        BankResult bankResult = null;
        try {
            systemOrderTrack =getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = nonVerifyKuaijiePayService.verifyParam(systemOrderTrack,nonVerifyKuaijiePayService.creditCardPayValidate);
            tradeObjectSquare.setBizType("6");
            merId=systemOrderTrack.getMerId();
            lock= merchantsLock.getLock(merId);
            isNull(lock,format("【无验证快捷支付---->收单任务】商户号：%s,获取到商户锁失败",merId));
            lock.lock();
            trade=nonVerifyKuaijiePayService.nonVerifyKuaiJieToSaveOrder( systemOrderTrack, tradeObjectSquare);
            bankResult=nonVerifyKuaijiePayService.nonVerifyKuaiJieToPay(trade);
            result =  nonVerifyKuaijiePayService.nonVerifyKuaiJieToUpdataPayAndPayWallet_MQ(trade,  bankResult);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        }catch (Exception e){
            if ( e instanceof PayException) {
                logger.warn("【无验证快捷支付---->收单任务】 提示：{}", e.getMessage());
            } else {
                e.printStackTrace();
            }
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if(null == result)  result = nonVerifyKuaijiePayService.getReturnJson_1(systemOrderTrack,tradeObjectSquare,trade,bankResult,e.getMessage());
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            if(isNotNull(lock)) lock.unlock();
            return result;
        }

    }
}
