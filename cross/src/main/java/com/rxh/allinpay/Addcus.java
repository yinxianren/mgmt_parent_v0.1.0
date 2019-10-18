package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rxh.exception.PayException;
import com.rxh.pojo.cross.BankResult;
import com.rxh.pojo.merchant.MerchantRegisterInfo;
import com.rxh.pojo.payment.SquareTrade;
import com.rxh.pojo.payment.TradeObjectSquare;
import com.rxh.square.pojo.ExtraChannelInfo;
import com.rxh.square.pojo.MerchantRegisterCollect;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//进件 addcus
@Controller
@RequestMapping("/allinPay")
public class Addcus {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private final static Logger logger = LoggerFactory.getLogger(Addcus.class);

	@RequestMapping("/addcus")
	@ResponseBody
    public BankResult addcus(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException {
		String type = trade.getTradeObjectSquare().getInnerType();
		BankResult bankResult = new BankResult();
		switch(type){
			case SystemConstant.INFORMATION_REGISTRATION:
				bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
				bankResult.setBankResult("商户信息登记成功");
				break;
			case SystemConstant.BANKCARD_REGISTRATION:
				bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
				bankResult.setBankResult("银行卡信息登记成功");
				break;
			case SystemConstant.SERVICE_FULFILLMENT:
				bankResult = trade(trade);
				break;
			default:
				bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
				bankResult.setBankResult("进件失败，接口编码错误");
				break;
		}

    	return  bankResult;

	}

//	@RequestMapping("/addcus")
//    @ResponseBody
    public BankResult trade(@RequestBody SquareTrade trade) throws UnsupportedEncodingException, PayException {
    	Map<String, Object> params = getAddcusParam(trade);
    	logger.info("进件请求参数"+JsonUtils.objectToJsonNonNull(params));
    	String other = trade.getExtraChannelInfo().getData();
    	JSONObject json = JSON.parseObject(other);
    	String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), json.getString("addcusUrl"), params);
//    	String content = "{\"appid\":\"0000924\",\"cusid\":\"101005129021\",\"outcusid\":\"20190709002\",\"retcode\":\"SUCCESS\",\"retmsg\":\"处理成功\",\"sign\":\"DA3BC90C50F42BD28818547032B1420A\"}";
    	return checkResult(content, trade);
    }


    /**
     * 请求结果处理
     * @param content
     * @param trade
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
	private BankResult checkResult(String content, SquareTrade trade) throws UnsupportedEncodingException {
    	BankResult bankResult = new BankResult();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode.equals("SUCCESS")) {
            	TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
    			String resultSign = result.get("sign").toString();
    			result.remove("sign");
    			String other = trade.getExtraChannelInfo().getData();
    	    	JSONObject param = JSON.parseObject(other);
    	    	result.put("key", param.getString("key"));
    			String sign = AlinPayUtils.getMd5Sign(result);
    			if(sign.equalsIgnoreCase(resultSign)) {
    				bankResult.setStatus(SystemConstant.BANK_STATUS_SUCCESS);
                    bankResult.setBankResult("进件成功");
                    bankResult.setParam(content);
    			}else {
    				bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                    bankResult.setBankResult("进件异常:签名验证不一致");
                    bankResult.setParam(content);
    			}
    		}else {
    			 bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
                 bankResult.setBankResult("进件失败:" + json.get("retmsg"));
                 bankResult.setParam(content);
    		}
    	}else {
    		bankResult.setStatus(SystemConstant.BANK_STATUS_FAIL);
            bankResult.setBankResult("进件失败：支付返回结果为空！");
            bankResult.setParam(content);
    	}
		logger.info("進件请求返回payment："+ JsonUtils.objectToJsonNonNull(bankResult));
    	return bankResult;
	}


	/**
	 * 获取请求 参数
	 * @param trade
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Map<String,Object> getAddcusParam(SquareTrade trade) throws UnsupportedEncodingException {
        ExtraChannelInfo channelInfo = trade.getExtraChannelInfo();
		MerchantRegisterInfo merchantAddCusInfo = trade.getMerchantRegisterInfo();
		MerchantRegisterCollect merchantRegisterCollect = trade.getMerchantRegisterCollect();
        JSONObject param = JSON.parseObject(channelInfo.getData());
        Integer bankCardType = Integer.valueOf(merchantRegisterCollect.getCardType());
        Map<String, Object> postData = new TreeMap<>();
        postData.put("acctid", merchantRegisterCollect.getCardNum());
        postData.put("acctname", merchantAddCusInfo.getUserName());
        if(bankCardType == 1) {
        	postData.put("accttp", "00");
        }else {
        	postData.put("accttp", "01");
        }
        postData.put("address", merchantAddCusInfo.getMerchantAddress());
        postData.put("appid", param.get("appid"));
        postData.put("areacode",merchantAddCusInfo.getCity());
        postData.put("belongorgid", param.get("orgid"));
        postData.put("cusname", merchantAddCusInfo.getTerminalMerName());
        postData.put("cusshortname", merchantAddCusInfo.getUserShortName());
//        postData.put("expanduser", "jcx2");
        postData.put("idno", merchantAddCusInfo.getIdentityNum());
        postData.put("legal", merchantAddCusInfo.getUserName());
        postData.put("merprovice", merchantAddCusInfo.getProvince());
        postData.put("version", "11");
        postData.put("orgid", param.get("orgid"));
        postData.put("outcusid", merchantRegisterCollect.getId());
        postData.put("phone", merchantAddCusInfo.getPhone());
        postData.put("key", param.get("key"));
		final JSONArray prodList = new JSONArray();

		final HashMap<String, Object> receiver1 = new HashMap<>();
		receiver1.put("trxcode", "QUICKPAY_OF_HP");
		receiver1.put("feerate", trade.getTradeObjectSquare().getPayFee());

//		final HashMap<String, Object> receiver2 = new HashMap<>();
//		receiver2.put("trxcode", "QUICKPAY_OF_NP");
//		receiver2.put("feerate",trade.getTradeObjectSquare().getPayFee());


		final HashMap<String, Object> receiver3 = new HashMap<>();
		receiver3.put("trxcode", "TRX_PAY");
		receiver3.put("feerate", trade.getTradeObjectSquare().getBackFee());

		final HashMap<String, Object> receiver4 = new HashMap<>();
		receiver4.put("trxcode", "QUICKPAY_NOSMS");
		receiver4.put("feerate", trade.getTradeObjectSquare().getPayFee());

		prodList.add(receiver1);
//		prodList.add(receiver2);
		prodList.add(receiver3);
		prodList.add(receiver4);


		postData.put("prodlist", prodList);
        postData.put("randomstr",AlinPayUtils.getRandomSecretkey());
        postData.put("sign", AlinPayUtils.getMd5Sign(postData));
		postData.remove("key");
        return postData;
    }
}
