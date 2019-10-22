
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title text-center" id="modal-title">付款账号信息</h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="ibox-content sk-loading no-padding no-borders">
        <div class="sk-spinner sk-spinner-wave">
            <div class="sk-rect1"></div>
            <div class="sk-rect2"></div>
            <div class="sk-rect3"></div>
            <div class="sk-rect4"></div>
            <div class="sk-rect5"></div>
        </div>
        <h3>订单信息：</h3>
        <div class="table-responsive">
            <table class="table table-condensed table-striped table-hover table-bordered table-th">
                <tr>
                    <th style="width: 50%;">订单号</th>
                    <td>{{cardHolderInfo.payId}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">收款主体名称</th>
                    <td>{{cardHolderInfo.cardholderName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行名称</th>
                    <td>{{cardHolderInfo.bankName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡号</th>
                    <td>{{cardHolderInfo.bankcardNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡类型</th>
                    <td>{{cardHolderInfo.bankcardType | getValueByList : bankcardType : 'firstValue' : 'name'  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易流水号</th>
                    <td>{{cardHolderInfo.orgOrderId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易时间</th>
                    <td>{{cardHolderInfo.channelBankTime | date:'yyyy-MM-dd HH:mm:ss' }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易币种</th>
                    <td>{{cardHolderInfo.currency  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">订单状态</th>
                    <td>
                        <div ng-if="cardHolderInfo.orderStatus == 0 ">
                            成功
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 1 ">
                            失败
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 2 ">
                            未支付
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 3 ">
                            支付中
                        </div></td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道名称</th>
                    <td>{{cardHolderInfo.channelName  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易费率</th>
                    <td>{{cardHolderInfo.payFee  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">终端商户号</th>
                    <td>{{cardHolderInfo.terminalMerId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">代理商名称</th>
                    <td>{{cardHolderInfo.agentMerchantName  }}</td>
                </tr>
                <tr>
                    <%-- <th class="text-center">姓名</th>
                     <th class="text-center">手机号</th>--%>
                    <th style="width: 50%;">证件类型</th>
                    <td>{{cardHolderInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">证件号码</th>
                    <td>{{cardHolderInfo.identityNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">产品类型</th>
                    <td>{{cardHolderInfo.productName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道返回结果</th>
                    <td>{{cardHolderInfo.channelBankResult  }}</td>
                </tr>
            </table>
           <%-- <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">通道交易流水号</th>
                    <th class="text-center">通道交易时间</th>
                    <th class="text-center">交易币种</th>
                    <th class="text-center">订单状态</th>
                </tr>
                <tr>
                    <td class="text-center">{{cardHolderInfo.orgOrderId  }}</td>
                    <td class="text-center">{{cardHolderInfo.channelBankTime | date:'yyyy-MM-dd HH:mm:ss' }}</td>
                    <td class="text-center">{{cardHolderInfo.currency  }}</td>
                    <td class="text-center">
                        <div ng-if="cardHolderInfo.orderStatus == 0 ">
                            成功
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 1 ">
                            失败
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 2 ">
                            未支付
                        </div>
                        <div ng-if="cardHolderInfo.orderStatus == 3 ">
                            支付中
                        </div>
                    </td>
                </tr>
            </table>--%>
           <%-- <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">通道返回结果</th>
                </tr>
                <tr>
                    <td class="text-center">{{cardHolderInfo.channelBankResult  }}</td>
                </tr>
            </table>
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">交易费率</th>
                    <th class="text-center">终端商户号</th>
                    <th class="text-center">代理商名称</th>
                </tr>
                <tr>
                    <td class="text-center">{{cardHolderInfo.terminalMerId  }}</td>
                    <td class="text-center">{{cardHolderInfo.terminalMerId  }}</td>
                    <td class="text-center">{{cardHolderInfo.agentMerchantName  }}</td>
                </tr>
            </table>
        </div>--%>

        <%--<h3  >证件信息：</h3>
        <div class="table-responsive" >
            <table class="table table-condensed table-striped table-hover table-bordered table-th">
                <tr>
                   &lt;%&ndash; <th class="text-center">姓名</th>
                    <th class="text-center">手机号</th>&ndash;%&gt;
                    <th style="width: 50%;">证件类型</th>
                       <td>{{cardHolderInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">证件号码</th>
                       <td>{{cardHolderInfo.identityNum}}</td>
                </tr>
               &lt;%&ndash; <tr  >
                    &lt;%&ndash;<td class="text-center">{{cardHolderInfo.cardholderName}}</td>
                    <td class="text-center">{{cardHolderInfo.cardholderPhone}}</td>&ndash;%&gt;
                    <td class="text-center">{{cardHolderInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                    <td class="text-center">{{cardHolderInfo.identityNum}}</td>
                </tr>&ndash;%&gt;
            </table>
        </div>--%>
    </div>
</div>
</div>
