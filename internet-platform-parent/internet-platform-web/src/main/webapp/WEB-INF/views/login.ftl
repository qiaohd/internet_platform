<!DOCTYPE html>
<html>
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
		<title>请登录</title>
		<link href="trd/bootstrap/css/bootstrap.css" rel="stylesheet" tyep="text/css"/>
		<link href="trd/uui/css/u.css" rel="stylesheet" tyep="text/css"/>
		<link href="${ctx}/css/ip/login.css" rel="stylesheet" type="text/css">	
	</head>
	<body class="bodybg notop ng-scope" style="display: block;">
		<script>
			window.$ctx = '${ctx}';
			window.$isAuthcodeShow='${isAuthcodeShow}';
		</script>
	    <#assign mouse = "${ctx}">
	    <script>var code_path="${mouse!}";</script>
		<script>var path="${ctx}";</script>
		<script>var exponent="${exponent}";</script>
		<script>var modulus="${modulus}";</script>
		<script>var isAuthcodeShow="${isAuthcodeShow}";</script>
	    <form  id="uformlogin" method="post" action="${ctx}/login/uformLogin"><!-- ngView:  -->
	    <div id="main_wraper">
	        <div class="wraper">
	            <div class="logo">
	                <a href="#" target="_blank"><i class="icon-logo"></i></a>
	            </div>
	            <div class="inputwrap login-input">
	                <label class="lable-login"><i class="icon-user"></i></label>
	                <input tabindex="1" name="username" class="padr1" id="email" placeholder="手机/邮箱/登录帐号" type="text" onfocus="cleanTip6()" >
	                <a href="javascript:void(0);" class="inputclear input-clear" onclick="clearAccount()"><i class="icon-clear"></i></a>
	            </div>
	            <div id="checkLoginInfoDiv"><font color="#FF0000"><label id="loginInfoCheckMeg"></label></font></div>
	            <div class="errortip ng-hide" id="emailtip"></div>
	            <div>
	                <div class="inputwrap input-password login-input">
	                    <label class="lable-login"><i class="icon-lock"></i></label>
	                    <input tabindex="2" id="password" placeholder="密码(8-20位，区分大小写)" name="password" type="password" onfocus="cleanTip7()">
	                    <a href="javascript:void(0);" class="input-clear inputclear" onclick="clearPassword()"><i class="icon-clear"></i></a>
	                </div>
	                <div id="checkLoginPwdDiv"><font color="#FF0000"><label id="loginPwdCheckMeg"></label></font></div>
	                <div class="errortip ng-hide"></div>
	            </div>
	            <!-- 图片验证码初始化状态 -->
	            <#if isAuthcodeShow=="true">
	            <div class="smswrap clearfix">
	                <div class="changeimg">
	                    <a href="javascript:void(0);" onclick="changeImage()" ><i class="icon-change" onclick="changeImage()" ></i>换一换</a>
	                    <span><img id="captchaImage" src="${ctx}/login/getImage" onclick="changeImage()" style="position: absolute;"/></span>
	                </div>
	                <div class="authcode imgcode"><input tabindex="2" name="imageCode" id="code" placeholder="请输入图片内容" type="text" onfocus="cleanTip8()"></div>
	                <!--<div class="authcode imgcode"><input tabindex="2" name="imageCode" id="code" placeholder="请输入图片内容" type="text" onfocus="cleanTip8()"></div>-->
	            </div>
	            <div id="checkLoginImageDiv"><font color="#FF0000"><label id="loginLoginImageCheckMeg"></label></font></div>
				</#if>
					            
	            <#if (accounterror?exists)>
					<div class="form-group">
				      <div > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				        <font color="red" id="error_info">${accounterror}</font>
				      </div>
				    </div>
				</#if>
				<div class="errortip ng-binding ng-hide"></div>
	            <div class="tiptext">
	            <label class="u-checkbox w-64" style="width: 125px;">
	            	<input type="checkbox" id="check" name="rememberMe" class='u-checkbox-input'>
	            	<span style="color:#29e">30天内自动登录</span>
	            </label>
	            <span style="float:right;"><a href="${ctx}/login/findPassword">忘记密码?</a></span>
	            </div>
	            
	            <div class="submitwrap">
	                <a href="javascript:void(0);" class="btn-submit" onclick="doLogin() ">登 录</a>
	            </div>
	            <div class="tiplink">
	                <a href="${ctx}/register">没有帐号，免费注册 <i class="icon-arrow"></i></a>
	            </div>
	        </div>
		</div>
	    </form>
	    <script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
		<script src="${ctx}/trd/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
		<script src="${ctx}/trd/uui/js/u.min.js" type="text/javascript"></script>
		<script src="${ctx}/trd/jQuery-cookie/jquery.cookie.js" type="text/javascript"></script>
	    <script src="${ctx}/js/ip/login/login.js" type="text/javascript" ></script>
	    <script src="${ctx}/js/ip/common/supportPlaceholder.js" type="text/javascript" ></script>
		<script src="${ctx}/js/security.js" type="text/javascript"></script>
		<script src="${ctx}/js/userLogin.js" type="text/javascript"></script>
	</body>
</html>
