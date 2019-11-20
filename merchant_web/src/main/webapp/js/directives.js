/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function (scope, element) {
            var listener = function (event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = 'INSPINIA | Responsive Admin Theme';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle) title = 'INSPINIA | ' + toState.data.pageTitle;
                $timeout(function () {
                    element.text(title);
                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    }
};

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element) {
            // Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function () {
                element.metisMenu();

            });

            // Colapse menu in mobile mode after click on element
            var menuElement = $('#side-menu a:not([href$="\\#"])');
            menuElement.click(function () {
                if ($(window).width() < 769) {
                    $("body").toggleClass("mini-navbar");
                }
            });

            // Enable initial fixed sidebar
            if ($("body").hasClass('fixed-sidebar')) {
                var sidebar = element.parent();
                sidebar.slimScroll({
                    height: '100%',
                    railOpacity: 0.9,
                });
            }
        }
    };
};

/**
 * minimalizaSidebar - Directive for minimalize sidebar
 */
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $("body").toggleClass("mini-navbar");
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 200);
                } else if ($('body').hasClass('fixed-sidebar')) {
                    $('#side-menu').hide();
                    setTimeout(
                        function () {
                            $('#side-menu').fadeIn(400);
                        }, 100);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            }
        }
    };
}

/**
 * landingScrollspy - Directive for scrollspy in landing page
 */
function landingScrollspy() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            element.scrollspy({
                target: '.navbar-fixed-top',
                offset: 80
            });
        }
    }
}

/**
 * passwordMeter - Directive for jQuery Password Strength Meter
 */
function passwordMeter() {
    return {
        restrict: 'A',
        scope: {
            pwOptions: '='
        },
        link: function (scope, element, attrs) {
            scope.$watch(scope.pwOptions, function () {
                render();
            });
            var render = function () {
                $(element).pwstrength(scope.pwOptions);
            };
        }
    }
}

function transDetails() {
    return {
        template:
        '<div ng-switch="{{row.bankStatus}}">' +
        '<button ng-switch-when="0" type="button" class="btn btn-xs btn-primary no-margins" ng-click="showModal(row)">成功</button>' +
        '<button ng-switch-when="1" type="button" class="btn btn-xs btn-danger no-margins" ng-click="showModal(row)">失败</button>' +
        '<button ng-switch-when="2" type="button" class="btn btn-xs btn-default no-margins" ng-click="showModal(row)">未支付</button>' +
        '<button ng-switch-when="3" type="button" class="btn btn-xs btn-info no-margins" ng-click="showModal(row)">支付中</button>' +
        '<button ng-switch-when="98" type="button" class="btn btn-xs btn-warning no-margins" ng-click="showModal(row)">超时</button>' +
        '</div>'
    };
}

function orderChangeBtn(httpSvc) {
    return {
        replace: true,
        scope: {row: '@', showModal: '&'},
        template:
            '<div>' +
            '<button class="btn btn-xs btn-primary no-margins" ng-click="showModal({row: thisRow})" ' +
            'ng-show="!(row.exceptionAmount >= row.amount || unaudited)" ng-disabled="(thisRow.exceptionStatus==2||thisRow.exceptionStatus==4)">退款</button>' +
            '<p class="no-margins" ng-show="unaudited">待审核</p>' +
            '</div>',
        link: function ($scope) {
            $scope.thisRow = angular.fromJson($scope.row);
            if ($scope.thisRow.exceptionAmount < $scope.thisRow.amount) {
                var searchInfo = {id: $scope.thisRow.id};
                httpSvc.getData('post', '/trans/unaudited', searchInfo).then(function (value) {
                    $scope.unaudited = value;
                });
            }
        }
    };
}

function userTbBtn() {
    return {
        replace: true,

        scope: {row: '@', statusChange: '&', edit: '&'},
        template:
        '<div style="min-width: 84px">' +
        '<button class="btn btn-xs no-margins" ' +
        'ng-class="{\'btn-primary active\': thisRow.status, \'btn-disabled\': !thisRow.status}" ' +
        'ng-click="statusChange({row: thisRow})">{{thisRow.status == 1 ? \'启用\' : \'禁用\'}}</button>' +
        '<button class="btn btn-xs btn-success" style="margin: 0 0 0 10px" ng-click="edit({row: thisRow, type: 1})">编辑</button>' +
        '<button class="btn btn-xs btn-warning" style="margin: 0 0 0 10px" ng-click="edit({row: thisRow, type: 2})">密码重置</button>' +
        '</div>',
        link: function ($scope) {
            $scope.thisRow = angular.fromJson($scope.row);
        }
    };
}

