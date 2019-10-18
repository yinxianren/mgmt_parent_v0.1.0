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
<div>
    <div class="modal-header">
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">结算账号信息</h3>
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
                               ng-model="merchantAccount.merId"
                               ng-blur="merIdBlur($event, merchantAccount.merId)"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>收款主体名称：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm"
                               name="benefitName"
                               type="text"
                               placeholder="必填"
                               ng-model="merchantAccount.benefitName"
                               ng-blur="benefitNameBlur($event, merchantAccount.benefitName)"
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>银行名称：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm"
                               name="bankName"
                               type="text"
                               placeholder="必填"
                               ng-model="merchantAccount.bankName"
                               ng-blur="bankNameBlur($event, merchantAccount.bankName)"
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>银行卡号：</label>
                    <div class="col-sm-10">
                        <input type="text"
                               class="form-control input-sm"
                               name="bankcardNum"
                               maxlength="20"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                               ng-model="merchantAccount.bankcardNum"
                               ng-blur="bankcardNumBlur($event, merchantAccount.bankcardNum)"
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>收款银行网点名称：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm"
                               name="bankBranchName"
                               type="text"
                               ng-model="merchantAccount.bankBranchName"
                               ng-blur="bankBranchNameBlur($event, merchantAccount.bankBranchName)"
                               >
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>收款行开户行号：</label>
                    <div class="col-sm-10">
                        <input type="text"
                               class="form-control input-sm"
                               name="bankBranchNum"
                               maxlength="20"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                               ng-model="merchantAccount.bankBranchNum"
                               ng-blur="bankBranchNumBlur($event, merchantAccount.bankBranchNum)"
                               required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>证件类型：</label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm"
                                name="identityType"
                                ng-model="merchantAccount.identityType"
                                ng-blur="identityTypeBlur($event, merchantAccount.identityType)"
                                ng-options="+(x.firstValue) as x.name for x in agentIdentityTypeList"
                                required>
                        </select>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>证件号码：</label>
                    <div class="col-sm-10">
                        <input type="text"
                               class="form-control input-sm"
                               maxlength="20"
                               oninput="inputRegex(this, /[^*()+\-\d]/g)"
                               name="identityNum"
                               ng-model="merchantAccount.identityNum"
                               ng-blur="identityNumBlur($event, merchantAccount.identityNum)"
                               required>
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
                                <input type="file"
                                       nv-file-select=""
                                       uploader="uploader"
                                       multiple
                                       ng-disabled="merchantAccount.merId == null"
                                       name="identityUrl1"
                                       ng-model="merchantAccount.identityUrl1"
                                       ng-blur="identityUrl1Blur($event, merchantAccount.identityUrl1)"
                                       accept="image/*"
                                       required>
                                上传证件
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(merchantAccount)"
                            ng-disabled="checkDisabled(myForm)"
                    >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
