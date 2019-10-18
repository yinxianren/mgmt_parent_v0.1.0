package com.rxh.haiyipay;

import com.alibaba.fastjson.JSONObject;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.square.pojo.ChannelInfo;
import com.rxh.square.pojo.TransOrder;
import com.rxh.utils.HttpClientUtils;
import com.rxh.utils.SystemConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author monkey
 * @date 20190709
 * 海懿代付接口
 */
@RestController
@RequestMapping("/haiyiPay")
public class HaiyiPayPayMent {

    private final static Logger logger = LoggerFactory.getLogger(HaiyiPayPayMent.class);

    /**
     * 获取token
     * @param json
     * @return
     */
    @RequestMapping("/getToken")
    public BankResult getToken(@RequestBody String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map param = new HashMap();
        param.put("appId",jsonObject.getString("appId"));
        param.put("appKey",jsonObject.getString("appKey"));
        param.put("service","getToken");
        logger.info("海懿支付获取token请求参数param"+JSONObject.toJSONString(param));
        String result = HttpClientUtils.doPostJson(HttpClientUtils.getHttpClient(),jsonObject.getString("paymentUrl"),JSONObject.toJSONString(param));
        logger.info("海懿支付获取token返回result"+result);
        BankResult bankResult = new BankResult();
        try {
            JSONObject resultMap = JSONObject.parseObject(result);
            if (resultMap.getString("code").equals("0000")){
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setParam(result);
                bankResult.setBankData(resultMap.getString("data"));//token信息
                bankResult.setBankResult("获取token信息成功");
            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setParam(result);
                bankResult.setBankResult("获取token信息失败； "+resultMap.getString("msg"));
            }
        }catch (Exception e){
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setParam(result);
            bankResult.setBankResult("获取token信息失败； 系统内部错误");
        }
        return bankResult;
    }

    /**
     * 代付申请
     * @param squareTrade
     * @return
     */
    @RequestMapping("/payment")
    public BankResult payment(@RequestBody SquareTrade squareTrade){
        TransOrder transOrder = squareTrade.getTransOrder();
        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        JSONObject jsonObject = JSONObject.parseObject(channelInfo.getOthers());
        Map<String,String> param = new HashMap();
        param.put("service","payment");
        param.put("orderNumber",transOrder.getMerOrderId());
        param.put("tranAmount",transOrder.getAmount().setScale(2).toString());
        param.put("inAcctNo",squareTrade.getTradeObjectSquare().getInAcctNo());
        param.put("inAcctName",squareTrade.getTradeObjectSquare().getInAcctName());
        param.put("token",jsonObject.getString("token"));
        logger.info("海懿代付请求参数param："+JSONObject.toJSONString(param));
//        String result = HttpClientUtil.doPostJson(HttpClientUtils.getHttpClient(),jsonObject.getString("paymentUrl"),param,"utf-8");
        String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"orderNumber\":\"2019070300003\",\"bussFlowNo\":\"20190703140405520521\"}}";
        logger.info("海懿代付返回结果result："+result);
        BankResult bankResult = new BankResult();
        try {
            JSONObject resultMap = JSONObject.parseObject(result);
            if (resultMap.getString("code").equals("0000")){
                bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                bankResult.setBankResult("代付申请成功");
                bankResult.setParam(result);
                JSONObject date = JSONObject.parseObject(resultMap.getString("data"));
                bankResult.setMerchantOrderId(date.getString("orderNumber"));
                bankResult.setBankOrderId(date.getString("bussFlowNo"));
                bankResult.setBankTime(new Date());
            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setParam(result);
                bankResult.setBankResult("代付失败； "+resultMap.getString("msg"));
            }
        }catch (Exception e){
            logger.warn("系统内部错误,"+e.getMessage());
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setParam(result);
            bankResult.setBankResult("代付申请失败； 系统内部错误");
        }
        return bankResult;
    }

//    @RequestMapping("/notify")
//    public String notify(String a){
//        return a;
//    }

