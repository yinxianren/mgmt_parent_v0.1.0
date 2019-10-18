<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/17
  Time: 10:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--<style type="text/css">
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
        /*overflow: hidden;*/
        display: inline-block;
        *display: inline;
        *zoom: 1
    }

    .a-upload input {
        width: 136px;
        height: 136px;
        position: absolute;
        /*font-size: 100px;*/
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
</style>--%>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="baseInformationCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'BASE_INFORMATION' | translate}}</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <form name="myForm" class="form-horizontal">
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户：</label>
                                <div class="col-lg-1">
                                    <input type="text" class="form-control input-sm" ng-model="merchantInfo.id"
                                           ng-disabled="true">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>姓名：</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control input-sm"
                                           name="merName"
                                           maxlength="50"
                                           ng-model="merchantInfo.name"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户类型：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="category"
                                            ng-model="merchantInfo.category"
                                            ng-options="+(x.firstValue) as x.name for x in merchantCategory"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>省份：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="province"
                                            ng-model="merchantInfo.province"
                                            ng-change="provinceChange()"
                                            ng-options="x.id as x.name for x in provinceInfo"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>城市：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="city"
                                            ng-model="merchantInfo.city"
                                            ng-change="cityChange()"
                                            ng-disabled="merchantInfo.province == null || cityInfo == null"
                                            ng-options="x.id as x.name for x in cityInfo"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>区县：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="area"
                                            ng-model="merchantInfo.area"
                                            ng-disabled="merchantInfo.city == null || areaInfo == null"
                                            ng-options="x.id as x.name for x in areaInfo"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>详细地址：</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control input-sm"
                                           name="address"
                                           maxlength="200"
                                           ng-model="merchantInfo.address"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>联系人：</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control input-sm"
                                           name="contacts"
                                           maxlength="50"
                                           ng-model="merchantInfo.contacts"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>电话：</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control input-sm"
                                           name="phone"
                                           maxlength="50"
                                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                           ng-model="merchantInfo.phone"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>邮箱：</label>
                                <div class="col-lg-3">
                                    <input type="email" class="form-control input-sm"
                                           name="email"
                                           maxlength="50"
                                           ng-model="merchantInfo.email"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label">传真：</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control input-sm"
                                           name="fax"
                                           maxlength="50"
                                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                           ng-model="merchantInfo.fax"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>QQ：</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control input-sm"
                                           name="qq"
                                           maxlength="20"
                                           oninput="inputRegex(this, /\D/g)"
                                           ng-model="merchantInfo.qq">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>证件：</label>
                                <div class="col-lg-10">
                                    <!-- The Gallery as lightbox dialog, should be a child element of the document body -->
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
                                           href="/merchant/certificateImg?certificate={{x}}" data-gallery="">
                                            <img ng-src="{{'/merchant/certificateImg?certificate='+x}}"
                                                 class="img-size">
                                        </a>
                                        <a href="javascript:" class="a-upload img-size">
                                            <input type="file" nv-file-select="" uploader="uploader" multiple
                                                   accept="image/*">上传证件
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-lg-6 center">
                                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm()"
                                            ng-disabled="confirmDisabled(myForm)">
                                        确定
                                    </button>
                                    <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                                        返回
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
