<%--
    author:xuzm
    date:20180419

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="cancel()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">{{ type == 0 ? '添加机构银行' : '修改机构银行'}}</h3>
</div>
<div class="modal-body" id="modal-body"  >
		 <form id="OrganizationObjForm" name="OrganizationObjForm" class="form-horizontal">
			  <div class="form-group">
			    <label   class="col-sm-2 control-label"><font color="red">*</font>机构名称:</label>
			    <div class="col-sm-10" >
					<select class="form-control b-r-sm" id="organizationName" name="organizationId" ng-model="Organization.organizationId"
							ng-blur="statusBlur($event, Organization.organizationName)" placeholder="必填/唯一" required="required"
							ng-options="(x.organizationId) as x.organizationName for x in organizations">
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			    <label   class="col-sm-2 control-label" ><font color="red">*</font>银行名称:</label>
			    <div class="col-sm-10" >
			     <input type="text" id="remark" name="bankName" ng-model="Organization.bankName" tip="2" class="form-control" placeholder="必填/唯一"
						ng-blur="remarkBlur($event,Organization.bankName)"
						required="required" />
		        </div>
			  </div>

			 <div class="form-group">
				 <label   class="col-sm-2 control-label" ><font color="red">*</font>银行编码:</label>
				 <div class="col-sm-10" >
					 <input type="text" id="" name="bankCode" ng-model="Organization.bankCode" tip="2" class="form-control" placeholder="必填/唯一"
							ng-blur="remarkBlur($event,Organization.bankCode)"
							required="required" />
				 </div>
			 </div>

			 <div class="form-group">
				 <label   class="col-sm-2 control-label" ><font color="red">*</font>费率:</label>
				 <div class="col-sm-10" >
					 <input type="number" id="" name="bankRate" ng-model="Organization.bankRate" tip="2" class="form-control" placeholder="必填/唯一"
							ng-blur="remarkBlur($event,Organization.bankRate)"
							required="required" />
				 </div>
			 </div>

			 <div class="form-group">
				 <label   class="col-sm-2 control-label" ><font color="red">*</font>单笔限额:</label>
				 <div class="col-sm-10" >
					 <input type="number" id="" name="singleMoney" ng-model="Organization.singleMoney" tip="2" class="form-control" placeholder="必填"
							ng-blur="remarkBlur($event,Organization.bankRate)"
							required="required" />
				 </div>
			 </div>

			 <div class="form-group">
				 <label   class="col-sm-2 control-label" ><font color="red">*</font>单日限额:</label>
				 <div class="col-sm-10" >
					 <input type="number" id="" name="dayMoney" ng-model="Organization.dayMoney" tip="2" class="form-control" placeholder="必填"
							ng-blur="remarkBlur($event,Organization.bankRate)"
							required="required" />
				 </div>
			 </div>

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
		</form>

<div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" id="addOrganizationID" type="button" ng-click="addOrganizationBank()"
						>
                 确定
                </button>
            </div>
        </div>
</div>
</div>
