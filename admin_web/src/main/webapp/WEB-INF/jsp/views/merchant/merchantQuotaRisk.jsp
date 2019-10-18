<%--
  Created by IntelliJ IDEA.
  User: hul
  Date: 2019/5/18
  Time: 14:34
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
    .img-size {
        height: 136px;
        width: 136px;
    }
    .a-upload {
        text-align: center;
        line-height: 128px;
        position: relative;
        cursor: pointer;
        color: #888;
        background: #f2f2f2;
        border: 1px solid #ddd;
        display: inline-block;
        *display: inline;
        *zoom: 1
    }
    .a-upload input {
        width: 136px;
        height: 136px;
        position: absolute;
        left: 0;
        top: 0;
        opacity: 0;
        cursor: pointer
    }

    .a-upload:hover {
        color: #444;
        background: #eee;
        border-color: #ccc;
        text-decoration: none
    }
</style>
<%--<div ng-show="goType == 0 || goType == 1 ">--%>
<div>
    <div class="modal-header">
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">风控配置信息</h3>
    </div>
    <div class="modal-body" id="modal-body">
        <form name="myForm" class="form-horizontal">
            <div class="ibox-content no-borders tab-content">
                <div class="form-group  m-t">
                    <label class="col-sm-4 control-label p-w-xs"><span class="text-danger">*</span>商户：</label>
                    <div class="col-sm-8">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="merchantQuotaRisk.merId"
                               ng-blur="merIdBlur($event, merchantQuotaRisk.merId)"
                               readonly
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-4 control-label p-w-xs"><span class="text-danger">*</span>单笔交易限额：</label>
                    <div class="col-sm-8">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="merchantQuotaRisk.singleQuotaAmount"
                               <%--ng-blur="merIdBlur($event, merchantQuotaRisk.singleQuotaAmount)"--%>
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-4 control-label p-w-xs"><span class="text-danger">*</span>日交易限额：</label>
                    <div class="col-sm-8">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="merchantQuotaRisk.dayQuotaAmount"
                               <%--ng-blur="merIdBlur($event, merchantQuotaRisk.dayQuotaAmount)"--%>
                               required>
                    </div>
                </div>
                <div class="form-group  m-t">
                    <label class="col-sm-4 control-label p-w-xs"><span class="text-danger">*</span>月交易限额：</label>
                    <div class="col-sm-8">
                        <input class="form-control b-r-sm"
                               name="merId"
                               type="text"
                               ng-model="merchantQuotaRisk.monthQuotaAmount"
                               <%--ng-blur="merIdBlur($event, merchantQuotaRisk.monthQuotaAmount)"--%>
                               required>
                    </div>
                </div>


            </div>
        </form>
        <div class="row">
            <div class="col-sm-12">
                <div class="center">
                    <button class="btn btn-sm btn-success" type="button" ng-click="confirm(merchantQuotaRisk)"
                            <%--ng-disabled="checkDisabled(myForm)"--%>
                    >
                        确定
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
