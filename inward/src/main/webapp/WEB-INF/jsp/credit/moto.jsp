<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><spring:message code="pay.1000"/></title>
    <link href="${pageContext.request.contextPath}/css/index.css" type="text/css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/keyboard.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery/jquery-1.12.4.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common/pay.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common/keyboard.js"></script>
    <script type="text/javascript">

        function editBillfun() {
            var ch = document.getElementById("editBill").checked;
            if (ch) {
                document.getElementById("nr_b_center").style.display = "";
            } else {
                document.getElementById("nr_b_center").style.display = "none";
            }
        }

        function sendFormLocale() {
            location.href = "/cross_pay?locale=" + document.getElementById("locale").value;
        }

        $(document).ready(function () {
            document.getElementById("locale").value = '<%=response.getLocale()%>';
            changecardnoshow('<%=request.getContextPath()%>');
            document.getElementById("nr_b_center").style.display = "none";
        });
    </script>
    <%
      Integer year=  Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        session.setAttribute("currYear", year);
    %>
</head>
<body>
<div id="warp">
    <div id="header">
        <div id="logo">
            <div id="logo_left">
                <img src="${pageContext.request.contextPath}/img/logo.jpg" height="49"/>
            </div>
        </div>
        <div id="subnav_menu">
            <select name="locale" id="locale" onchange="sendFormLocale()">
                <option value="en">English</option>
                <!-- 英语 -->
                <option value="zh_CN">简体中文</option>
                <option value="it">Italiano</option>
                <!-- 意大利语 -->
                <option value="ko">한국어</option>
                <!-- 韩语 -->
                <option value="ja">日語</option>
                <!-- 日语 -->
                <option value="ru">Русский</option>
                <!-- 俄语 -->
                <option value="es">Español</option>
                <!-- 西班牙语-->
                <option value="pt">Português</option>
                <!-- 葡萄牙语 -->
                <option value="fr">Français</option>
                <!-- 法语 -->
                <option value="de">Deutsch</option>
                <!-- 德语 -->
                <option value="ar">العربية</option>
                <!-- 阿拉伯语 -->
            </select>
        </div>
    </div>
    <div id="maincontent">
        <div class="left">
            <div id="payment">
                <div id="pay_left" class="float">
                    <img src="${pageContext.request.contextPath}/img/pay_left.gif" width="12" height="109"/>
                </div>
                <div id="pay_center" class="float">
                    <div id="pay_cen_title">
                        <img src="${pageContext.request.contextPath}/img/small_menu.gif"/>
                        <span><spring:message code="pay.1001"/>:</span>
                        <b style="font-weight: bold;">${tradeObject.amount} ${tradeObject.currency}</b>
                    </div>
                    <div id="pay_cen_nr"><spring:message code="pay.1002"/></div>
                </div>
                <div id="pay_right" class="float">
                    <img src="${pageContext.request.contextPath}/img/pay_right.gif" width="12" height="109"/>
                </div>
            </div>
            <form action="/cross_payment" name="sendForm" id="paySend" method="post"
                  onsubmit="return checkFormSb()">
                <div class="basic">
                    <h3 id="basic_title">
                        <img src="${pageContext.request.contextPath}/img/small_menu.gif"/>
                        <strong><spring:message code="pay.1003"/></strong>
                        &nbsp;&nbsp;&nbsp;
                        <span id="errorMsgId"></span>
                    </h3>

                    <div id="basic_nr">
                        <ul>
                            <li>
                                <label><spring:message code="pay.1004"/>:</label>
                                <input type="text" name="cardNo" id="cardNo" class="basic_input"
                                       onkeyup="changecardnoshow('<%=request.getContextPath()%>')"
                                       onselectstart="javascript:return false;" onpaste="javascript:return false; "
                                       maxlength="16"/>
                                <img id="vistaPic" src="${pageContext.request.contextPath}/img/left_ico01.gif"
                                     width="32" height="20"/>
                                <img id="vistaLoss" style="display: none"
                                     src="${pageContext.request.contextPath}/img/visa_loss.gif" width="32" height="20"/>
                                <img id="masterPic" src="${pageContext.request.contextPath}/img/left_ico02.gif"
                                     width="32" height="20"/>
                                <img id="masterLoss" style="display: none"
                                     src="${pageContext.request.contextPath}/img/master_loss.gif" width="32"
                                     height="20"/>
                                <img id="jcbPic" src="${pageContext.request.contextPath}/img/jcb.gif" width="32"
                                     height="20"/>
                                <img id="jcbLoss" style="display: none"
                                     src="${pageContext.request.contextPath}/img/jcb_loss.gif" width="32" height="20"/>
                                <img id="amexPic" src="${pageContext.request.contextPath}/img/amex.gif" width="32"
                                     height="20"/>
                                <img id="amexLoss" style="display: none"
                                     src="${pageContext.request.contextPath}/img/amex_loss.gif" width="32" height="20"/>
                            </li>
                            <li>
                                <label><spring:message code="pay.1005"/>:</label>
                                <select name="cardExpireMonth" id="cardExpireMonth">
                                    <option value="">--</option>
                                    <option value="01">01</option>
                                    <option value="02">02</option>
                                    <option value="03">03</option>
                                    <option value="04">04</option>
                                    <option value="05">05</option>
                                    <option value="06">06</option>
                                    <option value="07">07</option>
                                    <option value="08">08</option>
                                    <option value="09">09</option>
                                    <option value="10">10</option>
                                    <option value="11">11</option>
                                    <option value="12">12</option>
                                </select>
                                <select name="cardExpireYear" id="cardExpireYear">
                                    <option value="">----</option>
                                    <%--<c:forEach var="i" begin="${year}" end="${year + 9}" step="1">--%>
                                    <c:forEach var="i" begin="${currYear}" end="${currYear + 9}" step="1">
                                        <option value="${i}">${i}</option>
                                    </c:forEach>
                                </select>
                                <i id="cvvTxt" style="width: 40px; margin-right: 2px;">
                                    <spring:message code="pay.1006"/>:</i>
                                <input type="password" name="cvv2" id="cvv2" maxlength="4"
                                       class="keyboardInput basic_input"/>
                                <a href="cvv_demo.html" target="_blank" style="text-decoration: none;">
                                    <img src="${pageContext.request.contextPath}/img/left_ico03.jpg"/>&nbsp;&nbsp;?
                                </a>
                                <span id="helpsoftkey"></span>
                            </li>
                            <li>
                                <label><spring:message code="pay.1007"/>:</label>
                                <input name="issuingBank" id="issuingBank" type="text" value="${tradeObject.issue}"
                                       class="basic_input"/>
                            </li>
                            <li>
                                <label><spring:message code="pay.1008"/>:</label>
                                <input name="firstName" id="firstName" value="${tradeObject.billFirstName}" type="text"
                                       class="basic_input"/>
                            </li>
                            <li>
                                <label><spring:message code="pay.1009"/>:</label>
                                <input name="lastName" id="lastName" value="${tradeObject.billLastName}" type="text"
                                       class="basic_input"/>
                            </li>
                        </ul>
                    </div>
                    <div class="cl"></div>
                </div>

                <div class="basic_nr_bottom">
                    <div id="nr_b_title">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <img src="${pageContext.request.contextPath}/img/small_menu.gif"/>
                        <strong><spring:message code="pay.1011"/></strong>
                        &nbsp;&nbsp;
                        <input type="checkbox" id="editBill" name="editBill" onclick="editBillfun()"/>
                    </div>
                    <div id="nr_b_center">
                        <div id="cen_left">
                            <ul>
                                <li>
                                    <label><spring:message code="pay.1012"/>:</label>
                                    <input class="basic_input" name="address" id="address" type="text"
                                           value="${tradeObject.billAddress}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1013"/>:</label>
                                    <input class="basic_input" name="city" id="city" type="text"
                                           value="${tradeObject.billCity}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1014"/>:</label>
                                    <input class="basic_input" name="state" id="state" type="text"
                                           value="${tradeObject.billState}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1015"/>:</label>
                                    <input class="basic_input" type="text" name="country" id="country"
                                           value="${tradeObject.billCountry}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1016"/>:</label>
                                    <input class="basic_input" name="zip" id="zip" type="text"
                                           value="${tradeObject.billZip}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1017"/>:</label>
                                    <input class="basic_input" name="email" id="email" type="text"
                                           value="${tradeObject.billEmail}"/>
                                </li>
                                <li>
                                    <label><spring:message code="pay.1018"/>:</label>
                                    <input class="basic_input" name="phone" id="phone" type="text"
                                           value="${tradeObject.billPhone}"/>
                                </li>
                            </ul>
                        </div>
                        <div id="cen_right">
                            <spring:message code="pay.1027"/>
                        </div>
                    </div>
                    <div class="cl"></div>
                </div>
                <div class="btn">
                    <img src="${pageContext.request.contextPath}/img/shuo.jpg"/>
                    <spring:message code="pay.1019"/>
                    <br/>
                    <input type="submit" style="cursor: pointer" id="buttonSt" value=
                            <spring:message code="pay.1020"/>/>
                </div>
            </form>
            <div id="site_url">
                <a href="${orderTrack.refer}"><spring:message code="pay.1021"/> ${orderTrack.refer} </a>
            </div>
        </div>
        <!--end left-->
        <div class="right">
            <div id="r_top">
                <img src="${pageContext.request.contextPath}/img/right_top_bj.gif" width="11" height="699"/>
            </div>
            <div id="r_center">
                <div id="order">
                    <div id="order_title">
                        <img src="${pageContext.request.contextPath}/img/order_title.gif"/>
                        <span><spring:message code="pay.1022"/></span>
                    </div>
                    <div id="order_nr">
                        <dl>
                            <dd>
                                <spring:message code="pay.1023"/>:
                                <span class="font_yellow">${tradeObject.merNo}</span>
                            </dd>
                            <dd>
                                <spring:message code="pay.1024"/>:
                                <span class="font_yellow">${tradeObject.billNo}</span>
                            </dd>
                            <dd>
                                <spring:message code="pay.1025"/>:
                                <span class="font_yellow"> ${orderTrack.refer}</span>
                            </dd>
                        </dl>
                    </div>
                </div>
                <div id="text">
                    <div>
                        <spring:message code="pay.1026"
                                        arguments='<span style="color: #226FBB">${tradeObject.billEmail}</span>,<a href="${orderTrack.refer}" target="_blank">${orderTrack.refer}</a>'
                                        argumentSeparator=",">
                        </spring:message>
                    </div>
                </div>
                <div style="width: 100%; text-align: center; clear: both;">
                    <div style="padding-top: 10px;">
                        <img width="180" height="90" src="${pageContext.request.contextPath}/img/SL1.png"
                             style="cursor: pointer;"
                             onclick="javascript:window.open('https://portal.3sisecure.com/images/pcs/3CA6F06A-26F8-483D-B67D-94D220A252C6.html?style=&baseURL=', 'complianceSummary', 'location=no, toolbar=no, resizable=yes, scrollbars=yes, directories=no, status=no, width=800,height=1034'); return false;"
                             oncontextmenu="javascript:alert('This page and image are copyright protected by the Secure Logic Group.'); return false;"
                             alt="This site is protected by Secure Logic's 3Si Service"
                             title="This site is protected by Secure Logic's 3Si Service"/>
                    </div>

                    <div style="margin-top: 10px;">
                        <img width="180" height="90"
                             src="${pageContext.request.contextPath}/img/trustwave.png"
                             style="cursor: pointer;"
                             onclick="javascript:window.open('https://sealserver.trustwave.com/cert.php?customerId=281247&size=105x54&style=invert&language=en&form=&baseURL=merchant.rxhpay.com', 'trustwave', 'location=no, toolbar=no, resizable=yes, scrollbars=yes, directories=no, status=no, width=615, height=720'); return false;"
                             oncontextmenu="javascript:alert('Copying Prohibited by Law - Trusted Commerce is a Service Mark of TrustWave Holdings, Inc.'); return false;"
                             alt="This site is protected by Trustwave's Trusted Commerce program"
                             title="This site is protected by Trustwave's Trusted Commerce program"/>
                    </div>
                    <div style="margin-top: 10px;">
                        <img width="180" height="90" src="${pageContext.request.contextPath}/img/verified.jpg"
                             alt="verified"/>
                    </div>
                </div>
            </div>
            <div id="r_bottom">
                <img src="${pageContext.request.contextPath}/img/right_bottom_bj.gif" width="11" height="699"/>
            </div>
        </div>
        <div class="cl"></div>
    </div>
</div>
<!-- 实现错误信息国际化使用 -->
<span id="cardNoMsg" style="display:none"><spring:message code="pay.1029"/></span>
<span id="cardExpireMonthMsg" style="display:none"><spring:message code="pay.1030"/></span>
<span id="cardExpireYearMsg" style="display:none"><spring:message code="pay.1031"/></span>
<span id="cvv2Msg" style="display:none"><spring:message code="pay.1033"/></span>
<span id="issuingBankMsg" style="display:none"><spring:message code="pay.1034"/></span>
<span id="firstNameMsg" style="display:none"><spring:message code="pay.1035"/></span>
<span id="lastNameMsg" style="display:none"><spring:message code="pay.1036"/></span>
<span id="addressMsg" style="display:none"><spring:message code="pay.1037"/></span>
<span id="cityMsg" style="display:none"><spring:message code="pay.1038"/></span>
<span id="countryMsg" style="display:none"><spring:message code="pay.1039"/></span>
<span id="zipMsg" style="display:none"><spring:message code="pay.1040"/></span>
<span id="emailMsg" style="display:none"><spring:message code="pay.1041"/></span>
<span id="phoneMsg" style="display:none"><spring:message code="pay.1042"/></span>
</body>
</html>
