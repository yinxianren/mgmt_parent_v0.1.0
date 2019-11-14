<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="systemOrderTrackCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'TRANS_PAYMENT' | translate}}</h5>
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
                                        <%--<input class="form-control b-r-sm" type="text" ng-model="searchInfo.merId">--%>
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
                                        <span class="input-group-addon">订单号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.orderId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">交易时间：</span>
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
                            <table ng-table="systemOrderTrackTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merchantId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <td class="text-center" data-title="'订单号'">
                                        {{row.platformOrderId == null ? "--" :row.platformOrderId}}
                                    </td>
                                   <%-- <td class="text-center" data-title="'追踪状态'">
                                        {{row.orderTrackStatus }}
                                    </td>--%>
                                    <td class="text-center" data-title="'订单金额'" >
                                        {{row.amount == null ? "--":row.amount | number: 2}}
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'页面返回地址'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.returnUrl)">
                                                {{row.returnUrl == null ?"--":row.returnUrl}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'异步回调地址'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.noticeUrl)">
                                                {{row.noticeUrl == null?"--":row.noticeUrl}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'请求报文'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.requestMsg)">
                                                {{row.requestMsg}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'请求网址'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.requestPath)">
                                                {{row.requestPath}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'返回结果'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.responseResult)">
                                                {{row.responseResult}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'日志信息'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.platformPrintLog)">
                                                {{row.platformPrintLog}}
                                            </a>
                                        </div>
                                    </td>
                                    <%--<td class="text-center" style="max-width: 100px" data-title="'结果'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.result)">
                                                {{row.result}}
                                            </a>
                                        </div>
                                    </td>--%>
                                    <td class="text-center" data-title="'交易时间'">
                                        {{row.tradeTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="systemOrderTrackTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
