package com.rxh.controller.haiyi;

import com.rxh.controller.AbstractPayController;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.scheduler.HaiYiTokenSchedulerTask;
import com.rxh.service.kuaijie.RecordSquareService;
import com.rxh.service.haiyi.HaiYiPayService;
import com.rxh.square.pojo.SystemOrderTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  描述： 海懿支付
 * @author  panda
 * @date 20190710
 *
 * /haiYiPay/testMQ
 */
@Controller
@RequestMapping("/inwardPay")
public class HaiYiPayController extends AbstractPayController {

    @Autowired
    private HaiYiPayService haiYiPayService;

    @Autowired
    private  RecordSquareService recordSquareService;

//    @Autowired
    private HaiYiTokenSchedulerTask haiYiTokenSchedulerTask;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final Lock lock=new ReentrantLock(true);

    @RequestMapping(value="/payForAnother" , produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String haiYiPayForAnother(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result=null;
        TradeObjectSquare tradeObjectSquare =null;
        SquareTrade trade=null;
        BankResult bankResult=null;
        try{
            lock.lock();
            systemOrderTrack = getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = haiYiPayService.verifyMustParamOnHaiYiPayForAnother(systemOrderTrack);
            trade = haiYiPayService.haiYiPayForAnotherToSaveOrder(systemOrderTrack, tradeObjectSquare);
            bankResult=haiYiPayService.haiYiPayForAnotherToPay(trade);
            result=haiYiPayService.haiYiPayForAnotherToUpdataPayInfoAndPayWallet(trade,bankResult);
        }catch (Exception e){
            if(null == result){
                result= haiYiPayService.getReturnJson_1( systemOrderTrack, tradeObjectSquare, trade, bankResult,e);
            }
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            if( e instanceof PayException){
                logger.warn("【海懿代付】商户号:{},提示：{}",systemOrderTrack.getMerId(),e.getMessage());
            }else {
                e.printStackTrace();
            }
        }finally {
            if(StringUtils.isEmpty(systemOrderTrack.getTradeResult())){
                systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            }
            if (systemOrderTrack != null) {
                recordSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
            lock.unlock();
            return result;
        }
    }

    /**
     *  手动获取token
     * @return
     */
    @RequestMapping(value="/handOperationGetToken" , produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String handOperationGetToken(){
        return haiYiTokenSchedulerTask.handOperationGetToken();
    }
}
