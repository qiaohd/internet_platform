<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收不到验证码</title>
<link href="${ctx}/css/main_treaty.css" rel="stylesheet" type="text/css">
</head>

<body>
<#assign mouse = "${ctx}">
    <script>var test="${mouse!}";</script>
	<script>var path="${ctx}";</script>

<div class="top">
	<a id="indexLogo" class="toplogo" href="javascript:void(0)">
		<i class="icon-toplogo"></i>
	</a>
	<a class="btn-login ng-binding" href="${ctx}/login">登录</a>
</div>
<div class="tipwarp">
		<ul class="tip-list">
            <li class="title">收不到验证码？</li>
            <li>如果您使用的是手机注册，但无法接收到验证码的短信，建议您切换注册方式，使用邮箱进行注册；<br></li>
            <li>如果您使用的是邮件注册，但一直未接收到验证码<br>• 有可能您的邮箱设置屏蔽了我们的邮件，建议设置****.com为白名单域名<br>• 有可能被误判为垃圾邮件了，请到垃圾邮件文件夹查找。</li>
        </ul>
</div>
<div class="tipwrapbot">Copyright &copy; Zhengwu Corp. All Rights Reserved.</div>
</body>
</html>
