package com.rxh.controller.kuaijie;


import com.rxh.controller.AbstractCommonController;
import com.rxh.exception.PayException;
import com.rxh.pojo.merchant.MerchantBankCardBinding;
import com.rxh.pojo.merchant.MerchantBasicInformationRegistration;
import com.rxh.pojo.merchant.MerchantServiceFulfillment;
import com.rxh.service.kuaijie.IntoPiecesOfInformationService;
import com.rxh.square.pojo.SystemOrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 进件相关信息
 * @author  zhanguanghuo
 * @version 0.0.1
 * @date 20190625
 *  基本信息登记  /kuaijie/addCusInfo
 *  银行卡绑卡信息登记     /kuaijie/bankCardBind
 *  业务开通接口  /kuaijie/ServiceFulfillment
 */

@Controller
@RequestMapping("/kuaijie")
public class IntoPiecesOfInformationController extends  AbstractCommonController{

    @Autowired
    private IntoPiecesOfInformationService intoPiecesOfInformationService;
    /**
     *  基本信息登记
     * @param req
     * @param tradeInfo
     * @return
     * @version：
    *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value="/addCusInfo", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String intoPiecesOfInformation(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        MerchantBasicInformationRegistration  merchantBasicInformationRegistration=null;
        String result=null;
        try{
            systemOrderTrack =getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            merchantBasicInformationRegistration=intoPiecesOfInformationService.verifyMustParamOnAddCusInfo(systemOrderTrack);
            result =intoPiecesOfInformationService.saveIntoPiecesOfInformation(systemOrderTrack,merchantBasicInformationRegistration);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException){
                logger.warn("【基本信息登记】 提示：{}",e.getMessage());
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantBasicInformationRegistration,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantBasicInformationRegistration,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }

    /**
     *  银行卡绑卡信息登记
     * @param req
     * @param tradeInfo
     * @return
     * @version：
     *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value="/bankCardBind", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String bankCardBinding(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        MerchantBankCardBinding merchantBankCardBinding=null;
        try{
            systemOrderTrack =getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            merchantBankCardBinding=intoPiecesOfInformationService.verifyMustParamOnBankCardBinding(systemOrderTrack);
            result =intoPiecesOfInformationService.saveBankCardBinding(systemOrderTrack,merchantBankCardBinding);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());
            if(e instanceof PayException){
                logger.warn("【银行卡绑卡信息登记】 提示：{}",e.getMessage());
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantBankCardBinding,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantBankCardBinding,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }

    /**
     *  业务开通接口
     * @param req
     * @param tradeInfo
     * @return
     *
     * @version：
     *    0.0.2 -->代码优化，增加商户同步锁，一个商户一把锁
     */
    @RequestMapping(value="/serviceFulfillment", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String  serviceFulfillment(HttpServletRequest req, @RequestBody String tradeInfo){
        SystemOrderTrack systemOrderTrack =new SystemOrderTrack();
        String result=null;
        MerchantServiceFulfillment merchantServiceFulfillment=null;
        try{
            systemOrderTrack =getSystemOrderTrack(req,tradeInfo,systemOrderTrack);
            merchantServiceFulfillment=intoPiecesOfInformationService.verifyMustParamOnServiceFulfillment(systemOrderTrack);
            result =intoPiecesOfInformationService.saveServiceFulfillment(systemOrderTrack,merchantServiceFulfillment);
            systemOrderTrack.setTradeResult(result.length()>=500?result.substring(0,499):result);
        } catch (Exception e) {
            systemOrderTrack.setOrderTrackStatus(1);
            systemOrderTrack.setTradeResult( e.getMessage().length() >=500 ? e.getMessage().substring(0,499) :  e.getMessage());

            if(e instanceof PayException){
                logger.warn("【业务开通接口】 提示：{}",e.getMessage());
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantServiceFulfillment,e.getMessage(),((PayException) e).getResultCode());
            }
            else {
                if(null == result) result = intoPiecesOfInformationService.errorResult(systemOrderTrack,merchantServiceFulfillment,e.getMessage());
                e.printStackTrace();
            }
        } finally {
            recordPaymentSquareService.saveOrUpdateSystemOrderTrack(systemOrderTrack);
            return result;
        }
    }


}
