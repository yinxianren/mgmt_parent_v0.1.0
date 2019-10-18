package com.rxh.controller.nonVerifyKuaijie;

import com.rxh.controller.AbstractCommonController;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.nonVerifyKuaijie.NonVerifyKuaiJieBalanceQueryService;
import com.rxh.square.pojo.SystemOrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping("/nvkuaijie")
public class NonVerifyKuaiJieBalanceQueryController extends AbstractCommonController {

    @Autowired
    private NonVerifyKuaiJieBalanceQueryService nonVerifyKuaiJieBalanceQueryService;

    @RequestMapping(value = "/queryBalance", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String queryBalance(HttpServletRequest req, @RequestBody String tradeInfo)  throws Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        TradeObjectSquare tradeObjectSquare= null;
//        Lock lock=null;
        String merId=null;
        SquareTrade trade=null;
        BankResult bankResult=null;
        String result =null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = nonVerifyKuaiJieBalanceQueryService.verifyParam(systemOrderTrack,nonVerifyKuaiJieBalanceQueryService.noVerifyKuaijieBalanceQueryValidate);
//            merId=systemOrderTrack.getMerId();
//            lock=merchantsLockUtils.getLock(merId);
//            isNull(lock,format("【无验证快捷余额查询】商户号：%s,获取到商户锁失败",merId));
//            lock.lock();
            trade=nonVerifyKuaiJieBalanceQueryService.getQueryParam( systemOrderTrack, tradeObjectSquare);
            bankResult=nonVerifyKuaiJieBalanceQueryService.nonVerifyKuaiJieToQueryBalance(trade);
            result =nonVerifyKuaiJieBalanceQueryService.getReturnJson(bankResult,trade);
            logger.info("无验证快捷余额查询result："+result);
        } catch (Exception e) {
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            result = nonVerifyKuaiJieBalanceQueryService.getErrorReturnJson(systemOrderTrack,tradeObjectSquare,trade,bankResult,e.getMessage());
            e.printStackTrace();
        } finally {
            if (systemOrderTrack != null)  recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
//            if(isNotNull(lock)) lock.unlock();
            return result;
        }


    }
}
