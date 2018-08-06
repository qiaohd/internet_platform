define([ 'jquery', 'knockout','text!./noticeNew.html','css!./noticeNew.css','wangEditor','jqueryForm'],
	function($, ko, template) {
		var isSubmitFile=false;
		var viewModel = {
			data : ko.observable({})
		};
		function editDetail(){
			var time = new Date();
 	    	var this_href = window.location;
 	    	var this_path = this_href.hash.replace('#', '');
 	    	var this_val= this_path.split("=")[1]; 
 	    	if(this_val!=undefined){
 	    		$.ajax({
 					url: $ctx + "/notice/getNoticeDetail",
 					type: "GET",
 					dataType: "json",
 					data: {
 						"noticeId":this_val,
 						"time": time.getTime()
 					},
 					success: function (data) {
 						$("#noticeId").val(this_val);
 						data=data.noticeDetail;
 						$("#notice-name").val(data.noticeTitle);
 						document.getElementById("editor-trigger").innerHTML=data.noticeContent;
 						if(data.attachmentUrl!=""){
 							$(".notice-content").after('<p style=" margin-left: 10%;margin-top: 10px;" id="aa">点击替换【'+data.attachmentUrl+'】</p>');
 							$(".upload-file").addClass("hide");
 						}
 						$("#aa").on("click",function(){
 							$(".upload-file").removeClass("hide");
 							$("#aa").addClass("hide");
 							$("#aa").text("");
 						});
 					},
 					error:function(){
 						u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请稍后重试！", backdrop: true});
 					}
 	    		});
 	    	}else{
 	    		
 	    	}
 	    };
 	    function clearNot(){
 	    	var editor = new wangEditor('editor-trigger');
 	    	editor.clear();
 	    	$("#notice-name").val("");
 	    	var obj = document.getElementById('upload-file');
 	    	obj.outerHTML=obj.outerHTML; 
 	    };
		function goNew(){
 	    	$("#go-new").on("click",function(){
 	    		clearNot();
 	    	});
 	    	$("#go-turn").on("click",function(){
	    		 var href = $(this).attr('href');
			     window.location=href;
			     var path = this.hash.replace('#', '');
			     addRouter(path);
	    	})
 	    };
 	    function previewNotice(){
 	    	$("#preview").on("click",function(){
 	    		$("#preview-content-modal").modal({backdrop:'static',keyboard:false});
 	    		$("#preview-content").removeClass("hide");
 	    		var previewName = $("#notice-name").val();
 	    		var triggerHtml = editor.$txt.html();
 	    		$("#new-tit").text(previewName);
 	    		$("#preview-html").html(triggerHtml);
 	    		var myDate = new Date();  
 	    	    var date = myDate.getFullYear()+"-"+(myDate.getMonth()+1)+"-"+myDate.getDate();
 	    	    $("#pubdate").text(date);
 	    	});
 	    	$("#close-preview").on("click",function(){
 	    		$("#preview-content-modal").modal("hide");
 	    		$("#preview-content").addClass("hide");
 	    		$("#preview-html").html("");
 	    		$("#new-tit").text("");
 	    	});
 	    };

 	 //获取上传内容 保存与保存发布方法
 		viewModel.getImportData = function(data1){
 			var oldFile = $("#aa").text();
 			var noticeTitle = $.trim($("#notice-name").val());
    		var noticeContent = $.trim(editor.$txt.html());
    		$("#noticeContent").val(noticeContent);
    		$("#isPublish").val(data1);
    		$("#oldFile").val(oldFile);
    		var hirerId =$("#user").attr("class");
 			var FileController = "notice/saveNotice"; // 接收上传文件的后台地址
 			var this_href = window.location;
 	    	var this_path = this_href.hash.replace('#', '');
 	    	var this_val= this_path.split("=")[1];
 	    	if(this_val!=undefined){
 	    		this_val= this_val; 
 	    	}else{
 	    		this_val="";// 文件对象
 	    	}
 	    	
 	    	var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
 	    	if (isIE && isIE<="9.0") {
 	    		if(noticeTitle!=""&&!htmlFlag&&!textFlag){
 	    			$("#form1").attr("action",$ctx+"/notice/saveNoticeForIE");
 	    			$("#form1").submit();
 	    			clearNot();
 	    		}else{
	    			if(noticeTitle==""){
	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "标题不能为空！", backdrop: true});
	        		}
	        		if(htmlFlag){
	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "文字修饰过多溢出，请重新编辑！", backdrop: true});
	        		}
	        		if(textFlag){
	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "文本超出500字，请重新编辑！", backdrop: true});
	        		}
	    		}
 		    } else {
 		    	var fileObj = document.getElementById("upload-file").files[0]; //file对应前面的input file 的ID
 		    	if(fileObj==undefined){
 	    			fileObj="";
 	    		}
 	    		if(noticeTitle!=""&&!htmlFlag&&!textFlag){
 	    			var form  = new FormData();
 	 				form.append("file", fileObj);// 文件对象
 	 				form.append("attachmentUrl",fileObj);
 	 				form.append("noticeTitle", noticeTitle);// 文件对象
 	 				form.append("noticeContent", noticeContent);// 文件对象
 	 				form.append("isPublish", data1);// 文件对象
 	 				form.append("noticeId", this_val);
 	 				form.append("oldFile", oldFile);
 	 				
 	 				// XMLHttpRequest 对象
 	 				var xhr = new XMLHttpRequest();
 	 				xhr.open("post", FileController, true);
 	 				xhr.onload = function () {
 	 					if (xhr.status == 200) {
 	 					   var data = jQuery.parseJSON(xhr.responseText); //从后台返回来的值(分别是年度和单位list以及上传的路径)
 	 					    var flag = data.result;
 	 					    if (flag == "true"){
 	 					    	if(data1=="1"){
 	 					    		$("#save-success-text").text("发布成功！");
 	 	 							$("#save-success-new").css("display","block");
 	 	 	                        $("#save-success-new").hide(3000);
 	 	 	                        clearNot();
 	 					    	}else{
 	 					    		$("#save-success-text").text("保存成功！");
 	 	 							$("#save-success-new").css("display","block");
 	 	 	                        $("#save-success-new").hide(3000);
 	 					    	}
 	 					    	return;
 	 					    }
 	 					    if (flag == "false"){
 	 					    	u.showMessageDialog({type: "info", title: "提示信息", msg: "保存失败！", backdrop: true});
 	 					    	return;
 	 					    }
 	 					} else {
 	 						u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请稍后重试！", backdrop: true});
 	 					}
 	 				};
 	 				xhr.send(form);
 	    			
 	    		}else{
 	    			if(noticeTitle==""){
 	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "标题不能为空！", backdrop: true});
 	        		}
 	        		if(htmlFlag){
 	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "文字修饰过多溢出，请重新编辑！", backdrop: true});
 	        		}
 	        		if(textFlag){
 	        			u.showMessageDialog({type: "info", title: "提示信息", msg: "文本超出500字，请重新编辑！", backdrop: true});
 	        		}
 	    		}
 		    }
 		}
 		
		var init = function(){
			editDetail();
			goNew();
			previewNotice();
			$("#upload-file").on("change",function(){
	        	if(""!=$("#upload-file").val()){
	        		isSubmitFile=true;
	        	}else{
	        		  
	        	  }
	        });
		};
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);