define([ 'jquery', 'knockout','text!./editDuty.html','uui','tree','css!../newDuty/duty_permission_common.css'],
	function($, ko, template) {
		var app;
		var viewModelEdit = {
			data : ko.observable({}),
			showSubmitNotice: ko.observable(false),
			showEditWran : ko.observable(false),
			showChoiceDutyWran: ko.observable(false),
			dutyNameEdit : ko.observable(""),
			dutyDiscEdit : ko.observable(""),
			menuDataTable: new u.DataTable({
				params:{
					"cls" : "com.ufgov.ip.entity.system.IpMenu"
				},
				meta: {
					'menuId': {},
					'parentMenuId': {},
					'menuName': {},
					'menuDesc': {}
				}
			})
		};
		viewModelEdit.showNewWranCheack  = function(){
			if ($.trim(viewModelEdit.dutyNameEdit()).length <=0) {
				viewModelEdit.showEditWran(true);
	            }else{
				viewModelEdit.showEditWran(false);
	          }  
		};
		viewModelEdit.add_permission_open = function(){
			var choicedDutyEdit = viewModelEdit.menuDataTable.getSelectedDatas();
			if ($.trim(viewModelEdit.dutyNameEdit()).length <=0 || choicedDutyEdit.length <= 0) {
			    if($.trim(viewModelEdit.dutyNameEdit()).length <=0){
					$("#editDutyName").focus();
					viewModelEdit.showNewWranCheack(true);
					viewModelEdit.showChoiceDutyWran(true);
				} else {
					viewModelEdit.showChoiceDutyWran(true);
				}
			}else{
				$('#dialog_add_group_edit').modal({backdrop: 'static', keyboard: false});
				viewModelEdit.showSubmitNotice(true);
			}
		};
		viewModelEdit.add_permission_close = function(){
			$('#dialog_add_group_edit').modal('hide');
			viewModelEdit.showSubmitNotice(false);
		};
		viewModelEdit.getDutyEdit = function(){
			var queryData = {};
			var roleId = $("#editDutyName").attr("class");
			queryData["search_EQ_roleId"] = roleId;
			viewModelEdit.menuDataTable.addParams( queryData);
			var ctx = $("#ctx").val();
			app.serverEvent().addDataTable("menuDataTable", "all").fire({
				url: ctx + '/evt/dispatch',
				ctrl: 'role.RoleController',
				method: 'editPermission',
				success: function (data) {}
			});
		};
		viewModelEdit.saveEditDuty= function(){
			var duty_name = viewModelEdit.dutyNameEdit();
			var duty_disc = viewModelEdit.dutyDiscEdit();
			var roleId = $("#editDutyName").attr("class");
			var queryData = {};
			var getHirerId=$("#hirerId").text();
			queryData["role_roleId"] = roleId;
			queryData["role_roleName"] = duty_name;
			queryData["role_roleDesc"] = duty_disc;
			queryData["role_hirerId"] = getHirerId;
			viewModelEdit.menuDataTable.addParams( queryData);
			var ctx = $("#ctx").val();
			app.serverEvent().addDataTable("menuDataTable", "all").fire({
				url : ctx + '/evt/dispatch',
				ctrl : 'role.RoleController',
				method : 'savePermissionDataTable',
				success : function(data) {
					$('#dialog_add_group_edit').modal('hide');
					data = JSON.parse(data);
					if(data && "success"==data.flag){
						u.showMessageDialog({type:"info",title:"提示信息",msg:data.msg,backdrop:true});
					}
					window.location = $(".nav-pills > li.active>a").attr("href");
				}
			});
		};

		function editDutyInit() {
			ko.cleanNode($('#myTabContent')[0]);
			treeSetting = {
				"check": {
					enable: true,
					chkStyle: 'checkbox',
					chkboxType: { "Y" : "ps", "N" : "ps" }
				},
				"callback": {
				}
			};
			app = u.createApp(
				{
					el:'#myTabContent',
					model: viewModelEdit
				}
			);
			$(".duty-user li").each(function () {
				var cc = $(this).attr("class");
				if (cc == "active") {
					var name = $(this).find("a").text();
					var id = $(this).find("a").attr("id");
					var desc = $(this).find("a").find("input").val();
					viewModelEdit.dutyNameEdit(name);
					$("#editDutyName").attr("class",id);
					viewModelEdit.dutyDiscEdit(desc);
					viewModelEdit.getDutyEdit();
				}
			});
			$(".duty-user li").on("click", function () {
				$(this).addClass("active").siblings().removeClass("active");
				var name = $(this).find("a").text();
				var id = $(this).find("a").attr("id");
				var desc = $(this).find("a").find("input").val();
				viewModelEdit.dutyNameEdit(name);
				$("#editDutyName").attr("class",id);
				viewModelEdit.dutyDiscEdit(desc);
				viewModelEdit.getDutyEdit();
			});
			var father_node = $(".nav-pills > li.active>a").attr("href");
			$('.dutytTitle a:first').attr("href",father_node);
			$('.duty-cancel').attr("href",father_node);
			$('.back-right').attr("href",father_node);
		}
		var init = function(){
			editDutyInit();
		};
		return {
			'model' : viewModelEdit,
			'template' : template,
			'init' : init
		};
	}
);
