package com.rxh.yacolpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantCard;
import com.rxh.utils.JsonUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.yacolpay.utils.CallServiceUtil;
import com.rxh.yacolpay.utils.YaColIPaySignUtil;
import com.rxh.yacolpay.utils.YaColIPayTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *  3.2绑定银行卡短信验证
 * @author  panda
 * @date  20190703
 */
//@Controller
//@RequestMapping("/yacolPay")
@Component
public class YaColBindingBankCardAdvanceFastPay extends AbstractYaColIPay{
    private final static Logger logger = LoggerFactory.getLogger(YaColBindingBankCardAdvanceFastPay.class);

    /**
     *
     * @param squareTrade
     * @return
     */
//    @RequestMapping("/bindingBankCardAdvance")
    protected BankResult bindingBankCardAdvance(SquareTrade squareTrade){
        BankResult bankResult = new BankResult();
        try{
            ExtraChannelInfo extraChannelInfo = squareTrade.getExtraChannelInfo();
            JSONObject param = JSON.parseObject(extraChannelInfo.getData());
            Map<String,String> bondParam = getSendSMSParam(squareTrade);
            if(null == bondParam){
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setBankResult("cross 工程封装参数异常");
                bankResult.setParam(null);
                bankResult.setBankData(null);
                logger.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
                return bankResult;
            }
            String  publicCheckKey=(String)param.get("publicCheckKey");

            String params = YaColIPayTools.createLinkString(bondParam, true);
            logger.info("【酷宝支付-绑定银行卡短信验证】短信确认请求参数：{}",params);
            //发送请求
            String result = URLDecoder.decode(CallServiceUtil.sendPost(param.getString("url"), params), "UTF-8");


            logger.info("【酷宝支付-绑定银行卡短信验证】返回信息：{}",result);
            Map<String,Object> content=JsonUtils.jsonToMap(result);
            if(verificationParam(content,publicCheckKey)){
                if("APPLY_SUCCESS".equalsIgnoreCase(content.get("response_code").toString())){
                    String card_id= content.get("card_id").toString();
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("绑卡成功");
                    bankResult.setParam(result);
                    String bankData="{\"card_id\":\""+card_id+"\"}";
                    bankResult.setBankData(bankData);
                    logger.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }else {
                    bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                    bankResult.setBankResult(content.get("error_message")==null?content.get("errorMessage").toString():content.get("error_message").toString());
                    bankResult.setParam(result);
                    bankResult.setBankData(null);
                    logger.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                    return bankResult;
                }
            }else{
                bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                bankResult.setBankResult("验证酷宝返回参数不通过");
                bankResult.setParam(result);
                bankResult.setBankData(result);
                logger.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}",JsonUtils.objectToJson(bankResult));
                return bankResult;
            }

        }catch (Exception e){
            e.printStackTrace();
            bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
            bankResult.setBankResult("cross 工程处理请求过程中发生异常");
            bankResult.setParam(e.getMessage());
            bankResult.setBankData(null);
            logger.info("【酷宝支付-绑定银行卡短信验证】响应给peayment工程信息：{}", JsonUtils.objectToJson(bankResult));
            return bankResult;
        }

    }



    /**
     *  封装请求信息
     * @param trade
     * @return
     */
    private Map<String, String> getSendSMSParam(SquareTrade trade){
        try{

            ExtraChannelInfo extraChannelInfo = trade.getExtraChannelInfo();
            JSONObject extraChannelInfoData = JSON.parseObject(extraChannelInfo.getData());
            MerchantCard merchantCard = trade.getMerchantCard();
            JSONObject  banckData=JSON.parseObject(merchantCard.getBackData());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String privateKeyRSA =extraChannelInfoData.getString("privateKey");
            /************************基本参数************************/
            String service="fastpay_binding_bank_card_advance";//服务名称
            String version="1.0";//接口版本
            String request_time=formatter.format(new Date());//请求时间
            String _input_charset="utf-8";//字符集编码
            String sign_type="RSA";//签名类型
            String sign = "";//签名
            String partner_id=extraChannelInfoData.getString("partner_id");//签约合作方的酷宝唯一商户号

            /*******************业务参数**********************************/
            String ticket=banckData.getString("ticket");//返回的ticket
            String valid_code=trade.getTradeObjectSquare().getSmsCode();//短信验证码
            String client_ip = trade.getIp();;//IP

            Map<String,String> params=new HashMap<String, String>();
            params.put("service",service);
            params.put("version",version);
            params.put("request_time",request_time);
            params.put("partner_id",partner_id);
            params.put("_input_charset",_input_charset);
            params.put("ticket",ticket);
            params.put("valid_code", valid_code);
            params.put("client_ip",client_ip);
            String content= YaColIPayTools.createLinkString(params, false);
            //签名
            sign= YaColIPaySignUtil.sign(content, sign_type, privateKeyRSA, _input_charset);
//            sign= URLEncoder.encode(sign,_input_charset);
            //拼接发送参数
            params.put("sign_type",sign_type);
            params.put("sign",sign);
            return params;
        }catch (Exception e){
            e.getStackTrace();
            return null;
        }
    }

}
