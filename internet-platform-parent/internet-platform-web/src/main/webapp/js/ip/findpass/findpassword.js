// JavaScript Document
$(document).ready(function(){

  //文本框获得焦点，边框颜色变蓝
  $("input:text").focus(function(){
	  $(this).parent(this).addClass("focus");
	  if($(this).val() !== ""){
		  $(this).parent(this).addClass("hasContent");
		  }
	  });
  //文本框失去焦点，边框颜色变灰
  $("input:text").blur(function(){
	  $(this).parent(this).removeClass("focus");
	  });
  //密码框获得焦点，边框颜色变蓝
  $("input:password").focus(function(){
	  $(this).parent(this).addClass("focus");
	  });
  //密码框失去焦点，边框颜色变灰
  $("input:password").blur(function(){
	  $(this).parent(this).removeClass("focus");
	  });
  //多选框选中与取消  
  $("#check").click(function(){
	  var classname = $(this).attr("class");
	  if(classname == "icon-check"){
		  $(this).addClass("icon-checked");
		  }
      else{
		  $(this).removeClass("icon-checked");
	  }
	  });
  //单选框选中与取消
  $("#sex i").click(function(){
	  for(i=0; i<2; i++){
		  $("#sex i").eq(i).removeClass("icon-radioclick");
		  }
	  $(this).addClass("icon-radioclick");
	  });
	  
	  $("#username").val($.cookie("LoginUserName"));
	  
	  //异步加载验证码
    $("#changeCode").click(function(){
    	var path = $("#captchaImage").attr("src");
		now = new Date();
		$("#captchaImage").attr("src",$ctx+"/login/getImage?t="+ + now.getTime());
    });
    
    //点击图片更换验证码
    $("#captchaImage").click(function(){
    	var path = $("#captchaImage").attr("src");
		now = new Date();
		$("#captchaImage").attr("src",$ctx+"/login/getImage?t="+ + now.getTime());
    });
	
   $("#nextStep").click(function(){
	  // alert(checkPhone());
	  var flag1=false;
	  var flag2=false;
		var imageCode = $("#code").val();
		if(imageCode==""){
			$("#checkInfo-back").hide();
			$("#checkCodeInfo").html("验证码不能为空!");
			flag1=false;
			//viewModel.checkCodeInfo(true);
		}else if(imageCode.length != 4 ){
			$("#checkInfo-back").hide();
			$("#checkCodeInfo").html("验证码错误!");
			flag1=false;
		}else{
			$.ajax({
				url:path+"/login/checkImage",
				type:"POST",
				async:false,
				data:{
					imageCode:imageCode
				},
				success:function(data){
					console.log(path);
					if(data=="false"){
						$("#checkInfo-back").hide();
						$("#checkCodeInfo").html("验证码错误!");
						flag1=false;
						//viewModel.checkCodeInfo(true);
					}else{
						$("#checkCodeInfo").hide();
						flag1=true;
					}
				}

			});
		}
	  //验证码
		var isTel=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|17[01568][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|145[0-9]{8}|149[0-9]{8})$/;
		var tel_value=$("#username").val();
		if(tel_value==""){
			$("#checkUserNameInfo").html("用户名不能为空！");
			flag2=false;
		}
		else{
			if(isTel.test(tel_value)==false){
				$("#checkUserNameInfo").html("手机格式不正确!");
				flag2=false;
			}else{
				$("#checkUserNameInfo").html("");
				flag2=true;
			}
		}
		if(flag1==true && flag2==true){
			 window.location.href=$ctx+"/login/findPasswordStep2";
			 $.cookie("phoneNo",tel_value);
		}
   });
   
   $("#password").blur(function(){
	   checkPassword();
   });
   $("#re-password").blur(function(){
	   checkRepassword();
   });

   function checkPassword(){
		 var password =$("#password");
		 var password_value=password.val();
		 
		   if(password_value==null || password_value=="" || password_value==undefined){
			   password.addClass("errorfocus");
			 $("#pwdCheckMeg").show().html("密码不能为空");
			   return false;
			}else if(password_value.indexOf(" ")!=-1){
				password.addClass("errorfocus");
				 $("#pwdCheckMeg").show().html("密码不能包含空格");
				 return false;
			}else if(!(password_value.length>5 && password_value.length<21)){
				password.addClass("errorfocus");
				$("#pwdCheckMeg").show().html("密码的长度在6~20之间");
				return false;
			}else{
				$("#password").removeClass("focus").addClass("hasContent");
				password.parent().append("<i class='righticon'></i>");
				$("#pwdCheckMeg").hide();
				return true;
			}
		
		}

	function checkRepassword(){
		 var repassword = $("#re-password");
		 var password=($("#password").val());
		 var repassword_value=repassword.val();
		 
		   if(repassword_value==null || repassword_value=="" || repassword_value==undefined){
			   repassword.addClass("errorfocus");
			 $("#re-pwdCheckMeg").show().html("确认密码不能为空");
			 return false;
			}else if(password!=repassword_value){
				repassword.addClass("errorfocus");
				$("#re-pwdCheckMeg").show().html("两次密码不一致");
				return false;
			}else{
				$("#re-password").removeClass("focus").addClass("hasContent");
				repassword.parent().append("<i class='righticon'></i>");
				$("#re-pwdCheckMeg").hide();
				return true;
			}
		
		}
   $("#nextStep3").click(function(){
	  var ok3=checkPassword();
	  var ok4=checkRepassword();
	   if(ok3 && ok4){
		   $.ajax({
			   url:path+"/login/restPwd",
			   type:"GET",
			   data:{
				   "newPwd":$("#password").val(),
				   "phoneNo":$.cookie("phoneNo")  
			   },
			   success:function(data){
				   if(data.result=="success"){
					   window.location.href=$ctx+"/login/findPasswordSucc";
				   }
			   }
		   });
	   }
   });
});








