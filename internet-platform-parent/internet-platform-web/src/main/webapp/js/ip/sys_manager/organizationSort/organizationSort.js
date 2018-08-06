require(['jquery', 'knockout', 'bootstrap', 'uui','tree','jqUi'],
    function ($, ko) {
        window.ko = ko;
        var app;
        var viewModelSort = {
            data: ko.observable({}),
            dataTable1: new u.DataTable({
                meta: {
                    'coId': {
                        type: 'string'
                    },
                    'parentCoId': {
                        type: 'string'
                    },
                    'coName': {
                        type: 'string'
                    },
                    'coCodeAndName': {
                        type: 'string'
                    }
                }
            }),
            roleDataTable: new u.DataTable({
                meta: {
                    'roleId': {
                        type: 'string'
                    },
                    'parentRoleId': {
                        type: 'string'
                    },
                    'roleName': {
                        type: 'string'
                    }
                }
            }),
            departmentTreeSetting: {
                "callback": {
                    "onClick": function (e, id, node) {
                        var coId = node.id
                        $("#selected-node-name-department").val(node.name);
                        $("#selected-node-department").text(coId);
                        viewModelSort.getCurrentNodeData(coId);
                    }
                }
            },
            dutyTreeSetting: {
                "callback": {
                    "onClick": function (e, id, node) {}
                }
            },
            userTreeSetting: {
                "callback": {
                    "onClick": function (e, id, node) {
                        var node_id = node.id
                        $("#selected-node-name-user").val(node.name);
                        $("#selected-node-user").text(node_id);
                        viewModelSort.getCurrentNodeData_user(node_id);
                    }
                }
            },
        }
        //部门排序
        viewModelSort.getDepartment = function() {
            $("#departmentTree").css("display","block");
            $("#dutyTree").css("display","none");
            $("#userTree").css("display","none");
            var queryData = {};
            var getHirerId=$("#hirerId").text();
            queryData["search_EQ_hirerId"] = getHirerId;
            viewModelSort.dataTable1.addParams( queryData) ;
            app.serverEvent().addDataTable("dataTable1", "all").fire({
                url : $ctx + '/evt/dispatch',
                ctrl : 'org.OrganController',
                method : 'loadData',
                async: false,
                success : function(data) {}
            });
        }
        viewModelSort.getCurrentNodeData = function(coId) {
            $.ajax({
                url: $ctx + "/organizationSort/childOrg",
                type: "get",
                dataType: "json",
                data: {"coId": coId},
                success: function (data) {
                    viewModelSort.setDepartment(data);
                }
            });
        }
        viewModelSort.setDepartment = function(data) {
            $("#department-table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr class="' + data[i].coCode + '"><td class="department-num">'+ data[i].coCode +'</td><td class="'+ data[i].coId +' department-name">' + data[i].coName + '</td></tr>';
                $("#department-table tbody").append(html);
            }
        }
        viewModelSort.setDepartmentSort = function() {
            var coId = $("#selected-node-department").text();
        	var time = new Date();
            $.ajax({
                url: $ctx + "/organizationSort/childOrg",
                type: "get",
                dataType: "json",
                data: {
                	"coId": coId,
                	"time": time.getTime()
                	},
                success: function (data) {
                    viewModelSort.setDepartmentJump(data);
                }
            });
        }
        //可公用 设置弹窗排序内容
        viewModelSort.setDepartmentJump = function(data) {
            $("#sort-jump-window").modal({backdrop: 'static', keyboard: false});
            $(".jump-body table tbody").attr("class","department");
            $(".jump-body table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr class="'+ data[i].coCode +'"><td id="' + data[i].coId + '" class="'+ data[i].parentCoId +'">' + data[i].coName + '</td></tr>';
                $(".jump-body table tbody").append(html);
            }
            $( ".sotabjump-window" ).sortable({
                placeholder: "bg" , //拖动时，用css
                cursor: "move",
                items :"tr",
                opacity: 0.6,
                //revert: true, //释放时，增加动画
            });
        }
        //保存拖动排序--部门
        viewModelSort.saveDepartment = function() {
            var sorted_elements_array = [];
            var co_p_id;
            $(".jump-body table tbody tr").each(function(){
                var co_id = $(this).find("td").attr("id");
                var parent_coId = $(this).find("td").attr("class");
                sorted_elements_array.push(co_id);
                co_p_id = parent_coId;
            });
            viewModelSort.sendInfoSort(sorted_elements_array,co_p_id);
        }
        viewModelSort.sendInfoSort = function(array,id) {
            $.ajax({
                url: $ctx + "/organizationSort/orgSort",
                type: "POST",
                dataType: "json",
                data: {
                    coIds: array.join(","),
                    coPId: id
                },
                success: function (data) {
                    //{
                    //    "result" : "success",
                    //    "reason" : "更新成功！"
                    //}
                    if(data.result == "success"){
                        viewModelSort.getDepartment();
                        var ztree = $("#departmentTree")[0]['u-meta'].tree;
                        var open_node_name = $("#selected-node-name-department").val();
                        var node = ztree.getNodeByParam("name",open_node_name,null);
                        ztree.expandNode(node, true, false, true);
                        viewModelSort.getCurrentNodeData(node.id);
                    }
                }
            });
        }
        //按照首字母排序--部门
        viewModelSort.sortByLetterJumpDepartment = function() {
            $("#sure-jump-window").modal({backdrop: 'static', keyboard: false});
            $("#sort-by-letter-content").attr("class","department");
        }
        //保存首字母排序--部门
        viewModelSort.saveByFirstLetterSort_department = function() {
            var coId = $("#selected-node-department").text();
            $.ajax({
                url: $ctx + "/organizationSort/childOrg",
                type: "get",
                dataType: "json",
                data: {"coId": coId},
                success: function (data) {
                    viewModelSort.sortDepartmentByLetter(data);
                }
            });
        }
        viewModelSort.sortDepartmentByLetter = function(data) {
            data.sort(sortBy("coName"));
            viewModelSort.sendDepartmentByLetterSort(data);
        }
        viewModelSort.sendDepartmentByLetterSort = function(data) {
            var array = [];
            for(var i = 0; i < data.length;i ++){
                array.push(data[i].coId);
            }
            var coId = $("#selected-node-department").text();
            $.ajax({
                url: $ctx + "/organizationSort/orgSort",
                type: "POST",
                dataType: "json",
                data: {
                    coIds: array.join(","),
                    coPId: coId
                },
                success: function (data) {
                    if (data.result == "success") {
                        viewModelSort.getDepartment();
                        var ztree = $("#departmentTree")[0]['u-meta'].tree;
                        var open_node_name = $("#selected-node-name-department").val();
                        var node = ztree.getNodeByParam("name", open_node_name, null);
                        ztree.expandNode(node, true, false, true);
                        viewModelSort.getCurrentNodeData(node.id);
                    }
                }
            });
        }



        //职务排序
        //获取全部职务并展示成树
        viewModelSort.getDuty = function() {
            $("#departmentTree").css("display","none");
            $("#dutyTree").css("display","block");
            $("#userTree").css("display","none");
            var queryData = {};
            var getHirerId=$("#hirerId").text();
            queryData["search_EQ_hirerId"] = getHirerId;
            viewModelSort.roleDataTable.addParams( queryData) ;
            app.serverEvent().addDataTable("roleDataTable", "all").fire({
                url : $ctx + '/evt/dispatch',
                ctrl : 'org.OrgSort',
                method : 'loadRoleDataTable',
                async: false,
                success : function(data) {
                    var ztree = $("#dutyTree")[0]['u-meta'].tree;
                    var root_node = ztree.getNodes();
                    $("#selected-node-name-duty").val(root_node[0].name);
                    $("#selected-node-duty").text(root_node[0].id);
                    ztree.expandNode(root_node[0], true, false, true);
                    viewModelSort.getDutyList();
                }
            });
        }
        //获取全部职务并填充右侧表格
        viewModelSort.getDutyList = function() {
            var hirer_id=$("#hirerId").text();
            $.ajax({
                url: $ctx + "/organizationSort/getIpRoles",
                type: "get",
                dataType: "json",
                data: {"hirerId": hirer_id},
                success: function (data) {
                    viewModelSort.setDuty(data);
                }
            });
        }
        viewModelSort.setDuty = function(data) {
            $("#duty-table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr><td class="department-num"></td><td class="'+ data[i].roleId +' department-name">' + data[i].roleName + '</td></tr>';
                $("#duty-table tbody").append(html);
            }

        }
        //按照拖动排序--职务
        viewModelSort.setDutySort = function() {
            var hirer_id=$("#hirerId").text();
            $.ajax({
                url: $ctx + "/organizationSort/getIpRoles",
                type: "get",
                dataType: "json",
                data: {"hirerId": hirer_id},
                success: function (data) {
                    viewModelSort.setDutyJump(data);
                }
            });
        }
        viewModelSort.setDutyJump = function(data) {
            $("#sort-jump-window").modal({backdrop: 'static', keyboard: false});
            $(".jump-body table tbody").attr("class","duty");
            $(".jump-body table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr><td id="' + data[i].roleId + '">' + data[i].roleName + '</td></tr>';
                $(".jump-body table tbody").append(html);
            }
            $( ".sotabjump-window" ).sortable({
                placeholder: "bg" , //拖动时，用css
                cursor: "move",
                items :"tr",
                opacity: 0.6,
                //revert: true, //释放时，增加动画
            });

        }
        //保存拖动排序--职务
        viewModelSort.saveDuty = function() {
            var sorted_elements_array_duty = [];
            $(".jump-body table tbody tr").each(function(){
                var co_id_duty = $(this).find("td").attr("id");
                sorted_elements_array_duty.push(co_id_duty);
            });
            viewModelSort.sendInfoSortDuty(sorted_elements_array_duty);
        }
        viewModelSort.sendInfoSortDuty = function(array) {
            $.ajax({
                url: $ctx + "/organizationSort/roleSort",
                type: "POST",
                dataType: "json",
                data: {
                    roleIds: array.join(",")
                },
                success: function (data) {
                    //{
                    //    "result" : "success",
                    //    "reason" : "更新成功！"
                    //}
                    if(data.result == "success"){
                        viewModelSort.getDuty();
                    }
                }
            });
        }
        //按照首字母排序--职务
        viewModelSort.sortByLetterJumpDuty = function() {
            $("#sure-jump-window").modal({backdrop: 'static', keyboard: false});
            $("#sort-by-letter-content").attr("class","duty");
        }
        //保存首字母排序--职务
        viewModelSort.saveByFirstLetterSort_duty = function() {
            var hirer_id=$("#hirerId").text();
            $.ajax({
                url: $ctx + "/organizationSort/getIpRoles",
                type: "get",
                dataType: "json",
                data: {"hirerId": hirer_id},
                success: function (data) {
                    viewModelSort.sortDutyByLetter(data);
                }
            });
        }
        viewModelSort.sortDutyByLetter = function(data) {
            data.sort(sortBy("roleName"));
            viewModelSort.sendDutyByLetterSort(data);
        }
        viewModelSort.sendDutyByLetterSort = function(data) {
            var array = [];
            for(var i = 0; i < data.length;i ++){
                array.push(data[i].roleId);
            }
            viewModelSort.sendInfoSortDuty(array);
        }

        //用户排序
        viewModelSort.getUser = function() {
            $("#departmentTree").css("display","none");
            $("#dutyTree").css("display","none");
            $("#userTree").css("display","block");
            var queryData = {};
            var getHirerId=$("#hirerId").text();
            queryData["search_EQ_hirerId"] = getHirerId;
            viewModelSort.roleDataTable.addParams( queryData) ;
            app.serverEvent().addDataTable("roleDataTable", "all").fire({
                url : $ctx + '/evt/dispatch',
                ctrl : 'org.OrgSort',
                method : 'loadRoleDataTable',
                async: false,
                success : function(data) {
                    var ztree = $("#userTree")[0]['u-meta'].tree;
                    var root_node = ztree.getNodes();
                    ztree.expandNode(root_node[0], true, false, true);
                }
            });
        }
        //当前选择节点
        viewModelSort.getCurrentNodeData_user = function(node_id) {
            $.ajax({
                url: $ctx + "/organizationSort/getRoleUser",
                type: "get",
                dataType: "json",
                data: {"roleId": node_id},
                success: function (data) {
                    viewModelSort.setUser(data);
                }
            });
        }
        //获取全部人员并填充右侧表格
        viewModelSort.setUser = function(data) {
            $("#user-table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr><td class="user-num">'+ data[i].dispOrder +'</td><td id="'+ data[i].userId +'" class="user-name">' + data[i].userName + '</td><td class="user-account">'+ data[i].loginName +'</td><td id="'+ data[i].roleId +'" class="user-duty-name">'+ data[i].roleName +'</td></tr>';
                $("#user-table tbody").append(html);
            }
        }
        viewModelSort.setUserSort = function() {
            var roleId = $("#selected-node-user").text();
            $.ajax({
                url: $ctx + "/organizationSort/getRoleUser",
                type: "get",
                dataType: "json",
                data: {"roleId": roleId},
                success: function (data) {
                    viewModelSort.setUserJump(data);
                }
            });
        }
        //可公用 设置弹窗排序内容
        viewModelSort.setUserJump = function(data) {
            $("#sort-jump-window").modal({backdrop: 'static', keyboard: false});
            $(".jump-body table tbody").attr("class","user");
            $(".jump-body table tbody").html("");
            for(var i = 0;i < data.length;i ++){
                var html = '<tr><td id="' + data[i].userId + '" class="'+ data[i].roleId +'">' + data[i].userName + '</td></tr>';
                $(".jump-body table tbody").append(html);
            }
            $( ".sotabjump-window" ).sortable({
                placeholder: "bg" , //拖动时，用css
                cursor: "move",
                items :"tr",
                opacity: 0.6,
                //revert: true, //释放时，增加动画
            });
        }
        //保存拖动排序--人员
        viewModelSort.saveUser = function() {
            var sorted_elements_array = [];
            var co_p_id;
            $(".jump-body table tbody tr").each(function(){
                var co_id = $(this).find("td").attr("id");
                var parent_coId = $(this).find("td").attr("class");
                sorted_elements_array.push(co_id);
                co_p_id = parent_coId;
            });
            viewModelSort.sendInfoSortUser(sorted_elements_array,co_p_id);
        }
        viewModelSort.sendInfoSortUser = function(array,id) {
            $.ajax({
                url: $ctx + "/organizationSort/roleUserSort",
                type: "POST",
                dataType: "json",
                data: {
                    userIds: array.join(","),
                    roleId: id
                },
                success: function (data) {
                    //{
                    //    "result" : "success",
                    //    "reason" : "更新成功！"
                    //}
                    if(data.result == "success"){
                        viewModelSort.getUser();
                        var ztree = $("#userTree")[0]['u-meta'].tree;
                        var open_node_name = $("#selected-node-name-user").val();
                        var node = ztree.getNodeByParam("name",open_node_name,null);
                        ztree.expandNode(node, true, false, true);
                        viewModelSort.getCurrentNodeData_user(node.id);
                    }
                }
            });
        }
        //按照首字母排序--人员
        viewModelSort.sortByLetterJumpUser = function() {
            $("#sure-jump-window").modal({backdrop: 'static', keyboard: false});
            $("#sort-by-letter-content").attr("class","user");
        }
        //保存首字母排序--人员
        viewModelSort.saveByFirstLetterSort_user = function() {
            var roleId = $("#selected-node-user").text();
            $.ajax({
                url: $ctx + "/organizationSort/getRoleUser",
                type: "GET",
                dataType: "json",
                data: {"roleId": roleId},
                success: function (data) {
                    viewModelSort.sortUserByLetter(data);
                }
            });
        }
        viewModelSort.sortUserByLetter = function(data) {
            data.sort(sortBy("userName"));
            viewModelSort.sendUserByLetterSort(data);
        }
        viewModelSort.sendUserByLetterSort = function(data) {
            var array = [];
            for(var i = 0; i < data.length;i ++){
                array.push(data[i].userId);
            }
            var roleId = $("#selected-node-user").text();
            $.ajax({
                url: $ctx + "/organizationSort/roleUserSort",
                type: "POST",
                dataType: "json",
                data: {
                    userIds: array.join(","),
                    roleId: roleId
                },
                success: function (data) {
                    if (data.result == "success") {
                        viewModelSort.getUser();
                        var ztree = $("#userTree")[0]['u-meta'].tree;
                        var open_node_name = $("#selected-node-name-user").val();
                        var node = ztree.getNodeByParam("name", open_node_name, null);
                        ztree.expandNode(node, true, false, true);
                        viewModelSort.getCurrentNodeData_user(node.id);
                    }
                }
            });
        }
        //点击用户排序tab
        viewModelSort.getUserInit = function() {
            viewModelSort.getUser();
            var ztree = $("#userTree")[0]['u-meta'].tree;
            var root_node = ztree.getNodes();
            $("#selected-node-name-user").val(root_node[0].children[0].name);
            $("#selected-node-user").text(root_node[0].children[0].id);
            viewModelSort.getCurrentNodeData_user(root_node[0].children[0].id);
        }


        //排序弹窗
        viewModelSort.sortCloseWindow = function() {
            $("#sort-jump-window").modal('hide');
        }
        viewModelSort.sortSaveWindow = function() {
            viewModelSort.sortCloseWindow();
            var jump_window = $(".jump-body table tbody").attr("class");
            switch (jump_window) {
                case "department" :
                    viewModelSort.saveDepartment();
                    break;
                case "duty" :
                    viewModelSort.saveDuty();
                    break;
                case "user" :
                    viewModelSort.saveUser();
                    break;
            }
        }
        //首字母排序
        viewModelSort.sortByFirstLetterJumpClose = function() {
            $("#sure-jump-window").modal('hide');
        }
        viewModelSort.sortByFirstLetterJumpSure = function() {
            viewModelSort.sortByFirstLetterJumpClose();
            var jump_window_letter = $("#sort-by-letter-content").attr("class");
            switch (jump_window_letter) {
                case "department" :
                    viewModelSort.saveByFirstLetterSort_department();
                    break;
                case "duty" :
                    viewModelSort.saveByFirstLetterSort_duty();
                    break;
                case "user" :
                    viewModelSort.saveByFirstLetterSort_user();
                    break;
                //default:
                //    break;
            }
        }
            //错误提示框
        viewModelSort.errorCloseWindow = function() {
            $("#error-jump-window").modal('hide');
        }

        //按指定属性排序
        //data.sort(sortBy("coName"));
        function sortBy(field) {
            return function(a,b) {
                return a[field].localeCompare(b[field]);
            }
        }
        //页面初始化默认选择部门根节点
        viewModelSort.initSortList = function() {
            var ztree = $("#departmentTree")[0]['u-meta'].tree;
            var root_node = ztree.getNodes();
            $("#selected-node-name-department").val(root_node[0].name);
            $("#selected-node-department").text(root_node[0].id);
            ztree.expandNode(root_node[0], true, false, true);
            viewModelSort.getCurrentNodeData(root_node[0].id);
        }

        //点击部门排序绑定的方法
        viewModelSort.getDepartmentInit = function() {
            initPage();
        }

        //$("#departmentTree").css("display","block");
        //$("#dutyTree").css("display","none");
        function initPage() {
            viewModelSort.getDepartment();
            viewModelSort.initSortList();
        }
        app = u.createApp(
            {
                el:'body',
                model: viewModelSort
            }
        );
        initPage();
    }
);