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

//
function mainCtrl($scope, $http, $state, $uibModal) {
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

// 主页控制1
function homePageCtrl($scope, httpSvc, NgTableParams, $filter, csvExp, toaster) {

}


// 用户管理
function userMgmtCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
    $scope.delDisabled = true;
    $scope.selected = {};
    $scope.statusChange = function (row) {
        httpSvc.getData('post', '/admin/user/update', {
            id: row.id,
            userName: row.userName,
            available: !row.available
        }).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '用户管理',
                    body: '更新用户状态成功！'
                });
                tableReload();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '用户管理',
                    body: '更新用户状态失败！'
                });
            }
        });
    };

    httpSvc.getData('post', '/admin/role/getAll').then(function (value) {
        $scope.allRole = value.data;
        httpSvc.getData('post', '/admin/user/getAll').then(function (value1) {
            $scope.userTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });

    $scope.showModal = function () {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/user/user_register_modal',
            controller: 'userRegisterModalCtrl',
            resolve: {
                allRole: function () {
                    return $scope.allRole;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload();
        }, function () {
            // tableReload();
        });
    };

    $scope.edit = function (user, type) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/user/user_change_modal',
            controller: 'userChangeModalCtrl',
            resolve: {
                userInfo: function () {
                    return user;
                },
                type: function () {
                    return type;
                },
                allRole: function () {
                    return $scope.allRole;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload();
        }, function () {
        });
    };

    $scope.search = function (searchInfo) {
        angular.extend($scope.userTable.filter(), searchInfo);
    };

    $scope.reset = function () {
        $scope.searchInfo = {};
        $scope.userTable.filter({});
    };

    $scope.del = function () {
        var idList = [];
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                idList.push(id);
            }
        }

        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + idList.length + '条数据吗？</strong>' +
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
            httpSvc.getData('post', '/admin/user/delete', idList).then(function (value) {
                if (value.code == 0) {
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
                $scope.selected = {};
            });
        }, function () {
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
        httpSvc.getData('post', '/admin/user/getAll').then(function (value) {
            value = value.data;
            for (var i = 0; i < value.length; i++) {
                for (var j = 0; j < $scope.allRole.length; j++) {
                    if (value[i].roleId === $scope.allRole[j].id) {
                        value[i]['role'] = $scope.allRole[j];
                        break;
                    }
                }
            }
            $scope.userTable.settings({
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }
}

// 用户注册弹窗
function userRegisterModalCtrl($scope, $uibModalInstance, httpSvc, toaster, allRole) {
    // $scope.nextDisable = true;
    $scope.nameValid = false;
    $scope.passwordValid = false;
    $scope.allRole = allRole;

    $scope.userNameBlur = function ($event, userName) {
        if (!verification(userName, $event.target)) {
            httpSvc.getData('post', '/admin/user/isExist', JSON.stringify(userName)).then(function (value) {
                if (value) {
                    $scope.nameValid = false;
                    verification(undefined, $event.target);
                } else {
                    $scope.nameValid = true;
                    verification(value, $event.target);
                }
            });
        } else {
            $scope.nameValid = false;
        }
    };

    $scope.passwordKeyUp = function ($event, password) {
        if (password === undefined || password.length < 6) {
            angular.element($event.target).parent().removeClass('has-success');
            angular.element($event.target).parent().addClass('has-error');
        } else {
            angular.element($event.target).parent().removeClass('has-error');
            angular.element($event.target).parent().addClass('has-success');
        }
    };

    $scope.passwordBKeyUp = function ($event, passwordB, password) {
        if (passwordB !== password) {
            $scope.passwordValid = false;
            angular.element($event.target).parent().removeClass('has-success');
            angular.element($event.target).parent().addClass('has-error');
        } else {
            $scope.passwordValid = true;
            angular.element($event.target).parent().removeClass('has-error');
            angular.element($event.target).parent().addClass('has-success');
        }
    };

    $scope.namesBlur = $scope.phoneBlur = $scope.emailBlur = $scope.passwordBlur = $scope.passwordBBlur = function ($event, value) {
        verification(value, $event.target)
    };

    $scope.myRegex = function ($event, value) {
        myRegex($event, value, 'g');
    };

    $scope.nextBtn = function (tab) {
        if (tab !== 3) {
            $scope.tab += 1;
        } else {
            if ($scope.register.roleId === undefined) {
                $scope.register.roleId = 0;
            }
            httpSvc.getData('post', '/admin/user/register', $scope.register).then(function (value) {
                if (value.code == 0) {
                    $scope.register = null;
                    toaster.pop({
                        type: 'success',
                        title: '用户管理',
                        body: '用户注册成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '用户管理',
                        body: '用户注册失败！'
                    });
                }
            });
        }
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    $scope.nextDisable = function (myForm, tab) {
        switch (tab) {
            case 1:
                return !(myForm.userName.$valid && myForm.password.$valid && myForm.passwordB.$valid && $scope.nameValid && $scope.passwordValid);
            case 2:
                return !(myForm.realName.$valid && myForm.mobile.$valid && myForm.email.$valid);
            default:
                return false;
        }
    }
}

// 用户变更弹窗
function userChangeModalCtrl($scope, $uibModalInstance, httpSvc, toaster, userInfo, type, allRole) {
    $scope.tab = type;
    $scope.passwordValid = false;
    $scope.changType = type;
    $scope.changeInfo = angular.copy(userInfo);
    $scope.changeInfo.roleId += '';

    if (type === 1) {
        $scope.allRole = allRole;
    }

    $scope.passwordKeyUp = function ($event, password) {
        if (password === undefined || password.length < 6) {
            angular.element($event.target).parent().removeClass('has-success');
            angular.element($event.target).parent().addClass('has-error');
        } else {
            angular.element($event.target).parent().removeClass('has-error');
            angular.element($event.target).parent().addClass('has-success');
        }
    };

    $scope.passwordBKeyUp = function ($event, passwordB, password) {
        if (passwordB !== password) {
            $scope.passwordValid = false;
            angular.element($event.target).parent().removeClass('has-success');
            angular.element($event.target).parent().addClass('has-error');
        } else {
            $scope.passwordValid = true;
            angular.element($event.target).parent().removeClass('has-error');
            angular.element($event.target).parent().addClass('has-success');
        }
    };

    $scope.namesBlur = $scope.phoneBlur = $scope.emailBlur = function ($event, value) {
        verification(value, $event.target);
    };

    $scope.changeUser = function () {
        if ($scope.changeInfo === undefined || $scope.changeInfo === null) {
            $scope.changeInfo = {};
            $scope.changeInfo.roleId = '0';
        }
        $scope.changeInfo.id = userInfo.id;
        httpSvc.getData('post', '/admin/user/update', $scope.changeInfo).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '用户变更',
                    body: type === 1 ? '信息编辑成功！' : '密码变更成功！'
                });
                $uibModalInstance.close();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '用户变更',
                    body: type === 1 ? '信息编辑失败！' : '密码变更失败！'
                });
            }
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    $scope.nextDisable = function (myForm, tab) {
        switch (tab) {
            case 1:
                return !(myForm.realName.$valid && myForm.mobile.$valid && myForm.email.$valid && !angular.equals($scope.changeInfo, userInfo));
            case 2:
                return !(myForm.password.$valid && myForm.passwordB.$valid && $scope.passwordValid);
            default:
                return false;
        }
    }
}

