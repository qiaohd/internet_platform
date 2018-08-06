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
        <span class="text">重置密码</span>
    </div>
    <div>
        <div class="inputwrap input-password login-input">
            <label class="lable-login"><i class="icon-lock"></i></label>
            <input tabindex="2" history_value="" id="password" placeholder="密码(8-20位，区分大小写)" validate="" name="password" type="password">
            <a href="javascript:void(0);" class="input-clear inputclear"><i class="icon-clear"></i></a>
        </div>
        <div class="" id="pwdCheckMeg"></div>
    </div>
    <div>
        <div class="inputwrap input-password login-input">
            <label class="lable-login"><i class="icon-lock"></i></label>
            <input tabindex="2" history_value="" id="re-password" placeholder="确认密码" validate="" name="password" type="password">
            <a href="javascript:void(0);" class="input-clear inputclear"><i class="icon-clear"></i></a>
        </div>
        <div class="" id="re-pwdCheckMeg"></div>
    </div>
    <div class="submitwrap">
        <a href="javascript:void(0)" class="btn-submit ng-binding" id="nextStep3">下一步</a>
    </div>
</div>
<script type="text/javascript" src="${ctx}/trd/jquery/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="${ctx}/trd/jQuery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/findpass/findpassword.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/common/supportPlaceholder.js"></script>
</body>
</html>
