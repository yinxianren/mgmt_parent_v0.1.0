<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2019/1/25
  Time: 10:23
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'TERMINAL_MERCHANTS_WALLET' | translate}}</h5>
                    </div>
                    <div class="ibox-content" ng-controller="terminalwalletCtrl">
                        <form name="myForm">
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-3 form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">子商户号：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.terminalMerId"
                                               uib-typeahead="x.terminalMerId as x.terminalMerId for x in terminalMerIds | filter: {terminalMerId : $viewValue}"
                                               typeahead-min-length="0"
                                        >
                                    </div>
                                </div>
                                <div class="form-group form-group-sm">
                                    <button class="btn btn-sm btn-w-m btn-primary" ng-click="search(searchInfo)">
                                        查询
                                    </button>
                                    <button class="btn btn-sm btn-w-m btn-info"
                                            ng-click="resetForm()">
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                        <div class="table-responsive col-lg-12">
                            <table
                                    ng-table="terminalwalletTable"
                                    class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <%--<td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        <input type="checkbox" id="constantCheckBoxID"
                                               ng-model="selected[row.channelId]"/>
                                    </td>--%>
                                    <td class="text-center" data-title="'商户'">
                                        ${sessionScope.merchantInfo.merId}(${sessionScope.merchantInfo.merchantName})
                                    </td>
                                    <td class="text-center" data-title="'子商户号'">
                                        {{row.terminalMerId}}
                                    </td>
                                    <td class="text-center" data-title="'订单金额'">
                                        {{row.totalAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'入账金额'">
                                        {{row.incomeAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'出账金额'">
                                        {{row.outAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'手续费成本'">
                                        {{row.totalFee | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'总余额'">
                                        {{row.totalBalance | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'总可用余额'">
                                        {{row.totalAvailableAmount  | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'待结算'">
                                        {{row.totalUnavailableAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'保证金'">
                                        {{row.totalBond | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'冻结金额'">
                                        {{row.totalFreezeAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'更新时间'">
                                        {{row.updateTime | date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="terminalwalletTable"></my-pagination>
                        <%--<button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()"
                                ng-disabled="delDisabled()">删除
                        </button>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
