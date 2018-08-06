<!DOCTYPE html>
<html>
<head>
    <script>
        window.$ctx = '${ctx}';
    </script>
    <script>var path = "${ctx}";</script>
    <meta charset="utf-8"/>
    <title></title>
    <link href="trd/bootstrap/css/bootstrap.min.css" rel="stylesheet" tyep="text/css"/>
    <link href="trd/uui/css/u.min.css" rel="stylesheet" tyep="text/css"/>
    <link href="trd/uui/css/tree.css" rel="stylesheet" type="text/css"/>
    <link href="trd/uui/css/grid.css" rel="stylesheet" type="text/css"/>
    <link href="css/ecui.css" rel="stylesheet" type="text/css"/>
    <link href="css/indexMain.css" rel="stylesheet" type="text/css"/>
    <link href="trd/treeTable/custom/jquery.treeTable.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="css/ip/changeTree.css">
    <link href="css/ip/resetCommon.css" rel="stylesheet" type="text/css"/>
    <link href="trd/components/components.css" rel="stylesheet" tyep="text/css"/>
    
    <script src="${ctx}/trd/es5-shim.js" type="text/javascript"></script>
    <script src="${ctx}/trd/es5-sham.js" type="text/javascript"></script>
    <script src="${ctx}/trd/html5shiv.js" type="text/javascript"></script>

    <script src="${ctx}/trd/jQueryAlert/jquery.js" type="text/javascript"></script>
    <script src="${ctx}/trd/jQueryAlert/jquery.ui.draggable.js" type="text/javascript"></script>
    <script src="${ctx}/trd/jQueryAlert/jquery.alerts.js" type="text/javascript"></script>
    <script src="${ctx}/trd/jquery-validform/validate.js" type="text/javascript"></script>
    <script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
    <script src="${ctx}/trd/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx}/trd/requirejs/require.js"></script>
    <script src="${ctx}/js/require.config.js"></script>
    <script src="${ctx}/js/ip/menu/menuIndex.js" type="text/javascript"></script>
</head>
<body>
<script>
    $(function () {
        window.location = $("#menu-manage").attr("href");
    });
</script>
<div id="container">
<div class="top">
    <div class="header">
        <div class="main">
            <div class="left">
						<span>
							<h1>北京用友政务软件有限公司</h1>
						</span>
            </div>
            <ul class="right">
                <li class="user">
                    <div>
                        管理员
                        <img src="images/default-user.png"/>
                    </div>
                    <div id="sub_menu" class="dialog-shadow popHeadSimple">
						<i class="down-arrrow"></i>
						<input type="hidden" id="ctx" value="/internet-platform-web">
						<dl>
							<dd><a id="user-center" class="3" href="javascript:void(0)" onclick="document.getElementById('sub_menu').style.display='none';">个人设置</a></dd>
							<dd><a href="javascript:void(0)" onclick="document.getElementById('sub_menu').style.display='none';">修改头像</a></dd>
							<dd><a href="${ctx}/logout">退出</a></dd>
						</dl>
					</div>
                </li>
            </ul>
        </div>
    </div>
    <!--顶部结束   -->
    <div class="nav_shadow">
        <div class="main">
            <div class=" ui-tab-title">
                <label class="ui-tab-item ui-tab-item-selected">
                    <div class="left-menu"><a class="0" id="menu-manage" href="#/ip/menu/menu" onclick="menuChild()">
                        菜单管理</a></div>
                </label>
                <label class="ui-tab-item">
                    <div class="left-menu"><a id="code-generation" href="javascript:void(0);" onclick="getChild()">代码生成 </a></div>
                </label>
                <label class="ui-tab-item">
                    <div class="left-menu"><a id="data-partition" href="javascript:void(0);" onclick="getDataPartition()">数据分区 </a></div>
                </label>
                <label class="ui-tab-item">
                    <div class="left-menu"><a id="solr-configuration" href="javascript:void(0);" onclick="getSolrConfig()">solr配置 </a></div>
                </label>
            </div>
        </div>
    </div><!--导航结束   -->
    </div>
    <div class="wapper">
        <!-- left -->
        <div class="leftpanel">
            <ul class="left-menu" style="margin: 102px 0 0 0;"></ul>
        </div>
        <div class="rightpanel">
        <!-- content -->
        <div class="content"></div>
        </div>
    </div>
</div>
<div id="save-success-new" class="save-success-new"><img src="images/ip/menu/success_p.png" height="" width=""/><span id="save-success-text">保存成功！</span></div>
</body>
</html>
