package com.rxh.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/2
 * Time: 17:29
 * Project: Management
 * Package: com.rxh.utils
 *   SystemConstant.CUS_SUCCESS
 */
public class SystemConstant {
    /**
     * 四方系统常量
     */
    // 商户启用
    public final  static String SQUARE_ENABLE = "0";
    // 商户禁用
    public final  static String DIS_ENABLE = "1";
    // 商户未审核
    public final  static String DIS_CHECK = "2";
    // 未支付
    public final  static Integer NO_PAY = 2;
    // 未结算
    public final  static Integer NO_SETTLE = 0;
    //收单mq处理中
    public final static Integer MQ_PROCESSED=20;
    //收单漏单更新状态
    public final static Integer MQ_PROCESSED_23=23;
    //代付mq处理中
    public final static Integer MQ2_PROCESSED=30;
    //代付漏单更新状态
    public final static Integer MQ2_PROCESSED_33=33;
    //内部程序处理失败
    public final static Integer MQ_EXCEPITON_PROCESSED=40;
    //提现审核状态
    //待审核
    public final  static Integer DRAW_WAIT_AUDIT = 0;
    //待划款
    public final  static Integer DRAW_WAIT_PAY = 1;
    //已划款
    public final  static Integer DRAW_PAY_SUCCESS = 2;
    //未通过
    public final  static Integer DRAW_NO_SUCCESS = 3;

    /**
     * 风控引用类型
     */
    public final static String[] SQUARE_REF_TYPE = {"通道", "商户"};


    //绑卡类型
    public final static String BONDCARD ="1";
    //进件类型
    public final static String ADDCUS ="2";

    public final static Integer CHANNEL_XINSHENG =6;

    //支付平台单号
    public final static String payId = "PAY_ID";
    //商户/代理 id
    public final static String customerId = "CUSTOMER_ID";
    //提现id
    public final static String id = "ID";


    public final static String name = "NAME";
    public final static String exceptionId = "EXCEPTION_ID";
    //订单类型
    public final static String type = "TYPE";

    //提现说明
    public final static String remark = "REMARK";

    //订单追踪id
    public final static String sysId = "SYSID";
    //
    public final static String returnUrl = "RETURNURL";

    public final static String noticeUrl = "NOTICEURL";

    public final static String orderTrackStatus = "ORDERTRACKSTATUS";

    public final static String tradeInfo = "tradeinfo";

    public final static String refer = "REFER";
    //详情信息类型
    //冲正
    public final static String CORRECT = "6";
    public final static String DARW_MONEY = "7";





    //机构名称
    public final static String organizationName = "ORGANIZATION_NAME";

    //商户号
    public final static String merId = "MER_ID";

    //商户订单号
    public final static String merOrderId = "MER_ORDER_ID";

    //通道名称
    public final static String channelName = "CHANNEL_NAME";
    //提现状态
    public final static String DRAWSTATUS = "drawStatus";

    //支付类型
    public final static String payType = "PAY_TYPE";

    //商户通道编号
    public final static String channelTransCode = "CHANNEL_TRANS_CODE";

    //
    public final static String income = "income";

    // 新增
    public final static String SUCCESS = "0";
    //可用
    public final static Integer ENABLE = 0;
    //不可用
    public final static Integer DISABLE = 1;
    // 新增
    public final static String FAIL = "1";
    // 秒转毫秒
    public final static long SECOND_TO_MILLISECOND = 1000L;
    // 分钟转毫秒
    public final static long MINUTE_TO_MILLISECOND = 60 * SECOND_TO_MILLISECOND;
    // 小时转毫秒
    public final static long HOUR_TO_MILLISECOND = 60 * MINUTE_TO_MILLISECOND;

