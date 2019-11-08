package com.rxh.anew.channel.cross.allinpay.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.channel.cross.TransOrderCrossComponent;
import com.rxh.anew.channel.cross.tools.AllinPayUtils;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.TransOrderInfoTable;
import com.rxh.anew.table.channel.ChannelInfoTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:31
 * Description:
 */
@Component
@Slf4j
public class AllinPayTransOrderCrossComponentImpl implements TransOrderCrossComponent {
    @Override
    public CrossResponseMsgDTO payment(RequestCrossMsgDTO trade) {
        CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
//        bankResult.setTrade(trade);
        Map<String, Object> bondParam = getBondParam(trade);

        JSONObject others = (JSONObject) JSON.parse(trade.getChannelInfoTable().getChannelParam());
        log.info("allinPay付款交易参数："+JSONObject.toJSONString(bondParam));
        // String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), "https://ipay.allinpay.com/apiweb/acct/pay", bondParam); //生产环境
        String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), others.getString("oneTransUrl"), bondParam);// 测试环境
//        String content = "{\"acctno\":\"622700****7047\",\"actualamount\":\"200\",\"amount\":\"1000\",\"appid\":\"6666678\",\"errmsg\":\"提交成功,等待返回结果\",\"fee\":\"800\",\"fintime\":\"20190628100755\",\"orderid\":\"1906281007470130001\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"76403287ABD89F8637426FA2BC1BA4F9\",\"trxid\":\"19060000001378\",\"trxstatus\":\"0000\"}";
        log.info("allinPay付款银行返回："+content);
//        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
        JSONObject result = (JSONObject) JSON.parse(content);
        String resultCode = result.getString("retcode");

        if("SUCCESS".equals(resultCode)){

            String trxstatus = result.getString("trxstatus");
            bankResult.setChannelResponseTime(new Date());
            switch (trxstatus){
                case "0000":
                    bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                    bankResult.setCrossResponseMsg("付款成功");
                    bankResult.setChannelOrderId(result.getString("trxid"));
                    bankResult.setChannelResponseMsg(content);
                    break;
                case "2000":
                    bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                    bankResult.setCrossResponseMsg("交易已受理");
                    bankResult.setChannelResponseMsg(content);
                    break;
                case "0003":
                    bankResult.setCrossStatusCode(StatusEnum._3.getStatus());
                    bankResult.setCrossResponseMsg("交易异常,请查询交易");
                    bankResult.setChannelResponseMsg(content);
                    bankResult.setErrorCode(ResponseCodeEnum.RXH00007.getCode());
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH00007.getMsg());
                    break;
                case "3999":
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg("其他错误");
                    bankResult.setChannelResponseMsg(content);
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                    break;
                default:
                    bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg("付款失败"+result.getString("errmsg"));
                    bankResult.setChannelResponseMsg(content);
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                    break;
            }
        }else {
            bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("付款失败:"+result.getString("errmsg"));
            bankResult.setChannelResponseMsg(content);
            bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
            bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
        }
        log.info("allinPay付款返回参数："+JSONObject.toJSONString(bankResult));
        return bankResult;
    }

    private Map<String, Object> getBondParam(RequestCrossMsgDTO trade) {
        ChannelInfoTable channelInfo = trade.getChannelInfoTable();
        JSONObject param = JSON.parseObject(channelInfo.getChannelParam());
        RegisterCollectTable merchantRegisterCollect= trade.getRegisterCollectTable();
        JSONObject registerInfo = JSON.parseObject(merchantRegisterCollect.getChannelRespResult());
        TreeMap<String, Object> postData = new TreeMap<>();
        TransOrderInfoTable transOrder = trade.getTransOrderInfoTable();
        String amount = transOrder.getAmount().toString();
        String agreeid = trade.getMerchantCardTable().getCrossRespResult();
        String appid = param.getString("appid");// 公共必填
        String key = param.getString("key");// 公共必填
        String cusid = registerInfo.getString("cusid"); //商户号
        String fee =transOrder.getBackFee().toString();
//        String isall = param.getString("isall");
        String notifyurl = param.getString("notifyurl");
        String orderid = transOrder.getPlatformOrderId();
        String orgid = param.getString("orgid");// 公共必填
//        String trxreserve = param.getString("trxreserve");
        postData.put("cusid",cusid);
        postData.put("orderid",orderid);

        String amountStr=new BigDecimal(amount).multiply(new BigDecimal(100)).toString();
        postData.put("amount",amountStr.contains(".") ? amountStr.substring(0,amountStr.indexOf(".")): amountStr);
//        if(StringUtils.isNotEmpty(isall)) {
//        	postData.put("isall",isall);
//        }
        if(StringUtils.isNotEmpty(fee)) {
            fee=new BigDecimal(fee).multiply(new BigDecimal(100)).setScale(0).toString();
            postData.put("fee",fee);
        }
        postData.put("agreeid",agreeid);
//        if(StringUtils.isNotEmpty(trxreserve)) {
//            postData.put("trxreserve",trxreserve);
//        }
        postData.put("notifyurl",notifyurl);
        postData.put("orgid",orgid);
        postData.put("appid",appid);
        postData.put("randomstr", AllinPayUtils.getRandomSecretkey());
        postData.put("key", key);
        postData.put("sign",AllinPayUtils.getMd5Sign(postData));
        postData.remove("key");
        return postData;
    }
}
