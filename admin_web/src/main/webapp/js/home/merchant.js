
// 商户列表
function merchantListCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$state) {
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
            modalInstance.result.then(function () {
            }, function () {
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
            modalInstance.result.then(function () {

            }, function () {
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
            modalInstance.result.then(function () {
            }, function () {
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
            modalInstance.result.then(function () {

            }, function () {
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
        if(goType == 8){ // 商户银行费率
            var modalInstance = $uibModal.open({
                templateUrl: 'views/merchant/merchant_bank_rate',
                controller: 'merchantBankRateCtrl',
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
            modalInstance.result.then(function () {
            }, function () {
            });
        }
    };

    // 初始化数据
    httpSvc.getData('post', '/merchantInfo/getAllMerchantInfo',{}).then(function (value) {
        if(value.code == 0) { // 成功
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
        httpSvc.getData('post', '/merchantInfo/getAllMerchantInfo',searchInfo).then(function (value) {
            if(value.code == 0) { // 成功
                $scope.merchantInfoTable.settings({
                    dataset: value.data
                });
                $scope.merchantList = value.data;
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }
    // 查询
    $scope.search = function(){
        $scope.agentMerchantList = [];
        var searchInfo = angular.copy($scope.searchInfo);
        httpSvc.getData('post', '/merchantInfo/getMerchantInfoListByMerchantInfo',searchInfo).then(function (value) {
            if(value.code == 0){ // 成功
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
        $scope.merchantList = (data);
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
                if (value.code == 0) {
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

function merchantAddCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, $stateParams,FileUploader,$timeout,$state) {
    // 商户类型（0：A类:1：B类）
    $scope.merchantType = $stateParams.merchantType;
    $scope.merchantInfo = {};
    $scope.agentMerchants = [];
    $scope.goType = parseInt($stateParams.goType);
    // 初始化证件类型
    httpSvc.getData('post', '/merchantInfo/init').then(function (value) {
        $scope.merchantType = value.merchantType;
        $scope.agentIdentityTypeList = value.identityType;
        $scope.agentMerchants = value.agentMerchants;
        // 全部代理商
        // for (var i = 0 ;i<value.agentMerchants.length;i++){
        //     var item = {};
        //     item['id'] = parseInt(value.agentMerchants[i].agentMerchantId);
        //     item['name'] = value.agentMerchants[i].agentMerchantName;
        //     $scope.agentMerchants.push(item);
        // }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    var certificateImgUrl = [];
    // 新增
    if($scope.goType == 0){
        $scope.merchantInfo = getMerchantInfo();
        /*httpSvc.getData('get', '/merchantInfo/getMerchantIdIncre').then(function (value) {
            $scope.merchantInfo.merchantId = value;
            angular.element('.ibox-content').removeClass('sk-loading');
        });*/
        $scope.certificateImgUrl = [];
    }else{

        $scope.merchantInfo = angular.copy($stateParams.merchantInfo);


        if ($scope.merchantInfo.identityPath != null && $scope.merchantInfo.identityPath.trim() !== '') {
            $scope.certificateImgUrl = $scope.merchantInfo.identityPath.split(',');
            $scope.certificateImgUrl = angular.copy($scope.certificateImgUrl);
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
            merchantId: '',
            merchantName: '',
            merchantShortName: '',
            type: '',
            agentMerchantId: '',
            secretKey: '',
            identityType: '',
            identityNum: '',
            identityUrl: '',
            phone: '',
            phoneStatus: '',
            email: '',
            emailStatus: '',
            qq: '',
            agreementStartTime: '',
            agreementEndTime: '',
            status: 0 //商户状态(新增的时候默认给启用)，2：未审核 0：启用 1:禁用
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
    $scope.$watch('merchantInfo.agreementStartTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = newVal;
        }
    });
    $scope.$watch('merchantInfo.agreementEndTime', function (newVal) {
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
        item.formData.push({agentMerchantId: $scope.merchantInfo.merchantId});
    };
    $scope.uploader.onAfterAddingAll = function () {
        $scope.uploader.uploadAll();
    };
    $scope.uploader.onSuccessItem = function (item, response, status, headers) {
        $scope.certificateImgUrl.push(response);
        $scope.merchantInfo.identityPath = $scope.certificateImgUrl.toString();
    };
    $scope.uploader.onCompleteAll = function () {
        $scope.uploader.clearQueue();
    };


    // 审核确定
    $scope.confirmStatus = function (merchantInfo) {
        merchantInfo.identityPath = $scope.certificateImgUrl.toString();
        httpSvc.getData('post', '/merchantInfo/updateMerchantInfo', merchantInfo).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '商户',
                    body: '审核商户成功！'
                });
                $state.go('base_view.merchant.merchant_list');
            } else {
                toaster.pop({
                    type: 'error',
                    title: '商户',
                    body: '审核商户失败！'
                });
            }
        });
    }

    /**
     * 确定
     */
    $scope.confirm = function (merchantInfo) {
        merchantInfo.identityPath = $scope.certificateImgUrl.toString();
        if ($scope.goType === 0) {// type:0 新增
            httpSvc.getData('post', '/merchantInfo/addMerchantInfo', merchantInfo).then(function (value) {
                if (value.code == 0) {
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
                if (value.code == 0) {
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
        =  $scope.qqBlur  = $scope.identityUrlBlur =  $scope.agreementStartTimeBlur = $scope.agreementEndTimeBlur = $scope.secretKeyBlur  =  function ($event, name) {
        verification(name,$event.target);
    };
    // 表单校验
    $timeout(function () {
        $scope.checkDisabled = function (myForm) {
            return myForm.merchantId.$error.required || myForm.merchantName.$error.required || myForm.type.$error.required
                ||myForm.identityType.$error.required || myForm.phone.$error.required || myForm.email.$error.required
                || myForm.qq.$error.required || myForm.identityNum.$error.required || myForm.agreementStartTime.$error.required
                || myForm.agreementEndTime.$error.required || myForm.secretKey.$error.required;
                // || myForm.agreementStarttime.$error.required || myForm.agreementEndtime.$error.required;
        };
    });
    //
    $scope.getRandomSecretkey = function () {
        httpSvc.getData('post', '/merchantInfo/getRandomSecretkey').then(function (value) {
            if(value.code == 0){
                $scope.merchantInfo.secretKey = value.data;
            }
        });
    }
}

// 结算账户
function merchantAccountCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, merchantInfo,FileUploader,$timeout,$uibModalInstance,identityTypeList) {
    $scope.goType = parseInt(goType);
    $scope.certificateImgUrl = []; // 图片集合
    $scope.agentIdentityTypeList = identityTypeList;
    $scope.merchantAccount = {};
    $scope.merchantAccount = getMerchantAccount(merchantInfo);
    // var params = {"merId":$scope.merchantAccount.merId};
    httpSvc.getData('post', '/merchantAcount/search',{"merId":$scope.merchantAccount.merId}).then(function (value) {
        if(value.code == 0 ){
            if (value.data != null) {
                $scope.merchantAccount = value.data;
            }
            if ($scope.merchantAccount.identityUrl1 != null && $scope.merchantAccount.identityUrl1.trim() !== '') {
                $scope.certificateImgUrl = $scope.merchantAccount.identityUrl1.split(',');
                // certificateImgUrl = angular.copy(certificateImgUrl);
            }
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    function getMerchantAccount(merchantInfo) {
        var merchantAccount = {
            merId: merchantInfo.merchantId,
            benefitName: '',
            bankName: '',
            bankcardNum: '',
            bankcardType: '',
            bankBranchName: '',
            bankBranchNum: '',
            identityType: '',
            identityNum: '',
            identityUrl1: '',
            identityUrl2: ''
        };
        return merchantAccount;
    }
    /**
     * 确定
     */
    $scope.confirm = function (merchantAccount) {
        merchantAccount.identityPath = $scope.certificateImgUrl.toString();
        httpSvc.getData('post', '/merchantAcount/update', merchantAccount).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '结算账户',
                    body: '修改结算账户成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '结算账户',
                    body: '修改结算账户失败！'
                });
            }
        });
    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    $scope.merIdBlur = $scope.benefitNameBlur =  $scope.bankNameBlur  = $scope.bankcardNumBlur = $scope.bankBranchNameBlur = $scope.bankBranchNumBlur =
        $scope.identityNumBlur = $scope.identityTypeBlur = $scope.identityUrl1Blur =
        function ($event, name) {
            verification(name,$event.target);
        };
    $timeout(function () {
        $scope.checkDisabled = function (myForm) {
            return myForm.merId.$error.required || myForm.benefitName.$error.required || myForm.bankName.$error.required
                || myForm.bankcardNum.$error.required || myForm.bankBranchName.$error.required || myForm.bankBranchNum.$error.required
                ||  myForm.identityType.$error.required || myForm.identityNum.$error.required || myForm.identityUrl1.$error.required;
        };
    });

    // 文件上传
    $scope.uploader = new FileUploader({
        url: '/agentMerchantInfo/uploadAgentIdentityUrl'
    });
    $scope.uploader.onBeforeUploadItem = function (item) {
        item.formData.push({agentMerchantId: $scope.merchantAccount.merchantId});
    };
    $scope.uploader.onAfterAddingAll = function () {
        $scope.uploader.uploadAll();
    };
    $scope.uploader.onSuccessItem = function (item, response, status, headers) {
        $scope.certificateImgUrl.push(response);
        $scope.merchantAccount.identityUrl1 = $scope.certificateImgUrl.toString();
    };
    $scope.uploader.onCompleteAll = function () {
        $scope.uploader.clearQueue();
    };
}

// 商户配置
function merchantSettingCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, merchantInfo,FileUploader,$timeout,$uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.merchantSetting = {};
    $scope.merchantSetting = getMerchantSetting(merchantInfo);
    $scope.merchantId = merchantInfo.merchantId;
    function getMerchantSetting(merchantInfo) {
        var merchantSetting = {
            id: '',
            merchantId: merchantInfo.merchantId,
            organizationId: '',
            channelId:'',
            channelLevel: '',
            payType: ''
        };
        return merchantSetting;
    }
    var payids=[];
    var channleids=[];
    var orgpayids=[];
    // CheckBox选择框
    $scope.exists = function (value, selected) {
        if (value === null || selected === null || value === undefined || selected === undefined) {
            return false;
        }
        var checkab = selected.indexOf(value) > -1;
        return checkab;
    };

    $scope.patytoggle = function (value) {
        var index = payids.indexOf(value);
        if (index > -1) {
            payids.splice(index, 1);
        } else {
            payids.push(value);
        }
    };
    var channelAll= [];
    httpSvc.getData('post', '/merchantSetting/init').then(function (value) {
       $scope.merchantType=value.merchantType;
       $scope.channelLevel=value.channelLevel;
       $scope.agentMerchants=value.agentMerchants;
       $scope.payType=value.payType;
       var organizations = value.organizations;
       $scope.organizationAlls= [];
       for (var i = 0; i <  organizations.length; i++) {
           var item = {};
           item['organizationId'] = organizations[i].organizationId;
           item['organizationName'] = organizations[i].organizationName;
           $scope.organizationAlls.push(item) ;
       }
       var channelAlls=value.channels;
        $scope.channelAll = value.channels;
        for (var i = 0; i <  channelAlls.length; i++) {
            var item = {};
            item['organizationId'] = channelAlls[i].organizationId;
            item['channelName'] = channelAlls[i].channelName;
            item['channelId'] = channelAlls[i].channelId;
            item['productId'] = channelAlls[i].productId;
            channelAll[i] = (item) ;
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    // 初始化对象信息
    httpSvc.getData('post', '/merchantSetting/search',{"merchantId":$scope.merchantSetting.merchantId}).then(function (value1) {
        if(value1.code == 0 ){
            $scope.merchantSetting = value1.data;
            var merchantSettings = value1.data;
            // 初始化属性 机构
            if ($scope.merchantSetting === null) {
                $scope.merchantOrganizations = [];
                $scope.channels = [];
            } else {
                var channels ="";
                var merchantOrganizations="";
                for (var i = 0; i <  merchantSettings.length; i++) {
                    merchantOrganizations += merchantSettings[i].organizationId+",";
                    channels += merchantSettings[i].channelId+",";
                }
                $scope.merchantOrganizations = merchantOrganizations.split(',');
                $scope.channels = channels.split(',');
                // if($scope.merchantSetting.organizationId.indexOf(",")){
                // $scope.merchantOrganizations= $scope.merchantSetting.organizationId.split(',');
                // }else {
                //     $scope.merchantOrganizations[0]=$scope.merchantSetting.organizationId;
                // }
            }

          /*  // 初始化属性 通道
            if ($scope.merchantSetting.channelId === undefined) {
                $scope.merchantChannels = [];
            } else {
                $scope.merchantChannels = $scope.merchantSetting.channelId.split(',');
            }

            // 初始化属性 支付方式
            if ($scope.merchantSetting.payType === undefined) {
                $scope.merchantPayTypes = [];
            } else {
                $scope.merchantPayTypes = $scope.merchantSetting.payType.split(',');
            }*/

              // payids=angular.copy($scope.merchantPayTypes);
             channleids=angular.copy($scope.channels)
             orgpayids=angular.copy($scope.merchantOrganizations);
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    $scope.channletoggle = function (value) {
        var index = channleids.indexOf(value);
        if (index > -1) {
            channleids.splice(index, 1);
        } else {
            channleids.push(value);
        }
    };
    $scope.orgtoggle = function (value) {
        var index = orgpayids.indexOf(value);
        if (index > -1) {
            orgpayids.splice(index, 1);
        } else {
            orgpayids.push(value);
        }
        /*$scope.channels = [];
        for (var i = 0; i <  channelAll.length; i++) {
            var index = orgpayids.indexOf(channelAll[i].organizationId);
            if (index > -1) {
                var item = {};
                item['channelName'] = channelAll[i].channelName;
                item['channelId'] = channelAll[i].channelId;
                $scope.channels.push(item) ;
            }
        }*/
        // httpSvc.getData('get', '/merchantSetting/getChannels',{
        //     organizationIds:orgpayids
        // }).then(function (value) {
        //     $scope.channels = value.data;
        // });
    };

    /**
     * 确定
     */

    $scope.confirm = function (merchantInfo) {

        var channels=channleids.join(',');
        $scope.channelIds = [];
        for (var i = 0; i <  channelAll.length; i++) {
            var index = channels.indexOf(channelAll[i].channelId);
            if (index > -1) {
                var item = {};
                item = channelAll[i];
                $scope.channelIds.push(item) ;
            }
        }
        /*$scope.merchantInfo.payType=types;
        $scope.merchantInfo.channelId=$scope.channelIds;
        $scope.merchantInfo.organizationId=organizations;
        $scope.merchantInfo.mercahntId=$scope.merchantId;*/

        httpSvc.getData('post', '/merchantSetting/update', {
            channelId:$scope.channelIds,
            merchantId:$scope.merchantId
        }).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '商户配置',
                    body: '修改商户配置成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '商户配置',
                    body: '修改商户配置失败！'
                });
            }
        });

    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    $scope.merIdBlur = $scope.benefitNameBlur =  $scope.bankNameBlur  = $scope.bankcardNumBlur = $scope.bankBranchNameBlur = $scope.bankBranchNumBlur =
    $scope.identityTypeBlur =  function ($event, name) {
        verification(name,$event.target);
    };
    $timeout(function () {
        $scope.checkDisabled = function (myForm) {
            return myForm.merchantId.$error.required || myForm.benefitName.$error.required || myForm.bankName.$error.required
                || myForm.bankcardNum.$error.required || myForm.bankBranchName.$error.required || myForm.bankBranchNum.$error.required ||  myForm.identityType.$error.required;
        };
    });
}

//风控配置
function merchantQuotaRiskCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, merchantInfo,FileUploader,$timeout,$uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.merchantSetting = {};
    $scope.merchantSetting = getMerchantSetting(merchantInfo);
    function getMerchantSetting(merchantInfo) {
        var merchantSetting = {
            id: '',
            merchantId: merchantInfo.merchantId,
            organizationId: '',
            channelId: '',
            channelLevel: '',
            payType: ''
        };
        return merchantSetting;
    }
    // 初始化对象信息
    httpSvc.getData('post', '/merchantQuotaRisk/search',{"merchantId":$scope.merchantSetting.merchantId}).then(function (value1) {

        if(value1.code == 0 ){
            if (value1.data == null){
                $scope.merchantQuotaRisk = {};
                $scope.merchantQuotaRisk.merchantId = merchantInfo.merchantId;
            } else {
                $scope.merchantQuotaRisk = value1.data;
            }

        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });

    /**
     * 确定
     */
    $scope.confirm = function (merchantInfo) {
        httpSvc.getData('post', '/merchantQuotaRisk/update', merchantInfo).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '商户配置',
                    body: '修改商户配置成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '商户配置',
                    body: '修改商户配置失败！'
                });
            }
        });

    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    $scope.merIdBlur = $scope.benefitNameBlur =  $scope.bankNameBlur  = $scope.bankcardNumBlur = $scope.bankBranchNameBlur = $scope.bankBranchNumBlur =
    $scope.identityTypeBlur =  function ($event, name) {
        verification(name,$event.target);
    };
    $timeout(function () {
        $scope.checkDisabled = function (myForm) {
            return myForm.merchantId.$error.required || myForm.benefitName.$error.required || myForm.bankName.$error.required
                || myForm.bankcardNum.$error.required || myForm.bankBranchName.$error.required || myForm.bankBranchNum.$error.required ||  myForm.identityType.$error.required;
        };
    });
}
//商户费率
function merchantSquareRateCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, merchantInfo,FileUploader,$timeout,$uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.merchantSetting = {};
    $scope.selected=[];
    $scope.merchantRates = [];

    $scope.merchantSetting = getMerchantSetting(merchantInfo);
    function getMerchantSetting(merchantInfo) {
        var merchantSetting = {
            id: '',
            merchantId: merchantInfo.merchantId,
            organizationId: '',
            channelId: '',
            channelLevel: '',
            payType: ''
        };
        return merchantSetting;
    }

    httpSvc.getData('get', '/merchantSquareRate/init').then(function (value) {
        value = value.data;
        $scope.status = value.status;
        $scope.productTypes = value.productTypes;
        angular.element('.ibox-content').removeClass('sk-loading');

    // 初始化对象信息
    httpSvc.getData('post', '/merchantSquareRate/search',{"merchantId":$scope.merchantSetting.merchantId}).then(function (value1) {

            $scope.merchantRates = value1.data;
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    });

    statusChange=function(status){

        var temp=status;
        if (temp==1){
            return status =0;
        } else {
            return status =1;
        }

    }
    /**
     * 确定
     */
    $scope.confirm = function (merchantRates) {
        var codes = [];

        for (var code in $scope.selected) {
            if ($scope.selected[code]) {
                codes.push(code);
            }
        }
        httpSvc.getData('post', '/merchantSquareRate/update',
            merchantRates
        ).then(function (value) {

            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '商户配置',
                    body: '修改商户配置成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '商户配置',
                    body: '修改商户配置失败！'
                });
            }
        });

    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    $scope.singleFeeBlur =$scope.rateFeeBlur= $scope.marginRatioBlur =$scope.settleCycleBlur =$scope.marginCycleBlur =  function ($event, name) {
        verification(name,$event.target);
    };
    $timeout(function () {
        $scope.checkRateDisabled = function (myForm,merchantRates) {
            if(merchantRates.length == 0){
                return;
            }
            // for (var i=0;i<merchantRates.length;i++){
            if(merchantRates[0].status == 0 && merchantRates[1].status == 0 && merchantRates[2].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                    || myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[0].status == 0 && merchantRates[1].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                    || myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required;
            }
            if(merchantRates[0].status == 0 && merchantRates[2].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[1].status == 0 && merchantRates[2].status == 0) {// 启用
                return myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[0].status == 0){// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required
                    ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required;
            }
            if(merchantRates[1].status == 0){// 启用
                return myForm.singleFee1.$error.required ||myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required
                    ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required;
            }
            if(merchantRates[2].status == 0){// 启用
                return myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required
                    ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            // }
            return null;
        };
    });
}
// 商户用户管理
function merchantUserMgmtCtrl($scope, $state, $stateParams, $uibModal, toaster, NgTableParams, httpSvc) {
    if ($stateParams.merchantInfo !== null) {
        var id=$stateParams.merchantInfo.merchantId;
        $scope.delDisabled = true;
        $scope.selected = {};
        $scope.statusChange = function (row) {
            httpSvc.getData('post', '/merchant/updateMerchantUser', {
                id: row.id,
                status: row.status == 0 ? 1:0
            }).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '商户用户管理',
                        body: '更新用户状态成功！'
                    });
                    tableReload();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '商户用户管理',
                        body: '更新用户状态失败！'
                    });
                }
            })
        };
        httpSvc.getData('post', '/merchant/getMerchantUserByMerchantId',{"belongto":id}).then(function (value) {
            $scope.merchantUserTable = new NgTableParams({}, {
                dataset: value.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        httpSvc.getData('post', '/merchant/getMerchantRoleByMerchantId', {"belongto":id}).then(function (value) {
            $scope.merchantRole = value.data;
        });
        $scope.showModal = function (user, type) {
            var modalInstance = $uibModal.open({
                templateUrl: '/views/merchant/merchant_user_modal',
                controller: 'merchantUserModalCtrl',
                resolve: {
                    userInfo: function () {
                        return user;
                    },
                    // 0新增；1修改信息，2修改密码
                    type: function () {
                        return type;
                    },
                    merchantId: function () {
                        return id;
                    },
                    merchantRole: function () {
                        return $scope.merchantRole;
                    }
                }
            });
            modalInstance.result.then(function () {
                tableReload();
            }, function () {
            });
        };
        $scope.del = function () {
            var idList = [];
            for (var id in $scope.selected) {
                if ($scope.selected[id]) {
                    idList.push(id);
                }
            }
            httpSvc.getData('post', '/merchant/deleteMerchantUser', idList).then(function (value) {
                if (value) {
                    $scope.selected = {};
                    toaster.pop({
                        type: 'success',
                        title: '用户管理',
                        body: '删除用户成功！'
                    });
                    tableReload();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '用户管理',
                        body: '删除用户失败！'
                    });
                }
            });
        };
        $scope.delDisabled = function () {
            for (var id in $scope.selected) {
                if ($scope.selected[id]) {
                    return false;
                }
            }
            return true;
        };

        function tableReload() {
            angular.element('.ibox-content').addClass('sk-loading');
            httpSvc.getData('post', '/merchant/getMerchantUserByMerchantId', {"belongto":id}).then(function (value) {
                $scope.merchantUserTable.settings({
                    dataset: value.data
                });
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        }
    } else {
        $state.go('base_view.merchant.merchant_list');
    }
}


// 商户用户管理弹窗
function merchantUserModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, userInfo, merchantId, merchantRole) {
    if (type !== 0) {
        $scope.userInfo = angular.copy(userInfo);
    }
    $scope.merchantRole = merchantRole;
    $scope.nameValid = false;
    $scope.passwordValid = false;
    $scope.type = type;
    $scope.userNameBlur = function ($event, userName) {
        if (!verification(userName, $event.target)) {
            httpSvc.getData('post', '/merchant/isMerchantUserExist', {
                userName: userName,
                belongto: merchantId
            }).then(function (value) {
                if (value.code == 0) {
                    verification(null, $event.target);
                } else {
                    $scope.nameValid = true;
                }
            });
        } else {
            $scope.nameValid = false;
        }
    };
    $scope.passwordBlur = function ($event, password) {
        if (password === undefined || password.length < 6) {
            verification(undefined, $event.target);
        } else {
            verification(password, $event.target);
        }
    };
    $scope.passwordBBlur = function ($event, passwordB, password) {
        if (passwordB === undefined || passwordB !== password) {
            $scope.passwordValid = !verification(undefined, $event.target);
        } else {
            $scope.passwordValid = !verification(passwordB, $event.target);
        }
    };
    $scope.phoneBlur = $scope.emailBlur = function ($event, value) {
        verification(value, $event.target)
    };
    $scope.confirmDisabled = function (myForm) {
        switch (type) {
            case 0:
                return !myForm.$valid || !$scope.nameValid || !$scope.passwordValid;
            case 1:
                return !myForm.userName.$valid || !myForm.mobile.$valid || !myForm.email.$valid;
            case 2:
                return !$scope.passwordValid;
        }
    };
    $scope.confirm = function () {
        if (type === 0) {
            $scope.userInfo.belongto = merchantId;
            httpSvc.getData('post', '/merchant/addMerchantUser', $scope.userInfo).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '商户用户管理',
                        body: '新增用户成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '商户用户管理',
                        body: '新增用户失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/merchant/updateMerchantUser', $scope.userInfo).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '商户用户管理',
                        body: '更新用户成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '商户用户管理',
                        body: '更新用户失败！'
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    }
}

//商户银行费率
function merchantBankRateCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, merchantInfo,FileUploader,$timeout,$uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.merchantSetting = {};
    $scope.selected=[];
    $scope.merchantRates = [];

    $scope.merchantSetting = getMerchantSetting(merchantInfo);
    function getMerchantSetting(merchantInfo) {
        var merchantSetting = {
            id: '',
            merchantId: merchantInfo.merchantId,
            organizationId: '',
            channelId: '',
            channelLevel: '',
            payType: ''
        };
        return merchantSetting;
    }

    httpSvc.getData('get', '/merchantBankRate/init').then(function (value) {
        value = value.data;
        $scope.status = value.status;
        angular.element('.ibox-content').removeClass('sk-loading');

        // 初始化对象信息
        httpSvc.getData('get', '/merchantBankRate/search',{"merchantId":$scope.merchantSetting.merchantId}).then(function (value1) {

            $scope.merchantRates = value1.data;
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });

    statusChange=function(status){

        var temp=status;
        if (temp==1){
            return status =0;
        } else {
            return status =1;
        }

    }
    /**
     * 确定
     */
    $scope.confirm = function (merchantRates) {
        var codes = [];

        for (var code in $scope.selected) {
            if ($scope.selected[code]) {
                codes.push(code);
            }
        }
        httpSvc.getData('post', '/merchantBankRate/update',
            merchantRates
        ).then(function (value) {

            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '商户银行配置',
                    body: '修改商户银行配置成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '商户银行配置',
                    body: '修改商户银行配置失败！'
                });
            }
        });

    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    /*$scope.singleFeeBlur =$scope.rateFeeBlur= $scope.marginRatioBlur =$scope.settleCycleBlur =$scope.marginCycleBlur =  function ($event, name) {
        verification(name,$event.target);
    };*/
    $timeout(function () {
        $scope.checkRateDisabled = function (myForm,merchantRates) {
            if(merchantRates.length == 0){
                return;
            }
            // for (var i=0;i<merchantRates.length;i++){
           /* if(merchantRates[0].status == 0 && merchantRates[1].status == 0 && merchantRates[2].status == 0) {// 启用
                return myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                     || myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required
                     ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[0].status == 0 && merchantRates[1].status == 0) {// 启用
                return myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                     || myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required;
            }
            if(merchantRates[0].status == 0 && merchantRates[2].status == 0) {// 启用
                return myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required
                    ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[1].status == 0 && merchantRates[2].status == 0) {// 启用
                return  myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required
                    ||myForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }
            if(merchantRates[0].status == 0){// 启用
                return myForm.rateFee0.$error.required ||myForm.marginRatio0.$error.required
                    ||myForm.settleCycle0.$error.required ||myForm.marginCycle0.$error.required;
            }
            if(merchantRates[1].status == 0){// 启用
                return myForm.rateFee1.$error.required ||myForm.marginRatio1.$error.required
                    ||myForm.settleCycle1.$error.required ||myForm.marginCycle1.$error.required;
            }
            if(merchantRates[2].status == 0){// 启用
                returnmyForm.rateFee2.$error.required ||myForm.marginRatio2.$error.required
                    ||myForm.settleCycle2.$error.required ||myForm.marginCycle2.$error.required;
            }*/
            // }
            return null;
        };
    });
}


angular
    .module('inspinia')
    .controller('merchantListCtrl', merchantListCtrl)
    // .controller('addMerchantCtrl', addMerchantCtrl)
    .controller('merchantAddCtrl', merchantAddCtrl)
    .controller('merchantAccountCtrl',merchantAccountCtrl)
    .controller('merchantQuotaRiskCtrl',merchantQuotaRiskCtrl)
    .controller('merchantSquareRateCtrl',merchantSquareRateCtrl)
    .controller('merchantUserMgmtCtrl',merchantUserMgmtCtrl)
    .controller('merchantUserModalCtrl',merchantUserModalCtrl)
    .controller('merchantSettingCtrl',merchantSettingCtrl)
    .controller('merchantBankRateCtrl',merchantBankRateCtrl)
;



function verification(value, htmlId) {
    if (value === undefined || value === null || value === '') {
        angular.element(htmlId).parent().removeClass('has-success');
        angular.element(htmlId).parent().addClass('has-error');
        return true;
    } else {
        angular.element(htmlId).parent().removeClass('has-error');
        angular.element(htmlId).parent().addClass('has-success');
        return false;
    }
}
