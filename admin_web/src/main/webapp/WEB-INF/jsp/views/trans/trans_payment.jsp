
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="transPaymentCtrl">
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
                                        <span class="input-group-addon">平台订单号：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.payId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merId"
                                               uib-typeahead="x.merchantId as x.merchantId + '(' + x.merchantName + ')' for x in merchants | filter: {merchantId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                            <%--<div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">机构名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.organizationId"
                                               uib-typeahead="x.organizationId as x.organizationId + '(' + x.organizationName + ')' for x in organizations | filter: {organizationId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>--%>
                           <%-- <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">通道名称：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.channelId"
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
                                            <option value="1">未结算</option>
                                            <option value="0">已结算</option>
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

                        <div class="row" style="margin-top: 25px;margin-bottom: 10px;line-height: 33px;">
                            <div class="col-sm-1 col-md-2" style="width: 12.66666665%">
                                <strong>订单数：</strong><strong style="color: red">{{totalOrder}}</strong>&nbsp;<strong>单</strong>
                            </div>
                            <div class="col-sm-1 col-md-2" style="width: 12.66666665%">
                               <strong>总金额：</strong><strong style="color: red">{{customize.totalMoney}}</strong>&nbsp;<strong>元</strong>
                            </div>
                            <div class="col-sm-2 col-md-2" style="width: 12.66666665%">
                                <strong>手续费总金额：</strong><strong style="color: red">{{customize.totalFee}}</strong>&nbsp;<strong>元</strong>
                            </div>
                            <div class="col-sm-2 col-md-2" style="width: 13.66666665%">
                                <strong>手续费成本总金额：</strong><strong style="color: red">{{customize.totalChannelFee}}</strong>&nbsp;<strong>元</strong>
                            </div>
                           <%-- <div class="col-sm-2 col-md-2" style="width: 12.66666665%">
                            </div>--%>
                            <div style="float: right;margin-right: 20px;">
                                <button class="btn btn-sm btn-w-m btn-info" ng-click="excel(searchInfo)">
                                    <strong>excel导出</strong>
                                </button>
                            </div>
                        </div>

                        <div class="table-responsive">
                            <table ng-table="transPaymentTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merchantId}}({{row.merchantId  | getValueByList : merchants : 'merchantId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'" style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showTransPayment(1,row)">
                                        {{row.platformOrderId}}
                                    </td>
                                    <%--<td class="text-center" data-title="'代理商名称'">
                                        {{row.agmentId | getValueByList : agents : 'agentMerchantId':'agentMerchantName' }}
                                       &lt;%&ndash; {{row.agmentId}}&ndash;%&gt;
                                    </td>--%>
                                   <%-- <td class="text-center" data-title="'通道交易流水号'">
                                        {{row.orgOrderId}}
                                    </td>--%>
                                    <td class="text-center" data-title="'机构名称'">
                                        {{row.organizationId | getValueByList : organizations : 'organizationId':'organizationName' }}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道名称'">
                                        {{row.channelId | getValueByList : channels : 'channelId' : 'channelName' }}
                                    </td>--%>
                                    <td class="text-center" data-title="'支付产品'">
                                        {{row.productId | getValueByList : productTypes : 'firstValue':'name'}}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道商户编码'">--%>
                                <%--{{row.channelTransCode}}--%>
                                <%--</td>--%>
                                    <%--<td class="text-center" data-title="'订单币种'">
                                        {{row.currency}}
                                    </td>--%>
                                    <td class="text-center" data-title="'订单金额'" <%--style="text-decoration: underline;color: #1ab394;cursor: pointer;"
                                        ng-click="showTransPayment(2,row)"--%>>
                                        {{row.amount | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'实际支付金额'">
                                        {{row.inAmount | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'平台手续费收入'">
                                        {{row.merFee | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'手续费率(%)'">
                                        {{row.payFee | number: 2}}
                                    </td>
                                    <td class="text-center" data-title="'通道成本'">
                                        {{row.channelFee | number: 2}}
                                    </td>
                                <%--订单状态:默认为null，(0:成功, 1:失败, 2:未支付, 3:支付中)--%>
                                    <td class="text-center" data-title="'订单状态'">
                                        <div ng-if="row.status == 0 ">
                                            成功
                                        </div>
                                        <div ng-if="row.status == 1 ">
                                            失败
                                        </div>
                                        <div ng-if="row.status == 2 ">
                                            未支付
                                        </div>
                                        <div ng-if="row.status == 3 ">
                                            支付中
                                        </div>
                                    </td>
                                <%--结算状态:默认0，(0   未结算   1   已结算)--%>
                                    <td class="text-center" data-title="'结算状态'">
                                        <div ng-if="row.settleStatus == 1 ">
                                            未结算
                                        </div>
                                        <div ng-if="row.settleStatus == 0 ">
                                            已结算
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'交易时间'">
                                        {{row.createTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <%--<td class="text-center" data-title="'通道交易时间'">
                                        {{row.bankTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>--%>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="transPaymentTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
