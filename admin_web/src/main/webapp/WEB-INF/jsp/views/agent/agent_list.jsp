<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/24
  Time: 16:23
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="agentListCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'AGENT_LIST' | translate}}：</h5>
                    </div>
                    <div class="ibox-content sk-loading">
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
                                                ng-model="searchInfo.agentStatus">
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
                                        <span class="input-group-addon">代理商名称：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.agentMerchantName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">代理商编号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.agentMerchantId"
                                               uib-typeahead="x.id as x.id + '(' + x.name + ')' for x in agentMerchantList | filter: {id : $viewValue}"
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
                        <div class="hr-line-dashed"></div>
                        <button class="btn btn-sm btn-primary add-btn" ng-click="showModal(0)">新增</button>
                        <div class="table-responsive">
                            <table ng-table="AgentMerchantInfoTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        <input type="checkbox" id="constantCheckBoxID" ng-model="selected[row.agentMerchantId]"  />
                                    </td>
                                    <td class="text-center" data-title="'代理商编号'">
                                        {{row.agentMerchantId}}
                                    </td>
                                    <td class="text-center" data-title="'代理商名称'">
                                        {{row.agentMerchantName}}
                                    </td>
                                    <td class="text-center" data-title="'审核状态'" >
                                        <div ng-show="row.agentStatus == 0 ">
                                            启用
                                        </div>
                                        <div ng-show=" row.agentStatus == 1 ">
                                            禁用
                                        </div>
                                        <div ng-show=" row.agentStatus == 2 ">
                                            未审核
                                        </div>
                                    </td>
                                    <%--<td class="text-center" data-title="'代理商简称'">--%>
                                        <%--{{row.agentMerchantShortName}}--%>
                                    <%--</td>--%>
                                    <td class="text-center" data-title="'证件类型'">
                                        <div ng-show="row.agentIdentityType == 1 ">
                                            身份证
                                        </div>
                                        <div ng-show=" row.agentIdentityType == 2 ">
                                            护照
                                        </div>
                                    </td>
                                    <%--<td class="text-center" data-title="'证件号码'">--%>
                                        <%--{{row.agentIdentityNum}}--%>
                                    <%--</td>--%>
                                    <td class="text-center" data-title="'电话'">
                                        {{row.agentPhone}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.agentEmail}}
                                    </td>
                                    <%--<td class="text-center" data-title="'QQ'">--%>
                                        <%--{{row.agentQq}}--%>
                                    <%--</td>--%>
                                    <%--<td class="text-center" data-title="'单笔手续费'">
                                        {{row.agentMerchantSetting.singleFee}}
                                    </td>
                                    <td class="text-center" data-title="'手续费率'">
                                        {{row.agentMerchantSetting.rateFee}}
                                    </td>
                                    <td class="text-center" data-title="'结算周期'">
                                        {{row.agentMerchantSetting.settlecycle}}
                                    </td>--%>
                                    <td class="text-center" data-title="'代理商费率'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(4, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center" data-title="'用户管理'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(3, row)">
                                            查看/修改
                                        </button>
                                    </td>
                                    <td class="text-center p-6" style="max-width: 170px;" data-title="'操作'">
                                        <div style="min-width: 80px">
                                            <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(1, row)">
                                                编辑
                                            </button>
                                            <%--审核操作--%>
                                            <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(2, row)">
                                                审核
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="AgentMerchantInfoTable"></my-pagination>
                        <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
