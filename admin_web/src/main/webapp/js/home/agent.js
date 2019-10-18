// 代理商列表
function agentListCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $state) {
    $scope.selected = {};
    $scope.searchInfo = {};
    $scope.agentMerchantList = [];

    //新增
    $scope.showModal = $scope.edit = function (goType, agentMerchantInfo) {
        if (goType == 3) { // 用户
            console.log(agentMerchantInfo);
            $state.go('base_view.agent.agent_user', {
                // merchantType: $scope.init.merchantType,
                merchantType: null,
                goType: goType,
                merchantInfo: agentMerchantInfo
            });
        }else if (goType == 4){ // 代理商费率
            var modalInstance = $uibModal.open({
                templateUrl: 'views/agent/agent_rate',
                controller: 'agentRateCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    agentMerchantInfo: function () {
                        return agentMerchantInfo;
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
        }else {
            var modalInstance = $uibModal.open({
                templateUrl: 'views/agent/add_agent',
                controller: 'addAgentCtrl',
                resolve: {
                    goType: function () {
                        return goType;
                    },
                    agentMerchantInfo: function () {
                        return agentMerchantInfo;
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
                var searchInfo = angular.copy($scope.searchInfo);
                tableReload(searchInfo);
            }, function () {
            });
        }
    };

    // 重新加载
    function tableReload(searchInfo) {
        $scope.agentMerchantList = [];
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/agentMerchantInfo/getAllByVoAgentMerchantInfo', searchInfo).then(function (data) {
            $scope.AgentMerchantInfoTable.settings({
                dataset: data
            });

            // 重构全部的商户号下拉数组
            for (var i = 0; i < data.length; i++) {
                var agent = {};
                agent['id'] = data[i].agentMerchantId;
                agent['name'] = data[i].agentMerchantName;
                $scope.agentMerchantList.push(agent);
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    // 初始化
    httpSvc.getData('post', '/agentMerchantInfo/getAllByVoAgentMerchantInfo', {}).then(function (data) {
        $scope.AgentMerchantInfoTable = new NgTableParams({}, {
            dataset: data
        });
        // 重构全部的商户号下拉数组
        for (var i = 0; i < data.length; i++) {
            var agent = {};
            agent['id'] = data[i].agentMerchantId;
            agent['name'] = data[i].agentMerchantName;
            $scope.agentMerchantList.push(agent);
        }
        angular.element('.ibox-content').removeClass('sk-loading');
    });

    // 查询
    $scope.search = function () {
        $scope.agentMerchantList = [];
        var searchInfo = angular.copy($scope.searchInfo);
        // tableReload(searchInfo);
        httpSvc.getData('post', '/agentMerchantInfo/getAllByVoAgentMerchantInfo', searchInfo).then(function (data) {
            $scope.AgentMerchantInfoTable.settings({
                dataset: data
            });
            // 重构全部的商户号下拉数组
            for (var i = 0; i < data.length; i++) {
                var agent = {};
                agent['id'] = data[i].agentMerchantId;
                agent['name'] = data[i].agentMerchantName;
                $scope.agentMerchantList.push(agent);
            }
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };

    // 重置
    $scope.reset = function () {
        $scope.searchInfo = {};
        var searchInfo = {};
        tableReload(searchInfo);
    };

    // 批量删除
    $scope.batchDel = function () {
        var ids = []
        var count = 0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + count + '条数据吗？</strong>' +
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
            httpSvc.getData('post', '/agentMerchantInfo/batchDel', ids).then(function (value) {
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
                        body: '删除失败！' + value.msg
                    });
                }
                $scope.selected = {};
                idArr = [];
                tableReload($scope.searchInfo);
            });
        }, function () {
        });
    }

}

//商户费率
function agentRateCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter, goType, agentMerchantInfo,FileUploader,$timeout,$uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.agentMerchantSetting = {};
    $scope.selected=[];
    $scope.agentMerchantSettings = [];

    $scope.agentMerchantSetting = getAgentMerchantSetting(agentMerchantInfo);
    function getAgentMerchantSetting(agentMerchantInfo) {
        var agentMerchantSetting = {
            id: '',
            agentMerchantId: agentMerchantInfo.agentMerchantId,
            organizationId: '',
            channelId: '',
            channelLevel: '',
            payType: ''
        };
        return agentMerchantSetting;
    }

    console.log($scope.agentMerchantSetting.agentMerchantId);
    httpSvc.getData('post', '/merchantInfo/init').then(function (value) {
        $scope.merchantType=value.merchantType;
        $scope.channelLevel=value.channelLevel;
        $scope.agentMerchants=value.agentMerchants;
        $scope.status=value.status;
        $scope.payType=value.payType;
        $scope.channels=value.channels;
        angular.element('.ibox-content').removeClass('sk-loading');

        // 初始化对象信息
        httpSvc.getData('post', '/agentMerchantSetting/search',{"agentMerchantId":$scope.agentMerchantSetting.agentMerchantId}).then(function (value1) {
            $scope.agentMerchantSettings = value1;
            console.log($scope.agentMerchantSettings)
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
    $scope.confirm = function (merchantSquareRates) {
        var codes = [];

        for (var code in $scope.selected) {
            if ($scope.selected[code]) {
                codes.push(code);
            }
        }
        httpSvc.getData('post', '/agentMerchantSetting/update',
            merchantSquareRates
        ).then(function (value) {

            if (value.code == 1) {
                toaster.pop({
                    type: 'success',
                    title: '代理商配置',
                    body: '修改代理商配置成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '代理商配置',
                    body: '修改代理商配置失败！'
                });
            }
        });

    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    // 验证输入框的输入数据
    $scope.singleFeeBlur =$scope.rateFeeBlur= $scope.bondRateBlur =$scope.settlecycleBlur =$scope.bondCycleBlur =  function ($event, name) {
        verification(name,$event.target);
    };
    $timeout(function () {
        $scope.checkRateDisabled = function (myForm,merchantSquareRates) {
            if(merchantSquareRates.length == 0){
                return;
            }
            if(merchantSquareRates[0].status == 0 && merchantSquareRates[1].status == 0 && merchantSquareRates[2].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.bondRate0.$error.required ||myForm.settlecycle0.$error.required ||myForm.bondCycle0.$error.required
                    || myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.bondRate1.$error.required ||myForm.settlecycle1.$error.required ||myForm.bondCycle1.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.bondRate2.$error.required ||myForm.settlecycle2.$error.required ||myForm.bondCycle2.$error.required;
            }
            if(merchantSquareRates[0].status == 0 && merchantSquareRates[1].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.bondRate0.$error.required ||myForm.settlecycle0.$error.required ||myForm.bondCycle0.$error.required
                    || myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.bondRate1.$error.required ||myForm.settlecycle1.$error.required ||myForm.bondCycle1.$error.required;
            }
            if(merchantSquareRates[0].status == 0 && merchantSquareRates[2].status == 0) {// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.bondRate0.$error.required ||myForm.settlecycle0.$error.required ||myForm.bondCycle0.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.bondRate2.$error.required ||myForm.settlecycle2.$error.required ||myForm.bondCycle2.$error.required;
            }
            if(merchantSquareRates[1].status == 0 && merchantSquareRates[2].status == 0) {// 启用
                return myForm.singleFee1.$error.required || myForm.rateFee1.$error.required ||myForm.bondRate1.$error.required ||myForm.settlecycle1.$error.required ||myForm.bondCycle1.$error.required
                    ||myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.bondRate2.$error.required ||myForm.settlecycle2.$error.required ||myForm.bondCycle2.$error.required;
            }
            if(merchantSquareRates[0].status == 0){// 启用
                return myForm.singleFee0.$error.required ||myForm.rateFee0.$error.required ||myForm.bondRate0.$error.required
                    ||myForm.settlecycle0.$error.required ||myForm.bondCycle0.$error.required;
            }
            if(merchantSquareRates[1].status == 0){// 启用
                return myForm.singleFee1.$error.required ||myForm.rateFee1.$error.required ||myForm.bondRate1.$error.required
                    ||myForm.settlecycle1.$error.required ||myForm.bondCycle1.$error.required;
            }
            if(merchantSquareRates[2].status == 0){// 启用
                return myForm.singleFee2.$error.required ||myForm.rateFee2.$error.required ||myForm.bondRate2.$error.required
                    ||myForm.settlecycle2.$error.required ||myForm.bondCycle2.$error.required;
            }
            return null;
        };
    });
}

// 添加代理商 goType（0:新增，1：编辑，2:代理商审核）
function addAgentCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc, $filter, goType, agentMerchantInfo, FileUploader, $timeout, $uibModalInstance) {
    $scope.goType = parseInt(goType);
    $scope.agentMerchantInfo = {};
    var certificateImgUrl = [];
    if ($scope.goType == 0) {
        $scope.agentMerchantInfo = getAgentMerchantInfo();
        httpSvc.getData('get', '/agentMerchantInfo/getAgentMerchantIdIncre').then(function (value) {
            $scope.agentMerchantInfo.agentMerchantId = value;
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        $scope.certificateImgUrl = [];
    } else {
        $scope.agentMerchantInfo = angular.copy(agentMerchantInfo);
        if ($scope.agentMerchantInfo.agentIdentityUrl != null && $scope.agentMerchantInfo.agentIdentityUrl.trim() !== '') {
            $scope.certificateImgUrl = $scope.agentMerchantInfo.agentIdentityUrl.split(',');
            certificateImgUrl = angular.copy(certificateImgUrl);
        } else {
            $scope.certificateImgUrl = [];
        }
        if ($scope.agentMerchantInfo.agentIdentityType == 1) {
            $scope.xykCheck = true;
            $scope.yhkCheck = false;
        }
        if ($scope.agentMerchantInfo.agentIdentityType == 2) {
            $scope.xykCheck = false;
            $scope.yhkCheck = true;
        }
        // 审核状态默认给通过0：通过，1:未通过
        if ($scope.goType == 2) {
            if ($scope.agentMerchantInfo.agentStatus == 0) {
                $scope.agentStatusCheckqy = true;
                $scope.agentStatusCheckjy = false;
                $scope.agentStatusCheckwsh = false;
            }
            if ($scope.agentMerchantInfo.agentStatus == 1) {
                $scope.agentStatusCheckqy = false;
                $scope.agentStatusCheckjy = true;
                $scope.agentStatusCheckwsh = false;
            }
            if ($scope.agentMerchantInfo.agentStatus == 2) {
                $scope.agentStatusCheckqy = false;
                $scope.agentStatusCheckjy = false;
                $scope.agentStatusCheckwsh = true;
            }
        }
    }

    function getAgentMerchantInfo() {
        var agentMerchantInfo = {
            agentMerchantId: '',
            agentMerchantName: '',
            agentMerchantShortName: '',
            agentIdentityType: '',
            agentIdentityNum: '',
            agentIdentityUrl: '',
            agentPhone: '',
            agentPhoneStatus: '',
            agentEmail: '',
            agentEmailStatus: '',
            agentQq: '',
            agentStatus: 2,
            agentMerchantSetting: {
                singleFee: '',
                rateFee: '',
                settlecycle: '',
            }
        };
        return agentMerchantInfo;
    }

    httpSvc.getData('post', '/merchantInfo/init').then(function (data) {
        $scope.agentIdentityTypeList = data.identityType;
        angular.element('.ibox-content').removeClass('sk-loading');
    });

    // 验证代理商户号是否重复
    $scope.agentMerchantIdBlur = function ($event, name) {
        if (!verification(name, $event.target)) {
            httpSvc.getData('post', '/agentMerchantInfo/isAgentMerchantIdExist', JSON.stringify(name)).then(function (data) {
                if (data) {
                    verification("", $event.target);
                }
                $scope.isExist = data;
            });
        }
    };

    // 审核确定
    $scope.confirmStatus = function (agentMerchantInfo) {
        agentMerchantInfo.agentIdentityUrl = $scope.certificateImgUrl.toString();
        var params = angular.copy(agentMerchantInfo);
        httpSvc.getData('post', '/agentMerchantInfo/updateAgentMerchantInfo', params).then(function (value) {
            if (value) {
                toaster.pop({
                    type: 'success',
                    title: '代理商',
                    body: '审核代理商成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '代理商',
                    body: '审核代理商失败！'
                });
            }
        });
    }

    /**
     * 确定
     */
    $scope.confirm = function (agentMerchantInfo) {
        agentMerchantInfo.agentIdentityUrl = $scope.certificateImgUrl.toString();
        console.log(agentMerchantInfo);
        if ($scope.goType === 0) {// type:0 新增
            var params = angular.copy(agentMerchantInfo);
            httpSvc.getData('post', '/agentMerchantInfo/addAgentMerchantInfo', params).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '代理商',
                        body: '添加代理商成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '代理商',
                        body: '添加代理商失败！'
                    });
                }
            });
        }
        if ($scope.goType === 1) { //type:1 修改 typpe:2 审核
            var params = angular.copy(agentMerchantInfo);
            httpSvc.getData('post', '/agentMerchantInfo/updateAgentMerchantInfo', params).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '代理商',
                        body: '修改代理商成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '代理商',
                        body: '更新代理商失败！'
                    });
                }
            });
        }


    }
    //取消
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    // 验证输入框的输入数据
    $scope.agentMerchantNameBlur = $scope.agentIdentityNumBlur = $scope.agentPhoneBlur = $scope.agentEmailBlur =
        $scope.agentQqBlur = $scope.agentIdentityTypeBlur = $scope.agentIdentityUrlBlur = function ($event, name) {
            verification(name, $event.target);
        };

    //表单验证
    $timeout(function () {
        $scope.nextDisabled = function (myForm) {
            return myForm.agentMerchantId.$error.required || myForm.agentMerchantName.$error.required || myForm.agentIdentityNum.$error.required || myForm.agentIdentityType.$error.required
                || myForm.agentPhone.$error.required || myForm.agentEmail.$error.required || myForm.agentQq.$error.required || myForm.agentIdentityUrl.$error.required;
        };
    });

    // 文件上传
    $scope.uploader = new FileUploader({
        url: '/agentMerchantInfo/uploadAgentIdentityUrl'
    });
    $scope.uploader.onBeforeUploadItem = function (item) {
        item.formData.push({agentMerchantId: $scope.agentMerchantInfo.agentMerchantId});
    };
    $scope.uploader.onAfterAddingAll = function () {
        $scope.uploader.uploadAll();
    };
    $scope.uploader.onSuccessItem = function (item, response, status, headers) {
        $scope.certificateImgUrl.push(response);
        $scope.agentMerchantInfo.agentIdentityUrl = $scope.certificateImgUrl.toString();
    };
    $scope.uploader.onCompleteAll = function () {
        $scope.uploader.clearQueue();
    };
}

