define([ 'jquery', 'knockout','text!./ipUserLe123.html','uui','grid','css!./ipUserLe123.css'],
	function($, ko, template) {
		var app,viewModel;
		viewModel = {
			data: ko.observable(""),
			searchData: new u.DataTable({
				meta: ''
			}),
			gridData: new u.DataTable({
				meta:''
			}),
			editData: new u.DataTable({
				meta:''
			}),
			addData: new u.DataTable({
				meta:''
			}),
			comboData:[],
		};
		window.vm = viewModel;
		viewModel.getData = function () {
			$.ajax({
				url: 'gen/genTable/getColForTemplate',
				type: 'GET',
				dataType: 'json',
				data:{
			           'tableName':'ip_user_le'
			        },
				success: function (data){
					$("#search").html("");
					$("#grid").html("");
					$("#add").html("");
					$("#edit").html("");
					var datas = data.genTable.columnList;
					viewModel.datas = datas;
					for(var i = 0;i<datas.length;i++){
						if(datas[i].isPk == "Y"){
							viewModel.PK = datas[i].javaField;
						}
					}
					viewModel.dealDatas(datas);
				}
			})
		};
		viewModel.dealDatas = function(data) {
			viewModel.showSearchGrid(data);
			viewModel.showEdit(data);
			viewModel.showAdd(data);
			console.log(viewModel);
			ko.cleanNode($('.content')[0]);
			app = u.createApp({
				el: '.content',
				model: viewModel
			});
			var search_data = viewModel.searchData;
			var edit_data = viewModel.editData;
			var add_data = viewModel.addData;
			viewModel.initData(search_data,"searchData");
			viewModel.initData(edit_data,"editData");
			viewModel.initData(add_data,"addData");
			$("#edit").css("display","none");
			viewModel.searchInfo();
		};
		viewModel.showSearchGrid = function(data){
			viewModel.search_data = [];
			viewModel.grid_data = [];
			for (var i = 0; i < data.length; i++) {
				if (data[i].isQuery == "Y") {
					viewModel.search_data.push(data[i]);
				}
				if (data[i].isList == "Y") {
					viewModel.grid_data.push(data[i]);
				}
			}
			console.log(viewModel.search_data);
			console.log(viewModel.grid_data);

			//查询区域
			var searchBtn = "<div class='search-common'><button data-bind='click: searchInfo' class='u-button u-button-primary'>查询</button></div>";
			var searchData = viewModel.searchData;
			var data_name = "searchData";
			var search_dom = viewModel.editPage(viewModel.search_data,searchData,data_name);
			search_dom = search_dom + searchBtn;
			$("#search").append(search_dom);

			//表格区域
			var grid_dom = viewModel.showGrid(viewModel.grid_data);
			console.log(grid_dom);
			$("#grid").append(grid_dom);
		};
		viewModel.showEdit = function (data) {
			//编辑区
			viewModel.edit_data = [];
			for(var j = 0;j<data.length;j++){
				if(data[j].isEdit == "Y"){
					viewModel.edit_data.push(data[j]);
				}
			}
			console.log(viewModel.edit_data);
			var editData = viewModel.editData;
			var edit_name = "editData";
			var edit_dom = viewModel.editPage(viewModel.edit_data,editData,edit_name);
			var saveBtn = "<div class='search-common'><button data-bind='click: saveInfoEdit' class='u-button u-button-primary'>保存</button></div>";
			edit_dom = edit_dom + saveBtn;
			$("#edit").html("");
			$("#edit").html(edit_dom);
		};
		viewModel.showAdd = function (data) {
			//添加区
			viewModel.add_data = [];
			for(var j = 0;j<data.length;j++){
				if(data[j].isInsert == "Y"){
					viewModel.add_data.push(data[j]);
				}
			}
			var addData = viewModel.addData;
			var add_name = "addData";
			var add_dom = viewModel.editPage(viewModel.add_data,addData,add_name);
			var saveBtn = "<div class='search-common'><button data-bind='click: saveInfoAdd' class='u-button u-button-primary'>保存</button></div>";
			add_dom = add_dom + saveBtn;
			$("#add").html("");
			$("#add").html(add_dom);
		};
		viewModel.editPage = function(data,flag,flag_name) {
			var meta = '{';
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
			meta += "}";
			flag.meta = JSON.parse(meta);
			flag.data_ar = [];
			flag.select_ar = [];
			flag.radio_ar = [];
			flag.checkbox_ar = [];
			//拼接页面显示文本
			var  innerHTML = "";
			for(var j=0;j<data.length;j++){
				if(data[j].showType){
					if(data[j].showType == 'dateselect'){
						flag.data_ar.push(data[j]);
						innerHTML += "<div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +":</label>";
						innerHTML += "<div class='u-datepicker' u-meta='"+'{"id":"'+data[j].javaField + flag_name +'","type":"u-date","data":"'+ flag_name +'","field":"'+data[j].javaField+'"}'+"'>";
						innerHTML += "<input class='u-input' type='text'>";
						innerHTML += "</div></div>"
					}else if(data[j].showType == 'select'){
						flag.select_ar.push(data[j]);
						innerHTML += "<div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +":</label>";
						innerHTML += "<div id='"+ data[j].javaField + flag_name + "' class='"+ data[j].javaField +" u-combo u-text u-label-floating' u-meta='"+'{"id":"'+data[j].javaField +'","type":"u-combobox","datasource":"comboData","data":"'+ flag_name +'","isAutoTip":true,"field":"'+data[j].javaField+'"}'+ "'>";
						innerHTML += "<input class='u-input'/>";
						innerHTML += "<span class='u-combo-icon'></span>";
						innerHTML += "</div></div>";
					} else if(data[j].showType == 'radiobox'){
						flag.radio_ar.push(data[j]);
						innerHTML += "<div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +":</label>";
						innerHTML += "<div u-meta='"+'{"id":"'+data[j].javaField + flag_name + '","type":"u-radio","data":"'+ flag_name +'","field":"'+ data[j].javaField +'","datasource":"'+ data[j].javaField +'"}' + "'>";
						innerHTML += "<label id='"+ data[j].javaField+"' class='u-radio' >";
						innerHTML += "<input type='radio' class='u-radio-button' name='" + data[j].javaField + flag_name +"'>";
						innerHTML += "<span class='u-radio-label'></span>";
						innerHTML += "</label>";
						innerHTML += "</div></div>";
					} else if(data[j].showType == 'checkbox'){
    					flag.checkbox_ar.push(data[j]);
						innerHTML += "<div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +":</label>";
						innerHTML += "<div u-meta='" + '{"id":"'+data[j].javaField + flag_name + '","type":"u-checkbox","data":"'+ flag_name +'","field":"'+ data[j].javaField +'","datasource":"'+ data[j].javaField +'"}'+"'>";
						innerHTML += "<label class='u-checkbox w-64'>";
						innerHTML += "<input type='checkbox' class='u-checkbox-input'>";
						innerHTML += "<span class='u-checkbox-label'  data-role='name'></span>";
						innerHTML += "</label></div></div>";
					} else if(data[j].showType == 'input'){
						innerHTML += "<div class='search-common clearfix'>";
						innerHTML += "<label>"+ data[j].columnComments +":</label>";
						innerHTML += "<div>";
						// if(data[j].javaField == 'password'){
							// innerHTML += "<input type='password' id='"+ data[j].javaField + flag_name + "'>"
						// } else {
							innerHTML += "<input type='text' id='"+ data[j].javaField + flag_name + "'>";
						// }
						innerHTML += "</div></div>";
					}
				}
			}
			console.log(flag.data_ar);
			console.log(flag.select_ar);
			console.log(flag.radio_ar);
			console.log(flag.checkbox_ar);
			return innerHTML;
		};
		viewModel.showGrid = function (data) {
			var meta = '{';
			for(var j=0;j<data.length;j++){
				meta += '"' + data[j].javaField + '"';
				meta += ":{}";
				if(j < data.length - 1){
					meta += ",";
				}
			}
			meta += "}";
			viewModel.gridData.meta = JSON.parse(meta);
			var innerHTML = "<div type='text' u-meta='" + '{"id":"search-grid","data":"gridData","type":"grid","editable":false,"autoExpand":false}' + "'>";
			for(var i = 0; i < data.length; i++ ){
				if(data[i].showType == "dateselect"){
					innerHTML += "<div options='"+'{"field":"'+ data[i].javaField +'","dataType":"String","title":"'+ data[i].columnComments +'","editType":"string","width": 200}'+"'></div>";
				} else {
					innerHTML += "<div options='"+'{"field":"'+ data[i].javaField +'","dataType":"String","title":"'+ data[i].columnComments +'","editType":"string","width": 150}'+"'></div>";
				}
			}
			innerHTML += "<div options='" + '{"field":"operate","dataType":"String","title":"操作","editType":"string","width": 150,"renderType":"operationFun"}'+"'></div>";
			innerHTML += "</div>";
			return innerHTML;
		};
		operationFun = function(obj) {
			obj.element.innerHTML = '<a id="'+ obj.rowIndex +'" onclick="editFun(this.id)" class="other-fun">编辑</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="delFun(this.id)" class="other-fun">删除</a>';
		};
		editFun = function (index) {
			var r1;
			if(viewModel.editData.getAllDatas().length == 0){
				r1 = viewModel.editData.createEmptyRow();
			}
			r1 = viewModel.editData.getCurrentRow();
			var selected_node = $('#search-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
			console.log(selected_node);
			viewModel.PKValue = selected_node.value[viewModel.PK];
			console.log(viewModel.PKValue);
			var edit_data = viewModel.edit_data;
			console.log(edit_data);
			for(var i=0;i<edit_data.length;i++){
				switch (edit_data[i].showType) {
					case "dateselect":
						var date_set = selected_node.value[edit_data[i].javaField];
						var date = new Date(date_set);
						var data_Data = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
						r1.setValue(edit_data[i].javaField,data_Data);
						break;
					case "select":
						var select_set = selected_node.value[edit_data[i].javaField];
						r1.setValue(edit_data[i].javaField, select_set);
						break;
					case "radiobox":
						var radio_set = selected_node.value[edit_data[i].javaField];
						r1.setValue(edit_data[i].javaField,radio_set);
						break;
					case "checkbox":
						var checkbox_set = selected_node.value[edit_data[i].javaField];
						r1.setValue(edit_data[i].javaField,checkbox_set);
						break;
					case "input":
						var input_set = selected_node.value[edit_data[i].javaField];
						$("#"+ edit_data[i].javaField + "editData").val(input_set);
						break;
				}
			}
			$("#add").css("display","none");
			$("#edit").css("display","block");
			$("#table-add").click();
			$("#table-add").text('业务表编辑');
		};
		delFun = function (index) {
			var selected_node = $('#search-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
			var datas = '{"'+ viewModel.PK+'":"'+ selected_node.value[viewModel.PK] +'"}';
			$.ajax({
				url: "le_code/ipUserLe123/delete",
				type: "POST",
 				dataType: "json",
				contentType: 'application/json',
				data: datas,
				success: function (result) {
					if(result.result == "true"){
						viewModel.searchInfo();
						$("#save-success-text").text(result.reason);
						$("#save-success-new").css("display","block");
						$("#save-success-new").hide(3000);
					} else {
						alert("删除失败，请重新尝试！");
					}
				}
			})
		};
		viewModel.initData = function (flag,flag_name){
			//u.compMgr.updateComp(document.querySelector('#' + select_ar[i].javaField));
			// 设置下拉框
			var select_ar = flag.select_ar;
			var r1 = flag.createEmptyRow();
			for (var i = 0; i < select_ar.length; i++) {
				$.ajax({
					url: "gen/genTable/getColumnSel",
					type: "GET",
					dataType: "json",
					async: false,
					data: {
						"dicType": "",
						"dicName": select_ar[i].dictType
					},
					success: function (result) {
						console.log(result);
						var results = [];
						for(var l= 0;l<result.length;l++){
							var name_value = {
								"name": result[l].detailInfo,
								"value": result[l].detailInfo
							};
							results.push(name_value);
						}
						var combo1Obj = document.getElementById(select_ar[i].javaField +  flag_name)['u.Combo'];
						combo1Obj.setComboData(results);
						// 创建空行，设置默认值
						r1.setValue(select_ar[i].javaField, results[0].value);
					}
				});
			}
			// 设置日历
			var data_ar = flag.data_ar;
			for(var j=0;j< data_ar.length;j++){
				var date = new Date();
				var data_Data = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
				r1.setValue(data_ar[j].javaField,data_Data);
			}
			// 设置radio
			var radio_ar = flag.radio_ar;
			for(var k=0;k< radio_ar.length;k++){
				$.ajax({
					url: "gen/genTable/getColumnSel",
					type: "GET",
					dataType: "json",
					async: false,
					data: {
						"dicType": "",
						"dicName": radio_ar[k].dictType
					},
					success: function (result) {
						console.log(result);
						var results = [];
						for(var l= 0;l<result.length;l++){
							var name_value = {
								"name": result[l].detailInfo,
								"value": result[l].detailInfo
							};
							results.push(name_value);
						}
						console.log(results);
						var combo1Obj = app.getComp(radio_ar[k].javaField+flag_name);
						combo1Obj.setComboData(results);
						r1.setValue(radio_ar[k].javaField,results[0].value);
					}
				});
			}
			// 设置checkbox
			var checkbox_ar = flag.checkbox_ar;
			for(var m=0;m<checkbox_ar.length;m++){
				$.ajax({
					url: "gen/genTable/getColumnSel",
					type: "GET",
					dataType: "json",
					async: false,
					data: {
						"dicType": "",
						"dicName": checkbox_ar[m].dictType
					},
					success: function (result) {
						console.log(result);
						var results = [];
						for(var l= 0;l<result.length;l++){
							var name_value = {
								"name": result[l].detailInfo,
								"value": result[l].detailInfo
							};
							results.push(name_value);
						}
						console.log(results);
						var combo1Obj = app.getComp(checkbox_ar[m].javaField+flag_name);
						combo1Obj.setComboData(results);
						// r1.setValue(checkbox_ar[m].javaField,results[0].value);
					}
				});
			}
		};
		viewModel.getList = function () {
			$(".education > input").css("width","inherit");
			$(".search-common").css({
				"float":"left",
				"margin":"0",
				"margin-bottom":"10px"
			});
			$("#table-add").text('业务表添加');
			$("#add").css("display","block");
			$("#edit").css("display","none");
			viewModel.searchInfo();
			// viewModel.editData.clear();
		};
		viewModel.getFormat = function () {
			$(".education > input").css("width","160px");
			$(".search-common").css({
				"float":"none",
				"margin-left":"30px",
				"margin-top":"10px"
			})
		};
		viewModel.searchInfo = function () {
			var search_datas = viewModel.search_data;
			var current_rows = viewModel.searchData.getCurrentRow();
			console.log(current_rows);
			var datas = {};
			for(var i = 0; i < search_datas.length; i++){
				switch (search_datas[i].showType) {
					case "input":
						if($("#"+ search_datas[i].javaField+"searchData").val()){
							datas[search_datas[i].javaField] = $("#"+ search_datas[i].javaField+"searchData").val();
						} else {
							datas[search_datas[i].javaField] = "";
						}
						break;
					default :
						if(current_rows.data[search_datas[i].javaField].value){
							datas[search_datas[i].javaField] = current_rows.data[search_datas[i].javaField].value;
						} else {
							datas[search_datas[i].javaField] = "";
						}
				}
			}
			console.log(datas);
			$.ajax({
				url: "le_code/ipUserLe123/list",
				type: "POST",
				dataType: "json",
				data: datas,
				success: function (data) {
					console.log(data);
					for(var i=0;i<data.length;i++){
						console.log(data[i].graduatioinTime);
						var num_data = new Date(data[i].graduatioinTime);
						console.log(num_data);
						var data_Data = num_data.getFullYear()+"-"+(num_data.getMonth()+1)+"-"+num_data.getDate();
						console.log(data_Data);
						data[i].graduatioinTime = data_Data;
						console.log(data);
					}
					console.log(data);
					console.log(viewModel.gridData);
					viewModel.gridData.setSimpleData(data);
				}

			});
		};
		viewModel.saveInfoEdit = function () { 
			var current_row_edit = viewModel.editData.getCurrentRow();
			var edit_datas = viewModel.edit_data;
			var datas = {};
			for(var i = 0; i < edit_datas.length; i++){
				switch (edit_datas[i].showType) {
					case "input":
						if($("#"+ edit_datas[i].javaField+"editData").val()){
							datas[edit_datas[i].javaField] = $("#"+ edit_datas[i].javaField+"editData").val();
						} else {
							datas[edit_datas[i].javaField] = "";
						}
						break;
					default :
						if(current_row_edit.data[edit_datas[i].javaField].value){
							datas[edit_datas[i].javaField] = current_row_edit.data[edit_datas[i].javaField].value;
						} else {
							datas[edit_datas[i].javaField] = "";
						}
				}
			}
			datas[viewModel.PK] = viewModel.PKValue;
			console.log(datas);
			$.ajax({
				url: "le_code/ipUserLe123/update",
				type: "POST",
				dataType: "json",
				data: datas,
				success: function (data) {
					console.log(data);
					if(data.result == "true"){
						viewModel.editData.clear();
						$("#table-list").click();
						viewModel.getList();
						$("#save-success-text").text(data.reason);
						$("#save-success-new").css("display","block");
						$("#save-success-new").hide(3000);
					} else {
						alert("更新失败，请重新尝试！");
					}
				}
			});
		};
		viewModel.saveInfoAdd = function () {
			var current_row_add = viewModel.addData.getCurrentRow();
			var add_datas = viewModel.add_data;
			console.log(current_row_add);
			var datas = {};
			for(var i = 0; i < add_datas.length; i++){
				switch (add_datas[i].showType) {
					case "input":
						if($("#"+ add_datas[i].javaField+"addData").val()){
							datas[add_datas[i].javaField] = $("#"+ add_datas[i].javaField+"addData").val();
						} else {
							datas[add_datas[i].javaField] = "";
						}
						break;
					default :
						if(current_row_add.data[add_datas[i].javaField].value){
							datas[add_datas[i].javaField] = current_row_add.data[add_datas[i].javaField].value;
						} else {
							datas[add_datas[i].javaField] = "";
						}
				}
			}
			console.log(datas);
			$.ajax({
				url: "le_code/ipUserLe123/save",
				type: "POST",
				dataType: "json",
				data: datas,
				success: function (data) {
					console.log(data);
					if(data.result == "true"){
						viewModel.addData.clear();
						$("#save-success-text").text(data.reason);
						$("#save-success-new").css("display","block");
						$("#save-success-new").hide(3000);
					} else if(data.result == "disable"){
						alert("工作流已关闭，不能提交表单");
					}else if(data.result == "hirerLogin"){
						alert(data.reason);
					}else{
					    alert("保存失败，请重新尝试！");
					}
					// 设置表格 ajax请求要显示的数据（传grid_data给后台）
					// var grid_data = viewModel.grid_data;
					// console.log(grid_data);
					// viewModel.gridData.setSimpleData(grid_data);
				}
			});
		};
		var init = function(){
			viewModel.getData();
		};
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);