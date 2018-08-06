define([ 'jquery', 'knockout','text!./planConfigure.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','select2','css!./planConfigure.css'],
    function($, ko, template) {
        var viewModel = {
            data : ko.observable({}),
            planListData: new u.DataTable({
                meta: {
                    schemeName: {
                        type: 'string'
                    },
                    moduleName: {
                        type: 'string'
                    },
                    subModuleName: {
                        type: 'string'
                    },
                    functionName: {
                        type: 'string'
                    },
                    functionAuthor: {
                    type: 'string'
                    }
                }
            }),
            radioData: new u.DataTable({
                meta: {
                    radio:{}
                }
            }),
            radiodata:[{value:'0',name:'Controller/Service/Dao/Enitity/Mapper(包含工作流)'},{value:'1',name:'前端JS'},{value:'2',name:'前后端代码（不包含工作流）'},{value:'3',name:'全部'}]
        };
        viewModel.PlanList = function () {
            $("#work-add").text("生成方案添加");
            viewModel.searchPlan();
        };
        viewModel.planAdd = function () {
            $(".table-edit-text").css("display","block");
            // $("#work-add").click();
            $.ajax({
                url: 'gen/genScheme/form',
                type: 'GET',
                dataType: 'json',
                data: {
                    "id": "",
                    "schemeName": ""
                },
                success: function (data) {
                    var r = viewModel.radioData.createEmptyRow();
                    r.setValue('radio',"0");
                    $("input[name='createChoice']").prop("checked", 'true');
                    $("#tName").select2({
                        language: "zh-CN",
                        theme: "classic"
                    });
                    var html = "";
                    for(var i = 0;i < data.tableList.length;i++){
                        if(data.tableList[i].tableComments != ""){
                            html += "<option value='"+ data.tableList[i].id + "'>"+ data.tableList[i].tableName+":"+ data.tableList[i].tableComments +"</option>"
                        } else {
                            html += "<option value='"+ data.tableList[i].id + "'>"+ data.tableList[i].tableName +"</option>"
                        }
                    }
                    $("#tName").html(html);
                }
            })
        };
        planSetFun = function(obj) {
            obj.element.innerHTML = '<a id="'+ obj.rowIndex +'" onclick="editPlan(this.id)" class="other-fun">编辑</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="delPlan(this.id)" class="other-fun">删除</a>';
        };
        editPlan = function(index) {
            var selected_node = $('#plan-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#work-add").click();
            $("#work-add").text("生成方案编辑");
            $(".table-name").css("display","none");
            $(".table-edit-text").css("display","block");
            var work_id = selected_node.value.id;
            var work_name = selected_node.value.schemeName;
            viewModel.setFormReq(work_id,work_name);
        };
        viewModel.setFormReq = function (work_id,work_name) {
            $.ajax({
                url: 'gen/genScheme/form',
                type: 'GET',
                dataType: 'json',
                data: {
                    "id": work_id,
                    "schemeName": work_name
                },
                success: function (data) {
                    if (data.result == "false") {
                        alert(data.reason);
                    } else {
                        viewModel.setWorkInfo(data);
                    }
                }
            })
        };
        viewModel.setWorkInfo =function (data) {
            var html = "";
            for(var i = 0;i < data.tableList.length;i++){
                if(data.tableList[i].tableComments != ""){
                    html += "<option value='"+ data.tableList[i].id + "'>"+ data.tableList[i].tableName+":"+ data.tableList[i].tableComments +"</option>"
                } else {
                    html += "<option value='"+ data.tableList[i].id + "'>"+ data.tableList[i].tableName +"</option>"
                }
            }
            $("#tName").html(html);
            $("#planName").val(data.genScheme.schemeName);
            $("#planName").attr("class",data.genScheme.id);
            // 模板分类
            viewModel.radioData.setValue('radio',data.genScheme.schemeType);
            // $('input:radio').eq(data.genScheme.schemeType).attr('checked', 'true');
            $("#create-router").val(data.genScheme.packageName);
            $("#create-module-name").val(data.genScheme.moduleName);
            $("#create-child-module-name").val(data.genScheme.subModuleName);
            $("#create-fun-disc").val(data.genScheme.functionName);
            $("#create-fun-name").val(data.genScheme.functionNameSimple);
            $("#create-fun-author").val(data.genScheme.functionAuthor);
            // tName
            $("#tName").val(data.genScheme.genTable.id).trigger("change");
            //checkbox
            console.log(data.genScheme.replaceFile);
            if(data.genScheme.replaceFile == "true"){
                $("input[name='createChoice']").prop("checked", 'true');
            } else {
                $("input[name='createChoice']").removeAttr("checked");
            }
        };
        delPlan = function(index) {
            var selected_node = $('#plan-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $('#error_add_group').modal({backdrop: 'static', keyboard: false});
            var show_error = document.getElementById("delete_error_window");
            show_error.style.display = 'block';
            $("#jump-delete-text").attr("class",selected_node.value.id);
            $("#jump-delete-img").attr("class",selected_node.value.tableName);
        };
        viewModel.close_jump_window_delete = function () {
            var show_content_delete = document.getElementById("delete_error_window");
            $('#error_add_group').modal('hide');
            show_content_delete.style.display = 'none';
        };
        viewModel.delete_plan = function() {
            var delete_id = $("#jump-delete-text").attr("class");
            var delete_name = $("#jump-delete-img").attr("class");
            $.ajax({
                url: 'gen/genScheme/delete',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    "delFlag": '1',
                    "id": delete_id,
                    "tableName": delete_name
                }),
                success: function (data) {
                    viewModel.close_jump_window_delete();
                    $("#save-success-text").text("删除成功！");
                    $("#save-success-new").css("display","block");
                    $("#save-success-new").fadeOut(3000);
                    viewModel.searchPlan();
                }
            })
        };
        viewModel.searchPlan = function () {
            var tableName = $("#tableName").val();
            $.ajax({
                url: 'gen/genScheme/list',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    schemeName: tableName
                }),
                success: function (data) {
                    viewModel.planListData.setSimpleData(data);
                }
            })
        };
        viewModel.workSave = function (flag) {
            var scheme_type = viewModel.radioData.getValue('radio');
            $.ajax({
                url: 'gen/genScheme/save',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    flag : flag,
                    category : "curd",
                    schemeName : $("#planName").val(),
                    id : $("#planName").attr("class"),
                    schemeType : scheme_type,
                    packageName : $("#create-router").val(),
                    moduleName : $("#create-module-name").val(),
                    subModuleName : $("#create-child-module-name").val(),
                    functionName : $("#create-fun-disc").val(),
                    functionNameSimple : $("#create-fun-name").val(),
                    functionAuthor : $("#create-fun-author").val(),
                    genTableId : $("#tName").val(),
                    replaceFile : $("input[name='createChoice']:checked").length > 0 ? "true" : "false"
                }),
                success: function (data) {
                    if(data.result == "true"){
                        $("#work-list").click();
                        $("#save-success-text").text("保存成功！");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                        viewModel.clearSet();
                    } else {
                        $("#save-success-text").text("保存失败！");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                    }
                }
            })
        };
        viewModel.clearSet = function () {
            $("#planName").val("");
            $("#planName").attr("class","");
            $("input:radio[value='a'][name='modeClass']").attr('checked','true');
            $("#create-router").val("");
            $("#create-module-name").val("");
            $("#create-child-module-name").val("");
            $("#create-fun-disc").val("");
            $("#create-fun-name").val("");
            $("#create-fun-author").val("");
            // $("#tName").val(null).trigger("change");
            viewModel.planAdd();
            $("input[name='createChoice']").removeAttr("checked");
        };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            viewModel.searchPlan();
        };
        return {
            'model' : viewModel,
            'template' : template,
            'init' : init
        };
    }
);


