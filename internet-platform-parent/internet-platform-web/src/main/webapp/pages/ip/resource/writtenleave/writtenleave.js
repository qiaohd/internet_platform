define([ 'jquery', 'knockout','text!./writtenleave.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','lodopFuncs','css!./writtenleave.css'],
    function($, ko, template) {
        var viewModel = {
            data : ko.observable({}),
            leaveDataTable: new u.DataTable({
                meta: {
                	'id':{},
                    'startDate': {},
                    'endDate': {},
                    'type': {},
                    'reason': {},
                    'result': {}
                    
                }
            })
           
        };
        LodopFun = function(obj) {
        	console.log(obj);
            obj.element.innerHTML = '<a id="'+ obj.rowIndex +'" onclick="printNormal(this.id)" class="region-fun">打印</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="printTemp(this.id)" class="region-fun">套打</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="printDesign(this.id)" class="region-fun">打印维护</a>';
        }
        
        var userName="";
    	var depName="";
    	var leaveType="";
    	var stratTime="";
    	var endTime="";
    	var leaveReason="";
    	var result="";
    	
    	//获取打印的内容，即每条请假单的信息信息
        getPrintMsg=function(index){
        	var userId=$("#askUserId").text();
        	var selected_node = $('#leave-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            var aflId=selected_node.value.id;
            $.ajax({
            	url:$ctx+"/askForLeave/getquerydetails",
            	type: "GET",
            	data:{
            		"userId": userId,
            		"aflId":aflId 
            	},
            	async: false,
            	success: function(result){
            		console.log(result);
            		userName=result.data.proposer;
            		depName=result.coName;
            		leaveType=result.data.type;
            		leaveReason=result.data.reason;
            		stratTime=result.data.startDate;
            		endTime=result.data.endDate;
            		//id=result.data.id;
            	}
            });
        }
        //普通打印
        printNormal=function(index){
			LODOP=getLodop();
			getPrintMsg(index);
			LODOP.PRINT_INITA(10,10,894,548,"打印控件打印请假条普通打印adad");
			LODOP.ADD_PRINT_SHAPE(2,116,43,660,255,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(1,144,43,660,1,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,116,133,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,116,263,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,116,353,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,116,483,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,116,573,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(1,172,43,660,1,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(0,144,133,1,28,0,1,"#800000");
			LODOP.ADD_PRINT_SHAPE(1,340,43,660,1,0,1,"#800000");
			LODOP.ADD_PRINT_TEXT(30,329,70,30,"请假条");
			LODOP.SET_PRINT_STYLEA(0,"FontSize",15);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(86,44,80,25,"申报时间：");
			LODOP.ADD_PRINT_TEXT(86,122,100,25,"2016-09-10");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(119,42,80,25,"申请人");
			LODOP.ADD_PRINT_TEXT(120,146,109,20,userName);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(121,284,58,20,"部门");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF00FF");
			LODOP.ADD_PRINT_TEXT(120,354,89,23,depName);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(118,500,62,20,"假别");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF00FF");
			LODOP.ADD_PRINT_TEXT(120,601,68,20,leaveType);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(151,46,78,20,"请假时间");
			LODOP.ADD_PRINT_TEXT(150,147,193,19,stratTime+"至"+endTime);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(174,44,79,20,"请假原因：");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF00FF");
			LODOP.ADD_PRINT_TEXT(178,144,544,30,leaveReason);
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.ADD_PRINT_TEXT(348,47,81,20,"主管意见：");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#FF00FF");
			LODOP.ADD_PRINT_TEXT(346,590,110,20,"");
			LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
			LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
			LODOP.PREVIEW();
       }
        
       //套打的数据
        setPrintData=function(){
        	var imgsrc=$ctx+"/images/ip/leave1.png";
            LODOP.PRINT_INITA(-30,10,894,548,"简单套打");
            LODOP.ADD_PRINT_SETUP_BKIMG("<img border='0' src='"+imgsrc+"'>");
            LODOP.SET_SHOW_MODE("BKIMG_WIDTH","201.08mm");
            LODOP.SET_SHOW_MODE("BKIMG_HEIGHT","150.81mm");
            LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW",true);
            LODOP.ADD_PRINT_TEXT(129,116,123,20,"2016-09-26");
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.ADD_PRINT_TEXT(159,156,74,20,userName);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.ADD_PRINT_TEXT(158,386,77,20,depName);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.ADD_PRINT_TEXT(150,609,59,20,leaveType);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.ADD_PRINT_TEXT(187,150,162,20,stratTime+"至"+endTime);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.ADD_PRINT_TEXT(217,132,434,49,leaveReason);
            LODOP.SET_PRINT_STYLEA(0,"FontColor","#0000FF");
            LODOP.SET_SHOW_MODE("HIDE_PAPER_BOARD",1);
        }
        
        //套打
        printTemp=function(index){
        	getPrintMsg(index);
            LODOP=getLodop(); 
            setPrintData();
			LODOP.PREVIEW();
	 	}
        
        //套打维护
        printDesign=function(index){
        	getPrintMsg(index);
            LODOP=getLodop(); 
            setPrintData();
            LODOP.PRINT_SETUP();
        }

      //获得参数信息(hirerId、coId)
        viewModel.getparamInfo=function(){
        	//获得后写到隐藏标签
        	$.ajax({
                url: $ctx + "/askForLeave/getQueryParamInfo",
                type: "GET",
                contentType: "application/json",
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
         	    		viewModel.getsubmitTicket();
         	    	}else{
         	    		//查询当前员工的请假单
                    	viewModel.getCurUncheckTicket(this_val);
         	    	}
                }
            });
        }
        
        
      //获得当前请假单详情
        viewModel.getCurUncheckTicket=function(this_val){

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
	    	
        	
        }
        
        
        
        
        viewModel.getsubmitTicket=function(){
        	
        	//参数只需要userId
        	var askUserId=$("#askUserId").text();
        	
        	//用datatable查询当前部门的请假列表

        	var queryuserData = {};
        	
        	queryuserData["search_EQ_userId"] = askUserId;
        	
        	$.ajax({
        	        	            	url: $ctx + '/askForLeave/getCuruserForm',
        	        	            	type: "GET",
        	        	            	data:queryuserData,
        	        	            	dataType : 'json',
        	        	            	success:function(data){
        	        	            		if(data!=null){
							viewModel.leaveDataTable.setSimpleData(data.data.content);
							viewModel.leaveDataTable.totalPages(data.data.totalPages);
							viewModel.leaveDataTable.totalRow(data.data.totalElements);
						} 
        	        	            	}
        	        	            });    
        	
        	/*
        	viewModel.leaveDataTable.addParams(queryuserData);
        	        	
        	        	app.serverEvent().addDataTable("leaveDataTable", "all").fire({
        	        		url : $ctx + '/evt/dispatch',
        	        		ctrl : 'org.AskForLeaveController',
        	        		method : 'getCurSubmitTicket',
        	        		success : function(data) {
        	        		}
        	        	}); */
        	   	
        }
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            
            //查询当前用户的请假单
            viewModel.getparamInfo();
          
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
