package com.rxh.allinpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rxh.anew.dto.CrossResponseMsgDTO;
import com.rxh.anew.dto.RequestCrossMsgDTO;
import com.rxh.anew.table.business.RegisterCollectTable;
import com.rxh.anew.table.business.RegisterInfoTable;
import com.rxh.anew.table.channel.ChannelExtraInfoTable;
import com.rxh.anew.table.merchant.MerchantRateTable;
import com.rxh.enums.ResponseCodeEnum;
import com.rxh.enums.StatusEnum;
import com.rxh.exception.PayException;
import com.rxh.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//进件 addcus
@Controller
@RequestMapping("/allinPay")
public class Addcus {
	
    private final static Logger logger = LoggerFactory.getLogger(Addcus.class);

	@RequestMapping("/addcus")
	@ResponseBody
    public CrossResponseMsgDTO addcus(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException {
		String type = trade.getRegisterCollectTable().getBussType();
		CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
		switch(type){
			case BusinessTypeContans.B1:
				bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
				bankResult.setCrossResponseMsg("商户信息登记成功");
				break;
			case BusinessTypeContans.B2:
				bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
				bankResult.setCrossResponseMsg("银行卡信息登记成功");
				break;
			case BusinessTypeContans.B3:
				bankResult = trade(trade);
				break;
			default:
				bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
				bankResult.setCrossResponseMsg("进件失败，接口编码错误");
				bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
				bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
				break;
		}

    	return  bankResult;

	}

//	@RequestMapping("/addcus")
//    @ResponseBody
    public CrossResponseMsgDTO trade(@RequestBody RequestCrossMsgDTO trade) throws UnsupportedEncodingException, PayException {
    	Map<String, Object> params = getAddcusParam(trade);
    	logger.info("进件请求参数"+JsonUtils.objectToJsonNonNull(params));
    	String other = trade.getChannelExtraInfoTable().getChannelParam();
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
	private CrossResponseMsgDTO checkResult(String content, RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
    	CrossResponseMsgDTO bankResult = new CrossResponseMsgDTO();
    	if(StringUtils.isNotBlank(content)) {
    		JSONObject json = JSON.parseObject(content);
    		String retcode = json.getString("retcode");
    		if(retcode.equals("SUCCESS")) {
            	TreeMap<String,Object> result = JSON.parseObject(content, TreeMap.class);
    			String resultSign = result.get("sign").toString();
    			result.remove("sign");
    			String other = trade.getChannelExtraInfoTable().getChannelParam();
    	    	JSONObject param = JSON.parseObject(other);
    	    	result.put("key", param.getString("key"));
    			String sign = AlinPayUtils.getMd5Sign(result);
    			if(sign.equalsIgnoreCase(resultSign)) {
    				bankResult.setCrossStatusCode(StatusEnum._0.getStatus());
                    bankResult.setCrossResponseMsg("进件成功");
                    bankResult.setChannelResponseMsg(content);
    			}else {
    				bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                    bankResult.setCrossResponseMsg("进件异常:签名验证不一致");
                    bankResult.setChannelResponseMsg(content);
                    bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
                    bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
    			}
    		}else {
    			 bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
                 bankResult.setCrossResponseMsg("进件失败:" + json.get("retmsg"));
                 bankResult.setChannelResponseMsg(content);
				 bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
				 bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
    		}
    	}else {
    		bankResult.setCrossStatusCode(StatusEnum._1.getStatus());
            bankResult.setCrossResponseMsg("进件失败：返回结果为空！");
            bankResult.setChannelResponseMsg(content);
			bankResult.setErrorMsg(ResponseCodeEnum.RXH99999.getMsg());
			bankResult.setErrorCode(ResponseCodeEnum.RXH99999.getCode());
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
	public Map<String,Object> getAddcusParam(RequestCrossMsgDTO trade) throws UnsupportedEncodingException {
        ChannelExtraInfoTable channelExInfo = trade.getChannelExtraInfoTable();
		RegisterCollectTable merchantRegisterCollect = trade.getRegisterCollectTable();
		RegisterInfoTable registerInfoTable = trade.getRegisterInfoTable();
        JSONObject param = JSON.parseObject(channelExInfo.getChannelParam());
        Integer bankCardType = Integer.valueOf(merchantRegisterCollect.getBankCardType());
        Map<String, Object> postData = new TreeMap<>();
        postData.put("acctid", merchantRegisterCollect.getBankCardNum());
        postData.put("acctname", merchantRegisterCollect.getCardHolderName());
        if(bankCardType == 1) {
        	postData.put("accttp", "00");
        }else {
        	postData.put("accttp", "01");
        }
        postData.put("address", registerInfoTable.getAddress());
        postData.put("appid", param.get("appid"));
        postData.put("areacode",registerInfoTable.getCity());
        postData.put("belongorgid", param.get("orgid"));
        postData.put("cusname", registerInfoTable.getTerminalMerName());
        postData.put("cusshortname", registerInfoTable.getUserShortName());
//        postData.put("expanduser", "jcx2");
        postData.put("idno", registerInfoTable.getIdentityNum());
        postData.put("legal", registerInfoTable.getUserName());
        postData.put("merprovice", registerInfoTable.getProvince());
        postData.put("version", "11");
        postData.put("orgid", param.get("orgid"));
        postData.put("outcusid", merchantRegisterCollect.getId());
        postData.put("phone", registerInfoTable.getPhone());
        postData.put("key", param.get("key"));
		final JSONArray prodList = new JSONArray();

		final HashMap<String, Object> receiver1 = new HashMap<>();
		/*receiver1.put("trxcode", "QUICKPAY_OF_HP");
		receiver1.put("feerate", trade.getRegisterCollectTable().getMerchantRateTableCollect().getPayFee());*/

//		final HashMap<String, Object> receiver2 = new HashMap<>();
//		receiver2.put("trxcode", "QUICKPAY_OF_NP");
//		receiver2.put("feerate",trade.getTradeObjectSquare().getPayFee());


		final HashMap<String, Object> receiver3 = new HashMap<>();
		/*receiver3.put("trxcode", "TRX_PAY");
		receiver3.put("feerate", trade.getTradeObjectSquare().getBackFee());*/

		final HashMap<String, Object> receiver4 = new HashMap<>();
		/*receiver4.put("trxcode", "QUICKPAY_NOSMS");
		receiver4.put("feerate", trade.getTradeObjectSquare().getPayFee());*/

		for (MerchantRateTable merchantRateTable : trade.getRegisterCollectTable().getMerchantRateTableCollect()){
			switch (merchantRateTable.getProductId()){
				case "RH_QUICKPAY_SMALL_NOSMS" :
					receiver1.put("trxcode", "QUICKPAY_NOSMS");
					receiver1.put("feerate", merchantRateTable.getRateFee());
					break;
				case "RH_TRX_PAY" :
					receiver3.put("trxcode", "TRX_PAY");
					receiver3.put("feerate", merchantRateTable.getRateFee());
					break;
				case "RH_QUICKPAY_SMALL" :
					receiver4.put("trxcode", "TRX_PAY");
					receiver4.put("feerate", merchantRateTable.getRateFee());
					break;
			}
		}
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
