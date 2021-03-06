<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/14
  Time: 15:13
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{type == 0 ? '角色新增' : '角色编辑'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="ibox-content no-borders tab-content">
        <form name="myForm" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>角色名称：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text" name="roleName" placeholder="必填"
                           ng-model="roleInfo.roleName" ng-blur="nameBlur($event, roleInfo.roleName)" required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs">权限设置：</label>
                <div class="col-sm-10">
                    <div class="form-group m-b-xs" ng-repeat="x in menu">
                        <label class="control-label p-w-xs">{{x.description}}：</label>
                        <div class="clearfix">
                            <div class="col-sm-4 p-w-xs" ng-repeat="y in x.submenu">
                                <div class="checkbox">
                                    <input id="{{y.id}}" name="privileges" type="checkbox"
                                           ng-checked="exists(y.id, roleInfo.privilegesList)"
                                           ng-click="toggle(y.id, roleInfo.privilegesList)">
                                    <label for="{{y.id}}" class="no-padding">{{y.description}}</label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs">备注：</label>
                <div class="col-sm-10">
                    <textarea class="form-control b-r-sm" ng-model="roleInfo.remark"
                              style="resize: none;height: 100px"></textarea>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary" type="button" ng-click="confirmBtn()"
                        ng-disabled="nextDisabled(myForm)">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>