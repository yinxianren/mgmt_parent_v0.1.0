
// 支付交易查询
function transPaymentCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 全部机构
    $scope.organizations = [];
    // 通道名称
    $scope.channels = [];
    // 支付方式
    $scope.payTypes = [];
    // 订单状态
    $scope.orderStatus = [];
    // 结算状态
    $scope.settleStatus = [];
    // 商户
    $scope.merchants = [];
    //证件类型
    $scope.identityTypes = [];
    // 银行卡类型
    $scope.bankcardTypes = [];
    // 初始化数据
    $timeout(function () {
        httpSvc.getData('post', '/payOrder/init').then(function (value) {
            $scope.organizations = value.organizations;
            $scope.channels = value.channels;
            $scope.payTypes = value.payType;
            $scope.orderStatus = value.orderStatus;
            $scope.settleStatus = value.settleStatus;
            $scope.merchants = value.merchants;
            $scope.identityTypes = value.identityTypes;
            $scope.bankcardTypes = value.bankcardTypes;
            $scope.transPaymentTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/payOrder/findPayOrderPage', {
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
        $scope.transPaymentTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/payOrder/findPayOrderPage', {
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
    // 弹框(1:查看付款人信息，2：查看产品信息)
    $scope.showTransPayment = function (type,transOrder) {
        if(type == 1){
            var modalInstance = $uibModal.open({
                templateUrl: 'views/trans/card_holder_Info',
                controller: 'cardHolderInfoCtrl',
                size: 'lg',
                resolve: {
                    type: function () {
                        return type;
                    },
                    transOrder: function () {
                        return transOrder;
                    },
                    identityTypes: function () {
                        return $scope.identityTypes;
                    },
                    bankcardTypes: function () {
                        return $scope.bankcardTypes;
                    }
                }
            });
        }
        if(type == 2){
            var modalInstance = $uibModal.open({
                templateUrl: 'views/trans/pay_product_detial',
                controller: 'payProductDetialCtrl',
                size: 'lg',
                resolve: {
                    type: function () {
                        return type;
                    },
                    transOrder: function () {
                        return transOrder;
                    },
                    identityTypes: function () {
                        return $scope.identityTypes;
                    },
                    bankcardTypes: function () {
                        return $scope.bankcardTypes;
                    }
                }
            });
        }
    }
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
    $scope.searchOrder = function () {
        searchInfo = angular.copy($scope.searchInfo);
        if(searchInfo.siteId!==undefined){
            searchInfo.siteId=getSiteIdByUrl(searchInfo.siteId,$scope.siteUrls);
        }
        getTransStatistics(searchInfo);
        $scope.transTable.page(1);
        $scope.transTable.reload();
    };


}



function transBankInfoCtrl($scope, $uibModal, $uibModalInstance,toaster, NgTableParams, httpSvc,transOrder,$filter,csvExp) {
    httpSvc.getData('post', '/transOrder/init').then(function (value1) {
        // $scope.bankcardType=value1.bankcardType;
        $scope.identityType=value1.identityType;

        httpSvc.getData('post', '/transOrder/getTransBankInfo', transOrder.transId).then(function (value) {
            if(value.code == 1 ){
                $scope.TransBankInfo=value.data;
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

}

// 商品详情
function payProductDetialCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,type,transOrder,$uibModalInstance,$timeout) {
    $scope.productDetail = {};
    //初始付款人详情
    httpSvc.getData('post', '/payOrder/getProductInfo', transOrder.payId).then(function (value) {
        if(value.code == 1 ){
            $scope.productDetail=value.data;
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    //返回
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}

// 持卡人详情
function cardHolderInfoCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,type,transOrder,$uibModalInstance,identityTypes,bankcardTypes) {
    $scope.identityTypes = identityTypes;
    $scope.bankcardTypes = bankcardTypes;
    $scope.cardHolderInfo = {};
    //初始付款人详情
    httpSvc.getData('post', '/payOrder/getCardHolderInfo', transOrder.payId).then(function (value) {
        if(value.code == 1 ){
            $scope.cardHolderInfo=value.data;
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    //返回
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}


// 代付交易查询
function repayCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 显示数据
    $scope.organizations={};
    $scope.payType={};
    $scope.orderStatus={};
    $scope.settleStatus={};
    $scope.channels={};
    $scope.merchants={};
    $scope.agentMerchants={};
    $timeout(function () {
        httpSvc.getData('post', '/transOrder/init').then(function (value1) {
            $scope.organizations=value1.organizations;
            $scope.payType=value1.payType;
            $scope.orderStatus=value1.orderStatus;
            $scope.settleStatus=value1.settleStatus;
            $scope.channels=value1.channels;
            $scope.merchants=value1.merchants;
            $scope.agentMerchants=value1.agentMerchants;
            $scope.payTypes = value1.payType

        $scope.repayTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/transOrder/findPayOrderPage', {
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
        });});
    });

    // 弹框(1:查看付款人信息，2：查看产品信息)
    $scope.showModel = function (type,transOrder) {
        if(type == 1){
            var modalInstance = $uibModal.open({
                templateUrl: 'views/trans/transBankInfo',
                controller: 'transBankInfoCtrl',
                size: 'lg',
                resolve: {
                    type: function () {
                        return type;
                    },
                    transOrder: function () {
                        return transOrder;
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
        if(type == 2){
            var modalInstance = $uibModal.open({
                templateUrl: 'views/trans/transProductInfo',
                controller: 'transProductCtrl',
                size: 'lg',
                resolve: {
                    type: function () {
                        return type;
                    },
                    transOrder: function () {
                        return transOrder;
                    }
                }
            });
            modalInstance.result.then(function () {
            }, function () {
            });
        }
    }

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
        $scope.repayTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/transOrder/findPayOrderPage', {
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
    $scope.searchOrder = function () {
        searchInfo = angular.copy($scope.searchInfo);
        if(searchInfo.siteId!==undefined){
            searchInfo.siteId=getSiteIdByUrl(searchInfo.siteId,$scope.siteUrls);
        }
        getTransStatistics(searchInfo);
        $scope.transTable.page(1);
        $scope.transTable.reload();
    };



}

function withdrawalCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {

}
function transBankInfoCtrl($scope, $uibModal, $uibModalInstance,toaster, NgTableParams, httpSvc,transOrder,$filter,csvExp) {
    httpSvc.getData('post', '/transOrder/init').then(function (value1) {
        $scope.bankcardType=value1.bankcardType;
        $scope.identityType=value1.identityType;

    httpSvc.getData('post', '/transOrder/getTransBankInfo', transOrder.transId).then(function (value) {
        if(value.code == 1 ){
            $scope.TransBankInfo=value.data;
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    });
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

}
function transProductCtrl($scope, $uibModal, $uibModalInstance,toaster, NgTableParams, httpSvc,transOrder,$filter,csvExp) {
    httpSvc.getData('post', '/transOrder/init').then(function (value1) {
        $scope.bankcardType=value1.bankcardType;
        $scope.identityType=value1.identityType;

    httpSvc.getData('post', '/transOrder/getProductInfo', transOrder.transId).then(function (value) {
        if(value.code == 1 ){
            $scope.PayProductDetail=value.data;
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    });
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

}

angular
    .module('inspinia')
    .controller('transPaymentCtrl', transPaymentCtrl)
    .controller('cardHolderInfoCtrl',cardHolderInfoCtrl)
    .controller('payProductDetialCtrl',payProductDetialCtrl)
    .controller('transBankInfoCtrl',transBankInfoCtrl)
    .controller('transProductCtrl',transProductCtrl)
    .controller('repayCtrl', repayCtrl)
    .controller('withdrawalCtrl', withdrawalCtrl)
;
