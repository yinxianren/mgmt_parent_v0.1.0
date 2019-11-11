
// 支付通道管理
function paymentPassagewayCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.searchInfo = angular.copy(defaultSearch);
    var init;
    $scope.selected = {};
    var OrganizationInfo;

    httpSvc.getData('post', '/organization/init').then(function (value) {

        // $scope.organizations= value.organizations;
        $scope.status= value.status;
    httpSvc.getData('post', '/organization/getAll',searchInfo).then(function (value1) {
        OrganizationInfo = value1.data;
        $scope.OrganizationTable = new NgTableParams({}, {
            dataset: value1.data
        });
        angular.element('.ibox-content').removeClass('sk-loading');
    });
    });
    //更新状态
    $scope.statusChange = function ($event, row) {
        httpSvc.getData('post', '/organization/update', {
            organizationId: row.organizationId,
            id: row.id,
            status: row.status ? 0 : 1
        }).then(function (value) {
            if (value) {
                toaster.pop({
                    type: 'success',
                    title: '支付机构',
                    body: '更新支付机构状态成功！'
                });
                angular.element($event.target).toggleClass('active');
                angular.element($event.target).toggleClass('btn-primary');
                angular.element($event.target).toggleClass('btn-disabled');
                angular.element($event.target).hasClass('active') ?
                    angular.element($event.target).text('启用') :
                    angular.element($event.target).text('禁用');
            } else {
                toaster.pop({
                    type: 'error',
                    title: '支付机构',
                    body: '更新支付机构状态失败！'
                });
            }
        });
    };



    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/organization/getAll',defaultSearch).then(function (value) {
            $scope.OrganizationTable.settings({
                dataset: value.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    $scope.reset=function () {
        $scope.searchInfo = angular.copy(defaultSearch);
        tableReload();
    }
    $scope.search= function () {
        searchInfo = angular.copy($scope.searchInfo);
        if($scope.searchInfo!=defaultSearch){
            console.log(searchInfo);
            httpSvc.getData('post', '/organization/getAll',searchInfo).then(function (value1) {
                OrganizationInfo = value1.data;
                $scope.OrganizationTable = new NgTableParams({}, {
                    dataset: value1.data
                });
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        }else {
            $scope.searchInfo = angular.copy(defaultSearch);
            tableReload();
        }

    };

    $scope.showModal = $scope.edit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/OrganizationAdd',
            controller: 'OrganizationAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

   $scope.productEdit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/productAdd',
            controller: 'productAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

    $scope.bankEdit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/bank_add',
            controller: 'bankAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
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
            httpSvc.getData('post', '/organization/delete', idList).then(function (value) {
                if (!value.code) {
                    toaster.pop({
                        type: 'success',
                        title: '支付机构',
                        body: '删除支付机构成功！'
                    });
                    tableReload();
                } else {
                    toaster.pop({
                        type: 'warning',
                        title: '支付机构',
                        body: '支付机构无法删除！',
                        timeout: 0
                    });
                }
                $scope.selected = {};
            });
        }, function () {
        });
    };



}

function OrganizationAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, Organization) {
    $scope.type = type;
    if (type === 1) {
        $scope.Organization = angular.copy(Organization);
    }
    httpSvc.getData('post', '/organization/init', JSON.stringify('Status')).then(function (value) {
        $scope.status = value.status;
        $scope.Organizations = value.organizations;
    });
    $scope.nameBlur = $scope.remarkBlur = $scope.elementBlur=function ($event, remark) {
        verification(remark, $event.target);
    };
    // $scope.$watch('Organization.status', function (newVal) {
    //     if (newVal !== undefined) {
    //         angular.element('#bank-status').parent().removeClass('has-error');
    //         angular.element('#bank-status').parent().addClass('has-success');
    //     }
    // });

    $scope.exists = function (value, selected) {
        if (value === undefined || selected === undefined) {
            return false;
        }
        var checkab = selected.split(",") ;
        var flag = false;
        for(j = 0; j < checkab.length; j++) {
            if (checkab[j] == value){
                flag =true;
            }
        }
        return flag;
    };
    $scope.orgtoggle = function (value) {
        var index = orgpayids.indexOf(value);
        if (index > -1) {
            orgpayids.splice(index, 1);
        } else {
            orgpayids.push(value);
        }
    };
    $scope.addOrganization = function () {
        var orgpayids=[];
        $.each($("input[name='productIds']:checked"),function(){
            orgpayids.push($(this).val());
        });
        $scope.Organization.productIds = orgpayids.join(",");
        if (type === 0) {
            httpSvc.getData('post', '/organization/insert', $scope.Organization).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付机构管理',
                        body: '支付机构添加成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付机构管理',
                        body: '支付机构添加失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/organization/update', $scope.Organization).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付机构管理',
                        body: '支付机构修改成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付机构管理',
                        body: '支付机构修改失败！'
                    });
                }
            });
        }
    };
    // $scope.nextDisabled = function (OrganizationObjForm) {
    //     return !(OrganizationObjForm.organizationName.$valid && OrganizationObjForm.productIds.$valid  && !angular.equals($scope.Organization, Organization));
    // };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}

function productAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, Organization) {
    $scope.type = type;
    $scope.firstValue =null;
    if (type === 1) {
        $scope.Product = angular.copy(Organization);
    }
    httpSvc.getData('post', '/product/getProductAll').then(function (value) {
        $scope.products = value.data;
    });
    $scope.nameBlur = $scope.remarkBlur = $scope.elementBlur=function ($event, remark) {
        verification(remark, $event.target);
    };
    // $scope.$watch('Organization.status', function (newVal) {
    //     if (newVal !== undefined) {
    //         angular.element('#bank-status').parent().removeClass('has-error');
    //         angular.element('#bank-status').parent().addClass('has-success');
    //     }
    // });

    $scope.addProduct = function () {
        if (type === 0) {
            httpSvc.getData('post', '/product/addProduct', $scope.Product).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付产品管理',
                        body: '支付机构添加成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付产品管理',
                        body: '支付产品添加失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/product/addProduct', $scope.Product).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付产品管理',
                        body: '支付产品修改成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付产品管理',
                        body: '支付机构修改失败！'
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}

function bankAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type, Organization) {
    $scope.type = type;
    $scope.firstValue =null;
    if (type === 1) {
        $scope.Product = angular.copy(Organization);
    }
    httpSvc.getData('post', '/product/getProductAll').then(function (value) {
        $scope.products = value.data;
    });
    $scope.nameBlur = $scope.remarkBlur = $scope.elementBlur=function ($event, remark) {
        verification(remark, $event.target);
    };
    // $scope.$watch('Organization.status', function (newVal) {
    //     if (newVal !== undefined) {
    //         angular.element('#bank-status').parent().removeClass('has-error');
    //         angular.element('#bank-status').parent().addClass('has-success');
    //     }
    // });

    $scope.addProduct = function () {
        if (type === 0) {
            httpSvc.getData('post', '/product/addProduct', $scope.Product).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付产品管理',
                        body: '支付机构添加成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付产品管理',
                        body: '支付产品添加失败！'
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/product/addProduct', $scope.Product).then(function (value) {
                if (value) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '支付产品管理',
                        body: '支付产品修改成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '支付产品管理',
                        body: '支付机构修改失败！'
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
}


// 下发通道管理
function channelInfoCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.searchInfo = angular.copy(defaultSearch);
    var init;
    $scope.selected = {};
    var ChannelInfo;
    $scope.outChannels = [];
    httpSvc.getData('post', '/ChannelInfo/getInit').then(function (value) {
        init = value.data;
        $scope.organizations = value.organizations;
        $scope.paytype = value.paytype;
        $scope.status = value.status;
        $scope.channelLevel = value.channelLevel;
        $scope.productTypes = value.productTypes;
        httpSvc.getData('post', '/ChannelInfo/search',searchInfo).then(function (value1) {
            ChannelInfo = value1.data;
            $scope.ChannelInfoTable = new NgTableParams({}, {
                dataset: value1.data
            });
            // 代付通道
            // initOutChannels(value1);
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });
    // function  initOutChannels(value1){
    //     $scope.outChannels = [];
    //     /*for(var i=0;i<value1.length;i++){
    //         if (value1[i].type === 4){
    //             $scope.outChannels.push(value1[i]);
    //         }
    //     }*/
    //     $scope.outChannels = angular.copy(value1);
    //     console.log($scope.outChannels);
    // }

    $scope.reset=function () {
        $scope.searchInfo = angular.copy(defaultSearch);
        tableReload();
    }

    function tableReload() {
        $scope.channelList = [];
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/ChannelInfo/getAll').then(function (value) {
            $scope.ChannelInfoTable.settings({
                dataset: value.data
            });
            // initOutChannels(value.data);
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    $scope.showModal = $scope.edit = function ( type,ChannelInfo) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/channelInfo_modal',
            controller: 'channelInfoModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                ChannelInfo: function () {
                    return ChannelInfo;
                } ,
                init: function () {
                    return init;
                },
                outChannels: function () {
                    return $scope.outChannels;
                }
            }

        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

    //更新状态
    $scope.statusChange = function ($event, row) {
        row.status = row.status ? 0 : 1;
        var param = {};
        param = angular.copy(row);
        console.log(param);
        httpSvc.getData('post', '/ChannelInfo/update', param).then(function (value) {
            if (value) {
                toaster.pop({
                    type: 'success',
                    title: '支付通道',
                    body: '更新支付通道状态成功！'
                });
                angular.element($event.target).toggleClass('active');
                angular.element($event.target).toggleClass('btn-primary');
                angular.element($event.target).toggleClass('btn-disabled');
                angular.element($event.target).hasClass('active') ?
                    angular.element($event.target).text('启用') :
                    angular.element($event.target).text('禁用');
                tableReload();
            } else {
                toaster.pop({
                    type: 'error',
                    title: '支付通道',
                    body: '更新支付通道状态失败！'
                });
            }
        });
    };

    $scope.showInfo = function (Info) {
        var modalSize = 'lg';

        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/show_jsonParse_modal',
            controller: 'showJsonParseModalCtrl',
            size: modalSize,
            resolve: {
                info: function () {
                    return Info;
                }
            }
        });
        modalInstance.result.then(function () {

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
            httpSvc.getData('post', '/ChannelInfo/delete', idList).then(function (value) {
                if (value.code == 0) {
                    toaster.pop({
                        type: 'success',
                        title: '支付通道',
                        body: '删除支付通道成功！'
                    });
                    tableReload();
                } else {
                    var toasterBody = '';
                    for (var i = 0; i < value.length; i++) {
                        for (var j = 0; j < bank.length; j++) {
                            if (value[i] === bank[j].id) {
                                toasterBody += bank[j].name;
                                toasterBody += ';';
                                break;
                            }
                        }
                    }
                    toaster.pop({
                        type: 'error',
                        title: '支付通道',
                        body: '支付支付通道删除失败！',
                    });
                }
                $scope.selected = {};
            });
        }, function () {
        });
    };


    $scope.search= function () {
        searchInfo = angular.copy($scope.searchInfo);
        console.log(searchInfo);
        httpSvc.getData('post', '/ChannelInfo/search',searchInfo).then(function (value1) {
            ChannelInfo = value1.data;
            $scope.ChannelInfoTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };
}


function channelInfoModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type,init, ChannelInfo,outChannels) {
    $scope.checkOutChannelId = false;
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.outChannels = [];
    $scope.daiFuList = [];
    if (outChannels){
        $scope.outChannels = outChannels;
        for(var i=0;i<outChannels.length;i++){
            if (outChannels[i].type === 4){
                $scope.daiFuList.push(outChannels[i]);
            }
        }
    }
    $scope.searchInfo = angular.copy(defaultSearch);
    $scope.type = type;
    /*$scope.organizations = init.organizations;
    $scope.paytype = init.paytype;
    $scope.status = init.status;
    $scope.channelLevel = init.channelLevel;*/
    httpSvc.getData('post', '/ChannelInfo/getInit').then(function (value) {
        init = value.data;
        $scope.organizations = value.organizations;
        $scope.paytype = value.paytype;
        $scope.status = value.status;
        $scope.channelLevel = value.channelLevel;
        $scope.busiTypes = value.busiTypes;
    });
    if (type === 1) {
        $scope.ChannelInfo = angular.copy(ChannelInfo);
        httpSvc.getData('get', '/product/getProductList',{
            organizationId: ChannelInfo.organizationId
        }).then(function (value) {
            $scope.products = value.data;
        });
        checkChannelTransCodeUpdate();
    }else  {
        $scope.ChannelInfo = {others:'{}'};
    }
    $scope.$watch('Organization.status', function (newVal) {
        if (newVal !== undefined) {
            angular.element('#bank-status').parent().removeClass('has-error');
            angular.element('#bank-status').parent().addClass('has-success');
        }
    });
    $scope.checkChannelTransCode =  function(){
        checkChannelTransCodeUpdate();
    }
    $scope.checkChannelCode = function (value) {
        httpSvc.getData('get', '/product/getProductList',{
            organizationId: value
        }).then(function (value) {
            $scope.products = value.data;
        });
    }
    function checkChannelTransCodeUpdate(){

        for (var i=0; i<$scope.outChannels.length;i++){
            console.log($scope.outChannels[i].outChannelId);
            // 修改收单通道的商户号
            if($scope.ChannelInfo.type != 4 && !$scope.ChannelInfo.outChannelId){ // 4： 代付
                $scope.checkOutChannelId = false;
                return;
            }
            if ($scope.ChannelInfo.outChannelId == $scope.outChannels[i].channelId){
                if($scope.ChannelInfo.channelTransCode != $scope.outChannels[i].channelTransCode){
                    $scope.checkOutChannelId = true;
                    break;
                }else if ($scope.ChannelInfo.channelTransCode == $scope.outChannels[i].channelTransCode){
                    $scope.checkOutChannelId = false;
                    break;
                }
            }
            // 修改代付通道商户号
            if($scope.ChannelInfo.type == 4){ // 4： 代付
               for (var j=0; j<$scope.outChannels.length;j++){
                   // 遍历收单机构为改机构的通道(如果收单机构有多个，则取第一个)，验证支付商户号是否一致
                    if ($scope.ChannelInfo.channelId == $scope.outChannels[j].outChannelId){
                        if ($scope.ChannelInfo.channelTransCode != $scope.outChannels[j].channelTransCode){
                            $scope.checkOutChannelId = true;
                            return;
                        } else {
                            $scope.checkOutChannelId = false;
                            return;
                        }
                    }
               }
            }
        }
    }
    $scope.nameBlur = $scope.remarkBlur =$scope.element= function ($event, remark) {
        verification(remark, $event.target);
    };
    $scope.addOrganization = function (myForm, activeForm) {
        switch (activeForm) {
            case 0:
                $scope.activeForm++;
                break;
            case 1:
                if (type === 0) {
                    console.log(myForm);
                    httpSvc.getData('post', '/ChannelInfo/insert', myForm).then(function (value) {
                        if (!value.code) {
                            $scope.bankInfo = null;
                            toaster.pop({
                                type: 'success',
                                title: '支付通道',
                                body: '支付通道成功！'
                            });
                            $uibModalInstance.close();
                        } else {
                            toaster.pop({
                                type: 'error',
                                title: '支付通道',
                                body: '支付通道添加失败！' +
                                    '可能该通道已存在'
                            });
                        }
                    });
                } else {
                    httpSvc.getData('post', '/ChannelInfo/update', $scope.ChannelInfo).then(function (value) {
                        if (!value.code) {
                            $scope.bankInfo = null;
                            toaster.pop({
                                type: 'success',
                                title: '支付通道',
                                body: '支付通道修改成功！'
                            });
                            $uibModalInstance.close();
                        } else {
                            toaster.pop({
                                type: 'error',
                                title: '支付通道',
                                body: '支付通道修改失败！'
                            });
                        }
                    });
                }
                break;
        }

    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };

}

// 附属通道管理
function attachPassagewayMgmtCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.searchInfo = angular.copy(defaultSearch);
    var init;
    $scope.selected = {};
    var extraChannelInfo;
    $scope.channelInfoList = [];
    $scope.extraTypes = [];


    httpSvc.getData('post', '/ExtraChannelInfo/getInit').then(function (value) {
        init = value.data;
        $scope.organizations = value.organizations;
        $scope.paytype = value.paytype;
        $scope.status = value.status;
        $scope.channelLevel = value.channelLevel;
        $scope.channelInfoList = value.channelInfoList;
        $scope.extraTypes = value.extraTypes;
        console.log($scope.channelInfoList);
        httpSvc.getData('post', '/ExtraChannelInfo/search',searchInfo).then(function (value1) {
            extraChannelInfo = value1.data;
            $scope.extraChannelInfoTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });

    $scope.reset=function () {
        $scope.searchInfo = angular.copy(defaultSearch);
        tableReload();
    }

    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/ExtraChannelInfo/getAll').then(function (value) {
            $scope.extraChannelInfoTable.settings({
                dataset: value.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    $scope.showModal = $scope.edit = function ( type,ExtraChannelInfo) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/ExtraChannelInfo_modal',
            controller: 'ExtraChannelInfoAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                ExtraChannelInfo: function () {
                    return ExtraChannelInfo;
                } ,
                init: function () {
                    return init;
                }
            }

        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

    //更新状态
    $scope.statusChange = function ($event, row) {
        httpSvc.getData('post', '/ExtraChannelInfo/update', {
            id: row.id,
            status: row.status ? 0 : 1
        }).then(function (value) {
            if (!value.code) {
                toaster.pop({
                    type: 'success',
                    title: '附属通道',
                    body: '更新附属通道状态成功！'
                });
                angular.element($event.target).toggleClass('active');
                angular.element($event.target).toggleClass('btn-primary');
                angular.element($event.target).toggleClass('btn-disabled');
                angular.element($event.target).hasClass('active') ?
                    angular.element($event.target).text('启用') :
                    angular.element($event.target).text('禁用');
            } else {
                toaster.pop({
                    type: 'error',
                    title: '附属通道',
                    body: '更新附属通道状态失败！'
                });
            }
        });
    };

    $scope.showInfo = function (Info) {
        var modalSize = 'lg';

        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/show_jsonParse_modal',
            controller: 'showJsonParseModalCtrl',
            size: modalSize,
            resolve: {
                info: function () {
                    return Info;
                }
            }
        });
        modalInstance.result.then(function () {

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
            httpSvc.getData('post', '/ExtraChannelInfo/delete', idList).then(function (value) {
                if (!value.code) {
                    toaster.pop({
                        type: 'success',
                        title: '附属通道',
                        body: '删除附属通道成功！'
                    });
                    tableReload();
                } else {
                    var toasterBody = '';
                    for (var i = 0; i < value.length; i++) {
                        for (var j = 0; j < bank.length; j++) {
                            if (value[i] === bank[j].id) {
                                toasterBody += bank[j].name;
                                toasterBody += ';';
                                break;
                            }
                        }
                    }
                    toaster.pop({
                        type: 'warning',
                        title: '附属通道',
                        body: '删除附属通道失败！',
                        timeout: 0
                    });
                }
                $scope.selected = {};
            });
        }, function () {
        });
    };


    $scope.search= function () {
        searchInfo = angular.copy($scope.searchInfo);
        console.log(searchInfo);
        httpSvc.getData('post', '/ExtraChannelInfo/search',searchInfo).then(function (value1) {
            extraChannelInfo = value1.data;
            $scope.extraChannelInfoTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    };
}
function ExtraChannelInfoAddModalCtrl($scope, $uibModalInstance, httpSvc, toaster, type,init, ExtraChannelInfo) {
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.searchInfo = angular.copy(defaultSearch);
    $scope.type = type;
    httpSvc.getData('post', '/ExtraChannelInfo/getInit').then(function (value) {
        init = value.data;
        $scope.organizations = value.organizations;
        $scope.extraTypes = value.extraTypes;
    });
    // $scope.organizations  = [];
    // $scope.organizations = init.organizations;
    // $scope.paytype = init.paytype;
    // $scope.status = init.status;
    // 通道类型
    if (type === 1) {
        $scope.ExtraChannelInfo = angular.copy(ExtraChannelInfo);
    }else  {
        $scope.ExtraChannelInfo = {};
    }
    $scope.nameBlur = $scope.remarkBlur = $scope.extraChannelNameBlur = $scope.element= function ($event, remark) {
        verification(remark, $event.target);
    };
    $scope.$watch('Organization.status', function (newVal) {
        if (newVal !== undefined) {
            angular.element('#bank-status').parent().removeClass('has-error');
            angular.element('#bank-status').parent().addClass('has-success');
        }
    });
    $scope.addOrganization = function (myForm) {
        if (type === 0) {
            httpSvc.getData('post', '/ExtraChannelInfo/insert', myForm).then(function (value) {
                if (value.code == 0) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '附属通道',
                        body: '附属通道成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '附属通道',
                        body: value.msg
                    });
                }
            });
        } else {
            httpSvc.getData('post', '/ExtraChannelInfo/update', myForm).then(function (value) {
                if (value.code == 0) {
                    $scope.bankInfo = null;
                    toaster.pop({
                        type: 'success',
                        title: '附属通道',
                        body: '附属通道修改成功！'
                    });
                    $uibModalInstance.close();
                } else {
                    toaster.pop({
                        type: 'error',
                        title: '附属通道',
                        body: value.msg
                    });
                }
            });
        }
    };
    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
    //查询

}


