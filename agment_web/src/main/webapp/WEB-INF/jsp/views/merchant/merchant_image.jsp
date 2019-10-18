<%--
  Created by IntelliJ IDEA.
  User: hul
  Date: 2019/5/18
  Time: 14:34
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
    .img-size {
        height: 150px;
        width: 200px;
        !important;
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
        width: 150px;
        height: 200px;
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
    .merchant-rate{
        width: 700px;
        height: 550px;
    }
    .modal-content{
        width: 700px;
    }
    .rate-tr-width {
        height: 60px;
    }
    .form-horizontal {
        overflow: auto;
        height: 430px;
        margin-bottom: 10px;
    }
</style>
<%--<div ng-show="goType == 0 || goType == 1 ">--%>
<div class="merchant-rate">
    <div class="modal-header"  >
        <button class="close" ng-click="cancel()">
            <span aria-hidden="true">×</span>
        </button>
        <h3 class="modal-title" id="modal-title">代理商证件照</h3>
    </div>
    <div class="modal-body" id="modal-body"  >
        <div class="form-group  m-t">
            <label class="col-sm-3 control-label p-w-xs"><span class="text-danger"></span>身份证照片：</label>
            <div class="col-sm-8">

                <a ng-repeat="x in certificateImgUrl"
                   href="/agentMerchantInfo/certificateImg?certificate={{x}}" data-gallery="">
                    <img ng-src="{{'/agentMerchantInfo/certificateImg?certificate='+x}}"
                         style=" height: 180px; width: 300px;">
                </a>
            </div>
        </div>
    </div>
</div>
