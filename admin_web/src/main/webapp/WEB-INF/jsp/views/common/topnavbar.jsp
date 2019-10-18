<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/5
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<toaster-container></toaster-container>
<div class="row border-bottom">
    <nav class="navbar navbar-fixed-top animated fadeInDown" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <span minimaliza-sidebar></span>
            <%--<form role="search" class="navbar-form-custom" method="post" action="views/search_results.html">
                <div class="form-group">
                    <input type="text" placeholder="{{ 'SEARCH' | translate }}" class="form-control" name="top-search"
                           id="top-search">
                </div>
            </form>--%>
        </div>
        <ul class="nav navbar-top-links navbar-right">
            <li uib-dropdown>
                <a class="count-info" href uib-dropdown-toggle>
                    {{ 'LANGUAGE' | translate }}
                </a>
                <ul uib-dropdown-menu class="animated fadeInRight m-t-xs" ng-controller="translateCtrl">
                    <li><a ng-click="changeLanguage('zh_CN')">简中</a></li>
                    <li><a ng-click="changeLanguage('en')">English</a></li>
                </ul>
            </li>
            <li>
                <a href="/logout">
                    <i class="fa fa-sign-out"></i> Log out
                </a>
            </li>
        </ul>

    </nav>
</div>
