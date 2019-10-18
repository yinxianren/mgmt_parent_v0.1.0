package com.rxh.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CreditServiceTool {

    private static Logger logger = Logger.getLogger(CreditServiceTool.class.getName());

	public static LinkedHashMap<String,String> parseResponse(String response,String[] signFields,String myPfxPath,String myPfxPwd,String easyPublicKey) throws Exception {

		String des3KeyRsaEncryptedString = response.split("\\|")[0];
		String des3EncryptString = response.split("\\|")[1];
		String des3Key = Rsa.decrypt(des3KeyRsaEncryptedString, myPfxPath, myPfxPwd);
		String json = TripleDes.decrypt(des3Key, des3EncryptString);
		LinkedHashMap<String,String> bean = JSONObject.parseObject(json,LinkedHashMap.class);
		if("0000".equals(bean.get("responseCode"))) {
			if(!verify(bean,signFields,Strings.toString(bean.get("signMsg")),easyPublicKey)) {
				bean.put("responseCode", "E004");
				bean.put("responseRemark", "?????????????");
			}
		}

		return bean;
	}

	public static String encrypt(String json,String easyPublicKey) throws Exception {

		String des3Key = CreditBase64.encode(Strings.randomHex(24).getBytes());
		String des3EncryptString = TripleDes.encrypt(des3Key, json);
		String des3KeyRsaEncryptedString = Rsa.encrypt(des3Key, easyPublicKey);

		return des3KeyRsaEncryptedString+"|"+des3EncryptString;
	}

	private static String toSignString(HashMap<String,String> bean,String[] signFields) {

		StringBuffer signString = new StringBuffer();
		for(String field:signFields) {
			String tmp = Strings.toString(bean.get(field));
			if(!Strings.isNullOrEmpty(tmp)) {
				signString.append(tmp).append(" ");
			}
		}

		String string = signString.toString();
		if(string.length() > 0) {
			string = string.substring(0,string.length()-1);
		}

		logger.info("msg sign string:["+string+"]");

		return string;
	}

	public static String sign(HashMap<String,String> bean,String[] signFields,String myPfxPath,String myPfxPwd) throws Exception {

		String string = toSignString(bean,signFields);

		String ret = Rsa.sign(string, myPfxPath, myPfxPwd, algorithm);

		return ret;
	}

	private static String algorithm = "SHA1withRSA";//SHA1withRSA//SHA1withRSA
	public static boolean verify(HashMap<String,String> bean,String[] signFields,String signMsg,String publicKey) throws Exception {

		String string = toSignString(bean,signFields);

		return Rsa.verify(signMsg, publicKey, string, algorithm);
	}

	public static String getContextPath(HttpServletRequest request) {

		logger.info("X-Scheme:"+request.getHeader("X-Scheme"));
		if(!Strings.isNullOrEmpty(request.getHeader("X-Scheme"))) {
			return request.getHeader("X-Scheme")+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		} else {
			return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		}

	}
	public static String getResult(String responseCode){
		HashMap map = new HashMap();
		map.put("0000","交易成功");
		map.put("S001","请求已接收");
		map.put("H001","等待人工验证");
		map.put("H002","等待短信验证");
		map.put("H003","平台处理中");
		map.put("E001","无路由");
		map.put("E002","报文字段空错误");
		map.put("E003","报文字段非法长度");
		map.put("E004","验签失败");
		map.put("E005","系统异常");
		map.put("E006","加密异常");
		map.put("E007","解密异常");
		map.put("E008","签名异常");
		map.put("E009","验签异常");
		map.put("E010","交易超时");
		map.put("E011","姓名错");
		map.put("E012","身份证错");
		map.put("E013","登记手机号错");
		map.put("E014","无效账号");
		map.put("E015","无效手机号");
		map.put("E016","重复请求");
		map.put("E017","身份证或姓名错");
		map.put("E018","密码错");
		map.put("E019","cvn或有效期错");
		map.put("E020","无此订单");
		map.put("E021","msgId空错误");
		map.put("E022","次数超限");
		map.put("E023","格式错误");
		map.put("E024","成功率过低，交易受控制");
		map.put("E025","商户状态异常");
		map.put("E026","失败，其他原因");
		map.put("E027","账号状态异常");
		map.put("E028","账号被锁");
		map.put("E029","合同状态异常");
		map.put("E030","无效金额");
		map.put("E031","短信码错误");
		map.put("E032","订单超时");
		map.put("E033","验证码错误");
		map.put("E034","审批打回");
		map.put("E036","网络异常");
		map.put("E037","订单号空错误");
		map.put("E038","密码与订单号不符");
		map.put("E039","订单状态不符");
		map.put("E040","不支持的银行");
		map.put("E041","币种不符");
		map.put("E042","无效交易");
		map.put("E043","余额不足");
		map.put("E044","日切中");
		map.put("E045","清算日期异常");
		map.put("E035","超过金额上限");
		map.put("E046","系统维护中");
		map.put("E047","银联系统异常无记录");
		map.put("E048","找不到联行号");
		map.put("E049","二级审批打回结束");
		map.put("E050","二级审批通过，人工核实结果");
		map.put("E051","网络异常，无连接成功");
		map.put("E052","网络异常，有连接成功，人工核实订单");
		map.put("E053","重复金额");
		map.put("E054","交易成果未知，等待或人工核实");
		map.put("E055","新用户");
		map.put("E056","白名单");
		map.put("E057","黑名单");

		if(map.containsKey(responseCode)){
			return String.valueOf(map.get(responseCode));
		}

        return  null;

	}

	public static void main(String[] args)  {
System.out.print(getResult("0000"));
		//String signMsg = "Lt9ms44/3TsDqtLa/C0E7OWcCKngxhOJ5pwCa5dSWygZF1VzIOlC9jNb/8kK7yMYqLdkmGVzjvaXdl/fqT0hE/GrAX8FF0cJddnLlcE2UJBSKQRs0TwDQvcrYWHwb3A6SR7bjgYukzLLkzCj3aNZ+p+4pvHYGErCTTpKWeNo7fc=";
		//String signData = "i want to xxx you:[0200 190011 111 2016110657248621 06635595 0.1 156]ppppp";
		//boolean v = Rsa.verify(signMsg, Config.merchantPublicKey, signData, "SHA256withRSA");

		//String s = Rsa.sign(signData, Config.merchantPrivateKeyPath, Config.merchantPrivateKeyPwd, "SHA256withRSA");
		//System.out.println(s);
		//System.out.println(v);

		//String enc = "BPRaO0Nn7t0n/SEYcR7YxvTbnIwCxhnRltlAByuKq6AvXImmBCI+0siFWxd5clXzPZ/jcrzDTIsUTAoUwUFErPx1JnRYxKX8iZsqoRRSRhYdTFj4uLklgDVeL660Ze+cxeWZbL+oQBuPM8uA2SKwzw+r8ALYx/J0b+1umJYRzm8=";
		//System.out.println(new String(CreditBase64.decode(dec)));

		//enc = Rsa.encrypt(signData, Config.merchantPublicKey);
		//System.out.println(enc);
	}
}
