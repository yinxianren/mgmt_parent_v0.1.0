function merchantsDetailsCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    $scope.merchantsDetailsTable = null;
    $timeout(function () {
        httpSvc.getData('post', '/merchantWallet/init').then(function (value) {
            $scope.productTypes = value.productTypes;
            $scope.merchants = value.merchants;
            $scope.detailsTypes = value.detailsTypes;
            $scope.merchantsDetailsTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/merchantWallet/findMerchantsDetails', {
                        pageNum: params.page(),
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        params.total(value.data.total);
                        angular.element('.ibox-content').removeClass('sk-loading');
                        return value.data.records;
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
        $scope.merchantsDetailsTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/merchantWallet/findMerchantsDetails', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    searchInfo: $scope.searchInfo
                }).then(function (value) {
                    params.total(value.data.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.data.records;
                });
            }
        });
    }
}

angular
    .module('inspinia')
    .controller('merchantsDetailsCtrl', merchantsDetailsCtrl)
;
