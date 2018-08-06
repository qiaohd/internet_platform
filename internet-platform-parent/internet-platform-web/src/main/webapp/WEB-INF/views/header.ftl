<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
	    <#assign mouse = "${ctx}">
	    <script>var test="${mouse!}";</script>
		<script>var path="${ctx}";</script>
		<script>
	     window.$ctx = '${ctx}';
        </script>
	    <link href="${ctx}/trd/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>
		<link href="${ctx}/trd/uui/css/u.css" rel="stylesheet" type="text/css"/>
	    <link href="${ctx}/trd/uui/css/tree.css" rel="stylesheet" type="text/css"/>
	    <link href="${ctx}/trd/uui/css/grid.css" rel="stylesheet" type="text/css"/>
	    <link href="${ctx}/css/ip/reset.css" rel="stylesheet" type="text/css"/>
	    <link href="${ctx}/trd/components/components.css" rel="stylesheet" type="text/css">
	    <link href="${ctx}/css/systemMain.css" rel="stylesheet" type="text/css"/>
		<script src="${ctx}/trd/jQueryAlert/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jQueryAlert/jquery.ui.draggable.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jQueryAlert/jquery.alerts.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jquery-validform/validate.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
		<script src="${ctx}/trd/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="${ctx}/trd/requirejs/require.js"></script>
		<script src="${ctx}/js/require.config.js"></script>
		<script src="${ctx}/js/ip/sys_manager/sys_common.js"></script>
	</head>

	<body>
	<script>
	$(function(){
		$('.nav-pills li').each(function(){
        	var href = $(this).find('a').attr('href');
        	if(href == window.location.pathname) {
          		$(this).addClass('active');
          		//.siblings().removeClass("active");
        	}
    });
    $("#close-win").click(function(){
    	window.close();
    });
		function getCookie(c_name) {
	      if (document.cookie.length>0) {
	          c_start=document.cookie.indexOf(c_name + "=");
	          if (c_start!=-1) {
	              c_start=c_start + c_name.length+1
	              c_end=document.cookie.indexOf(";",c_start)
	              if (c_end==-1) c_end=document.cookie.length
	              return unescape(document.cookie.substring(c_start,c_end))
	          }
	      }
	      return ""
	  }
	  var u_usercode = getCookie('u_usercode');
	  $("#old_u_usercode").text(u_usercode);
 });
	</script>
	<div class="navbar navbar-fixed-top">
		<div class="system-top">
			<div class="system-logo">
				<img src="${ctx}/pages/ip/sys_manager/images/system_logo.gif" />
				<span id="old_u_usercode" style="display:none;"></span>
			</div>
			<div class="system-nav">
				<ul class="nav nav-pills">
					<li>
						<a href="${ctx}/tenant/tenantInfo">租户信息</a>
					</li>
					<li>
						<a href="${ctx}/organization/organInfo">组织机构</a>
					</li>

					<li>
						<a href="${ctx}/permission/dutyPermission">职务权限</a>
					</li>

					<li>
						<a href="${ctx}/organizationSort/sortPage">机构排序</a>
					</li>

					<li>
						<a href="#">提醒设置</a>
					</li>
				</ul>
			</div>
			<div class="top-right">
				<a id="close-win">关闭</a>
			</div>
			<div class="clearfix"></div>
		</div>
		<div id="save-success-new" class="save-success-new"><img src="../images/ip/menu/success_p.png" height="" width=""/><span id="save-success-text"></span></div>
	</div>
