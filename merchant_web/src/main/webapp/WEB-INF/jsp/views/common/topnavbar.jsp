<%@ page import="com.rxh.square.pojo.MerchantInfo" %><%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/5
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<toaster-container></toaster-container>
<div class="row border-bottom">
    <nav class="navbar navbar-fixed-top animated fadeInDown" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header" style="padding: 14px">
            <%--<% MerchantInfo merchantInfo = (MerchantInfo) request.getSession().getAttribute("merchantInfo");--%>
                <%--System.out.println(merchantInfo.getMerId());--%>
            <%--%>--%>

            <h3>您好：${sessionScope.merchantInfo.merId}(${sessionScope.merchantInfo.merchantName})，欢迎使用！</h3>
        </div>
        <ul class="nav navbar-top-links navbar-right">
            <li>
                <a href="/logout">
                    <i class="fa fa-sign-out"></i>&nbsp;退出
                </a>
            </li>
        </ul>
    </nav>
</div>
