<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'WITHDRAWAL_AUDIT' | translate }}</h5>
                    </div>
                    <div class="ibox-content sk-loading " ng-controller="financeDrawingAuditCtrl"  >
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>

                        <form name="myform">
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">商户号/代理商户号：</span>
                                            <input class="form-control b-r-sm" type="text" ng-model="searchInfo.customerId"
                                                   uib-typeahead="x.customerId as x.customerId + '(' + x.customerName + ')' for x in customers | filter: {id : $viewValue}"
                                                   typeahead-min-length="0">
                                        </div>
                                    </div>
                                </div>
                                <%--<div class="col-sm-6 col-md-6 col-lg-1-5">--%>
                                    <%--<div class="form-group form-group-sm">--%>
                                        <%--<div class="input-group">--%>
                                            <%--<span class="input-group-addon">状态：</span>--%>
                                            <%--<select id="exception_status" class="form-control"--%>
                                                    <%--ng-model="searchInfo.status"--%>
                                                    <%--&lt;%&ndash;ng-options="x.firstValue as x.name for x in types"&ndash;%&gt;--%>
                                            <%-->--%>
                                                <%--<option value=""></option>--%>
                                            <%--</select>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">申请起始区间：</span>
                                            <input type="text"
                                                   uib-tooltip="可只选择起始日期查询起始日期至今订单"
                                                   class="form-control" ng-readonly="true" ng-click="openDatepicker1()"
                                                   uib-datepicker-popup="{{format}}" ng-model="searchInfo.startDate"
                                                   is-open="popup1.opend"
                                                   datepicker-options="dateOptions1"
                                                   alt-input-formats="altInputFormats"
                                                   close-text="关闭" clear-text="清空" current-text="今天">
                                            <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker1()">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-1-5">
                                    <div class="form-group form-group-sm">
                                        <div class="input-group">
                                            <span class="input-group-addon">申请结束区间：</span>
                                            <input type="text"
                                                   class="form-control" ng-readonly="true" ng-click="openDatepicker2()"
                                                   uib-datepicker-popup="{{format}}" ng-model="searchInfo.endDate"
                                                   is-open="popup2.opend"
                                                   datepicker-options="dateOptions2"
                                                   alt-input-formats="altInputFormats"
                                                   ng-disabled="endDateDisable"
                                                   close-text="关闭" clear-text="清空" current-text="今天">
                                            <span class="input-group-btn">
                                            <button type="button" class="btn btn-default btn-sm no-margins"
                                                    ng-click="openDatepicker2()"
                                                    ng-disabled="endDateDisable">
                                                <i class="fa fa-calendar"></i>
                                            </button>
                                        </span>
                                        </div>
                                    </div>
                                </div>

                                <%--<div class="col-sm-6 col-md-6 col-lg-1-5">--%>
                                    <%--<div class="form-group form-group-sm">--%>
                                        <%--<div class="input-group">--%>
                                            <%--<span class="input-group-addon">审核起始日期：</span>--%>
                                            <%--<input type="text"--%>
                                                   <%--uib-tooltip="可只选择起始日期查询起始日期至今订单"--%>
                                                   <%--class="form-control"--%>
                                                   <%--ng-readonly="true"--%>
                                                   <%--ng-click="openDatepicker3()"--%>
                                                   <%--uib-datepicker-popup="{{format}}" ng-model="searchInfo.startDate2"--%>
                                                   <%--is-open="popup3.opend"--%>
                                                   <%--datepicker-options="dateOptions3"--%>
                                                   <%--alt-input-formats="altInputFormats"--%>
                                                   <%--close-text="关闭" clear-text="清空" current-text="今天">--%>
                                            <%--<span class="input-group-btn">--%>
                                            <%--<button type="button" class="btn btn-default btn-sm no-margins"--%>
                                                    <%--ng-click="openDatepicker3()">--%>
                                                <%--<i class="fa fa-calendar"></i>--%>
                                            <%--</button>--%>
                                        <%--</span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<div class="col-sm-6 col-md-6 col-lg-1-5">--%>
                                    <%--<div class="form-group form-group-sm">--%>
                                        <%--<div class="input-group">--%>
                                            <%--<span class="input-group-addon">审核结束日期：</span>--%>
                                            <%--<input type="text"--%>
                                                   <%--class="form-control" ng-readonly="true" ng-click="openDatepicker4()"--%>
                                                   <%--uib-datepicker-popup="{{format}}" ng-model="searchInfo.endDate2"--%>
                                                   <%--is-open="popup4.opend"--%>
                                                   <%--datepicker-options="dateOptions4"--%>
                                                   <%--alt-input-formats="altInputFormats"--%>
                                                   <%--ng-disabled="endDateDisable2"--%>
                                                   <%--close-text="关闭" clear-text="清空" current-text="今天">--%>
                                            <%--<span class="input-group-btn">--%>
                                            <%--<button type="button" class="btn btn-default btn-sm no-margins"--%>
                                                    <%--ng-click="openDatepicker4()"--%>
                                                    <%--ng-disabled="endDateDisable2">--%>
                                                <%--<i class="fa fa-calendar"></i>--%>
                                            <%--</button>--%>
                                        <%--</span>--%>
                                        <%--</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>


                            </div>
                            <div class="row">
                                <div class="col-sm-6 col-md-6 col-lg-3 col-lg-offset-3">
                                    <button type="submit" class="btn btn-sm btn-w-m btn-primary pull-right general-btn"
                                            ng-click="search()">
                                        查询
                                    </button>
                                </div>
                                <div class="col-sm-6 col-md-6 col-lg-3">
                                    <button type="button" class="btn btn-sm btn-w-m btn-info general-btn"
                                            ng-click="resetForm()">
                                        重置
                                    </button>
                                </div>

                            </div>
                        </form>
                        <div class="hr-line-dashed"></div>
                        <div class="table-responsive">

                        </div>
                        <div class="hr-line-dashed"></div>
                        <%-- ng_table --%>
                        <div class="table-responsive">
                            <table ng-table="financeDrawingTable"
                                   class="table orderChangetable table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" data-title="'提现流水号'">
                                        {{row.id}}
                                    </td>
                                    <td class="text-center" data-title="'用户编号'">
                                        {{row.customerId}}
                                    </td>
                                    <td class="text-center" data-title="'提现金额'">
                                        {{row.drawingAmount==null?0:row.drawingAmount |  number : 2}}
                                    </td>
                                    <td class="text-center" data-title="'划款金额'">
                                        {{row.transferAmount==null?0:row.transferAmount|  number : 2}}
                                    </td>
                                    <td class="text-center" data-title="'状态'">
                                        {{row.status | getValueByList : status : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" data-title="'申请人'">
                                        {{row.applicant }}
                                    </td>
                                    <td class="text-center" data-title="'提现时间'">
                                        {{row.applicationTime | date:'yyyy/MM/dd HH:mm'}}
                                    </td>
                                    <%--<td class="text-center" data-title="'划款人'">--%>
                                        <%--{{row.transferer}}--%>
                                    <%--</td>--%>
                                    <%--<td class="text-center" data-title="'划款时间'">--%>
                                        <%--{{row.transferTime  | date:'yyyy/MM/dd HH:mm'}}--%>
                                    <%--</td>--%>
                                    <td class="text-center" data-title="'操作'">
                                        <span   ><button
                                                class="btn btn-xs no-margins ng-binding btn-primary active"
                                        ng-click="showModal(row)"
                                        >
                                            审核
                                        </button></span>

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
