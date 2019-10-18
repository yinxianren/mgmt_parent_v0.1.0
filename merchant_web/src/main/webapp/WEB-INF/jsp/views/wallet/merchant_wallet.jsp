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
                        <h5>商户余额查询：</h5>
                    </div>
                    <div class="ibox-content" ng-controller="merchantWalletCtrl">

                        <div class="table-responsive col-lg-12"  >
                            <table
                                    ng-table="merchantWalletTable"
                                    class="table table-condensed table-striped table-hover table-bordered">

                                <tr ng-repeat="row in $data">

                                    <td class="text-center" data-title="'商户'">
                                        ${sessionScope.merchantInfo.merId}(${sessionScope.merchantInfo.merchantName})
                                    </td>
                                    <td class="text-center" data-title="'订单总额'">
                                        {{row.totalAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'入账总额'">
                                        {{row.incomeAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'出账总额'">
                                        {{row.outAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'手续费总额'">
                                        {{row.totalFee | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'总余额'">
                                        {{row.totalBalance | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'总可用余额'">
                                        {{row.totalAvailableAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'待结算'">
                                        {{row.totalUnavailableAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'冻结金额'">
                                        {{row.totalFreezeAmount | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'保证金'">
                                        {{row.totalBond | number:2  }}
                                    </td>
                                    <td class="text-center" data-title="'更新时间'">
                                        {{row.updateTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'操作'">
                                        <button class="btn btn-xs btn-primary no-margins" row="{{row}}" ng-click="showModal(row)" >提现</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="merchantWalletTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
