
// 商户钱包查询
function merchantWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.searchInfo = {};
    $scope. merchantWallet=null;
    httpSvc.getData('post', '/merchantWallet/idsInit').then(function (value) {
        $scope.agents = value.agents;
    });


    httpSvc.getData('post', '/merchantWallet/search',$scope.searchInfo
    ).then(function (value) {
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
    };

    tablereload=function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/merchantWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.merchantWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };
    $scope.showModal = function (order) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/wallet/wallet_change_modal',
            controller: 'walletChangeModalCtrl',
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
// 交易变更模态框
function walletChangeModalCtrl($scope,  $uibModalInstance,$uibModal, toaster, NgTableParams, httpSvc,$timeout,order) {
    $scope.Amountinit=0;
    $scope.searchInfo = {};
    $scope.order = order;
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    var myalert = function () {
        var title = '余额提现提醒';
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
        var title1 = '余额提现提醒';
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
        if (!order.totalAvailableAmount){
            myalert();
            $uibModalInstance.close(myalert);
            return;
        }else {
            if(order.totalAvailableAmount<order.withdrawalAmount){
                myalert();
                $uibModalInstance.close(myalert);
                return;
            }
        }

        if (order.withdrawalAmount==0){
            myalert1();
            $uibModalInstance.close(myalert1);
            return;
        }else {
            if(order.withdrawalAmount==null){
                myalert1();
                $uibModalInstance.close(myalert1);
                return;
            }
        }

        httpSvc.getData('post', '/merchantWallet/financeChange', $scope.order).then(function (value) {
            console.log("value:"+value);
            console.log($scope.amount);
            if (value) {
                toaster.pop({
                    type: 'true',
                    title: '余额提现',
                    body: '提现成功！'
                });
                $uibModalInstance.close(value);
                tablereload();
            } else {
                toaster.pop({
                    type: 'false',
                    title: '余额提现',
                    body: '提现失败！'
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



//子商户钱包查询
function terminalwalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.selected = {};
    $scope.searchInfo = {};
    $scope.terminalwalletTable = null;
    $scope.merchants = [];
    httpSvc.getData('post', '/terminalMerchantsWallet/idsInit').then(function (value) {
        $scope.paytype = value.paytype;
        $scope.organizations = value.organizations;
        $scope.merchants = value.merchants;
        $scope.terminalMerIds = value.terminalMerIds;
        httpSvc.getData('post', '/terminalMerchantsWallet/search', $scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    });

    $scope.search = function () {
        httpSvc.getData('post', '/terminalMerchantsWallet/search', $scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    tablereload = function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/terminalMerchantsWallet/search', $scope.searchInfo
        ).then(function (value) {
            $scope.terminalwalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    };

    $scope.resetForm = function () {
        tablereload();
    };

}



//代理商钱包查询
function agentWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.searchInfo = {};



    httpSvc.getData('post', '/agentWallet/idsInit').then(function (value) {
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

    $scope.resetForm = function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/agentWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.AgentWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    }

}
//通道钱包查询
function channelWalletCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    $scope.searchInfo = {};
    $scope. ChannelWallet=null;
    httpSvc.getData('post', '/channelWallet/idsInit').then(function (value) {
        $scope.paytype = value.paytype;
        $scope.channels = value.channels;
        $scope.organizations = value.organizations;
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    });

    $scope.search = function () {
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    $scope.resetForm = function () {
        $scope.searchInfo = {};
        httpSvc.getData('post', '/channelWallet/search',$scope.searchInfo
        ).then(function (value) {
            $scope.ChannelWalletTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }


}


function agentMerchantAddCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, $stateParams,FileUploader,$timeout,$state) {
    // 商户类型（0：A类:1：B类）
    $scope.merchantType = $stateParams.merchantType;
    $scope.merchantInfo = {};
    $scope.agentMerchants = [];
    $scope.goType = parseInt($stateParams.goType);
    // 初始化证件类型
    httpSvc.getData('post', '/merchantInfo/init').then(function (value) {
        $scope.merchantType = value.merchantType;
        $scope.agentIdentityTypeList = value.identityType;
        // 全部代理商
        $scope.agentMerchants = value.agentMerchants;
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    var certificateImgUrl = [];
    // 新增
    if($scope.goType == 0){
        $scope.merchantInfo = getMerchantInfo();

        httpSvc.getData('get', '/merchantInfo/getMerchantIdIncre').then(function (value) {
            $scope.merchantInfo.merId = value;
            httpSvc.getData('get', '/merchantInfo/parentId').then(function (value1) {
                $scope.merchantInfo.parentId=value1;
            });
        });
        $scope.certificateImgUrl = [];
    }else{

        $scope.merchantInfo = angular.copy($stateParams.merchantInfo);


        if ($scope.merchantInfo.identityUrl != null && $scope.merchantInfo.identityUrl.trim() !== '') {
            $scope.certificateImgUrl = $scope.merchantInfo.identityUrl.split(',');
            certificateImgUrl = angular.copy(certificateImgUrl);
        } else {
            $scope.certificateImgUrl = [];
        }
        // 审核状态默认给通过0：通过，1:未通过,2:未审核
        if($scope.goType == 2){
            if($scope.merchantInfo.status == 0){
                $scope.agentStatusCheckqy = true;
                $scope.agentStatusCheckjy = false;
                $scope.agentStatusCheckwsh = false;
            }
            if($scope.merchantInfo.status == 1){
                $scope.agentStatusCheckqy = false;
                $scope.agentStatusCheckjy = true;
                $scope.agentStatusCheckwsh = false;
            }
            if($scope.merchantInfo.status == 2){
                $scope.agentStatusCheckqy = false;
                $scope.agentStatusCheckjy = false;
                $scope.agentStatusCheckwsh = true;
            }
        }
    }
    function getMerchantInfo() {
        var merchantInfo = {
            merId: '',
            merchantName: '',
            merchantShortName: '',
            type: '',
            parentId: '',
            secretKey: '',
            identityType: '',
            identityNum: '',
            identityUrl: '',
            phone: '',
            phoneStatus: '',
            email: '',
            emailStatus: '',
            qq: '',
            agreementStarttime: '',
            agreementEndtime: '',
            status: 2 //商户状态(新增的时候默认给未审核)，2：未审核
        };
        return merchantInfo;
    }

    $scope.endDateDisable = true;
    $scope.format = 'yyyy/MM/dd';
    $scope.dateOptions1 = {
        formatYear: 'yyyy',
        startingDay: 1,
        minDate: new Date()
    };
    $scope.dateOptions2 = {
        formatYear: 'yyyy',
        startingDay: 1,
        minDate: new Date()
    };
    $scope.$watch('merchantInfo.agreementStarttime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = newVal;
        }
    });
    $scope.$watch('merchantInfo.agreementEndtime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = newVal;
        } else {
            $scope.dateOptions1.maxDate = null;
        }
    });
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

    // 文件上传
    $scope.uploader = new FileUploader({
        url: '/agentMerchantInfo/uploadAgentIdentityUrl'
    });
    $scope.uploader.onBeforeUploadItem = function (item) {
        item.formData.push({agentMerchantId: $scope.merchantInfo.merId});
    };
    $scope.uploader.onAfterAddingAll = function () {
        $scope.uploader.uploadAll();
    };
    $scope.uploader.onSuccessItem = function (item, response, status, headers) {
        $scope.certificateImgUrl.push(response);
        $scope.merchantInfo.identityUrl = $scope.certificateImgUrl.toString();
    };
    $scope.uploader.onCompleteAll = function () {
        $scope.uploader.clearQueue();
    };


    /**
     * 确定
     */
    $scope.confirm = function (merchantInfo) {
        merchantInfo.identityUrl = $scope.certificateImgUrl.toString();
        console.log(merchantInfo);
        if ($scope.goType === 0) {// type:0 新增
            httpSvc.getData('post', '/merchantInfo/addMerchantInfo', merchantInfo).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '商户',
                        body: '添加商户成功！'
                    });
                    $state.go('base_view.merchant.merchant_list');
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '商户',
                        body: '添加商户失败！'
                    });
                }
            });
        }
        if($scope.goType === 1){ //type:1 修改 typpe:2 审核
            httpSvc.getData('post', '/merchantInfo/updateMerchantInfo', merchantInfo).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '代理商',
                        body: '更新商户成功！'
                    });
                    $state.go('base_view.merchant.merchant_list');
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '代理商',
                        body: '更新商户失败！'
                    });
                }
            });
        }
    }
    //取消
    $scope.cancel = function () {
        $state.go('base_view.merchant.merchant_list');
    };
    // 验证输入框的输入数据
    $scope.merIdBlur = $scope.merchantNameBlur = $scope.typeBlur =  $scope.identityTypeBlur  = $scope.phoneBlur = $scope.emailBlur = $scope.identityNumBlur
        =  $scope.qqBlur  = $scope.identityUrlBlur =  $scope.agreementStarttimeBlur = $scope.agreementEndtimeBlur = $scope.secretKeyBlur  =  function ($event, name) {
        verification(name,$event.target);
    };
    // 表单校验
    $timeout(function () {
        $scope.checkDisabled = function (myForm) {
            return myForm.merId.$error.required || myForm.merchantName.$error.required || myForm.type.$error.required
                ||myForm.identityType.$error.required || myForm.phone.$error.required || myForm.email.$error.required
                || myForm.qq.$error.required || myForm.identityNum.$error.required || myForm.identityUrl.$error.required || myForm.agreementStarttime.$error.required
                || myForm.agreementEndtime.$error.required || myForm.secretKey.$error.required;
            // || myForm.agreementStarttime.$error.required || myForm.agreementEndtime.$error.required;
        };
    });
    //
    $scope.getRandomSecretkey = function () {
        httpSvc.getData('post', '/merchantInfo/getRandomSecretkey').then(function (value) {
            if(value.code == 1){
                console.log(111111111111111111);
                $scope.merchantInfo.secretKey = value.msg;
            }
        });
    }
}

