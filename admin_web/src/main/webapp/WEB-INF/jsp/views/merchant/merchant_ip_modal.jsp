
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">IP配置</h3>
</div>
<div ng-if="types == 0" >
    <div class="modal-body" id="modal-body">
        <div class="ibox-content no-borders tab-content">
            <form name="myForm" class="form-horizontal">
                <div class="form-group" ng-show="type != 2">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>关联商户：</label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm" name="status" ng-model="UserLoginIp.customerId"
                                ng-blur="merIdBlur($event, UserLoginIp.customerId)"
                                ng-options="x.merchantId as x.merchantId + '(' + x.merchantName + ')' for x in customers | filter: {id : $viewValue}" required>
                                <%--ng-options="x.id as x.name for x in merchantInfo" required>--%>
                        </select>
                    </div>
                </div>
                <div class="form-group" ng-show="type != 2">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户IP：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" name="loginIp" placeholder="必填"
                               ng-model="UserLoginIp.loginIp" ng-blur="siteUrlBlur($event, UserLoginIp.loginIp)"
                               required>
                    </div>
                </div>
            </form>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-primary" type="button" ng-click="confirm(UserLoginIp)"
                             >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div ng-if="types == 1" >
    <div class="modal-body" id="modal-body1">
        <div class="ibox-content no-borders tab-content">
            <form name="myForm" class="form-horizontal">
                <div class="form-group" ng-show="type != 2">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>关联商户：</label>
                    <div class="col-sm-10">
                        <select class="form-control b-r-sm" name="status" ng-model="UserLoginIp.customerId"
                                ng-blur="merIdBlur($event, UserLoginIp.customerId)"
                                ng-options="x.agentMerchantId as x.agentMerchantId + '(' + x.agentMerchantName + ')' for x in customers | filter: {id : $viewValue}" required>
                            <%--ng-options="x.id as x.name for x in merchantInfo" required>--%>
                        </select>
                    </div>
                </div>
                <div class="form-group" ng-show="type != 2">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户IP：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm" type="text" name="loginIp" placeholder="必填"
                               ng-model="UserLoginIp.loginIp" ng-blur="siteUrlBlur($event, UserLoginIp.loginIp)"
                               required>
                    </div>
                </div>
            </form>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-primary" type="button" ng-click="confirm(UserLoginIp)"
                    >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>