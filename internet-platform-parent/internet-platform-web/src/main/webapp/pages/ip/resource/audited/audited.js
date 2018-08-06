define([ 'jquery', 'knockout','text!./audited.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','css!./audited.css'],
    function($, ko, template) {
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
                    'result': {}
                }
            })
        };
        
        
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
                	
                	//查询当前部门经理未审批的请假单
                	viewModel.getCheckTicket();
                	
                }
            });
        }
        
        
      //查询当前部门已审批的请假单
        viewModel.getCheckTicket=function(){
        	//先从隐藏标签中获得hirerId、coId
        	var askHirerId=$("#askHirerId").text();
        	var askCoId=$("#askCoId").text();
        	var askUserId=$("#askUserId").text();
        	var askUserId=$("#askUserId").text();
        	
        	//用datatable查询当前部门的请假列表

        	var queryuserData = {};
        	
        	queryuserData["search_EQ_isEnabled"] = "1"; 
        	queryuserData["search_EQ_hirerId"] = askHirerId; 
        	queryuserData["search_EQ_coId"] = askCoId; 
        	queryuserData["search_EQ_roleName"] = "员工";
        	queryuserData["search_EQ_userId"] = askUserId;
        	
        	$.ajax({
        	        	            	url: $ctx + '/askForLeave/getApproved',
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
        }
        
        
        
        
        

        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            //获得参数信息
            viewModel.getparamInfo();
           // viewModel.mgrAgree();
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
