define([ 'jquery', 'knockout','text!./solrConfiguration.html','bootstrap', 'uui', 'director','grid','select2','dragJW','css!./solrConfiguration.css'],
    function($, ko, template) {
        var viewModelDictionary = {
            data : ko.observable({}),
            solrConfigDataTable: new u.DataTable({
                //params:{
                //    "cls" : "com.ufgov.ip.entity.IpMenu"
                //},
                meta: {
                    'menuName':{},//菜单名称
                    'catalog': {}, //索引分类
                    'routerAddr': {}, //路由地址
                    'interfaceAddr': {}, //接口地址
                    'interParam': {}, //参数
                    'isUse': {}//是否启用
                }
            }),
        };
        viewModelDictionary.getAllDic = function() {
        	$.ajax({
                url: $ctx+'/indexConfig/getAllIndexConfig',
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                	console.log(data);
                	var data=data.indexConfigList;
                	viewModelDictionary.solrConfigDataTable.setSimpleData(data);
                }
            })
            viewModelDictionary.getAllFirstMenu();
        }
        viewModelDictionary.getAllFirstMenu = function(){
    		$.ajax({
                url: $ctx+'/menuShow/getAllBaseLevelMenu',
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                	var data=data.firstMenuList;
                	$("#shiro-menu-name").html("");
                	for (var i = 0; i < data.length; i++) {
                        $("#shiro-menu-name").append("<option value="+data[i].menuId+">" + data[i].menuName + "</option>");
                    }
                }
            })
    	};
        viewModelDictionary.dictionaryTypeSelect = function() {/*
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
        */}
        viewModelDictionary.searchDictionary = function() {/*
            var dic_type = $("#dictionary-search-type").val();
            var dic_name = $("#dictionary-search-desc").val();
            if(dic_type == "请选择枚举类型"){
                dic_type = "";
            }
            var queryData = {};
            queryData["search_EQ_dicType"] = dic_type;
            queryData["search_LIKE_dicName"] = dic_name;
            viewModelDictionary.solrConfigDataTable.addParams( queryData);
            app.serverEvent().addDataTable("solrConfigDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'org.IndexConfigController',
                method: 'getAllIndexConfig',
                success: function (data) {}
            })

        */}
        dictionarySetFun = function(obj) {
            obj.element.innerHTML = '<span id="'+ obj.rowIndex +'" onclick="editDictionary(this.id)" class="dictionary-fun">编辑</span><span class="separator">|</span><span id="'+ obj.rowIndex +'" onclick="delDictionary(this.id)" class="dictionary-fun">删除</span>';
        }
        editDictionary = function(index) {
            viewModelDictionary.cancelDictionaryEdit();
            $.ajax({
                url: $ctx+'/menuShow/getAllBaseLevelMenu',
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                	var data=data.firstMenuList;
                	$("#shiro-menu-name").html("");
                	for (var i = 0; i < data.length; i++) {
                        $("#shiro-menu-name").append("<option value="+data[i].menuId+">" + data[i].menuName + "</option>");
                    }
                	var ops=document.getElementById("shiro-menu-name");
                    for(var i=0;i<ops.options.length;i++){
                    	if(ops.options[i].text==selected_node.value.menuName){
                    		ops.options[i].setAttribute("selected","true");
                    	}
                    }
                }
            });
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#shiro-menu-name option:selected").text(selected_node.value.menuName);
            $("#solr-classfy").attr("class",selected_node.value.configId);
            $("#solr-classfy").val(selected_node.value.catalog);
            $("#router-address").val(selected_node.value.routerAddr);
            $("#port-address").val(selected_node.value.interfaceAddr);
            $("#port-param-name").val(selected_node.value.interParam);
            $("#"+selected_node.value.isUse+"").prop("checked",true);
            $("#dictionary-edit-tab").click();
        }
        delDictionary = function(index) {
            $("#jump-delete-text").attr("class",index);
            $("#delete-dictionary-notice").modal({backdrop: 'static', keyboard: false});
        }
        viewModelDictionary.delete_dictionary_close = function() {
            $("#delete-dictionary-notice").modal("hide");
        }
        viewModelDictionary.delete_dictionary = function() {
            var index = $("#jump-delete-text").attr("class");
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            var config_id = selected_node.value.configId;
            $.ajax({
                url: $ctx + "/indexConfig/deleteIndexConfig",
                type: "GET",
                dataType: "json",
                data: {
                    "configId": config_id
                },
                success: function (data) {
                	console.log(data);
                    if(data.result == "true"){
                        $("#delete-dictionary-notice").modal("hide");
                        viewModelDictionary.getAllDic();
//                        //3秒消失
                        $("#save-success-text").text("删除成功！");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                    } else {
                        $("#delete-dictionary-notice").modal("hide");
                        $("#error-text").text("系统繁忙，请稍后重试！");
                        $("#error-notice").modal({backdrop: 'static', keyboard: false});
                    }
                }
            });
        }
        viewModelDictionary.checkDictionaryLabel = function() {
            //KValue-error-repeat
            var Key_value = $("#solr-classfy").val();
            if(Key_value == ""){
                $("#label-error").css("display","inline-block");
                return false;
            } else {
                $("#label-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryType = function() {
            var Key_value = $("#router-address").val();
            if(Key_value == ""){
                $("#type-error").css("display","inline-block");
                return false;
            } else {
                $("#type-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryDesc = function() {
            //type-error-repeat
            var Key_value = $("#port-address").val();
            if(Key_value == ""){
                $("#desc-error").css("display","inline-block");
                return false;
            } else {
                $("#desc-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryParam = function() {
            var Key_value = $("#port-param-name").val();
            if(Key_value == ""){
                $("#post-error").css("display","inline-block");
                return false;
            } else {
                $("#post-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.saveDictionaryEdit = function() {
        	var config_id = $("#solr-classfy").attr("class");
        	var menu_name = $("#shiro-menu-name option:selected").text();
        	var menu_id = $("#shiro-menu-name option:selected").val();
        	var catalog = $("#solr-classfy").val();
        	var router_addr = $("#router-address").val();
        	var interface_addr = $("#port-address").val();
        	var inter_param = $("#port-param-name").val();
        	var is_use = $(".solr-radio:checked").attr("id");
            var is_param = viewModelDictionary.checkDictionaryParam();
            var is_label =  viewModelDictionary.checkDictionaryLabel();
            var is_type = viewModelDictionary.checkDictionaryType();
            var id_desc = viewModelDictionary.checkDictionaryDesc();
            if(is_param && is_label && is_type && id_desc) {
                $.ajax({
                    url: $ctx + "/indexConfig/saveIndexConfig",
                    type: "POST",
                    dataType: "json",
                    data: {
                    	"configId":config_id,
                    	"menuName":menu_name,
                    	"menuId":menu_id,
                    	"catalog":catalog,
                    	"isUse":is_use,
                    	"routerAddr":router_addr,
                    	"interParam":inter_param,
                    	"interfaceAddr":interface_addr
                    },
                    success: function (data) {
                    	viewModelDictionary.sendAjaxSuccess(data);
                    }
                });
            }
        }
        viewModelDictionary.sendAjaxSuccess = function(data) {
             if(data.result == "true"){
            	 $("#save-success-text").text("保存成功！");
                 $("#save-success-new").css("display","block");
                 $("#save-success-new").fadeOut(3000);
                 viewModelDictionary.getAllDic();
                 $("#dictionary-list-tab").click();
                 viewModelDictionary.cancelDictionaryEdit();
             } else {
                 alert("系统繁忙，请稍后重试！");
             }
         }
        viewModelDictionary.cancelDictionaryEdit = function() {
            $("#dictionary-list-tab").click();
            $("#shiro-menu-name option:first").prop("selected",true)
            $("#solr-classfy").attr("class","");
            $("#solr-classfy").val("");
            $("#router-address").val("");
            $("#port-address").val("");
            $("#port-param-name").val("");
            $("#Y").prop("checked",true);
            $("#label-error").css("display","none");
            $("#type-error").css("display","none");
            $("#desc-error").css("display","none");
            $("#post-error").css("display","none");
        }
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModelDictionary
                }
            );
            viewModelDictionary.getAllDic();
        };
        return {
            'model' : viewModelDictionary,
            'template' : template,
            'init' : init
        };
    }
);
