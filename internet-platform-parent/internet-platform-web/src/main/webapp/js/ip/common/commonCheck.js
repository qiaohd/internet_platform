//验证邮箱格式
function isEmail(strEmail,userid) {
	var result={};
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    if(strEmail!=""){
    	if(reg.test(strEmail)){
       	 $.ajax({
   				url:$ctx + "/organization/checkUserEmail",
   				type: "GET",
   	            dataType: "json",
   	            async: false,
   	            data:{
   	            	"userEmail":strEmail,
   	            	"userId":userid
   	            },
   	            success: function (data) {
   	            	if(data.result=="success"){
   	            		 result["success"]="";
   	            	}else if(data.result=="H_two"){
   	            		result["fail"]="与租户邮箱重复！";	
   	            	}else if(data.result=="U_two"){
   	            		result["fail"]="与员工邮箱重复！";
   	            	}else if(data.result=="current"){
   	            		result["success"]="";
   	            	}
   	            }
   			});
        }
        else{
       	 result["fail"]="邮箱格式有误！";
        }
    }else{
    	result["success"]="";
    }
     return result;   
}

//校验该手机号
function checkPhone(strCellphone,userid){
	var result={};
	var ph =/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|17[01568][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|145[0-9]{8}|149[0-9]{8})$/;		
    if(ph.test(strCellphone)){
    	$.ajax({
			url:$ctx + "/organization/checkCellphoneNo",
			type: "GET",
            dataType: "json",
            async: false,
            data:{
            	"cellphoneNo":strCellphone,
            	"userId":userid
            },
            success: function (data) {
            	if(data.result=="success"){
            		result["success"]="";
            	}else if(data.result=="H_two"){
            		result["fail"]="与租户手机号重复！";
            	}else if(data.result=="U_two"){
            		result["fail"]="与员工手机号重复！";	
            	}else if(data.result=="current"){
            		result["success"]="";
            	}
            }
		});
    }
    else{
   	 result["fail"]="手机号格式有误！";
    }
	return result;
}

//密码校验
function checkPassword(strPass){ 
	   var result={};
	   if(strPass==null || strPass=="" || strPass==undefined){
		   result["fail"]="密码不能为空！";
		}else if(strPass.indexOf(" ")!=-1){
		   result["fail"]="密码不能包含空格！";
		}else if(!(strPass.length>5 && strPass.length<21)){
		   result["fail"]="密码的长度在6~20之间！";
		}else{
			result["success"]="";
		}	
	   return result;
}

//再次密码校验
function checkRepassword(strPass,strRePass){	
	   var result={};
	   if(strRePass==null || strRePass=="" || strRePass==undefined){
		   result["fail"]="密码不能为空！";
		}else if(strPass!=repasswordstrRePass){
			result["fail"]="两次密码不一致";
		}else{
			result["success"]="";
		}
	   return result;
	}

//电话校验
function checkPhoString(str){
	var result={};
	var isPhone = /^([0-9]{3,4})?[0-9]{7,8}$/;
	if(str!=""){
		if(isPhone.test(str)){
			 result["success"]="";
		}else{
			 result["fail"]="格式不正确，如:01088888888！";
		}
	}else{
		result["success"]="";
	}
	return result;	
}
