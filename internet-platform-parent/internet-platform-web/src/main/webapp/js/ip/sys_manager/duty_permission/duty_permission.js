require(['jquery', 'knockout', 'bootstrap', 'uui', 'director', 'tree', 'grid'],
    //$("#tree")[0]['u-meta'].tree
    //checkNode(nodes[i], true, true);
    function ($, ko) {
        var viewModel = {
            data: ko.observable({}),
            showAddStaff: ko.observable(false),
            dutyName: ko.observable(""),
            dutyDisc: ko.observable(""),
            dutyNameAddStaff: ko.observable(""),
            menuDataTable: new u.DataTable({
                params: {
                    "cls": "com.ufgov.ip.entity.system.IpMenu"
                },
                meta: {
                    'menuId': {type: 'string'},
                    'parentMenuId': {type: 'string'},
                    'menuName': {type: 'string'},
                    'menuDesc': {type: 'string'}
                }
            }),
            companyDataTable: new u.DataTable({
                params: {
                    "cls": "com.ufgov.ip.entity.sysmanager.IpUserAndCompanyEntity"
                },
                meta: {
                    'staffId': {type: 'string'},
                    'staffName': {type: 'string'},
                    'staffPid': {type: 'string'},
                    'isUser': {type: 'string'}
                }
            }),
            dataTableStaff: new u.DataTable({
                params: {
                    "cls": "com.ufgov.ip.entity.sysmanager.IpUserRole"
                },
                meta: {
                    'staffNumber': {type: 'string'},
                    'userName': {type: 'string'},
                    'roleName': {type: 'string'},
                    'coName': {type: 'string'}
                }
            }),
            treeChoiceStaffSetting: {
                "check": {
                    enable: true,
                    chkStyle: 'checkbox',
                    chkboxType: {"Y": "ps", "N": "ps"}
                },
                data: {
                    key: {
                        name: "name"
                    }
                },
                "callback": {
                    "onCheck": function (e, id, node) {
                        var choiced_staff = viewModel.companyDataTable.getSelectedDatas();
                        viewModel.choiceStaff(choiced_staff);
                    }
                }
            },
            treeSetting: {
                "callback": {}
            }
        };
        viewModel.choiceStaff = function (choiced_staff) {
            var html = "";
            $("#choiced-staff").html("");
            for (var j = 0; j < choiced_staff.length; j++) {
                if (choiced_staff[j].data.isUser.value == "1") {
                    html = '<li><img src="'+ $ctx +'/images/ip/sys/staff_icon.png"><span>' + choiced_staff[j].data.staffName.value + '</span><button class="delete-staff fr" id="' + choiced_staff[j].id + '"><img src="/${ctx}/images/ip/sys/close_gray.png"></button></li>';
                    $("#choiced-staff").append(html);
                    html = "";
                }
            }
            $(".delete-staff").on("click", function () {
                var id = $(this).attr("id");
                var del_staff = viewModel.companyDataTable.getIndexByRowId(id);
                viewModel.companyDataTable.setRowUnSelect(del_staff);
                var choiced_staff_h = viewModel.companyDataTable.getSelectedDatas();
                $(this).parent().remove();
            });
        }
        viewModel.getAllStaff = function () {
            viewModel.clearAllChoicedStaff();
            var queryData = {};
            var roleId = $("#duty-name").attr("class");
            var getHirerId = $("#hirerId").text();
            queryData["role_EQ_roleId"] = roleId;
            queryData["search_EQ_hirerId"] = getHirerId;
            viewModel.companyDataTable.addParams(queryData);
            app.serverEvent().addDataTable("companyDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'role.RoleController',
                async: false,
                method: 'initCompanyDataTable',
                success: function (data) {
                }
            });
        };
        viewModel.add_staff_choice_open = function () {
            $('#dialog_add_group').modal({backdrop: 'static', keyboard: false});
            var duty_name = viewModel.dutyName();
            var roleId = $("#duty-name").attr("class");
            viewModel.dutyNameAddStaff(duty_name);
            $("#jump-duty-name").attr("class", roleId);
            $("#duty-search-text").val("");
            viewModel.getAllStaff();
            var choiced_staff = viewModel.companyDataTable.getSelectedDatas();
            for (var i = 0; i < choiced_staff.length; i++) {
                var id = choiced_staff[i].id;
                var del_staff = viewModel.companyDataTable.getIndexByRowId(id);
                viewModel.companyDataTable.addRowSelect(del_staff);
            }
            viewModel.choiceStaff(choiced_staff);
        };
        viewModel.clearAllChoicedStaff = function () {
            var choiced_staff = viewModel.companyDataTable.getSelectedDatas();
            for (var i = 0; i < choiced_staff.length; i++) {
                var id = choiced_staff[i].id;
                var del_staff = viewModel.companyDataTable.getIndexByRowId(id);
                viewModel.companyDataTable.setRowUnSelect(del_staff);
            }
            $("#choiced-staff").html("");
        }
        viewModel.searchStaff = function () {
            var search_staff = $("#duty-search-text").val();
            var choice_staff_tree = $("#tree-choice-staff")[0]['u-meta'].tree;
            var search_staff_node = choice_staff_tree.getNodesByParamFuzzy("name",search_staff,null);
            choice_staff_tree.expandNode(search_staff_node[0],true,false,true);
            choice_staff_tree.selectNode(search_staff_node[0]);
        }
        $(document).keydown(function (e) {
            var evt = window.event ? window.event : e;
            if (evt.keyCode === 13) {
                $("#duty-search-btn").focus();
                viewModel.searchStaff();
            }
        });
        viewModel.add_staff_choice_close = function () {
            $('#dialog_add_group').modal('hide');
        };
        viewModel.delete_position_open = function () {
            $('#dialog_delete_group').modal({backdrop: 'static', keyboard: false});
        };
        viewModel.delete_position_close = function () {
            $('#dialog_delete_group').modal('hide');
        };
        viewModel.getStaff = function () {
               //传要查询的参数
                var queryData = {};
                var roleId = $("#duty-name").attr("class");
                queryData["search_EQ_roleId"] = roleId;
                viewModel.dataTableStaff.addParams(queryData);
               //发送请求获取数据
                viewModel.getDataTableStaff();
        },
        viewModel.pageChangeFun = function(pageIndex){
        	 viewModel.dataTableStaff.pageIndex(pageIndex);
        	 viewModel.getDataTableStaff();
        }
        viewModel.sizeChangeFun = function(size){
        	viewModel.dataTableStaff.pageSize(size);
            viewModel.dataTableStaff.pageIndex("0");
            viewModel.getDataTableStaff();
        },
        viewModel.getDataTableStaff = function () {
            app.serverEvent().addDataTable("dataTableStaff", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'role.RoleController',
                //async: false,
                method: 'initPermissionUser',
                success: function (data) {}
            });
        }
        viewModel.getDuty = function () {
            var queryData = {};
            var roleId = $("#duty-name").attr("class");
            queryData["search_EQ_roleId"] = roleId;
            viewModel.menuDataTable.addParams(queryData);
            app.serverEvent().addDataTable("menuDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'role.RoleController',
                method: 'initPermissionMenu',
                success: function (data) {
                }
            });
        };
        viewModel.delete_duty = function () {
            var roleId = $("#duty-name").attr("class");
            $('#dialog_delete_group').modal('hide');
            $.ajax({
                url: $ctx + "/permission/delPermission",
                type: "post",
                dataType: "json",
                data: {"roleId": roleId},
                success: function (data) {
                    if (data && "success" == data.flag) {
                        initialization();
                        $("#delete-success-new").css("display","block");
                        $("#delete-success-new").hide(3000);
                    } else {
                        $("#show-error-info p").text(data.msg);
                        $("#show_error").modal({backdrop: 'static', keyboard: false});
                    }
                }
            });
        };
        viewModel.saveChoicedStaff = function () {
            var queryData = {};
            var roleId = $("#jump-duty-name").attr("class");
            queryData["role_EQ_roleId"] = roleId;
            viewModel.companyDataTable.addParams(queryData);
            app.serverEvent().addDataTable("companyDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'role.RoleController',
                method: 'savePermissionUser',
                success: function (data) {
                    $('#dialog_add_group').modal('hide');
                    data = JSON.parse(data);
                    if (data && "success" == data.flag) {
                        $("#save-success-new-duty").css("display","block");
                        $("#save-success-new-duty").hide(3000);
                    } else {
                        u.showMessageDialog({type: "info", title: "提示信息", msg: data.msg, backdrop: true});
                    }
                    $("#duty-search-text").val("");
                    $("#profile-tab").click();
                }
            });
        };
        viewModel.close_error = function() {
            $("#show_error").modal("hide");
        }
        function initialization() {
        	var time = new Date();
            var hirerId = $("#hirerId").text();
            $.ajax({
                url: $ctx + "/permission/getRoleInfobyHirerId",
                type: "GET",
                cache : false,
                dataType: "json",
                data:{time: time.getTime()},
                success: function (data) {
                    $(".duty-user").html("");
                    var html = "";
                    for (var i = 0; i < data.length; i++) {
                        if (i == 0) {
                            html = '<li class="active"><a href="javascript:void(0);" id = ' + data[i].roleId + '>' + data[i].roleName + '<span class="glyphicon glyphicon-chevron-right"></span><input type="hidden" value="' + data[i].roleDesc + '" ></a></li>'
                        } else {
                            html = '<li><a href="javascript:void(0);" id = ' + data[i].roleId + '>' + data[i].roleName + '<span class="glyphicon glyphicon-chevron-right"></span><input type="hidden" value="' + data[i].roleDesc + '" ></a></li>'
                        }
                        $(".duty-user").append(html);
                        html = "";
                    }
                    $(".duty-user li").each(function () {
                        var cc = $(this).attr("class");
                        if (cc == "active") {
                            var name = $(this).find("a").text();
                            var id = $(this).find("a").attr("id");
                            var desc = $(this).find("a").find("input").val();
                            viewModel.dutyName(name);
                            $("#duty-name").attr("class", id);
                            viewModel.dutyDisc(desc);
                        }
                    });
                    $(".duty-user li").on("click", function () {
                        $(this).addClass("active").siblings().removeClass("active");
                        var name = $(this).find("a").text();
                        var id = $(this).find("a").attr("id");
                        var desc = $(this).find("a").find("input").val();
                        viewModel.dutyName(name);
                        $("#duty-name").attr("class", id);
                        viewModel.dutyDisc(desc);
                        viewModel.getDuty();
                        viewModel.dataTableStaff.pageIndex("0");
                        viewModel.getStaff();
                    });
                    viewModel.getDuty();
                }
            });
        }

        initialization();
        var app = u.createApp(
            {
                el: 'body',
                model: viewModel
            }
        );
        viewModel.savechange = function () {
            app.serverEvent().addDataTable("menuDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'role.RoleController',
                method: 'hhh',
                success: function (data) {
                }
            });
        }

    });
