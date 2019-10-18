<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/24
  Time: 16:23
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'INVERST' | translate}}：</h5>
                    </div>
                    <div class="sk-loading" ng-controller="investCtrl" >
                        <form name="myForm" class="form-horizontal" style="margin-top:50px;margin-left:550px">
<%--                        <form name="myForm" class="form-horizontal">--%>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>商户号：</label>
                                <div class="col-lg-3">
                                    <select class="form-control b-r-sm"
                                            name="merId"
                                            ng-change="changeed()"
                                            ng-model="investInfo.merId"
                                            ng-options="x.merId as x.merId for x in merchant"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>机构名称：</label>
                                <div class="col-lg-3">
                                    <select class="form-control b-r-sm"
                                            name="channelId"
                                            ng-model="investInfo.channelId"
                                            ng-options="x.channelId as x.channelName for x in channels"
                                            required>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>充值金额：</label>
                                <div class="col-lg-3">
                                    <input class="form-control b-r-sm"
                                           name="amount"
                                           type="number"
                                           placeholder="必填"
                                           ng-model="investInfo.amount"
                                           oninput="inputRegex(this, /[^[0-9]\d*\.\d*|0\.\d*[0-9]\d*$]/g)"
                                    >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-lg-2 control-label"><span class="text-danger">*</span>登录密码：</label>
                                <div class="col-lg-3">
                                    <input class="form-control b-r-sm"
                                           name="password"
                                           type="password"
                                           placeholder="必填"
                                           ng-model="investInfo.password"
                                    >
                                </div>
                            </div>

                        </form>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="center">
                                    <button class="btn btn-sm btn-success" type="button" ng-click="invest()"
                                            ng-disabled="nextDisabled(myForm)">
                                        确定
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
