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
        <span class="text">您正在找回帐号：<label id="phoneNo"></label></span>
    </div>
    <div class="inputwrap ng-scope hasContent">
        <label class="lable-login"><i class="icon-user"></i></label>
        <label id="selectShowWrap" ng-click="showSelect()"><div class="inputdisabled ng-binding" id="userPhone">158*****505(手机验证)</div><a href="javascript:void(0);" class="inputclear"><i class="icon-arrowdown"></i></a></label>
        <input tabindex="1" name="username" id="email" class="position_hidden ng-pristine ng-valid ng-touched" ng-model="selectType" ng-init="emailError=false" type="text">
        <ul class="findaccount-list ng-hide" ng-show="selectShow" ng-init="selectShow=false">
            <li><!-- ngIf: tel_account --><a class="ng-binding ng-scope" href="javascript:void(0);" id="userPhone1"></a><!-- end ngIf: tel_account --></li>
            <li><!-- ngIf: mail_account --></li>
        </ul>
    </div>
    <div id="checkUserPhone" style="color:red;"></div>
 	<!-- 验证码初始化状态 -->
    <div class="auth-code">
    <div class="smswrap clearfix">
	    <a href="javascript:void(0);" class="getauth ng-binding effect" onclick="getPhoneCode(this)"
	    id="get_phoneCode"><i class="icon-authsuccess"></i>获取验证码</a>
	    <div class="authcode"><input tabindex="3" name="phoneCode" id="authCode" placeholder="验证码" type="text" ></div>
    </div>
    <div id="phoneCheckImageDiv"><font color="#FF0000"><label id="phoneImagCheckMeg"></label></font></div>
    <div id="authcodeMsg" style="color:red;"></div>
     </div>
    <div class="submitwrap">
        <a href="javascript:void(0)" class="btn-submit ng-binding" id="nextStep2">下一步</a>
    </div>
    <div class="backreg">
        <a href="${ctx}/login/findPasswordError">收不到验证码？</a>
    </div>
</div>
<script type="text/javascript" src="${ctx}/trd/jquery/jquery-1.12.3.min.js"></script>
<script type="text/javascript" src="${ctx}/trd/jQuery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/findpass/findpasswordstep2.js"></script>
<script type="text/javascript" src="${ctx}/js/ip/common/supportPlaceholder.js"></script>
</body>
</html>
