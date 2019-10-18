<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/9
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="userMgmtCtrl">
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
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">用户名：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.userName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">角色：</span>
                                        <select class="form-control b-r-sm" ng-model="searchInfo.roleId"
                                                ng-options="x.id as x.roleName for x in allRole">
                                            <option value=""></option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">姓名：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.realName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">手机：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.mobile">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">电话：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.telphone">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">邮箱：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.email">
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
                        <div class="hr-line-dashed"></div>
                        <button class="btn btn-sm btn-primary" ng-click="showModal()">新增</button>
                        <div class="table-responsive">
                            <table ng-table="userTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="width: 13px">
                                        <input type="checkbox" ng-model="selected[row.id]">
                                    </td>
                                    <td class="text-center" data-title="'用户名'">
                                        {{row.userName}}
                                    </td>
                                    <td class="text-center" data-title="'角色'">
                                        <strong>{{row.roleId | getValueByList:allRole:'id':'roleName'}}</strong>
                                    </td>
                                    <td class="text-center" data-title="'姓名'">
                                        {{row.realName}}
                                    </td>
                                    <td class="text-center" data-title="'手机'">
                                        {{row.mobile}}
                                    </td>
                                    <td class="text-center" data-title="'电话'">
                                        {{row.telphone}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.email}}
                                    </td>
                                    <td class="text-center p-6" style="max-width: 100px;" data-title="'操作'">
                                        <user-tb-btn row="{{row}}"
                                                     status-change="statusChange(row)"
                                                     edit="edit(row, type)"></user-tb-btn>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <button class="btn btn-sm btn-primary pull-left" ng-click="del()" ng-disabled="delDisabled()">
                            删除
                        </button>
                        <my-pagination table-param="userTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--<script type="text/ng-template" id="checkboxheader.html">
    <input type="checkbox" ng-model="checkboxes.checked">
</script>--%>
