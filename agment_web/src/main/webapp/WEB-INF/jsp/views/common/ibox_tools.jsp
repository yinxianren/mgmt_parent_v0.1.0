<%--
  Created by IntelliJ IDEA.
  User: 陈俊雄
  Date: 2018/2/6
  Time: 10:13
--%>
<div class="ibox-tools" uib-dropdown>
    <a ng-click="showhide()"> <i class="fa fa-chevron-up"></i></a>
    <a href uib-dropdown-toggle>
        <i class="fa fa-wrench"></i>
    </a>
    <ul uib-dropdown-menu>
        <li><a href>Config option 1</a>
        </li>
        <li><a href>Config option 2</a>
        </li>
    </ul>
    <a ng-click="closebox()"><i class="fa fa-times"></i></a>
</div>