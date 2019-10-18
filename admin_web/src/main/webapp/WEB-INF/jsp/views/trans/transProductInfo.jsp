<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/3/1
  Time: 17:02
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title text-center" id="modal-title">产品详情</h3>
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
        <%--<h3>订单信息：</h3>--%>
        <div class="table-responsive">
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">产品ID</th>
                    <th class="text-center">产品名称</th>
                    <th class="text-center">产品价钱</th>
                    <th class="text-center">产品数量</th>
                    <th class="text-center">产品地址</th>
                </tr>
                <tr>
                    <td class="text-center">{{PayProductDetail.productId  }}</td>
                    <td class="text-center">{{PayProductDetail.productName  }}</td>
                    <td class="text-center">{{PayProductDetail.price  }}</td>
                    <td class="text-center">{{PayProductDetail.quantity  }}</td>
                    <td class="text-center">{{PayProductDetail.url  }}</td>
                </tr>
            </table>
        </div>

        <div class="table-responsive" >
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">产品描述</th>
                    <th class="text-center">产品类型</th>
                </tr>
                <tr>
                    <td class="text-center">{{PayProductDetail.productDescribe}}</td>
                    <td class="text-center">{{PayProductDetail.productTypeId}}</td>
                </tr>
            </table>
            <table class="table table-condensed table-striped table-hover table-bordered">
                <tr>
                    <th class="text-center">平台订单号</th>
                    <th class="text-center">商户订单号</th>
                </tr>
                <tr  >
                    <td class="text-center">{{PayProductDetail.transId}}</td>
                    <td class="text-center">{{PayProductDetail.merOrderId}}</td>
                </tr>
            </table>
        </div>
    </div>
</div>
