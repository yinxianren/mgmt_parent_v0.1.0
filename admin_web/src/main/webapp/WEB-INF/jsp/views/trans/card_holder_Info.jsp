
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
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">订单号</th>
                    <th class="text-center">收款主体名称</th>
                    <th class="text-center">银行名称</th>
                    <th class="text-center">银行卡号</th>
                    <th class="text-center">银行卡类型</th>
                </tr>
                <tr  >
                    <td class="text-center">{{cardHolderInfo.payId}}</td>
                    <td class="text-center">{{cardHolderInfo.benefitName}}</td>
                    <td class="text-center">{{cardHolderInfo.bankName}}</td>
                    <td class="text-center">{{cardHolderInfo.bankcardNum}}</td>
                    <td class="text-center">{{cardHolderInfo.bankcardType | getValueByList : bankcardType : 'firstValue' : 'name'  }}</td>
                </tr>
            </table>
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center" style="width: 50%;">收款银行网点名称</th>
                    <th class="text-center">收款行开户行号</th>
                </tr>
                <tr>
                    <td class="text-center">{{cardHolderInfo.bankBranchName  }}</td>
                    <td class="text-center">{{cardHolderInfo.bankBranchNum  }}</td>
                </tr>
            </table>
        </div>

        <h3  >证件信息：</h3>
        <div class="table-responsive" >
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                   <%-- <th class="text-center">姓名</th>
                    <th class="text-center">手机号</th>--%>
                    <th class="text-center" style="width: 50%;">证件类型</th>
                    <th class="text-center">证件号码</th>
                </tr>
                <tr  >
                    <%--<td class="text-center">{{cardHolderInfo.cardholderName}}</td>
                    <td class="text-center">{{cardHolderInfo.cardholderPhone}}</td>--%>
                    <td class="text-center">{{cardHolderInfo.identityType | getValueByList : identityType : 'firstValue' : 'name'   }}</td>
                    <td class="text-center">{{cardHolderInfo.identityNum}}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
