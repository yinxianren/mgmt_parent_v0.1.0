<%--
    author:xuzm
    date:20180419

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '编辑支付产品' : '编辑支付产品'}}</h3>
</div>
<div class="modal-body" id="modal-body"  >
    <form id="productObjForm" name="productObjForm" class="form-horizontal">
        <div class="form-group">
            <label   class="col-sm-2 control-label"><font color="red">*</font>机构名称:</label>
            <div class="col-sm-10" >
                <input  class="form-control b-r-sm" type="text" id="organizationName" name="organizationName" tip="1"
                        ng-model="Organization.organizationName" class="form-control"
                        placeholder="必填/唯一" required="required" disabled="disabled"
                        ng-blur="nameBlur($event,Organization.organizationName)"
                />
            </div>
        </div>
        <div class="form-group">
            <label   class="col-sm-2 control-label" ><font color="red">*</font>产品类型:</label>
            <div class="col-sm-10" >
                <%--<input type="text" id="status" name="status" ng-model="Organization.status" tip="2" class="form-control" placeholder="必填/唯一"--%>
                <%--ng-blur="remarkBlur($event,Organization.status)"--%>
                <%--required="required" />--%>
                <select class="form-control b-r-sm" name="status" ng-model="Organization.status"
                        ng-blur="statusBlur($event, Organization.status)" required
                        ng-options="+(x.firstValue) as x.name for x in status">
                </select>
            </div>
        </div>
        <div class="form-group">

                <%--{{row.channelLevel}}--%>
            <label   class="col-sm-2 control-label" ><font color="red">*</font><span class="hidden-text" uib-tooltip="必填数字">费率:</span></label>

            <div class="col-sm-10" >
                <input type="number" id="rate" name="rate" max = "100"  class="form-control"
                       onkeyup="value=value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3')"
                       required="required" />
            </div>
        </div>


    </form>

    <div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" id="addOrganizationID" type="button" ng-click="addProduct()"
                        ng-disabled="nextDisabled(productObjForm)">
                    确定
                </button>
            </div>
        </div>
    </div>
</div>
