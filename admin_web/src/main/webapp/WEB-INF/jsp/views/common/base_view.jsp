<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/25
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Wrapper-->
<div id="wrapper">
    <!-- Navigation -->
    <div ng-include="'views/common/navigation'"></div>

    <!-- Page wraper -->
    <!-- ng-class with current state name give you the ability to extended customization your view -->
    <div id="page-wrapper" class="gray-bg {{$state.current.name}}">

        <!-- Page wrapper -->
        <div ng-include="'views/common/topnavbar'"></div>

        <!-- Main view  -->
        <div ui-view></div>

        <!-- Footer -->
        <%--<div ng-include="'views/common/footer'"></div>--%>

    </div>
    <!-- End page wrapper-->
</div>
<!-- End wrapper-->