    @RequestMapping("/queryPayment")
    public BankResult queryPayment(@RequestBody SquareTrade squareTrade){
        TransOrder transOrder = squareTrade.getTransOrder();
        ChannelInfo channelInfo = squareTrade.getChannelInfo();
        JSONObject jsonObject = JSONObject.parseObject(channelInfo.getOthers());
        Map params = new HashMap();
        params.put("service","queryPayment");
        params.put("orderNumber",transOrder.getMerOrderId());
        params.put("token",jsonObject.getString("token"));
        logger.info("海懿代付订单查询参数param："+JSONObject.toJSONString(params));
//        String result = HttpClientUtil.doPostJson(HttpClientUtils.getHttpClient(),jsonObject.getString("paymentUrl"),params,"utf-8");
        String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"inAcctName\":\"谭廷东\",\"orderNumber\":\"2019071100000000011\",\"settleDate\":1562860800000,\"inAcctNo\":\"6228480078071578278\",\"tranAmount\":20.0,\"retCode\":\"3\",\"retMsg\":\"交易成功\",\"status\":\"05\"}}";
        logger.info("海懿代付订单查询返回结果result："+result);
        BankResult bankResult = new BankResult();

        try {
            JSONObject resultMap = JSONObject.parseObject(result);
            if (resultMap.getString("code").equals("0000")){
                JSONObject data = JSONObject.parseObject(resultMap.getString("data"));
                switch (data.getString("status")){
                    case "00" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
                        break;
                    case "01" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("待审核");
                        break;
                    case "02" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("审核通过");
                        break;
                    case "03" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
                        break;
                    case "04" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("代付申请已提交");
                        break;
                    case "05" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                        bankResult.setBankResult("代付交易成功");
                        break;
                    case "06" :
                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
                        break;
                    default:
                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
                        bankResult.setBankResult("代付交易处理中 "+resultMap.getString("retMsg"));
                        break;
                }
                bankResult.setParam(result);
                bankResult.setMerchantOrderId(data.getString("orderNumber"));
                bankResult.setBankTime(new Date());
            }else {
                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                bankResult.setParam(result);
                bankResult.setBankResult("代付失败 "+resultMap.getString("msg"));
            }
        }catch (Exception e){
            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setParam(result);
            bankResult.setBankResult("代付失败 系统内部错误");
        }
        logger.info("海懿代付订单查询返回结果bankResult："+JSONObject.toJSONString(bankResult));
        return bankResult;

    }

    @RequestMapping("/testNoticeUrl")
    public String testNoticeUrl(){
        return "success";
    }

//    public static void main(String[] args) {
//        String result = "{\"msg\":\"成功\",\"code\":\"0000\",\"data\":{\"inAcctName\":\"谭廷东\",\"orderNumber\":\"2019071100000000011\",\"settleDate\":1562860800000,\"inAcctNo\":\"6228480078071578278\",\"tranAmount\":20.0,\"retCode\":\"3\",\"retMsg\":\"交易成功\",\"status\":\"05\"}}";
//        logger.info("海懿代付订单查询返回结果result："+result);
//        BankResult bankResult = new BankResult();
//
//        try {
//            JSONObject resultMap = JSONObject.parseObject(result);
//            if (resultMap.getString("code").equals("0000")){
//                JSONObject data = JSONObject.parseObject(resultMap.getString("data"));
//                switch (data.getString("status")){
//                    case "00" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
//                        break;
//                    case "01" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
//                        bankResult.setBankResult("待审核");
//                        break;
//                    case "02" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
//                        bankResult.setBankResult("审核通过");
//                        break;
//                    case "03" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
//                        break;
//                    case "04" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_PENDING_PAYMENT);
//                        bankResult.setBankResult("代付申请已提交");
//                        break;
//                    case "05" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
//                        bankResult.setBankResult("代付交易成功");
//                        break;
//                    case "06" :
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
//                        break;
//                    default:
//                        bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//                        bankResult.setBankResult("代付交易失败 "+resultMap.getString("retMsg"));
//                        break;
//                }
//                bankResult.setParam(result);
//                bankResult.setMerchantOrderId(data.getString("orderNumber"));
//                bankResult.setBankTime(new Date());
//            }else {
//                bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//                bankResult.setParam(result);
//                bankResult.setBankResult("代付失败 "+resultMap.getString("msg"));
//            }
//        }catch (Exception e){
//            bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
//            bankResult.setParam(result);
//            bankResult.setBankResult("代付失败 系统内部错误");
//        }
//
//        logger.info("海懿代付订单查询返回结果bankResult："+JSONObject.toJSONString(bankResult));
//
//    }


}
