<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/25
  Time: 17:20
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="roleMgmtCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'ROLE_MGMT' | translate }}</h5>
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
                            <table ng-table="roleTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'角色名'">
                                        {{row.roleName}}
                                    </td>
                                    <td class="text-center" data-title="'菜单权限'">
                                        <span ng-repeat="pid in row.privilegeIdList">
                                            {{pid | getValueByList:privilegeList:'id':'description'}}
                                            <span ng-show="$index != row.privilegeIdList.length-1">、</span>
                                        </span>
                                    </td>
                                    <td class="text-center" data-title="'备注'">
                                        {{row.remark}}
                                    </td>
                                    <td class="text-center p-6" style="width: 110px;" data-title="'操作'">
                                        <button class="btn btn-xs btn-primary no-margins"
                                                ng-click="showModal(1, row)">
                                            编辑
                                        </button>
                                        <button class="btn btn-xs btn-danger" style="margin: 0 0 0 5px;"
                                                ng-click="showModal(2, row)">
                                            删除
                                        </button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="roleTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
