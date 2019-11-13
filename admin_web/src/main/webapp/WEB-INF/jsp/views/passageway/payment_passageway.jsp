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
                        <h5>{{'PAYMENT_PASSAGEWAY' | translate}}：</h5>
                    </div>
                    <div class="ibox-content sk-loading" ng-controller="paymentPassagewayCtrl">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>

                        <div class="row">
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
                                                <span class="input-group-addon">机构名称：</span>
                                                <input type="text" class="form-control" ng-model="searchInfo.organizationName"
                                                       uib-typeahead="x.organizationName  for x in organizations | filter: {organizationName : $viewValue}"
                                                       typeahead-min-length="0"
                                                >
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
                                <button class="btn btn-sm btn-primary" ng-click="showModal(0)">新增</button>
                                <div class="table-responsive">
                                    <table ng-table="OrganizationTable"
                                           class="table table-condensed table-striped table-hover table-bordered">
                                        <tr ng-repeat="row in $data">
                                            <td class="text-center p-6" style="width: 35px">
                                                <input type="checkbox" ng-model="selected[row.id]">
                                            </td>
                                            <td class="text-center" data-title="'机构ID'">
                                                {{row.organizationId}}
                                            </td>
                                            <td class="text-center" data-title="'名称'">
                                                {{row.organizationName}}
                                            </td>

                                            <td class="text-center" data-title="'创建人'">
                                                {{row.creator }}
                                            </td>
                                            <td class="text-center" data-title="'创建时间'">
                                                <%--{{row.createTime  | date : 'yyyy/MM/dd ' }}--%>
                                                {{row.createTime  | date : 'yyyy/MM/dd HH:mm:ss' }}
                                            </td>
                                           <%-- <td class="text-center" data-title="'备注'">
                                                {{row.remark }}
                                            </td>--%>
                                            <td class="text-center ng-isolate-scope" data-title="'操作'" >
                                                <div class="btn btn-xs no-margins btn-primary active" ng-click="statusChange($event, row)" ng-show="row.status == 1 ">
                                                    启用
                                                </div>
                                                <div  class="btn btn-xs no-margins btn-disabled" ng-click="statusChange($event, row)" ng-show=" row.status == 0 ">
                                                    禁用
                                                </div>
                                                <div class="btn btn-xs btn-success"  style="margin: 0 0 0 10px" ng-click="edit(1, row)">
                                                    编辑
                                                </div>
                                                <%--<div class="btn btn-xs btn-success"  style="margin: 0 0 0 10px" ng-click="productEdit(1, row)">
                                                    产品配置
                                                </div>--%>
                                            </td>

                                        </tr>
                                    </table>
                                </div>
                                <button class="btn btn-sm btn-primary pull-left" ng-click="del()" ng-disabled="delDisabled()">
                                    删除
                                </button>
                                <my-pagination table-param="OrganizationTable"></my-pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
