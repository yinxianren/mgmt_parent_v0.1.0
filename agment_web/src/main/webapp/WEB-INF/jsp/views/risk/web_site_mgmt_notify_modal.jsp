<%--
  Created by IntelliJ IDEA.
  User: 王德明
  Date: 2018/12/21
  Time: 16:17
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{notifyTitle}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <div class="p-sm">批量导入网址成功{{successNum}}条,失败{{failNum}}</div>
    <div class="col-sm-12 p-sm">
        <div class="center">
            <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                确定
            </button>
            <button class="btn btn-sm btn-primary" type="button" ng-click="confirm()" ng-show="isDownLoad">
                下载记录
            </button>
        </div>
    </div>
</div>

