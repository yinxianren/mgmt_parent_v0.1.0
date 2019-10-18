<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '新增' : '编辑'}}</h3>
</div>
<div class="modal-body" id="modal-body">
    <form id="webSiteObjForm" role="form" name="webSiteObjForm" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label"><span class="text-danger">*</span>商户域名</label>
            <div class="col-sm-8">
                <input class="form-control" type="text" name="siteUrl" ng-model="tempData.siteUrl"
                       ng-pattern="strRegex" ng-blur="urlBlur($event, tempData.siteUrl)" placeholder="域名格式：baidu.com" required/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label"><span class="text-danger">*</span>行业类型:</label>
            <div class="col-sm-8">
                <select class="form-control" ng-model="tempData.industryId" name="industryId"
                        ng-options="+(x.firstValue) as x.name for x in industryType"
                        required></select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-3 control-label">备注</label>
            <div class="col-sm-8">
                <input type="text" name="remark" tip="2" ng-model="tempData.remark" ng-focus="generalFocus($event)"
                       class="form-control" placeholder=""/>
            </div>
        </div>
    </form>
    <div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" type="button"
                        ng-disabled="webSiteObjForm.$invalid || isExist"
                        ng-click="confirm()">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