// 下发银行参数弹窗
function showJsonParseModalCtrl($scope, $uibModalInstance, info) {
    var value=info;
    value.replace()
    $scope.Info={};
    $scope.Info = angular.copy(info);
    $scope.messageJson =angular.copy(info);
    if (isJSON($scope.messageJson)){
    //     // 将超过15位的数字替换成字符串再转json对象
    //     $scope.messageJson = angular.fromJson($scope.Info.replace(/(\d{22,})/g, '"$1"'));
        $scope.messageJson = angular.fromJson($scope.Info);
    }
    $scope.cancel = function () {
        $uibModalInstance.dismiss()
    };
    function isJSON (str) {
        // if (pass_object && isObject(str)) return true;

        // if (!(str instanceof String)) return false;

        str = str.replace(/\s/g, '').replace(/\n|\r/, '');

        if (/^\{(.*?)\}$/.test(str))
            return /"(.*?)":(.*?)/g.test(str);

        if (/^\[(.*?)\]$/.test(str)) {
            return str.replace(/^\[/, '')
                .replace(/\]$/, '')
                .replace(/},{/g, '}\n{')
                .split(/\n/)
                .map(function (s) { return isJSON(s); })
                .reduce(function (prev, curr) { return !!curr; });
        }
        return false;
    }
}
// 支付通道管理
function paymentPassageBankCtrl($scope, $uibModal, toaster, NgTableParams, httpSvc,$filter,csvExp) {
    var defaultSearch = {};
    var searchInfo = angular.copy(defaultSearch);
    $scope.searchInfo = angular.copy(defaultSearch);
    var init;
    $scope.selected = {};
    var OrganizationInfo;

    httpSvc.getData('post', '/bankRate/init').then(function (value) {

        // $scope.organizations= value.organizations;
        $scope.status= value.status;
        httpSvc.getData('post', '/bankRate/search',searchInfo).then(function (value1) {
            OrganizationInfo = value1.data;
            $scope.OrganizationTable = new NgTableParams({}, {
                dataset: value1.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    });
    //更新状态
    $scope.statusChange = function ($event, row) {
        httpSvc.getData('post', '/bankRate/update', {
            organizationId: row.organizationId,
            id: row.id,
            status: row.status ? 0 : 1
        }).then(function (value) {
            if (value) {
                toaster.pop({
                    type: 'success',
                    title: '支付机构',
                    body: '更新支付机构状态成功！'
                });
                angular.element($event.target).toggleClass('active');
                angular.element($event.target).toggleClass('btn-primary');
                angular.element($event.target).toggleClass('btn-disabled');
                angular.element($event.target).hasClass('active') ?
                    angular.element($event.target).text('启用') :
                    angular.element($event.target).text('禁用');
            } else {
                toaster.pop({
                    type: 'error',
                    title: '支付机构',
                    body: '更新支付机构状态失败！'
                });
            }
        });
    };



    function tableReload() {
        angular.element('.ibox-content').addClass('sk-loading');
        httpSvc.getData('post', '/bankRate/search',defaultSearch).then(function (value) {
            $scope.OrganizationTable.settings({
                dataset: value.data
            });
            angular.element('.ibox-content').removeClass('sk-loading');
        });
    }

    $scope.reset=function () {
        $scope.searchInfo = angular.copy(defaultSearch);
        tableReload();
    }
    $scope.search= function () {
        searchInfo = angular.copy($scope.searchInfo);
        if($scope.searchInfo!=defaultSearch){
            console.log(searchInfo);
            httpSvc.getData('post', '/bankRate/getAll',searchInfo).then(function (value1) {
                OrganizationInfo = value1.data;
                $scope.OrganizationTable = new NgTableParams({}, {
                    dataset: value1.data
                });
                angular.element('.ibox-content').removeClass('sk-loading');
            });
        }else {
            $scope.searchInfo = angular.copy(defaultSearch);
            tableReload();
        }

    };

    $scope.showModal = $scope.edit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/OrganizationAdd',
            controller: 'OrganizationAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

    $scope.productEdit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/productAdd',
            controller: 'productAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
    };

    $scope.bankEdit = function ( type,Organization) {
        var modalInstance = $uibModal.open({
            templateUrl: '/views/passageway/bank_add',
            controller: 'bankAddModalCtrl',
            resolve: {
                type: function () {
                    return type;
                },
                Organization: function () {
                    return Organization;
                }
            }
        });
        modalInstance.result.then(function () {
            tableReload()
        }, function () {
            tableReload()
        });
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
            httpSvc.getData('post', '/organization/delete', idList).then(function (value) {
                if (!value.code) {
                    toaster.pop({
                        type: 'success',
                        title: '支付机构',
                        body: '删除支付机构成功！'
                    });
                    tableReload();
                } else {
                    toaster.pop({
                        type: 'warning',
                        title: '支付机构',
                        body: '支付机构无法删除！',
                        timeout: 0
                    });
                }
                $scope.selected = {};
            });
        }, function () {
        });
    };



}
angular
    .module('inspinia')
    .controller('paymentPassagewayCtrl', paymentPassagewayCtrl)
    .controller('paymentPassageBankCtrl', paymentPassageBankCtrl)
    .controller('OrganizationAddModalCtrl', OrganizationAddModalCtrl)
    .controller('productAddModalCtrl', productAddModalCtrl)
    .controller('bankAddModalCtrl', bankAddModalCtrl)
    .controller('channelInfoModalCtrl', channelInfoModalCtrl)
    .controller('channelInfoCtrl', channelInfoCtrl)
    .controller('attachPassagewayMgmtCtrl', attachPassagewayMgmtCtrl)
    .controller('ExtraChannelInfoAddModalCtrl', ExtraChannelInfoAddModalCtrl)
    .controller('showJsonParseModalCtrl', showJsonParseModalCtrl)
;
