<%--
  author: xuzm
  Date: 2018/4/19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link type="text/css" rel="stylesheet" href="css/constant/constant.css"/>
<link type="text/css" rel="stylesheet" href="css/common/common.css"/>
<script>


</script>
<!-- Main view -->
<div class="animated fadeInRight">

    <div class="wrapper wrapper-content ng-scope" ng-controller="sysGroupInfoCtrl" >
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>系统常量组别配置</h5>
                    </div>
                    <div class="ibox-content sk-loading">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>

                        <button class="btn btn-sm btn-primary add-btn" ng-click="showAddSysGroupModal()">新增</button><br>
                        <div class="table-responsive" >
                            <table ng-table="sysGroupTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        <input type="checkbox" id="sysGroupCheckBoxID" ng-model="selected[row.code]"  />
                                    </td>
                                    <td class="text-center" filter="{name: 'text'}" style="line-height: 2"
                                        data-title="'常量组名称'">
                                        {{row.name}}  {{row.id}}
                                    </td>
                                    <td class="text-center" filter="{code: 'text'}" style="line-height: 2"
                                        data-title="'常量组编码'">
                                        {{row.code}}
                                    </td>
                                    <td class="text-center" filter="{model: 'select'}"  filter-data="modelList" style="line-height: 2"
                                        data-title="'类型'">
                                        <span >{{getmodel(row.model)}}</span>
                                    </td>

                                    <td class="text-center" filter="{system: 'select'}"  filter-data="systemList" style="line-height: 2"
                                        data-title="'系统常量'">
                                        <span >{{getsystem(row.system)}}</span>
                                    </td>
                                    <td class="text-center" style="line-height: 2" data-title="'操作'">
                                        <button class="btn btn-xs btn-success operate-btn" style="margin:0 0 0 10px " ng-click="showModal(row)" >编辑</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="sysGroupTable"></my-pagination>
                        <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>