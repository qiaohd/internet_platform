<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="${ctx}/css/ip/findpassword.css" rel="stylesheet" type="text/css">
</head>
		<script>
			window.$ctx = '${ctx}';
		</script>
		<#assign mouse = "${ctx}">
		
        <script>var test="${mouse!}";</script>
	    <script>var path="${ctx}";</script>
<body>
   <div class="top">
	<a id="indexLogo" class="toplogo" href="javascript:void(0)">
		<i class="icon-toplogo"></i>
	</a>
    <a class="btn-login ng-binding" href="${ctx}/login">登录</a>
</div>
<div class="wraper">
	<div class="logo">
        <span class="text">找回密码</span>
    </div>
    <div class="inputwrap login-input">
        <label class="lable-login"><i class="icon-user"></i></label>
        <input tabindex="1" name="username" class="padr1" id="username" placeholder="手机/邮箱" type="text">
        <a href="javascript:void(0);" class="inputclear input-clear"><i class="icon-clear"></i></a>
    </div>
    <div id="checkUserNameInfo"></div>
    <div class="errortip ng-hide"></div>
 	<!-- 图片验证码初始化状态 -->
    <div class="smswrap clearfix">
        <div class="changeimg">
            <a href="javascript:void(0);" id="changeCode"><i class="icon-change"></i>换一换</a> 
            <span><img id="captchaImage" src="${ctx}/login/getImage" style="cursor:pointer"></span>
        </div>
        <div class="authcode imgcode"><input tabindex="2" name="password" id="code" placeholder="请输入图片内容" type="text"></div>
    </div>
    <div id="checkCodeInfo"></div>
    <div class="errortip ng-binding ng-hide"></div>
    <div class="submitwrap">
        <a href="javascript:void(0)" class="btn-submit ng-binding" id="nextStep">下一步</a>
    </div>
</div>
<script type="text/javascript" src="${ctx}/trd/jquery/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="${ctx}/trd/jQuery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/findpass/findpassword.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/common/supportPlaceholder.js"></script>
</body>
</html>
