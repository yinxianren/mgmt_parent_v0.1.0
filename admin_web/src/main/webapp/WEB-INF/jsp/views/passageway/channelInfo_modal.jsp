
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '添加通道信息' : '修改通道信息'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <form name="myForm" class="form-horizontal">
        <uib-tabset active="activeForm">
            <uib-tab index="0" classes="m-b">
                <uib-tab-heading><i class="fa fa-user"></i>&nbsp;基本信息</uib-tab-heading>
                <%--{{myForm.$error}}--%>
               <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>通道名称：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" name="channelName" type="text" placeholder="必填"
                               ng-model="ChannelInfo.channelName" ng-blur="nameBlur($event, ChannelInfo.channelName)"
                               required>
                    </div>
                </div>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>通道等级：</label>
                    <div class="col-sm-9">
                            <select class="form-control b-r-sm " name="channelLevel" ng-model="ChannelInfo.channelLevel"
                                    ng-blur="statusBlur($event, ChannelInfo.channelLevel)" required
                                    ng-options="+(x.firstValue) as x.name for x in channelLevel">
                            </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>通道机构：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm" ng-model="ChannelInfo.organizationId" ng-blur="checkChannelCode(ChannelInfo.organizationId)"
                                >
                            <option ng-repeat="x in organizations" value="{{x.organizationId}}">{{x.organizationName}}</option>
                        </select>
                    </div>
                </div>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>支付商户号：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" name="channelTransCode" type="text" placeholder="必填"
                               ng-model="ChannelInfo.channelTransCode"
                               ng-blur="checkChannelTransCode()"
                               required>
                    </div>
                </div>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>通道标识：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" name="channelTab" type="text" placeholder="必填"
                               ng-model="ChannelInfo.channelTab"
                               required />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>支付产品：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm" name="productId" ng-model="ChannelInfo.productId"
                                ng-blur="statusBlur($event, products)" required
                                ng-options="(x.productId) as x.productName for x in products">
                        </select>
                    </div>
                </div>
               <%-- <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">代付通道：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm" ng-model="ChannelInfo.outChannelId" ng-blur="checkChannelTransCode()">
                            <option value=""></option>
                            <option ng-repeat="x in daiFuList" value="{{x.channelId}}">{{x.channelName}}</option>
                        </select>
                    </div>
                </div>
                <div class="form-group" ng-if="checkOutChannelId">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"></label>
                    <div class="col-sm-9" style="color: #e6bd76;">
                       支付商户号和代付通道支付商户号不一致！
                    </div>
                </div>--%>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>请求地址：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" name="requestUrl" type="text" placeholder="必填"
                               ng-model="ChannelInfo.requestUrl" required>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>启用状态：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm" name="status" ng-model="ChannelInfo.status"
                                ng-blur="statusBlur($event, ChannelInfo.status)" required
                                ng-options="+(x.firstValue) as x.name for x in status">
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left"><span class="text-danger">*</span>业务类型：</label>
                    <div class="col-sm-9">
                        <select class="form-control b-r-sm" name="busiType" ng-model="ChannelInfo.busiType"
                                ng-blur="statusBlur($event, ChannelInfo.busiType)" required
                                ng-options="(x.firstValue) as x.name for x in busiTypes">
                        </select>
                    </div>
                </div>
            </uib-tab>
            <uib-tab index="1" disable="nextDisabled(myForm, 0)">
                <uib-tab-heading><i class="fa fa-address-card"></i>&nbsp;通道配置</uib-tab-heading>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">单笔手续费(元)：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.channelSingleFee" placeholder="单位(元)">
                    </div>
                </div>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">手续费比例(%)：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.channelRateFee" placeholder="单位(%)，如2.8">
                    </div>
                </div>

                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">单笔最小额：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.singleMinAmount">
                    </div>
                </div>
                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">单笔最大额：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.singleMaxAmount">
                    </div>
                </div>

                <div class="form-group m-t">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">结算周期(天)：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="text"
                               <%--oninput="inputRegex(this, /[^*()+\-\d]/g)"--%>
                               ng-model="ChannelInfo.settleCycle"
                                placeholder="单位(天)">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">日限额：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.dayQuotaAmount">
                    </div>
                </div>


                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">月限额：</label>
                    <div class="col-sm-9">
                        <input class="form-control b-r-sm" type="number" ng-model="ChannelInfo.monthQuotaAmount">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-3 control-label p-w-xs label-name-left">银行参数：</label>
                    <div class="col-sm-9">
                    <textarea class="form-control b-r-sm" ng-model="ChannelInfo.channelParam"
                              style="max-width: 100%;height: 100px"></textarea>
                    </div>
                </div>
            </uib-tab>

        </uib-tabset>
    </form>
    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm" type="button" ng-click="addOrganization(ChannelInfo, activeForm)"
                        ng-class="{'btn-primary': activeForm != 1, 'btn-success': activeForm == 1}"
                        ng-disabled="nextDisabled(myForm, activeForm)">
                    {{activeForm != 1 ? '下一步' : '确定'}}
                </button>
            </div>
        </div>
    </div>
</div>
