<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>payResult</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/result.css"/>
</head>
<script type="text/javascript">
    function countDown(secs, surl) {
        var jumpTo = document.getElementById('jumpTo');
        jumpTo.innerHTML = secs;
        if (--secs > 0) {
            setTimeout("countDown(" + secs + ",'" + surl + "')", 1000);
        }
        else {
            document.getElementById("resultForm").submit();
        }
    }
</script>

<body>

<div class="warp">
    <c:choose>
        <c:when test="${sessionScope.crossResult.bankStatus == 0}">
            <div class="ok">
                <span><spring:message code="pay.1047"/></span>
            </div>
        </c:when>
        <c:when test="${sessionScope.crossResult.bankStatus == 1}">
            <div class="no">
                <span><spring:message code="pay.1048"/></span>
            </div>
        </c:when>
        <c:when test="${sessionScope.crossResult.bankStatus == 3}">
            <div class="process">
                <spring:message code="pay.1043"/>
            </div>
        </c:when>
    </c:choose>
    <table border="0" cellspacing="0" cellpadding="4">
        <tr>
            <th><spring:message code="pay.1023"/>:</th>
            <td><span class="msg">${sessionScope.tradeOject.billNo}</span></td>
        </tr>
        <tr>
            <th><spring:message code="pay.1024"/>:</th>
            <td>${sessionScope.crossResult.orderId}</td>
        </tr>
        <tr>
            <th><spring:message code="pay.1001"/>:</th>
            <td>${sessionScope.tradeOject.amount}</td>
        </tr>
        <tr>
            <th><spring:message code="pay.1049"/>:</th>
            <td><spring:message code="${sessionScope.crossResult.bankCode}"/></td>
        </tr>
    </table>
</div>
<div class="footer">
    <input type="button" name="button" id="buttonSt" value=<spring:message code="pay.1045"/>/>
</div>
<div style="display: none;">
    <form action="${sessionScope.tradeObject.returnURL}" id="resultForm" method="post">
        <input name="billNo" value="${sessionScope.tradeObject.billNo}">
        <input name="tradeNo" value="${sessionScope.crossResult.orderId}">
        <input name="currency" value="${sessionScope.tradeObject.currency}">
        <input name="amount" value="${sessionScope.tradeObject.amount}">
        <input name="succeed" value="${sessionScope.crossResult.bankStatus}">
        <input name="customerId" value="${sessionScope.tradeObject.customerId}">
        <input name="result" value="<spring:message code="${sessionScope.crossResult.bankCode}"/>">
        <input name="md5Info" value="${sessionScope.crossResult.md5Info}">
        <input name="remark" value="<spring:message code="${sessionScope.crossResult.bankCode}"/>">
    </form>
</div>
<span id="jumpTo">5</span>秒后自动跳转到支付结果页
<script type="text/javascript">countDown(5, '${sessionScope.tradeObject.returnURL}');</script>
</body>
</html>