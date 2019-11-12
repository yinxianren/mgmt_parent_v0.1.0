<%--
    author:xuzm
    date:20180419

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '添加支付机构' : '修改支付机构'}}</h3>
</div>
<div class="modal-body" id="modal-body"  >
		 <form id="OrganizationObjForm" name="OrganizationObjForm" class="form-horizontal">
			  <div class="form-group">
			    <label   class="col-sm-2 control-label"><font color="red">*</font>机构名称:</label>
			    <div class="col-sm-10" >
			     <input  class="form-control b-r-sm" type="text" id="organizationName" name="organizationName" tip="1"
						 ng-model="Organization.organizationName" class="form-control"
						placeholder="必填/唯一" required="required"
						 ng-blur="nameBlur($event,Organization.organizationName)"
				 />
			    </div>
			  </div>
			  <%--<div class="form-group">
			    <label   class="col-sm-2 control-label" ><font color="red">*</font>备注:</label>
			    <div class="col-sm-10" >
			     <input type="text" id="remark" name="remark" ng-model="Organization.remark" tip="2" class="form-control" placeholder="必填/唯一"
						ng-blur="remarkBlur($event,Organization.remark)"
						required="required" />
		        </div>
			  </div>--%>

			 <div class="form-group">
				 <label   class="col-sm-2 control-label" ><font color="red">*</font>状态:</label>
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
				 <label class="col-sm-2 control-label p-w-xs"><span class="text-danger">*</span>支付产品：</label>
				 <div class="col-sm-10">
					 <div class="col-sm-3 p-w-xs" ng-repeat="x in productTypes">
						 <div class="checkbox no-margins">
							 <input id="{{x.firstValue}}" type="checkbox" name="productIds"
									ng-checked="exists(x.firstValue,Organization.productIds)"
									value="{{x.firstValue}}" required>
							 <label for="{{x.firstValue}}" class="no-padding">{{x.name}}</label>
						 </div>
					 </div>
				 </div>
			 </div>
		</form>

<div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" id="addOrganizationID" type="button" ng-click="addOrganization()"
						>
                 确定
                </button>
            </div>
        </div>
</div>
</div>
