define([ 'jquery', 'knockout','text!./noticeList.html','uui','css!./noticeList.css'],
	function($, ko, template) {
		var app;
		var viewModel = {
			data : ko.observable({})
	    };
		var is_pub = 1;
	    function goNew(){
 	    	$("#list-go-new").on("click",function(){
 	    		 var href = $(this).attr('href');
 			     window.location=href;
 			     var path = this.hash.replace('#', '');
 			     addRouter(path);
 	    	});
 	    	$("#goto-list").on("click",function(){
 	    		viewModel.getList();
//	    		 var href = $(this).attr('href');
//			     window.location=href;
//			     var path = this.hash.replace('#', '');
//			     addRouter(path);
	    	})
	    	$(".append-classfy a").on("click",function(){
	    		is_pub=$(this).attr("id");
	    		$('.'+is_pub+'').removeClass("hide");
	    		$('.'+is_pub+'').siblings("ul").addClass("hide");
	    		$(this).addClass("append-selected");
	    		$(this).siblings().removeClass("append-selected");
	    		viewModel.getNoticeList();
	    	});
 	    	$("#search-notice").on("click",function(){
 	    		viewModel.getNoticeList();
 	    	})
 	    };
 	   viewModel.getList = function(pageIndex){
 		   if(pageIndex==undefined){
 			  pageIndex=1;
 		   }
 		  var time = new Date();
 		   $.ajax({
 			    url: $ctx + "/notice/getNoticeSolrResult",
	            type: 'GET',
	            dataType: 'json',
	            data: {
	            	"keywords":"",
	            	"isPublish":"1",
	            	"pageNo":pageIndex,
	            	"time": time.getTime()
	            },
	            success: function (data) {
	            	//console.log(data);
	            	$("#1").addClass("append-selected");
		    		$("#0").removeClass("append-selected");
		    		$("#keyword").val("");
	            	if(data.isSolr=="false"){
	            		$("#has-total").text("("+data.isPub+")");
		            	$("#no-total").text('('+data.isSave+')');
		            	$(".publish").html("");
		            	if(data.totalCount>0){
		            		for(var i=0;i<data.resultList.length;i++){
			            		var date = new Date(data.resultList[i].updateDate);
								var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
			            		var temp='<li id="'+data.resultList[i].noticeId+'"><a href="#/ip/workspace/noticeDetail/noticeDetail?noticeId='+data.resultList[i].noticeId+'"><p class="notice-tit">'+data.resultList[i].noticeTitle+'</p><div class="notice-cont">'+data.resultList[i].noticeContent+'</div><p class="notice-tail"><span class="tail-author">'+data.resultList[i].linkman+'</span>发布于<span class="tail-time">'+date_time+'</span></p></a></li>';
			            		$(".publish").append(temp);
			            	}
		            	}else{
		            		$(".publish").html("暂无数据!");
		            	}
	            	}else if(data.isSolr=="true"){
	            		$("#has-total").text("("+ data.isPub +")");
		            	$("#no-total").text('('+data.isSave+')');
		            	$(".publish").html("");
		            	if(data.totalCount>0){
		            		for(var i=0;i<data.resultList.length;i++){
			            		var date = new Date(data.resultList[i].upDateDate);
								var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
			            		var temp='<li id=""><a href="'+data.resultList[i].routerUrl+'"><p class="notice-tit">'+data.resultList[i].name+'</p><div class="notice-cont">'+data.resultList[i].content+'</div><p class="notice-tail"><span class="tail-author">'+data.resultList[i].linkMan+'</span>发布于<span class="tail-time">'+date_time+'</span></p></a></li>';
			            		$(".publish").append(temp);
			            	}
		            	}else{
		            		$(".publish").html("暂无数据!");
		            	}
	            	}

	            	goDetails();
	            	initpages(data);
	            }
	       })

 	   };
 	   viewModel.getNoticeList = function(pageIndex) {
 		   var keywords = $("#keyword").val();
 		   if(pageIndex==undefined){
 			  pageIndex=1;
 		   }
 		  var time = new Date();
 		   $.ajax({
 			    url: $ctx + "/notice/getNoticeSolrResult",
	            type: 'GET',
	            dataType: 'json',
	            data: {
	            	"keywords":keywords,
	            	"isPublish":is_pub,
	            	"pageNo":pageIndex,
	            	"time": time.getTime()
	            },
	            success: function (data) {
	            	//console.log(data);
	            	if(data.isSolr=="false"){
	            		$("#has-total").text("("+data.isPub+")");
		            	$("#no-total").text("("+data.isSave+")");
		            	$(".publish").html("");
		            	if(data.totalCount>0){
		            		for(var i=0;i<data.resultList.length;i++){
			            		var date = new Date(data.resultList[i].updateDate);
								var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
			            		var temp='<li id="'+data.resultList[i].noticeId+'"><a href="#/ip/workspace/noticeDetail/noticeDetail?noticeId='+data.resultList[i].noticeId+'"><p class="notice-tit">'+data.resultList[i].noticeTitle+'</p><div class="notice-cont">'+data.resultList[i].noticeContent+'</div><p class="notice-tail"><span class="tail-author">'+data.resultList[i].linkman+'</span>发布于<span class="tail-time">'+date_time+'</span></p></a></li>';
			            		$(".publish").append(temp);
			            	}
		            	}else{
		            		$(".publish").html("暂无数据!");
		            	}
	            	}else if(data.isSolr=="true"){
	            		$("#has-total").text("("+data.isPub+")");
		            	$("#no-total").text("("+data.isSave+")");
		            	$(".publish").html("");
		            	if(data.totalCount>0){
		            		for(var i=0;i<data.resultList.length;i++){
			            		var date = new Date(data.resultList[i].upDateDate);
								var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
			            		var temp='<li id=""><a href="'+data.resultList[i].routerUrl+'"><p class="notice-tit">'+data.resultList[i].name+'</p><div class="notice-cont">'+data.resultList[i].content+'</div><p class="notice-tail"><span class="tail-author">'+data.resultList[i].linkMan+'</span>发布于<span class="tail-time">'+date_time+'</span></p></a></li>';
			            		$(".publish").append(temp);
			            	}
		            	}else{
		            		$(".publish").html("暂无数据!");
		            	}
	            	}

	            	goDetails();
	            	initpages(data);
	            }
	       })
       };
       function goDetails(){
	    	$(".list-content").find("a").on("click",function(){
	    		 var href = $(this).attr('href');
			     window.location=href;
			     var path = this.hash.replace('#', '');
			     path = path.split('?')[0];
			     addRouter(path);
	    	});
	    };
	    function typeFind(){
	    	var login_type = $("#bell").attr("class");
			if(login_type =="hirers"){
				$("#list-go-new").removeClass("hide");
				$(".append-classfy").removeClass("hide");
				$("#delete-notice").removeClass("hide");
			}
			if(login_type =="users"){
				$("#list-go-new").addClass("hide");
				$(".append-classfy").addClass("hide");
			}
	    };
		var init = function(){
			ko.cleanNode($('.content')[0]);
			app=u.createApp({
			    el:'.list-content',
			    model:viewModel
			});
			typeFind();
			viewModel.getNoticeList();
			goNew();
		};
		function initpages(data){
			var paginationDefault = document.getElementById('paginationDefault');
			var comp = new u.pagination({
		        el: paginationDefault,
		        jumppage: true
		    });
			if(data.totalCount>0){
				comp.update({
					totalPages: data.totalPages,
			        currentPage: data.currentPage,
			        totalCount: data.totalCount,
			        showState: false
				});
			}else{
				comp.update({
					totalPages: 0,
			        currentPage: 0,
			        totalCount: data.totalCount,
			        showState: false

				});
			}
		    comp.on('pageChange', function(pageIndex) {
		    	viewModel.getNoticeList(pageIndex+1);
		    });
		};

		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
