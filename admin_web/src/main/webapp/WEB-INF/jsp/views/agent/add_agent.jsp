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
<div ng-show="goType == 0 || goType == 1 ">
    <div class="modal-header">
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">{{ goType == 0 ? '添加代理商' : '修改代理商'}}</h3>
    </div>
    <div class="modal-body" id="modal-body" >
        <form name="myForm" class="form-horizontal">

            <div class="form-group  m-t" ng-show="{{goType == 0 ?false:true}}">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>代理商编号：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentMerchantId"
                           type="text"
                           ng-model="agentMerchantInfo.agentMerchantId"
                           ng-blur="agentMerchantIdBlur($event, agentMerchantInfo.agentMerchantId)"
                           readonly
                           required>
                </div>
                <div class="p-xxs" ng-show="isExist"><span class="text-danger">代理商编号已存在</span></div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                        class="text-danger">*</span>名称：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentMerchantName"
                           type="text"
                           placeholder="必填"
                           ng-model="agentMerchantInfo.agentMerchantName"
                           ng-blur="agentMerchantNameBlur($event, agentMerchantInfo.agentMerchantName)"
                           required>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left">简称：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentMerchantShortName"
                           type="text"
                           ng-model="agentMerchantInfo.agentMerchantShortName"
                    >
                </div>
            </div>
            <%--<div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left">商户登录名：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentMerchantShortName"
                           type="text"
                           ng-model="agentMerchantInfo.loginName"
                    >
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left">登录密码：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentMerchantShortName"
                           type="text"
                           ng-model="agentMerchantInfo.password"
                    >
                </div>
            </div>--%>

            <div class="form-group">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>证件类型：</label>
                <div class="col-sm-9">
                    <select class="form-control b-r-sm"
                            name="agentIdentityType"
                            ng-model="agentMerchantInfo.agentIdentityType"
                            ng-blur="agentIdentityTypeBlur($event, agentMerchantInfo.agentIdentityType)"
                            ng-options="+(x.firstValue) as x.name for x in agentIdentityTypeList"
                            required>
                    </select>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>证件号码：</label>
                <div class="col-sm-9">
                    <input class="form-control b-r-sm"
                           name="agentIdentityNum"
                           type="text"
                           placeholder="必填"
                           oninput="inputRegex(this, /[^\d|abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]/g)"
                           ng-model="agentMerchantInfo.agentIdentityNum"
                           ng-blur="agentIdentityNumBlur($event, agentMerchantInfo.agentIdentityNum)"
                           required>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                        class="text-danger">*</span>证件：</label>
                <div class="col-sm-9">
                    <div id="blueimp-gallery" class="blueimp-gallery">
                        <div class="slides"></div>
                        <h3 class="title"></h3>
                        <a class="prev">‹</a>
                        <a class="next">›</a>
                        <a class="close">×</a>
                        <a class="play-pause"></a>
                        <ol class="indicator"></ol>
                    </div>
                    <div>
                        <a ng-repeat="x in certificateImgUrl"
                           href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                            <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                                 class="img-size">
                        </a>
                        <a href="javascript:" class="a-upload img-size">
                            <input type="file" nv-file-select=""
                                   uploader="uploader"
                                   multiple
                                   ng-disabled="agentMerchantInfo.agentMerchantId == null"
                                   name="agentIdentityPath"
                                   ng-model="agentMerchantInfo.agentIdentityPath"
                                   ng-blur="agentIdentityUrlBlur($event, agentMerchantInfo.agentIdentityPath)"
                                   accept="image/*"
                                   required>
                            上传证件
                        </a>
                    </div>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                        class="text-danger">*</span>电话：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control input-sm"
                           name="phone"
                           maxlength="50"
                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                           ng-model="agentMerchantInfo.phone"
                           ng-blur="agentPhoneBlur($event, agentMerchantInfo.phone)"
                           required>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>联系邮箱：</label>
                <div class="col-sm-9">
                    <input type="text"
                           class="form-control input-sm"
                           name="email"
                           maxlength="50"
                           ng-model="agentMerchantInfo.email"
                           ng-blur="agentEmailBlur($event, agentMerchantInfo.email)"
                           required>
                </div>
            </div>
            <div class="form-group  m-t">
                <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                        class="text-danger">*</span>QQ：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control input-sm"
                           name="qq"
                           maxlength="50"
                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                           ng-model="agentMerchantInfo.qq"
                           ng-blur="agentQqBlur($event, agentMerchantInfo.qq)"
                           required>
                </div>
            </div>

        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(agentMerchantInfo)"
                            ng-disabled="nextDisabled(myForm)">
                        确定
                    </button>
                    <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                        取消
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div ng-show="goType == 2">
    <div class="modal-header">
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title-status">审核代理商</h3>
    </div>
    <div class="modal-body" id="modal-body-status">
        <form name="myForm-status" class="form-horizontal">
            <div class="ibox-content no-borders tab-content">
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>代理商编号：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm"
                               name="agentMerchantId"
                               type="text"
                               placeholder="必填"
                               ng-model="agentMerchantInfo.agentMerchantId"
                               ng-blur="agentMerchantIdBlur($event, agentMerchantInfo.agentMerchantId)"
                               readonly
                               required
                        >
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                            class="text-danger">*</span>名称：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm"
                               name="agentMerchantName"
                               type="text"
                               placeholder="必填"
                               ng-model="agentMerchantInfo.agentMerchantName"
                               ng-blur="agentMerchantNameBlur($event, agentMerchantInfo.agentMerchantName)"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">简称：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm"
                               name="agentMerchantShortName"
                               type="text"
                               ng-model="agentMerchantInfo.agentMerchantShortName"
                               readonly
                        >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>证件类型：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm"
                                name="agentIdentityType"
                                ng-model="agentMerchantInfo.agentIdentityType"
                                ng-blur="agentIdentityTypeBlur($event, agentMerchantInfo.agentIdentityType)"
                                ng-options="+(x.firstValue) as x.name for x in agentIdentityTypeList"
                                readonly
                                required>
                        </select>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>证件号码：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm"
                               name="agentIdentityNum" type="text" placeholder="必填"
                               ng-model="agentMerchantInfo.agentIdentityNum"
                               name="agentMerchantInfo.agentIdentityNum"
                               ng-blur="agentIdentityNumBlur($event, agentMerchantInfo.agentIdentityNum)"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                            class="text-danger">*</span>证件：</label>
                    <div class="col-sm-9">
                        <div id="blueimp-gallery-status" class="blueimp-gallery">
                            <div class="slides"></div>
                            <h3 class="title"></h3>
                            <a class="prev">‹</a>
                            <a class="next">›</a>
                            <a class="close">×</a>
                            <a class="play-pause"></a>
                            <ol class="indicator"></ol>
                        </div>
                        <div>
                            <a ng-repeat="x in certificateImgUrl"
                               href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                                <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                                     class="img-size">
                            </a>
                        </div>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                            class="text-danger">*</span>电话：</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control input-sm"
                               name="agentMerchantInfo.phone"
                               maxlength="50"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                               ng-model="agentMerchantInfo.phone"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>联系邮箱：</label>
                    <div class="col-sm-9">
                        <input type="text"
                               class="form-control input-sm"
                               name="email"
                               maxlength="50"
                               ng-model="agentMerchantInfo.email"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span
                            class="text-danger">*</span>QQ：</label>
                    <div class="col-sm-9">
                        <input type="text" class="form-control input-sm"
                               name="qq"
                               maxlength="50"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                               ng-model="agentMerchantInfo.qq"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>审核状态：</label>
                    <div class="col-sm-9">
                        <input type="radio" name="status" value="0" ng-model="agentMerchantInfo.status"
                               ng-checked="agentStatusCheckqy"/>启用
                        <input type="radio" name="status" value="1" ng-model="agentMerchantInfo.status"
                               ng-checked="agentStatusCheckjy"/>禁用
                        <input type="radio" name="status" value="2" ng-model="agentMerchantInfo.status"
                               ng-checked="agentStatusCheckwsh"/>未审核
                    </div>
                </div>
            </div>
        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirmStatus(agentMerchantInfo)">
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
