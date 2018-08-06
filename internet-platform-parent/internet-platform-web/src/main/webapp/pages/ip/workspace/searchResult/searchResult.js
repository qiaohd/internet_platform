define([ 'jquery', 'knockout','text!./searchResult.html','css!./searchResult.css'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		function initCategory(){
			$.ajax({
				url: $ctx + "/search/loadCategoryInfo",
				type: "GET",
				dataType: "json",
				data: {
				},
				success: function (data) {
					$("#select-choice-content").html("");
					for(var i=0;i<data.menuList.length;i++){
						var temp = '<li><a>'+data.menuList[i].menuName+'</a></li>';
						$("#select-choice-content").append(temp);
					}
					$("#update-time-content").html("");
					var temp2='<li class="twoWeeks">近两周<span class="hide">'+data.twoWeeks+'</span></li><li class="threeMonth">三个月内<span class="hide">'+data.threeMonth+'</span></li><li class="thisYear">今年<span class="hide">'+data.thisYear+'</span></li><li class="lastYear">去年至今<span class="hide">'+data.lastYear+'</span></li>';
					$("#update-time-content").append(temp2);
					gategroyClick();
				},
				error:function(){
					//alert("服务器繁忙，请重试！");
				}
    		});
		}
		function gategroyClick(){
			$("#select-choice-content li").on("click",function(){
				var choice_text = $(this).text();
				$("#choice-search").text(choice_text);
				viewModel.getNoticeList();
			});
			$("#update-time-content li").on("click",function(){
				var time_text = $(this).find("span").text();
				$("#time-search").text(time_text);
				viewModel.getNoticeList();
			});
			$("#search-conditions-content li").on("click",function(){
				 $(this).find("a").text("");
				 viewModel.getNoticeList();
			});
			$("#header-search-button").click(function(){
        		viewModel.getNoticeList();
        	});
			$("#search-info-content a").on("click",function(){
				var href = $(this).attr('href');
			     window.location=href;
			     var path = this.hash.replace('#', '');
			     path = path.split('?')[0];
			     addRouter(path);
	    	})
		};
		viewModel.getNoticeList = function(pageIndex) {
 		   var keywords = $("#header-search").val();
 		   var typeVal = $("#type-search").text();
 		   var choiceVal = $("#choice-search").text();
 		   var timeVal = $("#time-search").text();
 		   if(pageIndex==undefined){
 			  pageIndex=1;
 		   }
 		  var s1=new Date().getTime();
 		   $.ajax({
 			    url: $ctx + "/search/solrSearch",
	            type: 'GET',
	            dataType: 'json',
	            data: {
	            	"keywords":keywords,
	            	"pageNo":pageIndex,
	            	"index_catalog":choiceVal,
	            	"searchResult":typeVal,
	            	"searchDate":timeVal
	            },
	            success: function (data) {
	            	var s2=new Date().getTime();
	            	$("#total-count").text(data.totalCount);
	            	$("#total-info").text($("#header-search").val());
	            	$("#search-info-content").html("");
	            	$("#total-sec").text((s2-s1)/1000);
	            	if(data.totalCount>0){
	            		for(var i=0;i<data.solrMenu.length;i++){
		            		var date = new Date(data.solrMenu[i].updateDate);
							var date_time = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate();
							var temp='<a class="clearfix" href="'+data.solrMenu[i].routerUrl+'?'+data.solrMenu[i].params+'"><dl class="info-left"><dt>'+data.solrMenu[i].name+'</dt><dd>'+data.solrMenu[i].content+'</dd></dl><dl class="info-right"><dt>上传人：<span>'+data.solrMenu[i].linkMan+'</span></dt><dd>'+data.solrMenu[i].upDateDate+'</dd></dl></a>';
		            		$("#search-info-content").append(temp);
		            	}
	            	}else{
	            		$("#search-info-content").html("暂无数据!");
	            		$("#total-count").text("0");
	            	}
	            	goDetails();
	            	initpages(data);
	            }
	       })
       };
	   function goDetails(){
	    	$(".search-info-content").find("a").on("click",function(){
	    		 var href = $(this).attr('href');
			     window.location=href;
			     var path = this.hash.replace('#', '');
			     path = path.split('?')[0];
			     addRouter(path);
	    	});
	    };
		var init = function(){
			initCategory();
			viewModel.getNoticeList();
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
