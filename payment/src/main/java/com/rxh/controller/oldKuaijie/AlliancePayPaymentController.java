package com.rxh.controller.oldKuaijie;

import com.rxh.exception.PayException;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.service.oldKuaijie.AlliancePayPaymentServiceCommonService;
import com.rxh.service.oldKuaijie.RecordPaymentSquareService;
import com.rxh.service.oldKuaijie.SweepCodePaymentService;
import com.rxh.square.pojo.SystemOrderTrack;
import com.rxh.utils.GlobalConfiguration;
import com.rxh.utils.ParseTradeInfoSweepCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/kuaijie/payment")
public class AlliancePayPaymentController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final GlobalConfiguration gobalConfiguration;
    private final RecordPaymentSquareService recordPaymentSquareService;
    private final SweepCodePaymentService sweepCodePaymentService;
    private final AlliancePayPaymentServiceCommonService alliancePayPaymentService;

    @Autowired
    public AlliancePayPaymentController(GlobalConfiguration gobalConfiguration, RecordPaymentSquareService recordPaymentSquareService, SweepCodePaymentService sweepCodePaymentService, AlliancePayPaymentServiceCommonService alliancePayPaymentService) {
        this.gobalConfiguration = gobalConfiguration;
        this.recordPaymentSquareService = recordPaymentSquareService;
        this.sweepCodePaymentService = sweepCodePaymentService;
        this.alliancePayPaymentService = alliancePayPaymentService;
    }


    /**
     * 绑定银行卡
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/bondCard", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String bondCard(HttpServletRequest req, @RequestBody String tradeInfo) throws PayException, UnsupportedEncodingException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.alliancePayBondCard(systemOrderTrack);

            String result = alliancePayPaymentService.toBondCard(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result);
            return result;
        } catch (PayException e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }

    /**
     * 收单
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/getOrder", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getOrder(HttpServletRequest req, @RequestBody String tradeInfo)  throws Exception {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.allianceGetOrderPay(systemOrderTrack);
            String result = alliancePayPaymentService.toPayment(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        } catch (Exception e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }



    /**
     * 绑定银行卡
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/confirmBond", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmBond(HttpServletRequest req, @RequestBody String tradeInfo) throws PayException, UnsupportedEncodingException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.alliancePayConfirmBond(systemOrderTrack);

            String result = alliancePayPaymentService.toConfirmBond(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        } catch (Exception e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }

    /**
     * 付款确认
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/confirmPay", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String confirmPay(HttpServletRequest req, @RequestBody String tradeInfo) throws PayException, UnsupportedEncodingException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req, tradeInfo, systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.alliancePayConfirmPay(systemOrderTrack);
            String result = alliancePayPaymentService.toConfirmPay(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        } catch (Exception e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            throw e;
        }  finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }


    /**
     *  进件
     * @param req
     * @param tradeInfo
     * @return
     * @throws PayException
     *   /kuaijie/addCus
     */
//    @RequestMapping(value="/addCus", produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String  AddCus(HttpServletRequest req,@RequestBody String tradeInfo)  throws PayException{
//        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
//        try{
//            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
//            MerchantAddCusInfo merchantAddCusInfo=ParseTradeInfoSweepCode.allianceVerifyAddCus(systemOrderTrack);
//            String result = alliancePayPaymentService.toAddCus(systemOrderTrack,merchantAddCusInfo);
//            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
//            return result;
//        } catch (Exception e) {
//            logger.error(tradeInfo, e);
//            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
//            throw e;
//        } finally {
//            if (systemOrderTrack != null) {
//                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
//            }
//        }
//    }



    /**
     * 重新获取绑卡验证码
     * *
     * @param req       http请求
     * @param tradeInfo 交易信息（Xml报文进行Base64加密）
     * @return 交易结果
     */
    @RequestMapping(value = "/reGetBondCode", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String reGetBondCode(HttpServletRequest req, @RequestBody String tradeInfo) throws PayException, UnsupportedEncodingException {
        SystemOrderTrack systemOrderTrack = new SystemOrderTrack();
        try {
            systemOrderTrack = sweepCodePaymentService.getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            TradeObjectSquare tradeObjectSquare = ParseTradeInfoSweepCode.alliancePayBondSmsCode(systemOrderTrack);

            String result = alliancePayPaymentService.reGetBondCode(systemOrderTrack, tradeObjectSquare);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
            return result;
        } catch (Exception e) {
            logger.error(tradeInfo, e);
            systemOrderTrack.setTradeResult(e.getMessage().length()>=500?e.getMessage().substring(0,499):e.getMessage());
            throw e;
        } finally {
            if (systemOrderTrack != null) {
                recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            }
        }
    }

}
