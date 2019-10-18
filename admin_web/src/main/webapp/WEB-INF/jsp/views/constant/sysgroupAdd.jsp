<%--
    author:xuzm
    date:20180419
    
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="back()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">常量组别添加</h3>
</div>
<div class="modal-body" id="modal-body"  >
		 <form id="sysGroupObjForm" name="sysGroupObjForm" class="form-horizontal">
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label"><font color="red">*</font>组别名称:</label>
			    <div class="col-sm-8" >
			     <input  class="form-control b-r-sm" type="text" id="name" name="name" tip="1"   ng-model="sysGroup.name" class="form-control"
						placeholder="必填/唯一" required="required"
						 ng-blur="groupNameBlur($event,sysGroup.name)"
				 />
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>常量组编码:</label>
			    <div class="col-sm-8" >
			     <input type="text" id="code" name="code" ng-model="sysGroup.code" tip="2" class="form-control" placeholder="必填/唯一"
						ng-blur="groupCodeBlur($event,sysGroup.code)"
						required="required" />
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>类型:</label>
			    <div class="col-sm-8" style="padding-top: 7px">
					<input type="radio" id="model" name="model"  style="margin: 0px"  ng-value="1" ng-checked="true">系统</input>
					<input type="radio" id="model" name="model"  style="margin: 0px" ng-value="2" >商户</input>
					<input type="radio" id="model" name="model" style="margin: 0px"  ng-value="3" >代理商</input>
			   </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>系统常量:</label>
			    <div class="col-sm-8" style="padding-top: 7px">
					<input type="radio" id="system" name="system" style="margin: 0px"    ng-value="1" ng-checked="true">是</input>
					<input type="radio" id="system" name="system" style="margin: 0px"  ng-value="0" >否</input>
			    </div>
			  </div>		
		</form>

<div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn" id="addSysGroupID" type="button" ng-click="addSysGroup()"
						ng-disabled="nextDisabled(sysGroupObjForm)">
                 确定
                </button>
            </div>
        </div>
</div>
</div>
