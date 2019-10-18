<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/4/2
  Time: 19:10
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title text-center" id="modal-title">代理商余额提现</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <div class="ibox-content" style="border-style:none;">
            <div class="sk-spinner sk-spinner-wave">
                <div class="sk-rect1"></div>
                <div class="sk-rect2"></div>
                <div class="sk-rect3"></div>
                <div class="sk-rect4"></div>
                <div class="sk-rect5"></div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>代理商：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control"
                           value="{{order.agentMerchantId}}"
                           ng-readonly="true">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>可提现金额：</label>
                <div class="col-sm-9">
                    <input type="text" ng-readonly="true" class="form-control"
                           value="{{order.totalAvailableAmount?order.totalAvailableAmount:0.00 | number:2}}">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>操作类型：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" value="提现" ng-readonly="true">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>提现金额：</label>
                <div class="col-sm-9">
                    <div class="input-group">

                            <input type="number" class="form-control" ng-model="order.withdrawalAmount" ng-value="Amountinit"
                                   oninput="inputRegex(this, /[^[0-9]\d*\.\d*|0\.\d*[0-9]\d*$]/g)"
                               ng-change="amountChange()" required>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary" type="submit" ng-disabled="confirmDisabled(myForm)"
                        ng-click="confirm()">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
