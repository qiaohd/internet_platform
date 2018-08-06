require(['jquery','knockout','uui','tree','grid','jqUi','select2','layer'], function($, ko) {
	var excelRet=$("#resultExc").html();
	if(excelRet!=undefined && excelRet!=null){
		$(".organizaton-right-btns").css("display","none");
		$(".demo-datagrid").css("display","none");
		$("#batch-import-content").css("display","block");
		$(".pagination-container").css("display","none");
		$("#stepOne").css("display","block");
	}
	var flagAccount=false;
	var flagTel=false;
	var flagEmail=false;
	var part_job = false;
	var flag = false;
	var flagNo = false;
	var flagNoEdit = false;
	var oldCoCode = "";
	var app;
	var isEditorUser=false;
	var isEditOrSave="save";
	var viewModel = {
		addDeptName:ko.observable(""),
		editorDeptName:ko.observable(""),
		staffAccount:ko.observable(""),
		staffName:ko.observable(""),
		addNewDeptCode:ko.observable(""),
		editorDeptCode:ko.observable(""),
		staffAccountErrorTip:ko.observable(false),
		staffNameErrorTip:ko.observable(false),
		addDeptNameErrorTip:ko.observable(false),
		addDeptCodeErrorTip:ko.observable(false),
		editorDeptNameErrorTip:ko.observable(false),
		showAddParDeptTree:ko.observable(false),
		showStaffOwndeptTree:ko.observable(false),
		showEditorParDeptTree:ko.observable(false),
		showPartTimedeptTree:ko.observable(false),
		editorDeptCodeErrorTip:ko.observable(false),
		aaa:ko.observable(false),
	
		dataTable1: new u.DataTable({
			meta:{
				'coId' :{
						label:'ID',
						descs :{},
						type:'string'
				},
				'parentCoId' :{
						label:'父ID',
						descs :{},
						type:'string'
				},
				'coName':{
						label:'显示名称',
						descs :{},
						type:'string'
				},
				'coCodeAndName':{
					label:'显示编码名称',
					descs :{},
					type:'string'
				}
			}
		}),
		depInfoData: new u.DataTable({
			meta: {
				columnName: "",
				columnComments: "",
				javaField: "",
				showType: "",
                dictType: "",
				isUse: ""
			}
		}),
		comboData:[],
		depInfoDataMore: new u.DataTable({
			meta:''
		}),
		depInfoDataMoreEdit: new u.DataTable({
			meta:''
		}),
		showType: [],
		showTypeChange: function(obj){
			var Combo = $(obj.element).find('div')[0]['u.Combo'];
			Combo.setComboData(viewModel.showType, null);
		},
		dataTableUser :new u.DataTable({
		    meta: {
		    	'userName'   :{type:'string'},
		    	'userSex'    :{type:'string'},
		    	'coName'     :{type:'string'},
		    	'roleName'   :{type:'string'},
		    	'loginName'  :{type:'string'},
		    	'userEmail'  :{type:'string'},
		    	'userId'     :{type:'string'},
		    	'cellphoneNo':{type:'string'},
		    }
		}),
		menuEvents: {
			afterAdd:function(element, index, data){
	            if (element.nodeType === 1) {
	                u.compMgr.updateComp(element);
	            }
	        },
			beforeEdit: function(id) {
				isEditorUser=true;
				$("#staffTit").text("编辑人员");
				viewModel.dataTableUser.setRowSelect(id);
				viewModel.editoradd = 'edit';
				
				$("#dialog-add-staff").modal({backdrop:'static',keyboard:false});
				$("#p-pwd").hide();
				var getUserId = $(this)[0].data.userId.value;
				$("#showuserId").text(getUserId);
				 var time = new Date();
				$.ajax({
					url:$ctx + "/organization/backToShowUser",
					type:"GET",
		            data:{
		            	"userId": getUserId,
		            	time: time.getTime()
		            },
		            dataType: "json",
					success:function(data){
						if(data.back_user.userSex=="1"){
							$("#sexboy input").prop("checked",true);
						}else if(data.back_user.userSex=="0"){
							$("#sexgirl input").prop("checked",true);
						}
						var loginNames = data.back_user.loginName;
						$("#def-staff-login").val(loginNames);
						//$("#hirerNo").text(data.back_user.hirerNo);
						$("#def-staff-name").val(data.back_user.userName);
						$("#staff-own-dept").val(data.no_p_co[1]);
						$("#nowDept").text(data.no_p_co[1]);
						$("#staffDeptId").val(data.no_p_co[0]);
						if(data.no_p_role==""||data.no_p_role.roleId==""){
							$("#staff-own-job").append("<option selected='selected' value=''>请选择</option>");
							$("#nowDuty").text("");
						}
						
						for(var h=0; h<data.all_duty.length;h++){
							if(data.no_p_role.roleId==data.all_duty[h].roleId){
								$("#staff-own-job").append("<option selected='selected' value='"+data.all_duty[h].roleId+"'>"+data.all_duty[h].roleName+"</option>");
								$("#nowDuty").text(data.all_duty[h].roleName);
								$("#staff-own-job").prepend("<option value=''>请选择</option>");
							}else{
								$("#staff-own-job").append("<option value='"+data.all_duty[h].roleId+"'>"+data.all_duty[h].roleName+"</option>");
							}
						}
						if(data.back_user.education==""){
							$("#staff-own-education").append("<option selected='selected' value=''>请选择</option>");
						}
						for(var g=0; g<data.all_edu.length;g++){
							if(data.back_user.education==data.all_edu[g].detailInfo){
								$("#staff-own-education").append("<option selected='selected' >"+data.all_edu[g].detailInfo+"</option>");
								$("#staff-own-education").prepend("<option value=''>请选择</option>");
							}else{
								$("#staff-own-education").append("<option>"+data.all_edu[g].detailInfo+"</option>");
							}
						}
						$("#staff-own-tel").val(data.back_user.cellphoneNo);
						$("#staff-own-email").val(data.back_user.userEmail);
						$("#zuoji-phone").val(data.back_user.phoneNo);
						$("#fenji-phone").val(data.back_user.extension);
						$("#staff-own-employeeNo").val(data.back_user.employeeNo);
						$("#staff-own-nativePlace").val(data.back_user.nativePlace);
						$("#staff-own-GRADUATE_SCHOOL").val(data.back_user.graduateSchool);
						$("#staff-own-major").val(data.back_user.major);
						$("#staff-own-graduatioinTime").val(data.back_user.graduatioinTime);
						$("#staff-own-remark").val(data.back_user.remark);
						if(data.part_co.length>0){
							for (var j=0; j<data.part_co.length; j++){
								$("#part-time-job table tbody").append("<tr id><td class='no-border-left'><span class='' id>"+data.part_co[j][1]+"</span><a href='javascript:void(0)' class='select-part-time-dept'>*选择</a><span class='hide'>"+data.part_co[j][0]+"<span/></td><td><select id></select></td><td class='no-border-right'><a class='dele-part-time-job'>删除</a></td></tr>");
								for(var s=0; s<data.all_duty.length;s++){
									if(data.p_role[j].roleId==data.all_duty[s].roleId){
										$("#part-time-job table tbody tr select:last").append("<option selected='selected' value='"+data.all_duty[s].roleId+"'>" + data.all_duty[s].roleName + "</option>");
									}else{
										$("#part-time-job table tbody tr select:last").append("<option value='"+data.all_duty[s].roleId+"'>" + data.all_duty[s].roleName + "</option>");
									}
								}
								
								$(".dele-part-time-job").on("click",function(){
									var p_coid = $(this).parent().parent().find("td .select-part-time-dept").next("span").text();
									var delpJob = $(this).parent().parent();
									var p_coroleid = $(this).parent().parent().find("td option:checked").val();
									var time = new Date();
									$.ajax({
										url:$ctx + "/organization/deletePart",
										type:"GET",
							            data:{
							            	"userId": getUserId,
							            	"coId":p_coid,
							            	"roleId":p_coroleid,
							            	"time":time.getTime()
							            },
							            dataType: "json",
										success:function(data){
											if(data.result=="success"){
												delpJob.remove();
											}else if(data.result=="fail"){
												//删除兼职失败
												$("#sure_add_group").modal({backdrop:'static',keyboard:false});
								    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>删除兼职失败</span> ");
								    			var show_error = document.getElementById("jump-content");
												show_error.style.display='block';
											}
										}
									});
								});
									
							}
						}else{
							
						}
					}
				});
				
			},
			viewRowStop: function(id) {
				var getUserIdenbled = $(this)[0].data.userId.value;
				if($("#"+getUserIdenbled+" .1").text()=="[启用]"){
					$.ajax({
						url:$ctx + "/organization/updateUsersIsEnable",
						type:"GET",
						data:{
							"userId":getUserIdenbled,
							"isEnabled":"1"
						},
						dataType: "json",
						success:function(data){
							if(data.result=="success"){
								$("#"+getUserIdenbled+" .1").css("color","#adadad");
								$("#stop").click();
							}else if(data.result=="fail"){
								//启用失败
								$("#sure_add_group").modal({backdrop:'static',keyboard:false});
				    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>启用失败</span> ");
				    			var show_error = document.getElementById("jump-content");
								show_error.style.display='block';
							}
						}
					});
				}
				if($("#"+getUserIdenbled+" .0").text()=="[停用]"){
					$.ajax({
						url:$ctx + "/organization/updateUsersIsEnable",
						type:"GET",
						data:{
							"userId":getUserIdenbled,
							"isEnabled":"0"
						},
						dataType: "json",
						success:function(data){
							if(data.result=="success"){
								$("#"+getUserIdenbled+" .0").css("color","#adadad");
								$("#start").click();
							}else if(data.result=="fail"){
								//停用失败
								$("#sure_add_group").modal({backdrop:'static',keyboard:false});
				    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>停用失败</span> ");
				    			var show_error = document.getElementById("jump-content");
								show_error.style.display='block';
							}
						}
					});
				}
			},
		},
		treeSetting:{
            "check": {
	        "enable": false 
           },
           "callback":{
        	   "onDblClick":function(e, id, node){
        		   if(id=="tree3"){
        			   $("#staffDept").val(node.id);
//	           			var sendnodeValue = {
//	    	           			'coId':	$("#staffDept").val()
//	    	           		};
//	    	           		$.ajax({
//	    	           			url:$ctx + "/organization/preventAddUser",
//	    	    		    	type:"GET",
//	    	    	            data:sendnodeValue,
//	    	    		    	success: function(data){
//	    	    		    		if(data.result=="none"){
	    	    		    			$("#staffDeptId").val($("#staffDept").val());
	    	         				   $("#staff-own-dept").val($("#staff-own-dept-hide").val());
	    	         				   $("#nowDept").text($("#staff-own-dept").val());
	    	         				   viewModel.showStaffOwndeptTree(false);
//	    	    		    		}else if(data.result=="child"){
//	    	    		    			$("#staffDept").val("");
//	    	    		    			$("#staff-own-dept-hide").val("请选择");
//	    	    		    			$("#nowDept").text("");
//	    	    		    			viewModel.showStaffOwndeptTree(true);
//	    	    		    		}
//	    	    		    	}
//	    	           		});
        		   }
        	   },
	           "onClick":function(e, id, node){
	           		$("#adddeptlevelnumcoId").val(node.level);
	           		if(id=="tree1"){
	           			$("#tree1name").val(node.name);
	           			$("#adddeptParentcoId").val(node.id);
	           			$("#staffDepttree1").val(node.id);
	           			$("#tree4coId").val("");
	           			$("#tree4coName").val("");
	    	           	$("#tree1coName").val(node.name);
	           			$("#editdeptcode").val(node.name);
	           			if($("#adddeptlevelnumcoId").val()=="0"||$("#adddeptlevelnumcoId").val()==""){
	           				$("#all-btn-edit").addClass("btn-spe");
	           				flagNo = true;
		           		}else{
		           			$("#all-btn-edit").removeClass("btn-spe");
		           			flagNo = false;
		           		}
	           			if(node.level=="1"){
	           				flagNoEdit=true;
	           			}else{
	           				flagNoEdit=false;
	           			}
	           			$("#query-coId").val(node.id);
	           			if($("#adddeptlevelnumcoId").val()=="0"){
	           				var queryuserDatacoH = {};
		           			var getuserHirerIdh=$("#hirerId").text();
		           			queryuserDatacoH["search_EQ_hirerId"] = getuserHirerIdh; 
		           			queryuserDatacoH["search_EQ_coId"]="";
		           			queryuserDatacoH["search_EQ_isEnabled"]=$("#getStoporStart").val();
		           			viewModel.dataTableUser.addParams( queryuserDatacoH) ;
		           			viewModel.dataTableUser.pageIndex("0");
		           			app.serverEvent().addDataTable("dataTableUser", "all").fire({
		           				url : $ctx + '/evt/dispatch',
		           				ctrl : 'org.OrganController',
		           				method : 'getUsers',
		           				success : function(data) {	
			           				if($("#getStoporStart").val()=="0"){
				           				$(".0").text("[启用]");
				           				$(".0").addClass("1").removeClass("0");
			           				}
		           				}
		           			});
	           			}else{
	           				var queryuserDataco = {};
		           			var getusercoId=$("#query-coId").val();
		           			queryuserDataco["search_EQ_coId"] = getusercoId; 
		           			queryuserDataco["search_EQ_isEnabled"] = $("#getStoporStart").val();
		           			viewModel.dataTableUser.clear();
		           			viewModel.dataTableUser.addParams( queryuserDataco) ;
		           	        viewModel.dataTableUser.pageIndex("0");
		           			app.serverEvent().addDataTable("dataTableUser", "all").fire({
		           				url : $ctx + '/evt/dispatch',
		           				ctrl : 'org.OrganController',
		           				method : 'getUsers',
		           				success : function(data) {	
		           					if($("#getStoporStart").val()=="0"){
				           				$(".0").text("[启用]");
				           				$(".0").addClass("1").removeClass("0");
				           			}
		           				}
		           			});
	           			}
	           		}
	           		if(id=="tree2"){
	           			var part_name=node.name;
	       				var part_id=node.id;
	       				 $(".part_timeJob").prev("span").html(part_name);
	       				 $(".part_timeJob").next("span").html(part_id);
	           		}
	           		if(id=="tree3"){
//	           			$("#staffDept").val(node.id);
//	           			var sendnodeValue = {
//	    	           			'coId':	$("#staffDept").val()
//	    	           		};
//	    	           		$.ajax({
//	    	           			url:$ctx + "/organization/preventAddUser",
//	    	    		    	type:"GET",
//	    	    	            data:sendnodeValue,
//	    	    		    	success: function(data){
//	    	    		    		if(data.result=="none"){
	    	    		    			$("#staffDept").val(node.id);
	    	    		    			$("#staff-own-dept-hide").val(node.name)
//	    	    		    		}else if(data.result=="child"){
//	    	    		    			$("#staffDept").val("");
//	    	    		    			$("#staff-own-dept-hide").val("请选择")
//	    	    		    		}
//	    	    		    	}
//	    	           		});
	           		}
	           		if(id =="tree4"){
	           			if(node.level=="0"){
	           				flagNo=true;
	           			}else{
	           				flagNo=false;
	           			}
	           			$("#tree4coName").val(node.name);
	           			$("#tree4coId").val(node.id);
	           		}
	           		if(id=="tree5"){
	           			if(node.level=="0"){
	           				flagNoEdit=true;
	           			}else{
	           				flagNoEdit=false;
	           			}
	           			$("#addparentId").val(node.id);
	           			$("#checkparentId").val(node.pId);
	           			if($("#editdeptcoId").val()==$("#addparentId").val() || $("#editdeptcoId").val()==$("#checkparentId").val()){
	           				
	           			}else{
	           				$("#editparentId").val(node.id);
	           				$("#edit-parent-dept").val(node.name);
	           			}
	           		}
	           }
           }	
	    },
	};
	window.VM = viewModel;
	viewModel.pageChangeFun = function(pageIndex){
		viewModel.dataTableUser.pageIndex(pageIndex);
		viewModel.getDataTableUser();
	}
	viewModel.sizeChangeFun = function(size){
		viewModel.dataTableUser.pageSize(size);
		viewModel.dataTableUser.pageIndex("0");
		viewModel.getDataTableUser();
	}
	viewModel.getDataTableUser = function () {
		app.serverEvent().addDataTable("dataTableUser", "all").fire({
			url: $ctx + '/evt/dispatch',
			ctrl: 'org.OrganController',
			//async: false,
			method: 'getUsers',
			success: function (data) {}
		});
	}
	viewModel.dragElement = function(element) {
		var mouseStartPoint = {"left":0,"top":  0};
		var mouseEndPoint = {"left":0,"top":  0};
		var mouseDragDown = false;
		var oldP = {"left":0,"top":  0};
		var moveTartet ;
		$(document).ready(function(){
			$(document).on("mousedown",element,function(e){
				if($(e.target).hasClass("close"))//点关闭按钮不能移动对话框
					return;
				mouseDragDown = true;
				moveTartet = $(this).parent();
				mouseStartPoint = {"left":e.clientX,"top":  e.clientY};
				oldP = moveTartet.offset();
			});
			$(document).on("mouseup",function(e){
				mouseDragDown = false;
				moveTartet = undefined;
				mouseStartPoint = {"left":0,"top":  0};
				oldP = {"left":0,"top":  0};
			});
			$(document).on("mousemove",function(e){
				if(!mouseDragDown || moveTartet == undefined)return;
				var mousX = e.clientX;
				var mousY = e.clientY;
				if(mousX < 0)mousX = 0;
				if(mousY < 0)mousY = 25;
				mouseEndPoint = {"left":mousX,"top": mousY};
				var width = moveTartet.width();
				var height = moveTartet.height();
				mouseEndPoint.left = mouseEndPoint.left - (mouseStartPoint.left - oldP.left);//移动修正，更平滑
				mouseEndPoint.top = mouseEndPoint.top - (mouseStartPoint.top - oldP.top);
				moveTartet.offset(mouseEndPoint);
			});
		});
	};
	viewModel.editordept=function(){
		viewModel.editorDeptCodeErrorTip(false);
		if($("#all-btn-edit").attr("class")!="btn-spe"){
			var time = new Date();
			var sendeditValue={
				"coId":$("#adddeptParentcoId").val(),
				"time": time.getTime()
			};
			$.ajax({
				url:$ctx + "/organization/getEditcompny",
		    	type:"GET",
	            data:sendeditValue,
	            dataType: "json",
		    	success: function(data) {
					if(data.columnList){
						var dep_datas = data.columnList;
						var html = viewModel.editPage(dep_datas,viewModel.depInfoDataMoreEdit,"depInfoDataMoreEdit");
						$("#dep-drag-sort-depEdit tbody").html("");
						$("#dep-drag-sort-depEdit tbody").html(html);
						viewModel.initData(viewModel.depInfoDataMoreEdit,"depEdit");
						$("#dep-drag-sort-depEdit").sortable({
							placeholder: "bg" , //拖动时，用css
							cursor: "move",
							items :"tr",
							opacity: 0.6
							//revert: true, //释放时，增加动画
						});
						var r1;
						if(viewModel.depInfoDataMoreEdit.getAllDatas().length == 0){
							r1 = viewModel.depInfoDataMoreEdit.createEmptyRow();
						}
						r1 = viewModel.depInfoDataMoreEdit.getCurrentRow();
						var dep_sort_data = data.sortFieldAndValeList;
						for(var i = 0;i<dep_sort_data.length;i++){
							var dep_position = dep_sort_data[i].indexOf(":");
							var dep_key = dep_sort_data[i].slice(0, dep_position);
							var dep_value = dep_sort_data[i].slice(dep_position+1);
							r1.setValue(dep_key, dep_value);
						}
					} else {
						$("#dep-drag-sort-depEdit tbody").html("");
					}
					var d_type = {
						"0" : "基层预算单位",
						"1" : "主管预算单位",
						"2" : "一级主管部门",
						"3" : "行政区划"
					};
					var is_financial = {
						"0":"否",
						"1":"是"
					};
		    		$("#editparentId").val(data.childIC.parentCoId);
		            $("#editdeptcoId").val(data.childIC.coId);
		            $("#editdeptcode").val(data.childIC.coName);
		            $("#editnewdeptcode").val(data.childIC.coCode);
		            oldCoCode=data.childIC.coCode;
		            $("#editor-dept-desc").val(data.childIC.coDesc);
		            $("#edit-parent-dept").val(data.parentIC.coCodeAndName);
				}
			});
			$("#dialog-editor-dept").modal({backdrop:'static',keyboard:false});
		}
	};
	viewModel.checkDeptName=function(){
    	if($.trim(viewModel.addDeptName()).length<=0){
    		viewModel.addDeptNameErrorTip(true);
    	}else{
    		viewModel.addDeptNameErrorTip(false);
    	}
    },
    viewModel.checkaddNewDeptCode=function(){
    	var sCodes=$.trim(viewModel.addNewDeptCode());
    	var sCode = $.trim(viewModel.addNewDeptCode().replace(/\D/g,''));
    	var reg = /([^\s]+)\s.*/;
    	var pCode = $("#adddeptParentcode").val();
    	var pCodeLength = "";
    	var sCodeStr="";
    	pCode = pCode.replace(reg, "$1");
    	pCodeLength= pCode.length;
    	sCodeStr=sCode.substring(0,pCodeLength);
    	if((sCode.length<=pCodeLength&& !flagNo) || (sCodeStr!=pCode && !flagNo)||sCodes!=sCode){
    		viewModel.addDeptCodeErrorTip(true);
    		viewModel.addNewDeptCode("");
    	}else{
    		viewModel.addDeptCodeErrorTip(false);
    	}
    },
    viewModel.checkEditorDeptName= function(){
		if($.trim($("#editdeptcode").val()).length<=0){
			viewModel.editorDeptNameErrorTip(true);
		}else{
			viewModel.editorDeptNameErrorTip(false);
		}
	};
	viewModel.checkEditorDeptCode= function(){
		var sCodes = $.trim($("#editnewdeptcode").val());
		var sCode = $.trim($("#editnewdeptcode").val().replace(/\D/g,''));
		var reg = /([^\s]+)\s.*/;
		var pCode = $("#edit-parent-dept").val();
		var pCodeLength = "";
		var sCodeStr="";
		pCode = pCode.replace(reg, "$1");
		pCodeLength= pCode.length;
		sCodeStr=sCode.substring(0,pCodeLength);
		if(sCodes!=sCode || (sCode.length<=pCodeLength&&!flagNoEdit) || (sCodeStr!=pCode&&!flagNoEdit)){
			viewModel.editorDeptCodeErrorTip(true);
			$("#editnewdeptcode").val("");
		}else{
			viewModel.editorDeptCodeErrorTip(false);
		}
	};
	/*adddept*/
	viewModel.editPage = function(data,flag,flag_name) {
		var meta = '{';
		if(data!=null){
			for(var i=0;i<data.length;i++){
				meta += '"' + data[i].javaField +'"';
				meta += ":";
				meta += "{";
				if(data[i].showType){
					meta += '"type":"'+data[i].showType+'"';
				}
				if(data[i].dateselect){
					meta += ',"format":"YYYY-MM-DD"';
				}
				meta += "}";
				if(i < data.length - 1){
					meta += ",";
				}
			}
		}
		meta += "}";
		// flag.meta = JSON.parse(meta);
		var str = 'viewModel.' + flag_name +' =new u.DataTable({meta:JSON.parse(meta)});flag = viewModel. ' + flag_name +';';
		flag.data_ar = [];
		flag.select_ar = [];
		flag.radio_ar = [];
		//拼接页面显示文本
		var  innerHTML = "";
		if(data!=null){
			for(var j=0;j<data.length;j++){
				if(data[j].showType){
					if(data[j].showType == 'dateselect'){
						flag.data_ar.push(data[j]);
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div class='u-datepicker' u-meta='"+'{"id":"'+data[j].javaField + flag_name +'","type":"u-date","data":"'+ flag_name +'","field":"'+data[j].javaField+'"}'+"'>";
						innerHTML += "<input class='u-input' type='text'>";
						innerHTML += "</div></div></td></tr>"
					}else if(data[j].showType == 'select'){
						flag.select_ar.push(data[j]);
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div id='"+ data[j].javaField + flag_name + "' class='"+ data[j].javaField +" u-combo u-text u-label-floating' u-meta='"+'{"id":"'+data[j].javaField +'","type":"u-combobox","data":"'+ flag_name +'","isAutoTip":true,"datasource":"comboData","field":"'+data[j].javaField+'"}'+ "'>";
						innerHTML += "<input class='u-input'/>";
						innerHTML += "<span class='u-combo-icon'></span>";
						innerHTML += "</div></div></td></tr>";
					} else if(data[j].showType == 'radiobox'){
						flag.radio_ar.push(data[j]);
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div u-meta='"+'{"id":"'+data[j].javaField + flag_name + '","type":"u-radio","data":"'+ flag_name +'","field":"'+ data[j].javaField +'","datasource":"'+ data[j].javaField +'"}' + "'>";
						innerHTML += "<label id='"+ data[j].javaField+"' class='u-radio' >";
						innerHTML += "<input type='radio' class='u-radio-button' name='" + data[j].javaField + flag_name +"'>";
						innerHTML += "<span class='u-radio-label'></span>";
						innerHTML += "</label>";
						innerHTML += "</div></div></td></tr>";
					} else if(data[j].showType == 'checkbox'){
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div>";
						innerHTML += "<label class='u-checkbox w-64'>";
						innerHTML += "<input type='checkbox' class='u-checkbox-input' id='"+ data[j].javaField + flag_name + "' checked>";
						innerHTML += "<span class='u-checkbox-label'>"+ data[j].javaField +"</span>";
						innerHTML += "</label></div></div></td></tr>";
					} else if(data[j].showType == 'input'){
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div>";
						if(data[j].javaField == 'password'){
							innerHTML += "<input type='password' id='"+ data[j].javaField + flag_name + "'>"
						} else {
							innerHTML += "<input  u-meta='" + '{"id":"'+data[j].javaField + flag_name + '","type":"string","data":"'+ flag_name +'","field":"'+ data[j].javaField +'"}' +"'>";
						}
						innerHTML += "</div></div></td></tr>";
					} else if(data[j].showType == 'textarea'){
						innerHTML += "<tr id='"+ data[j].javaField +"'><td><div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +"：</label>";
						innerHTML += "<div class='u-text'  u-meta='" + '{"id":"'+data[j].javaField + flag_name + '","data":"'+ flag_name +'","field":"'+ data[j].javaField +'","type":"textarea"}'+"'>";
						innerHTML += "<textarea id='"+ data[j].javaField + flag_name + "'></textarea>";
						innerHTML += "</div></div></td></tr>";
					}
				}
			}
		}
		return innerHTML;
	};
	viewModel.initData = function (flag,flag_name){
		//u.compMgr.updateComp(document.querySelector('#' + select_ar[i].javaField));
		// 设置下拉框
		ko.cleanNode($('#dep-drag-sort-'+flag_name)[0]);
		var app = u.createApp({
			el:'#dep-drag-sort-'+flag_name,
			model:viewModel
		});
		var select_ar = flag.select_ar;
		var r1 = flag.createEmptyRow();
		for (var i = 0; i < select_ar.length; i++) {
			$.ajax({
				url: $ctx + "/gen/genTable/getColumnSel",
				type: "GET",
				dataType: "json",
				async: false,
				data: {
					"dicType": "",
					"dicName": select_ar[i].dictType
				},
				success: function (result) {
					var results = [];
					for(var l= 0;l<result.length;l++){
						var name_value = {
							"name": result[l].detailInfo,
							"value": result[l].detailInfo
						};
						results.push(name_value);
					}
					var combo1Obj = app.getComp(select_ar[i].javaField);
					combo1Obj.comp.setComboData(results);
					// 创建空行，设置默认值
					// results[0].value 如果值为空怎么修改
					combo1Obj.setValue(results[0].value);
					combo1Obj.comp.setValue(results[0].value);
					r1.setValue(select_ar[i].javaField, results[0].value);
				}
			});
		}
		// // 设置日历
		// var data_ar = flag.data_ar;
		// for(var j=0;j< data_ar.length;j++){
		// 	var date = new Date();
		// 	var data_Data = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
		// 	r1.setValue(data_ar[j].javaField,data_Data);
		// }
		// // 设置radio
		// var radio_ar = flag.radio_ar;
		// for(var k=0;k< radio_ar.length;k++){
		// 	$.ajax({
		// 		url: "gen/genTable/getColumnSel",
		// 		type: "GET",
		// 		dataType: "json",
		// 		async: false,
		// 		data: {
		// 			"dicType": "",
		// 			"dicName": radio_ar[k].dictType
		// 		},
		// 		success: function (result) {
		// 			var results = [];
		// 			for(var l= 0;l<result.length;l++){
		// 				var name_value = {
		// 					"name": result[l].detailInfo,
		// 					"value": result[l].detailInfo
		// 				};
		// 				results.push(name_value);
		// 			}
		// 			var combo1Obj = app.getComp(radio_ar[k].javaField+flag_name);
		// 			combo1Obj.setComboData(results);
		// 			r1.setValue(radio_ar[k].javaField,results[0].value);
		// 		}
		// 	});
		// }
	};
	/*初始化群组信息*/
	$("#init-data").bind("click",function(){
		var hirerId = $("#hirerId").text();
		$.ajax({
	        type: 'POST',
	        url: $ctx + "/iminitgroup/createGroup",
	        data: {"hirerId": hirerId},
	        dataType: 'json',
	        success: function (result) {
	            if (result.result == "success") {
	            	alert("初始化群组成功！");
	            }
	            else {
	            	alert("初始化群组失败！");
	            }
	        },
	        error: function (xhr) {
	            alert('初始化群组失败，请检测登录用户是否正确或应用服务器网络！');
	        }
	    });
	});
	/*一键同步*/
	var progressbar = "";
	var progressLabel ="";
	function create(){
		progressbar=$( "#progressbar");
		progressLabel=$( ".progress-label");
		progressbar.progressbar({
		     value: false,
		     change: function() {
		       progressLabel.text( progressbar.progressbar( "value" ) + "%" );
		     },
		     complete: function() {
		       //progressLabel.text( "完成！" );
		     }
		   });
	}
    var isSynchronized=false;
    //create();
    function com(result){
	  if(result){
		  $( "#progressbar" ).progressbar({
	        	 value: 100
	         });
		  layer.msg("同步成功");
	  } else if(result=="Enabled"){
		  layer.msg("数据已经同步");
	  }
	  else{
		  alert("一键同步失败！");
	  }
	}
   function progress() {
     var val = progressbar.progressbar( "value" ) || 0;
     progressbar.progressbar( "value", val + 1 );
     if ( val < 95) {
       setTimeout( progress, 100 );
     }else{
     	  if(isSynchronized==true && val>=95){
	 	   setTimeout(com,100);
	  }
   }
   }
  
	$("#one-data").bind("click",function(){
		var index=layer.open({
			area:['200px','20px'],
    		type: 1,
    		shade: 0.4,
    		title: false, //不显示标题
    		content: $('#test')
    	});
		create();
		progress();
		var hirerId = $("#hirerId").text();
		var time = new Date();
		$.ajax({
	        type: 'GET',
	        url: $ctx + "/synchronous/bpm",
	        data: {"hirerId": hirerId,"time": time.getTime()},
	        dataType: 'json',
	        success: function (result) {
	        	isSynchronized=result.result;
	        	com(isSynchronized);
	           	layer.close(index);
	        }/*,
	        error: function (xhr) {
	            alert('一键同步失败，请检测登录用户是否正确或应用服务器网络！');
	        }*/
	    });
	});
	viewModel.adddept=function(){
		viewModel.depInfoDataMore.clear();
		var current_time = new Date();
		$.ajax({
            url: $ctx + '/gen/genTable/getRestColumnProperties',
			type: 'GET',
			dataType: 'json',
			data: {
            	"cT": current_time,
            	"hirerId":$("#hirerId").text(),
            	"tableName":"ip_company"
			},
			success: function (data) {
				var dep_datas = data.ColumnList;
				var html = viewModel.editPage(dep_datas,viewModel.depInfoDataMore,"depInfoDataMore");
				$("#dep-drag-sort-depAdd tbody").html("");
				$("#dep-drag-sort-depAdd tbody").html(html);
				viewModel.initData(viewModel.depInfoDataMore,"depAdd");
               	$("#dep-drag-sort-depAdd").sortable({
                   placeholder: "bg" , //拖动时，用css
                   cursor: "move",
                   items :"tr",
                   opacity: 0.6
                   //revert: true, //释放时，增加动画
               });
				viewModel.showDepSet();
			}
		});
	};
	viewModel.showDepSet = function(){
		viewModel.addDeptCodeErrorTip(false);
		if($("#tree4coId").val()!=""){
			$("#adddeptParentcode").val($("#tree4coName").val());
			$("#adddeptParentcoId").val($("#tree4coId").val());
			$("#dialog-add-dept").modal({backdrop:'static',keyboard:false});
		}else{
			$("#adddeptParentcode").val($("#tree1coName").val());
			$("#adddeptParentcoId").val($("#staffDepttree1").val());
			$("#dialog-add-dept").modal({backdrop:'static',keyboard:false});
		}
	}
	viewModel.adddeptSave=function(){
		viewModel.checkaddNewDeptCode();
		if (($.trim($("#adddeptParentcode").val()) != "") && ($.trim($("#adddeptcode").val()) != "") && ($.trim($("#addNewDeptCode").val().replace(/\D/g, '')) != "")) {
			var d_type = {
				"基层预算单位" : "0",
				"主管预算单位" : "1",
				"一级主管部门" : "2",
				"行政区划"	  : "3"
			};
			var is_financial = {
				"否":"0",
				"是":"1"
			};
			var sendValue={
				"colevelNum":$("#adddeptlevelnumcoId").val(),
				"parentCoId":$("#adddeptParentcoId").val(),
				"hirerId":$("#hirerId").text(),
				"coName":$("#adddeptcode").val(),
				"coCode":$("#addNewDeptCode").val(),
				"coDesc":$("#adddeptDesc").val()
			};
			var field_sort = [];
			var current_rows = viewModel.depInfoDataMore.getCurrentRow();
			var current_rows1 = viewModel.depInfoDataMore.getAllRows();
			var current_rows2 = viewModel.depInfoDataMore.getSimpleData();
			$("#dep-drag-sort-depAdd tbody tr").each(function() {
				var dep_sort_id = $(this).attr("id");
				field_sort.push(dep_sort_id);
				sendValue[dep_sort_id] = current_rows.data[dep_sort_id].value;
			});
			sendValue.fieldSort = JSON.stringify(field_sort);
			$("#adddeptParentcodeError").addClass("hide");
			viewModel.addDeptNameErrorTip(false);
			$.ajax({
				url: $ctx + "/organization/saveOrgInfo",
				type: "POST",
				data: sendValue,
				dataType:"json",
				success: function (data) {
					if (data.result == "success") {
						$("#dialog-add-dept").modal('hide');
						$("#save-success-new").css("display", "block");
						$("#save-success-text").text("保存成功！");
						$("#save-success-new").fadeOut(4000);
						$("#addNewDeptCode").val("");
						$("#adddeptcode").val("");
						$("#adddeptDesc").val("");
						app.serverEvent().addDataTable("dataTable1", "all").fire({
							url: $ctx + '/evt/dispatch',
							ctrl: 'org.OrganController',
							method: 'loadData',
							success: function (data) {
								var ztree = $("#tree1")[0]['u-meta'].tree;
								var root_node = ztree.getNodes();
								var expand_node = $("#adddeptParentcoId").val();
								var node = ztree.getNodeByParam("id", expand_node, null);
								ztree.expandNode(root_node[0], true, false, true);
								ztree.selectNode(node);
								ztree.expandNode(node, true, false, true);
							}
						});
					} else if (data.result == "fail") {
						$("#sure_add_group").modal({backdrop: 'static', keyboard: false});
						$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>" + data.reason + "</span> ");
						var show_error = document.getElementById("jump-content");
						show_error.style.display = 'block';
					}
				}
			});
		} else {
			if ($.trim($("#adddeptParentcode").val()) == "") {
				$("#adddeptParentcodeError").removeClass("hide");
			} else if ($.trim($("#adddeptcode").val()) == "") {
				viewModel.addDeptNameErrorTip(true);
			} else if ($.trim($("#addNewDeptCode").val().replace(/\D/g, '')) == "") {
				viewModel.addDeptCodeErrorTip(true);
				$("#addNewDeptCode").val("");
			}
		}
	};
	//editsave
	viewModel.editor_save_dept_window=function(){
		viewModel.checkEditorDeptCode();
		if(($.trim($("#edit-parent-dept").val())!="") && ($.trim($("#editdeptcode").val())!="")&& ($.trim($("#editnewdeptcode").val().replace(/\D/g,''))!="")){
			var d_type = {
				"基层预算单位" : "0",
				"主管预算单位" : "1",
				"一级主管部门" : "2",
				"行政区划"	  : "3"
			};
			var is_financial = {
				"否":"0",
				"是":"1"
			};
			var sendeditValue={
				"parentCoId":$("#editparentId").val(),
				"coId":$("#editdeptcoId").val(),
				"coCode":$("#editnewdeptcode").val(),
				"hirerId":$("#hirerId").text(),
				"coName":$("#editdeptcode").val(),
				"coDesc":$("#editor-dept-desc").val(),
				"oldCoCode":oldCoCode
			};
			var current_rows = viewModel.depInfoDataMoreEdit.getCurrentRow();
			var field_sort_edit = [];
			$("#dep-drag-sort-depEdit tbody tr").each(function() {
				var dep_sort_id = $(this).attr("id");
				field_sort_edit.push(dep_sort_id);
				sendeditValue[dep_sort_id] = current_rows.data[dep_sort_id].value;
			});
			sendeditValue.fieldSort = JSON.stringify(field_sort_edit);
			$.ajax({
		    	url:$ctx + "/organization/updateOrgInfo",
		    	type:"POST",
	            data:sendeditValue,
	            dataType:"json",
		    	success: function(data){
		    		if(data.result=="success"){
		    			$("#dialog-editor-dept").modal("hide");
		    			viewModel.showEditorParDeptTree(false);
		    			$("#save-success-new").css("display","block");
		    			$("#save-success-text").text("保存成功！");
	            		$("#save-success-new").fadeOut(3000);
	            		app.serverEvent().addDataTable("dataTable1", "all").fire({
	            			url : $ctx + '/evt/dispatch',
	            			ctrl : 'org.OrganController',
	            			method : 'loadData',
	            			success : function(data) {	
	            				var ztree = $("#tree1")[0]['u-meta'].tree;
	            		        var root_node = ztree.getNodes();
	            		        var expand_node = $("#adddeptParentcoId").val();
	            		        var node = ztree.getNodeByParam("id",expand_node,null);
	            		        ztree.expandNode(root_node[0], true, false, true);
	            		        ztree.selectNode(node);
	            		        ztree.expandNode(node, true, false, true);
	            		        $("#tree1coName").val($("#editdeptcode").val());
	            		        $("#tree4coId").val("");
	            		        $("#tree4coName").val("");
	            		        viewModel.getuserFun();
	            			}
	            		});
		    		}else if(data.result=="fail"){
		    			$("#sure_add_group").modal({backdrop:'static',keyboard:false});
		    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+ data.reason +"</span> ");
		    			var show_error = document.getElementById("jump-content");
						show_error.style.display='block';
		    		}
		    	}
		    });
		}else{
        		if($.trim($("#editdeptcode").val())==""){
        			viewModel.editorDeptNameErrorTip(true);
        		}else if($.trim($("#editnewdeptcode").val().replace(/\D/g,''))==""){
        			viewModel.editorDeptCodeErrorTip(true);
        			$("#editnewdeptcode").val("");
        		}
        	} 
	};
	viewModel.checkStaffAccount=function(){
		if($.trim($("#def-staff-login").val()).length<=0){
			viewModel.staffAccountErrorTip(true);
		}else{
			viewModel.staffAccountErrorTip(false);
			var loginName=$.trim($("#def-staff-login").val());
			//var hirerNo=$("#hirerNo").text();
			var userIdCheck = $("#showuserId").text();
			var loginNameNo = loginName;//+hirerNo; 不要在后面加@租户号，同时在保存时做全局校验不重名. by hujf3
			$.ajax({
				url:$ctx + "/organization/checkLoginName",
				type: "GET",
	            dataType: "json",
	            async: false,
	            data:{
	            	"loginName":loginNameNo,
	            	"userId":userIdCheck
	            },
	            success: function (data) {
	            	if(data.result=="success"||data.result=="current"){
	            		$("#checkError").text("");
	            		flagAccount=true;
	            		return;
	            	}else if(data.result=="H_two"){
	            		$("#checkError").text("与租户名重复");
	            		flagAccount=false;
	            		return;
	            	}else if(data.result=="U_two"){
	            		$("#checkError").text("与员工登录名重复");
	            		flagAccount=false;
	            		return;
	            	}
	            }
			});
		}
	};
	viewModel.checkStaffName=function(){
		if($.trim($("#def-staff-name").val()).length<=0){
			viewModel.staffNameErrorTip(true);
			return false;
		}else{
			viewModel.staffNameErrorTip(false);
			return true;
		}
	};
	//15000000000
	viewModel.checkTelString=function(){
		var isTel=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|17[01568][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|145[0-9]{8}|149[0-9]{8})$/;		
		var tel_value=document.getElementById("staff-own-tel").value.trim();
		var userIdCheck = $("#showuserId").text();
		if(tel_value!=""){
			if(isTel.test(tel_value)){
				document.getElementById('labelTelString').innerText="";
				$.ajax({
					url:$ctx + "/organization/checkCellphoneNo",
					type: "GET",
		            dataType: "json",
		            async: false,
		            data:{
		            	"cellphoneNo":tel_value,
		            	"userId":userIdCheck
		            },
		            success: function (data) {
		            	if(data.result=="success"){
		            		document.getElementById('labelTelString').innerText="";
		            		flagTel=true;
		            		return;
		            	}else if(data.result=="H_two"){
		            		document.getElementById('labelTelString').innerText="与租户手机号重复";
		            		$("#labelTelString").css("text-align","left");
		            		flagTel=false;
		            		return;
		            	}else if(data.result=="U_two"){
		            		document.getElementById('labelTelString').innerText="与员工手机号重复";
		            		$("#labelTelString").css("text-align","left");
		            		flagTel=false;
		            		return;
		            	}else if(data.result=="current"){
		            		document.getElementById('labelTelString').innerText="";
		            		flagTel=true;
		            		return;
		            	}
		            }
				});
			}else{
				document.getElementById('labelTelString').innerText="请输入正确手机格式!";
				flagTel=false;
        		return;
			}
		}else{
			document.getElementById('labelTelString').innerText="不能为空！";
			$("#labelTelString").css("text-align","left");
			flagTel=false;
    		return;
		}
		
	};
	//@email
	viewModel.checkEmailString=function(){               	
    	var emailString = document.getElementById("staff-own-email").value;
    	var  myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    	var userIdCheck = $("#showuserId").text();
    	if(emailString!=""){
    		if(myreg.test(emailString)){
    			document.getElementById('labelEmailString').innerText="";
    			$.ajax({
    				url:$ctx + "/organization/checkUserEmail",
    				type: "GET",
    	            dataType: "json",
    	            async: false,
    	            data:{
    	            	"userEmail":emailString,
    	            	"userId":userIdCheck
    	            },
    	            success: function (data) {
    	            	if(data.result=="success"){
    	            		document.getElementById('labelEmailString').innerText="";
    	            		flagEmail=true;
    	            		return;
    	            	}else if(data.result=="H_two"){
    	            		document.getElementById('labelEmailString').innerText="与租户邮箱重复";
    	            		$("#labelEmailString").css("text-align","left");
    	            		flagEmail=false;
    	            		return;
    	            	}else if(data.result=="U_two"){
    	            		document.getElementById('labelEmailString').innerText="与员工邮箱重复";
    	            		$("#labelEmailString").css("text-align","left");
    	            		flagEmail=false;
    	            		return;
    	            	}else if(data.result=="current"){
    	            		document.getElementById('labelEmailString').innerText="";
    	            		flagEmail=true;
    	            		return;
    	            	}
    	            }
    			});
    		}else{
                document.getElementById('labelEmailString').innerText="请输入正确邮箱，如：123@163.com";
                $("#labelEmailString").css("text-align","left");
                flagEmail=false;
                return;
    		}
    	}else{
        	document.getElementById('labelEmailString').innerText="";
        	flagEmail=true;
        	return;
        }
    };
    viewModel.alongDept = function(){
    	if($("#staff-own-dept").val()=="请选择"){
			$("#staff-own-dept-warn").html("所属单位必选！");
			$("#staff-own-dept-warn").css("text-align","left");
			return false;
		}else{
			$("#staff-own-dept-warn").html("");
			return true;
		}
    };
    //点击切换标签
    $(".nav-tabs a").click(function(){
    	if(viewModel.showStaffOwndeptTree()==true){
    		viewModel.close_staff_own_tree_select();
    	}
    });
	//addstaffsave
	viewModel.addstaffSave=function(){
		viewModel.parttimeJob();
		viewModel.checkStaffAccount();
		var checkStaffNameTrue = viewModel.checkStaffName();
		viewModel.checkTelString();
		viewModel.checkEmailString();
		var alongDeptTrue = viewModel.alongDept();
		var userIdedit=$("#showuserId").text();
		var loginName=$.trim($("#def-staff-login").val());
		//var hirerNo=$("#hirerNo").text();
		var password=$.trim($("#def-staff-pwd").text());
		var userSex=$("input[name='staff-sex']:checked").val();
		var userName=$.trim($("#def-staff-name").val());
		var coId=$("#staffDeptId").val();
		var roleId=$("#staff-own-job option:checked").val();
		var duty=$("#staff-own-job option:checked").text();
		if(duty=="请选择"){
			duty="";
		}
		var cellphoneNo=$.trim($("#staff-own-tel").val());
		var userEmail=$.trim($("#staff-own-email").val());
		var phoneNo=$.trim($("#zuoji-phone").val());
		var extension=$.trim($("#fenji-phone").val());
		var employeeNo=$.trim($("#staff-own-employeeNo").val());
		var nativePlace=$.trim($("#staff-own-nativePlace").val());
		var graduateSchool=$.trim($("#staff-own-GRADUATE_SCHOOL").val());
		var major=$.trim($("#staff-own-major").val());
		var graduatioinTime=$("#staff-own-graduatioinTime").val();
		var education=$("#staff-own-education").val();
		if(education=="请选择"){
			education="";
		}
		var remark=$.trim($("#staff-own-remark").val());
		var isTel=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|17[01568][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|145[0-9]{8}|149[0-9]{8})$/;		
		var emailString = document.getElementById("staff-own-email").value;
    	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    	var data="";
    	//var isEditorUser="save";
    	if(isEditorUser==true){
    		isEditOrSave="edit";
    	}
    	if(part_job==false&& flagAccount==true && userName.length>=1 && alongDeptTrue && password!=""&& flagTel==true&&(flagEmail==true||emailString=="")){				
    		$.ajax({
		    	url:$ctx + "/organization/saveUserInfo",
		    	type:"POST",
		    	dataType: "json",
                data:{
                	"userId":userIdedit,
                	"loginName":loginName,//+hirerNo; 不要在后面加@租户号，同时在保存时做全局校验不重名. by hujf3
                	"userName":userName,
					"password":password,							
					"userSex":userSex,
				    "coId":coId,
					"duty":duty,
					"roleId":roleId,
					"cellphoneNo":cellphoneNo,
					"userEmail":userEmail,
					"phoneNo":phoneNo,
					"extension":extension,
					"employeeNo":employeeNo,
					"nativePlace":nativePlace,
					"graduateSchool":graduateSchool,
					"graduatioinTime":graduatioinTime,
					"major":major,
					"education":education,
					"remark":remark,
					"isParttimeInfo":isParttimeInfo,
					"isEditOrSave":isEditOrSave
                },
		    	success: function(data){
		    		if(data.result=="success"){
		    			$("#staffTit").text("添加人员");
		    			$("#dialog-add-staff").modal("hide");
		    			viewModel.showStaffOwndeptTree(false);
		    			$("#save-success-new").css("display","block");
		    			$("#save-success-text").text("保存成功！");
		    			//$("#pwdText").text(password);
	            		$("#save-success-new").fadeOut(6000);
	            		$("#staffDeptId").val("");
	            		$("#staff-own-job").html("");
	            		$("#nowDuty").text("");
	            		$("#staff-own-education").html("");
	            		$("#part-time-container tbody").html("");
	            		//start
	            		staffk();
	            		viewModel.cancel_update_pwd();
	            		staffWarn();
	            		viewModel.getuserFun();
	            		if($("#getStoporStart").val()=="0"){
	           				$(".0").text("[启用]");
	           				$(".0").addClass("1").removeClass("0");
	           			}
	            		//end
		    		}else if(data.result=="taskUnfinished"){
		    			$("#sure_add_group").modal({backdrop:'static',keyboard:false});
		    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+ data.reason +"</span> ");
		    			var show_error = document.getElementById("jump-content");
						show_error.style.display='block';
		    		}else{
		    			$("#sure_add_group").modal({backdrop:'static',keyboard:false});
		    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+ data.reason +"</span> ");
		    			var show_error = document.getElementById("jump-content");
						show_error.style.display='block';
		    		}
		    	}
		    });
		}
	};
	viewModel.parttimeJob=function(){
		var jobTr = $("#part-time-job table tbody tr");
		jobArr = [];
		for(var j=0,len = jobTr.length;j<len;j++){
			if(jobTr.eq(j).find("td .select-part-time-dept").next("span").text()!=""){
				var choiced = 
				{ 
				p_dept_id : jobTr.eq(j).find("td .select-part-time-dept").next("span").text(),	
				p_role_id : jobTr.eq(j).find("td option:checked").val(),
				isPartTime:"1"
				};
				jobArr.push(choiced);
				part_job=false;
			}else{
				part_job=true;
				$("#sure_add_group").modal({backdrop:'static',keyboard:false});
    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>兼职部门不能为空！</span> ");
    			var show_error = document.getElementById("jump-content");
				show_error.style.display='block';
			}
		}
		isParttimeInfo=JSON.stringify(jobArr);
	};
	viewModel.add_part_time_job=function(){
		$("#part-time-job table tbody").append("<tr id><td class='no-border-left'><span class='' id></span><a href='javascript:void(0)' class='select-part-time-dept'>*选择</a><span class='hide'><span/></td><td><select id></select></td><td class='no-border-right'><a class='dele-part-time-job'>删除</a></td></tr>");
		$.ajax({
			url:$ctx + "/organization/getRoleInfo",
			type: "GET",
            dataType: "json",
            success: function (data) {
            	for (var j = 0; j < data.length; j++) {
                    $("#part-time-job table tbody tr select:last").append("<option value='"+data[j].roleId+"'>" + data[j].roleName + "</option>");
                }
            }
		});
		$(".dele-part-time-job").on("click",function(){
			$(this).parent().parent().remove();
		});
	};
	viewModel.jump_btn_close=function(){
		$("#sure_add_group").modal("hide");
		var show_error = document.getElementById("jump-content");
		show_error.style.display='none';
		$("#dialog-editor-dept").modal("hide");
		viewModel.showEditorParDeptTree(false);
	};
	viewModel.show_parent_dept_tree=function(){			
		viewModel.showAddParDeptTree(true);
	};
	viewModel.close_jump_add_dept_window=function(){
		$("#dialog-add-dept").modal('hide');
		viewModel.showAddParDeptTree(false);
		$("#adddeptParentcode").val("");
		$("#tree4coName").val("");
		$("#tree4coId").val("");
		$("#adddeptcode").val("");
		$("#addNewDeptCode").val("");
		$("#adddeptDesc").val("");
		viewModel.addDeptNameErrorTip(false);
		$("#adddeptParentcodeError").addClass("hide");
	};
	viewModel.close_add_parent_dept_tree_select=function(){
		$("#adddeptParentcode").val($("#tree4coName").val());
		$("#adddeptParentcoId").val($("#tree4coId").val());
		viewModel.showAddParDeptTree(false);
	};
	viewModel.close_parent_dept_tree_select=function(){
		viewModel.showAddParDeptTree(false);
	};
	
	viewModel.addstaff=function(){
		$("#man").prop("checked","checked");
		isEditorUser=false;
		isEditOrSave="save";
		$("#p-pwd").css("display","inline-block");
		if($("#staffDepttree1").val()==""){
			$("#staff-own-dept").val("请选择");
		}else{
//		var sendnodeValue = {
//       			'coId':	$("#staffDepttree1").val()
//       		};
//       		$.ajax({
//       			url:$ctx + "/organization/preventAddUser",
//		    	type:"GET",
//	            data:sendnodeValue,
//		    	success: function(data){
//		    		if(data.result=="none"){
		    			$("#staffDeptId").val($("#staffDepttree1").val());
		    			$("#staff-own-dept").val($("#tree1name").val());
		    			$("#nowDept").text($("#tree1name").val());
//		    		}else if(data.result=="child"){
//		    			$("#staffDeptId").val("");
//		    			$("#staff-own-dept").val("请选择");
//		    			$("#nowDept").text("");
//		    		}
//		    	}
//       		});
		}
		$("#dialog-add-staff").modal({backdrop:'static',keyboard:false});
		viewModel.dragElement(".sys-jump-header");
		$.ajax({
			url: $ctx + "/organization/getRoleInfo",
            type: "GET",
            dataType: "json",
            success: function (data) {
            	$("#staff-own-job").append("<option>请选择</option>");
            	$("#staff-own-job option:selected").text(data.roleName);
            	$("#nowDuty").text(data.roleName);
            	for (var i = 0; i < data.length; i++) {
                    $("#staff-own-job").append("<option value="+data[i].roleId+">" + data[i].roleName + "</option>");
                }
            }
		});
		$.ajax({
			url:$ctx + "/organization/getEnumInfo",
			type: "GET",
            dataType: "json",
            success: function (data) {
            	$("#staff-own-education").append("<option>请选择</option>");
            	$("#staff-own-education option:selected").text(data.detailInfo);
            	for (var i = 0; i < data.length; i++) {
                    $("#staff-own-education").append("<option>" + data[i].detailInfo + "</option>");
                }
            }
		});
	};
	//职位同步
		$("#staff-own-job").change(function(){
			$("#nowDuty").text($(this).children('option:selected').text());
		});
	viewModel.select_staff_own_dept=function(){
		viewModel.showStaffOwndeptTree(true);
	};
	viewModel.close_jump_add_staff_window=function(){
		$("#staffTit").text("添加人员");
		$("#dialog-add-staff").modal("hide");
		viewModel.showStaffOwndeptTree(false);
		$("#staffDeptId").val("");
		$("#staff-own-job").html("");
		$("#nowDuty").text("");
		$("#staff-own-job").val("");
		$("#staff-own-education").html("");
		$("#part-time-container tbody").html("");
		staffk();
		viewModel.cancel_update_pwd();
		staffWarn();
		//end
	};
	//addstaff
	function staffWarn(){
		viewModel.staffAccountErrorTip(false);
		viewModel.staffNameErrorTip(false);
		$("#staff-own-dept-warn").html("");
		document.getElementById('labelTelString').innerText="";
		document.getElementById('labelEmailString').innerText="";
	};
	function staffk(){
		//start
		$("#showuserId").text("");
		$("#def-staff-login").val("");
		$("#def-staff-pwd").html("12345678");
		$("#sexboy input").prop("checek",true);
		$("#def-staff-name").val("");
		$("#staff-own-dept").val("请选择");
		$("#nowDept").text("");
		$("#staffDeptId").val("");
		$("#staff-own-job").html("");
		$("#nowDuty").text("");
		$("#staff-own-tel").val("");
		$("#staff-own-email").val("");
		$("#zuoji-phone").val("");
		$("#fenji-phone").val("");
		$("#staff-own-employeeNo").val("");
		$("#staff-own-nativePlace").val("");
		$("#staff-own-GRADUATE_SCHOOL").val("");
		$("#staff-own-major").val("");
		$("#staff-own-graduatioinTime").val("");
		$("#staff-own-education").val("");
		$("#staff-own-remark").val("");
		$("#checkError").text("");
		$("a[href='#add-staff-normal']").parent().addClass("active").siblings().removeClass("active");
		$("#add-staff-normal").addClass("active").siblings().removeClass("active");
	};
	viewModel.cancel_update_pwd=function(){
		$("#passwordWran").text("");
		$("#cancel-update-pwd").css("display","none");
		 $("#save-update-pwd").css("display","none");
		 $(".normal-form input.staff-pwd").css("display","none");
		 $("#def-staff-pwd").css("display","inline-block");
		 $("#update-pwd-a").css("display","inline-block"); 
	};

	viewModel.update_pwd_save=function(){
		if($("#login-staff-pwd").val().length>=8){
			$("#passwordWran").text("");
			$("#def-staff-pwd").text($("#login-staff-pwd").val());
			$("#save-update-pwd").css("display","none");
			$("#cancel-update-pwd").css("display","none");
			$(".normal-form input.staff-pwd").css("display","none");
			$("#def-staff-pwd").css("display","inline-block");
			$("#update-pwd-a").css("display","inline-block");
			
		}
		else{
			$("#passwordWran").text("不能少于8个字符");
		}
	};
    viewModel.addStaffbtnSave=function(){
    	viewModel.showPartTimedeptTree(false);
    	viewModel.showStaffOwndeptTree(false);
    };
    viewModel.save_staff_own_tree_select=function(){
    	if($("#staff-own-dept").val()!="请选择"){
    		$("#staff-own-dept-warn").html("");
    	}
    	if($("#staff-own-dept-hide").val()!=""){
    		$("#staffDeptId").val($("#staffDept").val());
    		$("#staff-own-dept").val($("#staff-own-dept-hide").val());
    		$("#nowDept").text($("#staff-own-dept").val());
    	}
		viewModel.showStaffOwndeptTree(false);
	};
	viewModel.close_staff_own_tree_select=function(){
		viewModel.showStaffOwndeptTree(false);
	};
	//deldeptwran
	viewModel.deldeptwran = function(){
		$("#sure_del_group").modal({backdrop:'static',keyboard:false});
		$("#jump-del-content").css("display","block");
		$("#error_del_group-warn span").text("确定要删除吗？");
		flag=false;
	};
	viewModel.jump_del_btn_close = function(){
		$("#sure_del_group").modal("hide");
		var show_error = document.getElementById("jump-del-content");
		show_error.style.display='none';
		$("#dialog-editor-dept").modal("hide");
		viewModel.showEditorParDeptTree(false);
		flag = false;
		$("#error_del_group-warn span").text("确定要删除吗？");
	};
	//deldeptInfo
	viewModel.deldeptInfo=function(){
		flag;
		//$("#sure_del_group").modal("hide");
		//var show_error = document.getElementById("jump-del-content");
		//show_error.style.display='none';
		var senddelInfo={
				"coId":$("#editdeptcoId").val(),
				"flag":flag
		};
		$.ajax({
	    	url:$ctx + "/organization/delOrgInfo",
	    	type:"GET",
            data:senddelInfo,
	    	success: function(data){
	    		if(data=="true"){
	    			$("#sure_del_group").modal("hide");
	    			var show_error = document.getElementById("jump-del-content");
	    			show_error.style.display='none';
	    			$("#dialog-editor-dept").modal("hide");
	    			$("#adddeptParentcode").val("");
	    			$("#tree1coName").val("");
	    			$("#staff-own-dept").val("请选择");
	    			$("#nowDept").text("");
	    			viewModel.showEditorParDeptTree(false);
	    			$("#save-success-new").css("display","block");
	    			$("#save-success-text").text("删除成功！");
            		$("#save-success-new").fadeOut(3000);
            		$("#all-btn-edit").addClass("btn-spe");
            		app.serverEvent().addDataTable("dataTable1", "all").fire({
            			url : $ctx + '/evt/dispatch',
            			ctrl : 'org.OrganController',
            			method : 'loadData',
            			success : function(data) {	
            				$("#edit-parent-dept").val("");
            				$("#editdeptcode").val("");
            				$("#editor-dept-desc").val("");
            				var ztree = $("#tree1")[0]['u-meta'].tree;
           		         	var root_node = ztree.getNodes();
           		         	ztree.expandNode(root_node[0], true, false, true);
						}
            		});
					$("#tree1>li>a").click();
					$("#error_del_group-warn span").text("确定要删除吗？");
            		flag=false;
	    		}else if(data=="false"){
	    			$("#error_del_group-warn span").text("删除失败，请重试!");
	    			flag=false;
	    			//$("#sure_add_group").modal({backdrop:'static',keyboard:false});
	    			//$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>删除失败，请重试</span> ");
	    			//var show_error = document.getElementById("jump-content");
					//show_error.style.display='block';
	    		}else if(data=="has_son"){
	    			$("#error_del_group-warn span").text("该单位含有子单位是否确定删除？");
	    			flag=true;
	    			//$("#sure_add_group").modal({backdrop:'static',keyboard:false});
	    			//$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>该单位含有子单位是否确定删除？</span> ");
	    			//var show_error = document.getElementById("jump-content");
					//show_error.style.display='block';
	    		}else if(data=="has_employee"){
	    			$("#error_del_group-warn span").text("该单位含有员工是否确定删除？");
	    			flag=true;
	    			//$("#sure_add_group").modal({backdrop:'static',keyboard:false});
	    			//$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>该单位含有员工是否确定删除？</span> ");
	    			//var show_error = document.getElementById("jump-content");
					//show_error.style.display='block';
	    		}
	    	}
	    });
	};
	viewModel.select_editor_parent_dept=function(){
		viewModel.showEditorParDeptTree(true);
	};
	viewModel.close_add_editor_dept_tree=function(){
		viewModel.showEditorParDeptTree(false);
	};
	viewModel.close_editor_dept_tree=function(){
		viewModel.showEditorParDeptTree(false);
	};
	viewModel.close_editor_dept_window=function(){
		$("#dialog-editor-dept").modal("hide");
		$("#editparentId").val("");
        $("#editdeptcoId").val("");
        $("#editdeptcode").val("");
        $("#editor-dept-desc").val("");
        $("#edit-parent-dept").val("");
		viewModel.showEditorParDeptTree(false);
	};
	viewModel.close_select_part_job=function(){
		viewModel.showPartTimedeptTree(false);
		$(".part_timeJob").prev("span").html("");
	};
	viewModel.depInfoSet = function () {
		$("#dep-info-set-window").modal({backdrop:'static',keyboard:false});
		var current_time = new Date();
		$.ajax({
            url: $ctx + '/gen/genTable/getCompanyRestCol',
			type: 'GET',
			dataType: 'json',
			data:{
            	"cT":current_time,
            	"hirerId" : $("#hirerId").text(),
				"tableName":"ip_company"
			},
			success: function (data) {
				viewModel.businessId = data.id;
				viewModel.showType = JSON.parse(viewModel.groupSelectOption(data.config));
				viewModel.depInfoData.removeAllRows();
				viewModel.depInfoData.setSimpleData(data.ColumnList);
			}
		})
	};
    viewModel.groupSelectOption = function (data) {
        var type_text = '[';
        for(var k =0;k < data.length;k++){
            type_text += '{"value":"'+ data[k].value +'","name":"'+ data[k].value +'"}';
            if(k != data.length -1){
                type_text += ',';
            }
        }
        type_text += "]";
        return type_text;
    };
	viewModel.jumpSetClose = function () {
		$("#dep-info-set-window").modal('hide');
	};
	viewModel.saveDeptInfo = function () {
		var rows = viewModel.depInfoData.getAllDatas();
		var new_rows = [];
		for(var i =0;i<rows.length;i++){
			if(rows[i].data.isUse.value == "gen_use") rows[i].data.isUse.value = "N";
			var html =
			{
				columnName : rows[i].data.columnName.value,
				columnComments : rows[i].data.columnComments.value,				
				javaField: rows[i].data.javaField.value,
				showType : rows[i].data.showType.value,
				dictType : rows[i].data.dictType.value,
				isUse: rows[i].data.isUse.value,
				id: rows[i].data.id.value
			};
			new_rows.push(html);
		}
		rows=JSON.stringify(new_rows);
		var current_time = new Date();
		$.ajax({
			url: $ctx + '/gen/genTable/restColumnSave',
			type: 'POST',
			dataType: 'json',
			data: {
				"cT":current_time,
				"id": viewModel.businessId,
				"hirerId" : $("#hirerId").text(),
				"tableName":"ip_company",
				"isGen":"1",
				"column_List":rows
			},
			success: function (data) {
				if(data.result == "false"){
					$('#save_error_group').modal({backdrop: 'static', keyboard: false});
					$("#jump-save-text").text(data.reason);
				} else {
					viewModel.jumpSetClose();
					$("#save-success-text").text("保存成功！");
					$("#save-success-new").css({"display":"block","z-index":"1000"});
					$("#save-success-new").fadeOut(3000);
				}
			}
		})
	};
	$("body").on("click",".select-part-time-dept",function(){
		$(this).parent().parent().siblings().find("td .select-part-time-dept").removeClass("part_timeJob");
		$(this).addClass("part_timeJob");
		viewModel.showPartTimedeptTree(true);
	});
	$("#update-pwd-a").click(function(){
	  $(".normal-form input.staff-pwd").css("display","inline-block");
	  $(this).css("display","none");
	  $("#def-staff-pwd").css("display","none");
	  $("#save-update-pwd").css("display","inline-block");
	  $("#cancel-update-pwd").css("display","inline-block");
	});
	
	app = u.createApp(
		{
			el:'body',
			model: viewModel
		}
	);
