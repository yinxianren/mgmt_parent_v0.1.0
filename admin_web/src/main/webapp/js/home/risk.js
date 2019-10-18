
// 风控
function riskTrackingCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
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
        $scope.riskTrackingTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/systemOrderTrack/findSystemOrder', {
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
        $scope.transPaymentTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/systemOrderTrack/findSystemOrder', {
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
    $scope.showInfo = function (Info) {
        var modalSize = 'lg';

        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/show_jsonParse_modal',
            controller: 'showJsonParseModalCtrl',
            size: modalSize,
            resolve: {
                info: function () {
                    return Info;
                }
            }
        });
        modalInstance.result.then(function () {

        }, function () {

        });
    };
}

angular
    .module('inspinia')
    .controller('riskTrackingCtrl', riskTrackingCtrl)
;