// 角色管理
function roleMgmtCtrl($scope, $uibModal, toaster, httpSvc, NgTableParams) {
    $scope.delDisabled = true;
    $scope.selected = {};
    $scope.delDisabled = function () {
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                return false;
            }
        }
        return true;
    };
    $scope.statusChange = function (row) {
        httpSvc.getData('post', '/admin/role/update',{
            id: row.id,
            available: !row.available
        }).then(function (value) {
            if (value.code == 0) {
                toaster.pop({
                    type: 'success',
                    title: '角色管理',
                    body: '更新角色状态成功！'
                });
                tableReload();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '角色管理',
                    body: '更新角色状态失败！'
                });
            }
        });
    };
    httpSvc.getData('post', '/admin/role/getAllPrivileges').then(function (value) {
        $scope.menus = value.data;
        httpSvc.getData('post', '/admin/role/getAll').then(function (value1) {
            $scope.roleTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });

    $scope.showModal = $scope.edit = function (type, roleInfo) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/user/role_modal',
            controller: 'roleModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                roleInfo: function () {
                    return roleInfo;
                },
                menus: function () {
                    return $scope.menus;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
        });
    };

    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/admin/role/getAll').then(function (value) {
            $scope.roleTable.settings({
                dataset: value.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    var str = '';
    var strList = str.split(',');
    var idArr = [];
    // $scope.collectSelectRows = function ($event, id) {
    //
    //     var isChecked = angular.element($event.target).prop("checked");
    //
    //     if (isChecked == true) {
    //
    //         idArr.push(id);
    //         $scope.delDisabled = false;
    //
    //     } else {
    //
    //         idArr.splice(id);
    //
    //     }
    //
    //     if (idArr.length == 0) {
    //
    //         $scope.delDisabled = true;
    //
    //     }
    //
    // }


    // 批量删除
    $scope.del = function () {
        var ids = []
        var count = 0;
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                ids.push(id);
                count++;
            }
        }
        ids = ids.join(",")
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
            httpSvc.getData('post', '/admin/role/delete', {ids: ids}).then(function (value) {

                if (value.code == 0) {

                    toaster.pop({
                        type: 'success',
                        title: '角色删除',
                        body: '删除成功！'
                    });

                } else {

                    toaster.pop({
                        type: 'error',
                        title: '角色删除',
                        body: '删除失败！'
                    });

                }
                count = 0;
                ids = [];
                $scope.selected = {};
                tableReload();
            });

        }, function () {
        });
    };


}

// 新增角色弹窗
function roleModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, roleInfo, menus) {
    $scope.oneAtATime = false;
    if (type === 0) {
        $scope.nameValid = false;
        $scope.roleInfo = {privilegesList: []};
    } else {
        $scope.nameValid = true;
        $scope.roleInfo = angular.copy(roleInfo);
        if ($scope.roleInfo.privilegesId !== undefined && $scope.roleInfo.privilegesId.trim() !== '') {
            $scope.roleInfo.privilegesList = $scope.roleInfo.privilegesId.replace(/[^,\d]/g, '').split(',');
        } else {
            $scope.roleInfo.privilegesList = [];
        }
    }

    $scope.exists = function (value, selected) {
        if (value === undefined || selected === undefined) {
            return false;
        }
        return selected.indexOf(value) > -1;
    };
    $scope.toggle = function (value, selected) {
        var index = selected.indexOf(value);
        if (index > -1) {
            selected.splice(index, 1);
        } else {
            selected.push(value);
        }
    };
    $scope.menu = [];
    for (var i = 0; i < menus.length; i++) {
        if (menus[i].parentId === undefined) {
            var menu = menus[i];
            menu['submenu'] = [];
            for (var j = 0; j < menus.length; j++) {
                if (menus[j].parentId === menus[i].id) {
                    menu.submenu.push(menus[j]);
                }
            }
            $scope.menu.push(menu);
        }
    }
    $scope.nameBlur = function ($event, roleName) {
        if (!verification(roleName, $event.target)) {
            httpSvc.getData('post', '/admin/role/isExist', JSON.stringify(roleName)).then(function (value) {
                if (value) {
                    verification(undefined, $event.target);
                    $scope.nameValid = false;
                } else {
                    verification(value, $event.target);
                    $scope.nameValid = true;
                }
            });
        }
    };
    $scope.nextDisabled = function (myForm) {
        if (type === 1) {
            return !(myForm.roleName.$valid && $scope.nameValid && !angular.equals($scope.roleInfo, roleInfo));
        } else {
            return !(myForm.roleName.$valid && $scope.nameValid);
        }
    };
    $scope.confirmBtn = function () {
        $scope.roleInfo.privilegesId = $scope.roleInfo.privilegesList.toString();
        if (type === 0) {
            httpSvc.getData('post', '/admin/role/add', $scope.roleInfo).then(function (value) {
                if (value.code == 0) {
                    toaster.pop({
                        type: 'success',
                        title: '角色管理',
                        body: '新增角色成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '角色管理',
                        body: '新增角色失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/admin/role/update', $scope.roleInfo).then(function (value) {
                if (value.code == 0) {
                    toaster.pop({
                        type: 'success',
                        title: '角色管理',
                        body: '更新角色成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '角色管理',
                        body: '更新角色失败！'
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}

// 系统日志
function systemLogCtrl($scope, $uibModal, NgTableParams, httpSvc) {
    var oldEndTime = undefined;
    $scope.endTimeDisable = true;
    $scope.format = 'yyyy/MM/dd';
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
    $scope.$watch('searchInfo.startTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.startTime;
            $scope.endTimeDisable = false;
            if (oldEndTime !== undefined) {
                $scope.searchInfo.endTime = oldEndTime;
            }
        } else {
            $scope.endTimeDisable = true;
            if ($scope.searchInfo !== undefined) {
                oldEndTime = $scope.searchInfo.endTime;
                $scope.searchInfo.endTime = undefined;
            }
        }
    });
    $scope.$watch('searchInfo.endTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = $scope.searchInfo.endTime;
        } else {
            $scope.dateOptions1.maxDate = new Date();
        }
    });

    $scope.search = function (searchInfo) {
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    $scope.reset = function () {
        $scope.searchInfo = {};
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    httpSvc.getData('post', '/system/getConstantByGroupName', JSON.stringify('Log')).then(function (value) {
        $scope.logType = value;
        $scope.logTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/system/getSystemLog', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    logSearch: $scope.searchInfo
                }).then(function (value) {
                    value = value.data;
                    var i, j;
                    params.total(value.total);
                    for (i = 0; i < value.records.length; i++) {
                        value.records[i].type += '';
                        for (j = 0; j < $scope.logType.length; j++) {
                            if (value.records[i].type === $scope.logType[j].firstValue) {
                                value.records[i]['typeCN'] = $scope.logType[j].name;
                            }
                        }
                    }
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.records;
                });
            }
        });
    });
    $scope.showModal = function (logInfo) {
        var modalSize = '';
        switch (logInfo.type) {
            case '9':
                modalSize = 'lg';
                break;
        }
        var modalInstance = $uibModal.open({
            templateUrl: '/views/system_settings/system_log_modal',
            controller: 'systemLogModalCtrl',
            size: modalSize,
            resolve: {
                logInfo: function () {
                    return logInfo;
                }
            }
        });
        modalInstance.result.then(function () {

        }, function () {

        });
    };
}

// 商户系统日志
function merchantSystemLogCtrl($scope, $uibModal, NgTableParams, httpSvc) {
    var oldEndTime = undefined;
    $scope.endTimeDisable = true;
    $scope.format = 'yyyy/MM/dd';
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
    $scope.$watch('searchInfo.startTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.startTime;
            $scope.endTimeDisable = false;
            if (oldEndTime !== undefined) {
                $scope.searchInfo.endTime = oldEndTime;
            }
        } else {
            $scope.endTimeDisable = true;
            if ($scope.searchInfo !== undefined) {
                oldEndTime = $scope.searchInfo.endTime;
                $scope.searchInfo.endTime = undefined;
            }
        }
    });
    $scope.$watch('searchInfo.endTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = $scope.searchInfo.endTime;
        } else {
            $scope.dateOptions1.maxDate = new Date();
        }
    });

    $scope.search = function (searchInfo) {
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    $scope.reset = function () {
        $scope.searchInfo = {};
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    httpSvc.getData('post', '/system/getConstantByGroupName', JSON.stringify('Log')).then(function (value) {
        $scope.logType = value;
        $scope.logTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/system/getMerchantSystemLog', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    logSearch: $scope.searchInfo
                }).then(function (value) {
                    value = value.data;
                    var i, j;
                    params.total(value.total);
                    for (i = 0; i < value.records.length; i++) {
                        value.records[i].type += '';
                        for (j = 0; j < $scope.logType.length; j++) {
                            if (value.records[i].type === $scope.logType[j].firstValue) {
                                value.records[i]['typeCN'] = $scope.logType[j].name;
                            }
                        }
                    }
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.records;
                });
            }
        });
    });
    $scope.showModal = function (logInfo) {
        var modalSize = '';
        switch (logInfo.type) {
            case '9':
                modalSize = 'lg';
                break;
        }
        var modalInstance = $uibModal.open({
            templateUrl: '/views/system_settings/system_log_modal',
            controller: 'systemLogModalCtrl',
            size: modalSize,
            resolve: {
                logInfo: function () {
                    return logInfo;
                }
            }
        });
        modalInstance.result.then(function () {

        }, function () {

        });
    };
}