function roleTbBtn() {
    return {
        replace: true,
        scope: {row: '@', statusChange: '&', edit: '&'},
        template:
        '<div style="min-width: 44px">' +
        '<button class="btn btn-xs no-margins" ' +
        'ng-class="{\'btn-primary active\': thisRow.available, \'btn-disabled\': !thisRow.available}" ' +
        'ng-click="statusChange({row: thisRow})">{{thisRow.status == 1 ? \'启用\' : \'禁用\'}}</button>' +
        '<button class="btn btn-xs btn-success" style="margin: 0 0 0 10px" ng-click="edit({type: 1, row: thisRow})">编辑</button>' +
        '</div>',
        link: function ($scope) {
            $scope.thisRow = angular.fromJson($scope.row);
        }
    };
}

/*
 *  把form形式的提交转成JSON字符串
 */
function serializer() {
    return {
        restrict: "A",
        scope: {
            onSubmit: "&serializer"
        },
        link: function ($scope, $element) {
            var form = $element;
            form.submit(function (event) {
                event.preventDefault();
                var serializedData = form.serializeArray();
                var jsonData = {};
                angular.forEach(serializedData, function (data) {
                    if (data.value !== '' && data.value !== null) {
                        jsonData[data.name] = data.value;
                    }
                });
                if (Object.keys(jsonData).length > 0) {
                    $scope.onSubmit({data: jsonData});
                } else {
                    $scope.onSubmit({data: null});
                }
            });

        }
    };
}

function limitToMax() {
    return {
        link: function ($scope, $element, attributes) {
            $element.on("keydown keyup", function (e) {
                if (Number($element.val()) > Number(attributes.max) &&
                    e.keyCode !== 46
                    &&
                    e.keyCode !== 8
                ) {
                    e.preventDefault();
                    $element.val(attributes.max);
                    // $scope.orderChange.amount = Number(attributes.max);
                }
            });
        }
    };
}

function zxPasswordMeter() {
    return {
        scope: {
            value: "@",
            max: "@?"
        },
        templateUrl: 'views/common/password-meter',
        link: function ($scope) {
            $scope.type = 'danger';
            $scope.max = (!$scope.max) ? 100 : $scope.max;

            $scope.$watch('value', function (newValue) {
                var strenghPercent = newValue / $scope.max;
                if (strenghPercent === 0) {
                    $scope.text = '';
                } else if (strenghPercent <= 0.25) {
                    $scope.type = 'danger';
                    $scope.text = '弱';
                } else if (strenghPercent <= 0.50) {
                    $scope.type = 'warning';
                    $scope.text = '较弱';
                } else if (strenghPercent <= 0.75) {
                    $scope.type = 'primary';
                    $scope.text = '较强';
                } else {
                    $scope.type = 'success';
                    $scope.text = '强';
                }
            });
        }
    }
}

function constantStatusBtn() {
    return {
        replace: true,
        scope: {style: '@', row: '@', statusConstant: '@', statusChange: '&'},
        template:
        '<button class="btn btn-xs {{btnStyle}}" ' +
        'ng-class="{\'no-margins\' : style == null}" ' +
        'style="{{style}}" ' +
        'ng-click="statusChange({$event: $event, row: thisRow})">{{btnText}}</button>',
        link: function ($scope) {
            $scope.thisRow = angular.fromJson($scope.row);
            $scope.statusConstant = angular.fromJson($scope.statusConstant);
            angular.forEach($scope.statusConstant, function (value, key) {
                if (value.firstValue === $scope.thisRow.status.toString()) {
                    $scope.btnStyle = value.secondValue;
                    $scope.btnText = value.name;
                }
            });
        }
    }
}

