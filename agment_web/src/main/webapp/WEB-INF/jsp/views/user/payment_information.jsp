<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/24
  Time: 11:57
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="paymentInformation">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'PAYMENT_INFORMATION' | translate}}</h5>
                    </div>
                    <div class="ibox-content">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <div class="form-group">
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">加密类型</th>
                                        <th class="text-center">密钥</th>
                                        <th class="text-center">状态</th>
                                        <th class="text-center">支持币种</th>
                                        <th class="text-center">支付方式</th>
                                        <th class="text-center">支付类型</th>
                                        <th class="text-center">扣率</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat-start="(key, val) in payModeTable">
                                        <td class="text-center" style="vertical-align: middle;"
                                            rowspan="{{payRowspan}}" ng-show="$index == 0">
                                            {{merchantPay.algorithm | getValueByList:encrypt:'firstValue':'name'}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;"
                                            rowspan="{{payRowspan}}" ng-show="$index == 0">
                                            {{merchantPay.secretKey}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;"
                                            rowspan="{{payRowspan}}" ng-show="$index == 0">
                                            {{merchantPay.status | getValueByList:status:'firstValue':'name'}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;max-width: 200px;word-break:break-all;"
                                            rowspan="{{payRowspan}}" ng-show="$index == 0">
                                            {{merchantPay.currency}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;"
                                            rowspan="{{val.length}}">
                                            {{key | getValueByList:payMode:'firstValue':'name'}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;">
                                            {{val[0].payType}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;">
                                            {{val[0].rate}}
                                        </td>
                                    </tr>
                                    <tr ng-repeat-end ng-repeat="value in val.slice(1)">
                                        <td class="text-center" style="vertical-align: middle;">
                                            {{value.payType}}
                                        </td>
                                        <td class="text-center" style="vertical-align: middle;">
                                            {{val[0].rate}}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">保证金周期</th>
                                        <th class="text-center">保证金比率</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center">
                                            {{bailCycle}}
                                        </td>
                                        <td class="text-center">
                                            {{bailRate}}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

