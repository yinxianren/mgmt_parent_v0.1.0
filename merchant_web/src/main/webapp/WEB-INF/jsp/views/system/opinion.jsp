<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/27
  Time: 10:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="opinionCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'OPINION' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <form>
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">文本内容：</span>
                                            <input type="text" class="form-control" ng-model="searchInfo.content">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">问题类型：</span>
                                            <select class="form-control b-r-sm" ng-model="searchInfo.type"
                                                    ng-options="x.firstValue as x.name for x in questionType">
                                                <option value=""></option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">状态：</span>
                                            <select class="form-control b-r-sm" ng-model="searchInfo.status"
                                                    ng-options="x.firstValue as x.name for x in questionStatus">
                                                <option value=""></option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">提交时间：</span>
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
                                    <button type="submit" class="btn btn-sm btn-w-m btn-primary pull-right"
                                            ng-click="search()">
                                        查询
                                    </button>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-3">
                                    <button type="button" class="btn btn-sm btn-w-m btn-info"
                                            ng-click="resetForm()">
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                        <button class="btn btn-sm btn-success" ng-click="showModal(0)">新增{{abcd}}</button>
                        <div class="table-responsive">
                            <table ng-table="opinionTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'编号'">
                                        {{row.id}}
                                    </td>
                                    <td class="text-center" data-title="'商户'">
                                        ${sessionScope.merchantInfo.merId}&nbsp(${sessionScope.merchantInfo.merchantName})
                                    </td>
                                    <td class="text-center" data-title="'电话'">
                                        {{row.phone}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.email}}
                                    </td>
                                    <td class="text-center" data-title="'提问类型'">
                                        {{row.type | getValueByList:questionType:'firstValue':'name'}}
                                    </td>
                                    <td class="text-center" data-title="'内容'">
                                        <%--<div ng-bind-html="row.content"></div>--%>
                                        <div class="hidden-text"
                                             ng-bind-html="row.content | trustHtml"
                                             uib-tooltip="{{row.content}}"
                                        >
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'提交时间'">
                                        {{row.time | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'状态'">
                                        {{row.status | getValueByList:questionStatus:'firstValue':'name'}}
                                    </td>
                                    <td class="text-center" data-title="'回复内容'">
                                        <div  ng-bind-html="row.answer | trustHtml"></div>
                                    </td>
                                    <td class="text-center" data-title="'回复人'">
                                        {{row.answerPeople}}
                                    </td>
                                    <td class="text-center" data-title="'回复时间'">
                                        {{row.answerTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center p-6" style="width: 50px;" data-title="'操作'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(1, row)"
                                                ng-show="row.status == 1">
                                            编辑
                                        </button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="opinionTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
