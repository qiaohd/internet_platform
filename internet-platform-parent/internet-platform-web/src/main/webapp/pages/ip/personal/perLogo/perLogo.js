define([ 'jquery', 'knockout','text!./perLogo.html','bootstrap', 'uui', 'director', 'tree', 'grid','Jcorp','css!./perLogo.css'],
    function($, ko, template) {
        var viewModelPerLogo = {
            data : ko.observable({}),
        };
        var ctx = $("#ctx").val();
      //获取登录人ID
        viewModelPerLogo.getLoginId = function(){
        	$.ajax({
    			type: "GET",
    			url: ctx +"/userset/getLoginId",
    			async:false,
    			success: function(data){
    				$("#hirerId").val(data.hirerId);
    				$("#userId").val(data.userId);
    			}
    		});
        };
        viewModelPerLogo.imgUpload = function(){
			$("#picupload").attr("action",ctx+"/userset/setuserheadIogo");
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
	viewModelPerLogo.previewImage = function() {
		$("#targetBg").css("display","none");
		var pic = document.getElementById("target");
		var file = document.getElementById("previewfile");
		var ext = file.value.substring(file.value.lastIndexOf(".") + 1).toLowerCase();
		var preview = document.getElementById("preview");
			
		$("#picupload").attr("action",ctx+"/sysmanager/hirercfg/uploadIogo");
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
			file.select();
			file.blur();
			ImgObj.src = file.value;
			ImgFileSize = Math.round(ImgObj.fileSize / 1024 * 100) / 100;
			
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
	viewModelPerLogo.perLogoImg = function(){
		var browser=navigator.appName ;
		var b_version=navigator.appVersion ;
		var version=b_version.split(";"); 
		var trim_Version="";
		if(version[1]!=null){
			trim_Version=version[1].replace(/[ ]/g,""); 
		}
    // Create variables (in this scope) to hold the API and image size
    var jcrop_api,
        boundx,
        boundy,

        // Grab some information about the preview pane
        $preview = $('#preview-pane'),
        $pcnt = $('#preview-pane .preview-container'),
        //此处有修改
        xsize = $pcnt.width(),
        ysize = $pcnt.height();
        if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"){
			 $pimg = $('#preview-pane .preview-container .jcrop-preview');
		}else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE9.0"){
			$pimg = $('#preview-pane .preview-container .jcrop-preview');
		} else{
			$pimg = $('#preview-pane .preview-container img');
		}
   
    $('#target').Jcrop({
      onChange: updatePreview,
      onSelect: updatePreview,
      aspectRatio: xsize / ysize
    },function(){
      // Use the API to get the real image size
      var bounds = this.getBounds();
      boundx = bounds[0];
      boundy = bounds[1];
      // Store the API in the jcrop_api variable
      jcrop_api = this;

      // Move the preview into the jcrop container for css positioning
      $preview.appendTo(jcrop_api.ui.holder);
    });

    function updatePreview(c)
    {
      if (parseInt(c.w) > 0)
      {
        var rx = xsize / c.w;
        var ry = ysize / c.h;

        $pimg.css({
          width: Math.round(rx * boundx) + 'px',
          height: Math.round(ry * boundy) + 'px',
          marginLeft: '-' + Math.round(rx * c.x) + 'px',
          marginTop: '-' + Math.round(ry * c.y) + 'px'
        });
      }
    };
	};
	
	viewModelPerLogo.menuClick = function(){
  	  $(".sub-menu li a").click(function(){
	       	var href = $(this).attr('href');
	       	 window.location=href;
	            var path = this.hash.replace('#', '');
	            addRouter(path);
	       });
     };
     viewModelPerLogo.isHirer = function(){
    	 var getHirerId = $("#hirerId").val();
    	 if(getHirerId!=""){
    		 $("#person-set").css("display","none");
    	 }
     };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelPerLogo
                }
            );
            viewModelPerLogo.getLoginId();
            viewModelPerLogo.isHirer();
            viewModelPerLogo.perLogoImg();
            viewModelPerLogo.menuClick();
        };
        return {
            'model' : viewModelPerLogo,
            'template' : template,
            'init' : init
        };
    }
);
