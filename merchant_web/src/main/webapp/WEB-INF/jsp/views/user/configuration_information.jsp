<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/24
  Time: 10:30
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="configurationInformation" >
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{'CONFIGURATION_INFORMATION' | translate}}</h5>
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
                            <label>结算信息</label>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">开户银行</th>
                                        <th class="text-center">开户人名</th>
                                        <th class="text-center">开户账号</th>
                                        <th class="text-center">结算币种</th>
                                        <th class="text-center">结算周期</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center">
                                            {{merchantSetting.bankName}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.bankOwner}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.bankAccount}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.transferCurrency | getValueByList : currency :
                                            'firstValue' : 'name'}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.settleCycle}}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>费用信息</label>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">开户费</th>
                                        <th class="text-center">年费</th>
                                        <th class="text-center">划款手续费（CNY）</th>
                                        <th class="text-center">拒付处理费</th>
                                        <th class="text-center">退款手续费</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center">
                                            {{merchantSetting.openFee}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.yearFee}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.transferFee}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.dishonor}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.refundFee}}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>合同信息</label>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">合同开始时间</th>
                                        <th class="text-center">合同结束时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center">
                                            {{merchantSetting.startTime | date:'yyyy-MM-dd hh:mm:ss sss'}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.endTime | date:'yyyy-MM-dd hh:mm:ss sss'}}
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>物流设置</label>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">跟踪设置</th>
                                        <th class="text-center">跟踪审核</th>
                                        <th class="text-center">是否妥投</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="text-center">
                                            {{merchantSetting.trackType | getValueByList : trackSetting : 'firstValue' :
                                            'name'}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.trackAudit | getValueByList : trackAudit : 'firstValue' :
                                            'name'}}
                                        </td>
                                        <td class="text-center">
                                            {{merchantSetting.correctDistribute | getValueByList : correctDistribute :
                                            'firstValue' : 'name'}}
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

