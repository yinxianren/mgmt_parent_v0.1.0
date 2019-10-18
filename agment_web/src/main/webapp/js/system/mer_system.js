/**
 * translateCtrl - Controller for translate
 */
// 角色管理
function roleMgmtCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
    httpSvc.getData('post', '/role/getPrivilegeList').then(function (value) {
        $scope.privilegeList = value;
    });
    httpSvc.getData('post', '/role/getRoleList').then(function (value) {
        for (var i = 0; i < value.length; i++) {
            if (value[i].privilegesId != null && value[i].privilegesId !== '') {
                value[i]['privilegeIdList'] = value[i].privilegesId.replace(/[^\d,]/g, '').split(',');
            }
        }
        $scope.roleTable = new NgTableParams({}, {
            dataset: value
        });
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    $scope.showModal = function (type, roleInfo) {
        var modalInstance;
        if (type !== 2) {
            modalInstance = $uibModal.open({
                templateUrl: '/views/system/role_modal',
                controller: 'roleMgmtModalCtrl',
                resolve: {
                    roleInfo: function () {
                        return roleInfo;
                    },
                    privilegeList: function () {
                        return $scope.privilegeList;
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
                httpSvc.getData('post', '/role/delete', JSON.stringify(roleInfo.roleName)).then(function (value) {
                    tableReload();
                    if (value) {
                        toaster.pop({
                            type: 'success',
                            title: '角色管理',
                            body: '删除角色成功！'
                        });
                    } else {
                        toaster.pop({
                            type: 'error',
                            title: '角色管理',
                            body: '删除角色失败！'
                        });
                    }
                });
            }, function () {
            });
        }
    };

    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/user/getRoleList').then(function (value) {
            for (var i = 0; i < value.length; i++) {
                if (value[i].privilegesId != null && value[i].privilegesId !== '') {
                    value[i]['privilegeIdList'] = value[i].privilegesId.replace(/[^\d,]/g, '').split(',');
                }
            }
            $scope.roleTable.settings({
                dataset: value
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }
}

// 角色管理模态框
function roleMgmtModalCtrl($scope, $uibModalInstance, toaster, httpSvc, roleInfo, type, privilegeList) {
    $scope.privilegeList = [];
    for (var i = 0; i < privilegeList.length; i++) {
        if (privilegeList[i].parentId === undefined) {
            var privilege = privilegeList[i];
            privilege['submenu'] = [];
            for (var j = 0; j < privilegeList.length; j++) {
                if (privilegeList[j].parentId === privilegeList[i].id) {
                    privilege.submenu.push(privilegeList[j]);
                }
            }
            $scope.privilegeList.push(privilege);
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
    $scope.type = type;
    if (type === 0) {
        $scope.exist = true;
        $scope.nameValid = false;
        $scope.roleInfo = {privilegeIdList: []};
        $scope.nameBlur = function ($event, roleName) {
            httpSvc.getData('post', '/role/isExist', {roleName: roleName}).then(function (value) {
                if (value) {
                    verification(null, $event.target);
                    $scope.exist = true;
                } else {
                    verification(roleName, $event.target);
                    $scope.exist = false;
                }
            });
        };
    } else {
        $scope.exist = false;
        $scope.roleInfo = angular.copy(roleInfo);
        if ($scope.roleInfo.privilegeIdList == null) {
            $scope.roleInfo.privilegeIdList = [];
        }
    }
    $scope.confirm = function () {
        $scope.roleInfo.privilegesId = $scope.roleInfo.privilegeIdList.toString();
        if (type === 0) {
            httpSvc.getData('post', '/role/add', $scope.roleInfo).then(function (value) {
                if (value) {
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
            httpSvc.getData('post', '/role/update', $scope.roleInfo).then(function (value) {
                if (value) {
                    toaster.pop({
                        type: 'success',
                        title: '角色管理',
                        body: '修改角色成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '角色管理',
                        body: '修改角色失败！'
                    });
                }
            });
        }
    };
    $scope.confirmDisabled = function (myForm) {
        return type === 0 ? !myForm.$valid || $scope.exist : false;
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}


// 用户管理
function merUserMgmtCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc) {
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
function merSecretKeyChangeCtrl($scope, httpSvc, toaster) {
    httpSvc.getData('post', '/merchant/getMerchantPay').then(function (value) {
        $scope.merchantPay = value;
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    $scope.confirm = function (newKey, userPassword) {
        var merchantPay = angular.copy($scope.merchantPay);
        merchantPay.secretKey = newKey;
        merchantPay.modifier = userPassword;
        httpSvc.getData('post', '/merchant/updateMerchantPay', merchantPay).then(function (value) {
            if (value) {
                $scope.newSecretKey = null;
                $scope.userPassword = null;
                toaster.pop({
                    type: 'success',
                    title: '密钥修改',
                    body: '修改成功！'
                });
                $scope.merchantPay.secretKey = newKey;
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
        httpSvc.getData('post', '/merchant/getRandomSecretkey').then(function (value) {
            $scope.newSecretKey = value;
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }


}



angular
    .module('inspinia')
    .controller('merUserMgmtCtrl', merUserMgmtCtrl)
    .controller('merUserMgmtModalCtrl', merUserMgmtModalCtrl)
    .controller('roleMgmtCtrl', roleMgmtCtrl)
    .controller('roleMgmtModalCtrl', roleMgmtModalCtrl)
    .controller('merSecretKeyChangeCtrl', merSecretKeyChangeCtrl)
;
