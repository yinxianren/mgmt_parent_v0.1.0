<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/4/1
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">用户注册</h3>
</div>
<div class="modal-body" id="modal-body">
    <ul class="nav nav-tabs">
        <li ng-class="{'active' : tab == 1}">
            <a ng-click="register != null ? tab = 1 : tab" ng-init="tab = 1">
                <i class="fa fa-user"></i>&nbsp;账户信息
            </a>
        </li>
        <li ng-class="{'active' : tab == 2}">
            <a ng-click="!myForm.$error.required ? tab = 2 : tab">
                <i class="fa fa-drivers-license"></i>&nbsp;基本信息
            </a>
        </li>
        <li ng-class="{'active' : tab == 3}">
            <a ng-click="!myForm.$error.required && !myForm.$error.email ? tab = 3 : tab">
                <i class="fa fa-users"></i>&nbsp;角色选择
            </a>
        </li>
    </ul>
    <form name="myForm" class="form-horizontal">
        <div class="ibox-content no-borders tab-content">
            <div id="tab-1" class="active" ng-show="tab == 1">
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>用户名：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" placeholder="必填" name="userName"
                               ng-model="register.userName" ng-blur="userNameBlur($event, register.userName)"
                               required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>密码：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="password" placeholder="必填" name="password"
                               ng-model="register.password" ng-keyup="passwordKeyUp($event, register.password)"
                               ng-blur="passwordBlur($event, register.password)" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>确认密码：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="password" placeholder="必填" name="passwordB"
                               ng-model="passwordB" ng-keyup="passwordBKeyUp($event, passwordB, register.password)"
                               ng-blur="passwordBBlur($event, register.password)" required>
                    </div>
                </div>
            </div>
            <div id="tab-2" class="active" ng-show="tab == 2">
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>真实姓名：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" placeholder="必填" name="realName"
                               ng-model="register.realName" ng-blur="namesBlur($event, register.realName)" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>手机：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" placeholder="必填" name="mobile"
                               ng-model="register.mobile" ng-blur="phoneBlur($event, register.mobile)"
                               ng-keyup="myRegex($event, '[^*()+\\-\\d]')" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs">电话：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" ng-model="register.telphone"
                               ng-keyup="myRegex($event, '[^*()+\\-\\d]')">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>邮箱：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="email" placeholder="必填" name="email"
                               ng-model="register.email"
                               ng-keyup="emailKeyUp($event, myForm.$error.email, register.email)"
                               ng-blur="emailBlur($event, register.email)" required>
                    </div>
                </div>
            </div>
            <div id="tab-3" class="active" ng-show="tab == 3">
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs">角色选择：</label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm" ng-model="register.roleId">
                            <option ng-repeat="x in allRole" value="{{x.id}}">{{x.roleName}}</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div class="text-center">
        <button class="btn btn-sm" ng-class="{'btn-primary' : tab != 3 , 'btn-success' : tab == 3}"
                type="button" ng-click="nextBtn(tab)" ng-disabled="nextDisable(myForm, tab)">
            {{tab != 3 ? '下一步' : '完成'}}
        </button>
    </div>
</div>