// 代理商系统日志
function agentSystemLogCtrl($scope, $uibModal, NgTableParams, httpSvc) {
    var oldEndTime = undefined;
    $scope.endTimeDisable = true;
    $scope.format = 'yyyy/MM/dd';
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
    $scope.$watch('searchInfo.startTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions2.minDate = $scope.searchInfo.startTime;
            $scope.endTimeDisable = false;
            if (oldEndTime !== undefined) {
                $scope.searchInfo.endTime = oldEndTime;
            }
        } else {
            $scope.endTimeDisable = true;
            if ($scope.searchInfo !== undefined) {
                oldEndTime = $scope.searchInfo.endTime;
                $scope.searchInfo.endTime = undefined;
            }
        }
    });
    $scope.$watch('searchInfo.endTime', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions1.maxDate = $scope.searchInfo.endTime;
        } else {
            $scope.dateOptions1.maxDate = new Date();
        }
    });

    $scope.search = function (searchInfo) {
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    $scope.reset = function () {
        $scope.searchInfo = {};
        $scope.logTable.page(1);
        $scope.logTable.reload();
    };
    httpSvc.getData('post', '/system/getConstantByGroupName', JSON.stringify('Log')).then(function (value) {
        $scope.logType = value;
        $scope.logTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/system/getAgentSystemLog', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    orderBy: params.sorting(),
                    logSearch: $scope.searchInfo
                }).then(function (value) {
                    value = value.data;
                    var i, j;
                    params.total(value.total);
                    for (i = 0; i < value.records.length; i++) {
                        value.records[i].type += '';
                        for (j = 0; j < $scope.logType.length; j++) {
                            if (value.records[i].type === $scope.logType[j].firstValue) {
                                value.records[i]['typeCN'] = $scope.logType[j].name;
                            }
                        }
                    }
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.records;
                });
            }
        });
    });
    $scope.showModal = function (logInfo) {
        var modalSize = '';
        switch (logInfo.type) {
            case '9':
                modalSize = 'lg';
                break;
        }
        var modalInstance = $uibModal.open({
            templateUrl: '/views/system_settings/system_log_modal',
            controller: 'systemLogModalCtrl',
            size: modalSize,
            resolve: {
                logInfo: function () {
                    return logInfo;
                }
            }
        });
        modalInstance.result.then(function () {

        }, function () {

        });
    };
}


// 系统日志弹窗
function systemLogModalCtrl($scope, $uibModalInstance, logInfo) {
    $scope.logInfo = angular.copy(logInfo);
    // $scope.messageJson = null;
    if (logInfo.type !== '0' && logInfo.type !== '9') {
        // 将超过15位的数字替换成字符串再转json对象
        $scope.logInfo.messageJson = angular.fromJson($scope.logInfo.message.replace(/(\d{22,})/g, '"$1"'));
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss()
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

/**
 * 系统常量配置管理
 *
 */
function constantInfoCtrl($scope, $uibModal, NgTableParams, httpSvc, toaster) {
    $scope.delDisabled = true;
    $scope.selected = {};
    $scope.delDisabled = function () {
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                return false;
            }
        }
        return true;
    };

    $scope.cleanCache = function () {
        httpSvc.getData('get', '/constant/cleanCache');
    }


    $scope.groupList = [{id: '', title: '--请选择常量组别--'}];
    $scope.constantTable = new NgTableParams({}, {
        getData: function (params) {
            angular.element('.ibox-content').addClass('sk-loading');
            return httpSvc.getData('post', '/constant/getConstantInfo', {
                pageNum: params.page() ,
                pageSize: params.count(),
                sortFieldName: "id",
                sortType: "desc",
                object: params.filter()
            }).then(function (value) {
                value = value.data;
                $scope.totalItems = value.total;
                params.total(value.total);
                angular.element('.ibox-content').removeClass('sk-loading');
                return value.records;
            });
        }
    });
    httpSvc.getData('post', '/constant/getGroupAll').then(function (value) {
        value = value.data;
        for (var i = 0; i < value.length; i++) {
            $scope.groupList.push({
                id: value[i].code,
                title: value[i].name
            });
        }


    });

    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/constant/getConstantList', {}).then(function (value) {
            $scope.constantTable.settings({
                dataset: value
            });
            $scope.totalItems = value.length;
            angular.element('.ibox-content').removeClass('sk-loading');
        });

    }

    $scope.showModal = function (constant) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/constant/constantUpdate',
            controller: 'constantInfoEditModalCtrl',
            resolve: {
                constant: function () {
                    return constant;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };
    $scope.showAddModal = function (constant) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/constant/constantAdd',
            controller: 'constantInfoAddModalCtrl',
            resolve: {
                constant: constant
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });


    };
    var idArr = [];
    $scope.collectSelectRows = function ($event, id) {

        var isChecked = angular.element($event.target).prop("checked");

        if (isChecked == true) {

            idArr.push(id);
            $scope.delDisabled = false;

        } else {

            idArr.remove(id);

        }

        if (idArr.length == 0) {

            $scope.delDisabled = true;

        }

    }

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
        ids = ids.join(",")
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


            httpSvc.getData('post', '/constant/batchDel', {ids: ids}).then(function (value) {

                if (value.result == 1) {

                    toaster.pop({
                        type: 'success',
                        title: '系统常量删除',
                        body: '删除成功！'
                    });

                } else {

                    toaster.pop({
                        type: 'error',
                        title: '系统常量删除',
                        body: '删除失败！'
                    });

                }
                $scope.selected = {};
                idArr = [];

                tableReload();


            });
        }, function () {
        });
    }


}

function sysGroupInfoCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {
    $scope.delDisabled = true;
    $scope.selected = {};
    $scope.delDisabled = function () {
        for (var code in $scope.selected) {
            if ($scope.selected[code]) {
                return false;
            }
        }
        return true;
    };


    $scope.modelList = [
        {id: 1, title: '系统'},
        {id: 2, title: '商户'},
        {id: 3, title: '代理商'}
    ];
    $scope.systemList = [
        {id: 0, title: '否'},
        {id: 1, title: '是'}
    ];
    $scope.sysGroupTable = new NgTableParams({}, {
        getData: function (params) {
            angular.element('.ibox-content').addClass('sk-loading');
            return httpSvc.getData('post', '/sysgroup/findSysGroup', {

                pageNum: params.page() ,
                pageSize: params.count(),
                object: params.filter()

            }).then(function (value) {
                value = value.data;
                $scope.totalItems = value.total;
                params.total(value.total);
                angular.element('.ibox-content').removeClass('sk-loading');

                return value.records;

            });
        }
    });

    function tableReload() {
        $scope.sysGroupTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/sysgroup/findSysGroup', {

                    pageNum: params.page(),
                    pageSize: params.count(),
                    object: params.filter()

                }).then(function (value) {
                    value = value.data;
                    $scope.totalItems = value.total;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');

                    return value.records;

                });
            }
        });
    }

    // 1:系统 2:商户 3:代理商 类型
    $scope.getmodel = function (temp) {
        if (temp == 1) {
            return "系统"
        }
        if (temp == 2) {
            return "商户"
        }
        if (temp == 3) {
            return "代理商"
        }
    };

    // 0: 否 1: 是
    $scope.getsystem = function (temp) {
        if (temp == 1) {
            return "是"
        }
        if (temp == 0) {
            return "否"
        }
    };

    $scope.showAddSysGroupModal = function (sysgroup) {

        var modalInstance = $uibModal.open({
            templateUrl: '/views/constant/sysgroupAdd',
            controller: 'sysGroupInfoAddModalCtrl',
            resolve: {
                sysgroup: sysgroup
            }
        });
        modalInstance.result.then(function () {
            // $state.reload()
            // $scope.sysGroupTable;
            tableReload()
        }, function () {
            // $state.reload()
            // $scope.sysGroupTable;
            tableReload()
        });
    };

    $scope.showModal = function (sysgroup) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/constant/sysgroupUpdate',
            controller: 'sysGroupInfoEditModalCtrl',
            resolve: {
                sysgroup: function () {
                    return sysgroup;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
            // $state.reload()
            // $scope.sysGroupTable
        }, function () {
            tableReload()
            // $state.reload()
            // $scope.sysGroupTable
        });

    }


    var codeArr = [];

    // $scope.collectSelectRows = function ($event, id) {
    //
    //     var isChecked = angular.element($event.target).prop("checked");
    //
    //     if (isChecked == true) {
    //
    //         codeArr.push(id);
    //         $scope.delDisabled = false;
    //
    //     } else {
    //
    //         codeArr.remove(id);
    //
    //     }
    //
    //     if (codeArr.length == 0) {
    //
    //         $scope.delDisabled = true;
    //
    //     }
    //
    // }


    //     toaster.pop({
    //         type: 'success',
    //         title: '刷新缓存',
    //         body: '刷新缓存成功'
    //     });
    //     // angular.element($event.target).toggleClass('active');
    //     // angular.element($event.target).toggleClass('btn-primary');
    //     // angular.element($event.target).toggleClass('btn-danger');
    //     // angular.element($event.target).hasClass('active') ? angular.element($event.target).text('启用') : angular.element($event.target).text('禁用');
    //     // $scope.limitTable.page(1);
    //     // $scope.limitTable.reload();
    // } else {
    //     toaster.pop({
    //         type: 'error',
    //         title: '刷新缓存',
    //         body: '刷新缓存成功'
    //     });
    // }


    // 批量删除
    $scope.batchDel = function () {
        var codes = [];
        var count = 0;
        for (var code in $scope.selected) {
            if ($scope.selected[code]) {
                codes.push(code);
                count++;
            }
        }
        codes = codes.join(",")
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
            httpSvc.getData('post', '/sysgroup/batchDel', {codes: codes}).then(function (value) {

                if (value.code == 0) {

                    toaster.pop({
                        type: 'success',
                        title: '系统常量删除',
                        body: '删除成功！'
                    });

                } else {

                    toaster.pop({
                        type: 'error',
                        title: '系统常量删除',
                        body: '删除失败！'
                    });

                }
                codes = [];
                $scope.selected = {};
                tableReload();
            });

        }, function () {
        });
    };


}

//添加常量组别配置
function refreshCacheCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {
    //刷新缓存
    $scope.refreshCache = function () {
        var title = '缓存刷新确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定刷新缓存吗？</strong>' +
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
            httpSvc.getData('post', '/user/refreshCache', {}).then(function (value) {

                if (value == 'SUCCESS') {
                    toaster.pop({
                        type: 'success',
                        title: '缓存刷新',
                        body: '成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '缓存刷新',
                        body: '操作失败！'
                    });
                }
            });
        }, function () {
        });
    }

}

//添加常量组别配置
function sysGroupInfoAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, sysgroup) {

    $scope.sysGroup = sysgroup;

    $scope.back = function () {

        $uibModalInstance.dismiss();

    };
    $scope.groupNameValid = false;
    $scope.groupCodeValid = false;

    $scope.groupNameBlur = function ($event, groupName) {
        if (!verification(groupName, $event.target)) {
            httpSvc.getData('post', '/sysgroup/groupName/isExist', JSON.stringify(groupName)).then(function (value) {
                if (value.code == 1) {
                    verification(undefined, $event.target);
                    $scope.groupNameValid = false;
                } else {
                    verification(value, $event.target);
                    $scope.groupNameValid = true;
                }
            });
        }
    }
    $scope.groupCodeBlur = function ($event, groupCode) {
        if (!verification(groupCode, $event.target)) {
            httpSvc.getData('post', '/sysgroup/groupCode/isExist', JSON.stringify(groupCode)).then(function (value) {
                if (value.code == 1) {
                    verification(undefined, $event.target);
                    $scope.groupCodeValid = false;
                } else {
                    verification(value, $event.target);
                    $scope.groupCodeValid = true;
                }
            });
        }
    }

    $scope.nextDisabled = function (myForm) {
        return !(myForm.name.$valid && myForm.code.$valid && $scope.groupCodeValid && $scope.groupNameValid);
    };

    $scope.addSysGroup = function () {

        var obj = angular.element("#sysGroupObjForm").serializeJson();

        httpSvc.getData('post', '/sysgroup/addSysGroup', obj).then(function (value) {

            if (value.code == 0) {

                toaster.pop({
                    type: 'success',
                    title: '常量组别添加',
                    body: '添加成功！'
                });

            } else {

                toaster.pop({
                    type: 'error',
                    title: '常量组别添加',
                    body: '添加失败！'
                });

            }

            $uibModalInstance.dismiss();

        });
    }
}

