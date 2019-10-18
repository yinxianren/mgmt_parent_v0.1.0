<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/13
  Time: 11:23
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{type == 0 ? '余额明细' : '提现'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="ibox-content no-padding no-borders" ng-class="{'sk-loading' : type == 0}">
        <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
        </div>

        <div ng-show="type == 0">
            <div class="table-responsive">
                <table class="table table-condensed table-striped table-hover table-bordered">
                    <tr>
                        <th class="text-center">快递公司</th>
                        <th class="text-center">快递单号</th>
                        <th class="text-center">查单网址</th>
                        <th class="text-center">提交时间</th>
                    </tr>
                    <tr>
                        <td class="text-center">{{shipInfo.expressName}}</td>
                        <td class="text-center">{{shipInfo.expressNo}}</td>
                        <td class="text-center">{{shipInfo.invoiceUrl}}</td>
                        <td class="text-center">{{shipInfo.createTime | date:'yyyy-MM-dd hh:mm:ss:sss'}}</td>
                    </tr>
                </table>
            </div>
        </div>

        <div ng-show="type == 1">
            <form name="myForm" class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-3 control-label">可用余额：</label>
                    <div class="col-sm-9">
                        <p class="form-control-static text-success">
                            <strong>{{balance | currency:'¥'}}</strong>
                        </p>
                    </div>

                    <label class="col-sm-3 control-label"><span class="text-danger">*</span>提现金额：</label>
                    <div class="col-sm-9">
                        <div class="row">
                            <div class="col-sm-8 col-md-8 col-lg-8">
                                <input type="number" min="0.01" step="0.01" class="form-control " ng-model="amount"
                                       ng-disabled="balance <= 0" ng-change="amountChange()" required
                                ng-blur="checkamount(balance,amount,$event)"
                                >
                            </div>
                            <p class="form-control-static text-success">元</p>
                        </div>
                    </div>
                </div>
            </form>
            <div class="row">
                <div class="col-sm-12">
                    <div class="center">
                        <button class="btn btn-sm btn-success" type="submit" ng-disabled="confirmDisabled()"
                                ng-click="confirm()">
                            确定
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>