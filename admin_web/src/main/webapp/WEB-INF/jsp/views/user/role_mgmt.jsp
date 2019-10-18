<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/13
  Time: 9:08
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">

    <div class="wrapper wrapper-content ng-scope" ng-controller="roleMgmtCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'ROLE_INFO' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <%--<div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">角色名：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.roleName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">拥有权限：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.privilegesId"
                                                ng-options="x.id as x.description for x in privileges">
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
                        <div class="hr-line-dashed"></div>--%>
                        <button class="btn btn-sm btn-primary" ng-click="showModal(0)">新增</button>
                        <div class="table-responsive">
                            <table ng-table="roleTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="width: 13px">
                                        <input type="checkbox" id="userRoleCheckBox" ng-model="selected[row.id]" >
                                    </td>
                                    <td class="text-center" data-title="'角色名'">
                                        <strong>{{row.roleName}}</strong>
                                    </td>
                                    <td class="text-center p-6" style="max-width: 100px;" data-title="'操作'">
                                        <role-tb-btn row="{{row}}"
                                                     status-change="statusChange(row)"
                                                     edit="edit(type, row)"></role-tb-btn>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="del()" ng-disabled="delDisabled()">
                            删除
                        </button>
                        <my-pagination table-param="roleTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>