<!DOCTYPE html>
<html>
	<head>
		<script>
			window.$ctx = '${ctx}';
			window.$isUsedImChat='${isUsedImChat}';
		</script>
		<#assign mouse = "${ctx}">
		
        <script>var test="${mouse!}";</script>
	    <script>var path="${ctx}";</script>
	    <script>var isUsedImChat="${isUsedImChat}";</script>
		<meta charset="utf-8" />
		<title></title>
		<link href="trd/bootstrap/css/bootstrap.css" rel="stylesheet" tyep="text/css"/>
		<link href="trd/components/components.css" rel="stylesheet" tyep="text/css"/>
		<link href="trd/uui/css/u.css" rel="stylesheet" tyep="text/css"/>
		<link href="trd/uui/css/grid.css" rel="stylesheet" tyep="text/css"/>
		<link href="trd/uui/css/tree.css" rel="stylesheet" tyep="text/css"/>
		<link href="css/index.css" rel="stylesheet" type="text/css" />
		<link href="css/ecui.css" rel="stylesheet" type="text/css" />
		<link href="css/indexMain.css" rel="stylesheet" type="text/css" />

		<script src="${ctx}/trd/es5-shim.js" type="text/javascript"></script>
	    <script src="${ctx}/trd/es5-sham.js" type="text/javascript"></script>
	    <script src="${ctx}/trd/html5shiv.js" type="text/javascript"></script>

		<script src="${ctx}/trd/jQueryAlert/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jQueryAlert/jquery.ui.draggable.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jQueryAlert/jquery.alerts.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jquery-validform/validate.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>		
		<script src="${ctx}/trd/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="${ctx}/js/ip/common/commonCheck.js" type="text/javascript"></script>
	</head>
	<body>
	<script>
		$(function(){
			window.location=$("#workbench").attr("href");
		});
	</script>
		<div id="container">
			<div class="top">
				<div class="header">
					<div class="main">
						<div class="left">
							<div id="company-logo" class="comLogo"><img src="" height="30" /></div>
							<h1 id="company-name"></h1>
						</div>
						<ul class="right">
							<li class="search">
								<div class="header-search">
									<div class="header-search-input ui-input" >
										<input id="header-search" placeholder="请输入关键词"/>
									</div>
									<a href="#/ip/workspace/searchResult/searchResult" id="header-search-button" class="header-search-button">
										<i></i>
									</a>
									<i class="clear"></i>
								</div>
							</li>
							<li id="dbsx" class="dbsx">
								<div>
									待办事项
									<span id="task-list" class="to-do hide"></span>
									<span class="ui-hide"></span>
								</div>
								<div id="backlog" class="backlog hide">
									<p>待审批 <a id="task-num"></a></p>
									<ul id="task-cont" class="clearfix">
									</ul>
									<a id="more-task" class="more-task" href="#/ip/resource/unaudited/unaudited"></a>
								</div>
							</li>
							<li class="msg">
								<div id="bell">
									<i></i>
									<span class="ui-hide"></span>
								</div>
							</li>
							<li class="user">
								<div id="user">
								<!-- 
									<img id="user-pic" src=""/>
									-->
								</div>
								<div id="sub_menu" class="dialog-shadow popHeadSimple">
									<i class="down-arrrow"></i>
									<input type="hidden" id="ctx" value="${ctx}"/>
									<dl>
										<dd><a id="user-center" class="3" href="">个人中心</a></dd>
										<dd><a id="setTheme" href="javascript:void(0)">自定义主题</a></dd>
										<dd><a href="${ctx}/logout">退出</a></dd>
									</dl>
								</div>
							</li>
							<li>
								<div id="header-logo" class="perLogo"><img src="${ctx}/images/default-user.png" height="30" id="photo-img" class="loginBtn"/></div>
							</li>
						</ul>
					</div>
				</div>
				<!--顶部结束   -->
				<div class="nav_shadow">
					<div class="main">
						<div class="ui-tab-title">
							<label class="ui-tab-item ui-tab-item-selected">
								<div class="left-menu">
									<a id="workbench" href="#/ip/workspace/ws">
										<i class="workbench"></i>
										工作台
									</a>
								</div>
							</label>
							<label id="headAdd" class="ui-tab-item" style="display: none;">
								<i class="add"></i>
							</label>
						</div>
						<!--&lt;!&ndash;隐藏菜单 开始 &ndash;&gt;-->
						<div class="app-items" id="app-items" style="display: none;"onclick="document.getElementById('app-items').style.display='none'">
							<div class="line"></div>
							<div class="layout">
								<div class="mid">
									<div class="ui-tab">
										<ul class="display_menu"></ul>
									</div>
								</div>
							</div>
						</div>
						<!--隐藏菜单 结束 -->
					</div>
				</div><!--导航结束   -->
			</div>
				<!-- left -->
				<div class="leftpanel">
					<ul class="left-menu" style="padding: 102px 0 0 0;"></ul>
				</div>
				<div class="rightpanel">
					<!-- content -->
					<div class="content"></div>
				</div>

            <div id = "change-theme" class="change-theme">
                <div class="change-theme-content">
                    <div class="change-theme-header clearfix">
                         <span class="theme-header">请选择更换的主题</span>
                    </div>
                    <div class="change-theme-main clearfix">
                        <dl>
                            <dt id="blue" class="blue-color"><span class="choice-sure"></span></dt>
                            <dd>经典蓝</dd>
                        </dl>
                        <dl>
                            <dt id="black" class="black-color"><span></span></dt>
                            <dd>深邃黑</dd>
                        </dl>
                        <dl>
                            <dt id="red" class="red-color"><span></span></dt>
                            <dd>中国红</dd>
                        </dl>
                    </div>
                    <div class="change-theme-footer">
                        <button class="choice-color-sure">确定</button>
                    </div>
                </div>
            </div>
		</div>
		<div id="save-success-new" class="save-success-new"><img src="images/ip/menu/success_p.png" height="" width=""/><span id="save-success-text">保存成功！</span></div>
		<!-- 加载聊天窗口-->	
		   <#if isUsedImChat=="true">
			<#include "./imchat.ftl">
			<script src="${ctx}/trd/res/js/YYIMSDK.js"></script>
			<script src="${ctx}/trd/res/js/YYIMUIDemo.js"></script>	
			<script>
				$(document).ready(function(){
				SNS_BASE_BATH = window.getSNSBasePath();
				initSNSIM();
				// 初始化
				function initSNSIM(){
				YYIMChat.init({
					onOpened : SNSHandler.getInstance().onOpened,
					onClosed : SNSHandler.getInstance().onClosed,
					onAuthError : SNSHandler.getInstance().onAuthError,
					onStatusChanged : SNSHandler.getInstance().onStatusChanged,
					onConnectError : SNSHandler.getInstance().onConnectError,
					onUserBind : SNSHandler.getInstance().onUserBind,
					onPresence : SNSHandler.getInstance().onPresence,
					onSubscribe : SNSHandler.getInstance().onSubscribe,
					onRosterUpdateded : SNSHandler.getInstance().onRosterUpdateded,
					onRosterDeleted : SNSHandler.getInstance().onRosterDeleted,
					onRoomMemerPresence : SNSHandler.getInstance().onRoomMemerPresence,
					onReceipts : SNSHandler.getInstance().onReceipts,
					onTextMessage : SNSHandler.getInstance().onTextMessage,
					onPictureMessage : SNSHandler.getInstance().onPictureMessage,
					onFileMessage : SNSHandler.getInstance().onFileMessage,
					onShareMessage : SNSHandler.getInstance().onShareMessage,
					onSystemMessage: SNSHandler.getInstance().onSystemMessage,
					onPublicMessage: SNSHandler.getInstance().onPublicMessage,
					onLocationMessage: SNSHandler.getInstance().onLocationMessage,
					onAudoMessage : SNSHandler.getInstance().onAudoMessage,
					onWhiteBoardMessage : SNSHandler.getInstance().onWhiteBoardMessage,
					onMessageout : SNSHandler.getInstance().onMessageout,
					onGroupUpdate :  SNSHandler.getInstance().onGroupUpdate,
					onTransferGroupOwner :  SNSHandler.getInstance().onTransferGroupOwner,
					onKickedOutGroup : SNSHandler.getInstance().onKickedOutGroup,
					onPubaccountUpdate :SNSHandler.getInstance().onPubaccountUpdate
				});
			};
			
		});
		</script>
	</#if>
	<script src="${ctx}/trd/requirejs/require.js"></script>
	<script src="${ctx}/js/require.config.js"></script>
	<script src="${ctx}/js/index.js" type="text/javascript"></script>
	<script src="${ctx}/js/ip/imchat.js" type="text/javascript"></script>
	</body>
</html>
