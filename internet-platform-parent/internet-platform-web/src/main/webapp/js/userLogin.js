$(function() {
	$('.navbar-static-top #top-nav').hide();
	$('.navbar-static-top #top-login').show();
	$("#checkLoginInfoDiv").hide();
	$("#checkLoginPwdDiv").hide();
	$("#checkLoginImageDiv").hide();
	$("#checkMailDiv").hide();
	//键盘回车执行登录
	$(document).keydown(function(e){
		var evt = window.event ? window.event : e ;
		if(evt.keyCode === 13){
			doLogin();
		}
	});
});
function clearAccount() {
	$("#email").val("")
}
function clearPassword() {
	$("#password").val("");
}
function doLogin() {
	$("#checkLoginImageDiv").hide();
	var plainPassword = $("#password").val();
	if (plainPassword.indexOf("_encrypted") < 0) {
		var key = RSAUtils.getKeyPair(exponent, '', modulus);
		var encryptedPwd = RSAUtils.encryptedString(key, plainPassword);
		$("#password").val(encryptedPwd + "_encrypted");
	}
	var username=$("#email").val();
	var imagecode = $("#code").val();
	var flag=true;
	if(plainPassword==null || plainPassword=="" || plainPassword==undefined){
		$("#loginPwdCheckMeg").html("密码不能为空");
		  $("#checkLoginPwdDiv").show();
		  $("#password").val("");
		  flag=false;
	}
	if(username==null || username=="" || username==undefined){
		$("#loginInfoCheckMeg").html("手机号、用户名、邮箱不能为空");
		  $("#checkLoginInfoDiv").show();
		  flag=false;
	}
	if(isAuthcodeShow=="true"){
		if(imagecode==null || imagecode=="" || imagecode==undefined){
			$("#loginLoginImageCheckMeg").html("验证码不能为空");
			  $("#checkLoginImageDiv").show();
			  flag=false;
		}
	}
	var em = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	var ph = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
	var now=new Date();
	if(ph.test(username)){//手机
		$("#uformlogin").attr("action",path+"/login/uformLogin?t="+now.getTime());
		if(!flag){
			return false;
		}else{
			$("#uformlogin").submit();
			isChecked();
		}
	}
	if(em.test(username)){//邮箱
		$("#uformlogin").attr("action",path+"/login/uformLogin?t="+now.getTime());
		if(!flag){
			return false;
		}else{
			$("#uformlogin").submit();
			isChecked();
		}
	}
	if(!ph.test(username) && !em.test(username)){
		if(!flag){
			return false;
		}else{
			$("#uformlogin").submit();
			isChecked();
		}
	}
}
function isChecked() {
	var checkClass = $("#check").attr("class");
	var is_checked = 'false';
	if(checkClass == "icon-check icon-checked"){
		is_checked = "true";
	}
	$.ajax({
		url: code_path + '/login/uformLogin',
		type: 'POST',
		dataType: 'json',
		contentType: 'application/json',
		// data: JSON.stringify({
		data: JSON.stringify({
			checkedFlag: is_checked
		}),
		success: function (data) {}
	})
}
function changeImage(){
	 var current_time = new Date();
	$("#captchaImage").attr("src",path+"/login/getImage?t=" + current_time.getTime());
}
function createXHR() {
	var xhr;
	try {
		xhr = new ActiveXObject("Msxm12.XMLHTTP");
	} catch (e) {
		try {
			xhr = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (E) {
			xhr = false;
		}
	}
	if (!(xhr && typeof XMLHttpRequest != 'undefined')) {
		xhr = new XMLHttpRequest();
	}
	return xhr;
}
function handleRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText == "false"){
				$("#loginLoginImageCheckMeg").html("验证码输入有误");
				$("#checkLoginImageDiv").show();
			} else {
				$("#checkLoginImageDiv").hide();
			}
		}
	}
}
//验证码校验
function checkImage(obj) {
	$("#checkLoginImageDiv").hide();
	var imagecode = $(obj).val();
	if(""==imagecode){
		$("#loginLoginImageCheckMeg").html("验证码不能为空");
		$("#checkLoginImageDiv").show();
		return;
	}
	req = createXHR();
	req.open("post",path+"/login/checkImage");// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = handleRequest;
	req.send("imageCode=" + imagecode);
}
function cleanTip8(){
	$("#checkLoginImageDiv").hide();
}
//用户登录用户名、电话、邮箱校验
function checkLongInfo(obj){
	var username=$("#email").val();
	if(""==username){
		$("#loginInfoCheckMeg").html("手机号、用户名、邮箱不能为空");
		$("#checkLoginInfoDiv").show();
		return;
	}
	// var em = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	// var ph = /^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/;
	// if(!ph.test(username) || !em.test(username)){//手机
	// 	// if(username.indexOf("@") == -1){
	// 		$("#loginInfoCheckMeg").html("手机或邮箱的格式输入有误");
	// 		$("#checkLoginInfoDiv").show();
	// 	// }
    //
	// }
}
function cleanTip6(){
	$("#checkLoginInfoDiv").hide();
}
//用户登录的密码校验
function loginPwdCheck(obj) {
	//$("#passwordtips").addClass("ng-hide");
	var password = $("#password").val();
	if ("" == password) {
		$("#loginPwdCheckMeg").html("密码不能为空");
		$("#checkLoginPwdDiv").show();
		return;
	}
	if (password == null || password == "" || password == undefined) {
		$("#loginPwdCheckMeg").html("密码不能为空");
		$("#checkLoginPwdDiv").show();
	} else if (password.indexOf(" ") != -1) {
		$("#loginPwdCheckMeg").html("密码不能包含空格");
		$("#checkLoginPwdDiv").show();
	} else if (!(password.length > 7 && password.length < 21)) {
		$("#loginPwdCheckMeg").html("密码的长度在8~20之间");
		$("#checkLoginPwdDiv").show();
	} else {};
}
function cleanTip7(){
	$("#checkLoginPwdDiv").hide();
}





