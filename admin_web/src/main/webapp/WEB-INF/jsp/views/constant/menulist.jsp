<%--
  author: xuzm
  Date: 2018/4/19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link type="text/css" rel="stylesheet" href="css/constant/constant.css"/>
<link type="text/css" rel="stylesheet" href="css/common/common.css"/>
<!-- Main view -->
<div class="animated fadeInRight">

    <div class="wrapper wrapper-content ng-scope" ng-controller="menuInfoCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>菜单页配置</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>

                        <button class="btn btn-sm btn-primary add-btn" ng-click="showAddModal()">新增</button>
                        <div class="table-responsive">
                            <table ng-table="menuTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        <input type="checkbox" id="sysGroupCheckBoxID" ng-model="selected[row.code]"  />
                                    </td>
                                    <td class="text-center" filter="{description: 'text'}" style="line-height: 2"
                                        data-title="'权限描述'">
                                        {{row.description}}
                                    </td>
                                    <td class="text-center" filter="{name: 'text'}" style="line-height: 2"
                                        data-title="'权限名称'">
                                        {{row.name}}
                                    </td>
                                    <td class="text-center" filter="{stateName: 'text'}" style="line-height: 2"
                                        data-title="'地址'">
                                        {{row.stateName}}
                                    </td>
                                    <%--<td class="text-center" filter="{groupCode: 'select'}" filter-data="groupList" style="line-height:2"--%>
                                        <%--data-title="'常量组别'">--%>
                                        <%--{{row.groupCode}}--%>
                                    <%--</td>--%>
                                    <td class="text-center" style="line-height: 2" data-title="'操作'">
                                        <button class="btn btn-xs btn-success operate-btn" style="margin:0 0 0 10px " ng-click="showModal(row)" >编辑</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="constantTable"></my-pagination>
                        <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>