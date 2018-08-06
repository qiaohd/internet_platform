// JavaScript Document
$(document).ready(function () {
    //文本框获得焦点，边框颜色变蓝
    $("input:text").focus(function () {
        $(this).parent(this).addClass("focus");
        if ($(this).val() !== "") {
            $(this).parent(this).addClass("hasContent");
        }
    });
    //文本框失去焦点，边框颜色变灰
    $("input:text").blur(function () {
        $(this).parent(this).removeClass("focus");
    });
    //密码框获得焦点，边框颜色变蓝
    $("input:password").focus(function () {
        $(this).parent(this).addClass("focus");
    });
    //密码框失去焦点，边框颜色变灰
    $("input:password").blur(function () {
        $(this).parent(this).removeClass("focus");
    });
    //多选框选中与取消
    $("#check").click(function () {
        var classname = $(this).attr("class");
        if (classname == "icon-check") {
            $(this).addClass("icon-checked");
        }
        else {
            $(this).removeClass("icon-checked");
        }
    });
    //单选框选中与取消
    $("#sex i").click(function () {
        for (i = 0; i < 2; i++) {
            $("#sex i").eq(i).removeClass("icon-radioclick");
        }
        $(this).addClass("icon-radioclick");
    });

    //校验该用户、公司是否已经注册

    var tip1 = $("#tip1").val();
    var tip2 = $("#tip2").val();
    if (tip1 == "user_error") {
        /*$("#personName").html("验证码错误");
         $("#errorName").show(500);*/
        alert("该用户已经注册");

    }
    if (tip2 == "company_error") {
        /*$("#errorName").html("用户名或者密码错误");
         $("#errorName").show(500);*/
        alert("该公司已经注册");
    }

    $("#checkpwdDiv").hide();
    $("#checkPhoneDiv").hide();
    $("#checkImageDiv").hide();
    $("#phoneCheckImageDiv").hide();
    $("#checkPersonNameDiv").hide();
    //$("#checkCompanyNameDiv").hide();
    $("#email").blur(function(){
    	$.cookie("LoginUserName",$("#email").val());
    });
    
});

//失去焦点验证登录邮箱是否符合邮箱格式
function VerifyEmail1() {
    var inputvalue = $("#email").val();
    if (inputvalue == "") {
        $("#email").parent(this).removeClass("errorfocus");
        $("#emailtip").addClass("ng-hide ").removeClass("errorfocus");
        $("#emailtip").html("");
    }
    else if (isEmail(inputvalue)) {
        $(this).parent(this).removeClass("focus");
        $("#emailtip").addClass("ng-hide ").removeClass("errorfocus");
        $("#emailtip").html("");
    }
    else {
        $("#email").parent(this).addClass("errorfocus");
        $("#emailtip").addClass("errorfocus").removeClass("ng-hide");
        $("#emailtip").html("请输入正确的邮箱");
    }
}

//验证邮箱格式
function isEmail(str) {
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    return reg.test(str);
}

//注册下拉列表
function selectlist(n) {
    var m = n;
    if (m == 0) {
        $("#regway-list-label span").html('+86');
        $("#regway-list").css("display", "none");
    }
    else {
        $("#regway-list-label span").html('邮箱');
        $("#regway-list").css("display", "none");
    }
}

//注册页面,密码框提示信息
function passwordtip() {
    $("#passwordtips").removeClass("ng-hide");
    $("#checkpwdDiv").hide();
}
function re_passwordtip() {
    $("#re-passwordtips").removeClass("ng-hide");
    $("#re-checkpwdDiv").hide();
}

function changeImage(){
 var capPath="login/getImage";
 $("#captchaImage").attr("src",capPath);
 alert($("#captchaImage").attr("src"));
 }
 


