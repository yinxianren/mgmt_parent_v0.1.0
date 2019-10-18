<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/5
  Time: 21:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse animated fadeInLeft">
        <ul side-navigation class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="profile-element" uib-dropdown>
                    <h2><strong style="color: #b7c2ce">后台管理中心</strong></h2>
                </div>
                <div class="logo-element">
                    IN+
                </div>
            </li>
            <li ui-sref-active="active">
                <a ui-sref="base_view.home.home_page">
                    <i class="fa fa-home"></i>
                    <span class="nav-label">{{ 'HOME' | translate }}</span>
                </a>
            </li>
            <li ng-repeat="x in menuList" ng-class="{active: $state.includes(x.stateName)}">
                <a href="">
                    <i class="{{x.iconFont}}"></i>
                    <span class="nav-label">{{x.name | replace : menuRegexp : '' | translate}}</span>
                    <span class="fa arrow"></span>
                </a>
                <ul class="nav nav-second-level collapse" ng-class="{in: $state.includes(x.stateName)}">
                    <li ui-sref-active="active" ng-repeat="y in x.submenu">
                        <a ui-sref="{{y.stateName}}"  ng-click="reloadState(y.stateName)"

                        >{{y.name | replace : menuRegexp : '' | translate}}</a>

                    </li>
                </ul>
            </li>
        </ul>
    </div>
</nav>