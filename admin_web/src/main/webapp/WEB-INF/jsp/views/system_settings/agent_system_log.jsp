
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="agentSystemLogCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'SYSTEM_LOG' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading">
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
                                        <span class="input-group-addon">日志类型：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.type">
                                            <option value=""></option>
                                            <option ng-repeat="x in logType" value="{{x.firstValue}}">{{x.name}}
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">操作者：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.operator">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">方法描述：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.methodDescription">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">起始日期：</span>
                                        <input type="text"
                                               class="form-control"
                                               ng-readonly="true"
                                               ng-click="openDatepicker1()"
                                               uib-datepicker-popup="{{format}}"
                                               ng-model="searchInfo.startTime"
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
                                        <input type="text"
                                               class="form-control"
                                               ng-readonly="true"
                                               ng-click="openDatepicker2()"
                                               uib-datepicker-popup="{{format}}"
                                               ng-model="searchInfo.endTime"
                                               is-open="popup2.opend"
                                               datepicker-options="dateOptions2"
                                               alt-input-formats="altInputFormats"
                                               ng-disabled="endTimeDisable"
                                               close-text="关闭" clear-text="清空" current-text="今天">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker2()"
                                                    ng-disabled="endTimeDisable">
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
                        <div class="hr-line-dashed"></div>
                        <div class="table-responsive">
                            <table ng-table="logTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'操作人'">
                                        {{row.operator}}
                                    </td>
                                    <td class="text-center" data-title="'日志类型'">
                                        {{row.typeCN}}
                                    </td>
                                    <td class="text-center" data-title="'方法名称'">
                                        {{row.methodName}}
                                    </td>
                                    <td class="text-center" data-title="'方法描述'">
                                        {{row.methodDescription}}
                                    </td>
                                    <td class="text-center" data-title="'方法用时'">
                                        {{row.spendTime == null ? '' : row.spendTime / 1000 + '&nbsp;s'}}
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'日志信息'">
                                        <div class="hidden-text">
                                            <a ng-click="showModal(row)">
                                                {{row.message}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'URI'">
                                        {{row.requestUri}}
                                    </td>
                                    <td class="text-center" data-title="'操作时间'">
                                        {{row.startTime | date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'用户IP'">
                                        {{row.requestIp}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="logTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