    // 内部管理员角色
    public final static String ROLE_INTERNAL_ADMIN = "ROLE_INTERNAL_ADMIN";
    // 内部用户角色
    public final static String ROLE_INTERNAL_USER = "ROLE_INTERNAL_USER";
    // 商户管理员角色
    public final static String ROLE_MERCHANT_ADMIN = "ROLE_MERCHANT_ADMIN";
    // 商户用户角色
    public final static String ROLE_MERCHANT_USER = "ROLE_MERCHANT_USER";
    // 管理员权限ID
    public final static long ROLE_ADMIN_ID = 777L;
    // 未审核
    public final static short ORDER_CHANGE_STATUS_UNAUDITED = 0;
    // 订单变更成功
    public final static short ORDER_CHANGE_STATUS_SUCCESSFUL = 1;
    // 订单变更失败
    public final static short ORDER_CHANGE_STATUS_FAIL = 2;
    // 订单变更银行处理中
    public final static short ORDER_CHANGE_STATUS_PROCESSING = 3;
    // 已通过
    public final static short PASSED = 1;
    // 未通过
    public final static short DID_NOT_PASS = 2;
    // 正常
    public final static short EXCEPTION_STATUS_NORMAL = 0;
    // 冻结
    public final static short EXCEPTION_STATUS_FREEZE = 1;
    // 退款
    public final static short EXCEPTION_STATUS_REFUND = 2;
    // 拒付
    public final static short EXCEPTION_STATUS_REFUSED = 4;
    // 交易汇率
    public final static short TRANSACTION_RATE = 0;
    // 结算汇率
    public final static short SETTLEMENT_RATE = 1;
    // 划款汇率
    public final static short FUNDING_RATE = 2;
    // 全额退款
    public final static short FULL_REFUND = 2;
    // 部分退款
    public final static short PARTIAL_REFUND = 3;
    // 全额拒付
    public final static short FULL_REFUSED = 4;
    // 部分拒付
    public final static short PARTIAL_REFUSED = 5;
    // 代理拒付
    public final static short PROXY_REFUSED = 6;
    // 银行处理退款中
    public final static short BANK_PROCESSING_REFUND = 7;
    // 人民币
    public final static String CURRENCY_CNY = "CNY";
    public final static String CURRENCY_USD = "USD";
    //存在状态 -启用(收单机构 core_acquirer ==> status)
//    public final static short STATUS_ENABLE = 0;
    public final static short STATUS_ENABLE = 1;
    /**
     * 限定笔
     */
    public final static int LIMIT_EVERY = 0;
    /**
     * 限定日
     */
    public final static int LIMIT_DAY = 1;

    /**
     * 限定月
     */
    public final static int LIMIT_MONTH = 2;

    /**
     * 限定年
     */
    public final static int LIMIT_YEAR = 3;

    /**
     * 风控引用类型
     */
    public final static String[] REF_TYPE = {"通用", "收单机构", "商户", "域名"};
    // 汇款支付
    public final static String REMITTANCE = "Remittance";
    // 银行状态
    public final static String BANK_STATUS = "BankStatus";
    // 快递状态
    public final static String SHIP_STATUS = "ShipStatus";
    // 已发货
    public final static short SHIP_STATUS_SHIPPED = 1;
    // 订单变更状态：未通过
    public final static short REFUND_STATUS_DID_NOT_PASS = 2;
    // 订单变更状态：银行处理中
    public final static short REFUND_STATUS_BANK_PROCESSING = 3;
    // 退款处理中
    public final static short EXCEPTION_STATUS_REFUND_PROCESSING = 7;
    // 清算状态
    public final static String FINANCE_STATUS = "FinanceStatus";
    // 洲
    public final static String CONTINENT = "Continent";
    // 币种
    public final static String CURRENCY = "Currency";
    // 语言
    public final static String LANG = "Lang";
    // 国家
    public final static String COUNTRY = "Country";
    //
    public final static String QUICK_TEST = "QuickTest";
    // 机构类型
    public final static String ACQUIRER_TYPE = "AcquirerType";
    // 通道交易币种类型
    public final static String CURRENCY_TYPE = "CurrencyType";
    // 风控
    public final static String RISK = "Risk";
    // 存在状态
    public final static String STATUS = "Status";
    //启用状态
    public final static String availableStatus = "availableStatus";
    //通道等级
    public final static String channelLevel = "channelLevel";
    public final static String PAYTYPE = "PayType";
    public final static String DETAILSTYPE = "detailsType";