function constantStatusBtnWidthEditBtn() {
    return {
        replace: true,
        scope: {row: '@', statusConstant: '@', statusChange: '&', edit: '&'},
        template:
        '<div style="min-width: 84px">' +
        '<button class="btn btn-xs no-margins {{btnStyle}}" ' +
        'ng-click="statusChange({$event: $event, row: thisRow})">{{btnText}}</button>' +
        '<button class="btn btn-xs btn-success" style="margin: 0 0 0 10px" ng-click="edit({type: 1, row: thisRow})">编辑</button>' +
        '</div>',
        link: function ($scope) {
            $scope.thisRow = angular.fromJson($scope.row);
            $scope.statusConstant = angular.fromJson($scope.statusConstant);
            angular.forEach($scope.statusConstant, function (value, key) {
                if (value.firstValue === ($scope.thisRow.status + '')) {
                    $scope.btnStyle = value.secondValue;
                    $scope.btnText = value.name;
                }
            });
        }
    }
}

function myPagination() {
    return {
        restrict: 'EA',
        templateUrl: '/views/common/my_pagination',
        scope: {tableParam: '='},
        link: function ($scope, $element, $attrs) {
            $scope.$watch('tableParam.prevParamsMemento.params.page', function (newVal) {
                if (newVal !== $scope.currentPage) {
                    $scope.currentPage = newVal;
                }
            });
            $scope.itemsPerPage = $scope.pageCount = 10;
            $scope.pageChanged = function (currentPage) {
                $scope.tableParam.page(currentPage);
            };
            $scope.pageJumpBlur = function (pageJump) {
                if (pageJump !== undefined && pageJump !== null && pageJump > 0) {
                    $scope.tableParam.page(pageJump);
                    $scope.currentPage = pageJump;
                }
            };
            $scope.pageCountBlur = function (pageCount) {
                if (pageCount !== undefined && pageCount !== null && pageCount > 0) {
                    $scope.currentPage = 1;
                    $scope.tableParam.page(1);
                    $scope.tableParam.count(pageCount);
                    $scope.itemsPerPage = pageCount;
                }
            };
        }
    };
}

var ngThumb = ['$window', function ($window) {
    var helper = {
        support: !!($window.FileReader && $window.CanvasRenderingContext2D),
        isFile: function (item) {
            return angular.isObject(item) && item instanceof $window.File;
        },
        isImage: function (file) {
            var type = '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    };

    return {
        restrict: 'A',
        template: '<canvas/>',
        link: function (scope, element, attributes) {
            if (!helper.support) return;

            var params = scope.$eval(attributes.ngThumb);

            if (!helper.isFile(params.file)) return;
            if (!helper.isImage(params.file)) return;

            var canvas = element.find('canvas');
            var reader = new FileReader();

            reader.onload = onLoadFile;
            reader.readAsDataURL(params.file);

            function onLoadFile(event) {
                var img = new Image();
                img.onload = onLoadImage;
                img.src = event.target.result;
            }

            function onLoadImage() {
                var width = params.width || this.width / this.height * params.height;
                var height = params.height || this.height / this.width * params.width;
                canvas.attr({width: width, height: height});
                canvas[0].getContext('2d').drawImage(this, 0, 0, width, height);
            }
        }
    };
}];

/**
 *
 * Pass all functions into module
 */
angular
    .module('inspinia')
    .directive('pageTitle', pageTitle)
    .directive('sideNavigation', sideNavigation)
    .directive('minimalizaSidebar', minimalizaSidebar)
    .directive('landingScrollspy', landingScrollspy)
    .directive('passwordMeter', passwordMeter)
    .directive('transDetails', transDetails)
    .directive('orderChangeBtn', orderChangeBtn)
    .directive('userTbBtn', userTbBtn)
    .directive('roleTbBtn', roleTbBtn)
    .directive('serializer', serializer)
    .directive('limitToMax', limitToMax)
    .directive('zxPasswordMeter', zxPasswordMeter)
    .directive('constantStatusBtn', constantStatusBtn)
    .directive('constantStatusBtnWidthEditBtn', constantStatusBtnWidthEditBtn)
    .directive('myPagination', myPagination)
    .directive('ngThumb', ngThumb);