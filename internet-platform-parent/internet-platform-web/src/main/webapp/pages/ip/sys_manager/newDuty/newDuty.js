define([ 'jquery','knockout','text!./newDuty.html','uui','css!./duty_permission_common.css','css!./newDuty.css'],
	function($, ko, template) {
		var app;
		ko.cleanNode($('body')[0]);
		var viewModel_duty = {
			data : ko.observable({}),
			showSubmitNotice: ko.observable(false),
			showNewWran : ko.observable(false),
			showChoiceDutyWran: ko.observable(false),
			dutyName : ko.observable(""),
			dutyDisc : ko.observable(""),
			menuDataTable: new u.DataTable({
				params:{
					"cls" : "com.ufgov.ip.entity.system.IpMenu"
				},
				meta: {
					'menuId': {type:'string'},
					'parentMenuId': {type:'string'},
					'menuName': {type:'string'},
					'menuDesc': {type:'string'}
				}
			})
		};
		viewModel_duty.showNewWranCheack = function(){
			var dutyName = $("#dutyName").val();
			if ($.trim(dutyName).length <=0) {
				viewModel_duty.showNewWran(true);
            }else{
				viewModel_duty.showNewWran(false);
          }  
		};
		viewModel_duty.add_permission_open = function(){
			var dutyName = $("#dutyName").val();
			var choicedDutyEdit = viewModel_duty.menuDataTable.getSelectedDatas();
			console.log(viewModel_duty.menuDataTable);
			if ($.trim(dutyName).length <=0 || choicedDutyEdit.length <= 0) {
				if($.trim(dutyName).length <=0){
					$("#dutyName").focus();
					viewModel_duty.showNewWran(true);
					viewModel_duty.showChoiceDutyWran(true);
				} else {
					viewModel_duty.showChoiceDutyWran(true);
				}
			} else {
				$('#dialog_add_group').modal({backdrop: 'static', keyboard: false});
				viewModel_duty.showSubmitNotice(true);
				viewModel_duty.showNewWran(false);
				viewModel_duty.showChoiceDutyWran(false);
			}
		};
		viewModel_duty.add_permission_close = function(){
			$('#dialog_add_group').modal('hide');
			viewModel_duty.showSubmitNotice(false);
		};
		viewModel_duty.save_new_department = function() {
			var duty_name = viewModel_duty.dutyName();
			var duty_disc = viewModel_duty.dutyDisc();
			var queryData = {};
			var getHirerId=$("#hirerId").text();
			queryData["role_roleName"] = duty_name;
			queryData["role_roleDesc"] = duty_disc;
			queryData["role_hirerId"] = getHirerId;
			viewModel_duty.menuDataTable.addParams( queryData);
			var ctx = $("#ctx").val();
			app.serverEvent().addDataTable("menuDataTable", "all").fire({
				url : ctx + '/evt/dispatch',
				ctrl : 'role.RoleController',
				method : 'savePermissionDataTable',
				success : function(data) {
					$('#dialog_add_group').modal('hide');
					//data = JSON.parse(data);
					if(data.flag == "fail"){
						u.showMessageDialog({type:"info",title:"提示信息",msg:data.msg,backdrop:true});
					} else {
						$("#save-success-new").css("display","block");
						$("#save-success-text").text("保存成功！");
						$("#save-success-new").hide(3000);
						window.location = $(".nav-pills > li.active>a").attr("href");
					}
				}
			});
		};
		function showGridAdd() {
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
					model: viewModel_duty
				}
			);
			var ctx = $("#ctx").val();
			app.serverEvent().addDataTable("menuDataTable", "all").fire({
				url : ctx + '/evt/dispatch',
				ctrl : 'role.RoleController',
				method : 'addPermission',
				success : function(data) {}
			});
			var father_node = $(".nav-pills > li.active>a").attr("href");
			$('.dutytTitle a:first').attr("href",father_node);
			$('.duty-cancel').attr("href",father_node);
			$('.back-right').attr("href",father_node);
		}
		var init = function(){
			showGridAdd();
		};
		return {
			'model' : viewModel_duty,
			'template' : template,
			'init' : init
		};
	}
);