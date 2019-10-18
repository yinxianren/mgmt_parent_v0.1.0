<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/5
  Time: 21:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html >
<html id="ng-app" ng-app="inspinia">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<link rel="icon" href="img/favicon.ico" type="image/x-icon">

<!-- Page title set in pageTitle directive -->
<title page-title>汇融支付后台管理</title>


<!-- Bootstrap -->
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">

<%-- Angular Css --%>
<link href="css/plugins/ng-table/ng-table.min.css" rel="stylesheet">

<!-- Font awesome -->
<link href="font-awesome/css/font-awesome.css" rel="stylesheet">

<!-- Main Inspinia CSS files -->
<link href="css/animate.css" rel="stylesheet">
<link id="loadBefore" href="css/style.css" rel="stylesheet">
<link href="css/plugins/sweetaltert/sweetalert.css" rel="stylesheet">
<%--<link type="text/css" rel="stylesheet" href="js/jdialog/css/jdialog.css"/>--%>

</head>

<!-- ControllerAs syntax -->
<!-- Main controller with serveral data used in Inspinia theme on diferent view -->
<body id="page-top" class="fixed-sidebar fixed-nav fixed-nav-basic {{$state.current.data.specialClass}}"
	landing-scrollspy ng-controller="mainCtrl">

	<!-- Main view  -->
	<div ui-view></div>

	<!-- jQuery and Bootstrap -->
	<script src="js/jquery/jquery-1.12.4.min.js"></script>
	<script src="js/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script src="js/bootstrap/bootstrap.min.js"></script>
	<script src="js/plugins/sweetalert/SweetAlert.min.js"></script>

	<!-- MetsiMenu -->
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>

	<!-- SlimScroll -->
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Peace JS -->
	<script src="js/plugins/pace/pace.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="js/inspinia.js"></script>
	<!-- Main Angular scripts-->
	<script src="js/angular/angular.min.js"></script>
	<script src="js/angular/angular-sanitize.min.js"></script>
	<script src="js/plugins/oclazyload/dist/ocLazyLoad.min.js"></script>
	<script src="js/angular-translate/angular-translate.min.js"></script>
	<script src="js/ui-router/angular-ui-router.min.js"></script>
	<script src="js/bootstrap/ui-bootstrap-tpls-2.5.0.min.js"></script>
	<script src="js/angular/angular-animate.min.js"></script>
	<script src="js/plugins/angular-idle/angular-idle.js"></script>
	<script src="js/plugins/ng-table/ng-table.min.js"></script>
	<script src="js/plugins/sweetalert/angular-sweetalert.min.js"></script>

	<!--
 You need to include this script on any page that has a Google Map.
 When using Google Maps on your own site you MUST signup for your own API key at:
 https://developers.google.com/maps/documentation/javascript/tutorial#api_key
 After your sign up replace the key in the URL below..
-->
	<!--<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70"></script>-->

	<!-- Anglar App Script -->
	<script src="js/app.js"></script>
	<script src="js/filter.js"></script>
	<script src="js/config.js"></script>
	<script src="js/service.js"></script>
	<script src="js/translations.js"></script>
	<script src="js/directives.js"></script>
	<script src="js/controllers.js"></script>
	<script src="js/system/mer_system.js"></script>
	<script src="js/merchant/trans.js"></script>
	<script src="js/merchant/wallet.js"></script>
	<script src="js/merchant/finance.js"></script>
	<script src="js/merchant/order.js"></script>
	<script src="js/merchant/merchant.js"></script>

</body>
</html>
