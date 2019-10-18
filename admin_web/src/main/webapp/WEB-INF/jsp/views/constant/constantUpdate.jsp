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
    <h3 class="modal-title" id="modal-title">系统常量编辑</h3>
</div>
<div class="modal-body" id="modal-body">
		 <form id="constantObjForm" name="constantObjForm" class="form-horizontal" >
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>名称</label>
			    <input type="hidden" id="id" name="id" value="{{constant.id}}" />
			    <div class="col-sm-8" >
			    <input type="text" id="name" name="name" tip="1" value="{{constant.name}}" ng-keyup="nameKepup()" ng-focus="nameFocus()" ng-blur="nameBlur()" ng-model="constant.name" class="form-control" placeholder="必填" required="required" />
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>值一</label>
			    <div class="col-sm-8" >
			    <input type="text" id="firstValue" name="firstValue" tip="2" value="{{constant.firstValue}}" ng-keyup="firstValueKepup()" ng-focus="firstValueFocus()" ng-blur="firstValueBlur()" class="form-control" placeholder="必填" required />
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" >值二</label>
			    <div class="col-sm-8" >
			    <input type="text" id="secondValue" name="secondValue" tip="1" value="{{constant.secondValue}}" ng-focus="generalFocus()" class="form-control" placeholder="必填" />
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>常量组别:</label>
			    <div class="col-sm-8 set-height33" >


			    <select id="groupCode" name="groupCode" tip="2" size="0" class="form-control select-style" ng-change="groupCodeChange()" ng-mousedown="groupCodeMouseDown($event)" ng-blur="groupCodeBlur()" ng-model="constant.groupCode" required >
		               <option value="String" style="display:none;"></option>
		               <option value="" ng-modal="groupCodeOption" ng-mouseover="optionMouseOver($event)" ng-click="optionMouseClick($event)" ></option>
		               <option ng-repeat="item in groupList" ng-selected="constant.groupCode == item.code" value="{{item.code}}" ng-modal="groupCodeOption_{{item.code}}" ng-mouseover="optionMouseOver($event)" ng-click="optionMouseClick($event)" >{{item.name}}</option>
		        </select>


		        <span id="groupCodePrompt" class="no-select-prompt" ><font color="#999" >必选</font></span>
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" >排序</label>
			    <div class="col-sm-8" >
			    <input type="text"
					   id="sortValue"
					   name="sortValue"
					   tip="2"
					   oninput="inputRegex(this, /[^*()+\-\d]/g)"
					   value="{{constant.sortValue}}"
					   ng-focus="sortValueFocus()"
					   ng-blur="sortValueBlur()"
					   class="form-control"
					   placeholder="需为整数" />
			    </div>
			  </div>		
		</form>
	    <div class="row padding-set">
        <div class="col-sm-12">
            <div class="center">
                <button class="btn btn-sm btn-primary general-btn"  type="button" ng-disabled="updateIsDisabled || constantObjForm.name.$invalid || constantObjForm.firstValue.$invalid || constantObjForm.groupCode.$invalid" ng-click="updateConstant()" >
                                                                        确定
                </button>
            </div>
        </div>
        </div>
</div>
