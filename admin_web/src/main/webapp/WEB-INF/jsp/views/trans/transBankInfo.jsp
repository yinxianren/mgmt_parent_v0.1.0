
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title text-center" id="modal-title">付款订单信息</h3>
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
                    <td>{{transOrder.platformOrderId}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">收款主体名称</th>
                    <td>{{transOrder.cardholderName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行名称</th>
                    <td>{{TransBankInfo.bankName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡号</th>
                    <td>{{transOrder.bankCardNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">银行卡类型</th>
                    <td>{{TransBankInfo.bankcardNum | getValueByList : bankCardType : 'firstValue' : 'name'  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易流水号</th>
                    <td>{{transOrder.channelOrderId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道交易时间</th>
                    <td>{{transOrder.updateTime | date:'yyyy-MM-dd HH:mm:ss' }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易币种</th>
                    <td>{{transOrder.currency  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">订单状态</th>
                    <td>
                        <div ng-if="transOrder.status == 0 ">
                            成功
                        </div>
                        <div ng-if="transOrder.status == 1 ">
                            失败
                        </div>
                        <div ng-if="transOrder.status == 2 ">
                            未支付
                        </div>
                        <div ng-if="transOrder.status == 3 ">
                            支付中
                        </div></td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道名称</th>
                    <td>{{TransBankInfo.channelName  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">交易费率</th>
                    <td>{{transOrder.backFee  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">终端商户号</th>
                    <td>{{transOrder.terminalMerId  }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">代理商名称</th>
                    <td>{{TransBankInfo.agentMerchantName  }}</td>
                </tr>
                <tr>
                    <%-- <th class="text-center">姓名</th>
                     <th class="text-center">手机号</th>--%>
                    <th style="width: 50%;">证件类型</th>
                    <td>{{TransBankInfo.identityNum | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">证件号码</th>
                    <td>{{transOrder.identityNum}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">产品类型</th>
                    <td>{{TransBankInfo.productName}}</td>
                </tr>
                <tr>
                    <th style="width: 50%;">通道返回结果</th>
                    <td>{{transOrder.channelRespResult  }}</td>
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
                     <td class="text-center">{{TransBankInfo.orgOrderId  }}</td>
                     <td class="text-center">{{TransBankInfo.channelBankTime | date:'yyyy-MM-dd HH:mm:ss' }}</td>
                     <td class="text-center">{{TransBankInfo.currency  }}</td>
                     <td class="text-center">
                         <div ng-if="TransBankInfo.orderStatus == 0 ">
                             成功
                         </div>
                         <div ng-if="TransBankInfo.orderStatus == 1 ">
                             失败
                         </div>
                         <div ng-if="TransBankInfo.orderStatus == 2 ">
                             未支付
                         </div>
                         <div ng-if="TransBankInfo.orderStatus == 3 ">
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
                     <td class="text-center">{{TransBankInfo.channelBankResult  }}</td>
                 </tr>
             </table>
             <table class="table table-condensed table-striped table-hover table-bordered">
                 <tr>
                     <th class="text-center">交易费率</th>
                     <th class="text-center">终端商户号</th>
                     <th class="text-center">代理商名称</th>
                 </tr>
                 <tr>
                     <td class="text-center">{{TransBankInfo.terminalMerId  }}</td>
                     <td class="text-center">{{TransBankInfo.terminalMerId  }}</td>
                     <td class="text-center">{{TransBankInfo.agentMerchantName  }}</td>
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
                           <td>{{TransBankInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                    </tr>
                    <tr>
                        <th style="width: 50%;">证件号码</th>
                           <td>{{TransBankInfo.identityNum}}</td>
                    </tr>
                   &lt;%&ndash; <tr  >
                        &lt;%&ndash;<td class="text-center">{{TransBankInfo.cardholderName}}</td>
                        <td class="text-center">{{TransBankInfo.cardholderPhone}}</td>&ndash;%&gt;
                        <td class="text-center">{{TransBankInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                        <td class="text-center">{{TransBankInfo.identityNum}}</td>
                    </tr>&ndash;%&gt;
                </table>
            </div>--%>
        </div>
    </div>
</div>
