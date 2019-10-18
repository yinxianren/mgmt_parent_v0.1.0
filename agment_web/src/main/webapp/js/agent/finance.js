// 商户提现查询
function financeDrawingCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.searchInfo = {};
    $scope. merchantWallet=null;
    httpSvc.getData('post', '/financeDrawing/idsInit').then(function (value) {
        $scope.customerId = value.customerId;
        $scope.id = value.id;
        $scope.agents = value.agents;
    });


    httpSvc.getData('post', '/financeDrawing/search',$scope.searchInfo
    ).then(function (value) {
        $scope.financeDrawingTable = new NgTableParams({}, {
            dataset: value
        });
        angular.element('.ibox-content').removeClass('sk-loading');
    });

    $scope.search = function () {
        $scope.financeDrawingTable=null;
        httpSvc.getData('post', '/financeDrawing/search',$scope.searchInfo
        ).then(function (value) {
            $scope.financeDrawingTable = new NgTableParams({}, {
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
        httpSvc.getData('post', '/financeDrawing/search',$scope.searchInfo
        ).then(function (value) {
            $scope.financeDrawingTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }



}
angular
    .module('inspinia')
    .controller('financeDrawingCtrl', financeDrawingCtrl)

;
