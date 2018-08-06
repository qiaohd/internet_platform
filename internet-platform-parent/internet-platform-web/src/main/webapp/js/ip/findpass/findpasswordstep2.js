	var flag=false;
	var authCode="";
$(function(){

	$("#phoneNo").text($.cookie("phoneNo"));
	var text=$("#phoneNo").text();
	$("#userPhone").text(text.substring(0,3)+"*****"+text.substring(text.length-3,text.length)+"(手机验证)");
    $("#getAuthCode").click(function(){
    	$.ajax({
    		url:path+"/login/getPhoneCode",
    		type:"GET",
    		data:{
    			"phoneNo":text
    		},
    		success:function(data){
    			console.log(data);
    			if(data.result=="success"){
    				//$("#authCode").val(data.PhoneCode);
    				authCode=data.PhoneCode;
    				flag=true;
    			}
    			if(data.result=="noExist"){
    				$("#authcodeMsg").html(data.reason);
    			}
    		}
    	});
    });

    $("#authCode").blur(function(){
    	checkAuthCode();
    });
    $("#nextStep2").click(function(){
    	checkAuthCode();
    	if(flag){
    		 window.location.href=$ctx+"/login/findPasswordStep3";
    	}
    	else{
    		$("#authCode").focus();
    	}
    });
    
    
    
});

function getPhoneCode(obj){
	wait=60;
	//clearTimeout(timer);
	 if (!$("#get_phoneCode").hasClass('getauth ng-binding regetauth')) {
		//begin_iuap_请求后台生成验证码_
		 var tran_mobile=$("#phoneNo").text();
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
		    		url:path+"/login/getPhoneCode",
		    		type:"GET",
		    		data:{
		    			"phoneNo":tran_info
		    		},
		    		success:function(data){
		    			console.log(data);
		    			if(data.result=="success"){
		    				//$("#authCode").val(data.PhoneCode);
		    				authCode=data.PhoneCode;
		    				countdown()
		    			}
		    			if(data.result=="noExist"){
		    				$("#authcodeMsg").html(data.reason);
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

//检验验证码是否填写正确
function checkAuthCode(){
	if($("#authCode").val()==""){
		$("#authcodeMsg").html("验证码不能为空！");
		flag=false;
	}else{
		if(authCode!=$("#authCode").val()){
			$("#authcodeMsg").html("验证码错误！");
			flag=false;
		}else{
			flag=true;
		}
	}
	
}