//	app.init(viewModel);
	var queryData = {};
	var getHirerId=$("#hirerId").text();
	queryData["search_EQ_hirerId"] = getHirerId;
	viewModel.dataTable1.addParams( queryData) ;
	app.serverEvent().addDataTable("dataTable1", "all").fire({
		url : $ctx + '/evt/dispatch',
		ctrl : 'org.OrganController',
		method : 'loadData',
		success : function(data) {
			var ztree = $("#tree1")[0]['u-meta'].tree;
			var root_node = ztree.getNodes();
			ztree.expandNode(root_node[0], true, false, true);
			// ztree.selectNode(root_node[0]);
			// viewModel.dataTable1.setRowSelect('0');
			// viewModel.dataTable1.setRowSelect(0);
			// $("#tree1_1_a").click();
			$("#tree1>li>a").click();
		}
	});
	//批量导入
	$("#importfilebtn").click(function(){
		$(".organizaton-right-btns").css("display","none");
		$(".demo-datagrid").css("display","none");
		$("#batch-import-content").css("display","block");
		$(".pagination-container").css("display","none");
		$("#stepOne").css("display","block");
		$("#file-upload-btn").attr("disabled",true);
		$("#file-upload-btn").css({"background":"#ccc","color":"#666"});
	});
	$("#uploadfile").change(function(){
    	var fileObject=document.getElementById("uploadfile"); 
    	var errorObject=$("#error"); 
    	var filepath=fileObject.value; 
    	var fileArr=filepath.split("//"); 
    	var fileTArr=fileArr[fileArr.length-1].toLowerCase().split("."); 
    	var filetype=fileTArr[fileTArr.length-1]; 
    	
    	var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
    	var fileSize=0;
    	var size = fileSize / 1024;
    	var filemaxsize = 1024*2;//2M  
    	if (isIE && !fileObject.files) {
	       //var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
	       //var file = fileSystem.GetFile (filePath);
	       //fileSize = file.Size;
    		fileObject.select();
			fileSize = Math.round(filepath.fileSize / 1024 * 100) / 100;
	    } else {
	       fileSize = fileObject.files[0].size;
	    }
    	size = fileSize / 1024;
    	if(filetype!="xls"&&filetype!="xlsx"){
    		fileObject.value="";
    		$("#sure_add_group").modal({backdrop:'static',keyboard:false});
			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>文件格式不正确</span> ");
			var show_error = document.getElementById("jump-content");
			show_error.style.display='block';
    		//("格式不正确");
			return;
    	}else if(size>filemaxsize){ 
    		fileObject.value="";
    		$("#sure_add_group").modal({backdrop:'static',keyboard:false});
			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>附件大小不能大于2M！</span> ");
			var show_error = document.getElementById("jump-content");
			show_error.style.display='block';
    		//("附件大小不能大于2M！"); 
    		return;
    	}else{
    		$("#excelFileUpload").attr("action",$ctx+"/organization/uploadUserInfo");
        	$("#excelFileUpload").submit();
    	}
    	
    });
	$("#file-upload-btn").attr("disabled",false);
	$("#file-upload-btn").css({"background":"#2298ef","color":"#fff"});
    $("#file-upload-btn").click(function(){
    	var time = new Date();
		$.ajax({  
            type:"GET",  //提交方式  
            data: {time: time.getTime()},
            dataType:"json", //数据类型  
            url: $ctx + "/organization/bathImportUserInfo", //请求url  
            success:function(data){ //提交成功的回调函数  
                if(data.result=="success"){
                	$("#stepOne").css("display","none");
                	$("#stepTwo").css("display","none");
                	$("#stepSuc").css("display","block");
                	$("#filerealNum").text(data.realNum);
                }else if(data.result=="format"){
                	$("#sure_add_group").modal({backdrop:'static',keyboard:false});
        			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>文件格式不正确，请下载附件重新上传</span> ");
        			var show_error = document.getElementById("jump-content");
        			show_error.style.display='block';
                	//("文件格式不正确，请下载附件重新上传");
                }
                else if(data.result=="fileNotFound"){
                	$("#sure_add_group").modal({backdrop:'static',keyboard:false});
        			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>还未上传文件，请上传</span> ");
        			var show_error = document.getElementById("jump-content");
        			show_error.style.display='block';
                }else if(data.result=="empty_excel"){
                	$("#sure_add_group").modal({backdrop:'static',keyboard:false});
        			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>此文件为空，请重新上传</span> ");
        			var show_error = document.getElementById("jump-content");
        			show_error.style.display='block';
                } else if(data.result=="fail"){
                	$("#sure_add_group").modal({backdrop:'static',keyboard:false});
        			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+data.reason+"</span> ");
        			var show_error = document.getElementById("jump-content");
        			show_error.style.display='block';
                }else if(data.result=="data_error"){
                	$("#stepOne").css("display","none");
                	$("#stepTwo").css("display","block");
                	$("#validstaff").html(data.valid);
        			$("#invalidstaff").html(data.invalid);
        			for(var i=0; i<=data.error_list.length-1; i++){
        				//姓名、性别
        				var usernameError=data.error_list[i].username;
        				var userSexError=data.error_list[i].userSex;
        				var loginNameError = data.error_list[i].verifyLoginName;
        				var userEmailError = data.error_list[i].verifyUserEmail;
        				var cellphoneNoError = data.error_list[i].verifyCellPhoneNo;
        				var dutyError = data.error_list[i].verifyDuty;
        				var deptError = data.error_list[i].verifyCoName;
        				if(loginNameError!=null||userEmailError!=null||cellphoneNoError!=null||dutyError!=null||deptError!=null){
        					$("#batch-table table tbody").append("<tr><td>"+(i+1)+"</td><td><p>"+ usernameError +"</p><p>"+ userSexError +"</p><p>"+ loginNameError +"</p><p>"+userEmailError +"</p><p>" +cellphoneNoError +"</p><p>"+dutyError +"</p><p>" +deptError+"</p></td></tr>");
        				}
        			}
                	$("#fileuploadNext").click(function(){
                		var time = new Date();
                		if($("#validstaff").text()==0){
                			$("#sure_add_group").modal({backdrop:'static',keyboard:false});
                			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>没有可导入人员，请重新上传</span> ");
                			var show_error = document.getElementById("jump-content");
                			show_error.style.display='block';
                			
                		}else if($("#validstaff").text()>=0){
                			$.ajax({
                        		type:"GET",
                        		data: {time: time.getTime()},
                        		dataType:"json",
                        		url: $ctx + "/organization/bathImportRestUserInfo",
                        		success:function(data){
                        			if(data.result=="success"){
                        				$("#stepOne").css("display","none");
                        				$("#stepTwo").css("display","none");
                                    	$("#stepSuc").css("display","block");
                        				$("#filerealNum").text(data.realNum);
                        			}else if(data.result=="fail"){
                        				$("#sure_add_group").modal({backdrop:'static',keyboard:false});
                            			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+data.reason+"</span> ");
                            			var show_error = document.getElementById("jump-content");
                            			show_error.style.display='block';
                        			}
                        		}
                        	});
                		}
                	});
                } 
            }  
		});
	});
    //返回第一步
    $("#returnOne").click(function(){
    	$("#stepTwo").css("display","none");
    	$("#stepOne").css("display","block");
    	$("#fileNameRet").text("");
    	$("#file-upload-btn").attr("disabled",true);
    	$("#file-upload-btn").css({"background":"#ccc","color":"#666"});
    });
    //批量导入完成，返回第一步
    $("#fileuploadSec").click(function(){
    	$("#stepOne").css("display","block");
		$("#stepTwo").css("display","none");
    	$("#stepSuc").css("display","none");
    	$("#fileNameRet").text("");
    	$("#file-upload-btn").attr("disabled",true);
    	$("#file-upload-btn").css({"background":"#ccc","color":"#666"});
    });
    //搜索人员
    $("#sys-nav-search-btn").click(function(){
    	var getsearchUser = $("#sys-nav-search-text").val();
    	var searchuserData = {};
    	searchuserData["searchs_LIKE_loginName"] = getsearchUser;
    	searchuserData["searchs_EQ_isEnabled"] = $("#getStoporStart").val();
    	var getSearchHirerId=$("#hirerId").text();
    	searchuserData["searchs_EQ_hirerId"] = getSearchHirerId;
    	viewModel.dataTableUser.addParams(searchuserData);
    	app.serverEvent().addDataTable("dataTableUser", "all").fire({
    		url : $ctx + '/evt/dispatch',
    		ctrl : 'org.OrganController',
    		method : 'searchUser',
    		async: false,
    		success : function(data) {
    			if($("#getStoporStart").val()=="0"){
    				$(".0").text("[启用]");
    	        	$(".0").addClass("1").removeClass("0");
    			}
    		}
    	});
    });
    //停用啟用
    $("#stop").click(function(){
    	if($("#adddeptlevelnumcoId").val()=="0"){
    		var querystopData = {};
        	var getstopHirerId=$("#hirerId").text();
        	querystopData["search_EQ_isEnabled"] = "0"; 
        	querystopData["search_EQ_hirerId"] = getstopHirerId; 
        	viewModel.dataTableUser.addParams(querystopData);
        	viewModel.dataTableUser.pageIndex("0");
        	viewModel.getuserFun();
        	//$("#sys-nav-search-btn").click();
        	
        	$(".0").text("[启用]");
        	$(".0").addClass("1").removeClass("0");
    	}else{
    		var querystopData = {};
	    	querystopData["search_EQ_isEnabled"] = "0"; 
	    	querystopData["search_EQ_coId"] = $("#staffDepttree1").val();
	    	viewModel.dataTableUser.addParams( querystopData);
	    	viewModel.dataTableUser.pageIndex("0");
	    	viewModel.getuserFun();
        	//$("#sys-nav-search-btn").click();

	    	$(".0").text("[启用]");
	    	$(".0").addClass("1").removeClass("0");
    	}
    });
    $("#start").click(function(){
    	if($("#adddeptlevelnumcoId").val()=="0"){
    		var querystartData = {};
        	var getstartHirerId=$("#hirerId").text();
        	querystartData["search_EQ_isEnabled"] = "1"; 
        	querystartData["search_EQ_hirerId"] = getstartHirerId; 
        	viewModel.dataTableUser.addParams( querystartData);
        	viewModel.dataTableUser.pageIndex("0");
        	viewModel.getuserFun();
        	//$("#sys-nav-search-btn").click();

        	$(".1").text("[停用]");
        	$(".1").addClass("0").removeClass("1");
    	}else{
    		var querystartData = {};
	    	querystartData["search_EQ_isEnabled"] = "1"; 
	    	querystartData["search_EQ_coId"] = $("#staffDepttree1").val();
	    	viewModel.dataTableUser.addParams( querystartData) ;
	        viewModel.dataTableUser.pageIndex("0");
	    	viewModel.getuserFun();
        	//$("#sys-nav-search-btn").click();
	    	
	    	$(".1").text("[停用]");
	    	$(".1").addClass("0").removeClass("1");
    	}
    });
    $("#organizaton-left-radios input").click(function(){
    	$("#getStoporStart").val($(this).val());
    });
    viewModel.getuserFun = function(){
		  app.serverEvent().addDataTable("dataTableUser", "all").fire({
				url : $ctx + '/evt/dispatch',
				ctrl : 'org.OrganController',
				method : 'getUsers',
				async: false,
				success : function(data) {
					console.log(viewModel.dataTableUser);
				}
			});
	  };
	function banBackSpace(e) {
		var ev = e || window.event;//获取event对象
		var obj = ev.target || ev.srcElement;//获取事件源
		var t = obj.type || obj.getAttribute('type');//获取事件源类型
		//获取作为判断条件的事件类型
		var vReadOnly = obj.getAttribute('readonly');
		//处理null值情况
		vReadOnly = (vReadOnly == "") ? false : vReadOnly;
		//当敲Backspace键时，事件源类型为密码或单行、多行文本的，
		//并且readonly属性为true或enabled属性为false的，则退格键失效
		var flag1 = (ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea") && vReadOnly == "readonly") ? true : false;
		//当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
		var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea") ? true : false;
		//判断
		if (flag2) {
			return false;
		}
		if (flag1) {
			return false;
		}
	}
	//禁止后退键 作用于Firefox、Opera
	document.onkeypress = banBackSpace;
	//禁止后退键  作用于IE、Chrome
	document.onkeydown = banBackSpace;
	window.viewModel = viewModel;
});