//编辑常量组别配置
function sysGroupInfoEditModalCtrl($scope, $uibModalInstance, httpSvc, toaster, sysgroup) {
    $scope.tempsystem = sysgroup.system;
    $scope.tempmodel = sysgroup.model;
    $scope.tempgroupname = sysgroup.name;
    // 1:系统 2:商户 3:代理商 类型
    $scope.getmodel = function (temp) {
        if (temp == 1) {
            return "系统"
        }
        if (temp == 2) {
            return "商户"
        }
        if (temp == 3) {
            return "代理商"
        }
    };

    // 0: 否 1: 是
    $scope.getsystem = function (temp) {
        if (temp == 1) {
            return "是"
        }
        if (temp == 0) {
            return "否"
        }
    };

    function tableReload() {
        $scope.sysGroupTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                return httpSvc.getData('post', '/sysgroup/findSysGroup', {

                    pageNum: params.page(),
                    pageSize: params.count(),
                    object: params.filter()

                }).then(function (value) {
                    value = value.data;
                    $scope.totalItems = value.total;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');

                    return value.records;

                });
            }
        });
    }

    $scope.updateIsDisabled = true;
    $scope.updateSortValueDisabled = false;
    $scope.sysgroup = sysgroup;
    $scope.back = function () {
        $uibModalInstance.dismiss();
    };
    $scope.groupNameValid = false;

    $scope.groupNameBlur = function ($event, groupName) {
        if (!verification(groupName, $event.target)) {
            httpSvc.getData('post', '/sysgroup/groupName/isExist', JSON.stringify(groupName)).then(function (value) {
                if (value.code == 1) {
                    verification(undefined, $event.target);
                    $scope.groupNameValid = true;
                } else {
                    verification(value, $event.target);
                    $scope.groupNameValid = false;
                }
            });
        } else {
            $scope.groupNameValid = true;
        }
    }


    $scope.updateSysgroup = function () {

        var obj = angular.element("#sysgroupObjForm").serializeJson();

        httpSvc.getData('post', '/sysgroup/update', obj).then(function (value) {

            if (value.code == 0) {

                toaster.pop({
                    type: 'success',
                    title: '系统常量编辑',
                    body: '更新成功！'
                });

            } else {

                toaster.pop({
                    type: 'error',
                    title: '系统常量编辑',
                    body: '更新失败！'
                });


            }

            $uibModalInstance.dismiss();

        });
        $scope.updateIsDisabled = true;


    }
}

// 添加系统常量配置
function constantInfoAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, constant) {

    $scope.constant = constant;
    httpSvc.getData('post', '/constant/getGroupAll').then(function (value) {

        $scope.groupList = value.data;

    });

    $scope.back = function () {

        $uibModalInstance.dismiss();

    };


    $scope.optionMouseOver = function ($event) {


        angular.element($event.target).parent().val("String");


    }

    $scope.optionMouseClick = function ($event) {

        angular.element($event.target).parent().attr("size", 0);
        var id = angular.element($event.target).parent().attr("id");
        angular.element("#selectInputDiv_" + id).remove();


    }

    $scope.addSortValueDisabled = true;

    $scope.nameFocus = function () {

        angular.element("#name").addClass("has-success");
        angular.element("#name").removeClass("has-error");
        angular.element("input:not(#name)").removeClass("has-success");

    }

    $scope.nameBlur = function () {

        var name = angular.element("#name").val();
        if (null == name || name == "") {

            angular.element("#name").addClass("has-error");
            angular.element("#name").removeClass("has-success");

        }

    }

    $scope.firstValueFocus = function () {
        angular.element("#firstValue").removeClass("has-error");
        angular.element("#firstValue").addClass("has-success");
        angular.element("input:not(#firstValue)").removeClass("has-success");
        angular.element("select:not(#firstValue)").removeClass("has-success");
    }

    $scope.firstValueBlur = function () {
        var firstValue = angular.element("#firstValue").val();
        if (null == firstValue || firstValue == "") {
            angular.element("#firstValue").addClass("has-error");
            angular.element("#firstValue").removeClass("has-success");
        }

    }


    $scope.sortValueFocus = function () {
        angular.element("#sortValue").removeClass("has-error");
        angular.element("#sortValue").addClass("has-success");
        angular.element("input:not(#sortValue)").removeClass("has-success");
        angular.element("select:not(#sortValue)").removeClass("has-success");
        $scope.addSortValueDisabled = false;

    }

    $scope.sortValueBlur = function () {
        var sortValue = angular.element("#sortValue").val();
        if (null != sortValue && sortValue != "" && !/^\d+?$/.test(sortValue)) {

            $scope.addSortValueDisabled = true;
            angular.element("#sortValue").addClass("has-error");
            angular.element("#sortValue").removeClass("has-success");

        }

    }

    $scope.groupCodeChange = function () {

        var groupCode = angular.element("#groupCode").val();
        angular.element("#groupCode").attr("size", "0");
        if (null == groupCode || groupCode == "") {

            angular.element("#groupCode").addClass("has-error");
            angular.element("#groupCode").removeClass("has-success");

            angular.element("#groupCodePrompt").addClass("select-prompt");
            angular.element("#groupCodePrompt").removeClass("no-select-prompt");


        } else {

            angular.element("#groupCode").removeClass("has-error");
            angular.element("#groupCode").addClass("has-success");

            angular.element("#groupCodePrompt").removeClass("select-prompt");
            angular.element("#groupCodePrompt").addClass("no-select-prompt");

        }


        angular.element("input:not(#groupCode)").removeClass("has-success");
        $("#selectInputDiv_groupCode").remove();

    }

    var idSave = "";
    $scope.groupCodeMouseDown = function ($event) {

        var id = angular.element($event.target).attr("id");
        var size = angular.element($event.target).attr("size");
        if (size == undefined) {

            size = 7;

        }
        if (id !== undefined) {

            idSave = id;
            angular.element($event.target).attr("size", "7");


        } else {

            angular.element($event.target).attr("size", "0");

        }

        if (id == undefined || size == 7) {

            len = 1;

        } else {

            len = 0

        }

        if (len == 0) {

            var addHtml = '<div id="selectInputDiv_' + idSave + '" class="align-middle" ><img src="img/arrow/arrow.png" class="select-arrow" /><input type="text" id="selectInput" name="selectInput" class="form-control limit-height" /></div>';
            angular.element($event.target).before(addHtml);

        }

        angular.element("input:not(#groupCode)").removeClass("has-success");
        angular.element("#groupCodePrompt").removeClass("select-prompt");
        angular.element("#groupCodePrompt").addClass("no-select-prompt");

    }


    $scope.groupCodeBlur = function () {

        var id = angular.element("#groupCode").attr("id");
        var groupCode = angular.element("#groupCode").val();
        angular.element("#groupCode").attr("size", "0");
        var len = angular.element("#selectInputDiv_" + id).length;
        if (len > 0) {

            angular.element("#selectInputDiv_" + id).remove();

        }
        if (null == groupCode || groupCode == "") {

            angular.element("#groupCode").addClass("has-error");
            angular.element("#groupCode").removeClass("has-success");
            $scope.addIsDisabled = true;
            angular.element("#groupCodePrompt").addClass("select-prompt");
            angular.element("#groupCodePrompt").removeClass("no-select-prompt");

        } else {

            angular.element("#groupCode").removeClass("has-error");
            angular.element("#groupCode").addClass("has-success");
            $scope.addIsDisabled = false;
            angular.element("#groupCodePrompt").removeClass("select-prompt");
            angular.element("#groupCodePrompt").addClass("no-select-prompt");

        }

    }

    $scope.generalFocus = function ($event) {
        console.log($event);
        $scope.addIsDisabled = false;
        var id = angular.element($event.target).attr("id");
        angular.element("input:not(#" + id + ")").removeClass("has-success");
        angular.element("select:not(#" + id + ")").removeClass("has-success");

    }

    $scope.addConstant = function () {

        var obj = angular.element("#constantObjForm").serializeJson();

        httpSvc.getData('post', '/constant/save', obj).then(function (value) {

            if (value.success == 1) {

                toaster.pop({
                    type: 'success',
                    title: '系统常量添加',
                    body: '添加成功！'
                });

            } else {

                toaster.pop({
                    type: 'error',
                    title: '系统常量添加',
                    body: '添加失败！'
                });

            }

            $uibModalInstance.dismiss();

        });

        $scope.addIsDisabled = true;

    }


}

