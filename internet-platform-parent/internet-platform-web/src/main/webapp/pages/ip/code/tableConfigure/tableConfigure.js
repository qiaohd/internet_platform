define([ 'jquery', 'knockout','text!./tableConfigure.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','select2','css!./tableConfigure.css'],
    function($, ko, template) {
        var viewModel = {
            data : ko.observable({}),
            listData: new u.DataTable({
                meta: {
                    tableName: {
                        type: 'string'
                    },
                    tableComments: {
                        type: 'string'
                    },
                    className: {
                        type: 'string'
                    },
                    parentTable: {
                        type: 'string'
                    }
                }
            }),
            tableListData: new u.DataTable({
                meta: {
                    columnName: "",
                    columnComments: "",
                    jdbcType: "",
                    javaType: "",
                    javaField: "",
                    isPk: "",
                    isNull: "",
                    isInsert: "",
                    isEdit: "",
                    isList: "",
                    isQuery: "",
                    queryType: "",
                    showType: "",
                    dictType: "",
                    sort: ""
                }
            }),
            javaType: [],
            queryType: [],
            showType: [],
            sortType: [],
            javaTypeChange: function(obj){
                var Combo = $(obj.element).find('div')[0]['u.Combo'];
                Combo.setComboData(viewModel.javaType, null);
            },
            queryTypeChange: function(obj){
                var Combo = $(obj.element).find('div')[0]['u.Combo'];
                Combo.setComboData(viewModel.queryType, null);
            },
            showTypeChange: function(obj){
                var Combo = $(obj.element).find('div')[0]['u.Combo'];
                Combo.setComboData(viewModel.showType, null);
            },
            // sortTypeChange: function(obj){
            //     var Combo = $(obj.element).find('div')[0]['u.Combo'];
            //     Combo.setComboData(viewModel.sortType, null);
            // },
        };
        viewModel.tableList = function () {
            $("#table-add").text("业务表添加");
            viewModel.searchBusiness();
        }
        viewModel.searchBusiness = function () {
            var tableName = $("#tableName").val();
            var tableIntro = $("#tableIntro").val();
            $.ajax({
                url: 'gen/genTable/list',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    delFlag: '0',
                    tableName: tableName,
                    tableComments: tableIntro
                }),
                success: function (data) {
                    viewModel.listData.setSimpleData(data);
                }
            })
        };
        businessSetFun = function(obj) {
            obj.element.innerHTML = '<a id="'+ obj.rowIndex +'" onclick="editBusiness(this.id)" class="other-fun">编辑</a><span class="separator">|</span><a id="'+ obj.rowIndex +'" onclick="delBusiness(this.id)" class="other-fun">删除</a>';
        }
        editBusiness = function(index) {
            var selected_node = $('#business-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $("#table-add").click();
            $("#table-add").text("业务表编辑");
            $(".table-name").css("display","none");
            $(".table-edit-text").css("display","block");
            var table_id = selected_node.value.id;
            var table_name = selected_node.value.tableName;
            viewModel.setFormReq(table_id,table_name);
        }
        delBusiness = function(index) {
            var selected_node = $('#business-grid').parent()[0]['u-meta'].grid.getRowByIndex(index);
            $('#error_add_group').modal({backdrop: 'static', keyboard: false});
            var show_error = document.getElementById("delete_error_window");
            show_error.style.display = 'block';
            $("#jump-delete-text").attr("class",selected_node.value.id);
            $("#jump-delete-img").attr("class",selected_node.value.tableName);
        }
        viewModel.close_jump_window_delete = function () {
            var show_content_delete = document.getElementById("delete_error_window");
            $('#error_add_group').modal('hide');
            show_content_delete.style.display = 'none';
        };
        viewModel.close_jump_window_save = function () {
            var show_content_save = document.getElementById("save_error_window");
            $('#save_error_group').modal('hide');
            show_content_save.style.display = 'none';
        };
        viewModel.delete_business = function() {
            viewModel.close_jump_window_delete();
            var delete_id = $("#jump-delete-text").attr("class");
            var delete_name = $("#jump-delete-img").attr("class");
            $.ajax({
                url: 'gen/genTable/delete',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    delFlag: '1',
                    id: delete_id,
                    tableName: delete_name
                }),
                success: function (data) {
                    if(data.result == "false"){
                        $("#jump-save-text").text(data.reason);
                        $('#save_error_group').modal({backdrop: 'static', keyboard: false});
                    } else {
                        $("#save-success-text").text("删除成功！");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                        viewModel.searchBusiness();
                    }
                }
            })
        }
        //添加业务表
        viewModel.addTable = function () {
            $("#table-add").text("业务表添加");
            $(".table-name").css("display","block");
            $(".table-edit-text").css("display","none");
            $.ajax({
                url: 'gen/genTable/form',
                type: 'GET',
                dataType: 'json',
                data:{
                    "id": null,
                    "tableName": null
                },
                success: function (data) {
                    viewModel.setSelectTable(data.tableList);
                }
            })
        }
        viewModel.setSelectTable = function (data) {
            var html = "";
            for(var i = 0;i < data.length;i++){
                if(data[i].tableComments != ""){
                    html += "<option value='"+ data[i].id +"'>"+ data[i].tableName+":"+ data[i].tableComments +"</optionid>"
                } else {
                    html += "<option value='"+ data[i].id +"'>"+ data[i].tableName +"</optionid>"
                }
            }
            $("#tName").html(html);
        }
        viewModel.getTableInfo = function () {
            var table_id = $("#tName").val();
            var table_name = $("#tName").find("option:selected").text();
            var position = table_name.indexOf(":");
            if(-1 != position){
            	table_name=table_name.substring(0,position);
            }
            viewModel.setFormReq(table_id,table_name);
        }
        viewModel.setFormReq = function (table_id,table_name) {
        	$.ajax({
                url: 'gen/genTable/form',
                type: 'GET',
                dataType: 'json',
                data:{
                    "id": table_id,
                    "tableName": table_name
                },
                success: function (data) {
                    viewModel.setTableInfo(data);
                }
            })
        }
        viewModel.setTableInfo = function (data) {
            $("#father-table").select2({
                language: "zh-CN",
                theme: "classic"
            });
            $("#other-key").select2({
                language: "zh-CN",
                theme: "classic"
            });
            $(".table-name").css("display","none");
            $(".table-edit-text").css("display","block");
            //基础信息
            $("#inTableName").val(data.genTable.tableName);
            $("#inTableName").attr("class",data.genTable.id);
            $("#inTableIntro").val(data.genTable.tableComments);
            $("#class-name").val(data.genTable.className);
            $("#show-column-num option").each(function(i,n){
                if($(n).text() == data.genTable.layout){
                    $(n).attr("selected",true);
                }
            });
            //父表
            var html = "<option value=''>无</option>";
            for(var i = 0;i < data.tableList.length;i++){
                html += "<option value='"+ data.tableList[i].tableName +"'>"+ data.tableList[i].tableName +"</option>"
            }
            $("#father-table").html(html);
            $("#father-table").val(data.genTable.parentTable).trigger("change");
            // 当前表外键
            var html_key = "<option value=''>无</option>";
            for(var j = 0;j < data.genTable.columnList.length;j++){
                html_key += "<option value='"+ data.genTable.columnList[j].columnName+":"+ data.genTable.columnList[j].columnComments +"'>"+ data.genTable.columnList[j].columnName+":"+ data.genTable.columnList[j].columnComments+"</option>"
            }
            $("#other-key").html(html_key);
            $("#other-key").val(data.genTable.parentTableFk).trigger("change");
            // var sort_type = [];
            // for(var n = 0;n < data.genTable.columnList.length;n++) {
            //     var html =
            //     {
            //         "value": n+1
            //     }
            //     sort_type.push(html);
            // }
            viewModel.javaType = JSON.parse(viewModel.groupSelectOption(data.config.javaTypeList));
            viewModel.queryType = JSON.parse(viewModel.groupSelectOption(data.config.queryTypeList));
            viewModel.showType = JSON.parse(viewModel.groupSelectOption(data.config.showTypeList));
            // viewModel.sortType = JSON.parse(viewModel.groupSelectOption(sort_type));
            viewModel.tableListData.removeAllRows();
            viewModel.tableListData.setSimpleData(data.genTable.columnList);
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
        viewModel.saveTableEdit = function () {
            var table_name = $("#inTableName").val();
            var table_id = $("#inTableName").attr("class");
            var table_intro = $("#inTableIntro").val();
            var class_name = $("#class-name").val();
            var layout = $("#show-column-num").find("option:selected").text();
            var father_table = $("#father-table").val();
            var other_key = $("#other-key").val();
            var rows = viewModel.tableListData.getAllDatas();
            var new_rows = [];
            for(var i =0;i<rows.length;i++){
                var html =
                    {
                        columnName : rows[i].data.columnName.value,
                        columnComments : rows[i].data.columnComments.value,
                        jdbcType : rows[i].data.jdbcType.value,
                        javaType : rows[i].data.javaType.value,
                        javaField : rows[i].data.javaField.value,
                        isPk : rows[i].data.isPk.value,
                        isNull : rows[i].data.isNull.value,
                        isInsert : rows[i].data.isInsert.value,
                        isEdit : rows[i].data.isEdit.value,
                        isList : rows[i].data.isList.value,
                        isQuery : rows[i].data.isQuery.value,
                        queryType : rows[i].data.queryType.value,
                        showType : rows[i].data.showType.value,
                        dictType : rows[i].data.dictType.value,
                        sort_table : rows[i].data.sort.value,
                        id : rows[i].data.id.value
                    };
                new_rows.push(html);
            }
            rows=JSON.stringify(new_rows);
            $.ajax({
                url: 'gen/genTable/save',
                type: 'POST',
                dataType: 'json',
                data: {
                    "tableName":table_name,
                    "id": table_id,
                    "tableComments":table_intro,
                    "className":class_name,
                    "layout":layout,
                    "parentTable":father_table,
                    "parentTableFk":other_key,
                    "column_List":rows
                },
                success: function (data) {
                    if(data.result == "false"){
                        $('#save_error_group').modal({backdrop: 'static', keyboard: false});
                        $("#jump-save-text").text(data.reason);
                    } else {
                        $("#inTableIntro").focus();
                        $("#save-success-text").text("保存成功！");
                        $("#save-success-new").css("display","block");
                        $("#save-success-new").fadeOut(3000);
                    }
                }
            })
        };
        var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            $("#tName").select2({
                language: "zh-CN",
                theme: "classic"
            });
            viewModel.searchBusiness();
        };
        return {
            'model' : viewModel,
            'template' : template,
            'init' : init
        };
    }
);


