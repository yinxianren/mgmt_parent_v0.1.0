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
        .state('base_view.wallet.agent', {
            url: '/agentwallet',
            templateUrl: 'views/wallet/agentwallet'
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
        .state('base_view.merchant', {
            abstract: true,
            url: '/merchant',
            templateUrl: 'views/common/content'
        })
        .state('base_view.merchant.merchant_add', {
            url: '/merchant_add',
            templateUrl: 'views/merchant/merchant_add'
            , params: {
                goType: 0,
                merchantInfo: null,
                merchantType: null
            }
            , resolve: {
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

        .state('base_view.merchant.merchant_list', {
            url: '/merchant_list',
            templateUrl: 'views/merchant/merchant_list'
        })
        .state('base_view.merchant.agment_merchant_list', {
            url: '/agment_merchant_list',
            templateUrl: 'views/merchant/agment_merchant_list'
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

        .state('base_view.system_settings', {
            abstract: true,
            url: '/system_settings',
            templateUrl: 'views/common/content'
        })
        .state('base_view.system_settings.user_mgmt', {
            url: '/user_mgmt',
            templateUrl: 'views/system_settings/user_mgmt',
        })
        .state('base_view.system_settings.secret_key_change', {
            url: '/secret_key_change',
            templateUrl: 'views/system_settings/secret_key_change',
        })
        .state('base_view.system_settings.role_mgmt', {
            url: '/role_mgmt',
            templateUrl: 'views/system_settings/role_mgmt',
        })
        .state('base_view.wallet-details', {
            abstract: true,
            url: '/wallet-details',
            templateUrl: 'views/common/content'
        })
        .state('base_view.wallet-details.agent_details', {
            url: '/agent',
            templateUrl: 'views/wallet/agent_wallet_details'
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
