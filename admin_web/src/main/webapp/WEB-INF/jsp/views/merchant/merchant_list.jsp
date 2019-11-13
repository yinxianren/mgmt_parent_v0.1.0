
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="merchantListCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'MERCHANT_MGMT' | translate}}</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">审核状态：</span>
                                        <select class="form-control b-r-sm"
                                                name="agentIdentityType"
                                                ng-model="searchInfo.status">
                                            <option value=""></option>
                                            <option value="0">启用</option>
                                            <option value="1">禁用</option>
                                            <option value="2">未审核</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户类型：</span>
                                        <select class="form-control b-r-sm"
                                                name="agentIdentityType"
                                                ng-model="searchInfo.type">
                                            <option value=""></option>
                                            <option value="00">公司商户</option>
                                            <option value="01">个体商户</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <%--<div class="col-sm-6 col-md-6 col-lg-1-5">--%>
                                <%--<div class="form-group form-group-sm">--%>
                                    <%--<div class="input-group">--%>
                                        <%--<span class="input-group-addon">商户名称：</span>--%>
                                        <%--<input type="text" class="form-control" ng-model="searchInfo.merchantName">--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merchantId"
                                               uib-typeahead="x.merchantId as x.merchantId + '(' + x.merchantName + ')' for x in merchantList | filter: {merchantId : $viewValue}"
                                               typeahead-min-length="0">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-md-6 col-lg-3 col-lg-offset-3">
                                <button class="btn btn-sm btn-w-m btn-primary pull-right" ng-click="search(searchInfo)">
                                    查询
                                </button>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-3">
                                <button class="btn btn-sm btn-w-m btn-info" ng-click="reset()">
                                    重置
                                </button>
                            </div>
                        </div>
                        <button class="btn btn-sm btn-primary add-btn" ng-click="showModal(0)">新增</button>
                        <div class="table-responsive">
                            <table ng-table="merchantInfoTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        <input type="checkbox" id="constantCheckBoxID" ng-model="selected[row.id]"  />
                                    </td>
                                    <td class="text-center" data-title="'商户号'">
                                        {{row.merchantId}}
                                    </td>
                                    <td class="text-center" data-title="'商户名称'">
                                        {{row.merchantName}}
                                    </td>
                                   <%-- <td class="text-center" data-title="'商户简称'">
                                        {{row.merchantShortName}}
                                    </td>--%>
                                    <td class="text-center" data-title="'商户类型'" >
                                        <div ng-show="row.type == 00 ">
                                            公司商户
                                        </div>
                                        <div ng-show=" row.type == 01 ">
                                            个体商户
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'审核状态'" >
                                        <div ng-show="row.status == 0 ">
                                            启用
                                        </div>
                                        <div ng-show=" row.status == 1 ">
                                            禁用
                                        </div>
                                        <div ng-show=" row.status == 2 ">
                                            未审核
                                        </div>
                                    </td>
                                    <%--<td class="text-center" data-title="'证件类型'">
                                        <div ng-show="row.identityType == 1 ">
                                            身份证
                                        </div>
                                        <div ng-show=" row.identityType == 2 ">
                                            护照
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'电话'">
                                        {{row.phone}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.email}}
                                    </td>--%>
                                    <td class="text-center" data-title="'合同开始时间'">
                                        {{row.agreementStartTime | date:'yyyy-MM-dd'}}
                                    </td>
                                    <td class="text-center" data-title="'合同终止时间'">
                                        {{row.agreementEndTime | date:'yyyy-MM-dd'}}
                                    </td>
                                   <%-- <td class="text-center" data-title="'创建时间'">
                                        {{row.createTime | date:'yyyy-MM-dd HH:mm:ss'}}
                                    </td>--%>
                                    <td class="text-center" data-title="'结算账号'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(3, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'商户通道配置'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(4, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'商户产品费率'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(5, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'商户银行费率'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(8, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'商户风控'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(6, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'用户管理'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(7, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center p-6" style="max-width: 170px;" data-title="'操作'">
                                        <div style="min-width: 80px">
                                            <button class="btn btn-xs btn-success no-margins" ng-click="showModal(1, row)">
                                                编辑
                                            </button>
                                            <button class="btn btn-xs btn-success no-margins" ng-click="showModal(2, row)">
                                                审核
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="merchantInfoTable"></my-pagination>
                        <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
