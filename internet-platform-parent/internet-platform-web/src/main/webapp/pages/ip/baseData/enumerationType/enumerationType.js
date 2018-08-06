define([ 'jquery', 'knockout','text!./enumerationType.html','bootstrap', 'uui', 'director','grid','dragJW','css!./enumerationType.css'],
    function($, ko, template) {
        var viewModelDictionary = {
            data : ko.observable({}),
            dicDataTable: new u.DataTable({
                //params:{
                //    "cls" : "com.ufgov.ip.entity.IpMenu"
                //},
                meta: {
                    'theId': {}, //key-value的id
                    'detailKey': {}, //key-value
                    'detailInfo': {}, //标签值
                    'dicId': {}, //枚举类型id
                    'dicType': {}, //枚举类型
                    'dicName': {}  //枚举描述
                }
            }),
        };
        viewModelDictionary.getAllDic = function() {
            app.serverEvent().addDataTable("dicDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'base.dicController',
                method: 'loadDictionary',
                success: function (data) {
                    if(data){
                        viewModelDictionary.dictionaryTypeSelect();
                    }
                }
            })
        }
        viewModelDictionary.dictionaryTypeSelect = function() {
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
        viewModelDictionary.searchDictionary = function() {
            var dic_type = $("#dictionary-search-type").val();
            var dic_name = $("#dictionary-search-desc").val();
            if(dic_type == "请选择枚举类型"){
                dic_type = "";
            }
            var queryData = {};
            queryData["search_EQ_dicType"] = dic_type;
            queryData["search_LIKE_dicName"] = dic_name;
            viewModelDictionary.dicDataTable.addParams( queryData);
            app.serverEvent().addDataTable("dicDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'base.dicController',
                method: 'loadDictionary',
                success: function (data) {}
            })

        }
        dictionarySetFun = function(obj) {
            obj.element.innerHTML = '<span id="'+ obj.rowIndex +'" onclick="editDictionary(this.id)" class="dictionary-fun">编辑</span><span class="separator">|</span><span id="'+ obj.rowIndex +'" onclick="delDictionary(this.id)" class="dictionary-fun">删除</span><span class="separator">|</span><span id="'+ obj.rowIndex +'" onclick="addDictionary(this.id)" class="dictionary-fun">添加键值</span>';
        }
        editDictionary = function(index) {
            viewModelDictionary.cancelDictionaryEdit();
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#dictionary-Key-value").val(selected_node.value.detailKey);
            $("#dictionary-Key-value").attr("class",selected_node.value.theId);
            $("#dictionary-label").val(selected_node.value.detailInfo);
            $("#dictionary-type").val(selected_node.value.dicType);
            $("#dictionary-type").attr("class",selected_node.value.dicId);
            $("#dictionary-desc").val(selected_node.value.dicName);
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
                        viewModelDictionary.getAllDic();
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
            viewModelDictionary.cancelDictionaryEdit();
            var selected_node = $('#dictionary-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#dictionary-type").attr("disabled","disabled");
            $("#dictionary-desc").attr("disabled","disabled");
            $("#dictionary-type").val(selected_node.value.dicType);
            $("#dictionary-type").attr("class",selected_node.value.dicId);
            $("#dictionary-desc").val(selected_node.value.dicName);
            $("#dictionary-edit-tab").click();
        }
        viewModelDictionary.checkDictionaryKeyValue = function() {
            //KValue-error-repeat
            var Key_value = $("#dictionary-Key-value").val();
            if(Key_value == ""){
                $("#KValue-error").css("display","inline-block");
                return false;
            } else {
                $("#KValue-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryLabel = function() {
            var Key_value = $("#dictionary-label").val();
            if(Key_value == ""){
                $("#label-error").css("display","inline-block");
                return false;
            } else {
                $("#label-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryType = function() {
            //type-error-repeat
            var Key_value = $("#dictionary-type").val();
            if(Key_value == ""){
                $("#type-error").css("display","inline-block");
                return false;
            } else {
                $("#type-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.checkDictionaryDesc = function() {
            var Key_value = $("#dictionary-desc").val();
            if(Key_value == ""){
                $("#desc-error").css("display","inline-block");
                return false;
            } else {
                $("#desc-error").css("display","none");
                return true;
            }
        }
        viewModelDictionary.saveDictionaryEdit = function() {
            var detail_key = $("#dictionary-Key-value").val();
            var the_id = $("#dictionary-Key-value").attr("class");
            var detail_info = $("#dictionary-label").val();
            var dicType = $("#dictionary-type").val();
            var dic_id = $("#dictionary-type").attr("class");
            var dicName = $("#dictionary-desc").val();
            var is_KValue = viewModelDictionary.checkDictionaryKeyValue();
            var is_label =  viewModelDictionary.checkDictionaryLabel();
            var is_type = viewModelDictionary.checkDictionaryType();
            var id_desc = viewModelDictionary.checkDictionaryDesc();
            if(is_KValue && is_label && is_type && id_desc) {
                $.ajax({
                    url: $ctx + "/dic/saveDic",
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify({
                        detailKey: detail_key,
                        theId: the_id,
                        detailInfo: detail_info,
                        dicType: dicType,
                        dicId: dic_id,
                        dicName: dicName
                    }),
                    success: function (data) {
                        viewModelDictionary.sendAjaxSuccess(data);
                    }
                });
            }
        }
        viewModelDictionary.sendAjaxSuccess = function(data) {
             if(data.result == "success"){
                 $("#save-success-new").css("display","block");
                 $("#save-success-text").text("保存成功！");
                 $("#save-success-new").fadeOut(3000);
                 viewModelDictionary.getAllDic();
                 $("#dictionary-list-tab").click();
                 viewModelDictionary.cancelDictionaryEdit();
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
        viewModelDictionary.cancelDictionaryEdit = function() {
            $("#dictionary-list-tab").click();
            $("#dictionary-Key-value").val("");
            $("#dictionary-Key-value").attr("class","");
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