// 商户用户管理
function agentUserMgmtCtrl($scope, $state, $stateParams, $uibModal, toaster, NgTableParams, httpSvc) {
    // console.log(1111111)
    if ($stateParams.merchantInfo !== null) {
        var id = $stateParams.merchantInfo.agentMerchantId;

        console.log(id);
        console.log($stateParams.merchantInfo);
        $scope.delDisabled = true;
        $scope.selected = {};
        $scope.statusChange = function (row) {
            httpSvc.getData('post', '/agent/updateMerchantUser', {
                id: row.id,
                available: !row.available
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
        // console.log(id);
        httpSvc.getData('post', '/agent/getMerchantUserByMerchantId', {"belongto": id}).then(function (value) {
            $scope.merchantUserTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
        httpSvc.getData('post', '/agent/getMerchantRoleByMerchantId', {"belongto": id}).then(function (value) {
            $scope.merchantRole = value;
        });
        $scope.showModal = function (user, type) {
            var modalInstance = $uibModal.open({
                templateUrl: '/views/agent/agent_user_modal',
                controller: 'agentUserModalCtrl',
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
            httpSvc.getData('post', '/agent/deleteMerchantUser', idList).then(function (value) {
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
            httpSvc.getData('post', '/agent/getMerchantUserByMerchantId', {"belongto": id}).then(function (value) {
                $scope.merchantUserTable.settings({
                    dataset: value
                });
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        }
    } else {
        $state.go('base_view.agent.agent_list');
    }
}


// 商户用户管理弹窗
function agentUserModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, userInfo, merchantId, merchantRole) {
    console.log(userInfo);
    if (type !== 0) {
        $scope.userInfo = angular.copy(userInfo);
    }
    $scope.merchantRole = merchantRole;
    $scope.nameValid = false;
    $scope.passwordValid = false;
    $scope.type = type;
    $scope.userNameBlur = function ($event, userName) {
        if (!verification(userName, $event.target)) {
            httpSvc.getData('post', '/agent/isMerchantUserExist', {
                userName: userName,
                belongto: merchantId
            }).then(function (value) {
                if (value) {
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
            httpSvc.getData('post', '/agent/addMerchantUser', $scope.userInfo).then(function (value) {
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
            httpSvc.getData('post', '/agent/updateMerchantUser', $scope.userInfo).then(function (value) {
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


angular
    .module('inspinia')
    .controller('agentListCtrl', agentListCtrl)
    .controller('agentUserModalCtrl', agentUserModalCtrl)
    .controller('agentUserMgmtCtrl', agentUserMgmtCtrl)
    .controller('agentRateCtrl',agentRateCtrl)
    .controller('addAgentCtrl', addAgentCtrl)
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
