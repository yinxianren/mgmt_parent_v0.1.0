<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/6/14
  Time: 14:38
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
    .img-size {
        height: 136px;
        width: 136px;
    }

    .a-upload {
        text-align: center;
        line-height: 128px;
        position: relative;
        cursor: pointer;
        color: #888;
        background: #f2f2f2;
        border: 1px solid #ddd;
        /*overflow: hidden;*/
        display: inline-block;
        *display: inline;
        *zoom: 1
    }

    .a-upload input {
        width: 136px;
        height: 136px;
        position: absolute;
        /*font-size: 100px;*/
        left: 0;
        top: 0;
        opacity: 0;
        cursor: pointer
    }

    .a-upload:hover {
        color: #444;
        background: #eee;
        border-color: #ccc;
        text-decoration: none
    }
</style>

<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="agentMerchantInfoCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'代理商户信息' | translate}}</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <div class="table-responsive">
                            <table ng-table="agmentMerchantInfoTable"
                                   class="table table-condensed table-striped table-hover table-bordered">
                                <tr ng-repeat="row in $data">
                                    <%--<td class="text-center" style="line-height: 2"
                                        data-title="''">
                                        &lt;%&ndash;  <input type="checkbox" id="constantCheckBoxID" ng-model="selected[row.merId]"  />&ndash;%&gt;
                                    </td>--%>
                                    <td class="text-center" data-title="'代理商'">
                                        {{row.agentMerchantId}}
                                    </td>
                                    <td class="text-center" data-title="'代理商户名称'">
                                        {{row.agentMerchantName}}
                                    </td>
                                    <td class="text-center" data-title="'代理商户简称'">
                                        {{row.agentMerchantShortName}}
                                    </td>
                                    <td class="text-center" data-title="'证件类型'" >
                                        <div ng-show="row.agentIdentityType == 1 ">
                                            身份证
                                        </div>
                                        <div ng-show="row.agentIdentityType == 2 ">
                                            护照
                                        </div>
                                        <div ng-show="row.agentIdentityType == 3 ">
                                            港澳回乡证
                                        </div>
                                        <div ng-show="row.agentIdentityType == 4 ">
                                            台胞证
                                        </div>
                                        <div ng-show="row.agentIdentityType == 5 ">
                                            军官证
                                        </div>

                                    </td>
                                    <td class="text-center" data-title="'证件号码'">
                                        {{row.agentIdentityNum}}
                                    </td>

                                    <td class="text-center" data-title="'电话'">
                                        {{row.agentPhone}}
                                    </td>
                                    <td class="text-center" data-title="'邮箱'">
                                        {{row.agentEmail}}
                                    </td>

                                    <td class="text-center" data-title="'商户状态'">
                                        <div ng-show="row.agentStatus == 0 ">
                                            启用
                                        </div>
                                        <div ng-show="row.agentStatus == 1 ">
                                            禁用
                                        </div>
                                        <div ng-show="row.agentStatus == 2 ">
                                            未审核
                                        </div>
                                    </td>
                                    <td class="text-center" data-title="'代理商费率'">
                                        <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(4, row)">
                                            查看
                                        </button>
                                    </td>
                                        <td class="text-center" data-title="'代理商证件照'">
                                            <button class="btn btn-xs btn-primary no-margins" ng-click="showModal(5, row)">
                                                查看
                                            </button>
                                        </td>

                                </tr>
                            </table>
                        </div>
                        <my-pagination table-param="agmentMerchantInfoTable"></my-pagination>
                        <%--
                                                <button class="btn btn-sm btn-primary del-btn marginTop-minus" ng-click="batchDel()" ng-disabled="delDisabled()" >删除</button>
                        --%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
