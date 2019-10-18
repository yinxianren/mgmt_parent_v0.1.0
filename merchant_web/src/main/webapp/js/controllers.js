/**
 * translateCtrl - Controller for translate
 */
// 多语言控制
function translateCtrl($translate, $scope) {
    $scope.changeLanguage = function (langKey) {
        $translate.use(langKey);
        $scope.language = langKey;
    };
}

// 确认弹窗
function alertModalCtrl($scope, $uibModalInstance, title, content) {
    $scope.alertTitle = title;
    $scope.alertContent = content;
    $scope.confirm = function () {
        $uibModalInstance.close();
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}

// 主控
function mainCtrl($scope, $http, $state) {
    $scope.menuRegexp = /_PRIVILEGE/g;
    $http({
        method: 'post',
        url: '/user/getMenuList'
    }).then(function (value) {
        $scope.menuList = value.data;
    });
    $scope.reloadState = function (stateName) {
        if ($state.includes(stateName)) {
            $state.reload(stateName);
        }
    };
}

// 主页控制
function homePageCtrl($scope, httpSvc,$injector,$uibModal) {

}

// 交易查询
function transInfoCtrl($scope, $uibModal, $filter, toaster, NgTableParams, httpSvc, $http) {

}


// 保证金查询
function transBondInfoCtrl($scope, $timeout, $filter, toaster, NgTableParams, httpSvc, $http) {

}

// 交易变更
function transChangeCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {

}

// 交易变更模态框
function transChangeModalCtrl($scope, $uibModalInstance, orderInfo, httpSvc, toaster) {

}

// 退款、拒付查询
function orderChangeInfoCtrl($scope, $timeout, $uibModal, $filter, toaster, NgTableParams, httpSvc, $http,$injector) {

}

// 余额查询
function balanceInquiryCtrl($scope, $state, $uibModal, NgTableParams, httpSvc) {

}



// 日月账单
function billCtrl($scope, $timeout, $filter, httpSvc, toaster) {

}

// 域名管理
function webSiteMgmtCtrl($scope, $uibModal, NgTableParams, toaster, httpSvc, $filter, csvExp,FileUploader) {

}





// 物流管理
function logisticsMgmtCtrl($scope, $uibModal, $filter, NgTableParams, toaster, httpSvc, $http) {

}

// 商户基本信息
function baseInformationCtrl($scope, httpSvc, toaster, FileUploader) {
    var merchantInfo;
    var certificateImgUrl = [];
    httpSvc.getData('post', '/merchant/getBaseInformationInit').then(function (value) {
        merchantInfo = angular.copy(value.merchantInfo);
        if (value.merchantInfo.certificate.trim() !== '') {
            $scope.certificateImgUrl = value.merchantInfo.certificate.split(',');
            certificateImgUrl = angular.copy($scope.certificateImgUrl);
        } else {
            $scope.certificateImgUrl = [];
        }
        $scope.merchantInfo = value.merchantInfo;
        $scope.provinceInfo = value.provinceInfo;
        $scope.merchantCategory = value.merchantCategory;
        $scope.$watch('merchantInfo.province', function (newVal) {
            if (newVal !== undefined) {
                for (var i = 0; i < $scope.provinceInfo.length; i++) {
                    if ($scope.provinceInfo[i].id === newVal) {
                        $scope.cityInfo = $scope.provinceInfo[i].childArea;
                        break;
                    }
                }
            }
        });
        $scope.$watch('merchantInfo.city', function (newVal) {
            if (newVal !== undefined && $scope.cityInfo !== undefined) {
                for (var i = 0; i < $scope.cityInfo.length; i++) {
                    if ($scope.cityInfo[i].id === newVal) {
                        $scope.areaInfo = $scope.cityInfo[i].childArea;
                        break;
                    }
                }
            }
        });
        $scope.provinceChange = function () {
            $scope.merchantInfo.city = null;
            $scope.merchantInfo.area = null;
        };
        $scope.cityChange = function () {
            $scope.merchantInfo.area = null;
        };
    });
    $scope.uploader = new FileUploader({
        url: '/merchant/uploadMerchantCertificate'
    });
    /*$scope.uploader.onAfterAddingFile = function () {
        $scope.uploader.uploadAll();
    };*/
    $scope.uploader.onAfterAddingAll = function () {
        $scope.uploader.uploadAll();
    };
    $scope.uploader.onSuccessItem = function (item, response, status, headers) {
        $scope.certificateImgUrl.push(response);
    };
    $scope.uploader.onCompleteAll = function () {
        $scope.uploader.clearQueue();
    };
    $scope.confirmDisabled = function (myForm) {
        return !myForm.$valid || (angular.equals(merchantInfo, $scope.merchantInfo) && angular.equals(certificateImgUrl, $scope.certificateImgUrl));
    };
    $scope.confirm = function () {
        $scope.merchantInfo.certificate = $scope.certificateImgUrl.toString();
        httpSvc.getData('post', '/merchant/updateMerchantInfo', $scope.merchantInfo).then(function (value) {
            if (value) {
                toaster.pop({
                    type: 'success',
                    title: '更新商户信息',
                    body: '更新商户信息成功！'
                });
                merchantInfo = angular.copy($scope.merchantInfo);
            } else {
                toaster.pop({
                    type: 'warning',
                    title: '更新商户信息',
                    body: '更新商户信息失败！'
                });
            }
        });
    };
}

// 配置信息
function configurationInformation($scope, httpSvc) {
    httpSvc.getData('post', '/merchant/getConfigurationInformation').then(function (value) {
        $scope.merchantSetting = value.merchantSetting;
        $scope.trackSetting = value.trackSetting;
        $scope.trackAudit = value.trackAudit;
        $scope.correctDistribute = value.correctDistribute;
        $scope.currency = value.currency;
    });
}

// 支付信息
function paymentInformation($scope, httpSvc) {
    $scope.payModeTable = {};
    $scope.payRowspan = 0;
    httpSvc.getData('post', '/merchant/getPaymentInformation').then(function (value) {
        $scope.encrypt = value.encrypt;
        $scope.status = value.status;
        $scope.merchantPay = value.merchantPay;
        $scope.payMode = value.payMode;
        $scope.bailCycle = value.merchantRate[0].bailCycle;
        $scope.bailRate = value.merchantRate[0].bailRate;
        var payMode = value.merchantPay.payMode.replace(/[^a-zA-z,]/g, '').split(',');
        // $scope.payModeTable
        for (var i = 0; i < payMode.length; i++) {
            $scope.payModeTable[payMode[i]] = [];
            for (var j = 0; j < value.merchantRate.length; j++) {
                if (value.merchantRate[j].payMode === payMode[i])
                    $scope.payModeTable[payMode[i]].push(value.merchantRate[j]);
            }
            $scope.payRowspan += $scope.payModeTable[payMode[i]].length;
        }
    });
}


// 意见与建议
function opinionCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
    $scope.searchInfo = {};
    $scope.$watch('searchInfo.startDate', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.startDate;
            $scope.endDateDisable = false;
        } else {
            $scope.endDateDisable = true;
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
    httpSvc.getData('post', '/system/getOpinionInit').then(function (value) {
        $scope.questionType = value.questionType;
        $scope.questionStatus = value.questionStatus;
        $scope.opinionTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/system/getOpinion', {
                    pageNum: params.page(),
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
    $scope.search = function () {
        $scope.opinionTable.page(1);
        $scope.opinionTable.reload();
    };
    $scope.resetForm = function () {
        $scope.searchInfo = {};
        $scope.opinionTable.page(1);
        $scope.opinionTable.reload();
    };
    $scope.showModal = function (type, opinionInfo) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/system/opinion_modal',
            controller: 'opinionModalCtrl',
            resolve: {
                opinionInfo: function () {
                    return opinionInfo;
                },
                questionType: function () {
                    return $scope.questionType;
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
    };

    function tableReload() {
        $scope.opinionTable.page(1);
        $scope.opinionTable.reload();
    }
}

// 意见与建议模态框
function opinionModalCtrl($scope, $uibModalInstance, toaster, httpSvc, opinionInfo, questionType, type, taOptions) {
    $scope.opinionInfo = angular.copy(opinionInfo);
    $scope.questionType = questionType;
    $scope.type = type;
    taOptions.toolbar = [
        ['h1', 'h2', 'p', 'quote', 'bold', 'italics', 'underline'],
        ['undo', 'redo', 'clear'],
        ['ul', 'ol', 'justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent', 'insertLink']
    ];
    $scope.textBlur = function (content) {
        $scope.opinionInfo.content = content;
    };
    $scope.confirmDisabled = function (myForm) {
        return !myForm.$valid;
    };
    $scope.confirm = function () {
        if (type === 0) {
            httpSvc.getData('post', '/system/addOpinion', $scope.opinionInfo).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '建议与意见',
                        body: '新增成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '建议与意见',
                        body: '新增失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/system/updateOpinion', $scope.opinionInfo).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '建议与意见',
                        body: '编辑成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '建议与意见',
                        body: '编辑失败！'
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}
// 提现查询
function withdrawDepositInquiryCtrl($scope, NgTableParams, httpSvc) {

}

// 交易模态框
function transModalCtrl($scope, NgTableParams, httpSvc) {

}


angular
    .module('inspinia')
    .controller('mainCtrl', mainCtrl)
    .controller('translateCtrl', translateCtrl)
    .controller('alertModalCtrl', alertModalCtrl)
    .controller('homePageCtrl', homePageCtrl)
    .controller('transInfoCtrl', transInfoCtrl)
    .controller('transModalCtrl', transModalCtrl)
    .controller('transBondInfoCtrl', transBondInfoCtrl)
    .controller('transChangeCtrl', transChangeCtrl)
    .controller('transChangeModalCtrl', transChangeModalCtrl)
    .controller('orderChangeInfoCtrl', orderChangeInfoCtrl)
    .controller('withdrawDepositInquiryCtrl', withdrawDepositInquiryCtrl)
    .controller('billCtrl', billCtrl)
    .controller('baseInformationCtrl', baseInformationCtrl)
    .controller('configurationInformation', configurationInformation)
    .controller('paymentInformation', paymentInformation)
    .controller('opinionCtrl', opinionCtrl)
    .controller('opinionModalCtrl', opinionModalCtrl)
    .controller('webSiteMgmtCtrl', webSiteMgmtCtrl)
    .controller('logisticsMgmtCtrl', logisticsMgmtCtrl)
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
function  getSiteIdByUrl(url,siteUrls) {
    if(url.hasOwnProperty("id")){
        return url.id;
    }
        // if(url.hasOwnProperty("id")){
    //     return url.id;
    // }
    else {
    for(var item in siteUrls){
        if(siteUrls[item].siteUrl==url){
           return siteUrls[item].id;
        }
    }
    }
}

function inputRegex(input, regex, replacement) {
    if (replacement === undefined || replacement === null)
        replacement = '';
    input.value = input.value.replace(regex, replacement);
}