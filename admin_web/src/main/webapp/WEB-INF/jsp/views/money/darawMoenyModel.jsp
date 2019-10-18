
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">划款</h3>
</div>

<div class="modal-body" id="modal-body">
    <div class="ibox-content no-borders tab-content">
        <form name="myForm" class="form-horizontal formTopSpacing" ng-model="row">
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">提现流水号：</label>
                <div class="col-sm-5">
                    <input id="merId" name="merId" type="text" class="form-control" ng-model="row.id" readonly>
                </div>
            </div>
            <!--商户名称-->
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">用户编号：</label>
                <div class="col-sm-5">
                    <input id="merName" name="merName" type="text" class="form-control" ng-model="row.customerId"
                           readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">提现金额: </label>
                <div class="col-sm-5">
                    <input id="currency" name="currency" type="text" class="form-control" ng-model="row.drawingAmount"
                           readonly>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">划款金额：</label>
                <div class="col-sm-5">
                    <input id="amount" name="amount" type="text" class="form-control" ng-model="row.transferAmount">
                    <span   ng-show="row.transferAmount>row.drawingAmount">
                <font color="red">*划款金额不得大于提现金额</font>
                    </span>
                    <span   ng-show="row.transferAmount<0">
                <font color="red">*划款金额不得小于0</font>
                    </span>
                    <span   ng-show="row.transferAmount==0">
                <font color="red">*划款金额不得等于0</font>
                    </span>
                </div>
            </div>

            <!--申请人-->
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">申请人: </label>
                <div class="col-sm-5">
                    <input id="applicant" name="applicant" type="text" class="form-control" ng-model="row.applicant "
                           readonly>
                </div>
            </div>
            <!--申请时间-->
            <div class="form-group">
                <label class="col-sm-4 control-label formTextVerticalMiddle">申请时间: </label>
                <div class="col-sm-5">
                    <input id="applicationTime" name="applicationTime" type="text" class="form-control"
                           value="{{ row.applicationTime | date: 'yyyy-MM-dd hh:mm:ss' }}" readonly>
                </div>
            </div>



        </form>
    </div>
</div>

<div class="modal-footer">
    <row>
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary" type="button"

                        ng-disabled="row.transferAmount>row.drawingAmount||row.transferAmount<0||row.transferAmount==0"
                        ng-click="confirm(row)"
                        >
                    确定
                </button>
            </div>
        </div>
    </row>
</div>