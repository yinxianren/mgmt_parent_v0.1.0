
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title text-center" id="modal-title">收单订单信息</h3>
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
                    <td>{{payOrder.platformOrderId}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">收款主体名称</th>
                    <td>{{cardHolderInfo.cardholderName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行名称</th>
                    <td>{{payOrder.bankCode}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡号</th>
                    <td>{{payOrder.bankCardNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡类型</th>
                    <td>{{cardHolderInfo.bankcardNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易流水号</th>
                    <td>{{payOrder.channelOrderId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易时间</th>
                    <td>{{cardHolderInfo.channelBankTime | date:'yyyy-MM-dd HH:mm:ss' }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易币种</th>
                    <td>{{payOrder.currency  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">订单状态</th>
                    <td>
                        <div ng-if="payOrder.status == 0 ">
                            成功
                        </div>
                        <div ng-if="payOrder.orderStatus == 1 ">
                            失败
                        </div>
                        <div ng-if="payOrder.status == 2 ">
                            未支付
                        </div>
                        <div ng-if="payOrder.status == 3 ">
                            支付中
                        </div></td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道名称</th>
                    <td>{{cardHolderInfo.channelName  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易费率</th>
                    <td>{{payOrder.payFee  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">终端商户号</th>
                    <td>{{payOrder.terminalMerId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">代理商名称</th>
                    <td>{{cardHolderInfo.agentMerchantName  }}</td>
                </tr>
                <tr>
                    <%-- <th class="text-center">姓名</th>
                     <th class="text-center">手机号</th>--%>
                    <th style="width: 50%;">证件类型</th>
                    <td>{{cardHolderInfo.identityNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">证件号码</th>
                    <td>{{payOrder.identityNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">产品类型</th>
                    <td>{{cardHolderInfo.productName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道返回结果</th>
                    <td>{{payOrder.channelRespResult  }}</td>
                </tr>
            </table>
    </div>
</div>
</div>
