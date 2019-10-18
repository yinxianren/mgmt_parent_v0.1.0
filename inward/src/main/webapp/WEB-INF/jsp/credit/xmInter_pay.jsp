
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.rxh.pojo.core.CoreOrder"%>
<%@page import="com.rxh.utils.SHA256Utils"%>

<%
    request.setCharacterEncoding("UTF-8");//字符编码
    CoreOrder order = (CoreOrder) session.getAttribute("order");
    String merNum = session.getAttribute("merNum").toString();
    String md5key = session.getAttribute("md5key").toString();
    String orderAmt = session.getAttribute("orderAmt").toString();
    String orderCurr = session.getAttribute("orderCurr").toString();

    String signature = SHA256Utils.SHA256Utils("Version=10" + "&DeviceType=WEB" + "&CharacterSet=utf8" + "&SignType=sha256" + "&GatewayName=MasterCard" + "&OrderId="+order.getId()
            + "&MerchantNo="+merNum + "&TerminalNo=05001703" + "&OrderCurr="+orderCurr
            + "&OrderAmt="+orderAmt) + md5key;
    String signature2 = SHA256Utils.SHA256Utils(signature);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>银行支付接口</title>
</head>
<body>
<form method="post" name="xmInter_pay" id="xmInter_pay" action="http://140.207.147.250:8006/cashier/payment/" style="display: none">
    <%--必须传递参数--%>
    <input type="hidden" name="Version" value="10"/>
    <input type="hidden" name="DeviceType" value="WEB"/>
    <input type="hidden" name="CharacterSet" value="utf8"/>
    <input type="hidden" name="SignType" value="sha256"/>
    <input type="hidden" name="GatewayName" value="MasterCard"/>
    <input type="hidden" name="OrderId" value="${order.id}"/>
    <input type="hidden" name="MerchantNo" value="${merNum}"/>
    <input type="hidden" name="TerminalNo" value="05001703"/>
    <input type="hidden" name="OrderCurr" value="${orderCurr}"/>
    <input type="hidden" name="OrderAmt" value="${orderAmt}"/>
    <input type="hidden" name="Signature" value="<%=signature2%>"/>
</form>

<script type="text/javascript">
    document.getElementById("xmInter_pay").submit();
</script>
</body>
</html>
