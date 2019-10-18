// 商户订单查询
function payOrderChangeCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    $scope.exceptionId = {};
    $scope.customerId = {};
    $scope.payId = {};
    $scope.type = {};
    // 初始化数据
    $timeout(function () {
        httpSvc.getData('post', '/payOrderChange/init').then(function (value) {
            $scope.exceptionId = value.exceptionId;
            $scope.customerId = value.customerId;
            $scope.payId = value.payId;
            $scope.type = value.type;
            $scope.payOrderChangeTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/payOrderChange/findPayOrderPage', {
                        pageNum: params.page() - 1,
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
    $scope.search = function () {
        var searchInfo = angular.copy($scope.searchInfo);
        tableReload(searchInfo);
    };
    // 重置
    $scope.reset = function () {
        $scope.searchInfo = {};
        var searchInfo = {};
        tableReload(searchInfo);
    };

    // 重新加载
    function tableReload(searchInfo) {
        angular.element('.ibox-content').addClass('sk-loading');
        $scope.payOrderChangeTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/payOrderChange/findPayOrderPage', {
                    pageNum: params.page() - 1,
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
angular
    .module('inspinia')
    .controller('payOrderChangeCtrl', payOrderChangeCtrl)

;