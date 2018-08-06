define([ 'jquery', 'knockout','text!./divisionManage.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','css!./divisionManage.css'],
    function($, ko, template) {
        var viewModelRegion = {
            data : ko.observable({}),
            regionDataTable: new u.DataTable({
                //params:{
                //    "cls" : "com.ufgov.ip.entity.IpMenu"
                //},
                meta: {
                    'theName': {},
                    'theCode': {},
                    'updateDate': {},
                    'theId': {},
                    'parentId': {},
                    'isValid': {},
                    'createDate': {}
                }
            }),
            fatherRegionDataTable: new u.DataTable({
                meta: {
                    'theName': {},
                    'theCode': {},
                    'updateDate': {},
                    'theId': {},
                    'parentId': {},
                    'isValid': {},
                    'createDate': {}
                }
            }),
            dataTable: new u.DataTable({
            	 meta: {
            		    "name": "",
            	        "time": "",
            	        "distance": "",
            	        "currency": "",
            	        "id":"",
            	        "pid":"",
            	        "String": ""
            	      }
            }),
        
            choiceFatherRigionSetting : {
                "callback": {
                    "onClick": function (e, id, node) {
                        $("#father-region").val(node.name);
                        $("#father-region").attr("class",node.id);
                    }   
                }
            }
        };
        
        viewModelRegion.searchRegion = function() {
            var search_name = $("#division-search-text").val();
            var reg_grid = $('#region-grid').parent()[0]['u-meta'].grid;
            var search_region_name = reg_grid.getRowIndexByValue('theName',search_name);
            var search_region_num = reg_grid.getRowIndexByValue('theCode',search_name);
            if(search_region_name >= 0 ) {
                var key_index = search_region_name;
            } else {
                var key_index = search_region_num;
             }
            reg_grid.expandNodeByIndex (key_index);
        }
        
        viewModelRegion.getRegionData = function(){
            /*
             * iuap3.0 datatable请求数据的方法
             */
            var queryData = {};
            var hirerId = $('#user').attr("class");
            var key = 'search_EQ_hirerId';
			queryData[key] = hirerId;
            $.ajax({
            	url: $ctx + '/region/regionByHiredId',
            	type: "GET",
            	data:queryData,
            	dataType : 'json',
            	success:function(data){
            		if(data!=null && data!= undefined){
            			viewModelRegion.dataTable.removeAllRows();
            			viewModelRegion.regionDataTable.setSimpleData(data.cata);
            		}
            	}
            });
        };
        
        regionUpdataTimeFun = function (obj) {
            var updata_time = obj.value;
            if(updata_time != ""){
                updata_time = getLocalTime(updata_time);
            }
            obj.element.innerHTML = '<span>'+ updata_time +'</span>';
        }
        function getLocalTime(time) {
            return new Date(parseInt(time) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
        }
        regionSetFun = function(obj) {
            obj.element.innerHTML = '<a id="'+ obj.rowIndex +'" onclick="editRegion(this.id)" class="region-fun">编辑</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="delRegion(this.id)" class="region-fun">删除</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="addChildRegion(this.id)" class="region-fun">添加下级</a>';
        }
        editRegion = function(index) {
            var selected_node = $('#region-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#region-name").val(selected_node.value.theName);
            $("#region-name").attr("class",selected_node.value.theId);
            $("#region-num").val(selected_node.value.theCode);
            var father_region = selected_node.value.parentId;
            if(father_region){
                var father_region_index = $('#region-grid').parent()[0]['u-meta'].grid.getRowIndexByValue('theId',father_region);
                var father_region_node = $('#region-grid').parent()[0]['u-meta'].grid.getRowByIndex(father_region_index);
                if(father_region_node == undefined) {
                	 $("#father-region").val("无");
                     $("#father-region").attr("class","1111111111");
                } else {
                	 $("#father-region").val(father_region_node.value.theName);
                     $("#father-region").attr("class",father_region_node.value.theId);
                }
            } else {
                $("#father-region").val("默认区划");
                $("#father-region").attr("class","0");
            }
            $("#division-edit-tab").click();
        }
        delRegion = function (index) {
            $("#jump-delete-text").attr("class",index);
            $("#delete-region-notice").modal({backdrop: 'static', keyboard: false});
        }
        viewModelRegion.delete_region_close = function() {
            $("#delete-region-notice").modal("hide");
        }
        viewModelRegion.error_notice_close = function() {
            $("#error-notice").modal("hide");
        }
        viewModelRegion.delete_region = function() {
            var index = $("#jump-delete-text").attr("class");
            var selected_node = $('#region-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            console.log(selected_node);
            var selected_node_id = selected_node.keyValue;
            $.ajax({
                url: $ctx + "/region/delRegion",
                type: "POST",
                dataType: "json",
                data: {
                    theId: selected_node_id
                },
                success: function (data) {
                    if(data.result == "success"){
                        $("#delete-region-notice").modal("hide");
                        $("#save-success-text").text("删除成功");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                        viewModelRegion.getRegionData();
                    } else {
                        $("#delete-region-notice").modal("hide");
                        $("#error-text").text(data.reason);
                        $("#error-notice").modal({backdrop: 'static', keyboard: false});
                    }
                }
            });
        }
        addChildRegion = function(index) {
        	$("#region-name").val("");
        	$("#region-num").val("");
            $("#more-division-info-btn").attr("disabled","disabled");
            var selected_node = $('#region-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#father-region").val(selected_node.value.theName);
            $("#father-region").attr("class",selected_node.value.theId);
            $("#division-edit-tab").click();
        }
        viewModelRegion.saveRegionDataEdit = function() {
            var region_name = $("#region-name").val();
            var region_id = $("#region-name").attr("class");
            var region_num = $("#region-num").val();
            var father_region_id = $("#father-region").attr("class");
            var check_name = viewModelRegion.checkRegionName();
            var check_num = viewModelRegion.checkRegionNum();
            if(check_name && check_num){
                $.ajax({
                    url: $ctx + "/region/saveRegion",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json',
                    data: JSON.stringify({
                        theName: region_name,
                        theId: region_id,
                        theCode: region_num,
                        parentId: father_region_id
                    }),
                    success: function (data) {
                        //Object {result: "fail", reason: "区划编码已存在，请确认！"}
                        if(data.result == "fail"){
                            alert(data.reason);
                        } else {
                            viewModelRegion.getRegionData();
                            viewModelRegion.cancelRegionDataEdit();
                            $("#division-list-tab").click();
                            $("#save-success-text").text("保存成功");
                            $("#save-success-new").css("display","block");
                            $("#save-success-new").fadeOut(3000);
                        }
                    }
                });
            }
        }
        viewModelRegion.cancelRegionDataEdit = function() {
            $("#division-list-tab").click();
            $("#region-name").val("");
            $("#region-name").attr("class","");
            $("#region-num").val("");
            $("#father-region").val("无");
            $("#father-region").attr("class","0");
            $("#name-error").css("display","none");
            $("#num-error").css("display","none");
            $("#num-error-style").css("display","none");
        }
        viewModelRegion.checkRegionName = function() {
            var region_name = $("#region-name").val();
            if(region_name == ""){
                $("#name-error").css("display","inline-block");
                return false;
            } else {
                $("#name-error").css("display","none");
                return true;
            }
        }
        viewModelRegion.checkRegionNum = function() {
            var region_num = $("#region-num").val();
            var reg = /\s/;
            if(region_num == ""){
                $("#num-error").css("display","inline-block");
                return false;
            } else if(reg.exec(region_num) != null || region_num.length != 6){
                $("#num-error").css("display","none");
                $("#num-error-style").css("display","inline-block");
                return false;
            } else {
                $("#num-error").css("display","none");
                $("#num-error-style").css("display","none");
                return true;
            }
        }
        viewModelRegion.closeRegionDataTree = function() {
            $("#father-region-choice-jump").modal('hide');
            $("#father-region").val("无");
            $("#father-region").attr("class","0");
        }
        viewModelRegion.closeRegionData = function() {
            $("#father-region-choice-jump").modal('hide');
        }
        viewModelRegion.getRegionDataTree = function(){
        	 $.ajax({
             	url: $ctx + '/region/region',
             	type: "GET",
             	dataType : 'json',
             	success:function(data){
             		if(data!=null && data!= undefined){
             			viewModelRegion.fatherRegionDataTable.setSimpleData(data.cata);
             		}
             	}
             });
        };
        function getFatherRegionTree(){
            $("#more-division-info-btn").on("click",function() {
                $("#father-region-choice-jump").modal({backdrop: 'static', keyboard: false});
                dragElement(".region-jump-header");
                viewModelRegion.getRegionDataTree();
            })
        }
        viewModelRegion.getChildRegion = function() {
            var rows = $('#region-grid').parent()[0]['u-meta'].grid.getSelectRows();
            $.each(rows, function() {
                var theId = this.theId;
                var theName = this.theName;
                if(this.parentId != ""){
                    var queryData = {};
                    queryData["search_EQ_theId"] = theId;
//                    $.ajax({
//                     	url: $ctx + '/region/region',
//                     	type: "GET",
//                     	data: queryData,
//                     	dataType : 'json',
//                     	success:function(data){
//                     		console.log(data);
//                     		if(data!=null && data!= undefined){
//                     			viewModelRegion.regionDataTable.setSimpleData(data.cata);
//                     		}
//                     	}
//                     });
                    viewModelRegion.regionDataTable.addParams(queryData);
                    app.serverEvent().addDataTable("regionDataTable", "all").fire({
                        url: $ctx + '/evt/dispatch',
                        ctrl: 'base.regionController',
                        method: 'loadRegionDataByFather',
                        success: function (data) {
                            $('#region-grid_content_tbody tr').each(function(){
                                $(this).find("div:first").css({
                                    "text-align":"left",
                                    "padding-left":"10px"
                                });
                            });
                        }
                    });
                }
            });
        }
        //function getChild() {
        //    $('#region-grid_content_tbody').on('click',function(){
        //        viewModelRegion.getChildRegion();
        //    });
        //}
        //viewModel.getHeight = function(){
        //    var allHeight=$("#organizaton-right").height();
        //    $("#organizaton-left").height(allHeight);
        //    $("#organization-container").height(allHeight);
        //};
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelRegion
                }
            );
            viewModelRegion.getRegionData();
            //getChild();
            viewModelRegion.closeRegionDataTree();
            getFatherRegionTree();
        };
       
        return {
            'model' : viewModelRegion,
            'template' : template,
            'init' : init
        };
    }
);
