<#include "header.ftl">
		<link href="${ctx}/css/find_password.css" rel="stylesheet" type="text/css" />
        <link href="${ctx}/css/reset.css" rel="stylesheet" type="text/css" />
		<div class="main">
			<div class="container-m">
				<div class="register-main">
					<div class="reg-title">
						用户中心－修改密码
						<a href="${ctx}/login/postpage" class="a-back">返回</a>
					</div>
					<div class="reset-password-main">
					<div class="fpassword-m" id="Step2">
						<div class="fpassword-1">
							<table width="100%">
								<tr>
									<td class="text-right">请输入新密码：</td>
									<td><input type="password" class="inputwrap" id="password" onblur="checkPassword()"></td>
									<td class="reg-tips"></td>
								</tr>
								<tr>
									<td></td>
									<td><div id="pwdCheckMeg" class="errortip ng-hide"></div></td>
									<td></td>
								</tr>
								<tr>
									<td class="text-right">请再次输入密码：</td>
									<td><input type="password" class="inputwrap" id="re-password" onblur="checkRepassword()"></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td><div id="re-pwdCheckMeg" class="errortip ng-hide"></div></td>
									<td></td>
								</tr>
							</table>
							<div class="nextbt">
								<a href="javascript:void(0)" class="btn-next" onclick="editPassword()">确定</a>
							</div>
						</div>
					</div><!--第二步 end -->
					<div class="fpassword-m" style="display:none" id="Step3">
						<div class="fpassword-1">
							<div class="success-icon">
								恭喜你，账号密码修改成功！
							</div>
							<div class="nextbt" style="padding-left: 0; text-align: center;">
								<a href="../login" class="btn-next">返回登录界面</a>
							</div>
						</div>
					</div><!--第三步 end -->
				</div>
				</div>
			</div>
		</div>
		<div class="footer">
			<p>主办：江苏省财政厅  备案号: 苏ICP备05009673号</p>
			<p>江苏省财政厅版权所有</p>
		</div>
		<script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
		<script src="${ctx}/js/findpassword.js" type="text/javascript"></script>
	</body>
</html>
