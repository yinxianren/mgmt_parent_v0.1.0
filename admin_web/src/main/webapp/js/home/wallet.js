
// 商户钱包查询
function merchantWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.selected={};
    $scope.searchInfo = {};
    $scope. merchantWallet=null;
    httpSvc.getData('post', '/merchantWallet/idsInit').then(function (value) {
        $scope.agents = value.agents;
        $scope.merchants = value.merchants;
    });
    httpSvc.getData('post', '/merchantWallet/search',$scope.searchInfo).then(function (value) {
        $scope.merchantWalletTable = new NgTableParams({}, {
            dataset: value
        });
        angular.element('.ibox-content').removeClass('sk-loading');
    });

    $scope.search = function () {
        $scope.merchantWalletTable=null;
        httpSvc.getData('post', '/merchantWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.merchantWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    $scope.resetForm = function () {
        tablereload();
    }

    tablereload=function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/merchantWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.merchantWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }


   //商户钱包删除
    $scope.batchDel = function () {
        var ids = []
        var count=0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + count+ '条数据吗？</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });
        modalInstance.result.then(function () {
            httpSvc.getData('post', '/merchantWallet/batchDel', ids).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '商户钱包删除',
                        body: '删除成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '商户钱包删除',
                        body: '删除失败！'
                    });
                }
                $scope.selected={};
                idArr = [];
                tablereload();
            });
        }, function () {
        });
    }



}
//代理商钱包查询
function agentWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.searchInfo = {};
    $scope.selected={};
    httpSvc.getData('post', '/merchantWallet/idsInit').then(function (value) {
        $scope.agents = value.agents;
        httpSvc.getData('post', '/agentWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.AgentWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });
    $scope.search = function () {
        httpSvc.getData('post', '/agentWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.AgentWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    tablereload=function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/agentWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.AgentWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    $scope.resetForm = function () {
        tablereload();

    }
    //代理商钱包删除
    $scope.batchDel = function () {
        var ids = []
        var count=0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + count+ '条数据吗？</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });
        modalInstance.result.then(function () {
            httpSvc.getData('post', '/agentWallet/batchDel', ids).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '代理商钱包删除',
                        body: '删除成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '代理商钱包删除',
                        body: '删除失败！'
                    });
                }
                $scope.selected={};
                idArr = [];
                tablereload();
            });
        }, function () {
        });
    }


}
//平台钱包查询
function channelWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.selected={};
    $scope.searchInfo = {};
    $scope. ChannelWallet=null;

    httpSvc.getData('post', '/channelWallet/idsInit').then(function (value) {
        $scope.paytype = value.paytype;
        $scope.channels = value.channels;
        $scope.organizations = value.organizations;
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo).then(function (result) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: result
            });
            console.log(value.length);
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });


    $scope.search = function () {
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: value
            });
            console.log($scope.ChannelWalletTable);
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    tablereload=function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo).then(function (value) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    }

    $scope.resetForm = function () {
        tablereload();
    }

    //通道钱包删除
    $scope.batchDel = function () {
        var ids = []
        var count=0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + count+ '条数据吗？</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });
        modalInstance.result.then(function () {
            httpSvc.getData('post', '/channelWallet/batchDel', ids).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '通道钱包删除',
                        body: '删除成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '通道钱包删除',
                        body: '删除失败！'
                    });
                }
                $scope.selected={};
                idArr = [];
                tablereload();
            });
        }, function () {
        });
    }



}

//终端商户钱包查询
function terminalwalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.selected={};
    $scope.searchInfo = {};
    $scope. terminalwalletTable=null;
    $scope.merchants = [];
    httpSvc.getData('post', '/terminalMerchantsWallet/idsInit').then(function (value) {
        $scope.paytype = value.paytype;
        // $scope.channels = value.channels;
        $scope.organizations = value.organizations;
        $scope.merchants = value.merchants;
        httpSvc.getData('post', '/terminalMerchantsWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    });

    $scope.search = function () {
        httpSvc.getData('post', '/terminalMerchantsWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    tablereload=function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/terminalMerchantsWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    }

    $scope.resetForm = function () {
        tablereload();
    }

    //通道钱包删除
    $scope.batchDel = function () {
        var ids = []
        var count=0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + count+ '条数据吗？</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });
        modalInstance.result.then(function () {
            httpSvc.getData('post', '/terminalMerchantsWallet/batchDel', ids).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '通道钱包删除',
                        body: '删除成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '通道钱包删除',
                        body: '删除失败！'
                    });
                }
                $scope.selected={};
                idArr = [];
                tablereload();
            });
        }, function () {
        });
    }

}


function agentWalletDetailsCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 代理商户号
    $scope.agentMerIds = [];
    // 商户订单号
    $scope.merOrderIds = [];
    // 平台订单号
    $scope.orderIds = [];
    // 变动类型
    $scope.types = [];

    // 初始化数据
    $timeout(function () {
        httpSvc.getData('post', '/agentWallet/init').then(function (value) {
            $scope.payTypes = value.payTypes;
            $scope.detailsTypes = value.detailsTypes;
            $scope.agents = value.agents;
            $scope.agentWalletDetailsTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/agentWallet/findAgentWallteDetailsPage', {
                        pageNum: params.page()-1,
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        params.total(value.total);
                        angular.element('.ibox-content').removeClass('sk-loading');
                        return value.rows;
                    });
                }
            });
        });
    });
    $scope.$watch('searchInfo.startDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.tradeTime;
            $scope.endDateDisable = false;
            if (oldEndDate !== undefined) {
                $scope.searchInfo.endDate = oldEndDate;
            }
        } else {
            $scope.endDateDisable = true;
            oldEndDate = $scope.searchInfo.endDate;
            $scope.searchInfo.endDate = undefined;
        }
    });
    $scope.$watch('searchInfo.endDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = $scope.searchInfo.endDate;
        } else {
            $scope.dateOptions1.maxDate = new Date();
        }
    });
    var oldEndDate = null;
    $scope.endDateDisable = true;
    $scope.format = 'yyyy-MM-dd';
    $scope.dateOptions1 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.dateOptions2 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.altInputFormats = ['yyyy/MM/dd'];
    $scope.popup1 = {
        opend: false
    };
    $scope.openDatepicker1 = function () {
        $scope.popup1.opend = !$scope.popup1.opend;
    };
    $scope.popup2 = {
        opend: false
    };
    $scope.openDatepicker2 = function () {
        $scope.popup2.opend = !$scope.popup2.opend;
    };
    // 查询
    $scope.search = function(){
        var searchInfo = angular.copy($scope.searchInfo);
        tableReload(searchInfo);
    };
    // 重置
    $scope.reset = function(){
        $scope.searchInfo = {};
        var searchInfo = {};
        tableReload(searchInfo);
    };

    // 重新加载
    function tableReload(searchInfo) {
        angular.element('.ibox-content').addClass('sk-loading');
        $scope.agentWalletDetailsTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/agentWallet/findAgentWallteDetailsPage', {
                    pageNum: params.page()-1,
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    searchInfo: $scope.searchInfo
                }).then(function (value) {
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    }
}

function channelWalletDetailsCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 通道ID
    $scope.channelIds = [];
    // 通道交易编码
    $scope.channelTransCodes = [];
    // 支付方式
    $scope.payTypes = [];
    // 商户订单号
    $scope.merOrderIds = [];
    // 平台订单号
    $scope.orderIds = [];
    // 变动类型
    $scope.types = [];

    // 初始化数据
    $timeout(function () {
        httpSvc.getData('post', '/channelWallet/init').then(function (value) {
            $scope.payTypes  = value.payTypes;
            $scope.detailsTypes = value.detailsTypes;
            $scope.channelWalletDetailsTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/channelWallet/findChannelWallteDetailsPage', {
                        pageNum: params.page()-1,
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        params.total(value.total);
                        angular.element('.ibox-content').removeClass('sk-loading');
                        return value.rows;
                    });
                }
            });
        });
    });
    $scope.$watch('searchInfo.startDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.tradeTime;
            $scope.endDateDisable = false;
            if (oldEndDate !== undefined) {
                $scope.searchInfo.endDate = oldEndDate;
            }
        } else {
            $scope.endDateDisable = true;
            oldEndDate = $scope.searchInfo.endDate;
            $scope.searchInfo.endDate = undefined;
        }
    });
    $scope.$watch('searchInfo.endDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = $scope.searchInfo.endDate;
        } else {
            $scope.dateOptions1.maxDate = new Date();
        }
    });
    var oldEndDate = null;
    $scope.endDateDisable = true;
    $scope.format = 'yyyy-MM-dd';
    $scope.dateOptions1 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.dateOptions2 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.altInputFormats = ['yyyy/MM/dd'];
    $scope.popup1 = {
        opend: false
    };
    $scope.openDatepicker1 = function () {
        $scope.popup1.opend = !$scope.popup1.opend;
    };
    $scope.popup2 = {
        opend: false
    };
    $scope.openDatepicker2 = function () {
        $scope.popup2.opend = !$scope.popup2.opend;
    };
    // 查询
    $scope.search = function(){
        var searchInfo = angular.copy($scope.searchInfo);
        tableReload(searchInfo);
    };
    // 重置
    $scope.reset = function(){
        $scope.searchInfo = {};
        var searchInfo = {};
        tableReload(searchInfo);
    };

    // 重新加载
    function tableReload(searchInfo) {
        angular.element('.ibox-content').addClass('sk-loading');
        $scope.channelWalletDetailsTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/channelWallet/findChannelWallteDetailsPage', {
                    pageNum: params.page()-1,
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    searchInfo: $scope.searchInfo
                }).then(function (value) {
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    }
}

