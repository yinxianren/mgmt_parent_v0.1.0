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
                        }
                    ]);
                }
            }
        })
        .state('base_view.code', {
            abstract: true,
            url: '/trans',
            templateUrl: 'views/common/content'
        })
        .state('base_view.code.inverst', {
            url: '/inverst',
            templateUrl: 'views/code/inverst'
        })
        .state('base_view.code.code_mgmt', {
            url: '/code_mgmt',
            templateUrl: 'views/code/code_mgmt',
        })
        .state('base_view.passageway', {
            abstract: true,
            url: '/passageway',
            templateUrl: 'views/common/content'
        })
        .state('base_view.passageway.payment_passageway', {
            url: '/payment_passageway',
            templateUrl: 'views/passageway/payment_passageway'
        })
        .state('base_view.passageway.bank', {
            url: '/bank',
            templateUrl: 'views/passageway/bank'
        })
        .state('base_view.passageway.lower_passageway_mgmt', {
            url: '/channelInfo',
            templateUrl: 'views/passageway/channelInfo'
        })
        .state('base_view.passageway.attach_passageway_mgmt', {
            url: '/attach_passageway_mgmt',
            templateUrl: 'views/passageway/attach_passageway_mgmt'
        })
        .state('base_view.agent', {
            abstract: true,
            url: '/agent',
            templateUrl: 'views/common/content'
        })
        .state('base_view.agent.agent_list', {
            url: '/agent_list',
            templateUrl: 'views/agent/agent_list'
        })
        .state('base_view.agent.agent_user', {
        url: '/agent_user',
        templateUrl: 'views/agent/agent_user',
        params: {
            goType: 0,
            merchantInfo: null,
            merchantType: null
        },
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
        .state('base_view.merchant', {
            abstract: true,
            url: '/merchant',
            templateUrl: 'views/common/content'
        })
        .state('base_view.merchant.merchant_list', {
            url: '/merchant_list',
            templateUrl: 'views/merchant/merchant_list'
        })
        .state('base_view.merchant.merchant_ip', {
            url: '/merchant_ip',
            templateUrl: 'views/merchant/merchant_ip'
        })
        .state('base_view.merchant.merchant_user', {
            url: '/merchant_user',
            templateUrl: 'views/merchant/merchant_user',
            params: {
                goType: 0,
                merchantInfo: null,
                merchantType: null
            },
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
        .state('base_view.merchant.merchant_add', {
            url: '/merchant_add',
            templateUrl: 'views/merchant/merchant_add',
            params: {
                goType: 0,
                merchantInfo: null,
                merchantType: null
            },
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
        .state('base_view.trans.audit', {
            url: '/audit',
            templateUrl: 'views/trans/audit'
        })
        .state('base_view.trans.withdrawal', {
            url: '/withdrawal',
            templateUrl: 'views/trans/withdrawal'
        })
        .state('base_view.order', {
            abstract: true,
            url: '/order',
            templateUrl: 'views/common/content'
        })
        .state('base_view.order.payment_order_sync', {
            url: '/payment_order_sync',
            templateUrl: 'views/order/payment_order_sync'
        })
        .state('base_view.order.payment_order_list', {
            url: '/payment_order_list',
            templateUrl: 'views/order/payment_order_list'
        })
        .state('base_view.order.lower_order_sync', {
            url: '/lower_order_sync',
            templateUrl: 'views/order/lower_order_sync'
        })
        .state('base_view.order.lower_order_list', {
            url: '/lower_order_list',
            templateUrl: 'views/order/lower_order_list'
        })
        .state('base_view.money', {
            abstract: true,
            url: '/money',
            templateUrl: 'views/common/content'
        })
        .state('base_view.money.withdrawal', {
            url: '/withdrawal',
            templateUrl: 'views/money/withdrawal'

        })
        .state('base_view.money.audit', {
            url: '/audit',
            templateUrl: 'views/money/audit'

        })
        .state('base_view.money.drawmoney', {
            url: '/drawmoney',
            templateUrl: 'views/money/drawmoney'
        })

        .state('base_view.user', {
            abstract: true,
            url: '/user',
            templateUrl: 'views/common/content'
        })
        .state('base_view.user.user_mgmt', {
            url: '/user_mgmt',
            templateUrl: 'views/user/user_mgmt'
        })
        .state('base_view.user.role_mgmt', {
            url: '/role_mgmt',
            templateUrl: 'views/user/role_mgmt'
        })
        .state('base_view.system_settings', {
            abstract: true,
            url: '/system_settings',
            templateUrl: 'views/common/content'
        })
        .state('base_view.system_settings.constantlist', {
            url: '/constantlist',
            templateUrl: 'views/constant/constantlist',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'jquery.utils',
                            files: ['js/jquery/jquery.utils.js']
                        },
                        {
                            name: 'ajax',
                            files: ['js/ajax/ajax.js']

                        }
                    ]);
                }
            }
        })
        .state('base_view.system_settings.sysgrouplist', {
            url: '/sysgrouplist',
            templateUrl: 'views/constant/sysgrouplist',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'jquery.utils',
                            files: ['js/jquery/jquery.utils.js']
                        },
                        {
                            name: 'ajax',
                            files: ['js/ajax/ajax.js']

                        }
                    ]);
                }
            }
        })
        .state('base_view.system_settings.refreshcache', {
            url: '/sysgrouplist',
            templateUrl: 'views/constant/refreshCache',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        {
                            name: 'jquery.utils',
                            files: ['js/jquery/jquery.utils.js']
                        },
                        {
                            name: 'ajax',
                            files: ['js/ajax/ajax.js']

                        }
                    ]);
                }
            }
        })
        .state('base_view.system_settings.system_log', {
            url: '/system_log',
            templateUrl: '/views/system_settings/system_log'
        })
        .state('base_view.system_settings.merchant_system_log', {
            url: '/merchant_system_log',
            templateUrl: '/views/system_settings/merchant_system_log'
        })
        .state('base_view.system_settings.agent_system_log', {
            url: '/agent_system_log',
            templateUrl: '/views/system_settings/agent_system_log'
        })
        .state('base_view.business', {
            abstract: true,
            url: '/business',
            templateUrl: 'views/common/content'

        })
        .state('base_view.business.merchant_mgmt', {
            url: '/merchant_mgmt',
            templateUrl: 'views/business/merchant_mgmt'
        })
        .state('base_view.business.merchant_info', {
            url: '/merchant_info',
            templateUrl: 'views/business/merchant_info',
            params: {
                merchant: null,
                goType: null,
                merchantCategory: null,
                businessType: null
            },
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
                        },
                        {
                            name: 'select-css',
                            files: ['css/select.css']
                        },
                        {
                            name: 'select.min',
                            files: ['js/bootstrap/select.js']
                        }
                    ]);
                }
            }
        })
        .state('base_view.business.merchant_setting', {
            url: '/merchant_setting',
            templateUrl: 'views/business/merchant_setting',
            params: {
                merchant: null,
                goType: null
            }
        })
        .state('base_view.business.merchant_user', {
            url: '/merchant_user',
            templateUrl: 'views/business/merchant_user',
            params: {
                merchant: null
            }
        })
        .state('base_view.business.quick_test', {
            url: '/quick_test',
            templateUrl: 'views/business/quick_test'
        })
        .state('base_view.wallet', {
            abstract: true,
            url: '/wallet',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet.merchant', {
            url: '/merchant',
            templateUrl: 'views/wallet/merchantwallet'
        })
        .state('base_view.wallet.agent', {
            url: '/agent',
            templateUrl: 'views/wallet/agentwallet'
        })
        .state('base_view.wallet-details.agent_details', {
            url: '/agent',
            templateUrl: 'views/wallet/agent_wallet_details'
        })

        .state('base_view.wallet-details.channel_details', {
            url: '/channel',
            templateUrl: 'views/wallet/channel_wallet_details'
        })


        .state('base_view.wallet.channel', {
            url: '/channel',
            templateUrl: 'views/wallet/channelwallet'
        })
        .state('base_view.wallet.terminal', {
            url: '/terminal',
            templateUrl: 'views/wallet/terminalwallet'
        })
        .state('base_view.risk', {
            abstract: true,
            url: '/risk',
            templateUrl: 'views/common/content'
        })
        .state('base_view.risk.order_tracking', {
            url: '/order_tracking',
            templateUrl: 'views/risk/order_tracking'
        })
        .state('base_view.risk.channel_history', {
            url: '/channel_history',
            templateUrl: 'views/risk/channel_history'
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
