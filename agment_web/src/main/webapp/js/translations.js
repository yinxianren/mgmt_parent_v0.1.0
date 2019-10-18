/**
 * INSPINIA - Responsive Admin Theme
 *
 */
function config($translateProvider) {

    $translateProvider
        .translations('zh_CN', {
            // Define all menu elements
            HOME: '首页',




            TRANS_PRIVILEGE: '交易查询',
            TRANS_PAYMENT: '支付交易查询',
            TRANS_REPAY: '下发交易查询',
            WALLET_ACCOUNT: '钱包账户',
            AGENT_WALLET: '代理商钱包',
            MERCHANT_INFORMATION: '商户管理',
            MERCHANT_ADD: '新增商户',
            MERCHANT_MGMT: '商户列表',
            SYSTEM_INFORMATION_PRIVILEGE: '系统配置',
            USER_MGMT_PRIVILEGE: '用户管理',
            SECRET_KEY_CHANGE_PRIVILEGE: '密钥修改',
            ROLE_MGMT_PRIVILEGE: '角色管理',
            WALLET_DETAILS:'钱包明细',
            AGENT_WALLET_DETAILS:'代理商钱包明细',



            TRANS: '交易查询',
            TRANS_INFO: '交易查询',
            TRANS_BOND_INFO: '保证金查询',
            TRANS_CHANGE: '退款拒付',
            REFUND_APPLICATION: '退款申请',
            REFUND_INQUIRY: '退款查询',
            REFUSED_INQUIRY: '拒付查询',
            ACCOUNT_MGMT: '账户管理',
            BALANCE_INQUIRY: '余额查询',
            WITHDRAW_DEPOSIT_INQUIRY: '提现查询',
            REPORT_CENTER: '报表中心',
            DAILY_BILL: '日账单',
            MONTHLY_BILL: '月账单',
            RISK_SETTINGS: '风控配置',
            WEB_SITE_MGMT: '网站管理',
            LOGISTICS_MGMT: '物流管理',
            USER_CENTER: '用户中心',
            BASE_INFORMATION: '基本信息',
            CONFIGURATION_INFORMATION: '配置信息',
            PAYMENT_INFORMATION: '支付信息',
            SYSTEM_INFORMATION: '系统配置',
            USER_MGMT: '用户管理',
            SECRET_KEY_CHANGE: '密钥修改',
            ROLE_MGMT: '角色管理',
            OPINION: '建议与意见',
            // Define some custom text
            WELCOME: '欢迎来到汇融支付后台管理',
            MESSAGE_INFO: '主页',
            LANGUAGE: '语言'
        })
        .translations('en', {
            // Define all menu elements
            HOME: 'Home',
            TRANS: 'Transaction query',
            TRANS_INFO: 'Transaction query',
            TRANS_BOND_INFO: 'Cash deposit inquiry',
            TRANS_CHANGE: 'Transaction change',
            REFUND_APPLICATION: 'Refund application',
            REFUND_INQUIRY: 'Refund inquiry',
            REFUSED_INQUIRY: 'Refused inquiry',
            ACCOUNT_MGMT: 'Account management',
            BALANCE_INQUIRY: 'Balance inquiry',
            WITHDRAW_DEPOSIT_INQUIRY: 'Withdraw deposit inquiry',
            REPORT_CENTER: 'Report center',
            DAILY_BILL: 'Daily bill',
            MONTHLY_BILL: 'Monthly bill',
            RISK_SETTINGS: 'Risk settings',
            WEB_SITE_MGMT: 'Web site management',
            LOGISTICS_MGMT: 'Logistics management',
            USER_CENTER: 'User center',
            BASE_INFORMATION: 'Base information',
            CONFIGURATION_INFORMATION: 'Configuration information',
            PAYMENT_INFORMATION: 'Payment information',
            SYSTEM_INFORMATION: 'System information',
            USER_MGMT: 'User management',
            SECRET_KEY_CHANGE: 'Secret key change',
            ROLE_MGMT: 'Role management',
            OPINION: 'Opinion',
            // Define some custom text
            WELCOME: 'Welcome RxhPay',
            MESSAGE_INFO: '主页',
            LANGUAGE: 'Language'
        });
    $translateProvider.preferredLanguage('zh_CN');
}

angular
    .module('inspinia')
    .config(config);

