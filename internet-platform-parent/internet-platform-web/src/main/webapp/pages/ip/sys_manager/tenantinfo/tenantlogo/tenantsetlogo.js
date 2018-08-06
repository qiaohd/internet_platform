define([ 'jquery', 'knockout','text!./tenantsetlogo.html','css!./tenantsetlogo.css'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		viewModel.imgUpload = function(){
			$("#picupload").attr("action",$ctx+"/sysmanager/hirercfg/uploadIogo");
			var cWidth = $(".jcrop-holder div:first").width();
	    	var cHeight = $(".jcrop-holder div:first").height();
	    	var pWidth = $(".jcrop-holder").width();
	    	var pHeight = $(".jcrop-holder").height();
	    	var cTop = $(".jcrop-holder div:first").position().top;
	    	var cLeft = $(".jcrop-holder div:first").position().left;
	    	var filePath=$("#previewfile").val();
	    	$("#x").val(cLeft);
	    	$("#y").val(cTop);
	    	$("#w").val(cWidth);
	    	$("#h").val(cHeight);
	    	$("#w1").val(pWidth);
	    	$("#h2").val(pHeight);
	    	
	    	$("#filePath").val(filePath);
	    	
			$("#picupload").submit();
			/*$("#picupload").ajaxSubmit(function (responseResult) {
				 //responseResult 为从后台返回信息，通常情况下返回的是JSON，我们工作中常使有的
				alert(responseResult);
		    });*/
			
			
		};
		
	var ImgObj = new Image(); //建立一个图像对象
	var ImgFileSize;
	viewModel.previewImage = function() {
		$("#targetBg").css("display","none");
		var pic = document.getElementById("target");
		var file = document.getElementById("previewfile");
		var ext = file.value.substring(file.value.lastIndexOf(".") + 1).toLowerCase();
		var preview = document.getElementById("preview");
			
		$("#picupload").attr("action",$ctx+"/sysmanager/hirercfg/uploadIogo");
		$("#upload-logo-save-btn").attr("disabled",false);
		$("#upload-logo-save-btn").css({"cursor":"pointer","background":"#2298ef"});
		// gif在IE浏览器暂时无法显示
		if (ext != 'png' && ext != 'jpg' && ext != 'jpeg' && ext != 'gif' && ext != 'bmp') {
			alert("不是支持的图片格式！");
			$("#targetBg").css("display","inline-block");
			$("#upload-logo-save-btn").attr("disabled",true);
			$("#upload-logo-save-btn").css({"cursor":"default","background":"#dadada"});
			return;
		}
		if (file.files && file.files[0]) {
			if(file.files[0].size > 102400){
				alert("文件大小不能大于100K！");
				$("#targetBg").css("display","inline-block");
				$("#upload-logo-save-btn").attr("disabled",true);
				$("#upload-logo-save-btn").css({"cursor":"default","background":"#dadada"});
				return;
				
			}
			html5Reader(file);
		} else {
			 // IE浏览器		
//			file.select();
//			file.blur();
//			ImgObj.src = file.value;
//			alert(ImgObj.fileSize);
//			ImgFileSize = Math.round(ImgObj.fileSize / 1024 * 100) / 100;
//			// <<<<<
//			var filePath=file.value;
//			var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
//			var file = fileSystem.GetFile(filePath);
//			ImgFileSize= file.Size;
//			
//			var image = new Image();
//			image.src=file.value;
//			image.onload =function(){
//			    var width = image.width;
//			    var height = image.height;
//			    var fileSize = image.fileSize;
//			    //alert(width+'======'+height+"====="+fileSize);
//			    alert(fileSize);
//			}
			 try {
	               
	                file.select();
	                file.blur();
	                var path = document.selection.createRange().text;
	                var fso = new ActiveXObject("Scripting.FileSystemObject");
	                fileSize = fso.GetFile(path).size;
	                ImgFileSize=Math.round(fileSize / 1024 * 100) / 100;
	                //alert(fileSize);//弹出文件大小
	            } catch (e) {
	                alert(e + "\n" + "如果错误为：Error:Automation 服务器不能创建对象；" + "\n" + "请按以下方法配置浏览器：" + "\n" + "请打开【Internet选项-安全-Internet-自定义级别-ActiveX控件和插件-对未标记为可安全执行脚本的ActiveX控件初始化并执行脚本（不安全）-点击启用-确定】");
	                return window.location.reload();
	            }
	            if(ImgFileSize > 100){
					alert("文件大小不能大于100K！");
					$("#targetBg").css("display","inline-block");
					$("#upload-logo-save-btn").attr("disabled",true);
					$("#upload-logo-save-btn").css({"cursor":"default","background":"#dadada"});
					return;
				
			}
			var reallocalpath = document.selection.createRange().text;
			var ie6 = /msie 6/i.test(navigator.userAgent);
			// IE6浏览器设置img的src为本地路径可以直接显示图片
			if (ie6) pic.src = reallocalpath;
			else {
				pic.src = reallocalpath;
				// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
				pic.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + pic.src + "\")";
				// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
				//pic.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw=='+pic.style.filter+'';				
				// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
				$(".preview-container img").attr("src",reallocalpath);
				document.getElementById("target").innerHTML=""; 
				document.getElementById("target").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + reallocalpath + "\")";//使用滤镜效果 
				document.getElementById("preview").innerHTML=""; 
				document.getElementById("preview").style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + reallocalpath + "\")";//使用滤镜效果 
			}
		}
	};
	
	function html5Reader(file) {
		var file = file.files[0];
		var reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload = function(e) {
//			var pic = document.getElementById("target");
//			pic.src = this.result;
			var picc = $(".jcrop-holder img");
			picc[0].src = this.result;
			picc[1].src = this.result;
			$("#target-img").css({"height":"320px","width":"320px"});
			//picc[2].src = this.result;
		};
	}
	
	viewModel.savePreviewImage = function(){
	};

		var init = function(){
	
		};
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);