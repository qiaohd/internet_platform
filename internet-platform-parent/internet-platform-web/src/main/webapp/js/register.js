/**
 * @author zhangbch
 * 
 * 完成性别的选择、注册的操作、校验
 * 
 */
$(function(){
	flag=-1;
	initDbArea();
});
regPhoneOrMail=true;
flagTel=true;
function selectlist(obj){
	if(obj==0){//手机
		if($("#mailRegister").attr("tabindex")!=undefined){//有邮箱input,准备换上手机input,开始替换属性
		   var shouji=$("<input tabindex='1' name='phoneNumber' pattern='\\d+' id='phoneNo' autocomplete='off' inputtype='number' placeholder='请输入手机号' class='padr1' type='text' onBlur='checkPhone(this)' onfocus='cleanTip1()'>")
		    $("#mailRegister").remove();
		    $(".regmobi").append(shouji);
		    $(".regway-list").hide();
		    $("#selReg").html("+86");
		}else{
			$(".regway-list").hide();
		}
		regPhoneOrMail=true;
	}else if(obj==1){//邮箱
		var youxiang=$("<input tabindex='1' name='phoneNumber' pattern='\\d+' id='mailRegister' autocomplete='off' inputtype='number' placeholder='请输入邮箱' class='padr1' type='text' onBlur='checkMail(this)' onfocus='cleanTip()'>")
		$("#phoneNo").remove();
	    $(".regmobi").append(youxiang);
	    $(".regway-list").hide();
	    $("#selReg").html("邮箱");
	    regPhoneOrMail=false;
	}
	
}

//邮箱注册校验
function checkMail(obj){
	var mail_v = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	var mailValue=$("#mailRegister").val();
	if(mailValue==""){
		$("#mailCheckMeg").html("邮箱不能为空！");
		$("#checkMailDiv").show();
		return;
	}
	if(!mail_v.test(mailValue)){
		$("#mailCheckMeg").html("邮箱输入的格式不对！");
		$("#checkMailDiv").show();
		return;
	}
	// $.ajax({
	// 	type: "POST",
	// 	url: test + "/menuShow/hideMenu",
	// 	data: jsonObject,
	// 	success: function (data) {
	// 		$("#" + id).parent().parent().remove();
	// 		menuAdd();
	// 	}
	// });
	req = createXHR();
	now=new Date();
	req.open("post",test+"/register/checkMailisExist?t="+now);// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = checkMailHandleRequest;
	req.send("mailValue=" + mailValue);
}
function checkMailHandleRequest(req) {
	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText=="false") {
				  $("#mailCheckMeg").html("该邮箱已经被注册");
				  $("#checkMailDiv").show();
				}
		}else{
			$("#checkMailDiv").hide();
		}
	}
}
function cleanTip(){
	$("#checkMailDiv").hide();
	$("#checkPhoneDiv").hide();
}


//sex
function selectOption(obj){
	
     var sex=($(obj).html()).substr(0,1);
        if(sex=="男"){ 
    	 $("#sex1").val("1");
    	 }else{
    		 $("#sex1").val("2");
    	 }
    
	
}

function checkPhoneHandleRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText=="false"){
				  $("#PhoneCheckMeg").html("该手机号已经被注册");
				  $("#checkPhoneDiv").show();
				  flagTel=false;
				  return;
				}else{
			    	flagTel=true;
			    	$("#checkPhoneDiv").hide();
			    	}
		    }
	}
}
//校验该手机号是否已经注册
function checkPhone(obj){
	
	var ph = /^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
	var phoneNoValue=$("#phoneNo").val();
	if(phoneNoValue==""){
		$("#PhoneCheckMeg").html("手机号不能为空！");
		$("#checkPhoneDiv").show();
		return;
	}
	if(!ph.test(phoneNoValue)){
		$("#PhoneCheckMeg").html("手机号输入的格式不对！");
		$("#checkPhoneDiv").show();
		return;
	}
	req = createXHR();
	now=new Date();
	req.open("post",test+"/register/checkPhoneisExist?t="+now);// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = checkPhoneHandleRequest;
	req.send("phone=" + phoneNoValue);
}
function cleanTip1(){
	$("#checkPhoneDiv").hide();
	$("#checkMailDiv").hide();
}




