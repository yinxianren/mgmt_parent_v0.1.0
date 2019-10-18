<%--
  Created by IntelliJ IDEA.
  User: hul
  Date: 2019/5/18
  Time: 14:34
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
        display: inline-block;
        *display: inline;
        *zoom: 1
    }
    .a-upload input {
        width: 136px;
        height: 136px;
        position: absolute;
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
    .merchant-rate{
        width: 700px;
        height: 550px;
    }
    .modal-content{
        width: 700px;
    }
    .rate-tr-width {
        height: 60px;
    }
    .form-horizontal {
        overflow: auto;
        height: 430px;
        margin-bottom: 10px;
    }
</style>
<%--<div ng-show="goType == 0 || goType == 1 ">--%>
<div class="merchant-rate">
    <div class="modal-header"  >
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">代理商其它信息</h3>
    </div>
    <div class="modal-body" id="modal-body"  >
        <form name="myForm" class="form-horizontal" >
            <div class="ibox-content no-borders tab-content">
                <div class="form-group  m-t">
                    <label class="col-sm-2 control-label p-w-xs"><span class="text-danger"></span>代理商：</label>
                    <div class="col-sm-10">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="agentMerchantSetting.agentMerchantId"
                               ng-blur="merIdBlur($event, agentMerchantSetting.agentMerchantId)"
                               readonly
                               required>
                    </div>
                </div>
                <table>
                    <tr>
                        <th class="text-center rate-tr-width" style="min-width: 80px!important;">状态</th>
                        <th class="text-center rate-tr-width" style="min-width: 100px">支付方式</th>
                        <th class="text-center rate-tr-width" style="min-width: 100px">单笔手续费(元)</th>
                        <th class="text-center rate-tr-width">交易费率(%)</th>
                        <th class="text-center rate-tr-width">保证金比例(%)</th>
                        <th class="text-center rate-tr-width">结算周期</th>
                        <th class="text-center rate-tr-width">保证金周期</th>
                    </tr>
                    <tr ng-repeat="row in agentMerchantSettings track by $index" class="rate-tr-width">
                        <td class="text-center col-lg-2" style="min-width: 100px" >
                            {{row.status | getValueByList : status : 'firstValue' : 'name'  }}

                        </td>


                        <td class="text-center " style="min-width: 100px"  >
                            {{row.payType | getValueByList : payType : 'firstValue' : 'name'  }}
                        </td>
                        <td class="text-center col-lg-2" style="min-width: 100px" >
                            <input type="number"
                                   class="form-control"
                                   name="singleFee{{$index}}"
                                   ng-blur="singleFeeBlur($event, 'singleFee'+$index)"
                                   ng-model="row.singleFee"
                                   readonly
                                   required>
                        </td>
                        <td class="text-center col-lg-2"   >
                            <input type="text" class="form-control"
                                   name="rateFee{{$index}}"
                                   ng-blur="rateFeeBlur($event, 'rateFee'+$index)"
                                   ng-model="row.rateFee"
                                   readonly
                                   required>
                        </td>
                        <td class="text-center col-lg-2" >
                            <input type="text" class="form-control"
                                   name="bondRate{{$index}}"
                                   ng-blur="bondRateBlur($event, 'bondRate'+$index)"
                                   ng-model="row.bondRate"
                                   readonly
                                   required>
                        </td>
                        <td class="text-center col-lg-2"  >
                            <input type="text" class="form-control"
                                   name="settlecycle{{$index}}"
                                   ng-blur="settlecycleBlur($event, 'settlecycle'+$index)"
                                   ng-model="row.settlecycle"
                                   readonly
                                   required>
                        </td>
                        <td class="text-center col-lg-2"  >
                            <input type="text" class="form-control"
                                   name="bondCycle{{$index}}"
                                   ng-blur="bondCycleBlur($event, 'bondCycle'+$index)"
                                   ng-model="row.bondCycle"
                                   readonly
                                   required>
                        </td>
                    </tr>
                </table>

            </div>
        </form>
        <div class="row">

            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success"
                            type="button"
                            ng-disabled="checkRateDisabled(myForm,agentMerchantSettings)"
                            ng-click="cancel()">
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