// 商户列表
function agentMerchantListCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$state) {
    $scope.init = {};
    $scope.selected={};
    $scope.searchInfo = {};
    $scope.merchantList = [];
    // 弹框 goType(0:新增，1,：修改，2：审核，3：结算, 4: 商户配置,5:商户费率,6:商户风控,7用户)
    $scope.showModal = $scope.edit = function (goType, merchantInfo) {
        if(goType == 0){ //1：编辑  // 跳转商户详情页面
            $state.go('base_view.merchant.merchant_add', {
                merchantType: null,
                goType: 0,
                merchantInfo: null
            });
        }
        if(goType == 1){ //1：编辑  // 跳转商户详情页面
            $state.go('base_view.merchant.merchant_add', {
                merchantType: $scope.init.merchantType,
                goType: goType,
                merchantInfo: merchantInfo
            });
        }
        if(goType == 2){ // 审核
            $state.go('base_view.merchant.merchant_add', {
                merchantType: $scope.init.merchantType,
                goType: goType,
                merchantInfo: merchantInfo
            });
        }
        if(goType == 3){ // 结算
            var modalInstance = $uibModal.open({
                templateUrl: 'views/merchant/merchant_account',
                controller: 'merchantAccountCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    merchantInfo: function () {
                        return merchantInfo;
                    },
                    identityTypeList: function(){
                        return $scope.identityTypeList;
                    },
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
            });
        }
        if(goType == 4){ // 商户配置
            var modalInstance = $uibModal.open({
                templateUrl: 'views/merchant/merchant_setting',
                controller: 'merchantSettingCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    merchantInfo: function () {
                        return merchantInfo;
                    },
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
            });
        }
        if(goType == 5){ // 商户费率
            var modalInstance = $uibModal.open({
                templateUrl: 'views/merchant/merchant_rate',
                controller: 'merchantSquareRateCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    merchantInfo: function () {
                        return merchantInfo;
                    },
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
            });
        }
        if(goType == 6){ // 结算
            var modalInstance = $uibModal.open({
                templateUrl: 'views/merchant/merchantQuotaRisk',
                controller: 'merchantQuotaRiskCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    merchantInfo: function () {
                        return merchantInfo;
                    },
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
            });
        }
        if(goType == 7){ // 用户
            console.log(merchantInfo);
            $state.go('base_view.merchant.merchant_user', {
                merchantType: $scope.init.merchantType,
                goType: goType,
                merchantInfo: merchantInfo
            });
        }
    };

    // 初始化数据
    httpSvc.getData('post', '/merchantInfo/getMerchants',{}).then(function (value) {
        if(value.code == 1) { // 成功
            $scope.merchantInfoTable = new NgTableParams({}, {
                dataset: value.data
            });
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    httpSvc.getData('post', '/merchantInfo/init').then(function (value) {
        $scope.init = value;
        $scope.merchantList = value.merchants;
        $scope.identityTypeList = value.identityType;
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    // 重新加载
    function tableReload(searchInfo) {
        $scope.agentMerchantList = [];
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/merchantInfo/getMerchants',searchInfo).then(function (value) {
            if(value.code == 1) { // 成功
                console.log(value.data)
                $scope.merchantInfoTable.settings({
                    dataset: value.data
                });
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }
    // 查询
    $scope.search = function(){
        $scope.agentMerchantList = [];
        var searchInfo = angular.copy($scope.searchInfo);
        httpSvc.getData('post', '/merchantInfo/getMerchantInfoListByMerchantInfo',searchInfo).then(function (value) {
            if(value.code == 1){ // 成功
                $scope.merchantInfoTable.settings({
                    dataset: value.data
                });
                setMerchantInfoList(value.data);
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    // 重构全部的商户号下拉数组
    function setMerchantInfoList(data){
        for (var i =0;i<data.length;i++){
            var merchant = {};
            merchant['id'] = data[i].merId;
            merchant['name'] = data[i].merchantName;
            $scope.merchantList.push(merchant);
        }
    }

    // 重置
    $scope.reset = function(){
        $scope.searchInfo = {};
        var searchInfo = {};
        tableReload(searchInfo);
    };

    // 批量删除
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
            httpSvc.getData('post', '/merchantInfo/batchDel', ids).then(function (value) {
                if (value.code == 1) {
                    toaster.pop({
                        type: 'success',
                        title: '代理商删除',
                        body: '删除成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '代理商删除',
                        body: '删除失败！'
                    });
                }
                $scope.selected={};
                idArr = [];
                tableReload($scope.searchInfo);
            });
        }, function () {
        });
    }
}



// 用户管理
function agentUserMgmtCtrlCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
    httpSvc.getData('post', '/user/getRoleList').then(function (value) {
        $scope.roleList = value;
    });
    httpSvc.getData('post', '/user/getUserList').then(function (value) {
        $scope.userTable = new NgTableParams({}, {
            dataset: value
        });
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    $scope.showModal = function (type, userInfo) {
        var modalInstance;
        if (type !== 3) {
            modalInstance = $uibModal.open({
                templateUrl: '/views/system/mer_user_modal',
                controller: 'merUserMgmtModalCtrl',
                resolve: {
                    userInfo: function () {
                        return userInfo;
                    },
                    roleList: function () {
                        return $scope.roleList;
                    },
                    type: function () {
                        return type;
                    }
                }
            });
            modalInstance.result.then(function () {
                tableReload();
            }, function () {
            });
        } else {
            var title = '删除确认';
            var content =
                '<div class="text-center">' +
                '<strong>确定要删除这1条数据吗？</strong>' +
                '</div>';
            modalInstance = $uibModal.open({
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
                httpSvc.getData('post', '/user/delete', JSON.stringify(userInfo.userName)).then(function (value) {
                    tableReload();
                    if (value) {
                        toaster.pop({
                            type: 'success',
                            title: '用户管理',
                            body: '删除用户成功！'
                        });
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: '用户管理',
                            body: '删除用户失败！'
                        });
                    }
                });
            }, function () {
            });
        }
    };

    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/user/getUserList').then(function (value) {
            $scope.userTable.settings({
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }
}

// 用户管理模态框
function merUserMgmtModalCtrl($scope, $uibModalInstance, toaster, httpSvc, userInfo, roleList, type) {
    $scope.userInfo = angular.copy(userInfo);
    $scope.roleList = roleList;
    $scope.type = type;
    var isExist = true;
    var passwordValid = false;
    var passwordValidB = false;
    $scope.userNameBlur = function ($event, userName) {
        if (!verification(userName, $event.target)) {
            httpSvc.getData('post', '/user/isExist', {userName: userName}).then(function (value) {
                if (value) {
                    verification(null, $event.target);
                    isExist = true;
                } else {
                    verification(userName, $event.target);
                    isExist = false;
                }
            });
        }
    };
    $scope.passwordBlur = function ($event, password) {
        if (password != null && password.length < 6) {
            verification(null, $event.target);
            passwordValid = false;
        } else {
            verification(password, $event.target);
            passwordValid = true;
        }
    };
    $scope.passwordChange = function () {
        passwordValidB = false;
        if ($scope.userInfo.passwordB != null && $scope.userInfo.password !== $scope.userInfo.passwordB) {
            angular.element('#passwordB').parent().removeClass('has-success');
            angular.element('#passwordB').parent().addClass('has-error');
        }
    };
    $scope.passwordBBlur = function ($event, passwordB) {
        if ($scope.userInfo != null && $scope.userInfo.password !== passwordB) {
            verification(null, $event.target);
            passwordValidB = false;
        } else {
            verification(passwordB, $event.target);
            passwordValidB = true;
        }
    };
    $scope.realNameBlur = $scope.mobileBlur = $scope.emailBlur = function ($event, value) {
        verification(value, $event.target);
    };
    switch (type) {
        case 0:
            $scope.confirmDisabled = function (myForm) {
                return !myForm.$valid || isExist || !passwordValid || !passwordValidB;
            };
            $scope.confirm = function () {
                httpSvc.getData('post', '/user/add', $scope.userInfo).then(function (value) {
                    if (value) {
                        toaster.pop({
                            type: 'success',
                            title: '用户管理',
                            body: '新增用户成功！'
                        });
                        $uibModalInstance.close();
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: '用户管理',
                            body: '新增用户失败！'
                        });
                    }
                });
            };
            break;
        case 1:
            $scope.confirmDisabled = function (myForm) {
                return !myForm.realName.$valid || !myForm.mobile.$valid || !myForm.email.$valid ||
                    !myForm.roleId.$valid || angular.equals($scope.userInfo, userInfo);
            };
            $scope.confirm = function () {
                httpSvc.getData('post', '/user/update', $scope.userInfo).then(function (value) {
                    if (value) {
                        toaster.pop({
                            type: 'success',
                            title: '用户管理',
                            body: '编辑用户成功！'
                        });
                        $uibModalInstance.close();
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: '用户管理',
                            body: '编辑用户失败！'
                        });
                    }
                });
            };
            break;
        case 2:
            $scope.confirmDisabled = function () {
                return !passwordValid || !passwordValidB;
            };
            $scope.confirm = function () {
                httpSvc.getData('post', '/user/update', $scope.userInfo).then(function (value) {
                    if (value) {
                        toaster.pop({
                            type: 'success',
                            title: '用户管理',
                            body: '重置密码成功！'
                        });
                        $uibModalInstance.close();
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: '用户管理',
                            body: '重置密码失败！'
                        });
                    }
                });
            };
            break;
        default:
            break;
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}
// 密钥修改
function agentSecretKeyChangeCtrlabc($scope, httpSvc, toaster) {
    console.log(222);
    httpSvc.getData('post', '/merchantInfo/getMerchantInfo').then(function (value) {
        $scope.merchantInfo = value[0];
        console.log(value);
        console.log(  $scope.merchantInfo);
        angular.element('.ibox-content').removeClass('sk-loading');

    });
    console.log(111111111111111111);
    $scope.confirm = function (newKey) {
        var merchantInfo = angular.copy($scope.merchantInfo);
        merchantInfo.secretKey = newKey;
        httpSvc.getData('post', '/merchantInfo/updateMerchantInfo', merchantInfo).then(function (value) {
            if (value) {
                $scope.newSecretKey = null;
                toaster.pop({
                    type: 'success',
                    title: '密钥修改',
                    body: '修改成功！'
                });
                $scope.merchantInfo.secretKey = newKey;
            } else {
                toaster.pop({
                    type: 'error',
                    title: '密钥修改',
                    body: '修改失败！'
                });
            }
        });
    };

    $scope.getNewSecretKey=function () {
        httpSvc.getData('post', '/merchantInfo/getRandomSecretkey').then(function (value) {
            console.log(111111111111111111);
            $scope.newSecretKey = value.msg;
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }


}
function merchantsDetailsCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    $scope.merchantsDetailsTable = null;
    $timeout(function () {
        httpSvc.getData('post', '/merchantsDetails/init').then(function (value) {
            $scope.payTypes = value.payTypes;
            $scope.detailsTypes = value.detailsTypes;
            $scope.merchants = value.merchants;
            $scope.merchantsDetailsTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/merchantsDetails/findMerchantsDetails', {
                        pageNum: params.page()-1,
                        pageSize: params.count(),
                        orderBy: params.sorting(),
                        searchInfo: $scope.searchInfo
                    }).then(function (value) {
                        console.log("value============="+value);
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
        $scope.merchantsDetailsTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/merchantsDetails/findMerchantsDetails', {
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
function terminalMerchantsDetailsCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$timeout) {
    $scope.searchInfo = {};
    $scope.terminalMerchantsDetailsTable = null;
    $timeout(function () {
        httpSvc.getData('post', '/terminalMerchantsDetails/init').then(function (value) {
            $scope.merchants = value.merchants;
            $scope.terminalMerIds = value.terminalMerIds;
            $scope.terminalMerchantsDetailsTable = new NgTableParams({}, {
                getData: function (params) {
                    angular.element('.ibox-content').addClass('sk-loading');
                    return httpSvc.getData('post', '/terminalMerchantsDetails/findTerminalMerchantsDetails', {
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
        $scope.terminalMerchantsDetailsTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/terminalMerchantsDetails/findTerminalMerchantsDetails', {
                    pageNum: params.page()-1,
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    searchInfo: $scope.searchInfo
                }).then(function (value) {
                    console.log("----------------------"+value);
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
    .controller('merchantWalletCtrl', merchantWalletCtrl)
    .controller('agentWalletCtrl', agentWalletCtrl)
    .controller('agentMerchantListCtrl', agentMerchantListCtrl)
    .controller('agentUserMgmtCtrlCtrl', agentUserMgmtCtrlCtrl)
    .controller('agentSecretKeyChangeCtrlabc', agentSecretKeyChangeCtrlabc)
    .controller('merUserMgmtModalCtrl', merUserMgmtModalCtrl)
    .controller('agentMerchantAddCtrl', agentMerchantAddCtrl)
    .controller('channelWalletCtrl', channelWalletCtrl)
    .controller('walletChangeModalCtrl', walletChangeModalCtrl)
    .controller('merchantsDetailsCtrl', merchantsDetailsCtrl)
    .controller('terminalMerchantsDetailsCtrl', terminalMerchantsDetailsCtrl)
    .controller('terminalwalletCtrl', terminalwalletCtrl)
;
