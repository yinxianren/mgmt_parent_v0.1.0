
// 支付订单同步
function paymentOrderSyncCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {


}

// 支付订单列表
function paymentOrderListCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {


}
// 下放订单同步
function lowerOrderSyncCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {


}
// 支付订单列表
function lowerOrderListCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {


}

angular
    .module('inspinia')
    .controller('paymentOrderSyncCtrl', paymentOrderSyncCtrl)
    .controller('paymentOrderListCtrl', paymentOrderListCtrl)
    .controller('lowerOrderSyncCtrl', lowerOrderSyncCtrl)
    .controller('lowerOrderListCtrl', lowerOrderListCtrl)
;
