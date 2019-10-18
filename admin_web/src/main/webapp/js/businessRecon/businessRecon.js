$(document).ready(function () {
    $("#promptNoneID").css("display", "none");
    $(".promptError").hide();
    $('input[name="merId"]').focus(function () {
        $(".promptError").hide();
        $("#businessReconTBID").empty();
    });

    $('input[name="merId"]').blur(function () {
        var merId = $('input[name="merId"]').val();
        if (merId == "") {
            $(".promptError").show();
            $('input[name="merId"]').addClass("has-error");
        }
    });

    $("[data-toggle='popover']").popover();

    Date.prototype.Format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,                          //月份
            "d+": this.getDate(),                               //日
            "h+": this.getHours(),                              //小时
            "m+": this.getMinutes(),                            //分
            "s+": this.getSeconds(),                            //秒
            "q+": Math.floor((this.getMonth() + 3) / 3),    //季度
            "S": this.getMilliseconds()                         //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length)); //格式化年份
        for (var k in o) //循环获取上面定义的月、日、小时等，格式化对应的数据。
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    $("#querybusinessReconID").click(function () {
        var dailyOrMonth = $("#dailyOrMonth").val();
        var merId = $('input[name="merId"]').val();
        if (null == merId || merId == "" || merId == undefined) {
            $('input[name="merId"]').addClass("has-error");
            $(".promptError").show();
            return;
        }

        $(".promptError").hide();
        $('input[name="merId"]').removeClass("has-error");
        if (dailyOrMonth == "1") {
            getBusinessDailyReconData(merId);
        } else if (dailyOrMonth == "2") {
            getBusinessMonthReconData(merId);
        }
    });


    function getBusinessDailyReconData(merId) {
        var data = {};
        var reconTime = $("#reconTime").val();
        if (reconTime != "") {
            reconTime = reconTime + "-01";
        }
        data = {merId: merId, reconType: 1, reconTime: reconTime}
        $.localAjax({
            url: "/businessRecon/getBusinessReconData",
            data: JSON.stringify(data),
            dataType: "json",
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function () {
            },
            complete: function (data) {
            },
            success: function (result) {
                $("#businessReconTBID").empty();
                if (result.businessReconMoneyList.length != 0) {
                    $("[data-toggle='popover']").popover('destroy');
                    $("#promptNoneID").css("display", "none");
                    $("#businessReconTBID").append("<tbody></tbody>");
                    var j = 0;
                    var i = 0;
                    for (; j < 36; j++) {
                        var htmlTD = '';
                        if (undefined == result.businessReconMoneyList[j] || null == result.businessReconMoneyList[j]) {
                            htmlTD = '<td class="text-center col-sm-2"></td>';
                        } else {
                            var dateObjFormat1 = new Date(result.businessReconMoneyList[j].reconTime).Format("yyyy年MM月dd日");
                            var dateObjFormat2 = new Date(result.businessReconMoneyList[j].reconTime).Format("yyyy-MM-dd");
                            htmlTD = '<td id="' + merId + '_' + dateObjFormat2 + '" class="text-center col-sm-2">' + dateObjFormat1 + '</td>';
                        }
                        if (j >= 6 && j % 6 == 0) {
                            i++;
                        }
                        if (j % 6 == 0) {
                            var htmlTR = '<tr id="TR_' + i + '" ></tr>';
                            $("#businessReconTBID tbody").append(htmlTR);
                        }
                        if (i == 8) {
                            break;
                        }
                        $("#businessReconTBID tbody " + "#TR_" + i).append(htmlTD);
                    }

                    $("#businessReconTBID tbody td").each(function () {
                        $(this).click(function () {
                            var param = $(this).attr("id");
                            if (undefined != param && param != "" && param != null) {
                                var paramArray = param.split("_");
                                var merId = paramArray[0];
                                var reconTime = paramArray[1];
                                window.location.href = "/businessRecon/downloadReconData?merId=" + merId + "&reconTime=" + reconTime + "&reconType=" + 1;
                            }
                        })
                    })
                } else {
                    $("#promptNoneID").css("display", "");
                }
            }
        });
    }
    function getLocalTime(nS) {
        return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');
    }
    function getBusinessMonthReconData(merId) {
        var reconTime = $("#reconTime").val();
        if (reconTime != "") {
            // reconTime = reconTime + "-01-01";
            reconTime=getLocalTime(reconTime);
        }
        console.log(reconTime);
        var data = {merId: merId, reconType: 2, reconTime: reconTime};
        $.localAjax({
            url: "/businessRecon/getBusinessReconData",
            data: JSON.stringify(data),
            dataType: "json",
            type: "POST",
            contentType: 'application/json;charset=UTF-8',
            beforeSend: function () {
            },
            complete: function (data) {
            },
            success: function (result) {
                $("#businessReconTBID").empty();
                if (result.businessReconMoneyList.length != 0) {
                    $("[data-toggle='popover']").popover('destroy');
                    $("#promptNoneID").css("display", "none");
                    $("#businessReconTBID").append('<tbody id="' + merId + '" ></tbody>');
                    $("#businessReconTBID tbody").append("<tr></tr>");
                    $("#businessReconTBID tbody tr").append('<td class="text-center">年份</td>');
                    $("#businessReconTBID tbody tr").append('<td class="text-center" colspan="12" >月份</td>');
                    var data = result.businessReconMoneyList;
                    var year = 2018;
                    for (var i = 0; i < data.length; i++) {
                        if (i == 0) {
                            year = parseInt(new Date(data[i].reconTime).Format("yyyy"));
                        }
                        $("#businessReconTBID tbody").append('<tr id="contentID"></tr>');
                        $("#businessReconTBID tbody #contentID").append('<td id="yearID" class="text-center"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_1" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_2" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_3" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_4" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_5" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_6" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_7" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_8" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_9" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_10" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_11" class="text-center col-sm-1"></td>');
                        $("#businessReconTBID tbody #contentID").append('<td id="ID_' + year + '_12" class="text-center col-sm-1"></td>');
                        if (i == 0) {
                            $("#businessReconTBID tbody #contentID #yearID").text(year + "年");
                        }
                        var month = parseInt(new Date(data[i].reconTime).Format("MM"));
                        $("#businessReconTBID tbody #contentID #ID_" + year + "_" + month).text(month + "月");
                    }

                    $("#businessReconTBID tbody #contentID td:not(#yearID)").each(function () {
                        $(this).click(function () {
                            var merId = $(this).parent().parent().attr("id");
                            var paramId = $(this).attr("id");
                            var paramText = $(this).text();
                            var paramArr = paramId.split("_");
                            var reconTime = paramArr[1] + "-" + paramArr[2] + "-" + "01";
                            if (undefined != paramText && paramText != "" && paramText != null) {
                                window.location.href = "/businessRecon/downloadReconData?merId=" + merId + "&reconTime=" + reconTime + "&reconType=" + 2;
                            }
                        })
                    })
                } else {
                    $("#promptNoneID").css("display", "");
                }
            }
        });
    }

    function isEmpty(obj) {
        for (var name in obj) {
            return false;
        }
        return true;
    }
});