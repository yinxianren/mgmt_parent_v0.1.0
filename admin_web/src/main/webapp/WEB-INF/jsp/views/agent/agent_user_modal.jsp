<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/26
  Time: 9:23
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">用户注册</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <div class="ibox-content no-borders tab-content">
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>用户名：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text" placeholder="必填" name="userName"
                           ng-model="userInfo.userName" ng-blur="userNameBlur($event, userInfo.userName)"
                           required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 1">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>密码：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="password" placeholder="必填" name="password"
                           ng-model="userInfo.password"
                           ng-blur="passwordBlur($event, userInfo.password)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 1">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>确认密码：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="password" placeholder="必填" name="passwordB"
                           ng-model="passwordB"
                           ng-blur="passwordBBlur($event, passwordB, userInfo.password)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>手机：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text" placeholder="必填" name="mobile"
                           ng-model="userInfo.mobile"
                           ng-blur="phoneBlur($event, userInfo.mobile)"
                           oninput="inputRegex(this, /[^*()+\-\d]/g)" required>
                </div>
            </div>
            <%--<div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs">电话：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text" ng-model="userInfo.telphone"
                           oninput="inputRegex(this, /[^*()+\-\d]/g)">
                </div>
            </div>--%>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>邮箱：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="email" placeholder="必填" name="email"
                           ng-model="userInfo.email"
                           ng-blur="emailBlur($event, userInfo.email)" required>
                </div>
            </div>
            <div class="form-group" ng-show="type != 2">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>角色：</label>
                <div class="col-sm-10">
                    <select class="form-control b-r-sm" name="role"
                            ng-model="userInfo.roleId"
                            ng-blur="roleBlur($event, userInfo.email)"
                            ng-options="x.id as x.roleName for x in merchantRole"
                            required>
                    </select>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div class="text-center">
        <button class="btn btn-sm btn-primary" type="button" ng-click="confirm()" ng-disabled="confirmDisabled(myForm)">
            完成
        </button>
    </div>
</div>
