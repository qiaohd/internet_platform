define([ 'jquery', 'knockout','text!./perSetting.html','bootstrap', 'uui', 'director', 'tree', 'grid','css!./perSetting.css'],
    function($, ko, template) {
        var viewModelperSetting = {
            data : ko.observable({}),
            perName : ko.observable(""),
            perSex : ko.observable(""),
            //perEmployeeNo : ko.observable(""),
            perDept : ko.observable(""),
            perDuty : ko.observable(""),
            cellphoneNo : ko.observable(""),
            cellphoneNoNo : ko.observable(""),
            editCellphoneVal : ko.observable(""),
			userEmail : ko.observable(""),
			userEmailNo : ko.observable(""),
			editUserEmailVal : ko.observable(""),
			phoneNo : ko.observable(""),
			phoneNoNo : ko.observable(""),
			editPhoneVal : ko.observable(""),
			extension : ko.observable(""),
			extensionNo:ko.observable(""),
			editExtensionVal : ko.observable(""),
			loginName : ko.observable(""),
			remark : ko.observable(""),
			remarkNo : ko.observable(""),
			editRemarkVal : ko.observable(""),
			pRole : ko.observable("")
        };
        var ctx = $("#ctx").val();
        //获取登录人ID
        viewModelperSetting.getLoginId = function(){
        	$.ajax({
    			type: "GET",
    			url: ctx +"/userset/getLoginId",
    			async:false,
    			success: function(data){
    				$("#getHirerId").val(data.hirerId);
    				$("#getUserId").val(data.userId);
    			}
    		});
        };
        //初始化
        //pho
        viewModelperSetting.noPho = function(){
     	   viewModelperSetting.phoneNoNo("添加电话号码");
     	   $("#edit-phone").hide();
        };
        viewModelperSetting.noExt = function(){
        	viewModelperSetting.extensionNo("添加分机号码");
			$("#edit-extension").hide();
        };
        viewModelperSetting.noRemark = function(){
        	viewModelperSetting.remarkNo("添加备注信息");
			$("#edit-remark").hide();
        };
        viewModelperSetting.noTel =function(){
        	viewModelperSetting.cellphoneNoNo("添加手机号码");
			$("#edit-cellphoneNo").hide();
        };
        viewModelperSetting.noEmail = function(){
        	viewModelperSetting.userEmailNo("添加");
			$("#edit-userEmail").hide();
        };
        viewModelperSetting.getUserInfo = function(){
        	var userId = $("#getUserId").val();
    		var hirerId = $("#getHirerId").val();
    		var jsonInfo={
    				'userId':userId,
    				'hirerId':hirerId
    			};
    		$.ajax({
    			type: "GET",
    			url: ctx +"/userset/showusersetinfo",
    			data:jsonInfo,
    			success: function(data){
    				console.log(data);
    				viewModelperSetting.perName(data.back_user.userName);
    				viewModelperSetting.perSex(data.back_user.userSex);
    				if(data.back_user.userSex=="1"){
    					viewModelperSetting.perSex("男");
    				}else if(data.back_user.userSex=="0"){
    					viewModelperSetting.perSex("女");
    				}
    				//viewModelperSetting.perEmployeeNo(data.back_user.employeeNo);
    				viewModelperSetting.perDept(data.no_p_co[1]);
    				viewModelperSetting.perDuty(data.no_p_role.roleName);
    				if(data.back_user.phoneNo==""){
    					viewModelperSetting.noPho();
    				}else{
    					viewModelperSetting.phoneNo(data.back_user.phoneNo);
    					viewModelperSetting.phoneNoNo("");
    					$("#edit-phone").show();
    				}
    				if(data.back_user.extension==""){
    					viewModelperSetting.noExt();
    				}else{
    					viewModelperSetting.extension(data.back_user.extension);
    					viewModelperSetting.extensionNo("");
    					$("#edit-extension").show();
    				}
    				if(data.back_user.remark==""){
    					viewModelperSetting.noRemark();
    				}else{
    					viewModelperSetting.remark(data.back_user.remark);
    					viewModelperSetting.remarkNo("");
    					$("#edit-remark").show();
    				}
    				viewModelperSetting.loginName(data.back_user.loginName);
    				viewModelperSetting.cellphoneNo(data.back_user.cellphoneNo);
    				if(data.back_user.cellphoneNo==""){
    					viewModelperSetting.noTel();
    				}else{
    					viewModelperSetting.cellphoneNo(data.back_user.cellphoneNo);
    					viewModelperSetting.cellphoneNoNo("");
//    					$("#edit-cellphoneNo").show();
    				}
    				//邮箱
    				if(data.back_user.userEmail==""){
    					viewModelperSetting.noEmail()
    				}else{
    					viewModelperSetting.userEmail(data.back_user.userEmail);
    					viewModelperSetting.userEmailNo("");
    					$("#edit-userEmail").show();
    				}
//    				var pLength = data.p_role.length;
//    				for(var i=0;i<=pLength-1;i++){
//    					$("#p-job").append("<span>"+data.part_co[i][1] +"-"+ data.p_role[i].roleName+"；</span>");
//    				}
    			}
    		});
    		
       };
       //手机号
       viewModelperSetting.addPhone = function(){
    	   $("#phone-cont").removeClass("hidden");
    	   //viewModelperSetting.phoneNoNo("");
       };
       viewModelperSetting.editPhone = function(){
    	   $("#phone-cont").removeClass("hidden");
    	   viewModelperSetting.editPhoneVal(viewModelperSetting.phoneNo());
       };
       //分机
       viewModelperSetting.addExtension = function(){
    	   $("#extension-cont").removeClass("hidden");
    	  // viewModelperSetting.extensionNo("");
       };
       viewModelperSetting.editExtension = function(){
    	   $("#extension-cont").removeClass("hidden");
    	   viewModelperSetting.editExtensionVal(viewModelperSetting.extension());
       };
       //remark
       viewModelperSetting.addRemark = function(){
    	   $("#remark-cont").removeClass("hidden");
    	 //  viewModelperSetting.remarkNo("");
       };
       viewModelperSetting.editRemark = function(){
    	   $("#remark-cont").removeClass("hidden");
    	   viewModelperSetting.editRemarkVal(viewModelperSetting.remark());
       };
       //手机号
       viewModelperSetting.addCellphoneNo = function(){
    	   $("#cellphoneNo-cont").removeClass("hidden");
    	   viewModelperSetting.cellphoneNoNo("");
       };
       viewModelperSetting.editCellphoneNo = function(){
    	   $("#cellphoneNo-cont").removeClass("hidden");
    	   viewModelperSetting.editCellphoneVal(viewModelperSetting.cellphoneNo());
       };
       //邮箱
       viewModelperSetting.addUserEmail = function(){
    	   $("#userEmail-cont").removeClass("hidden");
    	   //viewModelperSetting.userEmailNo("");
       };
       viewModelperSetting.editUserEmail = function(){
    	   $("#userEmail-cont").removeClass("hidden");
    	   viewModelperSetting.editUserEmailVal(viewModelperSetting.userEmail());
       };
       viewModelperSetting.cancelBtn = function(){
    	   $(".btn-cancel").click(function(){
        	   $(this).parent().addClass("hidden");
        	   $(this).next("u").text("");
        	   $(this).siblings("input").removeClass("e-line");
           });
       };
       viewModelperSetting.savePhone = function(){
    	   var userTel = viewModelperSetting.editPhoneVal();
    	   var flag = checkPhoString(userTel);
    	   if(flag["success"]==""){
    		   var userId = $("#getUserId").val();
        	   var hirerId = $("#getHirerId").val();
        	   var jsonInfo ={
        			 "userId":userId,
        			 "phoneNo":userTel,
        			// "hirerId":hirerId
        	   };
        	   $.ajax({
       			type: "POST",
       			url: ctx +"/userset/saveusersetinfo",
       			data:jsonInfo,
       			success: function(data){
       				$("#phone-cont").addClass("hidden");
       				viewModelperSetting.phoneNo(userTel);
       				viewModelperSetting.phoneNoNo("");
       				$("#edit-phone").show();
       				$("#phone-cont input").removeClass("e-line");
       				$("#phone-cont u").text("");
       				if(userTel==""){
       					viewModelperSetting.noPho();
       				}
       			}
        	   });
    	   }else{
    		   $("#phone-cont input").addClass("e-line");
    		   $("#phone-cont u").text(flag.fail);
    	   }
       };
       viewModelperSetting.saveExtension = function(){
    	   var userExt = viewModelperSetting.editExtensionVal();
    	   var userId = $("#getUserId").val();
    	   var hirerId = $("#getHirerId").val();
    	   var jsonInfo ={
    			 "userId":userId,
    			 "extension":userExt,
    			// "hirerId":hirerId
    	   };
    	   $.ajax({
   			type: "POST",
   			url: ctx +"/userset/saveusersetinfo",
   			data:jsonInfo,
   			success: function(data){
   				$("#extension-cont").addClass("hidden");
   				viewModelperSetting.extension(userExt);
   				viewModelperSetting.extensionNo("");
   				$("#edit-extension").show();
   				if(userExt==""){
   					viewModelperSetting.noExt();
   				}
   			}
    	   });
       };
       viewModelperSetting.saveRemark = function(){
    	   var userRemark = viewModelperSetting.editRemarkVal();
    	   var userId = $("#getUserId").val();
    	   var hirerId = $("#getHirerId").val();
    	   var jsonInfo ={
    			 "userId":userId,
    			 "remark":userRemark,
    			 //"hirerId":hirerId
    	   };
    	   $.ajax({
   			type: "POST",
   			url: ctx +"/userset/saveusersetinfo",
   			data:jsonInfo,
   			success: function(data){
   				$("#remark-cont").addClass("hidden");
   				viewModelperSetting.remark(userRemark);
   				viewModelperSetting.remarkNo("");
   				$("#edit-remark").show();
   				if(userRemark==""){
   					viewModelperSetting.noRemark();
   				}
   			}
    	   });
       };
       viewModelperSetting.saveTel = function(){
    	   var userTel = viewModelperSetting.editCellphoneVal();
    	   var userId = $("#getUserId").val();
    	   var hirerId = $("#getHirerId").val();
    	   var flag = checkPhone(userTel,userId);
    	   if(flag["success"]==""){
    		   var jsonInfo ={
	    			 "userId":userId,
	    			 "cellphoneNo":userTel,
	    			 //"hirerId":hirerId
	    	   };
	    	   $.ajax({
	   			type: "POST",
	   			url: ctx +"/userset/saveusersetinfo",
	   			data:jsonInfo,
	   			success: function(data){
	   				$("#cellphoneNo-cont").addClass("hidden");
	   				viewModelperSetting.cellphoneNo(userTel);
	   				$("#cellphoneNo-cont input").removeClass("e-line");
	     		    $("#cellphoneNo-cont u").text("");
	   			}
	    	   });
    	   }else{
    		   $("#cellphoneNo-cont input").addClass("e-line");
    		   $("#cellphoneNo-cont u").text(flag.fail);
    	   }
       };
       viewModelperSetting.saveEmail = function(){
    	   var userEmail = viewModelperSetting.editUserEmailVal();
    	   var userId = $("#getUserId").val();
    	   var hirerId = $("#getHirerId").val();
    	   var flag = isEmail(userEmail,userId);
    	   if(flag["success"]==""){
    		   var jsonInfo ={
	    			 "userId":userId,
	    			 "userEmail":userEmail,
	    			 //"hirerId":hirerId
	    	   };
	    	   $.ajax({
	   			type: "POST",
	   			url: ctx +"/userset/saveusersetinfo",
	   			data:jsonInfo,
	   			success: function(data){
	   				$("#userEmail-cont").addClass("hidden");
	   				viewModelperSetting.userEmail(userEmail);
	   				viewModelperSetting.userEmailNo("");
	   				$("#edit-userEmail").show();
	   				$("#userEmail-cont input").removeClass("e-line");
	     		    $("#userEmail-cont u").text("");
	   				if(userEmail==""){
	   					viewModelperSetting.noEmail();
	   				}
	   			}
	    	   });
    	   }else{
    		   $("#userEmail-cont input").addClass("e-line");
    		   $("#userEmail-cont u").text(flag.fail);
    	   }
       };
       viewModelperSetting.menuClick = function(){
    	  $(".sub-menu li a").click(function(){
	       	var href = $(this).attr('href');
	       	 window.location=href;
	            var path = this.hash.replace('#', '');
	            addRouter(path);
	       });
       };
       viewModelperSetting.isHirer = function(){
      	 var getHirerId = $("#getHirerId").val();
      	 if(getHirerId!=""){
      		 $("#person-set").css("display","none");
      		$("#person-pass").addClass("active");
      		$("#person-pass").css("background-color","rgb(232, 245, 254)");
      		
      	 }
       };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelperSetting
                }
            );
            viewModelperSetting.getLoginId();
            viewModelperSetting.isHirer();
            viewModelperSetting.getUserInfo();
            viewModelperSetting.cancelBtn();
            viewModelperSetting.menuClick();
        };
        return {
            'model' : viewModelperSetting,
            'template' : template,
            'init' : init
        };
    }
);
