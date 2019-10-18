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
                        <h5>代理商户提现查询：</h5>
                    </div>
                    <div class="ibox-content" ng-controller="financeDrawingCtrl">
                        <div class="table-responsive col-lg-12">
                            <table
                                    ng-table="financeDrawingTable"
                                    class="table table-condensed table-striped table-hover table-bordered">

                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'代理商'">
                                        {{row.customerId}}({{row.customerId | getValueByList : agents : 'agentMerchantId' : 'agentMerchantName'}})
                                    </td>
                                    <td class="text-center" data-title="'提现流水号'">
                                        {{row.id }}
                                    </td>
                                    <td class="text-center" data-title="'提现金额'">
                                        {{row.drawingAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'划款金额'">
                                        {{row.transferAmount | number:2 }}
                                    </td>
                                    <td class="text-center" data-title="'状态'">
                                        {{row.status | getValueByList : drawStatus : 'firstValue':'name'}}
                                    </td>
                                    <td class="text-center" data-title="'申请人'">
                                        {{row.applicant }}
                                    </td>
                                    <td class="text-center" data-title="'申请时间'">
                                        {{row.applicationTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center" data-title="'划款人'">
                                        {{row.transferer }}
                                    </td>
                                    <td class="text-center" data-title="'划款时间'">
                                        {{row.transferTime| date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="financeDrawingTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
