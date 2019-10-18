<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merchantsDetailsCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'MERCHANT_WALLET_DETAILS' | translate}}</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merId"
                                               uib-typeahead="x.merId as x.merId + '(' + x.merchantName + ')' for x in merchants | filter: {merId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">平台订单号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.orderId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户订单号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merOrderId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">类型：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.type"
                                                ng-options="x.firstValue as x.name for x in detailsTypes">
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">开始时间：</span>
                                        <input type="text" id="start_date" name="startDate"
                                               class="form-control" ng-readonly="true" ng-click="openDatepicker1()"
                                               uib-datepicker-popup="{{format}}" ng-model="searchInfo.startDate"
                                               is-open="popup1.opend"
                                               datepicker-options="dateOptions1"
                                               alt-input-formats="altInputFormats"
                                               close-text="关闭" clear-text="清空" current-text="今天">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker1()">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">结束日期：</span>
                                        <input type="text" id="end_date" name="endDate"
                                               class="form-control" ng-readonly="true" ng-click="openDatepicker2()"
                                               uib-datepicker-popup="{{format}}" ng-model="searchInfo.endDate"
                                               is-open="popup2.opend"
                                               datepicker-options="dateOptions2"
                                               alt-input-formats="altInputFormats"
                                               ng-disabled="endDateDisable"
                                               close-text="关闭" clear-text="清空" current-text="今天">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker2()"
                                                    ng-disabled="endDateDisable">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-3 col-lg-offset-3">
                                <button class="btn btn-sm btn-w-m btn-primary pull-right" ng-click="search(searchInfo)">
                                    查询
                                </button>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-3">
                                <button class="btn btn-sm btn-w-m btn-info" ng-click="reset()">
                                    重置
                                </button>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table ng-table="merchantsDetailsTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId  }}
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'">
                                        {{row.orderId  }}
                                    </td>
                                    <td class="text-center" data-title="'类型'">
                                        {{row.type | getValueByList : detailsTypes : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'订单金额'" >
                                        {{row.amount | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'入账金额'" >
                                        {{row.inAmount==null?'--':(row.inAmount | number: 2)}}
                                    </td>
                                    <td class="text-center" data-title="'出账金额'" >
                                        {{row.outAmount==null?'--':(row.outAmount | number: 2)}}
                                    </td>
                                    <td class="text-center" data-title="'手续费成本'" >
                                        {{row.fee | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'手续费利润'" >
                                        {{row.feeProfit | number: 2}}
                                    </td>
                                     <td class="text-center" data-title="'总余额'" >
                                        {{row.totalBalance | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'更新时间'">
                                        {{row.updateTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="merchantsDetailsTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
