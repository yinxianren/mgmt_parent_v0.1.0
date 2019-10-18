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
    <h3 class="modal-title" id="modal-title">订单号&nbsp;{{order.payId}}&nbsp;变更：</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <div class="ibox-content    ">
            <div class="sk-spinner sk-spinner-wave">
                <div class="sk-rect1"></div>
                <div class="sk-rect2"></div>
                <div class="sk-rect3"></div>
                <div class="sk-rect4"></div>
                <div class="sk-rect5"></div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>商户号：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control"
                           value="{{order.merId}}"
                           ng-readonly="true">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>可退款金额：</label>
                <div class="col-sm-9">
                    <input type="text" ng-readonly="true" class="form-control"
                           value="{{order.realAmount + ' '}}{{maxAmount | number:2}}">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>订单金额：</label>
                <div class="col-sm-9">
                    <input type="text" ng-readonly="true" class="form-control"
                           value="{{order.amount + ' '}}{{amount | number:2}}">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>操作类型：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control" value="退款" ng-readonly="true">
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-3 control-label"><span class="text-danger">*</span>退款金额：</label>
                <div class="col-sm-9">
                    <div class="input-group">
                        <input type="number" class="form-control" ng-model="order.changeAmount" ng-value="Amountinit"
                               oninput="inputRegex(this, /[^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$]/g)"
                               ng-change="amountChange()" required>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-3 control-label">说明：</label>
                <div class="col-sm-9">
                    <%--<input type="text" ng-model="order.remark" class="form-control" required>--%>
                    <div class="input-group">

                        <input type="text" class="form-control" ng-model="order.remark" ng-value="remarkinit"
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
