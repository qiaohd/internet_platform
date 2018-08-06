$(document).ready(function(){
  //文本框获得焦点，边框颜色变蓝
  $("input:text").focus(function(){
	  $(this).removeClass();
	  $(this).addClass("inputwrap focus");
	  $(this).next("i").remove();
	 });
//文本框失去焦点，边框颜色变灰
  $("input:text").blur(function(){
	  $(this).removeClass("focus");
	  });
  //密码框获得焦点，边框颜色变蓝
  $("input:password").focus(function(){
	  $(this).removeClass();
	  $(this).addClass("inputwrap focus");
	  });
//密码框失去焦点，边框颜色变灰
  $("input:password").blur(function(){
	  $(this).removeClass("focus");
	  });  
});
var ok1=false;
var ok2=false;
var ok3=false;
var ok4=false;

//校验该手机号
function checkPhone(){
	var phoneNo = $("#mobilephone");
	var ph = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
	var phoneNo_value=$("#mobilephone").val();
	if(phoneNo_value==""){
		phoneNo.addClass("errorfocus");
		$("#PhoneCheckMeg").show().html("手机号不能为空！");
	}else if(!ph.test(phoneNo_value)){
		phoneNo.addClass("errorfocus");
		$("#PhoneCheckMeg").show().html("手机号输入的格式不对！");
	}else{
		$("#PhoneCheckMeg").hide();
	}
}

//获得手机验证码

function getPhoneCode(obj){
	wait=60;
	//clearTimeout(timer);
	checkPhone();
	 if (!$("#get_phoneCode").hasClass('getauth ng-binding regetauth')) {
		//begin_iuap_请求后台生成验证码_
		 var tran_mobile=$("#mobilephone").val();
		 var jsonInfo={"phoneNo":tran_mobile};
			 $.ajax({
				   type: "GET",
				   url: "/ip_hbase/login/getPhoneCode",
				   data:jsonInfo,
				   success: function(data){
					   if(data.result =="noExist"){
						   $("#PhoneCheckMeg").show().html("该手机号不存在！");
					   }else if(data.result=="success"){
						   $("#get_phoneCode").text("验证码获取成功");
						   $("#PhoneCheckMeg").hide();
						   countdown();
                           ok1 = true;
					   }else{
						  alert("服务器忙，请稍后...");
					   }
				   }
				});
	 }
	//end_iuap_请求后台生成验证码_
}

function countdown() {
    var time = 60;
    var timer;
    $("#get_phoneCode").removeClass('getauth ng-binding effect').addClass('getauth ng-binding regetauth');
   
    timer = setInterval(function () {
        time--;
        $("#get_phoneCode").text(time + 's后重新获取');
        if (time == 0) {
            clearInterval(timer);
            $("#get_phoneCode").text("重新获取验证码");
            $("#get_phoneCode").removeClass('getauth ng-binding regetauth').addClass('getauth ng-binding effect');
            
        }
    }, 1000);
}

//手机验证码校验
function phoneActiveCode(obj){
	var auth_code=$("#authCode").val();
	if(auth_code==null || auth_code=="" || auth_code==undefined){
		  $("#authCode").addClass("errorfocus");
		  $("#phoneImagCheckMeg").show().html("验证码不能为空");
	}else{
		var auth_code={"auth_code":auth_code};
		$.ajax({
			   type: "GET",
			   url: "/ip_hbase/login/checkAuthCode",
			   data:auth_code,
			   success: function(data){
				   if(data=="false"){
					   $("#authCode").addClass("errorfocus");
					   $("#phoneImagCheckMeg").show().html("手机验证码不正确");
				   }else{
					   $("#authCode").parent().append("<i class='righticon'></i>");
					   $("#phoneImagCheckMeg").hide();
					   ok2 = true;
				   }
			   }
			});
	}
}

function nextStep(){
	checkPhone();
	phoneActiveCode();
	if(!ok1 || !ok2){
		return false;
	}else{
		$.ajax({
			   type: "GET",
			   url: "/ip_hbase/login/proResetPwd",
			   success: function(data){
				   if(data.result=="fail"){
					   alert("请求超时");
				   }else{
					   $("#LoginName").html(data.userLoginName);
					   $("#password").attr("value","");
					   $("#re-password").attr("value","");
					   $("#Step1").css("display","none");
					   $("#Step2").css("display","block");
				   }
			   }
			});
	}
}

//密码校验
function checkPassword(){
	 var password =$("#password");
	 var password_value=password.val();
	 
	   if(password_value==null || password_value=="" || password_value==undefined){
		   password.addClass("errorfocus");
		 $("#pwdCheckMeg").show().html("密码不能为空");
		}else if(password_value.indexOf(" ")!=-1){
			password.addClass("errorfocus");
			 $("#pwdCheckMeg").show().html("密码不能包含空格");
		}else if(!(password_value.length>5 && password_value.length<21)){
			password.addClass("errorfocus");
			$("#pwdCheckMeg").show().html("密码的长度在6~20之间");
		}else{
			$("#password").removeClass("focus").addClass("hasContent");
			password.parent().append("<i class='righticon'></i>");
			$("#pwdCheckMeg").hide();
			ok3 =true;
		}
	
	}
//再次密码校验
function checkRepassword(){
	 var repassword = $("#re-password");
	 var password=($("#password").val());
	 var repassword_value=repassword.val();
	 
	   if(repassword_value==null || repassword_value=="" || repassword_value==undefined){
		   repassword.addClass("errorfocus");
		 $("#re-pwdCheckMeg").show().html("确认密码不能为空");
		}else if(password!=repassword_value){
			repassword.addClass("errorfocus");
			$("#re-pwdCheckMeg").show().html("两次密码不一致");
		}else{
			$("#re-password").removeClass("focus").addClass("hasContent");
			repassword.parent().append("<i class='righticon'></i>");
			$("#re-pwdCheckMeg").hide();
			ok4 = true;
		}
	
	}

function resetPassword(){
	checkPassword();
	checkRepassword();

	if(!ok3 || !ok4){
		return false;
	}else{
		var password =$("#password").val();
		var jsonInfo={"newPwd":password};
		 $.ajax({
			   type: "GET",
			   url: "/ip_hbase/login/restPwd",
			   data:jsonInfo,
			   success: function(data){
				   if(data.result =="noUserId"){
					   alert("请求超时!"); 
					   return false;
				   }else if(data.result =="fail"){
					   alert("服务器忙，密码重置失败!"); 
					   return false;
				   }else{
					   $("#Step2").css("display","none");
					   $("#Step3").css("display","block");
				   }
			   }
			});
		
	}
}

function editPassword(){
	checkPassword();
	checkRepassword();

	if(!ok3 || !ok4){
		return false;
	}else{
		var password =$("#password").val();
		var jsonInfo={"newPwd":password};
		$.ajax({
			type: "GET",
			url: "/${ctx}/reset/restPwd",
			data:jsonInfo,
			success: function(data){
				if(data.result =="noUserId"){
					alert("请求超时!");
					return false;
				}else if(data.result =="fail"){
					alert("服务器忙，密码重置失败!");
					return false;
				}else{
					$("#Step2").css("display","none");
					$("#Step3").css("display","block");
				}
			}
		});

	}
}