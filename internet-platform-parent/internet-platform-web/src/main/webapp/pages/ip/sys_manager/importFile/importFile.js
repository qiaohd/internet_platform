define([ 'jquery','knockout','text!./importFile.html','uui'],
	function($, ko, template) {
	    var viewModel = {
	    	data : ko.observable({})
	    };
	    
	    viewModel.previewFile=function(target){
	    	var fileObject=document.getElementById("uploadfile"); 
	    	var errorObject=$("#error"); 
	    	var filepath=fileObject.value; 
	    	var fileArr=filepath.split("//"); 
	    	var fileTArr=fileArr[fileArr.length-1].toLowerCase().split("."); 
	    	var filetype=fileTArr[fileTArr.length-1]; 
	    	
	    	var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
	    	var fileSize = 0;
	    	if (isIE && !fileObject.files) {
		       var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
		       var file = fileSystem.GetFile (filePath);
		       fileSize = file.Size;
		    } else {
		       fileSize = fileObject.files[0].size;
		    }
		    var size = fileSize / 1024;
	    	var filemaxsize = 1024*2;//2M 

	    	if(filetype!="xls"&&filetype!="xlsx"){
	    		fileObject.value="";
	    		alert("格式不正确");
	    	}else if(size>filemaxsize){ 
	    		fileObject.value="";
	    		alert("附件大小不能大于"+filemaxsize/1024+"M！"); 
	    	} 
	    };
	    
	    viewModel.excelUpload = function(){
	    	var fileObject=$("#uploadfile"); 
	    	var errorObject=$("#error"); 
	    	var filepath=fileObject.val(); 
//			$("#excelFileUpload").attr("action","/internet-platform-web/organization/uploadUserInfo");
//			$("#excelFileUpload").submit();
	    	$("#excelFileUpload").on('submit', function(){
	    		$.ajaxSubmit({
		    		type:"POST",
		    		url:"/${ctx}/organization/uploadUserInfo",
		    		data:{
		    			"excelFile":filepath
		    		},
		    		success:function(data){
		    			alert("上传成功");
		    		}
		    	});
	    	});
	    };
	   
	    viewModel.fileUpdate=function(){
	    	$.ajax({  
                type:"GET",  //提交方式  
                dataType:"json", //数据类型  
                url:"/${ctx}/organization/bathImportUserInfo", //请求url  
                success:function(data){ //提交成功的回调函数  
                    if(data.result=="success"){
                    	alert("上传成功");
                    	$("#stepOne").css("display","none");
                    	$("#stepLast").css("display","block");
                    }else if(data.result=="fileNotFound"){
                    	alert("文件格式不正确，请下载附件重新上传");
                    }else if(data.result=="empty_excel"){
                    	alert("此文件为空，请重新上传");
                    } else if(data.result=="fail"){
                    	alert(data.reason);
                    }else if(data.result=="data_error"){
                    	$("#stepOne").css("display","none");
                    	$("#stepTwo").css("display","block");
                    	$.ajax({
                    		type:"POST",
                    		dataType:"json",
                    		url:"/${ctx}/organization/bathImportRestUserInfo",
                    		success:function(data){
                    			console.log("333333333");
                    			console.log(data);
                    			$("#validstaff").html(data.valid);
                    			$("#invalidstaff").html(data.invalid);
                    			console.log(data.error_list);
                    			for(var i=0; i<=data.error_list.length; i++){
                    				$("#batch-table table tbody").append("<tr><td>'"+ data.error_list[i].no +"'</td><td>'"+ data.error_list[i].info +"'</td></tr>");
                    			}
                    		}
                    	});
                    } 
                }  
    		});  
	    };
	    viewModel.returnOne=function(){
	    	$("#stepTwo").css("display","none");
        	$("#stepOne").css("display","block");
	    };
	    
	    
		var init = function(){
			
		};
		
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
});