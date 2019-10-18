<%--
  author: xuzm
  Date: 2018/4/19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<link type="text/css" rel="stylesheet" href="css/constant/constant.css"/>
<link type="text/css" rel="stylesheet" href="css/common/common.css"/>
<script>


</script>
<!-- Main view -->
<div class="animated fadeInRight">

    <div class="wrapper wrapper-content ng-scope" ng-controller="refreshCacheCtrl" >
        <div class="row">
            <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>刷新缓存</h5>
                    </div>
                    <div class="ibox-content ">
                        <button class="btn btn-sm btn-w-m btn-primary pull-left" ng-click="refreshCache()">刷新缓存</button><br>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>