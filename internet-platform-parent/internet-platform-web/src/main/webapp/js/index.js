require(['jquery', 'knockout', 'bootstrap', 'uui', 'director', 'jqTreeTable','colorPicker'], function ($, ko) {
    window.ko = ko;
    window.addRouter = function (path, func) {
        var pos = path.indexOf('/:');
        var truePath = path;
        if (pos != -1)
            truePath = path.substring(0, pos);
        func = func || function () {
                var params = arguments;
                initPage('pages' + truePath, params);
            };
        var tmparray = truePath.split("/");
        if (tmparray[1] in router.routes && tmparray[2] in router.routes[tmparray[1]] && tmparray[3] in router.routes[tmparray[1]][tmparray[2]] && tmparray[4] in router.routes[tmparray[1]][tmparray[2]][tmparray[3]]) {
            return;
        } else {
            router.on(path, func);
        }
    };

    window.router = Router();

    initMenuTree = function () {
        $('#show_side').click(function () {
            var $leftpanel = $('.leftpanel');
            //展开
            if ($leftpanel.hasClass('leftpanel-collapse')) {
                $leftpanel.removeClass('leftpanel-collapse');
                $('.content').removeClass('content-collapse');
                $('.left-menu').children('li').children('a').children('.title').show();
                $('.left-menu').children('li').children('a').children('.arrow').show();
            } else {
                //合闭
                $leftpanel.addClass('leftpanel-collapse');
                $('.content').addClass('content-collapse');
                $('.left-menu').children('li').children('a').children('.title').hide();
                $('.left-menu').children('li').children('a').children('.arrow').hide();
                $('.left-menu').children('li.open').children('a').children('.arrow').removeClass('open').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
                $('.left-menu').children('li.open').children('a').children('.arrow').removeClass('active');
                $('.left-menu').children('li.open').children('.sub-menu').slideUp(200);
            }
            setThemeInit();
        });
        
        $('.left-menu li>a').on('click', function (e) {
            if ($(this).children('.title').length > 0 && !$(this).children('.title').is(':visible')) {
                $('#show_side').click();
            }
            if ($(this).next().hasClass('sub-menu') === false) {
                return;
            }
            var parent = $(this).parent().parent();
            
            parent.children('li.open').children('a').children('.arrow').removeClass('open').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
            parent.children('li.open').children('a').children('.arrow').removeClass('active');
            parent.children('li.open').children('.sub-menu').slideUp(200);
            parent.children('li').removeClass('open');
            parent.children('li').siblings().children('a').css("background-color","white");
            //  parent.children('li').removeClass('active');

            var sub = $(this).next();
            if (sub.is(":visible")) {
                $('.arrow', $(this)).removeClass("open").removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-left');
                $(this).parent().removeClass("active");
                sub.slideUp(200);
            } else {
                $('.arrow', $(this)).addClass("open").removeClass('glyphicon-chevron-left').addClass('glyphicon-chevron-down');
                $(this).parent().addClass("open");
                sub.slideDown(200);
            }
            setThemeInit();
            e.preventDefault();
        });

        $(".sub-menu li").on('click',function() {
            $(this).attr("class","active");
            $(this).siblings().removeClass("active");
            setThemeInit();
            $(this).siblings().css("background-color","white");
        })
    };
    
    function menuDel(id, name, isShow, url) {
        var jsonObject = {menuId: id, menuName: name, isShow: isShow};
        $("#headAdd").show();
        headadd();
        $.ajax({
            type: "POST",
            url: test + "/menuShow/hideMenu",
            data: jsonObject,
            success: function (data) {
                $("#" + id).parent().parent().remove();
                menuAdd();
            }
        });
    }

    function headadd() {
        $("#headAdd").on("click", function () {
            var is_show = document.getElementById('app-items').style.display== 'none';
            if(is_show){
                document.getElementById('app-items').style.display='block';
                menuAdd();
            } else {
                document.getElementById('app-items').style.display='none';
            }
        })
    }
     menuAdd = function() {
    	 var time = new Date();
        $.ajax({
            type: "GET",
            url: test + "/menuShow/userHideMenu",
            data: {
            	"time":time.getTime()
            },
            success: function (data) {
                $(".display_menu").html('');
                var menuHideList = data.menuHideList;
                for(var i = 0;i < menuHideList.length;i ++){
                    //var menuLogo = menuHideList[i].menuLogo.substring(0,5);
                    var menuLogo = menuHideList[i].menuLogo;
                    if(menuLogo == null){
                        var tag = "<li class='ui-app' id='" + menuHideList[i].menuId + "'><i class='Icon1'></i><div class='" + menuHideList[i].url + "'>" + menuHideList[i].menuName + "</div></li>";
                    }
                    else {
                        menuLogo = menuLogo.substring(0,5);
                        var tag = "<li class='ui-app' id='" + menuHideList[i].menuId + "'><i class='" + menuLogo + "'></i><div class='" + menuHideList[i].url + "'>" + menuHideList[i].menuName + "</div></li>";
                    }
                    $(".display_menu").append(tag);
                }
                choicedHideMenu();
            }
        });
    }
    
    choicedHideMenu = function() {
    	var time = new Date();
        $(".display_menu li").on('click',function(){
            var load = $(this).find("div").attr("class");
            var menuId = $(this).attr("id");
            $.ajax({
                type: "post",
                url: test + "/menuShow/showMenu",
                data: {
                    "menuId": menuId,
                    "time": time.getTime()
                },
                success: function (data) {
                    if (data) {
                        $(this).remove();
                        initFirstMenu();
                        initCheckHideMenu();
                        var href = load.substring(0, 4);
                        if(href == "null"){
                            window.location = $("#workbench").attr("href");
                            $("#"+menuId).click();
                        } else {
                            if (href == "http") {
                                window.open(load);
                            } else {
                                window.location = load;
                            }
                        }
                    }
                }
            });
        });
    }

    getChild = function () {
        $('.ui-tab-item > div > a').on('click', function () {
        	$(".ui-tab-item").removeClass("ui-tab-item-selected");
        	$(this).parent().parent().addClass("ui-tab-item-selected");
            $("#app-items").css("display","none");
            var href = $(this).attr('href');
            if (href == 'javascript:void(0);') {
                $('.content').css({
                    "padding-left": "10px"
                });
                var menuPId = $(this).attr("id");
                $.ajax({
                    type: 'GET',
                    url: $ctx + "/menuShow/getChildMenus",
                    data: {
                        "menuPId": menuPId
                    },
                    dataType: 'json',
                    success: function (data) {
                        setChild(data);
                    }
                });
            } else {
                var is_jump = $(this).attr('class');
                if (is_jump == "0") {
                    $('.content').css({
                        "padding-left": "10px"
                    });
                    $('.leftpanel').css("display", "none");
                    var jump = href.substring(0,1);
                    if(jump == "/"){
                        window.location = $("#workbench").attr("href");
                    } else {
                        window.location=href;
                    }
                }
            }
        });
    }

    setChild = function(data){
        initFuncTree(data);
        $('.content').css({
            "min-width": "1050px",
            "min-height": "530px",
            "padding-left": "235px",
        });
        $('.leftpanel').css("display","block");
        var child = data.children;
        for(var m = 0; m < child.length;m ++){
            if(child[m].children.length != "0"){
                $('.leftpanel > .left-menu > li:first').attr("class","open");
                var load = $('.leftpanel > .left-menu > li:first').find("a").attr("href");
                if(load == 'javascript:void(0);'){
                    load =$('.leftpanel > .left-menu > li:first').find("ul").find("li:first>a").attr("href");
                    $('.leftpanel > .left-menu > li:first').find("ul").find("li:first").attr("class","active");
                }
                var firstLoad = load.substring(0,1);
                if(firstLoad == "#"){
                	var currentUrl = window.location.hash;
                	if(load != currentUrl){
                        $('.content').html("");
                        window.location=load;
                	}
                } else {
                     $('.content').html("");
                    // window.open(load);
                }
            }
        }
        $('.leftpanel > .left-menu > li:first > a').click();
        setThemeInit();
    }

    delMenu = function () {
        $('.menu-remove').on('click', function () {
            var id = $(this).prev().attr('id');
            var name = $(this).prev().text();
            var url = $(this).prev().attr('href');
            var pId = $(".leftpanel > .left-menu").attr("id");
            if(id == pId){
                $('.leftpanel').css("display", "none");
                window.location = $("#workbench").attr("href");
            }
            menuDel(id, name, "0", url);
        });
    }

    initFirstNav = function (data) {
        for (var i = 0; i < data.length; i++) {
            var Nav_label = $('<label class=\"ui-tab-item\"></label>');
            var Nav_div = $('<div class=\"left-menu\"></div>');
            if(data[i].isLeaf == "1"){
                var http = data[i].url.substring(0,4);
                if(http == "http") {
                    var Nav_a = $('<a id=\"' + data[i].menuId + '\" href=\"' + data[i].url + '\" target="_blank" class="'+ data[i].isJump +'">' + data[i].menuName + '</a>');
                } else {
                    if(data[i].menuName == "系统管理"){
                        var Nav_a = $('<a id=\"' + data[i].menuId + '\" href=\"'+ $ctx + data[i].url + '\" target="_blank" class="'+ data[i].isJump +'">' + data[i].menuName + '</a>');
                    } else {
                        var Nav_a = $('<a id=\"' + data[i].menuId + '\" href=\"' + data[i].url + '\" class="'+ data[i].isJump +'">' + data[i].menuName + '</a>');
                    }
                }
            } else {
                var Nav_a = $('<a id=\"' + data[i].menuId + '\" href=\"javascript:void(0);\">' + data[i].menuName + '</a>');
            }
            if(data[i].menuName == "系统管理"){
                var Nav_del_i = $('');
            } else {
                var Nav_del_i = $('<i class=\"menu-remove\"></i>');
            }
            $(Nav_div).append(Nav_a);
            $(Nav_label).append(Nav_div);
            $(Nav_div).append(Nav_del_i);
            $('#headAdd').before(Nav_label);
        }
        $('.left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        delMenu();
        getChild();
    }

    initFirstMenu = function () {
        $(".ui-tab-title").html("");
        var workSpace = '<label class="ui-tab-item ui-tab-item-selected"><div class="left-menu"><a class="0" id="workbench" href="#/ip/workspace/ws"><i class="workbench"></i>工作台</a></div></label><label id="headAdd" class="ui-tab-item" style="display: none;"><i class="add"></i></label>';
        $(".ui-tab-title").append(workSpace);
        var time = new Date();
        $.ajax({
            type: 'GET',
            url: $ctx + "/menuShow/userMenu",
            data: {"time": time.getTime()},
            dataType: 'json',
            success: function (data) {
                $('#company-name').text(data.hirerCompanyName);
                $('#user').text(data.cusername);
                $('#user').attr("class",data.hirerId);
                //登录
                //loginIm($('#user').text(),'',false);
                var loginname=data.loginname;
                if(isUsedImChat=="true"){
                	loginIm(loginname,'',false);
                }
              
                //修改密码
                if(data.userId!=null){
                	$('#fixPwd').attr("href","/${ctx}/reset/resetUserPassword?ylUserID="+data.userId);
                }else{
                	$('#fixPwd').attr("href","/${ctx}/reset/resetUserPassword?ylHirerID="+data.hirerId);
                }

                //显示logo
                if(data.hirerLogoUrl == ""){
                    $('#company-logo').css("display","none");
                } else {
                    $('#company-logo img').attr("src",data.hirerLogoUrl);
                }

                //用户或者租户头像
                if(data.hirerPicUrl == "" || data.hirerPicUrl==null){
                    //$('#header-logo').css("display","none");
                	$('#header-logo img').attr("src",$ctx+"/images/default-user.png");
                } else {
                    $('#header-logo img').attr("src",data.hirerPicUrl);
                }

                var menu_list = data.menuList
                $('.content').css({
                    "margin-left": "0px"
                });
                initFirstNav(menu_list);
                setThemeInit();
            }
        });
    }

    initCheckHideMenu = function() {
    	var time = new Date();
        $.ajax({
            type: "GET",
            url: test+"/menuShow/userHideMenu",
            data:{
            	"time":time.getTime()
            },
            success: function(data){
                if(data.menuHideList.length=="0"){
                    $("#headAdd").hide();
                }else{
                    $("#headAdd").show();
                    headadd();
                }
            }
        });
    }

    setThemeIndex = function () {
        $("#setTheme").on("click",function () {
            $("#change-theme").css("display","block");
            currentTheme();
            selectTheme();
        })
    }

    currentTheme = function () {
        var current_user = $("#user").text();
        var new_color = localStorage.getItem(current_user + "#choiceColorNew");
        $(".change-theme-main dl dt").each(function () {
            var choice_theme_id = $(this).attr("id");
            if (choice_theme_id == new_color) {
                $(".choice-sure").remove();
                $(this).append("<span class='choice-sure'></span>");
            }
        })
    };

    selectTheme = function() {
        var all_color = {
            "blue" : {
                "header": "#4898e8",
                "headerNav": "white",
                "headerNavColor": "#333",
                "leftNav": "#eee",
                "leftNavChild": "#e8f5fe"
            },
            "black" : {
                "header": "#4f4f4f",
                "headerNav": "black",
                "headerNavColor": "#4f4f4f",
                "leftNav": "#eee",
                "leftNavChild": "#ddd"
            },
            "red" : {
                "header": "#a40000",
                "headerNav": "red",
                "headerNavColor": "#a40000",
                "leftNav": "#ffe1e1",
                "leftNavChild": "#fccfcf"
            }
        };
        $(".change-theme-main dl dt").on("click",function(){
            var header = $(".header");
            var header_nav = $(".ui-tab-title");
            var header_nav_color = $(".left-menu > a");
            var left_nav = $(".left-menu>li>a");
            var left_nav_child = $(".left-menu>li>ul>li.active");
            var current_user = $("#user").text();
            $(".choice-sure").remove();
            $(this).append("<span class='choice-sure'></span>");
            var item = $(this).attr("id");
            header.css("background-color",all_color[item].header);
            // header_nav.css("background-color",all_color[item].headerNav);
            header_nav_color.css("color",all_color[item].headerNavColor);
            left_nav.css("background-color",all_color[item].leftNav);
            left_nav_child.css("background-color",all_color[item].leftNavChild);
            localStorage.setItem(current_user + "#choiceColorNew", item);
        });
        $(".choice-color-sure").on("click",saveSelect);
    }

    saveSelect = function(){
        var current_user = $("#user").text();
        var new_color = localStorage.getItem(current_user + "#choiceColorNew");
        if(new_color == null){
            new_color = "blue";
        }
        localStorage.setItem(current_user + "#choiceColorOld", new_color);
        $("#change-theme").css("display","none");
    }

    setThemeInit = function(){
        var all_color = {
            "blue" : {
                "header": "#4898e8",
                "headerNav": "white",
                "headerNavColor": "#333",
                "leftNav": "#eee",
                "leftNavChild": "#e8f5fe"
            },
            "black" : {
                "header": "#4f4f4f",
                "headerNav": "black",
                "headerNavColor": "#4f4f4f",
                "leftNav": "#eee",
                "leftNavChild": "#ddd"
            },
            "red" : {
                "header": "#a40000",
                "headerNav": "red",
                "headerNavColor": "#a40000",
                "leftNav": "#ffe1e1",
                "leftNavChild": "#fccfcf"
            }
        };
        var header = $(".header");
        var header_nav = $(".ui-tab-title");
        var header_nav_color = $(".left-menu > a");
        var left_nav = $(".left-menu>li.open>a");
        var current_user = $("#user").text();
        var left_nav_child = $(".left-menu>li>ul>li.active");
        var old_color = localStorage.getItem(current_user + "#choiceColorNew");
        if(old_color == null){
            old_color = "blue";
        }
        header.css("background-color",all_color[old_color].header);
        // header_nav.css("background-color",all_color[old_color].headerNav);
        header_nav_color.css("color",all_color[old_color].headerNavColor);
        left_nav.css("background-color",all_color[old_color].leftNav);
        left_nav_child.css("background-color",all_color[old_color].leftNavChild);
    };

    $(function () {
    	getLoginId();
        initFirstMenu();
        initCheckHideMenu();
        getBackShow();
        $('.left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        window.router.init();
        setThemeIndex();
//        $('img.loginBtn').dblclick();
//        $("#photo-img").click(function(){
//        	$("#snsim_window_wide").css("display","block !important");
//        });
    })

    initFuncTree = function (menuData) {
        $(".leftpanel > .left-menu").html("");
        $(".leftpanel > .left-menu").attr("id",menuData.menuId);
        //if (menuData.id == 0) {
        //    var rootMenuArray = menuData.children;
            var rootMenuArray = menuData.children;
            for (var i = 0; i < rootMenuArray.length; i++) {
                var menu = rootMenuArray[i];
                var liObj = $("<li class=\"\">");
                var http = rootMenuArray[i].url.substring(0,4);
//                if(http == "http"){
//                    var aObj = $("<a href=\""+ rootMenuArray[i].url +"\" target='_blank'><span class=\"title\">" + menu.menuName + "</span></a>");
//                } else { 
//                    var aObj = $("<a href=\"javascript:void(0);\"> <i class=\"fa\"></i> <span class=\"title\">" + menu.menuName + "</span> <span class=\"arrow glyphicon glyphicon-chevron-left\"></span> </a>");
//                }
                var ulObj = $("<ul class=\"sub-menu\">");
                if (menu.children.length > 0) {
                    var aObj = $("<a href=\"javascript:void(0);\"> <i class=\"fa\"></i> <span class=\"title\">" + menu.menuName + "</span> <span class=\"arrow glyphicon glyphicon-chevron-left\"></span> </a>");

                    for (var j = 0; j < menu.children.length; j++) {
                        var subMenuObj = menu.children[j];
                        var httpChild = subMenuObj.url.substring(0,4);
                        if(httpChild != "http") {
                            var subLiObj = $("<li> <a class='0' href=\"" + subMenuObj.url + "\" >" + subMenuObj.menuName + "</a> </li>");
                        } else {
                            var subLiObj = $("<li> <a class=\"" + subMenuObj.isJump + "\" href=\"" + subMenuObj.url + "\" target='_blank'>" + subMenuObj.menuName + "</a> </li>");
                            window.location = "#";
                        }
                        $(ulObj).append(subLiObj);
                    }
                }else{
                	 var firstLoad = menu.url.substring(0,1);
                	 if(firstLoad=="#"){
                		 var aObj = $("<a href=\""+ rootMenuArray[i].url +"\"><span class=\"title\">" + menu.menuName + "</span></a>");
                	 }else{
                		 var aObj = $("<a href=\""+ rootMenuArray[i].url +"\" target='_blank'><span class=\"title\">" + menu.menuName + "</span></a>");
                	 }
                }
                if(http == "http"){
                    $(liObj).append(aObj);
                } else {
                    $(liObj).append(aObj).append(ulObj);
                }
                $(".leftpanel > .left-menu").append(liObj);
            }
        //}
        initMenuTree();
        $('.left-menu').find("a[href*='#']").each(function() {
             var path = this.hash.replace('#', '');
            addRouter(path);
         });
    };

    function initPage(p, id) {
        var module = p;
        requirejs.undef(module);
        require([module], function (module) {
            ko.cleanNode($('.content')[0]);
            $('.content').html('');
            $('.content').html(module.template);
            if (module.model) {
                module.model.data.content = ko.observableArray([]);
                ko.applyBindings(module.model, $('.content')[0]);
                module.init(id);
            } else {
                module.init(id, $('.content')[0]);
            }
        });
    }
    //function cleanDirty() {
    //    $('[role=tooltip]').remove();
    //};

    //window.ajaxAuthTimeout = function (XMLHttpRequest, textStatus, errorThrown) {
    //    var msg_json = 'auth check error!';  //当返回数据为json格式时候，
    //    var msg_txt = '{"msg":"auth check error!"}'; //当返回数据为 txt格式时候
    //    var return_info;
    //    if (XMLHttpRequest.responseJSON == undefined) {
    //        return_info = XMLHttpRequest.responseText;
    //    } else {
    //        return_info = XMLHttpRequest.responseJSON.msg
    //    }
    //    if (XMLHttpRequest.status == 306 &&
    //        ( return_info == msg_json || return_info == msg_txt   )) {
    //        jAlert('会话时间过期，请重新登录!!', '提示', function () {
    //            document.location.href = $ctx + "/";
    //        });
    //    }
    //
    //    //没有权限提示
    //    var status = XMLHttpRequest.getResponseHeader("errorStatus")
    //    if (status == '501') {
    //        alertErrorInfo('没有此操作权限');
    //    }
    //}

    /**统一设置ajax的参数信息，当状态返回为306时候，说明会话时间过期，需要重新登录,当自定义complete方法是，此方法被覆盖 */
    //$(function () {
    //    $.ajaxSetup({
    //        complete: function (xhr, status) {
    //            ajaxAuthTimeout(xhr, status);
    //        }
    //    });
    //})


    //提示信息框
    window.alertErrorInfo = function (message) {//显示后台返回   失败信息
        if (message == undefined || message == null || message == 'undefined') {
            message = "操作失败";
        }
        $.showMessage({
            type: "warning",
            msg: message,
            pos: {
                bottom: "30px",
                right: "30px"
            }
        });
    }

    window.alertSuccessInfo = function (message) {//显示后台返回   成功信息
        if (message == undefined || message == null || message == 'undefined') {
            message = "操作成功";
        }
        $.showMessage({
            type: "success",
            msg: message,
            pos: {
                bottom: "30px",
                right: "30px"
            }
        });
    }

    window.alertDangerInfo = function (message) {//显示后台返回   失败信息
        if (message == undefined || message == null || message == 'undefined') {
            message = "操作异常";
        }
        $.showMessage({
            type: "danger",
            msg: message

        });
    };
    //获取登录人ID
    function getLoginId(){
    	var time = new Date();
    	$.ajax({
			type: "GET",
			url: $ctx +"/userset/getLoginId",
			data: {time: time.getTime()},
			async:false,
			success: function(data){
				if(data.hirerId!="" && data.userId==undefined){
					$("#user-center").attr("href","#/ip/personal/modifyPass/modifyPass");
					$("#bell").attr("class","hirers");
				}else
				if(data.userId!="" && data.hirerId==undefined){
					$("#user-center").attr("href","#/ip/personal/perSetting/perSetting");
					$("#bell").attr("class","users");
				}
			}
		});
    };
    //获取待办事项
    function getBackShow(){
    	var time = new Date();
    	$.ajax({
            url: $ctx+'/askForLeave/getNoticeList',
            type: 'GET',
            data:{
        		"time": time.getTime()
        	},
            dataType: 'json',
            success: function (data) {
            	if(data.flag == "success"){
            		$("#more-task").text("");
            		if(data.data!=null){
            			var list = data.data;
                		var listLength = list.length;
                		if(listLength>0 && listLength<=5){
                			$("#task-list").removeClass("hide");
                			$("#task-list").text(listLength);
                    		$("#task-num").text("("+listLength+")");
                    		$("#task-cont").html("");
                    		for(var i=0; i<listLength;i++){
                    			var temp = '<li><a href="#/ip/resource/unaudited/unaudited?aflId='+list[i].id+'">'+list[i].proposer+'申请单</a></li>';
                    			$("#task-cont").append(temp);
                    		}
                    		$("#more-task").text("");
                    		toggleClick();
                		}else if(listLength>5){
                			$("#task-list").removeClass("hide");
                			$("#task-list").text(listLength);
                    		$("#task-num").text("("+listLength+")");
                    		$("#task-cont").html("");
                    		for(var i=0; i<5;i++){
                    			var temp = '<li><a href="#/ip/resource/unaudited/unaudited?aflId='+list[i].id+'">'+list[i].proposer+'申请单</a></li>';
                    			$("#task-cont").append(temp);
                    		}
                    		$("#more-task").text("更多");
                    		$("#more-task").on("click",function(){
                    			var href = $(this).attr('href');
                    		    window.location=href;
                    		    var path = this.hash.replace('#', '');
                    		    path = path.split('?')[0];
                    		    addRouter(path);
                    		});
                    		toggleClick();
                		}
                		else{
                			$("#task-list").addClass("hide");
                		}
            		}
            		goToUnaudited();
            	}else{
            		alert(data.msg);
            	}
            }
        })
    };
    function toggleClick(){
    	$("#dbsx").click(function(e){
    		$("#backlog").toggleClass("hide");
    		e.stopPropagation();
    	})
    }
	$(document).bind('click',function(){ 
    	if(!$('#backlog').hasClass('hide')){
    		$('#backlog').addClass('hide');
    	} 
    });
    function goToUnaudited(){
    	$("#task-cont a").on("click",function(){
    		var href = $(this).attr('href');
		    window.location=href;
		    var path = this.hash.replace('#', '');
		    path = path.split('?')[0];
		    addRouter(path);
    	})
    };
    $("#user-center").click(function(){
    	$('.content').html("");
    	$('.leftpanel').css("display", "none");
    	$('.content').css({
            "min-width": "1275px",
            "min-height": "530px",
            "padding-left": "230px"
        });
    	var href = $(this).attr('href');
    	 window.location=href;
         var path = this.hash.replace('#', '');
         addRouter(path);
    });
});