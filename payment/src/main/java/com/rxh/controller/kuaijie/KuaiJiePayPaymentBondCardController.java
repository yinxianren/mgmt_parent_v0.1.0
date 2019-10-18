package com.rxh.controller.kuaijie;

import com.rxh.controller.AbstractCommonController;
import com.rxh.controller.IBondCardControll;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.pojo.sys.SysConstant;
import com.rxh.service.kuaijie.KuaiJiePayPaymentBondCardService;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/kuaijie")
public class KuaiJiePayPaymentBondCardController extends AbstractCommonController implements IBondCardControll {

    @Autowired
    protected KuaiJiePayPaymentBondCardService kuaiJiePayPaymentBondCardService;
    /**
     * 绑定银行卡
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value = "/bondCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String bondCard(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result = null;
        TradeObjectSquare tradeObjectSquare =null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentBondCardService.alliancePayBondCard(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.BONDCARD_APPLY);
            squareTrade=kuaiJiePayPaymentBondCardService.getBondParam(systemOrderTrack,tradeObjectSquare);
            if (squareTrade.getMerchantCard().getStatus() == SystemConstant.BANK_STATUS_SUCCESS){
                bankResult = new BankResult();
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("您的卡已绑定，请勿重复操作");
                result = kuaiJiePayPaymentBondCardService.getBondReturnJsonS(squareTrade,bankResult);
                systemOrderTrack.setTradeResult( result.length()>=500 ? result.substring(0,499) : result );
                return result;
            }
            bankResult=kuaiJiePayPaymentBondCardService.toBond(squareTrade);
            MerchantCard merchantCard=settingBondCardParam(squareTrade,bankResult);
            kuaiJiePayPaymentBondCardService.updateBondStatus(merchantCard);
            if (bankResult.getStatus() == (SystemConstant.BANK_STATUS_PENDING_PAYMENT)){
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
            }
            result = kuaiJiePayPaymentBondCardService.getBondReturnJson(squareTrade,bankResult);
            systemOrderTrack.setTradeResult( result.length()>=500 ? result.substring(0,499) : result );
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException){
                logger.warn("【绑定银行卡】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (systemOrderTrack != null)  recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }


    /**
     * 确定绑定银行卡
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     *
     *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value = "/confirmBond", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmBond(HttpServletRequest req, @RequestBody String tradeInfo)   {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result = null;
        SquareTrade squareTrade=null;
        BankResult bankResult=null;
        TradeObjectSquare tradeObjectSquare=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentBondCardService.alliancePayConfirmBond(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.BONDCARD_CONFIRM);
            squareTrade=kuaiJiePayPaymentBondCardService.getConfirmBondParam(systemOrderTrack, tradeObjectSquare);
            if (squareTrade.getMerchantCard().getStatus() == ((int)SystemConstant.BANK_STATUS_SUCCESS)){
                bankResult=kuaiJiePayPaymentBondCardService.confirmBond(squareTrade);
            }else {
                bankResult=kuaiJiePayPaymentBondCardService.confirmBond(squareTrade);
                MerchantCard merchantCard=settingBondCardParam(squareTrade,bankResult);
                kuaiJiePayPaymentBondCardService.updateBondStatus(merchantCard);
            }
            result = kuaiJiePayPaymentBondCardService.getBondReturnJson(squareTrade,bankResult);
            systemOrderTrack.setTradeResult( result.length()>=500 ? result.substring(0,499) : result );
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException){
                logger.warn("【确定绑定银行卡】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (systemOrderTrack != null)  recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }


    /**
     * 重新获取绑卡验证码
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     *
     *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value = "/reGetBondCode", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String reGetBondCode(HttpServletRequest req, @RequestBody String tradeInfo)  {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        String result=null;
        SquareTrade squareTrade=null;
        TradeObjectSquare tradeObjectSquare=null;
        BankResult bankResult=null;
        try {
            systemOrderTrack = super.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            tradeObjectSquare = kuaiJiePayPaymentBondCardService.alliancePayBondSmsCode(systemOrderTrack);
            tradeObjectSquare.setInnerType(SystemConstant.BONDCARD_SMS);
            squareTrade=kuaiJiePayPaymentBondCardService.getBondCodeParam( systemOrderTrack,tradeObjectSquare);
            bankResult=kuaiJiePayPaymentBondCardService.toGetBondCode(squareTrade);
            result = kuaiJiePayPaymentBondCardService.getBondReturnJson(squareTrade,bankResult);
            systemOrderTrack.setTradeResult( result.length()>=500 ? result.substring(0,499) : result );
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException){
                logger.warn("【重新获取绑卡验证码】 提示：{}",e.getMessage());
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = kuaiJiePayPaymentBondCardService.errorResult(systemOrderTrack,tradeObjectSquare,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (systemOrderTrack != null)  recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }

}
