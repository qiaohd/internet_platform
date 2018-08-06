define([ 'jquery', 'knockout','text!./dataPartition.html','bootstrap', 'uui', 'director','grid','dragJW','css!./dataPartition.css'],
    function($, ko, template) {
		var ctrlBathPath = $ctx+'/partition';
        var viewModelPartition = {
            data : ko.observable({}),
            mainDataTable: new u.DataTable({
                meta: {
                    'data_id': {}, //key-value的id
                    'host': {}, //key-value
                    'port': {}, //标签值
                    'user_name': {}, //枚举类型id
                    'password': {}, //枚举类型
                    'area_name': {}, 
                    'db_driver': {},  
                    'url': {},  
                    'create_date': {},  
                    'update_date': {}  
                },
                // pageIndex:0,
                // pageSize: 10
                
            }),
        };
        viewModelPartition.queryMain = function() {
			var queryData = {};
			// var searchValue = viewModel.searchText();
			// var key = 'search_LIKE_productName';
			// queryData[key] = searchValue;
	        
	        queryData["pageIndex"] = viewModelPartition.mainDataTable.pageIndex();
	        queryData["pageSize"] = viewModelPartition.mainDataTable.pageSize();
			$.ajax({
				type : 'GET',
				url : ctrlBathPath+'/page',
				data : queryData,
				dataType : 'json',
				success : function(result) {
					var data = result.data;
					if(data!=null){
						viewModelPartition.mainDataTable.setSimpleData(data);
					} else {
						
					}
				}
			});
		}
        viewModelPartition.dictionaryTypeSelect = function() {
            $.ajax({
                url: $ctx + "/dic/getDicInfo",
                type: "GET",
                dataType: "json",
                data: {},
                success: function (data) {
                    $("#dictionary-search-type").css("color","");
                    $("#dictionary-search-type").html("");
                    //var please_choice = '<option value="" class="placeholder-type">请选择枚举类型</option>';
                    var please_choice = '<option>请选择枚举类型</option>';
                    $("#dictionary-search-type").append(please_choice);
                    for(var i = 0 ; i < data.length; i++){
                        //var temp = '<option value="'+ data[i].dicId +'">'+ data[i].dicType +'</option>';
                        var temp = '<option>'+ data[i].dicType +'</option>';
                        $("#dictionary-search-type").append(temp);
                    }
                }
            });
        }
        viewModelPartition.searchDictionary = function() {
            var dic_type = $("#dictionary-search-type").val();
            var dic_name = $("#dictionary-search-desc").val();
            if(dic_type == "请选择枚举类型"){
                dic_type = "";
            }
            var queryData = {};
            queryData["search_EQ_dicType"] = dic_type;
            queryData["search_LIKE_dicName"] = dic_name;
            viewModelPartition.dicDataTable.addParams( queryData);
            app.serverEvent().addDataTable("dicDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'base.dicController',
                method: 'loadDictionary',
                success: function (data) {}
            })

        }
        dictionarySetFun = function(obj) {
            obj.element.innerHTML = '</span><span id="'+ obj.rowIndex +'" onclick="delDictionary(this.id)" class="dictionary-fun">删除</span><span class="separator">';
        }
        
        delDictionary = function(index) {
            $("#jump-delete-text").attr("class",index);
            $("#delete-dictionary-notice").modal({backdrop: 'static', keyboard: false});
        }
        viewModelPartition.delete_dictionary_close = function() {
            $("#delete-dictionary-notice").modal("hide");
        }
        viewModelPartition.delete_dictionary = function() {
            var index = $("#jump-delete-text").attr("class");
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            var the_id = selected_node.value.theId;
            var dic_id = selected_node.value.dicId;
            $.ajax({
                url: $ctx + "/dic/delDicInfo",
                type: "POST",
                dataType: "json",
                data: {
                    theId: the_id,
                    dicId: dic_id
                },
                success: function (data) {
                    if(data.result == "success"){
                        $("#delete-dictionary-notice").modal("hide");
                        viewModelPartition.getAllDic();
                        //3秒消失
                        $("#save-success-new").css("display","block");
                        $("#save-success-text").text("删除成功！");
                        $("#save-success-new").fadeOut(3000);
                    } else {
                        $("#delete-dictionary-notice").modal("hide");
                        $("#error-text").text(data.reason);
                        $("#error-notice").modal({backdrop: 'static', keyboard: false});
                    }
                }
            });
        }
        addDictionary = function(index) {
            viewModelPartition.cancelDictionaryEdit();
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#dictionary-type").attr("disabled","disabled");
            $("#dictionary-desc").attr("disabled","disabled");
            $("#dictionary-type").val(selected_node.value.dicType);
            $("#dictionary-type").attr("class",selected_node.value.dicId);
            $("#dictionary-desc").val(selected_node.value.dicName);
            $("#dictionary-edit-tab").click();
        }
        viewModelPartition.checkHost = function() {
            //KValue-error-repeat
            var Key_value = $("#host").val();
            if(Key_value == ""){
                $("#KValue-error").css("display","inline-block");
                return false;
            } else {
                $("#KValue-error").css("display","none");
                return true;
            }
        }
        viewModelPartition.checkDictionaryLabel = function() {
            var Key_value = $("#port").val();
            if(Key_value == ""){
                $("#label-error").css("display","inline-block");
                return false;
            } else {
                $("#label-error").css("display","none");
                return true;
            }
        }
        viewModelPartition.checkDictionaryType = function() {
            //type-error-repeat
            var Key_value = $("#user-name").val();
            if(Key_value == ""){
                $("#type-error").css("display","inline-block");
                return false;
            } else {
                $("#type-error").css("display","none");
                return true;
            }
        }
        viewModelPartition.checkDictionaryDesc = function() {
            var Key_value = $("#password").val();
            if(Key_value == ""){
                $("#desc-error").css("display","inline-block");
                return false;
            } else {
                $("#desc-error").css("display","none");
                return true;
            }
        }
        
        viewModelPartition.checkSchemaName = function() {
            var Key_value = $("#schema-name").val();
            if(Key_value == ""){
                $("#schema-error").css("display","inline-block");
                return false;
            } else {
                $("#schema-error").css("display","none");
                return true;
            }
        }
        // 检查必输入项
        viewModelPartition.checkMustInput = function() {
        	var is_KValue = viewModelPartition.checkHost();
            var is_label =  viewModelPartition.checkDictionaryLabel();
            var is_type = viewModelPartition.checkDictionaryType();
            var id_desc = viewModelPartition.checkDictionaryDesc();
            var schema_name = viewModelPartition.checkSchemaName();
            if(is_KValue && is_label && is_type && id_desc && schema_name){
            	return true;
            }
            
            return false;
		}
        
        viewModelPartition.editInfo =function(){
        	var host = $("#host").val();
            var db_driver = $("#db-driver").val();
            var port = $("#port").val();
            var user_name = $("#user-name").val();
            var password = $("#password").val();
            var area_name = $("#area-name").val();
            
            var resValue = {
            	host: host,
            	dbDriver: db_driver,
            	port: port,
            	userName: user_name,
            	password: password,
            	areaName: area_name
            };
            
            return resValue;
        }
        
        viewModelPartition.savePartitionEdit = function() {
        	
            var infoByjosn = viewModelPartition.editInfo();
           
            var checkFlg = viewModelPartition.checkMustInput();
            
            if(checkFlg) {
                $.ajax({
                    url: ctrlBathPath + "/savePartition",
                    type: "POST",
                    dataType: "json",
                    data: infoByjosn,
                    success: function (data) {
                    	if(data.result == "fail"){
                            alert(data.reason);
                        } else {
                        	viewModelPartition.queryMain();
                            $("#partition-list-tab").click();
                            $("#dictionary-edit :input").each(function () { 
                            	if($(this).attr("id")!="db-driver"){                         		
                            		$(this).val(""); 
                            	}
                            });
                        }
                    }
                });
            }
        }
        
        
        
        viewModelPartition.sendAjaxSuccess = function(data) {
             if(data.result == "success"){
                 $("#save-success-new").css("display","block");
                 $("#save-success-new").fadeOut(3000);
                 viewModelPartition.getAllDic();
                 $("#partition-list-tab").click();
                 viewModelPartition.cancelDictionaryEdit();
             } else {
                 if(data.detailKey != undefined){
                     $("#KValue-error-repeat").text(data.detailKey);
                     $("#KValue-error-repeat").css("display","inline-block");
                 } else {
                     $("#KValue-error-repeat").css("display","none");
                 }
                 if(data.dicType != undefined) {
                     $("#type-error-repeat").text(data.dicType);
                     $("#type-error-repeat").css("display","inline-block");
                 } else {
                     $("#type-error-repeat").css("display","none");
                 }
             }
         }
        viewModelPartition.cancelDictionaryEdit = function() {
            $("#partition-list-tab").click();
            $("#host").val("");
            $("#host").attr("class","");
            $("#dictionary-label").val("");
            $("#dictionary-type").val("");
            $("#dictionary-type").attr("class","");
            $("#dictionary-desc").val("");
            $("#KValue-error").css("display","none");
            $("#label-error").css("display","none");
            $("#type-error").css("display","none");
            $("#desc-error").css("display","none");
            $("#dictionary-type").removeAttr("disabled");
            $("#dictionary-desc").removeAttr("disabled");
            $("#KValue-error-repeat").css("display","none");
            $("#type-error-repeat").css("display","none");
        }
        
        
        viewModelPartition.testPartitionEdit = function() {
        	
        	var infoByjosn = viewModelPartition.editInfo();
            
            var checkFlg = viewModelPartition.checkMustInput();
            
            if(checkFlg) {
            	$.ajax({
                    url: ctrlBathPath + "/testConnection",
                    type: "POST",
                    dataType: "json",
                    data:infoByjosn,
                    success: function (data) {
                    	alert(data.reason);
                    }
                });
            }
		}
        
        viewModelPartition.clearEditPartition = function(){
        	$("#dictionary-edit :input").each(function () { 
            	if($(this).attr("id")!="db-driver"){                         		
            		$(this).val(""); 
            	}
            });
        	
        	$("#dictionary-edit span").each(function () { 
            	if($(this).attr("class")=="error-notice"){                         		
            		$(this).css("display","none");
            	}
            });
        }
        
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelPartition
                }
            );
            viewModelPartition.queryMain();
        };
        return {
            'model' : viewModelPartition,
            'template' : template,
            'init' : init
        };
    }
);
