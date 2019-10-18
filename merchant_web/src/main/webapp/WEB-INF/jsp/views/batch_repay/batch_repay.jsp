<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/28
  Time: 14:55
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'BATCH_REPAY' | translate }}：</h5>
                    </div>
                    <div class="ibox-content sk-loading" ng-controller="batchRepayCtrl">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>

                        <form name="myForm">
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">商户订单号：</span>
                                            <input type="text" class="form-control" ng-model="searchInfo.merOrderId">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">平台订单号：</span>
                                            <input type="text" class="form-control" ng-model="searchInfo.orderId">
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
                                    <button type="submit" class="btn btn-sm btn-w-m btn-primary pull-right"
                                            ng-click="search()">
                                        查询
                                    </button>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-3">
                                    <button type="button" class="btn btn-sm btn-w-m btn-info"
                                            ng-click="reset()">
                                        重置
                                    </button>
                                </div>
                                <div class="pull-right col-sm-6 col-md-6 col-lg-3">
                                    <button type="button" class="pull-right btn btn-sm btn-sm btn-success"
                                            ng-click="showModal()">
                                        <i class="fa fa-download"></i>&nbsp;批量操作
                                    </button>
                                </div>
                            </div>
                        </form>

                        <div class="hr-line-dashed"></div>

                        <div class="table-responsive">
                            <table ng-table="batchRepayTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户号'">
                                        {{row.merId }}
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'">
                                        {{row.orderId}}
                                    </td>
                                    <td class="text-center" data-title="'银行编码'">
                                        {{row.bankCode}}
                                    </td>
                                    <td class="text-center" data-title="'收款卡号'">
                                        {{row.inAcctNo}}
                                    </td>
                                    <td class="text-center" data-title="'收款户名'">
                                        {{row.inAcctName}}
                                    </td>
                                    <td class="text-center" data-title="'备注'">
                                        {{row.remark}}
                                    </td>
                                    <td class="text-center" data-title="'返回地址'">
                                        {{row.returnUrl}}
                                    </td>
                                    <td class="text-center" data-title="'通知地址'">
                                        {{row.noticeUrl}}
                                    </td>
                                    <td class="text-center" data-title="'状态'">
                                        {{row.status==0?'上传成功':'上传失败'}}
                                    </td>
                                    <td class="text-center" data-title="'交易时间'">
                                        {{row.createTime | date:'yyyy/MM/dd hh:mm'}}
                                    </td>

                                </tr>
                            </table>
                        </div>

                        <my-pagination table-param="batchRepayTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>