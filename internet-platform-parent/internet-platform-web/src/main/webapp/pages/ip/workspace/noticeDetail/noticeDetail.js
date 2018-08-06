define([ 'jquery', 'knockout','text!./noticeDetail.html','css!./noticeDetail.css'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		function goNew(){
 	    	$("#detail-go-new").on("click",function(){
 	    		 var href = $(this).attr('href');
 			     window.location=href;
 			     var path = this.hash.replace('#', '');
 			     addRouter(path);
 	    	});
 	    	$("#go-list").on("click",function(){
 	    		 var href = $(this).attr('href');
 			     window.location=href;
 			     var path = this.hash.replace('#', '');
 			     addRouter(path);
 	    	})
 	    };
 	    function initDetail(){
 	    	var time = new Date();
 	    	var this_href = window.location;
 	    	var this_path = this_href.hash.replace('#', '');
 	    	var this_val= this_path.split("=")[1];
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
						$("#notice-tit").text(data.noticeTitle);
						$("#notice-author").text(data.linkman);
						var date = new Date(data.updateDate);
						var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
						$("#notice-time").text(date_time);
						document.getElementById("notice-cont").innerHTML=data.noticeContent;
						var userId = $("#user").attr("class");
						if(data.attachmentUrl!=""){
							$("#notice-cont").after('<a href="'+ $ctx +'/notice/downLoadAttachment?noticeId=' + this_val+'">下载附件【'+data.attachmentUrl+'】</a>');
						}
				},
				error:function(){
					//alert("服务器繁忙，请重试！");
				}
    		});
 	    };
 	    viewModel.editNotice = function(){
 	    	var noticeId = $("#noticeId").val();
 	    	var href = '#/ip/workspace/noticeNew/noticeNew?noticeId='+noticeId+'';
 	    	window.location=href;
		    path = "/ip/workspace/noticeNew/noticeNew";
		    addRouter(path);
 	    };
 	   viewModel.deleteNotice = function(){
 		  var time = new Date();
 		  var noticeId = $("#noticeId").val();
	    	$.ajax({
				url: $ctx + "/notice/deleteNotice",
				type: "GET",
				dataType: "json",
				data: {
					"noticeId":noticeId,
					"time": time.getTime()
				},
				success: function (data) {
					if(data.result=="true"){
						alert("删除成功");
						window.location="#/ip/workspace/noticeList/noticeList";
						addRouter("/ip/workspace/noticeList/noticeList");
					}else if(data.result=="false"){
						alert("删除失败");
					}
				},
				error:function(){
					//alert("服务器繁忙，请重试！");
				}
	    	});
	    };
	    function typeClass(){
	    	var login_type = $("#bell").attr("class");
			if(login_type =="hirers"){
				$("#detail-go-new").removeClass("hide");
				$("#edit-notice").removeClass("hide");
				$("#delete-notice").removeClass("hide");
			}
			if(login_type =="users"){
				$("#detail-go-new").addClass("hide");
				$("#edit-notice").addClass("hide");
				$("#delete-notice").addClass("hide");
			}
	    };
		var init = function(){
			typeClass();
			initDetail();
			goNew();
		};
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
