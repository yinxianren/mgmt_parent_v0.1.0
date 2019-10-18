<%--
    author:xuzm
    date:20180419
    
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<div class="modal-header">
    <button class="close" ng-click="back()">
        <span aria-hidden="true">×</span>
    </button>
    <h3 class="modal-title" id="modal-title">系统常量添加</h3>
</div>
<div class="modal-body" id="modal-body">
		 <form id="constantObjForm" name="constantObjForm" class="form-horizontal">
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label"><font color="red">*</font>名称:</label>
			    <input type="hidden" id="id" name="id" />
			    <div class="col-sm-8" >
			     <input type="text" id="name" name="name" tip="1"  ng-keyup="nameKepup()" ng-focus="nameFocus()" ng-blur="nameBlur()" ng-model="constant.name" class="form-control" placeholder="必填" required="required" />
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>值一:</label>
			    <div class="col-sm-8" >
			     <input type="text" id="firstValue" name="firstValue" tip="2"  ng-keyup="firstValueKepup()" ng-focus="firstValueFocus()" ng-blur="firstValueBlur()" class="form-control" placeholder="必填" required />
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" >值二:</label>
			    <div class="col-sm-8" >
			    <input type="text" id="secondValue" name="secondValue" tip="1" class="form-control" ng-focus="generalFocus($event)"  />
			   </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" ><font color="red">*</font>常量组别:</label>
			    <div class="col-sm-8 set-height33">
			    <select id="groupCode" name="groupCode" tip="2" class="form-control select-style" size="0" ng-change="groupCodeChange()" ng-mousedown="groupCodeMouseDown($event)" ng-blur="groupCodeBlur()" ng-model="constant.groupCode" required >
		               <option value="String" style="display:none;"></option>
		               <option value="" ng-modal="groupCodeOption" ng-mouseover="optionMouseOver($event)" ng-click="optionMouseClick($event)" ></option>
		               <option ng-repeat="item in groupList"  value="{{item.code}}" ng-modal="groupCodeOption_{{item.code}}" ng-mouseover="optionMouseOver($event)" ng-click="optionMouseClick($event)" >{{item.name}}</option>
		        </select>
		        <span id="groupCodePrompt" class="select-prompt" ><font color="#999" >必选</font></span>
		        </div>
			  </div>
			  <div class="form-group">
			    <label for="name" class="col-sm-3 control-label" >排序:</label>
			    <div class="col-sm-8" >
			    <input type="text"
					   oninput="inputRegex(this, /[^*()+\-\d]/g)"
					   id="sortValue" name="sortValue" tip="2"  class="form-control" ng-focus="sortValueFocus()" ng-blur="sortValueBlur()" placeholder="需为整数" />
			    </div>
			  </div>		
		</form>

<div class="row padding-set">
        <div class="col-sm-12">
            <div class="center"><%--|| addSortValueDisabled--%>
                <button class="btn btn-sm btn-primary general-btn" id="addConstantID" type="button" ng-click="addConstant()"
                        ng-disabled="addIsDisabled  || constantObjForm.name.$invalid || constantObjForm.firstValue.$invalid || constantObjForm.groupCode.$invalid">
                                                                        确定
                </button>
            </div>
        </div>
</div>
</div>
