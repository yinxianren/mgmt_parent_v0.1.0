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
        <h3 class="modal-title" id="modal-title">{{ goType == 0 ? '添加' : '修改商户'}}</h3>
    </div>
    <div class="modal-body" id="modal-body">
        <form name="myForm" class="form-horizontal">
            <div class="ibox-content no-borders tab-content">
                <uib-tabset active="activeForm">
                    <uib-tab index="0" classes="m-b">
                        <uib-tab-heading><i class="fa fa-user"></i>&nbsp;基本信息</uib-tab-heading>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户：</label>
                            <div class="col-sm-10">
                                <input class="form-control b-r-sm"
                                       name="merId"
                                       type="text"
                                       ng-model="merchantInfo.merId"
                                       ng-blur="merIdBlur($event, merchantInfo.merId)"
                                       readonly
                                       required>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户名：</label>
                            <div class="col-sm-10">
                                <input class="form-control b-r-sm"
                                       name="merchantName"
                                       type="text"
                                       placeholder="必填"
                                       ng-model="merchantInfo.merchantName"
                                       ng-blur="merchantNameBlur($event, merchantInfo.merchantName)"
                                       required>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs">商户简称：</label>
                            <div class="col-sm-10">
                                <input class="form-control b-r-sm"
                                       name="merchantShortName"
                                       type="text"
                                       ng-model="merchantInfo.merchantShortName"
                                >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户类型：</label>
                            <div class="col-sm-10">
                                <select class="form-control b-r-sm"
                                        name="type"
                                        ng-model="merchantInfo.type"
                                        ng-blur="identityTypeBlur($event, merchantInfo.type)"
                                        ng-options="+(x.id) as x.name for x in merchantType"
                                        required>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>证件类型：</label>
                            <div class="col-sm-10">
                                <select class="form-control b-r-sm"
                                        name="identityType"
                                        ng-model="merchantInfo.identityType"
                                        ng-blur="identityTypeBlur($event, merchantInfo.identityType)"
                                        ng-options="+(x.id) as x.name for x in agentIdentityTypeList"
                                        required>
                                </select>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>证件号码：</label>
                            <div class="col-sm-10">
                                <input type="text"
                                       class="form-control input-sm"
                                       name="identityNum"
                                       maxlength="20"
                                       oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                       ng-model="merchantInfo.identityNum"
                                       >
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>证件：</label>
                            <div class="col-sm-10">
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
                                    <a ng-repeat="x in certificateImgUrl" href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                                        <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                                             class="img-size">
                                    </a>
                                    <a href="javascript:" class="a-upload img-size">
                                        <input type="file" nv-file-select="" uploader="uploader" multiple
                                               ng-disabled="merchantInfo.merId == null"
                                               accept="image/*">上传证件
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>电话：</label>
                            <div class="col-sm-10">
                                <input type="text"
                                       class="form-control input-sm"
                                       name="phone"
                                       maxlength="20"
                                       oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                       ng-model="merchantInfo.phone"
                                       ng-blur="phoneBlur($event, merchantInfo.phone)"
                                       required>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>联系邮箱：</label>
                            <div class="col-sm-10">
                                <input type="text"
                                       class="form-control input-sm"
                                       name="email"
                                       maxlength="50"
                                       ng-model="merchantInfo.email"
                                       ng-blur="emailBlur($event, merchantInfo.email)"
                                       required>
                            </div>
                        </div>
                        <div class="form-group  m-t">
                            <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>QQ：</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control input-sm"
                                       name="qq"
                                       maxlength="50"
                                       oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                       ng-model="merchantInfo.qq"
                                       ng-blur="qqBlur($event, merchantInfo.qq)"
                                       required>
                            </div>
                        </div>
                    </uib-tab>
                </uib-tabset>
            </div>
        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(merchantInfo)"
                            ng-disabled="checkDisabled(myForm)"
                        >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