//注册验证码的校验
var registerCheckImageCode = false;
function registerCheckImage(obj) {

	var imagecode = $(obj).val();
	if(""==imagecode){
		$("#ImagCheckMeg").html("验证码不能为空");
		  $("#checkImageDiv").show();
		  return;
	}
	req = createXHR();
	req.open("post",test+"/login/checkImage");// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = registerCheckImageHandleRequest;
	req.send("imageCode=" + imagecode);
}
function registerCheckImageHandleRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText=="false")
				{
				  $("#ImagCheckMeg").html("验证码输入有误");
				  $("#checkImageDiv").show();
					registerCheckImageCode = false
				}
			else {
				registerCheckImageCode = true;
			}
		    }
	}
}
function cleanTip2(){
	$("#checkImageDiv").hide();
	
}


//手机验证码校验
 var checkTelCode = false;
function phoneActiveCode(obj){

	var authCode=$("#authCode").val();
	if(authCode==null || authCode=="" || authCode==undefined){
		  $("#phoneImagCheckMeg").html("手机验证码不能为空");
		  $("#phoneCheckImageDiv").show();
		  //flag=false;
	}else{
		
		var authCode=$("#authCode").val();
		var auth_code={"auth_code":authCode};
		$.ajax({
			   type: "GET",
			   url: "register/checkAuthCode",
			   data:auth_code,
			   success: function(data){
				   if(data=="false"){
					   $("#phoneImagCheckMeg").html("手机验证码不正确");
					   $("#phoneCheckImageDiv").show();
					   checkTelCode = false;
				   } else {
					   checkTelCode = true;
				   }
			   }
			});
		return checkTelCode;
	}
	
}
function cleanTip3(){
	$("#phoneCheckImageDiv").hide();
}



//密码校验
function passwordtip_hide(obj){
	
	$("#passwordtips").addClass("ng-hide");
	
	 var password=($(obj).val());
	 
	   if(password==null || password=="" || password==undefined){
		 $("#pwdCheckMeg").html("密码不能为空");
		  $("#checkpwdDiv").show();
		}else if(password.indexOf(" ")!=-1){
			 $("#pwdCheckMeg").html("密码不能包含空格");
			  $("#checkpwdDiv").show();
		}else if(!(password.length>7 && password.length<21)){
			$("#pwdCheckMeg").html("密码的长度在8~20之间");
			  $("#checkpwdDiv").show();
		}else{
			;
		}
	
	}
//再次密码校验
function re_passwordtip_hide(obj){
	
	$("#re-passwordtips").addClass("ng-hide");
	 var password=($("#password").val());
	 var repassword=($(obj).val());
	 
	   if(repassword==null || repassword=="" || repassword==undefined){
		 $("#re-pwdCheckMeg").html("确认密码不能为空");
		  $("#re-checkpwdDiv").show();
		}else if(repassword.indexOf(" ")!=-1){
			 $("#re-pwdCheckMeg").html("密码不能包含空格");
			  $("#re-checkpwdDiv").show();
		}else if(!(repassword.length>7 && repassword.length<21)){
			$("#re-pwdCheckMeg").html("密码的长度在8~20之间");
			  $("#re-checkpwdDiv").show();
		}else if(password!=repassword){
			$("#re-pwdCheckMeg").html("两次密码不一致");
			  $("#re-checkpwdDiv").show();
		}else{
			;
		}
	
	}

//姓名校验
function checkPersonNameHandleRequest() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			if (req.responseText=="false")
				{
				  $("#personNameCheckMeg").html("用户已经被注册");
				  $("#checkPersonNameDiv").show();
				}
				
		    }else{
		    	$("#checkPersonNameDiv").hide();
		    }
	}
}
function regCheckUserName(obj){

	var personName=$("#personName").val();
	if(""==personName){
		$("#personNameCheckMeg").html("姓名不能为空");
		  $("#checkPersonNameDiv").show();
		  return;
	}
	req = createXHR();
	req.open("post",test+"/register/regCheckUserName");// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = checkPersonNameHandleRequest;
	req.send("personName="+personName);
}

function cleanTip4(){
	$("#checkPersonNameDiv").hide();
}


