<%--
  Created by IntelliJ IDEA.
  User: hul
  Date: 2019/5/18
  Time: 14:34
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
    .img-size {
        height: 136px;
        width: 136px;
    }
    .a-upload {
        text-align: center;
        line-height: 128px;
        position: relative;
        cursor: pointer;
        color: #888;
        background: #f2f2f2;
        border: 1px solid #ddd;
        display: inline-block;
        *display: inline;
        *zoom: 1
    }
    .a-upload input {
        width: 136px;
        height: 136px;
        position: absolute;
        left: 0;
        top: 0;
        opacity: 0;
        cursor: pointer
    }

    .a-upload:hover {
        color: #444;
        background: #eee;
        border-color: #ccc;
        text-decoration: none
    }
</style>
<%--<div ng-show="goType == 0 || goType == 1 ">--%>
<div>
    <div class="modal-header">
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">通道配置信息</h3>
    </div>
    <div class="modal-body" id="modal-body">
        <form name="myForm" class="form-horizontal">
            <div class="ibox-content no-borders tab-content">
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户号：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="merchantSetting.merId"
                               ng-blur="merIdBlur($event, merchantSetting.merId)"
                               readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>支付机构：</label>
                    <div class="col-sm-10">
                        <div class="col-sm-3 p-w-xs" ng-repeat="x in organizations">
                            <div class="checkbox no-margins">
                                <input id="{{x.organizationId}}" type="checkbox" name="currency" onclick="return false"
                                       ng-checked="exists(x.organizationId,merchantOrganizations)"
                                       ng-click="orgtoggle(x.organizationId, merchantOrganizations)"
                                       readonly>
                                <label for="{{x.organizationId}}" class="no-padding">{{x.organizationName}}</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>支付通道：</label>
                    <div class="col-sm-10">
                        <div class="col-sm-3 p-w-xs" ng-repeat="x in channels">
                            <div class="checkbox no-margins">
                                <input id="{{x.channelId}}" type="checkbox" name="currency" onclick="return false"
                                       ng-checked="exists(x.channelId, merchantChannels)"
                                       ng-click="channletoggle(x.channelId, merchantChannels)"
                                       readonly>
                                <label for="{{x.channelId}}" class="no-padding">{{x.channelName}}</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>通道等级：</label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm"
                                name="identityType"
                                ng-model="merchantSetting.channelLevel"
                                ng-blur="identityTypeBlur($event, merchantSetting.channelLevel)"
                                ng-options="+(x.firstValue) as x.name for x in channelLevel"
                                disabled>
                        </select>
                    </div>
                </div>
            </div>
        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(merchantSetting)"
                    >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
