
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
</style>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merchantAddCtrl">
        <div class="row">
            <div class="col-lg-12">
                <%--新增和修改--%>
                <div class="ibox float-e-margins" ng-show="goType == 0 || goType == 1">
                    <div class="ibox-title">
                        <h5 ng-show="goType == 0">{{'MERCHANT_ADD' | translate}}</h5>
                        <h5 ng-show="goType == 1">{{'MERCHANT_MANAGEMENT' | translate}}</h5>
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
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户号：</label>
                                <div class="col-lg-1">
                                    <input class="form-control b-r-sm"
                                           name="merId"
                                           type="text"
                                           ng-model="merchantInfo.merId"
                                           ng-blur="merIdBlur($event, merchantInfo.merId)"
                                           readonly
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>密钥：</label>
                                <div class="col-lg-2">
                                    <input class="form-control b-r-sm"
                                           name="secretKey"
                                           type="text"
                                           placeholder="必填"
                                           ng-model="merchantInfo.secretKey"
                                           ng-blur="secretKeyBlur($event, merchantInfo.secretKey)"
                                           required>
                                </div>
                                <button class="btn btn-sm btn-info" type="button" ng-click="getRandomSecretkey()">
                                    生成密钥
                                </button>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户名：</label>
                                <div class="col-lg-2">
                                    <input class="form-control b-r-sm"
                                           name="merchantName"
                                           type="text"
                                           placeholder="必填"
                                           ng-model="merchantInfo.merchantName"
                                           ng-blur="merchantNameBlur($event, merchantInfo.merchantName)"
                                           required>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户简称：</label>
                                <div class="col-lg-2">
                                    <input class="form-control b-r-sm"
                                           name="merchantShortName"
                                           type="text"
                                           ng-model="merchantInfo.merchantShortName">
                                </div>
                            </div>
                            <div class="form-group" ng-show="goType == 0">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>登录名称：</label>
                                <div class="col-lg-2">
                                    <input class="form-control b-r-sm"
                                           name="merchantShortName"
                                           type="text"
                                           ng-model="merchantInfo.loginName">
                                </div>
                            </div>
                            <div class="form-group" ng-show="goType == 0">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>登录密码：</label>
                                <div class="col-lg-2">
                                    <input class="form-control b-r-sm"
                                           name="merchantShortName"
                                           type="text"
                                           ng-model="merchantInfo.password">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户类型：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="type"
                                            ng-model="merchantInfo.type"
                                            ng-blur="typeBlur($event, merchantInfo.type)"
                                            ng-options="+(x.firstValue) as x.name for x in merchantType"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label">代理商：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm" ng-model="merchantInfo.parentId">
                                        <option ng-repeat="x in agentMerchants" value="{{x.id}}">{{x.name}}</option>
                                    </select>
                                       <%-- <select class="form-control b-r-sm"
                                                name="parentId"
                                                ng-model="merchantInfo.parentId"
                                                ng-options="+(x.id) as x.name for x in agentMerchants"
                                                required>
                                        </select>--%>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>证件类型：</label>
                                <div class="col-lg-2">
                                    <select class="form-control b-r-sm"
                                            name="identityType"
                                            ng-model="merchantInfo.identityType"
                                            ng-blur="identityTypeBlur($event, merchantInfo.identityType)"
                                            ng-options="+(x.firstValue) as x.name for x in agentIdentityTypeList"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>证件号码：</label>
                                <div class="col-lg-2">
                                    <input type="text"
                                           class="form-control input-sm"
                                           maxlength="20"
                                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                           name="identityNum"
                                           ng-model="merchantInfo.identityNum"
                                           ng-blur="identityNumBlur($event, merchantInfo.identityNum)"
                                           required>
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
                                        <a ng-repeat="x in certificateImgUrl" href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                                            <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                                                 class="img-size">
                                        </a>
                                        <a href="javascript:" class="a-upload img-size">
                                            <input type="file"
                                                   nv-file-select=""
                                                   uploader="uploader"
                                                   multiple
                                                   ng-disabled="merchantInfo.merId == null"
                                                   name="identityUrl"
                                                   ng-model="merchantInfo.identityUrl"
                                                   ng-blur="identityUrlBlur($event, merchantInfo.identityUrl)"
                                                   accept="image/*"
                                                   required>
                                            上传证件
                                        </a>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>电话：</label>
                                <div class="col-lg-2">
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
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>联系邮箱：</label>
                                <div class="col-lg-2">
                                    <input type="text"
                                           class="form-control input-sm"
                                           name="email"
                                           maxlength="50"
                                           ng-model="merchantInfo.email"
                                           ng-blur="emailBlur($event, merchantInfo.email)"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>QQ：</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control input-sm"
                                           name="qq"
                                           maxlength="50"
                                           oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                           ng-model="merchantInfo.qq"
                                           ng-blur="qqBlur($event, merchantInfo.qq)"
                                           required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>合同开始时间：</label>
                                <div class="col-lg-3">
                                    <div class="input-group m-b">
                                    <input type="text"
                                           name="agreementStarttime"
                                           class="form-control"
                                           ng-readonly="false"
                                           ng-click="openDatepicker1()"
                                           uib-datepicker-popup="{{format}}"
                                           is-open="popup1.opend"
                                           datepicker-options="dateOptions1"
                                           alt-input-formats="altInputFormats"
                                           close-text="关闭"
                                           clear-text="清空"
                                           current-text="今天"
                                           ng-model="merchantInfo.agreementStarttime"
                                           ng-blur="agreementStarttimeBlur($event, merchantInfo.agreementStarttime)"
                                           required
                                           >
                                        <span class="input-group-btn">
                                        <button type="button" class="btn btn-default no-margins"
                                                ng-click="openDatepicker1()">
                                            <i class="fa fa-calendar"></i>
                                        </button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>合同结束时间：</label>
                                <div class="col-lg-3">
                                    <div class="input-group m-b">
                                    <input type="text"
                                           name="agreementEndtime"
                                           class="form-control"
                                           ng-readonly="false"
                                           ng-click="openDatepicker2()"
                                           uib-datepicker-popup="{{format}}"
                                           is-open="popup2.opend"
                                           datepicker-options="dateOptions2"
                                           alt-input-formats="altInputFormats"
                                           close-text="关闭"
                                           clear-text="清空"
                                           current-text="今天"
                                           ng-model="merchantInfo.agreementEndtime"
                                           ng-blur="agreementEndtimeBlur($event, merchantInfo.agreementEndtime)"
                                            required
                                    >
                                        <span class="input-group-btn">
                                        <button type="button" class="btn btn-default no-margins"
                                                ng-click="openDatepicker2()">
                                            <i class="fa fa-calendar"></i>
                                        </button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-lg-6 center">
                                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(merchantInfo)"
                                            ng-disabled="checkDisabled(myForm)">
                                        确定
                                    </button>
                                    <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                                        取消
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                    <div class="ibox float-e-margins" ng-show="goType == 2 ">
                        <div class="ibox-title">
                            <h5 ng-show="goType == 2">{{'MERCHANT_CHECK' | translate}}</h5>
                        </div>
                        <div class="ibox-content">
                            <div class="sk-spinner sk-spinner-wave">
                                <div class="sk-rect1"></div>
                                <div class="sk-rect2"></div>
                                <div class="sk-rect3"></div>
                                <div class="sk-rect4"></div>
                                <div class="sk-rect5"></div>
                            </div>
                            <form name="myForm-status" class="form-horizontal">
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户号：</label>
                                    <div class="col-lg-1">
                                        <input class="form-control b-r-sm"
                                               name="merId"
                                               type="text"
                                               ng-model="merchantInfo.merId"
                                               ng-blur="merIdBlur($event, merchantInfo.merId)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户名：：</label>
                                    <div class="col-lg-2">
                                        <input class="form-control b-r-sm"
                                               name="merchantName"
                                               type="text"
                                               placeholder="必填"
                                               ng-model="merchantInfo.merchantName"
                                               ng-blur="merchantNameBlur($event, merchantInfo.merchantName)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>密钥：</label>
                                    <div class="col-lg-2">
                                        <input class="form-control b-r-sm"
                                               name="secretKey"
                                               type="text"
                                               placeholder="必填"
                                               ng-model="merchantInfo.secretKey"
                                               ng-blur="secretKeyBlur($event, merchantInfo.secretKey)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户简称：</label>
                                    <div class="col-lg-2">
                                        <input class="form-control b-r-sm"
                                               name="merchantShortName"
                                               type="text"
                                               readonly
                                               ng-model="merchantInfo.merchantShortName">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户类型：</label>
                                    <div class="col-lg-2">
                                        <select class="form-control b-r-sm"
                                                name="type"
                                                ng-model="merchantInfo.type"
                                                ng-blur="typeBlur($event, merchantInfo.type)"
                                                ng-options="+(x.firstValue) as x.name for x in merchantType"
                                                readonly
                                                required>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label">代理商：</label>
                                    <div class="col-lg-2">
                                        <select class="form-control b-r-sm"
                                                name="parentId"
                                                ng-model="merchantInfo.parentId"
                                                ng-options="+(x.agentMerchantId) as x.agentMerchantName for x in agentMerchants"
                                                readonly
                                        >
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>证件类型：</label>
                                    <div class="col-lg-2">
                                        <select class="form-control b-r-sm"
                                                name="identityType"
                                                ng-model="merchantInfo.identityType"
                                                ng-blur="identityTypeBlur($event, merchantInfo.identityType)"
                                                ng-options="+(x.firstValue) as x.name for x in agentIdentityTypeList"
                                                readonly
                                                required>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>证件号码：</label>
                                    <div class="col-lg-2">
                                        <input type="text"
                                               class="form-control input-sm"
                                               maxlength="20"
                                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                               name="identityNum"
                                               ng-model="merchantInfo.identityNum"
                                               ng-blur="identityNumBlur($event, merchantInfo.identityNum)"
                                               readonly
                                               required>
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
                                            <a ng-repeat="x in certificateImgUrl" href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                                                <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                                                     class="img-size">
                                            </a>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>电话：</label>
                                    <div class="col-lg-2">
                                        <input type="text"
                                               class="form-control input-sm"
                                               name="phone"
                                               maxlength="20"
                                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                               ng-model="merchantInfo.phone"
                                               ng-blur="phoneBlur($event, merchantInfo.phone)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>联系邮箱：</label>
                                    <div class="col-lg-2">
                                        <input type="text"
                                               class="form-control input-sm"
                                               name="email"
                                               maxlength="50"
                                               ng-model="merchantInfo.email"
                                               ng-blur="emailBlur($event, merchantInfo.email)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>QQ：</label>
                                    <div class="col-lg-2">
                                        <input type="text" class="form-control input-sm"
                                               name="qq"
                                               maxlength="50"
                                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                                               ng-model="merchantInfo.qq"
                                               ng-blur="qqBlur($event, merchantInfo.qq)"
                                               readonly
                                               required>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>合同开始时间：</label>
                                    <div class="col-lg-3">
                                        <div class="input-group m-b">
                                            <input type="text"
                                                   name="agreementStarttime"
                                                   class="form-control"
                                                   ng-readonly="true"
                                                   uib-datepicker-popup="{{format}}"
                                                   is-open="popup1.opend"
                                                   datepicker-options="dateOptions1"
                                                   alt-input-formats="altInputFormats"
                                                   close-text="关闭"
                                                   clear-text="清空"
                                                   current-text="今天"
                                                   ng-model="merchantInfo.agreementStarttime"
                                                   ng-blur="agreementStarttimeBlur($event, merchantInfo.agreementStarttime)"
                                                   required
                                            >
                                            <span class="input-group-btn">
                                        <button type="button" class="btn btn-default no-margins">
                                            <i class="fa fa-calendar"></i>
                                        </button>
                                    </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-lg-2 control-label"><span class="text-danger">*</span>合同结束时间：</label>
                                    <div class="col-lg-3">
                                        <div class="input-group m-b">
                                            <input type="text"
                                                   name="agreementEndtime"
                                                   class="form-control"
                                                   ng-readonly="true"
                                                   uib-datepicker-popup="{{format}}"
                                                   is-open="popup2.opend"
                                                   datepicker-options="dateOptions2"
                                                   alt-input-formats="altInputFormats"
                                                   close-text="关闭"
                                                   clear-text="清空"
                                                   current-text="今天"
                                                   ng-model="merchantInfo.agreementEndtime"
                                                   ng-blur="agreementEndtimeBlur($event, merchantInfo.agreementEndtime)"
                                                   required
                                            >
                                            <span class="input-group-btn">
                                        <button type="button" class="btn btn-default no-margins">
                                            <i class="fa fa-calendar"></i>
                                        </button>
                                    </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>审核状态：</label>
                                    <div class="col-sm-10">
                                        <input type="radio" name="statusName" value="0" ng-model="merchantInfo.status" ng-checked="agentStatusCheckqy"/>启用
                                        <input type="radio" name="statusName" value="1" ng-model="merchantInfo.status" ng-checked="agentStatusCheckjy"/>禁用
                                        <input type="radio" name="statusName" value="2" ng-model="merchantInfo.status" ng-checked="agentStatusCheckwsh"/>未审核
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-lg-6 center">
                                        <button class="btn btn-sm btn-success" type="button" ng-click="confirmStatus(merchantInfo)">
                                            确定
                                        </button>
                                        <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                                            取消
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
