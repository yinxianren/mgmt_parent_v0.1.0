<%--
    author:xuzm
    date:20180419
    
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link type="text/css" rel="stylesheet" href="css/common/common.css"/>

<div class="modal-header">
    <button class="close" ng-click="back()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">常量组别编辑</h3>
</div>
<div class="modal-body" id="modal-body" >
		 <form id="sysgroupObjForm" name="sysgroupObjForm" class="form-horizontal" >
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>组别名称</label>
			    <div class="col-sm-8" >
			    <input type="text" id="name" name="name" tip="1" value="{{sysgroup.name}}"  ng-model="tempgroupname" class="form-control" placeholder="必填/唯一"
					   required="required"
					   ng-blur="groupNameBlur($event,tempgroupname)"
				/>

			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>常量组编码</label>
			    <div class="col-sm-8" >
			    <input type="text" id="temp" name="temp" tip="2"  disabled="true"  value="{{sysgroup.code}}" class="form-control" placeholder="必填" required="required"  />
                    <input type="hidden" id="code" name="code" value="{{sysgroup.code}}" />
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>类型</label>
			    <div class="col-sm-8" style="padding-top: 7px" >
                    <input type="radio" id="model" name="model"  style="margin: 0px"    ng-value="1" ng-checked="tempmodel==1">系统</input>
                    <input type="radio" id="model" name="model"  style="margin: 0px"   ng-value="2" ng-checked="tempmodel==2">商户</input>
                    <input type="radio" id="model" name="model"  style="margin: 0px"   ng-value="3" ng-checked="tempmodel==3">代理商</input>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>系统常量</label>
			    <div class="col-sm-8" style="padding-top: 7px" >
                    <input type="radio" id="system" name="system"  style="margin: 0px"    ng-value="1" ng-checked="tempsystem==1">是</input>
                    <input type="radio" id="system" name="system"  style="margin: 0px"  ng-value="0" ng-checked="tempsystem==0">否</input>
			    </div>
			  </div>		
		</form>
	    <div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn"  type="button"  ng-click="updateSysgroup()"
						ng-disabled="groupNameValid"
				>
                                                                        确定
                </button>
            </div>
        </div>
        </div>
</div>
