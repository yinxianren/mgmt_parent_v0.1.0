<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/4/10
  Time: 17:08
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{changType == 1 ? '编辑' : '密码重置'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <div class="ibox-content no-borders tab-content">
            <div ng-show="tab == 1">
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>真实姓名：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" placeholder="必填" name="realName"
                               ng-model="changeInfo.realName" ng-blur="namesBlur($event, changeInfo.realName)" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>手机：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" placeholder="必填" name="mobile"
                               ng-model="changeInfo.mobile" ng-blur="phoneBlur($event, changeInfo.mobile)"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs">电话：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" ng-model="changeInfo.telphone"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>邮箱：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="email" placeholder="必填" name="email"
                               ng-model="changeInfo.email" ng-keyup="emailKeyUp(myForm.$error.email, changeInfo.email)"
                               ng-blur="emailBlur($event, changeInfo.email)" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs">
                        角色选择：
                    </label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm" ng-model="changeInfo.roleId">
                            <option ng-repeat="x in allRole" value="{{x.id}}">{{x.roleName}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div ng-show="tab == 2">
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>新密码：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="password" placeholder="必填" name="password"
                               ng-model="changeInfo.password" ng-keyup="passwordKeyUp($event, changeInfo.password)"
                               required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>确认密码：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="password" placeholder="必填" name="passwordB"
                               ng-model="passwordB" ng-keyup="passwordBKeyUp($event, passwordB, changeInfo.password)"
                               required>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="modal-footer">
    <div class="text-center">
        <button class="btn btn-sm btn-primary" type="button" ng-click="changeUser(changeInfo)"
                ng-disabled="nextDisable(myForm, tab)">
            完成
        </button>
    </div>
</div>