    // 附属通道类型
    public final static String EXTRATYPE = "extraType";
    public final static String ORDERSTATUS = "orderStatus";
    public final static String SETTLESTATUS = "settleStatus";
    //商户类型
    public final static String MERCHANTTYPE = "merchantType";
    //证件类型
    public final static String IDENTITYTYPE = "identityType";
    //银行卡类型
    public final static String BANKCARDTYPE = "bankcardType";
    // 商户状态
    public final static String MERCHANT_STATUS = "MerchantStatus";
    // 银行状态：成功
    public final static short BANK_STATUS_SUCCESS = 0;
    // 银行状态：失败
    public final static short BANK_STATUS_FAIL = 1;
    // 银行状态：未支付
    public final static short BANK_STATUS_UNPAID = 2;
    // 银行状态：支付中
    public final static short BANK_STATUS_PENDING_PAYMENT = 3;
    // 银行状态：超时
    public final static short BANK_STATUS_TIME_OUT = 98;
    // 银行状态：重发
    public final static short BANK_STATUS_RESEND = 4;
    // 系统日志类型：
    public final static short LOG_LOGIN = 0;
    // 系统日志类型：
    public final static short LOG_ADD = 1;
    // 系统日志类型：
    public final static short LOG_DELETE = 2;
    // 系统日志类型：
    public final static short LOG_UPDATE = 3;
    // 系统日志类型：
    public final static short LOG_READ = 4;
    // 系统日志类型：未知方法
    public final static short LOG_UNKNOWN_METHOD = 8;
    // 系统日志类型：未知方法
    public final static short LOG_ERROR = 9;
    // 申请类型:商户 2:内部人员
    public final static short FINANCE_DRAWING_TYPE_MERCHANT = 1;
    public final static short FINANCE_DRAWING_TYPE_INTERNAL = 2;
    public final static String COUNRTY_CODE_CHN = "CHN";
    public final static String QUESTION_TYPE = "QuestionType";
    public final static String QUESTION_STATUS = "QuestionStatus";

    // 交易状态-支付网关
    public final static short PAY_STATUS_GATEWAY = 0;

    // 交易状态-支付页面
    public final static short PAY_STATUS_PAGE = 1;

    // 交易状态-平台风控
    public final static short PAY_STATUS_RISK = 2;

    // 交易状态-外置风控
    public final static short PAY_STATUS_OUTLAY = 3;

    // 交易状态-发送银行
    public final static short PAY_STATUS_SEND = 4;

    // 交易状态-结果处理
    public final static short PAY_STATUS_RESPONSE = 5;

    // 交易状态-返回商户
    public final static short PAY_STATUS_RETURN = 6;

    // 跳转接口
    public final static short INTERFACE_REDIRECT = 0;

    // 内嵌接口
    public final static short INTERFACE_INNER = 1;

    // 退款失败
    public final static short CHANGE_BANK_STATUS_FAIL = 2;

    //进件通道状态 0：表示可用
    public final  static Integer CUS_USABLE=0;
    //进件通道状态 1：表示不可用
    public final static Integer CUS_DISABLE=1;
    //进件通道状态 2：表示处理中  processed   SystemConstant.CUS_PROCESSD
    public final static Integer CUS_PROCESSD=2;

    //基础信息登记
    public final static String INFORMATION_REGISTRATION ="J001";
    //银行卡等级
    public final static String BANKCARD_REGISTRATION ="J002";
    //业务开通
    public final static String SERVICE_FULFILLMENT="J003";
    //绑卡申请
    public final static String BONDCARD_APPLY="B001";
    //绑卡短信
    public final static String BONDCARD_SMS="B002";
    //绑卡确认
    public final static String BONDCARD_CONFIRM="B003";
    //支付申请
    public final static String PAY_APPLY="P001";
    //支付短信
    public final static String PAY_SMS="P002";
    //支付确认
    public final static String PAY_CONFIRM="P003";
    //代付
    public final static String GENERATION_PAY="D001";

    //支付类型集合
    public final static Map<String,String> PAY_TYPE_MAP = new HashMap<String, String>(){
        {
            put("COMMON", "0");
            put("VISA", "1");
            put("MASTER", "2");
            put("JCB", "3");
        }
    };

}
