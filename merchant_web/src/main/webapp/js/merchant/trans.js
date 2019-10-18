
// 支付交易查询
function transPaymentCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 全部机构
    $scope.organizations = [];
    // 通道名称
    $scope.channels = [];
    //商户订单号
    $scope.merOrderId = [];
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

    $scope.remark = [];

    $scope.agents = [];

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
            $scope.remark = value.remark;
            $scope.agents = value.agents;
            $scope.transPaymentTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/payOrder/findPayOrderPage', {
                        pageNum: params.page()-1,
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        $scope.totalOrder = value.total;
                        $scope.customize = value.customize;
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
    //退款
    $scope.change = function(){
        var searchInfo = angular.copy($scope.searchInfo);
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
                    $scope.totalOrder = value.total;
                    $scope.customize = value.customize;
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
    };

    $scope.showModal = function (order) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/wallet/pay_change_modal',
            controller: 'transChangeModalCtrl',
            resolve: {
                order: function () {
                    return order;
                }
            }
        });
        modalInstance.result.then(function () {
        }, function () {
        });
    };

    $scope.$watch('searchInfo.startDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.startDate;
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

// 交易变更模态框
function transChangeModalCtrl($scope,  $uibModalInstance,$uibModal, toaster, NgTableParams, httpSvc,$timeout,order) {
    console.log(order);
    $scope.Amountinit = 0;
    $scope.remarkinit = 0;
    $scope.searchInfo = {};
    $scope.order = order;
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    var myalert = function () {
        var title = '退款提醒';
        var content =
            '<div class="text-center">' +
            '<strong>可用余额不足</strong>' +
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

    };
    var myalert1 = function () {
        var title1 = '退款提醒';
        var content1 =
            '<div class="text-center">' +
            '<strong>请输入正确金额</strong>' +
            '</div>';
        var modalInstance1 = $uibModal.open({
            templateUrl: '/views/common/alert_modal',
            controller: 'alertModalCtrl',
            resolve: {
                title: function () {
                    return title1;
                },
                content: function () {
                    return content1;
                }
            }
        });

    };

    $scope.confirm = function () {
        if (order.changeAmount>order.realAmount) {
            myalert();
            $uibModalInstance.close(myalert);
            return;
        }

        if (order.changeAmount == 0) {
            myalert1();
            $uibModalInstance.close(myalert1);
            return;
        } else {
            if (order.changeAmount == null) {
                myalert1();
                $uibModalInstance.close(myalert1);
                return;
            }
        }

        httpSvc.getData('post', '/payOrderChange/orderChange', $scope.order).then(function (value) {
            console.log("value:" + value);
            if (value) {
                toaster.pop({
                    type: 'true',
                    title: '订单变更',
                    body: '订单变更成功！'
                });
                $uibModalInstance.close(value);
            } else {
                toaster.pop({
                    type: 'false',
                    title: '订单变更',
                    body: '订单变更失败,此订单已操作！'
                });
                $uibModalInstance.close(value);
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        $scope.confirmDisabled = function (myForm) {
            return !myForm.$valid;
        };
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    }
}

    function transBankInfoCtrl($scope, $uibModal, $uibModalInstance, toaster, NgTableParams, httpSvc, transOrder, $filter, csvExp) {
        httpSvc.getData('post', '/transOrder/init').then(function (value1) {
            // $scope.bankcardType=value1.bankcardType;
            $scope.identityType = value1.identityType;

            httpSvc.getData('post', '/transOrder/getTransBankInfo', transOrder.transId).then(function (value) {
                if (value.code == 1) {
                    $scope.TransBankInfo = value.data;
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
    function payProductDetialCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $filter, type, transOrder, $uibModalInstance, $timeout) {
        $scope.productDetail = {};
        //初始付款人详情
        httpSvc.getData('post', '/payOrder/getProductInfo', transOrder.payId).then(function (value) {
            if (value.code == 1) {
                $scope.productDetail = value.data;
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        //返回
        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
    }

// 持卡人详情
    function cardHolderInfoCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $filter, type, transOrder, $uibModalInstance, identityTypes, bankcardTypes) {
        $scope.identityTypes = identityTypes;
        $scope.bankcardTypes = bankcardTypes;
        $scope.cardHolderInfo = {};
        //初始付款人详情
        httpSvc.getData('post', '/payOrder/getCardHolderInfo', transOrder.payId).then(function (value) {
            if (value.code == 1) {
                $scope.cardHolderInfo = value.data;
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        //返回
        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
    }


// 代付交易查询
    function repayCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $timeout) {
        $scope.searchInfo = {};
        // 显示数据
        $scope.organizations = {};
        $scope.merOrderId = {};
        $scope.payType = {};
        $scope.orderStatus = {};
        $scope.settleStatus = {};
        $scope.channels = {};
        $scope.merchants = {};
        $scope.agentMerchants = {};
        $scope.agentMerchants = {};
        $timeout(function () {
            httpSvc.getData('post', '/transOrder/init').then(function (value1) {
                $scope.organizations = value1.organizations;
                $scope.payType = value1.payType;
                $scope.orderStatus = value1.orderStatus;
                $scope.settleStatus = value1.settleStatus;
                $scope.channels = value1.channels;
                $scope.merchants = value1.merchants;
                $scope.agentMerchants = value1.agentMerchants;
                $scope.payTypes = value1.payType;
                $scope.agentMerchants = value1.agentMerchants;
                $scope.repayTable = new NgTableParams({}, {
                    getData: function (params) {
                        angular.element('.ibox-content').addClass('sk-loading');
                        return httpSvc.getData('post', '/transOrder/findPayOrderPage', {
                            pageNum: params.page() - 1,
                            pageSize: params.count(),
                            orderBy: params.sorting(),
                            searchInfo: $scope.searchInfo
                        }).then(function (value) {
                            $scope.totalOrdert = value.total;
                            $scope.customizet = value.customize;
                            params.total(value.total);
                            angular.element('.ibox-content').removeClass('sk-loading');
                            return value.rows;
                        });
                    }
                });
            });
        });

        // 弹框(1:查看付款人信息，2：查看产品信息)
        $scope.showModel = function (type, transOrder) {
            if (type == 1) {
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
            if (type == 2) {
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
            $scope.repayTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/transOrder/findPayOrderPage', {
                        pageNum: params.page() - 1,
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        $scope.totalOrdert = value.total;
                        $scope.customizet = value.customize;
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
            if (searchInfo.siteId !== undefined) {
                searchInfo.siteId = getSiteIdByUrl(searchInfo.siteId, $scope.siteUrls);
            }
            getTransStatistics(searchInfo);
            $scope.transTable.page(1);
            $scope.transTable.reload();
        };
    }

// 代付交易查询
    function withdrawalCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $filter, csvExp) {

    }

    function transBankInfoCtrl($scope, $uibModal, $uibModalInstance, toaster, NgTableParams, httpSvc, transOrder, $filter, csvExp) {
        httpSvc.getData('post', '/transOrder/init').then(function (value1) {
            $scope.bankcardType = value1.bankcardType;
            $scope.identityType = value1.identityType;

            httpSvc.getData('post', '/transOrder/getTransBankInfo', transOrder.transId).then(function (value) {
                if (value.code == 1) {
                    $scope.TransBankInfo = value.data;
                }
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        });
        //取消
        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };

    }

    function transProductCtrl($scope, $uibModal, $uibModalInstance, toaster, NgTableParams, httpSvc, transOrder, $filter, csvExp) {
        httpSvc.getData('post', '/transOrder/init').then(function (value1) {
            $scope.bankcardType = value1.bankcardType;
            $scope.identityType = value1.identityType;

            httpSvc.getData('post', '/transOrder/getProductInfo', transOrder.transId).then(function (value) {
                if (value.code == 1) {
                    $scope.PayProductDetail = value.data;
                }
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        });
        //取消
        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };

    }

//批量代付
function batchRepayCtrl($scope, $uibModal, $filter, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    // 显示数据
    $scope.merId = {};
    $scope.merOrderId = {};
    $scope.orderId = {};


    $timeout(function () {
        httpSvc.getData('post', '/transOrder/getBatchRepayInit').then(function (value1) {

            $scope.batchRepayTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/transOrder/getBatchRepayOrderList', {
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
        $scope.batchRepayTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/transOrder/getBatchRepayOrderList', {
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
        if (searchInfo.siteId !== undefined) {
            searchInfo.siteId = getSiteIdByUrl(searchInfo.siteId, $scope.siteUrls);
        }
        getTransStatistics(searchInfo);
        $scope.transTable.page(1);
        $scope.transTable.reload();
    };

    $scope.showModal = function (order) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/batch_repay/batch_load_modal',
            controller: 'transUploadModalCtrl',
            resolve: {
                order: function () {
                    return order;
                }
            }
        });
        modalInstance.result.then(function () {
        }, function () {
        });
    };


}
//批量代付model
function transUploadModalCtrl($scope, httpSvc, toaster, csvExp, NgTableParams,FileUploader,$timeout,$uibModalInstance) {
    console.log("进入批量代付model");
    $scope.changeTableShow = false;
    $scope.changeList = [];
    $scope.typeSelect = [{id: '', title: ''},
        {id: '1', title: '冻结'},
        {id: '2', title: '全额退款'},
        {id: '3', title: '部分退款'},
        {id: '4', title: '全额拒付'},
        {id: '5', title: '部分拒付'}];
    $scope.uploader = new FileUploader({
        url: '/transOrder/batchOperation'
    });

    $scope.uploader.filters.push({
        name: 'fileFilter',
        fn: function (item) {
            if (item.type === 'application/vnd.ms-excel') {
                return myFilter(item.name);
            }
            if (item.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
                return myFilter(item.name);
            }
            function myFilter(name) {
                var fileName = name.split('.');
                switch (fileName[0]) {
                    case '批量代付':
                        return true;
                    default:
                        break;
                }
                toaster.pop({
                    type: 'error',
                    title: '订单批量处理',
                    body: '文件名不符合格式要求！'
                });
                return false;
            }
        }
    });
    console.log($scope.uploader.onErrorItem );
    $scope.uploader.onErrorItem = function (item, response, status, headers) {
        toaster.pop({
            type: 'error',
            title: '批量操作',
            body: '文件：' + item.file.name + ' 上传失败！'
        });
    };
    $scope.uploader.onCompleteItem = function (item, response, status) {
        console.log(response);
        if (response === 0) {
            toaster.pop({
                type: 'success',
                title: '批量操作',
                body: '文件：' + item.file.name + ' 上传成功！'
            });
            $scope.changeList = $scope.changeList.concat(response);
        };
        if (response === 1) {
            toaster.pop({
                type: 'error',
                title: '批量操作',
                body: '文件：' + item.file.name + ' 上传失败！'
            });
            $scope.changeList = $scope.changeList.concat(response);
        };
        if (response === 2) {
            toaster.pop({
                type: 'error',
                title: '批量操作',
                body: '文件：' + item.file.name + ' 存在的重复商户订单号！'
            });
            $scope.changeList = $scope.changeList.concat(response);
        };
        if (response === 3) {
            toaster.pop({
                type: 'error',
                title: '批量操作',
                body:  ' 已存在重复的商户订单号！'
            });
            $scope.changeList = $scope.changeList.concat(response);
        };
      /*  if (status === 200) {
            toaster.pop({
                type: 'success',
                title: '批量操作',
                body: '文件：' + item.file.name + ' 上传成功！'
            });
            $scope.changeList = $scope.changeList.concat(response);
        }*/
    };
    $scope.uploader.onCompleteAll = function () {
        if ($scope.changeList.length > 0) {
            $scope.changeTable = new NgTableParams({}, {
                dataset: $scope.changeList
            });
            $scope.changeTableShow = true;
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
    $scope.example = function (type) {
        var csvData = {
            'data': [{'merOrderId': '190715182260', 'amount': 100.00, 'bankCode': 'ABC', 'inAcctNo': '6227001935600377518', 'inAcctName': '林大头', 'remark': '备注信息', 'returnUrl': 'https://www.baidu.com', 'noticeUrl': 'https://www.baidu.com'}],
            'fileName': '批量代付',
            'columns': {
                'title': ['商户订单号', '金额', '银行编码', '收款卡号', '收款户名', '备注', '返回地址', '通知地址'],
                'key': ['merOrderId', 'amount', 'bankCode', 'inAcctNo', 'inAcctName', 'remark', 'returnUrl', 'noticeUrl']
            }
        };
        if (csvExp.export(csvData)) {
            toaster.pop({
                type: 'success',
                title: '批量操作',
                body: '示例导出成功！'
            });
        } else {
            toaster.pop({
                type: 'warning',
                title: '批量操作',
                body: '示例导出失败！'
            });
        }
    }
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
    .controller('transChangeModalCtrl', transChangeModalCtrl)
    .controller('batchRepayCtrl',batchRepayCtrl)
    .controller('transUploadModalCtrl',transUploadModalCtrl)
;
