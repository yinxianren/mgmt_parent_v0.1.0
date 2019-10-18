<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/5/15
  Time: 15:40
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="clearfix">
    <div class=" p-xxs b-r-sm pull-right">
        &nbsp;&nbsp;总数：<strong>{{tableParam.total()}}</strong>
    </div>
    <div class="pull-right" style="width: 103px">
        <div class="p-h-xxs pull-left" style="width: 73px">
            &nbsp;&nbsp;显示条数：
        </div>
        <div class="pull-right" style="width: 30px">
            <input class="form-control input-table-pager b-r-sm text-center" type="number" ng-model="pageCount"
                   ng-blur="pageCountBlur(pageCount)"/>
        </div>
    </div>
    <div class="pull-right" style="width: 117px">
        <div class="p-h-xxs pull-left" style="width: 60px">
            &nbsp;&nbsp;跳转至：
        </div>
        <div class="p-h-xxs pull-right">
            &nbsp;页
        </div>
        <div class="pull-right" style="width: 40px">
            <input class="form-control input-table-pager b-r-sm text-center"
                   type="number"
                   ng-model="pageJump"
                   ng-blur="pageJumpBlur(pageJump)"/>
        </div>
    </div>
    <ul uib-pagination
        class="pull-right no-margins"
        boundary-links="true"
        direction-links="false"
        total-items="tableParam.total()"
        ng-model="currentPage"
        ng-change="pageChanged(currentPage)"
        max-size="5"
        items-per-page="itemsPerPage"
        boundary-link-numbers="true"
        first-text="&laquo;"
        last-text="&raquo;">
    </ul>
</div>