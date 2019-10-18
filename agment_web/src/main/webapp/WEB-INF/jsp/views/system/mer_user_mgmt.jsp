<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/25
  Time: 10:30
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merUserMgmtCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'USER_MGMT' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <button class="btn btn-sm btn-success" ng-click="showModal(0)">新增</button>
                        <div class="table-responsive">
                            <table ng-table="userTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'用户名'">
                                        {{row.userName}}
                                    </td>
                                    <td class="text-center" data-title="'真实姓名'">
                                        {{row.realName}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.email}}
                                    </td>
                                    <td class="text-center" data-title="'手机'">
                                        {{row.mobile}}
                                    </td>
                                    <td class="text-center" data-title="'角色'">
                                        {{row.roleId | getValueByList:roleList:'id':'roleName'}}
                                    </td>
                                    <td class="text-center" data-title="'创建人'">
                                        {{row.creator}}
                                    </td>
                                    <td class="text-center" data-title="'创建时间'">
                                        {{row.createTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'修改人'">
                                        {{row.modifier}}
                                    </td>
                                    <td class="text-center" data-title="'修改时间'">
                                        {{row.modifyTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center p-6" style="max-width: 170px;" data-title="'操作'">
                                        <div style="min-width: 160px">
                                            <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(1, row)">
                                                编辑
                                            </button>
                                            <button class="btn btn-xs btn-warning" style="margin: 0 0 0 5px;"
                                                    ng-click="showModal(2, row)">
                                                重置密码
                                            </button>
                                            <button class="btn btn-xs btn-danger" style="margin: 0 0 0 5px;"
                                                    ng-click="showModal(3, row)">
                                                删除
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="userTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
