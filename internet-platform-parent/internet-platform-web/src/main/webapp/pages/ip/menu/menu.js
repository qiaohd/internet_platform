define(['jquery','knockout', 'text!./menu.html','bootstrap','tree','dragJW','css!./menu.css'],
    function ($, ko, template) {
        var app;
        var viewModel = {
            data: ko.observable({}),
            showURL: ko.observable(false),
            showURLEdit : ko.observable(false),
            menuDataTable: new u.DataTable({
                params: {
                    "cls": "com.ufgov.ip.entity.system.IpMenu"
                },
                meta: {
                    'menuId': {type: 'string'},
                    'parentMenuId': {type: 'string'},
                    'menuName': {type: 'string'}
                }
            }),
            choiceFatherMenuSetting: {
                "callback": {
                    "onClick": function (e, id, node) {
                        var set_father_level = {
                            "0": "一级",
                            "1": "二级",
                            "2": "三级",
                            "3": "四级"
                        };
                        var manage_jump_choice = $("#manage-jump-choice");
                        var manage_jump_menu_rank = $("#manage-jump-menu-rank");
                        var node_id = node.id;
                        if(node_id == "00"){
                            node_id = "0"
                        }
                        manage_jump_choice.attr('class', node_id);
                        manage_jump_choice.val(node.name);
                        var node_name = node.name;
                        if(node_name == "无"){
                            node.level = -1;
                        }
                        manage_jump_menu_rank.val(set_father_level[node.level + 1]);
                        viewModel.setOtherFunction();
                    }
                }
            },
            choiceFatherMenuEditSetting: {
                "callback": {
                    "onClick": function (e, id, node) {
                        var set_father_level = {
                            "0": "一级",
                            "1": "二级",
                            "2": "三级",
                            "3": "四级"
                        };
                        var manage_jump_choice = $("#manage-jump-choice-edit");
                        var manage_jump_menu_rank = $("#manage-jump-menu-rank-edit");
                        var node_id = node.id;
                        if(node_id == "00"){
                            node_id = "0"
                        }
                        manage_jump_choice.attr('class', node_id);
                        manage_jump_choice.val(node.name);
                        var node_name = node.name;
                        if(node_name == "无"){
                            node.level = -1;
                        }
                        manage_jump_menu_rank.val(set_father_level[1 + node.level]);
                        viewModel.setOtherFunctionEdit();
                    }
                }
            }
        };
        viewModel.f_group_add = function () {
            var menu_btn_click_choice = $("#treeTable1 tbody tr input:checked").length;
            if (menu_btn_click_choice == 1) {
                $('#dialog_add_group').modal({
                    backdrop: 'static',
                    keyboard: false
                });
                dragElement(".manage-jump-header");
                var show_content = document.getElementById("manage-jump");
                show_content.style.display = 'block';
                var add_id_min = $("#treeTable1 td input:checked").attr("class");
                if (add_id_min != undefined) {
                    var set_rank = {
                        "一级": "1",
                        "二级": "2",
                        "三级": "3",
                        "四级": "4"
                    };
                    var add_rank = {
                        "1": "一级",
                        "2": "二级",
                        "3": "三级",
                        "4": "四级"
                    };
                    var add_choice_text_level = $('#' + add_id_min).find(".2").text();
                    var add_choice_num = set_rank[add_choice_text_level];
                    //放开四级添加
                    // if (add_choice_num != "3") {
                        var add_choice_text = $("#treeTable1 td input:checked").parent("span").text();
                        var add_choice_text_id = add_id_min;
                        $("#manage-jump-choice").val(add_choice_text);
                        $("#manage-jump-choice").attr("class", add_choice_text_id);
                        var rank = add_rank[++add_choice_num];
                        $("#manage-jump-menu-rank").val(rank);
                        viewModel.setOtherFunction();
                    // }
                }
            } else {
                this.f_group_add_sure();
            }
            $("#manage-jump-notice").css("display", "none");
        };
        viewModel.close_jump_window = function () {
            var show_content = document.getElementById("manage-jump");
            $('#dialog_add_group').modal('hide');
            viewModel.close_manage_jump_choice();
            show_content.style.display = 'none';
            $('#manage-jump-menu-name-value').val("");
            $('#manage-jump-choice').val("无");
            $('#manage-jump-menu-rank').val("一级");
            $('#manage-jump-menu-order').val("3");
            $("#manage-jump-choice-low").val("否");
            $("#manage-jump-choice-low").removeAttr("disabled");
            $("#manage-jump-choice-low").css("background-color", "white");
            $("#manage-jump-choice-jump").val("否");
            $("#manage-jump-choice-jump").removeAttr("disabled");
            $("#manage-jump-choice-jump").css("background-color", "white");
            $('#manage-jump-url').val("");
            $('#manage-jump-brief-introduction-text').val("");
        };
        //检查显示顺序（依据级别）
        viewModel.checkPositive = function () {
            var menu_level = $('#manage-jump-menu-rank').val();
            if (menu_level == "一级") {
                var show_order = $("#manage-jump-menu-order").val();
                if (show_order <= 2) {
                    $("#manage-jump-menu-order").val("3");
                }
            } else {
                var show_order = $("#manage-jump-menu-order").val();
                if (show_order <= 0) {
                    $("#manage-jump-menu-order").val("1");
                }
            }
        };
        viewModel.checkPositiveEdit = function () {
            var menu_level = $('#manage-jump-menu-rank-edit').val();
            if (menu_level == "一级") {
                var show_order = $("#manage-jump-menu-order-edit").val();
                if (show_order <= 2) {
                    $("#manage-jump-menu-order-edit").val("3");
                }
            } else {
                var show_order = $("#manage-jump-menu-order-edit").val();
                if (show_order <= 0) {
                    $("#manage-jump-menu-order-edit").val("1");
                }
            }
        };
        //选不同父级:设置底级、显示顺序
        viewModel.setOtherFunction = function () {
            var menu_level = $('#manage-jump-menu-rank').val();
        	if (menu_level == "四级") {
                $("#manage-jump-menu-order").val("1");
                $("#manage-jump-choice-low").val("是");
                $("#manage-jump-choice-low").attr("disabled", "disabled");
                $("#manage-jump-choice-low").css("background-color", "rgb(235, 235, 228)");
            }else if (menu_level == "三级") {
                $("#manage-jump-menu-order").val("1");
                $("#manage-jump-choice-low").removeAttr("disabled");
                $("#manage-jump-choice-low").css("background-color", "white");
            } else if (menu_level == "二级") {
                $("#manage-jump-menu-order").val("1");
                $("#manage-jump-choice-low").removeAttr("disabled");
                $("#manage-jump-choice-low").css("background-color", "white");
            } else {
                $("#manage-jump-menu-order").val("3");
                $("#manage-jump-choice-low").attr("disabled", "disabled");
                $("#manage-jump-choice-low").css("background-color", "white");
            }
        };
        viewModel.setOtherFunctionEdit = function () {
            var menu_level = $('#manage-jump-menu-rank-edit').val();            
            if (menu_level == "四级") {
                $("#manage-jump-menu-order-edit").val("1");
                $("#manage-jump-choice-low-edit").val("是");
                $("#manage-jump-choice-low-edit").attr("disabled", "disabled");
                $("#manage-jump-choice-low-edit").css("background-color", "rgb(235, 235, 228)");
            }else if (menu_level == "三级") {
               $("#manage-jump-menu-order-edit").val("1");
                $("#manage-jump-choice-low-edit").removeAttr("disabled");
                $("#manage-jump-choice-low-edit").css("background-color", "white");
            } else if (menu_level == "二级") {
                $("#manage-jump-menu-order-edit").val("1");
                $("#manage-jump-choice-low-edit").removeAttr("disabled");
                $("#manage-jump-choice-low-edit").css("background-color", "white");
            } else {
                $("#manage-jump-menu-order-edit").val("3");
                $("#manage-jump-choice-low-edit").attr("disabled", "disabled");
                $("#manage-jump-choice-low-edit").css("background-color", "white");
            }
        };
        //检查url类型
        viewModel.checkUrl = function () {
            var url = $("#manage-jump-url").val();
            if(url !=""){           	
            var url_style = url.substring(0, 1);
            if (url_style != "#") {
                $("#manage-jump-choice-jump").val("是");
                $("#manage-jump-choice-jump").attr("disabled", "disabled");
                $("#manage-jump-choice-jump").css("background-color", "rgb(235, 235, 228)");
            } else {
                $("#manage-jump-choice-jump").val("否");
                $("#manage-jump-choice-jump").attr("disabled", "disabled");
                // $("#manage-jump-choice-jump").removeAttr("disabled");
                $("#manage-jump-choice-jump").css("background-color", "rgb(235, 235, 228)");
            }
           }
        };
        viewModel.checkUrlEdit = function () {
            var url = $("#manage-jump-url").val();
            if(url !=""){           	
            var url_style = url.substring(0, 1);
            if (url_style != "#") {
                $("#manage-jump-choice-jump-edit").val("是");
                $("#manage-jump-choice-jump-edit").attr("disabled", "disabled");
                $("#manage-jump-choice-jump-edit").css("background-color", "rgb(235, 235, 228)");
            } else {
                $("#manage-jump-choice-jump-edit").val("否");
                $("#manage-jump-choice-jump-edit").attr("disabled", "disabled");
                // $("#manage-jump-choice-jump").removeAttr("disabled");
                $("#manage-jump-choice-jump-edit").css("background-color", "rgb(235, 235, 228)");
            }
           }
        };
        viewModel.f_group_add_sure = function () {
            $('#sure_add_group').modal({backdrop: 'static', keyboard: false});
            var show_content_sure = document.getElementById("sure_error_window");
            show_content_sure.style.display = 'block';
        };
        viewModel.close_jump_window_sure = function () {
            var show_content_sure = document.getElementById("sure_error_window");
            $('#sure_add_group').modal('hide');
            show_content_sure.style.display = 'none';
            $("#delete-error-text").html('<img src="images/ip/menu/errorNotice.png" />请您选择一项，进行编辑。');
        };
        viewModel.manage_jump_choice = function () {
            $('#manage-jump-choice-content').css('display', 'block');
            this.choice_menu_father_save();

        };
        viewModel.close_manage_jump_choice = function () {
            $("#manage-jump-choice-content").css('display', 'none');
        };
        //确定删除
        viewModel.f_error_add = function () {
            var menu_btn_click_choice = $("#treeTable1 tbody tr input:checked").length;
            if (menu_btn_click_choice == 0) {
                this.f_group_add_sure();
            } else {
                $('#error_add_group').modal({backdrop: 'static', keyboard: false});
                var show_error = document.getElementById("delete_error_window");
                show_error.style.display = 'block';
            }
        };
        viewModel.close_jump_error = function () {
            var show_error = document.getElementById("delete_error_window");
            $('#error_add_group').modal('hide');
            show_error.style.display = 'none';
        };
        viewModel.close_jump_window_edit = function () {
            var show_content = document.getElementById("manage-jump-edit");
            $('#dialog_add_group_edit').modal('hide');
            show_content.style.display = 'none';
        };
        viewModel.manage_jump_choice_edit = function () {
            $('#manage-jump-choice-content-edit').css('display', 'block');
            this.choice_menu_father_edit();
        };
        viewModel.close_manage_jump_choice_edit = function () {
            $("#manage-jump-choice-content-edit").css('display', 'none');
        };
        viewModel.close_jump_window_delete = function () {
            var show_content_delete = document.getElementById("delete_error_window");
            $('#error_add_group').modal('hide');
            show_content_delete.style.display = 'none';
        };
        viewModel.menu_edit = function () {
            var level = {
                "1": "1-强制授权，必须展示",
                "2": "2-租户管理",
                "3": "3-业务功能"
            };
            var menu_btn_click_choice = $("#treeTable1 tbody tr input:checked").length;
            if (menu_btn_click_choice == 0 || menu_btn_click_choice >= 2) {
                this.f_group_add_sure();
            } else {
                $('#dialog_add_group_edit').modal({backdrop: 'static', keyboard: false});
                var show_content = document.getElementById("manage-jump-edit");
                show_content.style.display = 'block';
                var edit_id_min = $("#treeTable1 td input:checked").parent("span").parent("td").parent("tr").attr("id");
                var edit_name_text = $('#' + edit_id_min).find("span").text();
                var edit_choice_text = $('#' + edit_id_min).find(".1").text();
                var edit_menu_rank_text = $('#' + edit_id_min).find(".2").text();
                var edit_order_text = $('#' + edit_id_min).find(".3").text();
                var edit_low_text = $('#' + edit_id_min).find(".4").text();
                var edit_url_text = $('#' + edit_id_min).find(".5").text();
                var edit_icon_id = $('#' + edit_id_min).find(".6").find('img').attr("class");
                var edit_icon_src = $('#' + edit_id_min).find(".6").find('img').attr("src");
                var edit_rank_text = level[$('#' + edit_id_min).find(".7").text()];
                var edit_jump_text = $('#' + edit_id_min).find(".8").text();
                var edit_desc_text = $('#' + edit_id_min).find(".9").text();
                var edit_permission_text = $('#' + edit_id_min).find(".10").text();
                var edit_name_id = $("#manage-jump-menu-name-edit input").attr("class", edit_id_min);
                var edit_name = $("#manage-jump-menu-name-edit input").val(edit_name_text);
                var edit_choice = $("#manage-jump-choice-edit").val(edit_choice_text);
                var edit_menu_rank = $("#manage-jump-menu-rank-edit").val(edit_menu_rank_text);
                var edit_order = $("#manage-jump-menu-order-edit").val(edit_order_text);
                var edit_low = $('#manage-jump-choice-low-edit').val(edit_low_text);
                var edit_url = $("#manage-jump-url-edit").val(edit_url_text);
                var edit_permission = $("#manage-jump-permission-edit").val(edit_permission_text);
                var edit_icon = '<img class="'+ edit_icon_id +'" src="'+ edit_icon_src +'" style="width: 24px; height: 24px; margin-left: 5px;margin-right: 5px;"> 请选择'
                var edit_show = $("#edit-menu-select-icon").html(edit_icon);
                var edit_rank = $("#manage-jump-set-rank-edit").val(edit_rank_text);
                var edit_jump = $("#manage-jump-choice-jump-edit").val(edit_jump_text);
                var edit_desc = $("#manage-jump-brief-introduction-text-edit").val(edit_desc_text);
            }
        };

        viewModel.choice_menu_father_edit = function () {
            this.add_menu_father_edit();
        };
        viewModel.choice_menu_father_save = function () {
            this.add_menu_father_save();
        };

        viewModel.add_menu_father_save = function () {
            var queryData = {};
            queryData["search_IN_levelNum"] = "1";
            viewModel.menuDataTable.addParams( queryData) ;
            app.serverEvent().addDataTable("menuDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'menu.MenuController',
                async: false,
                method: 'loadMenuTree',
                success: function (data) {}
            });
        };
        viewModel.add_menu_father_edit = function () {
            var queryData = {};
            queryData["search_IN_levelNum"] = "1";
            viewModel.menuDataTable.addParams( queryData) ;
            app.serverEvent().addDataTable("menuDataTable", "all").fire({
                url: $ctx + '/evt/dispatch',
                ctrl: 'menu.MenuController',
                async: false,
                method: 'loadMenuTree',
                success: function (data) {}
            });
        };
        viewModel.checkURL = function () {
            var url = $("#manage-jump-url").val();
            var menu_level = $('#manage-jump-menu-rank').val();
            if (url.length <= "0") {
                var is_low = $("#manage-jump-choice-low").val();
                if (menu_level == "一级" || menu_level == "二级") {
                    if (is_low == "是") {
                         viewModel.showURL(true);
                    } else {
                         viewModel.showURL(false);
                         viewModel.checkPermission(menu_level);
                    }
                } else {
                    if(menu_level == "三级"){
                        viewModel.showURL(true);
                    } else {
                        viewModel.checkPermission(menu_level);                       
                    }   
                }
             } else {
                 viewModel.showURL(false);
                 viewModel.checkPermission(menu_level);
             }
        };
        viewModel.checkPermission = function (menu_level) {
            var permission = $("#manage-jump-permission").val();
            if(permission == ""){
                if(menu_level == "四级"){
                    $("#save-success-text").text("权限标识必须添加");
                    $("#save-success-new").css("display", "block");
                    $("#save-success-new").css("z-index", "9999999");
                    $("#save-success-new").fadeOut(10000);
                    // alert("权限标识必须添加");
                } else {
                    viewModel.save_new_menu_checked();   
                }
            } else {
                viewModel.save_new_menu_checked();                
            }
        }
        viewModel.checkURLEdit = function () {
            var url = $("#manage-jump-url-edit").val();
            var menu_level = $('#manage-jump-menu-rank-edit').val();
            if (url.length <= 0) {
                var is_low = $("#manage-jump-choice-low-edit").val();
                if (menu_level == "一级" || menu_level == "二级") {
                    if (is_low == "是") {
                        viewModel.showURLEdit(true);
                    } else {
                        viewModel.showURLEdit(false);
                        viewModel.checkPermissionEdit(menu_level);
                    }
                } else {
                    if(menu_level == "三级"){
                        viewModel.showURLEdit(true);
                    } else {
                        viewModel.checkPermissionEdit(menu_level);                        
                    }
                }
            } else {
                viewModel.showURL(false);
                viewModel.checkPermissionEdit(menu_level);
            }
        };
        viewModel.checkPermissionEdit = function (menu_level) {
            var permission = $("#manage-jump-permission-edit").val();
            if(permission == ""){
                if(menu_level == "四级"){
                    $("#save-success-text").text("权限标识必须添加");
                    $("#save-success-new").css("display", "block");
                    $("#save-success-new").css("z-index", "9999999");
                    $("#save-success-new").fadeOut(6000);
                    // alert("权限标识必须添加");
                } else {
                    viewModel.save_change_menu_checked();
                }
            } else {
                viewModel.save_change_menu_checked();
            }
        }
        viewModel.save_new_menu_checked = function () {
            var set_rank = {
                "一级": "1",
                "二级": "2",
                "三级": "3",
                "四级": "4"
            };
            var set_choice = {
                "是": "1",
                "否": "0"
            };
            var save_menu_name = $('#manage-jump-menu-name-value').val();
            var save_choice = $('#manage-jump-choice').val();
            var save_menu_parent_id = $('#manage-jump-choice').attr('class');
            var save_menu_rank = set_rank[$('#manage-jump-menu-rank').val()];
            var save_menu_order = $('#manage-jump-menu-order').val();
            var save_choice_low = set_choice[$('#manage-jump-choice-low').val()];
            var save_url = $('#manage-jump-url').val();
            var save_permission = $('#manage-jump-permission').val();
            var save_choice_show = set_choice[$('#manage-jump-choice-show').val()];
            var save_set_rank = $('#manage-jump-set-rank').val();
            var save_menu_logo_name = $('#menu-select-icon > img').attr("class");
            var save_menu_logo_src = $('#menu-select-icon > img').attr("src");
            var save_menu_logo = save_menu_logo_name + save_menu_logo_src;
            var save_choice_jump = set_choice[$('#manage-jump-choice-jump').val()];
            var save_introduction_text = $('#manage-jump-brief-introduction-text').val();
            $.ajax({
                url: 'menuShow/create',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    menuId: '',
                    parentMenuId: save_menu_parent_id,
                    menuName: save_menu_name,
                    parentMenuName: save_choice,
                    levelNum: save_menu_rank,
                    dispOrder: save_menu_order,
                    isLeaf: save_choice_low,
                    url: save_url,
                    permission:save_permission,
                    isShow: save_choice_show,
                    authLevel: save_set_rank.charAt(0),
                    menuLogo: save_menu_logo,
                    isJump: save_choice_jump,
                    menuDesc: save_introduction_text
                }),
                success: function (data) {
                    if (data.result == "fail") {
                        $("#manage-jump-notice").css("display", "inline-block");
                        $("#manage-jump-notice").text(data.reason);
                    } else {
                        var show_content = document.getElementById("manage-jump");
                        $('#dialog_add_group').modal('hide');
                        show_content.style.display = 'none';
                        $('#manage-jump-menu-name-value').val("");
                        $('#manage-jump-choice').val("无");
                        $('#manage-jump-menu-rank').val("一级");
                        $('#manage-jump-menu-order').val("3");
                        $('#manage-jump-choice-low').val("否");
                        $("#manage-jump-choice-jump").val("否");
                        $("#manage-jump-choice-jump").removeAttr("disabled");
                        $("#manage-jump-choice-jump").css("background-color", "white");
                        $('#manage-jump-url').val("");
                        $('#manage-jump-brief-introduction-text').val("");
                        $("#save-success-text").text("保存成功！");
                        $("#save-success-new").css("display", "block");
                        $("#save-success-new").fadeOut(3000);
                        viewModel.load_menu();
                        var option = {
                            theme: 'vsStyle',
                            expandLevel: 1,
                        };
                        $('#treeTable1').treeTable(option);
                    }
                }
            });
        };
        viewModel.save_new_menu = function () {
            var must_menu_name = $("#manage-jump-menu-name-value").val();
            if (must_menu_name=="") {
                $("#manage-jump-notice").css("display", "inline-block");
                $("#manage-jump-notice").text("菜单名称不能为空!");
            } else if (must_menu_name.length > 10) {
                $("#manage-jump-notice").css("display", "inline-block");
                $("#manage-jump-notice").text("菜单名称过长，请保持10位以内！");
            } else {
                $("#manage-jump-notice").css("display", "none");
                viewModel.checkURL();
            }
        };
        viewModel.save_change_menu_checked =function() {
            var edit_id = $("#manage-jump-menu-name-edit input").attr("class");
            var edit_name = $("#manage-jump-menu-name-edit input").val();
            var edit_choice = $("#manage-jump-choice-edit").val();
            var edit_menu_rank = $("#manage-jump-menu-rank-edit").val();
            var edit_order = $("#manage-jump-menu-order-edit").val();
            var edit_low = $('#manage-jump-choice-low-edit').val();
            var edit_url = $("#manage-jump-url-edit").val();
            var edit_permission = $("#manage-jump-permission-edit").val();
            var edit_show = $("#manage-jump-choice-show-edit").val();
            var edit_auth_rank = $("#manage-jump-set-rank-edit").val();
            var edit_menu_logo_name = $("#edit-menu-select-icon > img").attr("class");
            var edit_menu_logo_src = $("#edit-menu-select-icon > img").attr("src");
            var edit_menu_logo = edit_menu_logo_name + edit_menu_logo_src;
            var edit_jump = $("#manage-jump-choice-jump-edit").val();
            var edit_desc = $("#manage-jump-brief-introduction-text-edit").val();
            var set_auth_rank = {
                "1-强制授权，必须展示": "1",
                "2-租户管理": "2",
                "3-业务功能": "3"
            };
            var set_menu_rank = {
                "一级": "1",
                "二级": "2",
                "三级": "3",
                "四级": "4"
            };
            var set_choice = {
                "是": "1",
                "否": "0"
            };
            $.ajax({
                url: 'menuShow/update',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({
                    menuId: edit_id,
                    parentMenuId: '',
                    menuName: edit_name,
                    parentMenuName: edit_choice,
                    levelNum: set_menu_rank[edit_menu_rank],
                    dispOrder: edit_order,
                    isLeaf: set_choice[edit_low],
                    url: edit_url,
                    permission: edit_permission,
                    isShow: set_choice[edit_show],
                    authLevel: set_auth_rank[edit_auth_rank],
                    menuLogo: edit_menu_logo,
                    isJump: set_choice[edit_jump],
                    menuDesc: edit_desc
                }),
                success: function (data) {
                    viewModel.close_jump_window_edit();
                    $("#save-success-text").text("保存成功！");
                    $("#save-success-new").css("display", "block");
                    $("#save-success-new").fadeOut(3000);
                    viewModel.load_menu();
                    var option = {
                        theme: 'vsStyle',
                        expandLevel: 1,
                    };
                    $('#treeTable1').treeTable(option);
                }
            });
        };
        viewModel.save_change_menu = function () {
            var must_menu_name = $("#manage-jump-menu-name-value-edit").val();
            if (!must_menu_name) {
                $("#manage-jump-notice-edit").css("display", "inline-block");
            } else {
                viewModel.checkURLEdit();
            }
        };
        viewModel.delete_menu = function () {
            var edit_id_arr = [];
            var edit_id = $("#treeTable1 td input:checked").each(function () {
                edit_id_arr.push($(this).parent("span").parent("td").parent("tr").attr("id"));
            });
            $.ajax({
                url: 'menuShow/delete',
                type: 'GET',
                data: {"menuIds": edit_id_arr.toString()},
                success: function (data) {
                    if(data.result == "fail"){
                        viewModel.close_jump_window_delete();
                        $("#sure_add_group").modal();
                        viewModel.f_group_add_sure();
                        $("#delete-error-text").html(data.reason);
                    } else {
                        viewModel.close_jump_window_delete();
                        $("#delete-error-text").html('<img src="images/ip/menu/errorNotice.png" />请您选择一项，进行编辑。');
                        $("#save-success-text").text("删除成功！");
                        $("#save-success-new").css("display", "block");
                        $("#save-success-new").fadeOut(3000);
                        viewModel.load_menu();
                        var option = {
                            theme: 'vsStyle',
                            expandLevel: 1,
                        };
                        $('#treeTable1').treeTable(option);
                    }

                }
            });
        };
        //$(document).keydown(function (e) {
        //    var evt = window.event ? window.event : e;
        //    if (evt.keyCode === 13) {
        //        $("#manage-nav-search-btn").focus();
        //        $("#treeTable1 tbody").html("");
        //        viewModel.search_menu();
        //    }
        //});
        viewModel.search_menu = function () {
            var found_menu = $("#manage-nav-search-text").val();
            var jsonName = {"menuName": encodeURIComponent(found_menu)};
            $("#treeTable1 tbody").html("");
            $.ajax({
                url: 'menuShow/likeMenuName',
                type: 'GET',
                data: jsonName,
                success: function (data) {
                    console.log(data);
                    var set_rank = {
                        "1": "一级",
                        "2": "二级",
                        "3": "三级",
                        "4": "四级"                   
                    };
                    var set_choice = {
                        "1": "是",
                        "0": "否"
                    };
                    var p = 0;
                    for (var i = 0; i < data.length; i++) {
                        var template_tree_table = "";
                        if (data[i].parentMenuName == "无" || data[i].parentMenuName == "") {
                            p = 0;
                        } else {
                            for (var j = 0; j < data.length; j++) {
                                if (data[i].parentMenuName == data[j].menuName) {
                                    p = data[j].menuId;
                                }
                            }
                        }
                        template_tree_table = '<tr id="' + data[i].menuId + '" pId="' + p + '"><td class="text-left manage-common-indent"><span><input class="' + data[i].menuId + '" type="checkbox" class="show-padding"/>' + data[i].menuName + '</span></td><td class="1">' + data[i].parentMenuName + '</td><td class="2">' + set_rank[data[i].levelNum] + '</td><td class="3">' + data[i].dispOrder + '</td><td class="4">' + set_choice[data[i].isLeaf] + '</td><td class="5">' + data[i].url + '</td><td class="6">' + set_choice[data[i].isShow] + '</td><td class="7">' + data[i].authLevel + '</td><td class="8">' + set_choice[data[i].isJump] + '</td><td class="9">' + data[i].menuDesc + '</td></tr>';
                        $("#treeTable1 tbody").append(template_tree_table);
                    };
                    var option = {
                        theme: 'vsStyle',
                        expandLevel: 1
                    };
                    $('#treeTable1').treeTable(option);
                }
            });
            var option = {
                theme: 'vsStyle',
                expandLevel: 1,
            };
            $('#treeTable1').treeTable(option);
        };
        viewModel.load_menu = function () {
        	var time = new Date();
            $("#treeTable1 tbody").html("");
            var set_rank = {
                "1": "一级",
                "2": "二级",
                "3": "三级",
                "4": "四级"
            };
            var set_choice = {
                "1": "是",
                "0": "否"
            };
            $.ajax({
                url: 'menuShow/menuList',
                type: 'GET',
                dataType: 'json',
                data:{time: time.getTime()},
                success: function (data) {
                    var menu_match_pid_arr = [];
                    var menu_match_id_arr = [];
                    var p = 0;
                    for (var i = 0; i < data.length; i++) {
                        var template_tree_table = "";
                        if (data[i].parentMenuName == "无" || data[i].parentMenuName == "") {
                            p = 0;
                        } else {
                            for (var j = 0; j < data.length; j++) {
                                if (data[i].parentMenuName == data[j].menuName) {
                                    p = data[j].menuId;
                                }
                            }
                        }
                        var menuLogo = data[i].menuLogo;
                        if(menuLogo == null){
                            menuLogo = 'pages/ip/menu/images/icon/TaskIcon1.png';
                            menuLogoId = 'Icon1';
                        } else {
                            menuLogoId = menuLogo.substring(0,5);
                            menuLogo = menuLogo.substring(5);
                        }
                        template_tree_table = '<tr id="' + data[i].menuId + '" pId="' + p + '"><td class="text-left manage-common-indent"><span><input class="' + data[i].menuId + '" type="checkbox" class="show-padding"/>' + data[i].menuName + '</span></td><td class="1">' + data[i].parentMenuName + '</td><td class="2">' + set_rank[data[i].levelNum] + '</td><td class="3">' + data[i].dispOrder + '</td><td class="4">' + set_choice[data[i].isLeaf] + '</td><td class="5">' + data[i].url + '</td><td class="10">' + data[i].permission + '</td><td class="6"><img class="'+ menuLogoId +'" src="'+ menuLogo +'" width="48" height="48" style="width: 24px; height: 24px;"></td><td class="7">' + data[i].authLevel + '</td><td class="8">' + set_choice[data[i].isJump] + '</td><td class="9">' + data[i].menuDesc + '</td></tr>';
                        $("#treeTable1 tbody").append(template_tree_table);
                    };
                    var option = {
                        theme: 'vsStyle',
                        expandLevel: 1,
                    };
                    $('#treeTable1').treeTable(option);
//                     $('#treeTable1-edit').treeTable(option);
//                     $('#treeTable2').treeTable(option);
                }
            });
        };

        viewModel.menuselectIcon = function(){
        	$("#add-menu").css("display","block");
            viewModel.menuiconOn();
        };
        viewModel.menuiconOn = function(){
        	$("#add-menu ul li").on('click',function(){
	        	$(this).find("span").addClass("active");
	        	$(this).siblings().find("span").removeClass("active");
	        	$(this).find("img").clone().prependTo("#menu-select-icon").siblings().remove();
	        	$("#getIconAdd").val($("#menu-select-icon img").attr("class"));
	        	$("#menu-select-icon img").css({"width":"24px","height":"24px","margin-left":"5px","margin-right":"5px"});
        	});
        };
        viewModel.save_menu_icon=function(){
        	$("#add-menu").css("display","none");
        };
        viewModel.close_menu_icon = function(){
        	$("#add-menu").css("display","none");
        	//$("#menu-select-icon").text("请选择");
        };

        viewModel.editMenuselectIcon = function(){
            var edit_icon_current = $("#edit-menu-select-icon img").attr("class");
            $("#edit-menu-select-icons li").each(function(){
                var edit_all_icon_id = $(this).find("img").attr("class");
                if(edit_all_icon_id == edit_icon_current){
                    $(this).find("span").attr("class","active");
                    $(this).siblings().find("span").removeClass("active");
                }
            })
            $("#edit-menu").css("display","block");
            viewModel.editMenuiconOn();
        };
        viewModel.editMenuiconOn = function() {
            $("#edit-menu ul li").on('click',function(){
                $(this).find("span").addClass("active");
                $(this).siblings().find("span").removeClass("active");
                $(this).find("img").clone().prependTo("#edit-menu-select-icon").siblings().remove();
                $("#getIconEdit").val($("#edit-menu-select-icon img").attr("class"));
                $("#edit-menu-select-icon img").css({"width":"24px","height":"24px","margin-left":"5px","margin-right":"5px"});
            });
        };
        viewModel.edit_save_menu_icon=function(){
            $("#edit-menu").css("display","none");
        };
        viewModel.edit_close_menu_icon = function(){
            $("#edit-menu").css("display","none");
            //$("#edit-menu-select-icon").text("请选择");
        };
        var init = function () {
        	//用户权限验证
            ko.cleanNode($('body')[0]);
            app = u.createApp(
                {
                    el: 'body',
                    model: viewModel
                }
            );
            viewModel.load_menu();
        };
        return {
            'model': viewModel,
            'template': template,
            'init': init
        };
    }
);