//公司校验
//function checkCompanyNameHandleRequest() {
//	if (req.readyState == 4) {
//		if (req.status == 200) {
//			if (req.responseText=="false")
//				{
//				  $("#companyNameCheckMeg").html("用户已经被注册");
//				  $("#checkCompanyName1Div").show();
//				}
//				
//		    }else{
//		    	$("#checkCompanyName1Div").hide();
//		    }
//	}
//}
function regCheckCompanyName(obj){

	var companyName=$("#companyName").val();
	if(companyName.length<=2){
		$("#checkCompanyName1Div").show();
		$("#companyNameCheckMeg").html("单位名称不能为空且不能少于3个汉字");
		  
		  //return;
	}
//	req = createXHR();
//	req.open("post",test+"/register/regCheckUserName");// 这里为一个Check的servlet，用来接收参数，进行数据库查找处理
//	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
//	req.onreadystatechange = checkCompanyNameHandleRequest;
//	req.send("companyName="+companyName);
}
function cleanTip5(){
	$("#checkCompanyName1Div").hide();
}

//


//获得手机验证码

function getPhoneCode(obj){
	wait=60;
	//clearTimeout(timer);
	 if (!$("#get_phoneCode").hasClass('getauth ng-binding regetauth')) {
		//begin_iuap_请求后台生成验证码_
		 var tran_mobile=$("#phoneNo").val();
		 var mailRegister_info=$("#mailRegister").val();
		 var tran_info;
		 if(tran_mobile!=null && tran_mobile!="" && tran_mobile!=undefined){
			 tran_info=tran_mobile;
		 }
		 if(mailRegister_info!=null && mailRegister_info!="" && mailRegister_info!=undefined){
			 tran_info=mailRegister_info;
		 }
		 var transInfo={"tran_info":tran_info};
		 //mailRegister==null || mailRegister=="" || mailRegister==undefined
		 if((tran_info!=null && tran_info!="" && tran_info!=undefined) || (mailRegister_info!=null && mailRegister_info!="" && mailRegister_info!=undefined))
		 { 
			 $.ajax({
				   type: "GET",
				   url: "register/getMobileCode",
				   data:transInfo,
				   success: function(data){
					   if(data=="true"){
						   $("#get_phoneCode").text("验证码获取成功");
						   countdown();
					   }else{
						  alert("服务器忙，请稍后...");
					   }
				   }
				});
		 }else{
			 if(tran_info==null || tran_info==""){
				 $("#phoneImagCheckMeg").html("获得手机验证码时，手机号不能为空");
				 $("#phoneCheckImageDiv").show();
			 }else if(tran_info==null || tran_info==""){
				 $("#phoneImagCheckMeg").html("获得手机验证码时，邮箱不能为空");
				 $("#phoneCheckImageDiv").show();
			 }
		 }
	 }
	//end_iuap_请求后台生成验证码_
}

 
function time(o) {  
	
		if (wait == -1) {
			  $("#get_phoneCode").text("重新获取验证码");
			  $("#get_phoneCode").removeClass('getauth ng-binding regetauth').addClass('getauth ng-binding effect');
			  flag=-1;
			  return;      
            
        } else {   
        	$("#get_phoneCode").removeClass('getauth ng-binding effect').addClass('getauth ng-binding regetauth');
            $(o).text("(" + wait + ")秒后重新获取");  
            wait--;
            timer=setTimeout(function() {  
                time(o)  
            },  
            1000);
        }
	   
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


/*function changeImage(){
	var capPath="/login/getImage";
	var imgSrc=$("#captchaImage").attr("src");
	imgSrc=imgSrc.substr(0, imgSrc.indexOf(2,'/')); 
	
	
	$("#captchaImage").attr("src",imgSrc+capPath);
	
	alert($("#captchaImage").attr("src"));
}*/

function initDbArea() {
	var time = new Date();
	$.ajax({
		url:path+"/partition/getDataPartition",
		type:"GET",
		dataType: "json",
        data:{time: time.getTime()},
        success: function (result) {
        	 for (var i = 0; i < result.data.length; i++) {           	
        		 $("#dbArea").append("<option value='" + result.data[i].data_id +"' >"+result.data[i].host+"【"+ result.data[i].area_name + "】</option>");
             }
        }
	})
}

function startRegister(){
	var personName=$("#personName").val();// 租户名称
	
	var password=$("#password").val();// 密码
	var repassword=$("#re-password").val();//确认密码
	var companyName=$("#companyName").val();// 公司
	
	var code=$("#code").val();// 验证码
	var authCode=$("#authCode").val();// 手机验证码
	
	var choiceService = $("#check").attr("class"); //服务条款
	var flag=true;
	// checkPhoneHandleRequest();
	if(regPhoneOrMail){//手机
		var phoneNo=$("#phoneNo").val();// 手机号
		if(phoneNo==null || phoneNo=="" || phoneNo==undefined){
			$("#PhoneCheckMeg").html("手机号不能为空！");
			$("#checkPhoneDiv").show();
			flag=false;
			}
	}else{
		var mailRegister=$("#mailRegister").val();
		if(mailRegister==null || mailRegister=="" || mailRegister==undefined){
			$("#checkPhoneDiv").html("邮箱不能为空");
			$("#checkMailDiv").show();
			flag=false;
			}
	}
	
	
	if(code==null || code=="" || code==undefined){
		  $("#ImagCheckMeg").html("验证码不能为空");
		  $("#checkImageDiv").show();
		  flag=false;
		// return false;
	}else{
			$.ajax({
				url:path+"/login/checkImage",
				type:"POST",
				async:false,
				data:{
					imageCode:code
				},
				success:function(data){
					if(data=="false"){
						$("#checkCodeInfo").html("验证码错误");
						flag=false;
					}else{
						
						flag=true;
					}
				}

			});

	}
	if(authCode==null || authCode=="" || authCode==undefined){
		  $("#phoneImagCheckMeg").html("手机验证码不能为空");
		  $("#phoneCheckImageDiv").show();
		  flag=false;
		  // return false;
	}
	if(password==null || password=="" || password==undefined){
		$("#pwdCheckMeg").html("密码不能为空");
		  $("#checkpwdDiv").show();
		  flag=false;
	}
	if(repassword==null || repassword=="" || repassword==undefined ){
		$("#re-pwdCheckMeg").html("确认密码不能为空");
		$("#re-checkpwdDiv").show();
		flag=false;
	}
	if(repassword.indexOf(" ")!=-1){
		$("#re-pwdCheckMeg").html("密码不能包含空格");
		$("#re-checkpwdDiv").show();
		flag=false;
	}
	if(!(repassword.length>7 && repassword.length<21)){
		$("#re-pwdCheckMeg").html("密码的长度在8~20之间");
		$("#re-checkpwdDiv").show();
		flag=false;
	}
	if(repassword!=password){
		$("#re-pwdCheckMeg").html("两次密码不一致");
		$("#re-checkpwdDiv").show();
		flag=false;
	}
	if(personName==null || personName=="" || personName==undefined){
		$("#personNameCheckMeg").html("姓名不能为空");
		  $("#checkPersonNameDiv").show();
		  flag=false;
	}
	if(companyName==null || companyName.length<=2 || companyName==undefined){
		  $("#companyNameCheckMeg").html("公司名称不能为空且不能少于3个汉字");
		  $("#checkCompanyNameDiv").show();
		  flag=false;
	}
	if(choiceService == "icon-check"){
		$("#choiceServiceCheckMeg").html("请勾选服务条款与保密协议");
		$("#choiceService").show();
		flag=false;
	} else {
		$("#choiceService").hide();
	}
	if(!flag){
		return false;
	}else if(!flagTel){
		return false;
	}else{
		var checkTelCodeFun = phoneActiveCode();
		if(checkTelCodeFun){
			$("#formRegister").submit();
		} else {
			$("#phoneImagCheckMeg").html("手机验证码不正确");
			$("#phoneCheckImageDiv").show();
		}
	}
	//$("#formRegister").submit();
	/*if(personName==null || personName=="" || personName==undefined){
		   alert("手机号不能为空");
		   return false;
		}*/
	/*if(phoneNo==null || phoneNo=="" || phoneNo==undefined){
		   alert("租户名称不能为空");
		   return false;
		}
	
	if(password==null || password=="" || password==undefined){
		   alert("密码不能为空");
		   return false;
		}
	
	 if(companyName==null || companyName=="" || companyName==undefined){
		   alert("单位不能为空");
		   return false;
		}
	 
	  if(imgcode==null || imgcode=="" || imgcode==undefined){
			   alert("验证码不能为空");
			   return false;
		}
		
		if(phoneCode==null || phoneCode=="" || phoneCode==undefined){
			   alert("手机验证码不能为空");
			   return false;
		}*/
     
	    
	
	   // alert("开始提交 ");
		
}