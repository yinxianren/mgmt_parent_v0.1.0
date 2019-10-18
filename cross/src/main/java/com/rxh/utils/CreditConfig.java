package com.rxh.utils;

public class CreditConfig {


	final public static String[] easyRepaymentUrls = {
		"http://183.129.219.202:9021/repayment/repayment",
	};

	final public static String[] easyRepaymentAppUrls = {
		"http://183.129.219.202:9021/repayment/repaymentApp",
	};

	final public static String[] easyAgentPayUrls = {
		"http://183.129.219.202:9021/repayment/agentPay",
	};

	final public static String[] easyQueryUrls = {
		"http://183.129.219.202:9021/repayment/query",
	};

	final public static String[] easyPaymentUrls = {
		"http://183.129.219.202:9021/repayment/payment",
	};

	final public static String easyPublicKey = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGiYdXXRFuYDIG5Q8wKc49Bj4kz4J/2DMRN7NIipZt1GxeYGx3hCPAVRv45fFTTvd50WKQQK1fbBABec3MUIghTvJ+r9bulaxaMPkNM69DKMTMFun5bwtkTUmfx+X1T1+9/l2LKc+JC9ux3vxYYkl0iz/Kd4qrol6FRM79GV/QrLAgMBAAE=";

	final public static String merchantPublicKey = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgE1gtjGhl7kyNnM4vzdTACmkaKet1Ff9RjTUYFiycQY8iqt0EPRDKY++sDN+UgI9x1QHuAOY/QpWVgDDS6B3K7XCNmIQbroBKzXOfzvyZc6M6kCKT43nZPXbwYQ0e/0nJ+noL6OFZNbImHGmIfsXJM3c390Lrvu/RuEB97VKlVYpAgMBAAE=";
	final public static String merchantPrivateKeyPwd = "11111111";

	//��Ҫǩ���������ֶΣ���˳��
	final public static String[] reqSignFields = {
			"serviceCode",
			"processCode",
			"merchantNo",
			"merchantOrderNo",
			"sequenceNo",
			"merchantPlanNo",
			"orderNo",
			"orderDate",
			"ppNo",
			"applyDate",
			"payPlanNos",
			"amount",
			"payRate",
			"repayFee",
			"bindingId",
			"accountNo",
			"repayAccountNo",
			"payAccountNo",
			"mobileNo",
			"idcardNo",
			"idcardType",
			"name",
			"asynResUrl",
			"synResUrl",
			"area"
			};

	//��Ҫǩ���ķ����ֶΣ���˳��
	final public static String[] resSignFields = {
			"serviceCode",
			"processCode",
			"merchantNo",
			"merchantOrderNo",
			"sequenceNo",
			"orderNo",
			"bankName",
			"accountType",
			"merchantPlanNo",
			"ppNo",
			"amount",
			"settleDate",
			"reckonSeqNo",
			"orderState",
			"payAmount",
			"planState",
			"responseCode",
			"returnType"
			};


	//���֡�����ѯ��Ҫǩ���������ֶΣ���˳��
	final public static String[] commReqSignFields = {
			"serviceCode",
			"processCode",
			"merchantNo",
			"merchantOrderNo",
			"sequenceNo",
			"direction",
			"orderNo",
			"orderDate",
			"amount",
			"refundAmount",
			"currency",
			"accountNo",
			};
	//绑卡签名字段
	final public static String[] bindCardSignFields = {
			"serviceCode",
			"processCode",
			"merchantNo",
			"sequenceNo", //请求流水
			"bindingId",
			"accountNo",
			"mobileNo",
			"idcardNo",
			"name",
			"asynResUrl",
			"synResUrl",
			"area",
	};

	//���֡�����ѯ��Ҫǩ���ķ����ֶΣ���˳��
	final public static String[] commResSignFields = {
			"serviceCode",
			"processCode",
			"merchantNo",
			"merchantOrderNo",
			"sequenceNo",
			"orderNo",
			"amount",
			"refundAmount",
			"settleDate",
			"reckonSeqNo",
			"orderState",
			"accountCode",
			"idOrNameCode",
			"mobileCode",
			"responseCode"
			};

}
