<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/14
  Time: 14:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="auditCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'TRANS_AUDIT' | translate}}</h5>
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
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merOrderId"
                                               uib-typeahead="x.merId as x.merId + '(' + x.merchantName + ')' for x in merchants | filter: {merId : $viewValue}"
                                               typeahead-min-length="0">
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
                            <table ng-table="auditTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'平台订单号'">
                                        {{row.transId}}
                                    </td>
                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'商户订单号'">
                                        {{row.merOrderId}}
                                    </td>
                                    <td class="text-center" data-title="'订单币种'">
                                        {{row.currency}}
                                    </td>
                                    <td class="text-center" data-title="'总金额'">
                                        {{row.amount}}
                                    </td>
                                    <td class="text-center" data-title="'审核状态'">
                                        <div ng-show="row.status == 0 ">
                                            已审核
                                        </div>
                                        <div ng-show=" row.status == 2 ">
                                            未审核
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'审核时间'">
                                        {{row.transferTime | date:'yyyy-MM-dd HH:mm:ss' }}
                                    </td>
                                    <td class="text-center" data-title="'审核人'">
                                        {{row.transferer}}
                                    </td>
                                    <td class="text-center p-6" style="width: 110px" data-title="'操作'">
                                        <%--<div class="btn btn-xs no-margins btn-primary active" ng-click="statusChange($event, row)" ng-show="row.status == 0 ">--%>
                                            <%--取消审核--%>
                                        <%--</div>--%>
                                        <div  class="btn btn-xs no-margins btn-disabled" ng-click="statusChange($event, row)" ng-show=" row.status == 2 ">
                                            审核
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="auditTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
