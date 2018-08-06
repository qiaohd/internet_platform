require(['jquery', 'knockout', 'bootstrap', 'uui', 'director', 'jqTreeTable'], function ($, ko) {
    window.ko = ko;
    window.addRouter = function (path, func) {

        var pos = path.indexOf('/:');
        var truePath = path;
        if (pos != -1)
            truePath = path.substring(0, pos);
        func = func || function () {
                var params = arguments;
                initPage('pages/' + truePath, params);
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
        });

        $('.left-menu li > a').on('click', function (e) {
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
            parent.children('li').siblings().children('a').css("background-color", "#eee");
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
            e.preventDefault();
        });

        $(".sub-menu li").on('click', function () {
            $(this).attr("class", "active");
            $(this).siblings().removeClass("active");
            // $(this).siblings().css("background-color", "#fff");
        })
    };
    getChild = function () {
        $('.content').html("");
        $('.content').css({"min-width": "1050px", "min-height": "530px", "padding-left": "10px", "margin-left": "230px"});
        $('.leftpanel').css("display", "block");
        $(".leftpanel > .left-menu").html("");
        $(".leftpanel > .left-menu").html("<li class='open'>"
            + "<a href='javascript:void(0)'><i class='fa'></i><span>代码生成</span><span class='arrow glyphicon open glyphicon-chevron-down'></span> </a>"
            + "<ul class='sub-menu'><li class='active'><a href='#/ip/code/tableConfigure/tableConfigure'>业务表配置</a></li><li><a href='#/ip/code/planConfigure/planConfigure'>生成方案配置</a></li></ul></li>"
            + "<li><a href='javascript:void(0)'><i class='fa'></i><span>生成示例</span><span class='arrow glyphicon open glyphicon-chevron-left'></span> </a>"
            + "<ul class='sub-menu'><li><a href='#/ip/doc/ipUserLe123'>单表</a></li></ul>"
            + "</li>");
        initMenuTree();
        $('.leftpanel > .left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        $('.leftpanel > .left-menu > li:first > a').click();
        // window.location="#/ip/code/planConfigure/planConfigure";
        window.location = $('.leftpanel > .left-menu > li:first > ul > li > a').attr("href");
    }

    menuChild = function(){
        $('.leftpanel').css("display", "none");
        $('.content').css({"min-width": "1050px",
            "min-height": "530px",
            "padding-left": "10px",
            "margin-left": "0px"});
    }
    // 数据分区设置菜单
    getDataPartition = function () {
        $('.content').html("");
        $('.content').css({"min-width": "1050px", "min-height": "530px", "padding-left": "10px", "margin-left": "230px"});
        $('.leftpanel').css("display", "block");
        $(".leftpanel > .left-menu").html("");
        $(".leftpanel > .left-menu").html("<li class='open'>"
            + "<a href='javascript:void(0)'><i class='fa'></i><span>分区设置</span><span class='arrow glyphicon open glyphicon-chevron-down'></span> </a>"
            + "<ul class='sub-menu'>" 
            +	"<li class='active'><a href='#/ip/hirerManage/dataPartition/dataPartition'>数据分区</a></li></ul></li>" 
            + "<li><a href='javascript:void(0)'><i class='fa'></i><span>租户信息</span><span class='arrow glyphicon open glyphicon-chevron-left'></span> </a>"
            + "<ul class='sub-menu'>" +
            	"<li><a href='#/ip/hirerManage/hirerAudit/hirerAudit'>租户信息</a></li></ul></li>");
        initMenuTree();
        window.location="#/ip/hirerManage/dataPartition/dataPartition";
        $('.leftpanel > .left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        $('.leftpanel > .left-menu > li:first > a').click();
    }
    //solr配置
    
    getSolrConfig = function () {
        $('.content').html("");
        $('.content').css({"min-width": "1050px", "min-height": "530px", "padding-left": "10px", "margin-left": "230px"});
        $('.leftpanel').css("display", "block");
        $(".leftpanel > .left-menu").html("");
        $(".leftpanel > .left-menu").html("<li class='open'>"
            + "<a href='javascript:void(0)'><i class='fa'></i><span>solr设置</span><span class='arrow glyphicon open glyphicon-chevron-down'></span> </a>"
            + "<ul class='sub-menu'>" 
            +	"<li class='active'><a href='#/ip/solrConfig/solrConfiguration/solrConfiguration'>solr配置</a></li></ul></li>");
        initMenuTree();
        window.location="#/ip/solrConfig/solrConfiguration/solrConfiguration";
        $('.leftpanel > .left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        $('.leftpanel > .left-menu > li:first > a').click();
    }
    $(function () {
        $('.left-menu').find("a[href*='#']").each(function () {
            var path = this.hash.replace('#', '');
            addRouter(path);
        });
        window.router.init();
        $('.content').css({
            "margin-left": "0px"
        });
    });

    function initPage(p, id) {
        var module = p;
        requirejs.undef(module);
        require([module], function (module) {
     
            ko.cleanNode($('.content')[0]);
            $('.content').html('');
            $('.content').html(module.template);
            if (module.model) {
                module.model.data.content = ko.observableArray([]);
                //ko.applyBindings(module.model, $('.content')[0]);
                module.init(id);
            } else {
                module.init(id, $('.content')[0]);
            }
        });
    }
});