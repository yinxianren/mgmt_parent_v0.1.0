<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/25
  Time: 10:33
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title" ng-switch="type">
        <div ng-switch-when="0">新增</div>
        <div ng-switch-when="1">编辑</div>
        <div ng-switch-when="2">重置密码</div>
    </h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="ibox-content no-borders tab-content">
        <form name="myForm" class="form-horizontal">
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>用户名：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text" ng-model="userInfo.userName"
                           ng-blur="userNameBlur($event, userInfo.userName)" ng-disabled="type == 1" required>
                </div>
            </div>
            <div class="form-group" ng-show="type == 0 || type == 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>密码：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="password" ng-model="userInfo.password"
                           ng-blur="passwordBlur($event, userInfo.password)" ng-change="passwordChange()" required>
                </div>
            </div>
            <div class="form-group" ng-show="type == 0 || type == 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>确认密码：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" id="passwordB" type="password" ng-model="userInfo.passwordB"
                           ng-blur="passwordBBlur($event, userInfo.passwordB)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>真实姓名：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" name="realName" type="text" ng-model="userInfo.realName"
                           ng-blur="realNameBlur($event, userInfo.realName)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>手机：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" name="mobile" type="text" ng-model="userInfo.mobile"
                           ng-blur="mobileBlur($event, userInfo.mobile)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>邮箱：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" name="email" type="text" ng-model="userInfo.email"
                           ng-blur="emailBlur($event, userInfo.email)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>角色：</label>
                <div class="col-sm-10">
                    <select class="form-control b-r-sm" name="roleId" ng-model="userInfo.roleId"
                            ng-options="x.id as x.roleName for x in roleList" required>
                    </select>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-success" type="button" ng-click="confirm()"
                        ng-disabled="confirmDisabled(myForm)">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>