// 线下充值
function investCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
    // 初始化数据
    httpSvc.getData('post', '/payOrder/init').then(function (value) {
        $scope.organizations = value.organizations;
        $scope.channels = value.channels;
        $scope.payTypes = value.payType;
        $scope.orderStatus = value.orderStatus;
        $scope.settleStatus = value.settleStatus;
        $scope.merchant = value.merchants;
        $scope.identityTypes = value.identityTypes;
        $scope.bankcardTypes = value.bankcardTypes;
        $scope.agents = value.agents;
    });
    $scope.investInfo={};
    function getMerchants() {
        var investInfo = {
            merId: $scope.merId
        };
        return investInfo;
    }
    $scope.investInfo = getMerchants();
    $scope.changeed = function () {
        httpSvc.getData('post', '/payOrder/getChannels',$scope.investInfo).then(function (value) {
            $scope.channels = value.data;
        })
    }

    function getInvestInfo() {
        var investInfo = {
            merId: $scope.merId,
            amount: $scope.amount,
            password:$scope.password,
            updateTime: ''
        };
        return investInfo;
    }
    $scope.investInfo=getInvestInfo();
    var myalert = function () {
        var title = '密码验证提醒';
        var content =
            '<div class="text-center">' +
            '<strong>密码错误</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });

    }
    var myalert1 = function () {
        var title = '商户号验证提醒';
        var content =
            '<div class="text-center">' +
            '<strong>商户号不存在</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });

    }
    var myalert2 = function () {
        var title = '验证提醒';
        var content =
            '<div class="text-center">' +
            '<strong>商户号不能为空</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });

    }
    var myalert3 = function () {
        var title = '验证提醒';
        var content =
            '<div class="text-center">' +
            '<strong>金额不能为空</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });

    }

    $scope.invest = function () {
        if($scope.investInfo.merId==null){
            myalert2();
            return;
        }
        if($scope.investInfo.amount==null){
            myalert3();
            return;
        }
        var title = '充值确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定往商户' + $scope.investInfo.merId + '充值' + $scope.investInfo.amount+ '元吗？</strong>' +
            '</div>';
        var modalInstance = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title;
                },
                content: function () {
                    return content;
                }
            }
        });
        modalInstance.result.then(function () {
            httpSvc.getData('post', '/merchantWallet/invest', $scope.investInfo).then(function (value) {
                if (value ==1) {
                    toaster.pop({
                        type: 'success',
                        title: '商户钱包充值',
                        body: '充值成功！'
                    });
                } else if(value==2) {
                    myalert();
                    return;
                }else if(value==3){
                    myalert1();
                    return;
                }else{
                    toaster.pop({
                        type: 'error',
                        title: '商户钱包充值',
                        body: '充值失败！'
                    });
                }
            });
        });
    }


}

angular
    .module('inspinia')
    .controller('merchantWalletCtrl', merchantWalletCtrl)
    .controller('agentWalletCtrl', agentWalletCtrl)
    .controller('channelWalletCtrl', channelWalletCtrl)
    .controller('terminalwalletCtrl', terminalwalletCtrl)
    .controller('agentWalletDetailsCtrl', agentWalletDetailsCtrl)
    .controller('channelWalletDetailsCtrl', channelWalletDetailsCtrl)
    .controller('investCtrl',investCtrl)
;
