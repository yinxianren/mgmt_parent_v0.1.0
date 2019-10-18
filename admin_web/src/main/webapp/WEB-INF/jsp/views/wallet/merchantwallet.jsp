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
                        <h5>{{'MERCHANT_WALLET' | translate}}</h5>
                    </div>
                    <div class="ibox-content" ng-controller="merchantWalletCtrl">
                        <form name="myForm">
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">代理商：</span>
                                            <select class="form-control b-r-sm" ng-model="searchInfo.parentId"
                                                    ng-options="x.agentMerchantId as x.agentMerchantName for x in agents">
                                                <option value=""></option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-3 form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">商户：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.merId"
                                               uib-typeahead="x.merId as x.merId + '(' + x.merchantName + ')' for x in merchants | filter: {merId : $viewValue}"
                                               typeahead-min-length="0"
                                        >
                                    </div>
                                </div>
                                <div class="form-group form-group-sm">
                                    <button class="btn btn-sm btn-w-m btn-primary"
                                            ng-click="search()" ng-disabled="searchInfo.merId==null&&searchInfo.parentId==null">
                                        查询
                                    </button>
                                    <button class="btn btn-sm btn-w-m btn-info"
                                            ng-click="resetForm()">
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                        <div class="table-responsive col-lg-12" >
                            <table ng-table="merchantWalletTable"
                                    class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">

                                    <td class="text-center" data-title="'商户'">
                                        {{row.merId}}({{row.merId  | getValueByList : merchants : 'merId':'merchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'订单金额'">
                                        {{row.totalAmount == null?"--":(row.totalAmount | number: 2)}}
                                    </td>
                                    <td class="text-center" data-title="'入账金额'">
                                        {{row.incomeAmount == null?"--":(row.incomeAmount | number: 2) }}
                                    </td>
                                    <td class="text-center" data-title="'出账金额'">
                                        {{row.outAmount == null?"--":(row.outAmount | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'手续费成本'">
                                        {{row.totalFee == null?"--":(row.totalFee | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'手续费利润'">
                                        {{row.feeProfit == null?"--":(row.feeProfit | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'总余额'">
                                        {{row.totalBalance == null?"--":(row.totalBalance | number: 2) }}
                                    </td>
                                    <td class="text-center" data-title="'总可用余额'">
                                        {{row.totalAvailableAmount == null?"--":(row.totalAvailableAmount | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'待结算'">
                                        {{row.totalUnavailableAmount == null?"--":(row.totalUnavailableAmount | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'冻结金额'">
                                        {{row.totalFreezeAmount == null?"--":(row.totalFreezeAmount | number: 2) }}
                                    </td>
                                    <td class="text-center" data-title="'保证金'">
                                        {{row.totalBond == null?"--":(row.totalBond | number: 2)  }}
                                    </td>
                                    <td class="text-center" data-title="'更新时间'">
                                        {{row.updateTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="merchantWalletTable"></my-pagination>
                        <%--<button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
