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
                        <h5>{{'ATTACH_PASSAGEWAY_MGMT' | translate}}：</h5>
                    </div>
                    <div class="ibox-content sk-loading" ng-controller="attachPassagewayMgmtCtrl">
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
                                        <span class="input-group-addon">通道名称：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.extraChannelName">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6 col-md-6 col-lg-1-5">
                                <div class="form-group form-group-sm">
                                    <div class="input-group">
                                        <span class="input-group-addon">请求地址：</span>
                                        <input type="text" class="form-control" ng-model="searchInfo.requestUrl">
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
                            <table ng-table="extraChannelInfoTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center p-6" style="width: 35px">
                                        <input type="checkbox" ng-model="selected[row.id]">
                                    </td>
                                    <td class="text-center" data-title="'通道ID'">
                                        {{row.extraChannelId  }}
                                    </td>
                                    <td class="text-center" data-title="'通道名称'">
                                        {{row.extraChannelName}}
                                    </td>
                                    <td class="text-center" data-title="'通道类型'">
                                        {{row.bussType | getValueByList : extraTypes : 'firstValue' : 'name'}}
                                    </td>
                                    <td class="text-center" style="max-width: 80px" data-title="'请求地址'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.requestUrl)">
                                                {{row.requestUrl}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" style="max-width: 100px" data-title="'参数'">
                                        <div class="hidden-text">
                                            <a ng-click="showInfo(row.channelParam)">
                                                {{row.channelParam}}
                                            </a>
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'机构'">
                                        {{row.organizationId | getValueByList : organizations : 'organizationId' : 'organizationName' }}
                                    </td>
                                    <td class="text-center" data-title="'操作人'">
                                        {{row.creator}}
                                    </td>
                                    <td class="text-center" data-title="'创建时间'">
                                        {{row.createTime | date : 'yyyy/MM/dd HH:mm:ss'}}
                                    </td>
                                    <td class="text-center p-6" style="width: 110px" data-title="'操作'">
                                        <div class="btn btn-xs no-margins btn-primary active" ng-click="statusChange($event, row)" ng-show="row.status == 1 ">
                                            启用
                                        </div>
                                        <div  class="btn btn-xs no-margins btn-disabled" ng-click="statusChange($event, row)" ng-show=" row.status == 0 ">
                                            禁用
                                        </div>
                                        <div class="btn btn-xs btn-success"  style="margin: 0 0 0 10px" ng-click="edit(1, row)">
                                            编辑
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <button class="btn btn-sm btn-primary pull-left" ng-click="del()" ng-disabled="delDisabled()">
                            删除
                        </button>
                        <my-pagination table-param="extraChannelInfoTable"></my-pagination>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
