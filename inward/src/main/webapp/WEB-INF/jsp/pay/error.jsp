<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>RxhPay Payment errors</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css"/>
</head>

<body>
<div id="main">
    <div id="content">
        <div id="title"><img src="${pageContext.request.contextPath}/img/logo.jpg"/></div>
        <div id="left">
            <img src="${pageContext.request.contextPath}/img/forbid.gif"/>
        </div>
        <div id="right">
            <spring:message code="pay.1044"/> : <font style="color: red;">error.${code}</font>
            <br/>
            <spring:message code="pay.1049"/> : <font style="color: red;"><spring:message code="error.${code}"/></font>
            <br/>
            <a href=''><spring:message code="pay.1045"/></a>
        </div>
        <div id="clean">
            <div>
                <spring:message code="pay.1046"/>&nbsp;&nbsp;<a href="mailto:service@rxhpay.com">service@rxhpay.com</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
