define([ 'jquery', 'knockout','text!./unaudited.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','css!./unaudited.css'],
    function($, ko, template) {
	var arFlag="";
        var viewModel = {
            data : ko.observable({}),
            leaveDataTable: new u.DataTable({
                meta: {
                    'id':{},
                    'proposer':{},
                    'startDate': {},
                    'endDate': {},
                    'type': {},
                    'reason': {},
                    'taskId': {}
                }
            })
        };

        SetFun = function(obj) {
            var aa=obj.row.value.taskId
            $("#askTaskId").val(aa);
            if(arFlag=="true"){
            	obj.element.innerHTML = '<a class="region-fun" id='+obj.rowIndex+' onclick="mgrAgree('+obj.rowIndex+')">同意</a> <a id='+obj.rowIndex+' onclick="Opposition('+obj.rowIndex+')" class="region-fun">驳回</a>';
            }
            else{
            	obj.element.innerHTML ='';
            }
        }
        Opposition = function (i) {
        	var taskId=$("#"+i).parent().parent().prev().attr("title");
        	var businessId=$("#"+i).parent().parent().parent().find("td").eq(0).attr("title");
        	$("#askTaskId").text(taskId);
        	$("#businessId").text(businessId);
        	
            $("#delete-region-notice").modal({backdrop: 'static', keyboard: false});
        }
        viewModel.delete_region_close = function() {
            $("#delete-region-notice").modal("hide");
        }
        viewModel.errorNoticeClose = function() {
        	$("#error-notice").modal("hide");
        }
        mgrDisAgree=function(){
        	var askHirerId=$("#askHirerId").text();
        	var askCoId=$("#askCoId").text();
        	var askUserId=$("#askUserId").text();
      	    var roleType=$("#roleType").text();
      	    var askTaskId=$("#askTaskId").text();
      	    var businessId=$("#businessId").text();
      	    var suggestion=$("#suggestion").val();
      	  $.ajax({
              url: $ctx + "/askForLeave/doApprove",
              type: "POST",
              data:{
             	"userId":askUserId,
                "taskId":askTaskId,
             	"check" :"0",
             	"hirerId":askHirerId,
             	"businessId":businessId,
             	"suggestion":suggestion
             },
             success: function (data) {
             	if(data.result=="fail"){
             		$("#error-notic-text").text(data.reason);
					$("#error-notice").modal({backdrop:'static',keyboard:false});
             	}else if(data.result=="true"){
              		$("#sure_add_group").modal({backdrop:'static',keyboard:false});
	    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>任务执行成功！</span> ");
	    			var show_error = document.getElementById("jump-content");
					show_error.style.display='block';
					
              	}else if(data.result=="Enabled"){
              		$("#sure_add_group").modal({backdrop:'static',keyboard:false});
	    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>工作流已经关闭，暂时不能提交请假申请！</span> ");
	    			var show_error = document.getElementById("jump-content");
					show_error.style.display='block';
					
	             }
              	
              }
          });
        }
        
        
        //执行审批
        mgrAgree=function(i){
        	var time = new Date();
      	    var askHirerId=$("#askHirerId").text();
        	var askCoId=$("#askCoId").text();
        	var askUserId=$("#askUserId").text();
      	    var roleType=$("#roleType").text();
        	var taskId=$("#"+i).parent().parent().prev().attr("title");
        	var businessId=$("#"+i).parent().parent().parent().find("td").eq(0).attr("title");
        	var suggestion="同意";
        	$.ajax({
                url: $ctx + "/askForLeave/doApprove",
                type: "POST",
                data:{
               	"userId":askUserId,
                "taskId":taskId,
               	"check" :"1",
               	"hirerId":askHirerId,
               	"businessId":businessId,
               	"suggestion":suggestion,
               	"time": time.getTime()
               },
               success: function (data) {
               	if(data.result=="fail"){
               		$("#error-notic-text").text(data.reason);
					$("#error-notice").modal({backdrop:'static',keyboard:false});
               	}else if(data.result=="true"){
                		$("#save-success-new").css("display","block");
                		$("#save-success-text").text("审核通过!");
	            		$("#save-success-new").fadeOut(4000);
	            		viewModel.getUncheckTicket();
	            		getBackShow();
                	}else if(data.result=="Enabled"){
                		$("#save-success-new").css("display","none");
	            		$("#save-success-new").fadeOut(4000);
   	                }
                	
                }
            });
        }

        //获得参数信息(hirerId、coId)
        viewModel.getparamInfo=function(){
        	var time = new Date();
        	//获得后写到隐藏标签
        	$.ajax({
                url: $ctx + "/askForLeave/getQueryParamInfo",
                type: "GET",
                contentType: "application/json",
                data:{
            		"time": time.getTime()
            	},
                success: function (data) {
                	//将参数信息写到隐藏标签中
                	var hirerId=data.hirerId;
                	var userid=data.userId;
                	var coId=data.coId;
                	var roleName=data.roleName;
                	$("#askHirerId").text(hirerId);
                	$("#askCoId").text(coId);
                	$("#askUserId").text(userid);
                	$("#roleType").text(roleName);
                	
                	var this_href = window.location;
         	    	var this_path = this_href.hash.replace('#', '');
         	    	var this_val= this_path.split("=")[1]; 
         	    	if(this_val==undefined){
         	    		//查询当前部门经理未审批的请假单
                    	viewModel.getUncheckTicket();
         	    	}else{
         	    		viewModel.getCurUncheckTicket(this_val);
         	    	}
                	
                	
                }
            });
        }
        
        
        
        
      //查询当前部门经理未审批的请假单
        viewModel.getUncheckTicket=function(){
        	
        	var askHirerId=$("#askHirerId").text();
        	var askUserId=$("#askUserId").text();
        	var askCoId=$("#askCoId").text();
        	
        	
        	//用datatable查询当前部门的请假列表
        	var queryuserData = {};
        	queryuserData["search_EQ_userId"] = askUserId;
        	queryuserData["search_EQ_hirerId"] = askHirerId;
        	queryuserData["search_EQ_roleName"] = "员工";
        	queryuserData["search_EQ_isEnabled"] = "1"; 
        	queryuserData["search_EQ_coId"] = askCoId;
       	       	    
        	$.ajax({
            	url: $ctx + '/askForLeave/getNotApproved',
            	type: "GET",
            	data:queryuserData,
            	dataType : 'json',
            	success:function(data){
            		if(data.data!=null){
            			viewModel.leaveDataTable.setSimpleData(data.data.content);
            			viewModel.leaveDataTable.totalPages(data.data.totalPages);
            			viewModel.leaveDataTable.totalRow(data.data.totalElements);
            		} 
            	}
            });       	        	              	
        }
        
        
        //获得当前请假单详情
        viewModel.getCurUncheckTicket=function(this_val){
        	var time = new Date();
	    		$.ajax({
	            	url: $ctx + '/askForLeave/getTicketDetail',
	            	type: "GET",
	            	data:{
	            		"aflId":this_val,
	            		"time": time.getTime()
	            	},
	            	dataType : 'json',
	            	success:function(data){
	            		if(data!=null){
	            			viewModel.leaveDataTable.setSimpleData(data.data);
	            		} 
	            	}
	            });     
	    	
        	
        }
        
        
        viewModel.closeModel=function(){
        	$("#sure_add_group").modal("hide");
            viewModel.getUncheckTicket();
        	//window.location= $ctx +'/#/ip/resource/audited/audited';
        }
      //获得按钮权限信息
        viewModel.getBtnInfo=function(){
        	//获得后写到隐藏标签
        	$.ajax({
                url: $ctx + "/PermissionAuthIP/IfApprovePermessionAuthed",
                type: "POST",
                contentType: "application/json",
                success: function (data) {
                	arFlag=data.flag;
                  viewModel.getparamInfo();
                }
              })
        }
        //刷新待办事项
        function getBackShow(){
        	var time = new Date();
        	$.ajax({
                url: $ctx+'/askForLeave/getNoticeList',
                type: 'GET',
                data:{
            		"time": time.getTime()
            	},
                dataType: 'json',
                success: function (data) {
                	if(data.flag == "success"){
                		$("#more-task").text("");
                		if(data.data!=null){
                			var list = data.data;
                    		var listLength = list.length;
                    		if(listLength>0 && listLength<=5){
                    			$("#task-list").removeClass("hide");
                    			$("#task-list").text(listLength);
                        		$("#task-num").text("("+listLength+")");
                        		$("#task-cont").html("");
                        		for(var i=0; i<listLength;i++){
                        			var temp = '<li><a href="#/ip/resource/unaudited/unaudited?aflId='+list[i].id+'">'+list[i].proposer+'申请单</a></li>';
                        			$("#task-cont").append(temp);
                        		}
                        		$("#more-task").text("");
                    		}else if(listLength>5){
                    			$("#task-list").removeClass("hide");
                    			$("#task-list").text(listLength);
                        		$("#task-num").text("("+listLength+")");
                        		$("#task-cont").html("");
                        		for(var i=0; i<5;i++){
                        			var temp = '<li><a href="#/ip/resource/unaudited/unaudited?aflId='+list[i].id+'">'+list[i].proposer+'申请单</a></li>';
                        			$("#task-cont").append(temp);
                        		}
                        		$("#more-task").text("更多");
                        		$("#more-task").on("click",function(){
                        			var href = $(this).attr('href');
                        		    window.location=href;
                        		    var path = this.hash.replace('#', '');
                        		    path = path.split('?')[0];
                        		    addRouter(path);
                        		});
                    		}
                    		else{
                    			$("#task-list").addClass("hide");
                    		}
                		}
                	}else{
                		alert(data.msg);
                	}
                }
            })
        };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            viewModel.getBtnInfo();

            //获得参数信息
            /*var this_href = window.location;
 	    	var this_path = this_href.hash.replace('#', '');
 	    	var this_val= this_path.split("=")[1]; 
 	    	if(this_val==undefined){
 	    		viewModel.getparamInfo();
 	    	}else{
 	    		$.ajax({
 	            	url: $ctx + '/askForLeave/getTicketDetail',
 	            	type: "GET",
 	            	data:{
 	            		"aflId":this_val
 	            	},
 	            	dataType : 'json',
 	            	success:function(data){
 	            		console.log(data);
 	            		if(data!=null){
 	            			viewModel.leaveDataTable.setSimpleData(data.data);
 	            		} 
 	            	}
 	            });     
 	    	}*/
        };

        return {
            'model' : viewModel,
            'template' : template,
            'init' : init
        };
    }
);
/**
 * Created by shi on 16/7/26.
 */
