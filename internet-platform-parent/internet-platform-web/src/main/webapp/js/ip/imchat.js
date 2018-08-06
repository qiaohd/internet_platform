/*
 * 登录的调用是在index.js调用的
 */
var hirerId, userId;
var loginIm = function (username,password,isToken) {
    //请求后台判断当前登录的是租户还是普通用户
    
    $.ajax({
			type: "GET",
			url: $ctx +"/userset/getLoginId",
			async:false,
			success: function(data){
				hirerId=data.hirerId;
				userId=data.userId;
			}
		});
	if(hirerId=="" || hirerId==null || hirerId==undefined){
	
        if (!SNSCommonUtil.isStringAndNotEmpty(username) && YYIMChat.enableAnonymous()) {
                YYIMChat.login(null, "anonymous", new Date().getTime() + 14 * (24 * 60 * 60 * 1000));
                return;
            }
            if (!username || username.isEmpty() || !password || password.isEmpty()) {
                //throw '用户名或密码为空';
            }

            SNSApplication._loginName = username;
            parseName(username);
            username = username.replace(/@/g, YYIMChat.getTenancy().SEPARATOR);

            if (DEVELOP_MODE) {
                YYIMChat.login(username, $.md5(password).toUpperCase());
                return;
            }
            if (isToken && isToken == true)
                YYIMChat.login(username, password);
            else
                SNSApplication.prototype.getToken(username, password);

            function parseName() {
                $.ajax({
                    type: 'POST',
                    url: $ctx+"/imaccess/getImParam",
                    dataType: 'json',
                    async: false,
                    success: function (data) {
                        
                        var config={
                            "address":data.address,
                            "wsport":data.wsport,
                            "hbport" :data.hbport,
                            "servlet" :data.servlet,
                            "safeServlet" :data.safeServlet
                        }
                        
                        YYIMChat.initSDK(data.appId, data.eptId,config);
                    }
                });
            }
        }else{
        	//alert("是租户，没法进行聊天");
        }
}

$(document).ready(function(){
	
	jQuery("div.link-item a").mouseover(function(event) {
		var img = $(this).find("img");
		var src = img.attr("src");
		img.attr("src", src.replace(".png", "_hl.png"));
		event.stopPropagation();
	});
	
	jQuery("div.link-item a").mouseout(function(event) {
		var img = $(this).find("img");
		var src = img.attr("src");
		img.attr("src", src.replace("_hl.png", ".png"));
		event.stopPropagation();
	});
	
	jQuery('#snsim_chat_window_tab_head').on('click','li',function(){
		setTimeout(function(){jQuery('#snsim_sendbox_content').focus();},0);
	})

	// 登陆的回车事件 -- start
	//jQuery("#username").focus();
	// 登陆的回车事件 --end
	/*
     * 登录的初始化方法
     */
	
    var username = jQuery("#user").text();
    var flag=false;  // 是否显示  ----已经登录并显示
	var islogin=false; // 是否登录---已经登录
	
		jQuery("img.loginBtn").bind("click", function() {
				if(userId!="" && userId!=null && userId!=undefined){
					if(navigator.appName == "Microsoft Internet Explorer" && (navigator.appVersion.match(/9./i)=="9." || navigator.appVersion.match(/8./i)=="8.")){
				       return;
				    }else{
						var username = jQuery("#user").text();
						if(flag){
							if(islogin){
								if(username){
									if(username && password){
										//SNSApplication.getInstance().login(username, '12346');
										//loginIm(username,'', false);
										islogin=false;
									}
								}	
							}else{
							    //alert(document.getElementById('snsim_window_narrow').style.display);
							    if(document.getElementById('snsim_window_narrow').style.display=='none' || document.getElementById('snsim_window_narrow').style.display==''){
									$("#snsim_window_wide").show();
								}else{
									$("#snsim_window_narrow").show();
								}
							}
							 flag=false;
						}else{
							flag=true;
							if(document.getElementById('snsim_window_narrow').style.display=='block'){
									$("#snsim_window_narrow").hide();
								}else{
									$("#snsim_window_wide").hide();
								}
						}
						}
				}else{
					return false;
				}
			});

	
	
	
	//jQuery("img.loginBtn").click();
	
	/*
	jQuery("#username").on({
		"blur":function(event){
			checkUsername(jQuery("#username")[0]);
		},
		"keydown":function(event) {
			if (event.keyCode == 13){
				checkUsername(jQuery("#username")[0]);
				if(this.value.length){
					jQuery("#userpwd").focus();
				}
			}
		}
	});
	*/
	/*
	jQuery("#userpwd").on({
		"blur":function(event){
			checkPassword(jQuery("#userpwd")[0]);
		},
		"keydown":function(event) {
			if (event.keyCode == 13){
				if(this.value.length){
					jQuery("input.loginBtn").trigger("click");
				}
			}
		}
	});
	*/
	/*
	function checkUsername(obj){
		username = trim(obj.value);
		var exp = /\w+((-w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+/;
		if(exp.test(username)){
			obj.value = username;
			jQuery('#errmsg').hide();
			return username;
		}else{
			obj.value = '';
			jQuery('#errmsg').html('用户名格式不正确').show();
			return;
		}
	}
	*/
	/*
	function checkPassword(obj){
		password = obj.value;
		var exp = /^\S+$/;
		if(exp.test(password)){
			jQuery('#errmsg').hide();
			return password;
		}else{
			jQuery('#errmsg').html('密码不能为空').show();
			return;
		}
	}
	*/
	/*
	function trim(t){
		return (t||"").replace(/^\s+|\s+$/g, "");
	} 
	
   */
   /*
	function afterLogin() {
		jQuery(".loginPanel").css("display", "none");
		jQuery("body").addClass("bg");
		jQuery("#linkItems").css("display", "block");
	}
	*/
	
	
	});


