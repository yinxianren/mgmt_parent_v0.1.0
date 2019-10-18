<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="channelHistoryCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'CHANNEL_HISTORY' | translate}}</h5>
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
                                        <span class="input-group-addon">机构名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.organizationId"
                                               uib-typeahead="x.organizationId as x.organizationId + '(' + x.organizationName + ')' for x in organizations | filter: {organizationId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">通道名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.channelId"
                                               uib-typeahead="x.channelId as x.channelId + '(' + x.channelName + ')' for x in channels | filter: {channelId : $viewValue}"
                                               typeahead-min-length="0">

                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">运行状态：</span>
                                        <select class="form-control b-r-sm"
                                                name="agentIdentityType"
                                                ng-model="searchInfo.status">
                                            <option value=""></option>
                                            <option value="0">启用</option>
                                            <option value="1">禁用</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">登记开始时间：</span>
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
                                        <span class="input-group-addon">登记结束日期：</span>
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
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">切换开始时间：</span>
                                        <input type="text"
                                               id="start_date2"
                                               name="startDate2"
                                               class="form-control"
                                               ng-readonly="true"
                                               ng-click="openDatepicker3()"
                                               uib-datepicker-popup="{{format}}"
                                               ng-model="searchInfo.startDate2"
                                               is-open="popup3.opend"
                                               datepicker-options="dateOptions3"
                                               alt-input-formats="altInputFormats"
                                               close-text="关闭" clear-text="清空" current-text="今天">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker3()">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">切换结束日期：</span>
                                        <input type="text"
                                               id="end_date2"
                                               name="endDate2"
                                               class="form-control"
                                               ng-click="openDatepicker2()"
                                               uib-datepicker-popup="{{format}}"
                                               ng-model="searchInfo.endDate2"
                                               is-open="popup4.opend"
                                               datepicker-options="dateOptions4"
                                               alt-input-formats="altInputFormats"
                                               ng-disabled="endDateDisable"
                                               close-text="关闭" clear-text="清空" current-text="今天">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker4()"
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
                            <table ng-table="channelHistoryTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'机构名称'">
                                        {{row.organizationId | getValueByList : organizations : 'organizationId' : 'organizationName'}}
                                    </td>
                                    <td class="text-center" data-title="'通道名称'">
                                        {{row.channelId  | getValueByList : channels : 'channelId' : 'channelName'}}
                                    </td>
                                    <td class="text-center" data-title="'支付方式'">
                                        {{row.channelType | getValueByList : payTypes : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'运行状态'" >
                                        {{row.status | getValueByList : statusList : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'总订单金额'" >
                                        {{row.amount | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'总交易笔数'" >
                                        {{row.tradeCount}}
                                    </td>
                                    <td class="text-center" data-title="'切换原因'" >
                                        {{row.reason}}
                                    </td>
                                    <td class="text-center" data-title="'登记时间'">
                                        {{row.createTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'切换时间'">
                                        {{row.changeTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="channelHistoryTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
