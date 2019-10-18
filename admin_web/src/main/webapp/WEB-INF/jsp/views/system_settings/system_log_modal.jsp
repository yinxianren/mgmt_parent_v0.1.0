<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/5/15
  Time: 9:09
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title">日志信息</h3>
</div>
<div class="modal-body">
    <pre class="p-m" ng-show="logInfo.messageJson == null">{{logInfo.message}}</pre>
    <pre class="p-m" ng-show="logInfo.messageJson != null">{{logInfo.messageJson | json}}</pre>
</div>
