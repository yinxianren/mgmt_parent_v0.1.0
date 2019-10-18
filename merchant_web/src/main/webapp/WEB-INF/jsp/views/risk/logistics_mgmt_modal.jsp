<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">上传</h3>
</div>
<div class="modal-body" id="modal-body">
    <form id="logisticsObjForm" role="form" name="logisticsObjForm" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label"><font color="red">*</font>订单号</label>
            <div class="col-sm-8">
                <input class="form-control" type="text" name="orderId" tip="1" ng-model="expressModel.orderId"
                       disabled="expressModel.orderId"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><font color="red">*</font>快递单号</label>
            <div class="col-sm-8">
                <input type="text" name="expressNo" tip="2" ng-model="expressModel.expressNo"
                       class="form-control" required/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label"><font color="red">*</font>快递公司:</label>
            <div class="col-sm-8 set-height33">
                <select class="form-control" ng-model="expressModel.expressName" required
                        ng-options="x.firstValue as x.name for x in expressName">
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><font color="red">*</font>查单网址</label>
            <div class="col-sm-8">
                <input type="text" name="invoiceUrl" tip="2" ng-model="expressModel.invoiceUrl"
                       class="form-control" required/>
            </div>
        </div>
    </form>
    <div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" type="button"
                        ng-disabled="logisticsObjForm.expressNo.$invalid || logisticsObjForm.expressName.$invalid || logisticsObjForm.invoiceUrl.$invalid  || logisticsObjForm.orderId.$invalid"
                        ng-click="confirm()">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