// 编辑系统常量配置
function constantInfoEditModalCtrl($scope, $uibModalInstance, httpSvc, toaster, constant) {

    $scope.updateIsDisabled = true;
    $scope.updateSortValueDisabled = false;

    $scope.constant = constant;
    httpSvc.getData('post', '/constant/getGroupAll').then(function (value) {

        $scope.groupList = value.data;

    });

    $scope.back = function () {

        $uibModalInstance.dismiss();

    };

    $scope.optionMouseOver = function ($event) {


        angular.element($event.target).parent().val("String");


    }

    $scope.optionMouseClick = function ($event) {

        angular.element($event.target).parent().attr("size", 0);
        var id = angular.element($event.target).parent().attr("id");
        angular.element("#selectInputDiv_" + id).remove();


    }

    $scope.nameFocus = function () {

        $scope.updateIsDisabled = false;
        angular.element("#name").addClass("has-success");
        angular.element("#name").removeClass("has-error");
        angular.element("input:not(#name)").removeClass("has-success");
        angular.element("select:not(#name)").removeClass("has-success");

    }

    $scope.nameBlur = function () {

        var name = angular.element("#name").val();
        if (null == name || name == "") {

            $scope.updateIsDisabled = true;
            angular.element("#name").addClass("has-error");
            angular.element("#name").removeClass("has-success");

        }

    }

    $scope.sortValueFocus = function () {

        angular.element("#sortValue").removeClass("has-error");
        angular.element("#sortValue").addClass("has-success");
        angular.element("input:not(#sortValue)").removeClass("has-success");
        angular.element("select:not(#sortValue)").removeClass("has-success");
        $scope.updateSortValueDisabled = false;

    }

    $scope.sortValueBlur = function () {
        var sortValue = angular.element("#sortValue").val();
        if (null != sortValue && sortValue != "" && !/^\d+?$/.test(sortValue)) {

            $scope.updateSortValueDisabled = true;
            angular.element("#sortValue").addClass("has-error");
            angular.element("#sortValue").removeClass("has-success");

        }

    }

    var idSave = "";
    $scope.groupCodeMouseDown = function ($event) {

        var id = angular.element($event.target).attr("id");
        var size = angular.element($event.target).attr("size");
        if (size == undefined) {

            size = 7;

        }
        if (id !== undefined) {

            idSave = id;
            angular.element($event.target).attr("size", "7");


        } else {

            angular.element($event.target).attr("size", "0");

        }

        if (id == undefined || size == 7) {

            len = 1;

        } else {

            len = 0

        }

        if (len == 0) {

            var addHtml = '<div id="selectInputDiv_' + idSave + '" class="align-middle" ><img src="img/arrow/arrow.png" class="select-arrow" /><input type="text" id="selectInput" name="selectInput" class="form-control limit-height" /></div>';
            angular.element($event.target).before(addHtml);

        }

        angular.element("input:not(#groupCode)").removeClass("has-success");
        angular.element("#groupCodePrompt").removeClass("select-prompt");
        angular.element("#groupCodePrompt").addClass("no-select-prompt");

    }

    $scope.firstValueFocus = function () {

        $scope.updateIsDisabled = false;
        angular.element("#firstValue").removeClass("has-error");
        angular.element("#firstValue").addClass("has-success");
        angular.element("input:not(#firstValue)").removeClass("has-success");

    }

    $scope.firstValueBlur = function () {

        var firstValue = angular.element("#firstValue").val();
        if (null == firstValue || firstValue == "") {

            $scope.updateIsDisabled = true;
            angular.element("#firstValue").addClass("has-error");
            angular.element("#firstValue").removeClass("has-success");

        }

    }

    $scope.groupCodeChange = function () {

        var groupCode = angular.element("#groupCode").val();
        angular.element("#groupCode").attr("size", "0");
        if (null == groupCode || groupCode == "") {

            angular.element("#groupCode").addClass("has-error");
            angular.element("#groupCode").removeClass("has-success");

            angular.element("#groupCodePrompt").addClass("select-prompt");
            angular.element("#groupCodePrompt").removeClass("no-select-prompt");

            $scope.updateIsDisabled = true;

        } else {

            angular.element("#groupCode").removeClass("has-error");
            angular.element("#groupCode").addClass("has-success");

            angular.element("#groupCodePrompt").removeClass("select-prompt");
            angular.element("#groupCodePrompt").addClass("no-select-prompt");

            $scope.updateIsDisabled = false;


        }

        angular.element("input:not(#groupCode)").removeClass("has-success");
        $("#selectInputDiv_groupCode").remove();

    }


    $scope.groupCodeBlur = function () {

        var groupCode = angular.element("#groupCode").val();
        angular.element("#groupCode").attr("size", "0");
        var id = angular.element("#groupCode").attr("id");
        var len = angular.element("#selectInputDiv_" + id).length;
        if (len > 0) {

            angular.element("#selectInputDiv_" + id).remove();

        }
        if (null == groupCode || groupCode == "") {

            angular.element("#groupCode").addClass("has-error");
            angular.element("#groupCode").removeClass("has-success");
            $scope.addIsDisabled = true;
            angular.element("#groupCodePrompt").addClass("select-prompt");
            angular.element("#groupCodePrompt").removeClass("no-select-prompt");

        } else {

            angular.element("#groupCode").removeClass("has-error");
            angular.element("#groupCode").addClass("has-success");
            $scope.updateIsDisabled = false;
            angular.element("#groupCodePrompt").removeClass("select-prompt");
            angular.element("#groupCodePrompt").addClass("no-select-prompt");

        }


    }

    $scope.generalFocus = function ($event) {

        $scope.updateIsDisabled = false;
        var id = angular.element($event.target).attr("id");
        angular.element("input:not(#" + id + ")").removeClass("has-success");
        angular.element("select:not(#" + id + ")").removeClass("has-success");

    }

    $scope.updateConstant = function () {

        var obj = angular.element("#constantObjForm").serializeJson();

        httpSvc.getData('post', '/constant/update', obj).then(function (value) {

            if (value.code == 0) {

                toaster.pop({
                    type: 'success',
                    title: '系统常量编辑',
                    body: '更新成功！'
                });

            } else {

                toaster.pop({
                    type: 'error',
                    title: '系统常量编辑',
                    body: '更新失败！'
                });

            }

            $uibModalInstance.dismiss();

        });

        $scope.updateIsDisabled = true;

    }


}


// 商户提现查询
function financeDrawingCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {

    var searchInfo = {};
    $scope.searchInfo = {};

    //获取初始值和异步获取merchant_question
    httpSvc.getData('post', '/financeDrawing/getBalanceInit').then(function (value) {
        $scope.status = value.status;
        $scope.customers = value.customers;
        $scope.financeDrawingTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/financeDrawing/search', {
                    pageFirst: (params.page() - 1) * 10,
                    pageSize: params.count(),
                    searchInfo: searchInfo
                }).then(function (value) {
                    $scope.totalItems = value.resultTotal;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    });


    $scope.search = function () {
        $scope.financeDrawingTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/financeDrawing/search', {
                    pageFirst: (params.page() - 1) * 10,
                    pageSize: params.count(),
                    searchInfo: searchInfo
                }).then(function (value) {
                    $scope.totalItems = value.resultTotal;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    };


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
        }
        // else {
        //     $scope.dateOptions1.maxDate = new Date();
        // }
    });
    $scope.$watch('searchInfo.startDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions4.minDate = $scope.searchInfo.startDate2;
            $scope.endDateDisable2 = false;
        } else {
            $scope.endDateDisable2 = true;
            oldEndDate = $scope.searchInfo.endDate2;
            $scope.searchInfo.endDate2 = undefined;
        }
    });
    $scope.$watch('searchInfo.endDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions3.maxDate = $scope.searchInfo.endDate2;
        } else {
            $scope.dateOptions3.maxDate = new Date();
        }
    });
    $scope.format = 'yyyy/MM/dd';
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
    $scope.dateOptions3 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.dateOptions4 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.endDateDisable = true;
    $scope.format = 'yyyy-MM-dd';

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
    $scope.popup3 = {
        opend: false
    };
    $scope.openDatepicker3 = function () {
        $scope.popup3.opend = !$scope.popup3.opend;
    };
    $scope.popup4 = {
        opend: false
    };
    $scope.openDatepicker4 = function () {
        $scope.popup4.opend = !$scope.popup4.opend;
    };


    $scope.resetForm = function () {
        $scope.searchInfo = {};
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    };


}

