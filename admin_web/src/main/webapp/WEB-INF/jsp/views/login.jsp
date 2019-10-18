<%--
  Created by IntelliJ IDEA.
  User: hulian
  Date: 2019/5/6
  Time: 22:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>汇融支付管理后台登录</title>
    <link rel="icon" href="/img/favicon.ico" type="image/x-icon">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="/css/animate.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/css/admin_login.css"/>

    <style>
        .label-top {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
        }
        .flexbox{
            width: 120px;
            height: 110px;
            background: #fff;
            display: flex;
            align-items: center;
        }
        .flexbox img{
            align-items: center;
        }
        .flexbox-logo1{
            /*margin-left: 361px;*/
            /* margin-left: auto;*/
        }
        .flexbox-logo2 {
            /*margin-left: auto;*/
            /*margin-left: 196px;*/
        }
        .label-for-name {
            text-align: left!important;
        }
        .form-group {
            margin-bottom: 25px;
        }
    </style>

</head>

<script src="/js/jquery/jquery-3.1.1.min.js"></script>
<script src="/js/bootstrap/bootstrap.min.js"></script>

<body style="background:#FFFFFF;">

    <div class="label-top">
        <div class="flexbox flexbox-logo1" style="margin-left: auto">
            <img src="/img/login/logo.png">
        </div>
        <div class="flexbox flexbox-logo2" style="margin-left: auto">
            <img src="/img/login/3.png" style="width: 30px;height: 30px;">&nbsp;&nbsp;&nbsp;7*24小时
        </div>

        <div class="flexbox flexbox-logo2" style="margin-left: 10%">
            <img src="/img/login/2.png" style="width: 27px;height: 31px;">&nbsp;&nbsp;&nbsp;专业化团队
        </div>

        <div class="flexbox flexbox-logo2" style="margin-left: 10%;margin-right: auto">
            <img src="/img/login/1.png" style="width: 23px;height: 29px;">&nbsp;&nbsp;&nbsp;资金安全
        </div>
    </div>
    <div style="background: url('/img/login/mer_background1.png') center;height: 679px" class="col-md-12">
        <div class="col-md-3 col-md-offset-8">
            <div class="ibox-content b-r-md" style="margin-top:140px;box-shadow: 0 0 10px #e2d3d3;border: 1px solid #f1eded;height: 340px;width: 360px;">
                <form class="form-horizontal m-t" role="form" method="post" action="/login">
                    <div><h2 class="text-center p-md" style="color:#357ebd;font-weight: 600;">后台管理系统</h2></div>
                    <div class="form-group">
                        <label for="username" class="col-sm-3 control-label label-for-name">用户名</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control"
                                   id="username"
                                   name="username"
                                   placeholder="请输入您的用户名" required/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="password" class="col-sm-3 control-label label-for-name">密码</label>
                        <div class="col-sm-9">
                            <input type="password" class="form-control"
                                   id="password"
                                   name="password"
                                   placeholder="请输入您的密码"
                                   required/>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-success col-sm-12" style="background-color: #449d44!important;">登录</button>
                </form>
            </div>
        </div>
    </div>
    <div style="clear: both">
        <h3 class="text-center p-md">Copyright 2000 - 2015 IPS.All Rights Reserved 厦门融信汇网络科技有限公司 闽ICP备17023977号</h3>
    </div>

    <div class="modal fade bs-example-modal-sm" tabindex="-1" role="dialog" aria-labelledby="warningMsg" data-toggle="modal" id="warningMsg">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="modal-title" id="gridSystemModalLabel" style="text-align:center;font-size: 24px">登录失败</div>
                </div>
                <div style="height: 20px;"></div>
                <div class="modal-body" style="font-size: 15px">
                    ${warning_msg}
                </div>
                <div style="height: 20px;"></div>

                <div class="modal-footer">
                    <center>
                        <button type="button" style="text-align:center" class="btn-sm btn-success" data-dismiss="modal">确定
                        </button>
                    </center>
                </div>
            </div>
        </div>
    </div>

</body>

</html>


<script type="text/javascript">

    function centerModals(modalId) {
        $("#" + modalId).each(function (i) {
            var $clone = $(this).clone().css('display', 'block').appendTo('body');
            var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
            top = top > 0 ? top : 0;
            $clone.remove();
            $(this).find('.modal-content').css("margin-top", top);
        });
    }
    var msg='${warning_msg}';
    if (msg) {
        $(window).on('resize', centerModals('warningMsg'));
        $('#warningMsg').modal('show');
    }
    window.onload = function () {
        if (msg) {
            $(window).on('resize', centerModals('warningMsg'));
            $('#warningMsg').modal('show');
        }
    }
</script>


