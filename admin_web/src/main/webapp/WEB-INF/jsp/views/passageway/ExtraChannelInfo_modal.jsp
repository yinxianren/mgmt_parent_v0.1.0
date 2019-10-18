<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/5/9
  Time: 11:47
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '添加附属通道信息' : '修改附属通道信息'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>通道名称：</label>
            <div class="col-sm-10">
                <input class="form-control b-r-sm"
                       name="extraChannelName" type="text" placeholder="必填"
                       ng-model="ExtraChannelInfo.extraChannelName"
                       ng-blur="extraChannelNameBlur($event, ExtraChannelInfo.extraChannelName)"
                       required>
            </div>
        </div>
        <div class="form-group m-t">
            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>请求地址：</label>
            <div class="col-sm-10">
                <input class="form-control b-r-sm"
                       name="url" type="text" placeholder="必填"
                       ng-model="ExtraChannelInfo.url"
                       ng-blur="nameBlur($event, ExtraChannelInfo.url)"
                       required>
            </div>
        </div>
        <div class="form-group m-t">
            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>参数：</label>
            <div class="col-sm-10">
                <input class="form-control b-r-sm"
                       name="data" type="text" placeholder="必填"
                       ng-model="ExtraChannelInfo.data"
                       ng-blur="nameBlur($event, ExtraChannelInfo.data)"
                       required>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>通道类型：</label>
            <div class="col-sm-10">
                <select class="form-control b-r-sm" ng-model="ExtraChannelInfo.type">
                    <option ng-repeat="x in extraTypes" value="{{x.firstValue}}">{{x.name}}</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>机构：</label>
            <div class="col-sm-10">
                <select class="form-control b-r-sm" ng-model="ExtraChannelInfo.organizationId">
                    <option ng-repeat="x in organizations" value="{{x.organizationId}}">{{x.organizationName}}</option>
                </select>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-success" type="button" ng-click="addOrganization(ExtraChannelInfo)">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
