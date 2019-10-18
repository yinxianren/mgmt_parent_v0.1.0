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
                        <h5>{{'AGENT_WALLET' | translate}}</h5>
                    </div>
                    <div class="ibox-content" ng-controller="agentWalletCtrl">
                        <form name="myForm">
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-3 form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">代理商：</span>
                                        <input class="form-control b-r-sm" type="text" ng-model="searchInfo.agentMerchantId"
                                               uib-typeahead="x.agentMerchantId as x.agentMerchantId + '(' + x.agentMerchantName + ')' for x in agents | filter: {agentMerchantId : $viewValue}"
                                               typeahead-min-length="0"
                                        >
                                    </div>
                                </div>
                                <div class="form-group form-group-sm">
                                    <button class="btn btn-sm btn-w-m btn-primary"
                                            ng-click="search()" ng-disabled="searchInfo.agentMerchantId==null">
                                        查询
                                    </button>
                                    <button class="btn btn-sm btn-w-m btn-info"
                                            ng-click="resetForm()">
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                        <div class="table-responsive col-lg-12"  >
                            <table
                                    ng-table="AgentWalletTable"
                                    class="table table-condensed table-striped table-hover table-bordered">

                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'代理商'">
                                        {{row.agentMerchantId}}({{row.agentMerchantId  | getValueByList : agents : 'agentMerchantId':'agentMerchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'订单金额'">
                                        {{row.totalAmount== null?"--":(row.totalAmount | number: 2)  }}
                                    </td>

                                    <td class="text-center" data-title="'入账金额'">
                                        {{row.incomeAmount== null?"--":(row.incomeAmount | number: 2) }}
                                    </td>

                                    <td class="text-center" data-title="'出账金额'">
                                        {{row.outAmount== null?"--":(row.outAmount | number: 2) }}
                                    </td>

                                    <td class="text-center" data-title="'手续费成本'">
                                        {{row.totalFee== null?"--":(row.totalFee | number: 2)  }}
                                    </td>

                                    <td class="text-center" data-title="'总余额'">
                                        {{row.totalBalance== null?"--":(row.totalBalance | number: 2)  }}
                                    </td>

                                    <td class="text-center" data-title="'总可用余额'">
                                        {{row.totalAvailableAmount== null?"--":(row.totalAvailableAmount | number: 2)  }}
                                    </td>

                                    <td class="text-center" data-title="'待结算'">
                                        {{row.totalUnavailableAmount== null?"--":(row.totalUnavailableAmount | number: 2) }}
                                    </td>

                                    <td class="text-center" data-title="'冻结金额'">
                                        {{row.totalFreezeAmount== null?"--":(row.totalFreezeAmount | number: 2)}}
                                    </td>
                                    <td class="text-center" data-title="'更新时间'">
                                        {{row.updateTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="AgentWalletTable"></my-pagination>
                        <%--<button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
