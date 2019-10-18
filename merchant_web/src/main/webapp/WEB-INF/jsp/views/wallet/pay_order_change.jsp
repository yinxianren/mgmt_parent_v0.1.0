<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/14
  Time: 14:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="payOrderChangeCtrl">
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
                                        <span class="input-group-addon">异常订单号：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.exceptionId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">平台订单号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.payId">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">类型：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.type"
                                                ng-options="x.firstValue as x.name for x in type">
                                        </select>
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
                            <table ng-table="payOrderChangeTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'商户'">
                                        ${sessionScope.merchantInfo.merId}(${sessionScope.merchantInfo.merchantName})
                                    </td>
                                    <td class="text-center" data-title="'异常订单号'">
                                        {{row.exceptionId}}
                                    </td>
                                    <td class="text-center" data-title="'平台订单号'">
                                        {{row.payId}}
                                    </td>
                                    <td class="text-center" data-title="'订单金额'">
                                        {{row.changeAmount   }}
                                    </td>
                                    <td class="text-center" data-title="'订单类型'">
                                        <div ng-if="row.type == 0 ">
                                            退款
                                        </div>
                                        <div ng-if="row.type == 1 ">
                                            拒付
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'订单状态'">
                                        <div ng-if="row.changeStatus == 0 ">
                                            退款中
                                        </div>
                                        <div ng-if="row.changeStatus == 1 ">
                                            已退款
                                        </div>
                                        <div ng-if="row.changeStatus == 2 ">
                                            拒绝退款
                                        </div>
                                        <div ng-if="row.changeStatus == 3 ">
                                            拒付
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'备注'">
                                        {{row.remark  }}
                                    </td>
                                    <td class="text-center" data-title="'创建时间'">
                                        {{row.changeTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="payOrderChangeTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
