<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/25
  Time: 16:11
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merchantUserMgmtCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'MERCHANT_USER' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <button class="btn btn-sm btn-primary" ng-click="showModal(null, 0)">新增</button>
                        <div class="table-responsive">
                            <table ng-table="merchantUserTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="width: 13px">
                                        <input type="checkbox" ng-model="selected[row.id]">
                                    </td>
                                    <td class="text-center" data-title="'用户名'">
                                        {{row.userName}}
                                    </td>
                                    <td class="text-center" data-title="'姓名'">
                                        {{row.realName}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.email}}
                                    </td>
                                    <td class="text-center" data-title="'电话'">
                                        {{row.telphone}}
                                    </td>
                                    <td class="text-center" data-title="'手机'">
                                        {{row.mobile}}
                                    </td>
                                    <td class="text-center p-6" style="max-width: 100px;" data-title="'操作'">
                                        <user-tb-btn row="{{row}}"
                                                     status-change="statusChange(row)"
                                                     edit="showModal(row, type)"></user-tb-btn>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <button class="btn btn-sm btn-primary pull-left" ng-click="del()" ng-disabled="delDisabled()">
                            删除
                        </button>
                        <my-pagination table-param="merchantUserTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>