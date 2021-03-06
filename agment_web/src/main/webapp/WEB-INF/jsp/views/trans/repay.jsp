<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/14
  Time: 14:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="repayCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'TRANS_REPAY' | translate}}</h5>
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
                                        <span class="input-group-addon">平台订单号：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.transId">
                                    </div>
                                </div>
                            </div>
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
                                        <span class="input-group-addon">订单状态：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.orderStatus"
                                                ng-options="x.firstValue as x.name for x in orderStatus">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">结算状态：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.settleStatus"
                                                ng-options="x.firstValue as x.name for x in settleStatus">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">支付方式：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.payType"
                                                ng-options="x.firstValue as x.name for x in payTypes">
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">交易开始时间：</span>
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
                                        <span class="input-group-addon">交易结束时间：</span>
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
                            <table ng-table="repayTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'"
                                        style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showModel(1,row)">
                                        {{row.transId}}
                                    </td>
                                    <%--<td class="text-center" data-title="'机构名称'">
                                        {{row.organizationId | getValueByList : organizations : 'organizationId' : 'organizationName'}}
                                    </td>--%>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道名称'">
                                        {{row.channelId  | getValueByList : channels : 'channelId' : 'channelName'}}
                                    </td>--%>
                                    <td class="text-center" data-title="'支付方式'">
                                        {{row.payType | getValueByList : payTypes : 'firstValue' : 'name'  }}
                                    </td>
                                    <td class="text-center" data-title="'订单币种'">
                                        {{row.currency}}
                                    </td>
                                    <td class="text-center" data-title="'订单金额'"
                                        style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showModel(2,row)"
                                    >
                                        {{row.amount}}
                                    </td>
                                    <td class="text-center" data-title="'实际支付金额'">
                                        {{row.realAmount}}
                                    </td>
                                    <td class="text-center" data-title="'平台手续费收入'">
                                        {{row.fee}}
                                    </td>
                                    <td class="text-center" data-title="'分润金额'">
                                        {{row.agentFee}}
                                    </td>
                                    <td class="text-center" data-title="'订单状态'">
                                        {{row.orderStatus | getValueByList : orderStatus : 'firstValue' : 'name' }}
                                    </td>
                                    <td class="text-center" data-title="'结算状态'">
                                        {{row.settleStatus | getValueByList : settleStatus : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'交易时间'">
                                        {{row.tradeTime | date:'yyyy-MM-dd HH:mm:ss' }}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="repayTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
