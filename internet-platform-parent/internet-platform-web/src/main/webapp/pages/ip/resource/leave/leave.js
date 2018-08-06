define([ 'jquery', 'knockout','text!./leave.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','css!./leave.css'],
	function($, ko, template) {
		var asktTicket = {
			data : ko.observable({})
		};
		
		
		
		//获得请假申请人信息并显示
		asktTicket.getProposer = function() {
			//先请求后端获得userid
			$.ajax({
                url: $ctx + "/askForLeave/getProposer",
                type: "GET",
                contentType: "application/json",
                success: function (data) {
                	if(data.userId!=null){
                		$('#proposer').attr("class",data.userId);
                		$('#proposer').text(data.cusername);
                    }else{
                    	$('#proposer').attr("class",data.hirerId);
                    	$('#proposer').text(data.cusername);
                    }
                }
            });
		}
		
		/**
		 * @Id
		private String id;
	
		@Column(name="end_date")
		private String endDate;
	
		private String proposer;
	
		private String reason;
	
		@Column(name="start_date")
		private String startDate;
	
		private int status;
	
		private int type;
	
		private String usid;
		 */
		//提交申请
		asktTicket.submitSupport=function(){
			var proposer=$("#proposer").text();// 用户名
			var startTime=$("#staff-own-askStartTime").val();
			var endTime=$("#staff-own-askEndTime").val();
			var type=$("input[name='qjlx']:checked").val();
			var reason=$("#reason").val();
            var userId=$('#proposer').attr("class");
			var jsonInfo={
		        	"proposer":proposer,
					"startDate":startTime,							
					"endDate":endTime,
				    "type":type,
					"reason":reason,
					"usid":userId
		        };
			
			$.ajax({
			    url: $ctx + "/askForLeave/saveAndStart",
			    type:'POST',
				data:jsonInfo,
				dataType: 'json',
		        success: function (data) {
	             if(data.result=="true"){
	            	 window.location= $ctx +'/#/ip/resource/writtenleave/writtenleave';
	             }else if(data.result=="Enabled"){
	            	
	            	 $("#sure_add_group").modal({backdrop:'static',keyboard:false});
		    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>工作流已经关闭，暂时不能提交请假申请</span> ");
		    			var show_error = document.getElementById("jump-content");
						show_error.style.display='block';
						//window.location= $ctx +'/#/ip/resource/leave/leave';
	            	 
	             }else{
	            	 window.location= $ctx +'/#/ip/resource/leave/leave';
	             }
		        }
		    });
		}
		
		
		jump_btn_close=function(){
        	$("#sure_add_group").modal("hide");
    		var show_error = document.getElementById("jump-content");
    		show_error.style.display='none';
    		$("#sure_add_group").modal("hide");
    		//跳转到查询已审批单据页面 
    		window.location= $ctx +'/#/ip/resource/leave/leave';
        }
		
		
		
		
		
		
		
		var init = function(){
			ko.cleanNode($('.content')[0]);
			app = u.createApp(
					{
						el:'.content',
						model: asktTicket
					}
			);
			asktTicket.getProposer();
			u.compMgr.updateComp();
		};
		
		return {
			'model' : asktTicket,
			'template' : template,
			'init' : init
		};
	}
);
