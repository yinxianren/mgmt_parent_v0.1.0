/**
 * INSPINIA - Responsive Admin Theme
 *
 * Inspinia theme use AngularUI Router to manage routing and views
 * Each view are defined as state.
 * Initial there are written state for all view in theme.
 *
 */
function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider, $httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');

    // Configure Idle settings
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds

    $urlRouterProvider.otherwise('/base_view/home/home_page');

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false
    });

    $stateProvider
        .state('base_view', {
            abstract: true,
            url: '/base_view',
            templateUrl: 'views/common/base_view',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            name: 'toaster',
                            files: ['js/plugins/angular-toaster/toaster.min.js', 'css/plugins/angular-toaster/toaster.min.css']
                        }
                    ]);
                }
            }
        })
        .state('base_view.home', {
            abstract: true,
            url: '/home',
            templateUrl: 'views/common/content'
        })
        .state('base_view.home.home_page', {
            url: '/home_page',
            templateUrl: 'views/home_page',
            data: {pageTitle: 'HomePage'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            serie: true,
                            name: 'chart.js',
                            files: ['js/plugins/chartJs/Chart.min.js', 'js/plugins/angular-chart/angular-chart.min.js']
                        },
                        {
                            files: ['js/plugins/c3Charts/c3.min.js', 'js/plugins/c3Charts/d3.min.js', 'css/plugins/charts/c3.min.css']
                        }
                    ]);
                }
            }
        })


        .state('base_view.trans', {
            abstract: true,
            url: '/trans',
            templateUrl: 'views/common/content'
        })
        .state('base_view.trans.trans_payment', {
            url: '/trans_payment',
            templateUrl: 'views/trans/trans_payment'
        })
        .state('base_view.trans.repay', {
            url: '/repay',
            templateUrl: 'views/trans/repay'
        })

        .state('base_view.wallet', {
            abstract: true,
            url: '/wallet',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet.merchant_wallet', {
            url: '/merchant_wallet',
            templateUrl: 'views/wallet/merchant_wallet'
        })
        .state('base_view.wallet.terminal_mer_wallte', {
            url: '/terminal_mer_wallte',
            templateUrl: 'views/wallet/terminal_mer_wallte'
        })
        .state('base_view.wallet.merchant', {
            url: '/merchant',
            templateUrl: 'views/wallet/merchantwallet'
        })



        .state('base_view.batch_repay', {
            abstract: true,
            url: '/batch_repay',
            templateUrl: 'views/common/content'

        })
        .state('base_view.batch_repay.load', {
            url: '/batch_repay_load',
            templateUrl: 'views/batch_repay/batch_repay',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'angularFileUpload',
                            files: ['js/plugins/angular-file-upload/angular-file-upload.min.js']
                        },
                        {
                            files: [
                                'css/plugins/Gallery-master/blueimp-gallery.min.css',
                                'js/plugins/Gallery-master/jquery.blueimp-gallery.min.js']
                        }
                    ]);
                }
            }

        })


        .state('base_view.wallet-details', {
            abstract: true,
            url: '/wallet-details',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet-details.merdetails', {
            url: '/merdetails',
            templateUrl: 'views/wallet-details/merdetails'
        })
        .state('base_view.wallet-details.terminal', {
            url: '/terminal',
            templateUrl: 'views/wallet-details/terminal'
        })
        .state('base_view.finance', {
            abstract: true,
            url: '/finance',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet.finance_drawing', {
            url: '/finance_drawing',
            templateUrl: 'views/wallet/finance_drawing'
        })
        .state('base_view.order', {
            abstract: true,
            url: '/order',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet.pay_order_change', {
            url: '/pay_order_change',
            templateUrl: 'views/wallet/pay_order_change'
        })
        .state('base_view.merchant', {
            abstract: true,
            url: '/merchant',
            templateUrl: 'views/common/content'
        })
        .state('base_view.merchant.merchant_list', {
            url: '/merchant_list',
            templateUrl: 'views/merchant/merchant_list'
        })
        .state('base_view.card', {
            abstract: true,
            url: '/card',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet.merchant_card', {
            url: '/merchant_card',
            templateUrl: 'views/wallet/merchant_card'
        })



        .state('base_view.trans_change', {
            abstract: true,
            url: '/trans',
            templateUrl: 'views/common/content'
        })
        .state('base_view.trans_change.refund_application', {
            url: '/trans_change',
            templateUrl: 'views/trans/trans_change'
        })
        .state('base_view.trans_change.refund_inquiry', {
            url: '/refund_inquiry',
            templateUrl: 'views/trans/refund_inquiry'
        })
        .state('base_view.trans_change.refund_inquiry_status', {
            url: '/refund_inquiry/{changeStatus}',
            templateUrl: 'views/trans/refund_inquiry'
        })
        .state('base_view.trans_change.refused_inquiry', {
            url: '/refused_inquiry',
            templateUrl: 'views/trans/refused_inquiry'
        })
        .state('base_view.account_mgmt', {
            abstract: true,
            url: '/account',
            templateUrl: 'views/common/content'
        })
        .state('base_view.account_mgmt.balance_inquiry', {
            url: '/balance_inquiry',
            templateUrl: 'views/account/balance_inquiry'
        })
        .state('base_view.account_mgmt.withdraw_deposit_inquiry', {
            url: '/withdraw_deposit_inquiry',
            templateUrl: 'views/account/withdraw_deposit_inquiry'
        })
        .state('base_view.report_center', {
            abstract: true,
            url: '/report',
            templateUrl: 'views/common/content'
        })
        .state('base_view.report_center.daily_bill', {
            url: '/daily_bill',
            templateUrl: 'views/report/daily_bill'
        })
        .state('base_view.report_center.monthly_bill', {
            url: '/monthly_bill',
            templateUrl: 'views/report/monthly_bill'
        })
        .state('base_view.risk_settings', {
            abstract: true,
            url: '/risk',
            templateUrl: 'views/common/content'
        })
        .state('base_view.risk_settings.web_site_mgmt', {
            url: '/web_site_mgmt',
            templateUrl: 'views/risk/web_site_mgmt',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'angularFileUpload',
                            files: ['js/plugins/angular-file-upload/angular-file-upload.min.js']
                        },
                        {
                            files: ['css/plugins/jasny-bootstrap/jasny-bootstrap.min.css']
                        }
                    ]);
                }
            }
        })
        .state('base_view.risk_settings.logistics_mgmt', {
            url: '/logistics_mgmt',
            templateUrl: 'views/risk/logistics_mgmt',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'angularFileUpload',
                            files: ['js/plugins/angular-file-upload/angular-file-upload.min.js']
                        },
                        {
                            files: ['css/plugins/jasny-bootstrap/jasny-bootstrap.min.css']
                        }
                    ]);
                }
            }
        })
        .state('base_view.user_center', {
            abstract: true,
            url: '/user_center',
            templateUrl: 'views/common/content'
        })
        .state('base_view.user_center.base_information', {
            url: '/base_information',
            templateUrl: 'views/user/base_information',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'angularFileUpload',
                            files: ['js/plugins/angular-file-upload/angular-file-upload.min.js']
                        },
                        {
                            files: [
                                'css/plugins/Gallery-master/blueimp-gallery.min.css',
                                'js/plugins/Gallery-master/jquery.blueimp-gallery.min.js']
                        }
                    ]);
                }
            }
        })
        .state('base_view.user_center.configuration_information', {
            url: '/configuration_information',
            templateUrl: 'views/user/configuration_information'
        })
        .state('base_view.user_center.payment_information', {
            url: '/payment_information',
            templateUrl: 'views/user/payment_information'
        })
        .state('base_view.system_information', {
            abstract: true,
            url: '/user_center',
            templateUrl: 'views/common/content'
        })
        .state('base_view.system_information.user_mgmt', {
            url: '/user_mgmt',
            templateUrl: 'views/system/mer_user_mgmt'
        })
        .state('base_view.system_information.secret_key_change', {
            url: '/secret_key_change',
            templateUrl: 'views/system/mer_secret_key_change'
        })
        .state('base_view.system_information.role_mgmt', {
            url: '/role_mgmt',
            templateUrl: 'views/system/role_mgmt'
        })
        .state('base_view.system_information.opinion', {
            url: '/opinion',
            templateUrl: 'views/system/opinion',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'textAngular',
                            files: [
                                'css/plugins/textAngular/textAngular.css',
                                'js/plugins/textAngular/textAngular-rangy.min.js',
                                'js/plugins/textAngular/textAngular-sanitize.min.js',
                                'js/plugins/textAngular/textAngular.min.js',
                                'js/plugins/textAngular/textAngularSetup.js'
                            ],
                            serie: true
                        }
                    ]);
                }
            }
        })
    ;
}

function authInterceptor($rootScope, $q, $window) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            if ($window.sessionStorage.token) {
                config.headers.Authorization = 'Bearer ' + $window.sessionStorage.token;
            }
            return config;
        },
        response: function (response) {
            if (response.status === 401) {
                location.reload();
            }
            return response || $q.when(response);
        }
    };
}

angular
    .module('inspinia')
    .factory('authInterceptor', authInterceptor)
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });
