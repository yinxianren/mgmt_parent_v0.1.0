
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merchantIpCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'CUSTOMER_IP' | translate }}</h5>
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
                                        <span class="input-group-addon">商户号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.customerId"
                                               uib-typeahead="x.customerId as x.customerId + '(' + x.customerName + ')' for x in customers | filter: {id : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">IP：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.loginIp"
                                               typeahead-min-length="3"
                                        >
                                    </div>
                                </div>
                            </div>



                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-3 col-lg-offset-3">
                                <button class="btn btn-sm btn-w-m btn-primary pull-right" ng-click="search()">
                                    查询
                                </button>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-3">
                                <button class="btn btn-sm btn-w-m btn-info" ng-click="resetForm()">
                                    重置
                                </button>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <button class="btn btn-sm btn-primary" ng-click="showAddModal(0)">新增商户IP</button>
                        <button class="btn btn-sm btn-primary" ng-click="showAddModal(1)">新增代理商IP</button>
                        <div class="table-responsive">
                            <table ng-table="webSiteTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center p-6"  >
                                        <input type="checkbox" ng-model="selected[row.id]">
                                    </td>
                                    <td class="text-center" data-title="'内部编号'">
                                        {{row.id}}
                                    </td>
                                    <td class="text-center" data-title="'用户编号'">
                                        {{row.customerId}}
                                    </td>
                                    <td class="text-center" data-title="'IP地址'">
                                        {{row.loginIp}}
                                    </td>
                                    <td class="text-center" data-title="'创建时间'">
                                        {{row.addTime | date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                    <%--<td class="text-center" data-title="'操作人'">--%>
                                        <%--{{row.modifier}}--%>
                                    <%--</td>--%>
                                </tr>
                            </table>
                        </div>
                        <button class="btn btn-sm btn-primary pull-left" ng-click="del()" ng-disabled="delDisabled()">
                            删除
                        </button>
                        <my-pagination table-param="webSiteTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>