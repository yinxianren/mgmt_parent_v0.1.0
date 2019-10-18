<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/5/23
  Time: 9:47
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{alertTitle}}</h3>
</div>
<div class="modal-body" id="modal-body">
   </div><div class="p-sm" ng-bind-html="alertContent"></div>
    <div class="col-sm-12 p-sm">
        <div class="center">
            <button class="btn btn-sm btn-primary" type="button" ng-click="confirm()">
                确定
            </button>
            <button class="btn btn-sm btn-info" type="button" ng-click="cancel()">
                取消
            </button>
        </div>
    </div>
</div>