define([ 'jquery', 'knockout','text!./ws.html','tree','layer','css!./ws.css','datatimepicker'],
	function($, ko, template) {
		var app;
		var colect_menu_id,
			colect_menu_name,
			colect_menu_url,
			colect_icon_id,
			colect_icon_url;
		var viewModel = {
			data : ko.observable({}),
			menuDataTable: new u.DataTable({
                params: {
                    "cls": "com.ufgov.ip.entity.system.IpMenu"
                },
                meta: {
                    'menuId': {type: 'string'},
                    'parentMenuId': {type: 'string'},
                    'menuName': {type: 'string'},
                    'url':{type: 'string'}
                }
            }),
            choiceFatherMenuSetting: {
                "callback": {
                	"onClick":function(e, id, node){
                		//console.log(node);
                			// 获取到节点的idValue
        					var idValue = node.id;
        					var oThis = app.getComp('choice-menu');
        					var rowId = oThis.getRowIdByIdValue(idValue);
        					var index = oThis.dataTable.getIndexByRowId(rowId);
        					var url = viewModel.menuDataTable.rows._latestValue[index].data.url.value;
        					if(url!=""){
        						colect_menu_id = node.id
        						colect_menu_name = node.name;
        						colect_menu_url = url;
                		    }else{
                		    	layer.msg('请选择末级！', {icon: 0, time: 2000});
                		    }
                	}
                }
            }
		};
		function typeUser(){
	    	var login_type = $("#bell").attr("class");
			if(login_type =="hirers"){
				$("#top-role").text("管理员");
			}
			if(login_type =="users"){
				$("#top-role").text("普通用户");
			}
	    };
		//初始化个人资料
		function initUser(){
			var time = new Date();
			$.ajax({
	            type: 'GET',
	            url: $ctx + "/menuShow/userMenu",
	            data: {"time": time.getTime()},
	            dataType: 'json',
	            success: function (data) {
	            	//console.log(data);
	            	typeUser();
	                $('#top-company').text(data.hirerCompanyName);
	                $('#top-tel').text(data.cusername);
	                $("#user-id").val(data.uesrId);
	                if(data.hirerPicUrl == "" || data.hirerPicUrl==null){
	                	$('#top-img img').attr("src",$ctx+"/images/default-user.png");
	                } else {
	                    $('#top-img img').attr("src",data.hirerPicUrl);
	                }
	                initCollectFun();
	            }
	        });
		};
		function initNoticeList(){
			var time = new Date();
			$.ajax({
				url: $ctx + "/notice/getAllNotice",
				type: "GET",
				dataType: "json",
				data: {
					"time": time.getTime()
				},
				success: function (data) {
					//console.log(data);
					$("#all-notices").html("");
					
					if(data.allNotice.length>8){
						for(var i=0;i<8;i++){
							var temp = '<li><span style="font-size: 18px; margin-right:10px;">&middot;</span><a href="#/ip/workspace/noticeDetail/noticeDetail?noticeId='+data.allNotice[i].noticeId+'" title="'+data.allNotice[i].noticeTitle+'">'+data.allNotice[i].noticeTitle+'</a></li>'
							$("#all-notices").append(temp);
						}
						$("#more-notice").text("更多");
					}else if(data.allNotice.length<9){
						for(var i=0;i<data.allNotice.length;i++){
							var temp = '<li><span style="font-size: 18px; margin-right:10px;">&middot;</span><a href="#/ip/workspace/noticeDetail/noticeDetail?noticeId='+data.allNotice[i].noticeId+'" title="'+data.allNotice[i].noticeTitle+'">'+data.allNotice[i].noticeTitle+'</a></li>'
							$("#all-notices").append(temp);
						}
						$("#more-notice").text("");
					}
					goDetail();
				},
				error:function(){
					u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请重试！", backdrop: true});
				}
    		});
		};
		function goDetail(){
 	    	$(".ws-right").find("a").on("click",function(){
 	    		 var href = $(this).attr('href');
 			     window.location=href;
 			     var path = this.hash.replace('#', '');
 			     path = path.split('?')[0];
 			    // console.log(path);
 			     addRouter(path);
 	    	});
 	    };
 	    
 	    //初始化收藏菜单表展示
 	    function initCollectFun(){
 	    	var time = new Date();
 	    	var userId = $("#user-id").val();
 	    	$.ajax({
				url: $ctx + "/menuShow/loadMenuIcon",
				type: "POST",
				dataType: "json",
				data: {
					"userId":userId,
					"time": time.getTime()
				},
				success: function (data) {
					//console.log(data);
					if(data.result=="success"){
						data=data.dataList;
						$("#center-content-menu").html("");
						for(var i=0;i<data.length;i++){
							var temp='<dl class="'+data[i].menu_id+'"><a href="'+data[i].url+'"><dt><img class="'+data[i].icon_id+'" src="'+data[i].icon_path+'" style="" height="64" width="64" /></dt><dd>'+data[i].menu_name+'</dd></a><dd class="close-icon">×</dd></dl>';
							$("#center-content-menu").append(temp);
						}
						viewModel.delCollect();
						menuClick();
					}else{
						alert("初始化失败");
					}
				},
				error:function(){
					u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请重试！", backdrop: true});
				}
    		});
 	    };
 	    //删除收藏表菜单
 	    viewModel.delCollect = function(){
 	    	$(".center-content-menu .close-icon").on("click",function(){
 	 	    	var menuId = $(this).parent("dl").attr("class");
 	 	    	var userId = $("#user-id").val();
 	 	    	$.ajax({
 					url: $ctx + "/menuShow/delMenuIcon",
 					type: "POST",
 					dataType: "json",
 					data: {
 						"userId":userId,
 						"menuId":menuId
 					},
 					success: function (data) {
 						if(data.result=="success"){
 							$("#save-success-text").text("删除成功！");
 							$("#save-success-new").css("display","block");
 	                        $("#save-success-new").hide(3000);
 							initCollectFun();
 							menuClick();
 						}else{
 							u.showMessageDialog({type: "info", title: "提示信息", msg: "删除失败！", backdrop: true});
 						}
 					},
 					error:function(){
 						u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请重试！", backdrop: true});
 					}
 	    		});
 	 	    });
 	    };
 	    
 	    function addMenu(){
 	    	$("#menu-add").on("click",function(){
 	    		$("#dialog_add_group").modal({backdrop:'static',keyboard:false});
 	    		var show_content = document.getElementById("manage-jump");
 	            show_content.style.display = 'block';
 	            $("#menuicon-list ul li").eq(0).find("span").addClass("active");
 	            $("#menuicon-list ul li").eq(0).siblings().find("span").removeClass("active");
 	            $("#getIconAdd").val($("#menuicon-list ul li").eq(0).find("img").attr("class"));
	        	$("#menu-select-icon").val($("#menuicon-list ul li").eq(0).find("img").attr("src"));
	        	colect_menu_id = "";
    			colect_menu_name = "";
    			colect_menu_url = "";
	        	var queryData = {};
	            queryData["search_EQ_userId"] = $("#user-id").val();
	            viewModel.menuDataTable.addParams( queryData);
	        	app.serverEvent().addDataTable("menuDataTable", "all").fire({
	                url: $ctx + '/evt/dispatch',
	                ctrl: 'menu.MenuController',
	                async: false,
	                method: 'loadMenuTreeNoIcon',
	                success: function (data) {
	                }
	            });
 	    	})
 	    };
 	   viewModel.menuiconOn = function(){
       	   $("#menuicon-list ul li").on('click',function(){
	        	$(this).find("span").addClass("active");
	        	$(this).siblings().find("span").removeClass("active");
	        	$("#getIconAdd").val($(this).find("img").attr("class"));
	        	$("#menu-select-icon").val($(this).find("img").attr("src"));
           });
       };
       //保存收藏菜单
 	   viewModel.save_new_menu = function (){
 		  var colect_icon_id = $("#getIconAdd").val();
 		  var colect_icon_url = $("#menu-select-icon").val();
      	  if(colect_menu_id!=""){
      		$.ajax({
				url: $ctx + "/menuShow/saveMenuIcon",
				type: "POST",
				dataType: "json",
				data: {
					"userId":$("#user-id").val(),
					"iconId": colect_icon_id,
					"iconPath": colect_icon_url,
					"menuId":colect_menu_id,
					"menuName":colect_menu_name,
					"url":colect_menu_url
				},
				success: function (data) {
					if(data.result=="success"){
						viewModel.close_jump_window();
						$("#save-success-text").text("保存成功！");
						$("#save-success-new").css("display","block");
                        $("#save-success-new").hide(3000);
                        initCollectFun();
						viewModel.delCollect();
						menuClick();
					}else{
						u.showMessageDialog({type: "info", title: "提示信息", msg: "保存失败！", backdrop: true});
					}
				},
				error:function(){
					u.showMessageDialog({type: "info", title: "提示信息", msg: "服务器繁忙，请重试！", backdrop: true});
				}
    		});
      	  }else{
      		  alert("请选择需要收藏的菜单！");
      	  }
 	   }
 	   viewModel.close_jump_window = function (){
 		  var show_content = document.getElementById("manage-jump");
          $('#dialog_add_group').modal('hide');
          show_content.style.display = 'none';
 	   }
 	  function searchClick(){
 	    	$("#header-search-button").on("click",function(){
 	        	var href = $(this).attr('href');
 			     window.location=href;
 			     var path = this.hash.replace('#', '');
 			     addRouter(path);
 	        });
 	    };
 	    //点击收藏表菜单 进入该页面
 	   function menuClick(){
 		  $(".center-content-menu a").on("click",function(){
   		     var href = $(this).attr('href');
 		     window.location=href;
 		     var path = this.hash.replace('#', '');
 		     addRouter(path);
  	   });
 	   }
 	   
		var init = function(){
//			ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el: '#manage-jump-content',
                    model: viewModel
                }
            );
            initNoticeList();
            searchClick();
			initUser();
			addMenu();
			viewModel.menuiconOn();
		};
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
