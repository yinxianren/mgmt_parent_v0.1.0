<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/7/27
  Time: 14:55
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{type==0?'新增':'编辑'}}</h3>
</div>
<div class="modal-body">
    <div class="ibox-content no-borders tab-content">
        <form name="myForm" class="form-horizontal">
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>商户：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" type="text"
                           value="(${sessionScope.merchantInfo.merId})&nbsp${sessionScope.merchantInfo.merchantName}"
                           ng-disabled="true">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>电话：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" name="realName" type="text" ng-model="opinionInfo.phone"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>邮箱：</label>
                <div class="col-sm-10">
                    <input class="form-control b-r-sm" name="realName" type="text" ng-model="opinionInfo.email"
                           required>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>类型：</label>
                <div class="col-sm-10">
                    <select class="form-control b-r-sm" name="roleId" ng-model="opinionInfo.type"
                            ng-options="+(x.firstValue) as x.name for x in questionType" required>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>提交内容：</label>
                <div class="col-sm-10" ng-if="true">
                    <div text-angular ng-model="opinionInfo.content" ng-blur="textBlur(opinionInfo.content)"
                         required></div>
                </div>
            </div>
        </form>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-success" type="button" ng-click="confirm()"
                        ng-disabled="confirmDisabled(myForm)">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
