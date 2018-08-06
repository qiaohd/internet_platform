<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>注册</title>
   
    <link href="${ctx}/css/ip/login.css" rel="stylesheet" type="text/css">
   
</head>
<body>
<#assign mouse = "${ctx}">
    <script>var test = "${mouse!}";</script>
    <script>var path = "${ctx}";</script>

    <div class="top">
        <a id="indexLogo" class="toplogo" href="javascript:void(0)">
            <i class="icon-toplogo"></i>
        </a>
        <a class="btn-login ng-binding" href="${ctx}/login">登录</a>
    </div>
    <div class="wraper">
        <div class="logo">
            <span class="text">创建单位/机构帐号</span>
            <div class="regtypetip">每个单位只需要注册一次，注册人就是管理员</div>
        </div>
        <input type="hidden" id="tip1">
        <input type="hidden" id="tip2">
        <form method="post" id="formRegister" action="${ctx}/register/formRegister">
            <div class="register_wrap">
                <div class="inputwrap clearfix email_input_wrap">
                
                    <div class="mobiarea">
                        <label id="regway-list-label" class="inline_block username_type_switch"
                               onClick="document.getElementById('regway-list').style.display='block'">
                            <span id="selReg" class="ng-binding">+86</span>
                            <i class="icon-arrowdown"></i>
                        </label>
                        <ul class="regway-list" id="regway-list">
                            <li><a href="javascript:void(0);" onClick="selectlist(0)">手机</a></li>
                            <li><a href="javascript:void(0);" onClick="selectlist(1)">邮箱</a></li>
                        </ul>
                    </div>
                   
                   
                    <div class="regmobi">
                        <input tabindex="1" name="phoneNumber" id="phoneNo" autocomplete="off"
                                placeholder="请输入手机号" class="padr1" type="number"
                               onBlur="checkPhone(this)" onfocus="cleanTip1()">
                    </div>
                    <a href="javascript:void(0);" class="inputclear input-clear"><i class="icon-clear"></i></a>
                </div>
                <div id="checkPhoneDiv"><font color="#FF0000"><label id="PhoneCheckMeg"></label></font></div>
                <div id="checkMailDiv"><font color="#FF0000"><label id="mailCheckMeg"></label></font></div>
                <div class="errortip ng-binding ng-hide" id="emailtip"></div>
                <!-- 图片验证码初始化状态 -->
                <div class="smswrap clearfix">
                    <div class="changeimg">
                        <a href="javascript:void(0);" onclick="changeImage()"><i class="icon-change"
                                                                                 onclick="changeImage()"></i>换一换</a>
                        <span style="position: absolute;"><img id="captchaImage" src="${ctx}/login/getImage" onclick="changeImage()"></span>
                    </div>
                    <div class="authcode imgcode"><input tabindex="2" name="imgcode" id="code" placeholder="请输入图片内容"
                                                         type="text" onBlur="registerCheckImage(this)"
                                                         onfocus="cleanTip2()"></div>
                </div>
                <div id="checkImageDiv"><font color="#FF0000"><label id="ImagCheckMeg"></label></font></div>
                <div class="errortip ng-binding ng-hide"></div>
                <!-- 验证码初始化状态 -->
                <div class="auth-code">
                    <div class="smswrap clearfix">
                        <a href="javascript:void(0);" class="getauth ng-binding effect" onclick="getPhoneCode(this)"
                           id="get_phoneCode"><i class="icon-authsuccess"></i>获取验证码</a>
                        <div class="authcode"><input tabindex="3" name="phoneCode" id="authCode" placeholder="验证码"
                                                     type="text" onfocus="cleanTip3()" onBlur="phoneActiveCode(this)">
                        </div>
                    </div>
                    <div id="phoneCheckImageDiv"><font color="#FF0000"><label id="phoneImagCheckMeg"></label></font>
                    </div>
                    <div class="errortip ng-binding ng-hide"></div>
                </div>
                <div>
                    <div id="register-password" class="inputwrap input-password ">
                        <label class="lable-login"><i class="icon-lock"></i></label>
                        <input tabindex="4" id="password" placeholder="密码（8-20位，区分大小写）" name="password" class="cutpad"
                               type="password" onFocus="passwordtip()" onBlur="passwordtip_hide(this)">
                        <a href="javascript:void(0);" class="input-clear inputclear moveleft"><i class="icon-clear"></i></a>
                        <a href="javascript:void(0);" class="inputclear psd-visible"><i class="icon-eye"></i></a>
                        <div class="inputProgress ng-hide" id="passwordtips">
                    <span class="triangle">
                        <span class="inner"></span>
                    </span>
                            <ul class="tip-list">
                                <li class="tip-item item1 normal">
                                    <i class="tip-icon"></i><span class="tip-text">长度8-20位</span>
                                </li>
                                <li class="tip-item item2 normal">
                                    <i class="tip-icon"></i><span class="tip-text">支持数字、字母（区分大小写）、标点符号</span>
                                </li>
                                <li class="tip-item item3 normal">
                                    <i class="tip-icon"></i><span class="tip-text">不允许空格</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div id="checkpwdDiv"><font color="#FF0000"><label id="pwdCheckMeg"></label></font></div>
                    <div class="errortip ng-binding ng-hide"></div>
                </div>

                <div>
                    <div id="register-re-password" class="inputwrap input-password">
                        <label class="lable-login"><i class="icon-lock"></i></label>
                        <input tabindex="4" id="re-password" placeholder="再次确认密码" name="password" class="cutpad"
                               type="password" onFocus="re_passwordtip()" onBlur="re_passwordtip_hide(this)">
                        <a href="javascript:void(0);" class="input-clear inputclear moveleft"><i class="icon-clear"></i></a>
                        <a href="javascript:void(0);" class="inputclear psd-visible"><i class="icon-eye"></i></a>
                        <div class="inputProgress ng-hide" id="re-passwordtips">
                    <span class="triangle">
                        <span class="inner"></span>
                    </span>
                            <ul class="tip-list">
                                <li class="tip-item item1 normal">
                                    <i class="tip-icon"></i><span class="tip-text">长度8-20位</span>
                                </li>
                                <li class="tip-item item2 normal">
                                    <i class="tip-icon"></i><span class="tip-text">支持数字、字母（区分大小写）、标点符号</span>
                                </li>
                                <li class="tip-item item3 normal">
                                    <i class="tip-icon"></i><span class="tip-text">不允许空格</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div id="re-checkpwdDiv"><font color="#FF0000"><label id="re-pwdCheckMeg"></label></font></div>
                    <div class="errortip ng-binding ng-hide"></div>
                </div>

                <div class="inputwrap input-person-name">
                    <input tabindex="5" id="personName" placeholder="姓名" type="text" class="cutpad padr3"
                           name="username" onBlur="regCheckUserName(this)" onfocus="cleanTip4()">
            <span class="gender" id="sex">
                <label onclick="selectOption(this)">男<i class="icon-radio icon-radioclick"></i></label><label
                    onclick="selectOption(this)">女<i class="icon-radio"></i></label>
                <input id="sex1" type="hidden" name="sex" value="1"/>
            </span>
                </div>
                <div id="checkPersonNameDiv"><font color="#FF0000"><label id="personNameCheckMeg"></label></font></div>

                <div class="errortip ng-binding ng-hide"></div>
                <div class="inputwrap">
                    <input tabindex="6" id="companyName" placeholder="单位名称" class="cutpad" type="text"
                           name="companyName" onBlur="regCheckCompanyName(this)" onfocus="cleanTip5()">
                </div>
                <div id="checkCompanyName1Div"><font color="#FF0000"><label id="companyNameCheckMeg"></label></font>
                </div>
                <div class="errortip ng-binding ng-hide"></div>
                <div class="region-choice">
                	<label class="own-region">所属区域</label>
	    			<select name="dbArea" id="dbArea" class="inputwrap"><option>无分区</option></select>
	    		</div>
                <div class="tiptext disagree">
                    <label for="ieagreelable"><i class="icon-check icon-checked" id="check"></i>
                        <input id="ieagreelable" name checked class="position_hidden" type="redio">我已阅读并同意</label>
                    <a target="_blank" href="${ctx}/info/serve_treaty" class="font_blue">《服务条款》</a>
                    <a target="_blank" href="${ctx}/info/secrecy_treaty" class="font_blue">《保密协议》</a>
                </div>

                <div id="choiceService"><font color="#FF0000"><label id="choiceServiceCheckMeg"></label></font></div>
                <div class="errortip disagree ng-hide">要使用我们的服务，您必须同意北京用友政务软件有限公司的服务条款</div>
                <div class="submitwrap">
                    <a href="javascript:void(0);" class="btn-submit ng-binding" onclick="startRegister()">创建单位</a>
                </div>
            </div>
        </form>
        <div class="backreg">
            <a href="${ctx}/info/vcodeError" target="_blank">收不到验证码？</a>
        </div>
    </div>
    <div class="tipwrapbot">Copyright &copy; Zhengwu Corp. All Rights Reserved.</div>
    <script src="${ctx}/trd/jquery/jquery-1.12.3.min.js" type="text/javascript"></script>
    <script src="${ctx}/js/ip/common/supportPlaceholder.js" type="text/javascript"></script>
    <script src="${ctx}/js/ip/login/login.js" type="text/javascript"></script>
    <script src="${ctx}/js/userLogin.js" type="text/javascript"></script>
    <script src="${ctx}/js/register.js" type="text/javascript"></script>
</body>
</html>
