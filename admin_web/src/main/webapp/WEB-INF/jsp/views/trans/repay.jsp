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
                                        <input type="text" class="form-control" ng-model="searchInfo.platformOrderId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户名：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merId"
                                               uib-typeahead="x.merId as x.merId + '(' + x.merchantName + ')' for x in merchants | filter: {merId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                            <%--<div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">机构名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.organizationId"
                                               &lt;%&ndash;uib-typeahead="x organizationId x.organizationName for x in organizations | filter: {organizationId : $viewValue}"&ndash;%&gt;
                                               uib-typeahead="x.organizationId as x.organizationId + '(' + x.organizationName + ')' for x in organizations | filter: {organizationId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>--%>
                            <%--<div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">通道名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.channelId"
                                               &lt;%&ndash;uib-typeahead="x channelId x.channelName for x in channels | filter: {channelId : $viewValue}"&ndash;%&gt;
                                               uib-typeahead="x.channelId as x.channelId + '(' + x.channelName + ')' for x in channels | filter: {channelId : $viewValue}"
                                               typeahead-min-length="0">

                                    </div>
                                </div>
                            </div>--%>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">订单状态：</span>
                                        <select class="form-control b-r-sm"
                                                name="type"
                                                ng-model="searchInfo.orderStatus">
                                            <option value=""></option>
                                            <option value="0">成功</option>
                                            <option value="1">失败</option>
                                            <option value="2">未支付</option>
                                            <option value="3">支付中</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">结算状态：</span>
                                        <select class="form-control b-r-sm"
                                                name="type"
                                                ng-model="searchInfo.settleStatus">
                                            <option value=""></option>
                                            <option value="1">已结算</option>
                                            <option value="0">未结算</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">支付产品：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.productId"
                                                ng-options="x.firstValue as x.name for x in productTypes">
                                        </select>
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

                        <div class="row" style="margin-top: 25px;margin-bottom: 10px;">
                            <div class="col-sm-1 col-md-2" style="width: 12.66666665%">
                                <strong>订单数：</strong><strong style="color: red">{{totalOrdert}}</strong>&nbsp;<strong>单</strong>
                            </div>
                            <div class="col-sm-1 col-md-2" style="width: 12.66666665%">
                                <strong>总金额：</strong><strong style="color: red">{{customizet.totalMoney}}</strong>&nbsp;<strong>元</strong>
                            </div>
                            <div class="col-sm-2 col-md-2" style="width: 12.66666665%">
                                <strong>手续费总金额：</strong><strong style="color: red">{{customizet.totalFee}}</strong>&nbsp;<strong>元</strong>
                            </div>
                            <div class="col-sm-2 col-md-2" style="width: 13.66666665%">
                                <strong>手续费成本总金额：</strong><strong style="color: red">{{customizet.totalChannelFee}}</strong>&nbsp;<strong>元</strong>
                            </div>
                        </div>

                        <div class="table-responsive">
                            <table ng-table="repayTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merchantId}}({{row.merchantId  | getValueByList : merchants : 'merchantId':'merchantName'}})
                                        <%-- {{row.merId}}--%>
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'"
                                        style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showModel(1,row)">
                                        {{row.platformOrderId}}
                                    </td>
                                    <%--<td class="text-center" data-title="'代理商名称'">
                                        {{row.agmentId | getValueByList : agentMerchants : 'agentMerchantId':'agentMerchantName' }}
                                    </td>--%>
                                   <%-- <td class="text-center" data-title="'通道交易流水号'">
                                        {{row.orgOrderId}}
                                    </td>--%>
                                    <td class="text-center" data-title="'机构名称'">
                                        {{row.organizationId | getValueByList : organizations : 'organizationId' : 'organizationName'}}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道名称'">
                                        {{row.channelId  | getValueByList : channels : 'channelId' : 'channelName'}}
                                    </td>--%>
                                    <td class="text-center" data-title="'支付产品'">
                                        {{row.productId | getValueByList : productTypes : 'firstValue' : 'name'  }}
                                    </td>
                                    <%--<td class="text-center" data-title="'支付商户号'">
                                        {{row.channelTransCode}}
                                    </td>--%>
                                   <%-- <td class="text-center" data-title="'订单币种'">
                                        {{row.currency}}
                                    </td>--%>
                                    <td class="text-center" data-title="'订单金额'"
                                        <%--style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showModel(2,row)"--%>
                                    >
                                        {{row.amount | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'实际支付金额'">
                                        {{row.outAmount | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'手续费率(%)'">
                                        {{row.backFee | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'平台手续费收入'">
                                        {{row.merFee | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'平台毛利润'">
                                        {{row.platformIncome | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'通道成本'">
                                        {{row.channelFee | number:2}}
                                    </td>
                                    <td class="text-center" data-title="'订单状态'">
                                        {{row.status | getValueByList : orderStatus : 'firstValue' : 'name' }}
                                    </td>
                                    <td class="text-center" data-title="'结算状态'">
                                        {{row.settleStatus | getValueByList : settleStatus : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'交易时间'">
                                        {{row.updateTime | date:'yyyy-MM-dd HH:mm:ss' }}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道交易时间'">
                                        {{row.bankTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>--%>
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
