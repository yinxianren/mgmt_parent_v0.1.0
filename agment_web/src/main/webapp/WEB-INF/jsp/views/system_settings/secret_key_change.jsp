<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/27
  Time: 10:35
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!-- Main view -->
<div class="animated fadeInRight">
    <div class="wrapper wrapper-content ng-scope" ng-controller="agentSecretKeyChangeCtrl">
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>{{ 'SECRET_KEY_CHANGE' | translate }}</h5>
                    </div>
                    <div class="ibox-content <%--sk-loading--%>">
                        <div class="sk-spinner sk-spinner-wave">
                            <div class="sk-rect1"></div>
                            <div class="sk-rect2"></div>
                            <div class="sk-rect3"></div>
                            <div class="sk-rect4"></div>
                            <div class="sk-rect5"></div>
                        </div>
                        <form name="myForm" class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label p-w-xs"><span
                                        class="text-danger">*</span>原密钥：</label>
                                <div class="col-sm-2">
                                    <input class="form-control b-r-sm" type="text" value="{{merchantInfo.secretKey}}"
                                           ng-disabled="true"><button class="btn btn-xs btn-primary" style="display: inline"    ng-click="getNewSecretKey()" >生成秘钥</button>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label p-w-xs"><span
                                        class="text-danger">*</span>新密钥：</label>
                                <div class="col-sm-2">
                                    <input class="form-control b-r-sm" name="realName" type="text"
                                           ng-model="newSecretKey" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button class="btn btn-sm btn-w-m btn-success" ng-disabled="!myForm.$valid"
                                            ng-click="confirm(newSecretKey)">
                                        确定
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>