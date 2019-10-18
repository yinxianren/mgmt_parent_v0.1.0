package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.Result;
import com.rxh.pojo.cross.AlinTradeObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;



/**
 * 提现 /withdraw
 */
@Controller
@RequestMapping("/allinPay")
public class AllinPayWithdraw {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(AllinPayWithdraw.class);

    private final PaymentInfo paymentInfo;

    @Autowired
    public AllinPayWithdraw(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }


    /**
     * 请求
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/withdraw")
    @ResponseBody
    public BankResult withdraw(@RequestBody SquareTrade trade) throws UnsupportedEncodingException {
        BankResult bankResult = new BankResult();
        Map<String, Object> bondParam = getBondParam(trade);

         String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/acct/withdraw", bondParam); //生产环境
//        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://test.allinpaygd.com/ipayapiweb/acct/withdraw", bondParam);// 测试环境
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if("SUCCESS".equals(resultCode)){
            String trxstatus = result.getString("trxstatus");
            switch (trxstatus){
                case "0000":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("提现成功");
                    bankResult.setParam(content);
                    break;
                case "2000":
                    bankResult.setStatus(SystemConstant.CHANGE_BANK_STATUS_FAIL);
                    bankResult.setBankResult("交易已受理");
                    bankResult.setParam(content);
                    break;
                case "0003":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("交易异常,请查询交易");
                    bankResult.setParam(content);
                    break;
                case "3999":
                    bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("其他错误");
                    bankResult.setParam(content);
                    break;
                default:
                    bankResult = new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
                    bankResult.setBankResult("提现失败");
                    bankResult.setParam(content);
                    break;
            }
        }else {
            bankResult = new BankResult(SystemConstant.BANK_STATUS_TIME_OUT, "error.5001");
            bankResult.setBankResult("提现失败");
            bankResult.setParam(content);
        }
        return bankResult;
    }

    private Map<String, Object> getBondParam(SquareTrade trade) throws UnsupportedEncodingException {

        ChannelInfo channelInfo = trade.getChannelInfo();
        JSONObject param = JSON.parseObject(channelInfo.getOthers());
        MerchantRegisterInfo merchantRegisterInfo = trade.getMerchantRegisterInfo();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterInfo.getBackData());

        Map<String, Object> postData = new TreeMap<>();
        String amount = trade.getFinanceDrawing().getDrawingAmount().toString();
        String appid = param.getString("appid");// 公共必填
        String cusid = registerInfo.getString("cusid"); //商户号
        String fee = param.getString("fee");
        String isall = param.getString("isall"); //全额提现(1-代表全额提取)
        String notifyurl = param.getString("notifyurl");
        String orderid = trade.getFinanceDrawing().getCustomerId();
        String orgid = param.getString("orgid");// 公共必填
        String trxreserve = param.getString("trxreserve");

        String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
        postData.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//        postData.put("amount",amount.multiply(new BigDecimal(100)));//元转分

        postData.put("appid",appid);
        postData.put("cusid",cusid);
        postData.put("fee",fee);
        postData.put("isall",isall);
        postData.put("notifyurl",notifyurl);
        postData.put("orderid",orderid);
        postData.put("orgid",orgid);
        postData.put("randomstr",AlinPayUtils.getRandomSecretkey());
        postData.put("trxreserve",trxreserve);
        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
        return postData;
    }


    /**
     * 成功交易结果通知
     * @param alinTradeObject
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/withdrawNotifyurl")
    @ResponseBody
    public String payMoneyNotifyurl(@RequestBody AlinTradeObject alinTradeObject) throws UnsupportedEncodingException {
        Map<String,Object> map = new TreeMap<>();
        map.put("acct",alinTradeObject.getAcct());
        map.put("appid",alinTradeObject.getAppid());
        map.put("cusid",alinTradeObject.getCusid());
        map.put("cusorderid",alinTradeObject.getCusorderid());
        map.put("fee",alinTradeObject.getFee());
        map.put("initamt",alinTradeObject.getInitamt());
        map.put("outtrxid",alinTradeObject.getOuttrxid());
        map.put("paytime",alinTradeObject.getPaytime());
        map.put("trxamt",alinTradeObject.getTrxamt());
        map.put("trxcode",alinTradeObject.getTrxcode());
        map.put("trxdate",alinTradeObject.getTrxdate());
        map.put("trxid",alinTradeObject.getTrxid());
        map.put("trxreserved",alinTradeObject.getTrxreserved());
        map.put("trxstatus",alinTradeObject.getTrxstatus());
        String md5Sign=  AlinPayUtils.getMd5Sign(map);
        if (StringUtils.equals(md5Sign,alinTradeObject.getSign())){
            /**
             支付结果处理
             */
            BankResult bankResult = new BankResult();
            bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            Date date = DateUtils.dateFormat(dateFormat, alinTradeObject.getPaytime());
            bankResult.setBankTime(date == null ? new Date() : date);
            if ("0000".equals(alinTradeObject.getTrxstatus())) {// 支付成功
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("付款成功");
                bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            } else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                try {
                    bankResult.setBankCode(BankResultInfoCode.CommunicateCodeTwo.valueOf("A" + alinTradeObject.getTrxstatus()).getStatusMsg());
                } catch (Exception e) {
                    logger.info("AllinPay未定义的错误信息,错误编码为：" + alinTradeObject.getTrxstatus());
                    bankResult.setBankCode("error.5000");
                }
                bankResult.setBankResult("付款失败：" + alinTradeObject.getTrxstatus());
                bankResult.setParam(JsonUtils.objectToJson(alinTradeObject));
            }
            String msg = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(), paymentInfo.getBankNotifyUrl(), JsonUtils.objectToJson(bankResult));
            Result paymentResult = JsonUtils.jsonToPojo(msg, Result.class);
            if (paymentResult == null) {
                return "FAIL";
            }
            if (paymentResult.getCode() == Result.SUCCESS) {
                return "";
            } else {
                return paymentResult.getMsg();
            }
        }else{
            logger.info("非法请求：签名信息验证失败");
            return "签名信息验证失败";
        }
    }


}