//提现审核
function financeDrawingAuditCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {

    var searchInfo = {};
    $scope.searchInfo = {status: 0};

    //获取初始值和异步获取merchant_question
    httpSvc.getData('post', '/financeDrawing/getBalanceInit').then(function (value) {
        $scope.status = value.status;
        $scope.customers = value.customers;
        $scope.financeDrawingTable = new NgTableParams({}, {

            getData: function (params) {
                // angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/financeDrawing/search', {
                    pageFirst: (params.page() - 1) * 10,
                    pageSize: params.count(),
                    searchInfo: searchInfo
                }).then(function (value) {
                    $scope.totalItems = value.resultTotal;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    });


    $scope.search = function () {
        $scope.financeDrawingTable = null;
        httpSvc.getData('post', '/financeDrawing/search', $scope.searchInfo
        ).then(function (value) {
            $scope.financeDrawingTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };


    $scope.showModal = function (row) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/money/auditModel',
            controller: 'financeChangeCtrl',
            resolve: {
                row: function () {
                    return row;
                }
            }
        });
        modalInstance.result.then(function () {
            tablereload();
        }, function () {
            tablereload();
        });
    };


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
        }
        // else {
        //     $scope.dateOptions1.maxDate = new Date();
        // }
    });
    $scope.$watch('searchInfo.startDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions4.minDate = $scope.searchInfo.startDate2;
            $scope.endDateDisable2 = false;
        } else {
            $scope.endDateDisable2 = true;
            oldEndDate = $scope.searchInfo.endDate2;
            $scope.searchInfo.endDate2 = undefined;
        }
    });
    $scope.$watch('searchInfo.endDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions3.maxDate = $scope.searchInfo.endDate2;
        } else {
            $scope.dateOptions3.maxDate = new Date();
        }
    });
    $scope.format = 'yyyy/MM/dd';
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
    $scope.dateOptions3 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.dateOptions4 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.endDateDisable = true;
    $scope.format = 'yyyy-MM-dd';

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
    $scope.popup3 = {
        opend: false
    };
    $scope.openDatepicker3 = function () {
        $scope.popup3.opend = !$scope.popup3.opend;
    };
    $scope.popup4 = {
        opend: false
    };
    $scope.openDatepicker4 = function () {
        $scope.popup4.opend = !$scope.popup4.opend;
    };

    $scope.search = function () {
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    };
    $scope.resetForm = function () {
        $scope.searchInfo = {status: 0};
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    };

    tablereload = function () {
        $scope.searchInfo = {status: 0};
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    }


}

function drawingMoneyCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {

    var searchInfo = {};
    $scope.searchInfo = {status: 1};

    //获取初始值和异步获取merchant_question
    httpSvc.getData('post', '/financeDrawing/getBalanceInit').then(function (value) {
        $scope.status = value.status;
        $scope.customers = value.customers;
        $scope.financeDrawingTable = new NgTableParams({}, {

            getData: function (params) {
                // angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/financeDrawing/search', {
                    pageFirst: (params.page() - 1) * 10,
                    pageSize: params.count(),
                    searchInfo: searchInfo
                }).then(function (value) {
                    $scope.totalItems = value.resultTotal;
                    params.total(value.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.rows;
                });
            }
        });
    });


    $scope.search = function () {
        $scope.financeDrawingTable = null;
        httpSvc.getData('post', '/financeDrawing/search', $scope.searchInfo
        ).then(function (value) {
            $scope.financeDrawingTable = new NgTableParams({}, {
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };


    $scope.showModal = function (row) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/money/darawMoenyModel',
            controller: 'drawingMoneyModelCtrl',
            resolve: {
                row: function () {
                    return row;
                }
            }
        });
        modalInstance.result.then(function () {
            tablereload();
        }, function () {
            tablereload();
        });
    };


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
        }
        // else {
        //     $scope.dateOptions1.maxDate = new Date();
        // }
    });
    $scope.$watch('searchInfo.startDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions4.minDate = $scope.searchInfo.startDate2;
            $scope.endDateDisable2 = false;
        } else {
            $scope.endDateDisable2 = true;
            oldEndDate = $scope.searchInfo.endDate2;
            $scope.searchInfo.endDate2 = undefined;
        }
    });
    $scope.$watch('searchInfo.endDate2', function (newVal) {
        if (newVal !== undefined && newVal !== null) {
            $scope.dateOptions3.maxDate = $scope.searchInfo.endDate2;
        } else {
            $scope.dateOptions3.maxDate = new Date();
        }
    });
    $scope.format = 'yyyy/MM/dd';
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
    $scope.dateOptions3 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.dateOptions4 = {
        formatYear: 'yyyy',
        startingDay: 1,
        maxDate: new Date()
    };
    $scope.endDateDisable = true;
    $scope.format = 'yyyy-MM-dd';

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
    $scope.popup3 = {
        opend: false
    };
    $scope.openDatepicker3 = function () {
        $scope.popup3.opend = !$scope.popup3.opend;
    };
    $scope.popup4 = {
        opend: false
    };
    $scope.openDatepicker4 = function () {
        $scope.popup4.opend = !$scope.popup4.opend;
    };

    $scope.search = function () {
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    };
    $scope.resetForm = function () {
        $scope.searchInfo = {status: 1};
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    };

    tablereload = function () {
        $scope.searchInfo = {status: 1};
        $scope.financeDrawingTable.page(1);
        $scope.financeDrawingTable.reload();
    }


}

function drawingMoneyModelCtrl($scope, $uibModal, $state, $uibModalInstance, NgTableParams, httpSvc, toaster, row) {

    $scope.row = row;
    // 关闭
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    // 审核操作
    $scope.confirm = function (row) {
        httpSvc.getData('post', '/financeDrawing/drawMoney', row).then(function (value) {
            if (value == true) {
                toaster.pop({
                    type: 'success',
                    title: '划款成功',
                    body: '划款成功'
                });
            } else {
                toaster.pop({
                    type: 'error',
                    title: '划款失败',
                    body: '划款失败'
                });
            }
            $uibModalInstance.dismiss();
        });
    }


}

function financeChangeCtrl($scope, $uibModal, $state, $uibModalInstance, NgTableParams, httpSvc, toaster, row) {

    $scope.row = row;
    $scope.row.status = 1;
    // 关闭
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

    // 审核操作
    $scope.confirm = function (row) {
        httpSvc.getData('post', '/financeDrawing/update', row).then(function (value) {
            if (value == true) {
                toaster.pop({
                    type: 'success',
                    title: '审核成功',
                    body: '审核成功'
                });
            } else {
                toaster.pop({
                    type: 'error',
                    title: '审核失败',
                    body: '审核失败'
                });
            }
            $uibModalInstance.dismiss();
        });
    }


}


