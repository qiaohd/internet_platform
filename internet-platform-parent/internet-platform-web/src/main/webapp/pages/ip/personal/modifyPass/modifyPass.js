define([ 'jquery', 'knockout','text!./modifyPass.html','bootstrap', 'uui', 'director', 'tree', 'grid','css!./modifyPass.css'],
    function($, ko, template) {
        var viewModelPass = {
            data : ko.observable({}),
            oldPassword: ko.observable(""),
            password : ko.observable(""),
            rePassword : ko.observable(""),
            oldPassError:ko.observable(""),
            passError : ko.observable(""),
            repassError : ko.observable("")
        };
        var oFlag = false;
        var pFlag = false;
        var rFlag = false;
        var ctx = $("#ctx").val();
        //原密码
        viewModelPass.checkOldPassword = function(){
        	var oldPasswordTrim = $.trim(viewModelPass.oldPassword());
	       	 if(oldPasswordTrim=="" || oldPasswordTrim==null || oldPasswordTrim==undefined){
	       		 viewModelPass.oldPassError("原密码不能为空");
	       		 oFlag = false;
	       		 return;
	       	 }else{
	       		viewModelPass.oldPassError("");
	       		var oldPasswordTrim = $.trim(viewModelPass.oldPassword());
	       		var ylUserId = $("#getUserId").val();
        		var ylHirerId = $("#getHirerId").val();
        		var jsonInfo={
        				"password2":oldPasswordTrim,
        				'userId':ylUserId,
        				'hirerId':ylHirerId
        			};
        		$.ajax({
        			type: "POST",
        			url: ctx+"/userset/checkoriginpass",
        			async:false,
        			data:jsonInfo,
        			success: function(data){
        				console.log(data)
        				if(data.result=="success"){
        					viewModelPass.oldPassError("");
        					oFlag=true;
        				}else if(data.result =="fail"){
        					viewModelPass.oldPassError(data.reason);
        					oFlag=false;
        					return;
        				}else{
        					viewModelPass.oldPassError(data.other);
        					oFlag=false;
        					return;
        				}
        			}
        		});
	       	 }
        };
        //校验密码
        viewModelPass.checkpassword = function(){
        	var passwordTrim = $.trim(viewModelPass.password());
        	 if(passwordTrim=="" || passwordTrim==null || passwordTrim==undefined){
        		 viewModelPass.passError("密码不能为空");
        		 pFlag = false;
        		 return;
        	 }else if(!(passwordTrim.length > 7 && passwordTrim.length < 21)){
        		 viewModelPass.passError("密码的长度在8~20之间");
        		 pFlag = false;
        		 return;
        	 }else {
        		 viewModelPass.passError("");
        		 pFlag = true;
        	 }
        };
        //校验重复密码
        viewModelPass.checkRePassword = function(){
        	var passwordTrim = $.trim(viewModelPass.password());
        	var rePasswordTrim = $.trim(viewModelPass.rePassword());
        	 if(rePasswordTrim=="" ||rePasswordTrim==null || rePasswordTrim==undefined){
        		 viewModelPass.repassError("重复密码不能为空");
        		 rFlag = false;
        		 return;
        	 }else if(passwordTrim!=rePasswordTrim){
        		 viewModelPass.repassError("两次密码不一致");
        		 rFlag = false;
        		 return;
        	 }else {
        		 viewModelPass.repassError("");
        		 rFlag = true;
        	 }
        };
        //获取登录人ID
        viewModelPass.getLoginId=function(){
     	   $.ajax({
    			type: "GET",
    			url: ctx+"/userset/getLoginId",
    			async:false,
    			success: function(data){
    				$("#getHirerId").val(data.hirerId);
    				$("#getUserId").val(data.userId);
    			}
    		});
        };
        //提交
        viewModelPass.modifyPassword = function(){
        	viewModelPass.checkOldPassword();
        	viewModelPass.checkpassword();
        	viewModelPass.checkRePassword();
        	if(pFlag && rFlag && oFlag){
        		var passwordTrim = $.trim(viewModelPass.password());
        		var rePasswordTrim = $.trim(viewModelPass.rePassword());
        		var ylUserId = $("#getUserId").val();
        		var ylHirerId = $("#getHirerId").val();
        		var jsonInfo={
        				"newPwd":passwordTrim,
        				'ylUserID':ylUserId,
        				'ylHirerID':ylHirerId
        			};
        		$.ajax({
        			type: "GET",
        			url: ctx+"/reset/restPwd",
        			data:jsonInfo,
        			success: function(data){
        				if(data.result =="noUserId"){
        					alert("请求超时!");
        					return false;
        				}else if(data.result =="fail"){
        					alert("服务器忙，密码重置失败!");
        					return false;
        				}else if(data.result == "success"){
        					viewModelPass.oldPassword("");
        					viewModelPass.password("");
        					viewModelPass.rePassword("");
        					$("#save-success-new").show();
        					$("#save-success-text").text("修改成功！");
        					$("#save-success-new").fadeOut(6000);
        				}
        			}
        		});
        	}
       };
       viewModelPass.menuClick = function(){
 	  	  $(".sub-menu li a").click(function(){
	       	var href = $(this).attr('href');
	       	 window.location=href;
	            var path = this.hash.replace('#', '');
	            addRouter(path);
	       });
 	     };
 	    viewModelPass.isHirer = function(){
	    	 var getHirerId = $("#getHirerId").val();
	    	 if(getHirerId!=""){
	    		 $("#person-set").css("display","none");
	    	 }
	     };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelPass
                }
            );
            
            //获得当前登录人的id
            viewModelPass.getLoginId();
            viewModelPass.isHirer();
            viewModelPass.menuClick();
        };
        return {
            'model' : viewModelPass,
            'template' : template,
            'init' : init
        };
    }
);