function merchantIpCtrl($scope, $uibModal, $state, NgTableParams, httpSvc, toaster) {
    $scope.selected = {};
    httpSvc.getData('post', '/customer/getInit').then(function (value) {
        $scope.agents = value.agents;
        $scope.merChants = value.merChants;
        $scope.webSiteTable = new NgTableParams({}, {

            getData: function (params) {
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/customer/searchIp', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    object: searchInfo
                }).then(function (value) {
                    // $scope.totalItems = value.total;
                    params.total(value.data.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.data.records;
                });
            }
        });

    });

    $scope.showAddModal = function (type) {
        //商户
        if (type == 0) {
            var modalInstance = $uibModal.open({
                templateUrl: '/views/merchant/merchant_ip_modal',
                controller: 'merchantIpEditCtrl',
                resolve: {
                    customers: function () {
                        return   $scope.merChants;
                    },
                    types: function () {
                        return   type;
                    }
                }
            });
            modalInstance.result.then(function () {
                tablereload();
            }, function () {
                tablereload();
            });
        }
        //代理商
        if (type == 1) {
            var modalInstance = $uibModal.open({
                templateUrl: '/views/merchant/merchant_ip_modal',
                controller: 'merchantIpEditCtrl',
                resolve: {
                    customers: function () {
                        return   $scope.agents;
                    },
                    types: function () {
                        return   type;
                    }
                }
            });
            modalInstance.result.then(function () {
                tablereload();
            }, function () {
                tablereload();
            });
        }
    };

    $scope.del = function () {
        var idList = [];
        for (var id in $scope.selected) {
            if ($scope.selected[id]) {
                idList.push(id);
            }
        }
        var title = '删除确认';
        var content =
            '<div class="text-center">' +
            '<strong>确定删除这' + idList.length+ '条数据吗？</strong>' +
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
            httpSvc.getData('post', '/customer/delIp', idList).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: 'IP管理',
                        body: '删除IP成功！'
                    });
                } else {

                    toaster.pop({
                        type: 'warning',
                        title: 'IP管理',
                        body: '删除IP失败！',
                    });
                }
                $scope.selected = {};
                tablereload();
            });
        }, function () {
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

    tablereload=function(){
        $scope.webSiteTable = new NgTableParams({}, {
            getData: function (params) {
                // angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/customer/searchIp', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    object: searchInfo
                }).then(function (value) {
                    // $scope.totalItems = value.total;
                    params.total(value.data.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.data.records;
                });
            }
        });
    };
    $scope.search = function () {
        $scope.webSiteTable = new NgTableParams({}, {
            getData: function (params) {
                angular.element('.ibox-content').addClass('sk-loading');
                searchInfo = angular.copy($scope.searchInfo);
                return httpSvc.getData('post', '/customer/searchIp', {
                    pageNum: params.page(),
                    pageSize: params.count(),
                    object: searchInfo
                }).then(function (value) {
                    // $scope.totalItems = value.total;
                    params.total(value.data.total);
                    angular.element('.ibox-content').removeClass('sk-loading');
                    return value.data.records;
                });
            }
        });
    };
    $scope.resetForm = function () {
        $scope.searchInfo={};
        $scope.webSiteTable.page(1);
        $scope.webSiteTable.reload();
    };
}

function merchantIpEditCtrl($scope, $uibModal, $state,$uibModalInstance, NgTableParams, httpSvc, toaster,customers,types) {
    $scope.customers=customers;
    $scope.types = types;
// 关闭
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    $scope.confirm = function (webSiteInfo) {
            httpSvc.getData('post', '/customer/insertIp', webSiteInfo).then(function (value) {
                if (value.code == 0) {
                    toaster.pop({
                        type: 'success',
                        title: 'IP管理',
                        body: '添加IP成功！'
                    });
                } else {
                    toaster.pop({
                        type: 'error',
                        title: 'IP管理',
                        body: '添加IP失败！存在重复IP'
                    });
                }
                $uibModalInstance.dismiss();
            });
    };
}

angular
    .module('inspinia')
    .controller('mainCtrl', mainCtrl)
    .controller('translateCtrl', translateCtrl)
    .controller('homePageCtrl', homePageCtrl)
    .controller('userMgmtCtrl', userMgmtCtrl)
    .controller('roleMgmtCtrl', roleMgmtCtrl)
    .controller('userRegisterModalCtrl', userRegisterModalCtrl)
    .controller('userChangeModalCtrl', userChangeModalCtrl)
    .controller('roleModalCtrl', roleModalCtrl)
    .controller('systemLogCtrl', systemLogCtrl)
    .controller('systemLogModalCtrl', systemLogModalCtrl)
    .controller('agentSystemLogCtrl', agentSystemLogCtrl)
    .controller('merchantSystemLogCtrl', merchantSystemLogCtrl)
    .controller('alertModalCtrl', alertModalCtrl)
    .controller('constantInfoCtrl', constantInfoCtrl)
    .controller('constantInfoAddModalCtrl', constantInfoAddModalCtrl)
    .controller('constantInfoEditModalCtrl', constantInfoEditModalCtrl)
    .controller('sysGroupInfoCtrl', sysGroupInfoCtrl)
    .controller('sysGroupInfoAddModalCtrl', sysGroupInfoAddModalCtrl)
    .controller('sysGroupInfoEditModalCtrl', sysGroupInfoEditModalCtrl)
    .controller('financeDrawingCtrl', financeDrawingCtrl)
    .controller('financeDrawingAuditCtrl', financeDrawingAuditCtrl)
    .controller('financeChangeCtrl', financeChangeCtrl)
    .controller('drawingMoneyCtrl', drawingMoneyCtrl)
    .controller('drawingMoneyModelCtrl', drawingMoneyModelCtrl)
    .controller('merchantIpCtrl', merchantIpCtrl)
    .controller('merchantIpEditCtrl', merchantIpEditCtrl)
    .controller('refreshCacheCtrl', refreshCacheCtrl)

;

function getDateStr(AddDayCount) {
    var today = new Date();
    today.setDate(today.getDate() + AddDayCount);// 获取AddDayCount天后的日期
    var y = today.getFullYear();
    var m = today.getMonth() + 1;// 获取当前月份的日期
    var d = today.getDate();
    return y + "-" + m + "-" + d;
}

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

function inputRegex(input, regex, replacement) {
    if (replacement === undefined || replacement === null)
        replacement = '';
    input.value = input.value.replace(regex, replacement);
}

function myRegex($event, regex, flags, replacement) {
    if (replacement === undefined || replacement === null)
        replacement = '';
    var reg = new RegExp(regex, flags);
    $event.target.value = $event.target.value.replace(reg, replacement);
}

function toPercent(point) {
    if (isNaN(point)) {
        return "0%";
    } else {
        var str = Number(point * 100).toFixed(2);
        str += "%";
        return str;
    }
}

//重复确认
function confirmd() {
    var msg = "您真的确定要删除吗？";
    if (confirm(msg) == true) {
        return true;
    } else {
        return false;
    }
}

//删除确认
function delModal() {
    return $uibModal.open({
        templateUrl: '/views/common/delete_modal',
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
}

function getSiteIdByUrl(url, siteUrls) {
    if (url.hasOwnProperty("id")) {
        return url.id;
    }
    else {
        for (var item in siteUrls) {
            if (siteUrls[item].siteUrl == url) {
                return siteUrls[item].id;
            }
        }
    }
}


