<script>
	window.$ctx = '${ctx}';
</script>
<script>
var agent = navigator.userAgent.match(/MSIE\s(\d)./);
if(agent && agent[1]){
	var IEversion = agent[1];

	if(window.parseInt(IEversion) < 9){
		//alert(window.parseInt(IEversion));
	   /*
		alert("您的浏览器不支持此页面，请使用chrome、firefox、IE9以上版本,或使用极速模式！");
		var _html = document.getElementsByTagName('html')[0];
		_html.parentNode.removeChild(_html);
		*/
	}else{
		var snsLoginConflict = false;
	}
	
}
function getSNSBasePath(){
	return $ctx ;
};
</script>

<link rel="stylesheet" type="text/css" href="${ctx}/trd/res/skin/homepage.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/trd/res/skin/default/css/YYIMUIDemo.css" />
<div class="bg">

		<div id="linkItems" style="display: none;">
            <div class="link-item">
                、
            </div>
            <div class="link-item">
                 、
            </div>
            <div class="link-item">
                  、
            </div>
            <div class="link-item">
                 、
            </div>
      </div>



      <!-- 登陆框 -->
      <div class="loginPanel" style="z-index:1000;display:none">
            <div class="content-960">
                  <p style="margin-top: 15%;">
                        <img src="/internet-platform-web/trd/res/skin/default/icons/login_logo.png" style="width: 79px; height: 78px;">
                  </p>
                  <p style="margin-top: 17px; font-size: 24px;color: #028b7d;">用户登录</p>
                  <div class="login-area" style="positin:relative;">
                        <form id="loginform" action="" method="post">
                              <p id="errmsg" style="color: red; background-color: #fedbd1; line-height: 28px; font-size: 14px; display: none;position:absolute;width:100%;height:28px;left:0;top:0;">
                                    <!-- 错误信息 -->
                              </p>
                              <input id="username" value="aaa@aa.aa" name="uname" class="login-text" type="text" placeholder="name@app.corp" title="name@app.corp" value="" autofocus="autofocus"/>
                              <input id="userpwd" name="psw" value='1' class="login-text" type="password" placeholder="请输入密码" title="请输入密码" value="" />
                              <div>
	                              <input class="loginBtn fl" type="button" value="登录">
	                              <!-- <input class="registBtn fr" type="button" value="注册" OnClick="window.location.href='register.html';" style="background: #A7A7A7;margin-left:2%;"> -->
                              </div>
                              <div style="clear:both;"></div>
                        </form>
                  </div>
	            <div class="ewm">
	            </div>
            </div>
      </div>
      
      <div id="snsim_webim" class="sns_webim snsim_webim">

      <!-- 右侧好友列表===窄版 -->
      <div id="snsim_window_narrow" class="snsim_list snsim_fold">
            <div class="snsim_list_wrap ">
                  <div title="切换状态" style="cursor: pointer;" class="snsim_state_new clearfix">
                        <a class="snsim_user_presence_btn" href="javascript:void(0);">
                              <!-- show -->
                              <span style="font-size: 12px;">
                                    <i class="W_fl snsim_user_presence snsim_unavailable"></i>
                                    <span class="snsim_user_presence_text">离线</span>
                              </span>
                        </a>
                  </div>
                  <div class="snsim_list_main clearfix">
                      <ul id="narrow_roster_container" class="narrow_roster_container list_content_li"></ul>
                  </div>
                  <div class="snsim_list_setting">
                     <div class="state_setting_btn">
                        <a class="snsim_toggle_fold_btn unfold_btn">
                             <span class="snsim_icon_leftfold1"></span>
                        </a>
                     </div>
                  </div>
            </div>
            <a style="cursor: pointer;" href="javascript:void(0);" class="snsim_toggle_fold_btn snsim_fold_btn">
                  <span class="btn_click">
                        <!--<em class="sns_snsim_arrow"></em>-->
                  </span>
            </a>
      </div>

      <!-- 右侧好友列表===宽版 -->
      <div id="snsim_window_wide" node-type="maxRoot" class="snsim_list snsim_unfold">
            <div node-type="listWrap" class="snsim_list_wrap ">
                  <div id="snsim_wide_tab_container" class="snsim_list_header clearfix">
                        <div class="head_config_panel hide">
                              <a class="system_config" onclick="javascript:SNSSkin.changeSkin();">
                                    <span class=""></span>
                              </a>
                        </div>
                        <div id="snsim_local_search" class="snsim_search searchList_head">
                           <a id="snsim_local_search_btn" class="snsim_icon_search_lb"></a>
                           <input id="snsim_local_search_input" type="text" placeholder="查找联系人" class="searchList_head" />
                           <div class="local_search_result">
                               <ul class="snsim_local_search_result_list list_content_li"></ul>
                           </div>
                        </div>
                        <div id="snsim_wide_tab_head_container" node-type="snsim_max_tab_root" class="snsim_tab">
                              <ul class="tab_list clearfix">
                                    <li id="snsim_tab_head_roster" title="好友" node-type="snsRostersTab"
                                          class="snsim_tab_head snsim_tab_head_roster clearfix cur">
                                          <a href="javascript:void(0);">
                                                <span class=" snsim_icon_tab snsim_icontab_link"></span>
                                          </a>
                                    </li>
                                    <li id="snsim_tab_head_chatroom" title="群聊" node-type="snsGroupsTab"
                                          class="snsim_tab_head  snsim_tab_head_chatroom clearfix">
                                          <a href="javascript:void(0);">
                                                <span class=" snsim_icon_tab snsim_icontab_group "></span>
                                          </a>
                                    </li>
                              </ul>
                        </div>

                        <div id="snsim_wide_tab_content_container" node-type="maxMain" class="snsim_list_main clearfix">
                              <div id="snsim_tab_content_roster" class=" snsim_tab_content snsim_tab_content_roster snsRostersScroll cur">
                                    <div id="grouproster_container" class="snsim_list_con" node-type="snsRostersContainer">
                                          <!-- 联系人列表 -->
                                    </div>
                              </div>
                              <!-- chatroom列表 -->
                              <div id="snsim_tab_content_chatroom" class="snsim_tab_content snsGroupsScroll" node-type="snsGroupsScroll">
                                    <div class="snsim_list_con" node-type="snsGroupsContainer">
                                          <div class="snsim_chat_tips snsim_tips01">
                                                <div node-type="snsNoGroups" class="nogroup_add">
                                                      <div class="add_list">
                                                            <ul>
                                                                  <li>
                                                                        <a class="snsim_create_chatroom_btn add_list_box" node-type="CreateChatRoom"
                                                                              hidefocus="true" href="javascript:void(0);">
                                                                              <span class="add_list_txt" hidefocus="true">创建群组</span>
                                                                              <span class="snsim_icon_groupcreat"></span>
                                                                        </a>
                                                                  </li>
                                                            </ul>
                                                      </div>
                                                </div>
                                          </div>
                                          <!-- chatroom名称输入 -->
                                          <div class="sns_chatroom_name_div">
                                                <p>
                                                      <input type="text" placeholder="请输入群组名称" class="sns_chatroom_name_input" hidefocus="true">
                                                      <a class="sns_chatroom_name_btn W_fr" style="border: none;height: 22px;padding-right: 13px;background: none;">
                                                            <span style="background-color: #ffa00a;padding: 0px 1px 1px 4px;height: 19px;line-height: 21px;margin-right: 0px;">确认</span>
                                                      </a>
                                                </p>
                                          </div>
                                          <div class="list_box clearfix">
                                                <div class="chatRoomListContent">
                                                      <ul id="snsim_chatroom_list_container" class="list_content_li">
                                                      </ul>
                                                </div>
                                          </div>
                                    </div>
                              </div>
                        </div>
                  </div>
                  <div node-type="snsBottomContainer" class="snsim_list_setting">
                        <div  id="snsim_wide_window_bottom_container"  class="list_setting_box">
                              <div class="setting_r">
                                    <a href="javascript:void(0);" class="snsim_toggle_fold_btn setting_btn" title="回到窄版">
                                          <span class="snsim_icon_rightfold"></span>
                                    </a>
                              </div>
                              <div node-type="onlineStatus" class="online_status">
                                    <a class="setting_cho snsim_user_presence_btn" href="javascript:void(0);">
                                          <em node-type="onlineClass" style="margin-top: 2px;float: left;" class="snsim_user_presence snsim_unavailable"></em>
                                          <span node-type="onlineText" class="snsim_user_presence_text setting_txt">离线</span>
                                    </a>
                              </div>
                              <!-- 系统消息 -->
                              <a id="snsim_unread_message_indicator" node-type="systemMessages" class="system_messages system_messages_bg" title="系统消息">
                                    <span></span>
                              </a>
                              <div node-type="layerSetting" class="layer_setting"></div>
                        </div>
                  </div>
            </div>
            <a href="javascript: void(0);" class="snsim_toggle_fold_btn snsim_fold_btn">
                  <span class="btn_click list_unfold">
                        <!--<em class="sns_snsim_arrow"></em>-->
                  </span>
            </a>
      </div>

      <div id="snsim_list_relink" class="snsim_chat_tips snsim_tips02 snsim_list_relink">
            <span class="container" id="snsim_connecting_wait">
                  <span>连接服务器中断，</span>
                  <span id="snsim_relink_wait_seconds">10</span>
                  秒后重新连接，
                  <a id="snsim_relink_connect_imm" href="javascript:void(0);" title="立即连接">立即连接</a>
            </span>
            <span class="container" id="snsim_relink_connecting">
                  <span class="snsim_loading">正在连接，</span>
                  <a id="snsim_relink_cancel_connect" href="javascript:void(0);" title="取消连接">取消</a>
            </span>
            <span class="container" id="snsim_relink_connecting_fail">
                  连接失败，
                  <a id="snsim_relink_reconnect_imm" href="javascript:void(0);" title="重新连接">重新连接</a>
            </span>
      </div>

      <div class="snsim_chat_box" id="snsim_chat_box" style="border-radius: 6px;">
            <!-- vcard, 更改密码及配置窗口 -->
            <div id="snsim_settings_window" class="sns_setting_win">
                  <div class="snsim_tit2_rt">
                        <a class="rt_icon snsim_icon_close1 snsim_settings_window_close" title="关闭"></a>
                  </div>
                  <div class="sns_setting_win_header">
                        <ul class="sns_setting_win_header_container">
                              <li id="snsim_user_info_head" class="snsim_tab_head cur">
                                    <a>个人信息</a>
                              </li>
                              <li id="snsim_system_config_head" style="display: none;" class="snsim_tab_head">
                                    <a>系统设置</a>
                              </li>
                              <li id="snsim_change_pasword_head" style="display: none;" class="snsim_tab_head">
                                    <a>更改密码</a>
                              </li>
                        </ul>

                  </div>
                  <div class="sns_setting_win_body">
                        <ul>
                              <li id="snsim_user_info_content" class="sns_setting_win_body_item snsim_tab_content snsim_tab_user_info cur">
                                <div id="snsim_user_info_settings" style="position: absolute;overflow: hidden;height: 400px;margin-top: 34px;">
                                   <div class="snsim_user_info_settings">
                                   	<div id="snsim_avatar_uploader" class="avatarUploaderContainer">
	                                    <div class="head_icon">
						       				<span style="font-size: 12px;vertical-align: top;">头像</span>
						       				<img class="user_settings_head_icon" src="/internet-platform-web/trd/res/skin/default/icons/normal_pic_50x50.png">
						       				<!-- <span id="user_change_head_icon"></span> -->
						       				<a id="user_change_head_icon" class="avatar_upload_btn" title="更改头像">更改头像
                                            </a>
                                           	<input id="user_avatar_upload_input" style="visibility:hidden; height:1px;width:1px;" accept="image/*" type="file">
						       				<span class="instruction">仅支持JPG、JPEG、PNG文件且小于5M</span>
			       						</div>
                                    </div>
                                    <div id="user_avatar_upload_progress_container" class="hide"></div>
							       	<div id="snsim_user_avatar_uploader_preview" class="user_avatar_uploader_preview hide">
							       		<div id="bgDiv_user" class="bgDiv">
										       	<div id="dragDiv_user" class="dragDiv">
										          <div id="rRightDown_user" class="rRightDown"> </div>
										          <div id="rLeftDown_user" class="rLeftDown"> </div>
										          <div id="rRightUp_user" class="rRightUp"> </div>
										          <div id="rLeftUp_user" class="rLeftUp"> </div>
										        </div>
								      	</div>
							       	</div>
                                    <div class="snsim_vcard_container"></div>
                                   </div>
                                 </div>
                                  <div class="sns_vcard_change_btn_div">
                                  	 <div class="sns_vcard_change_bottom">
                                  	  	<p style="float: right;padding-top: 10px;text-align: center;margin-right: 5px;">
                                        <a class="sns_vcard_change_btn_cancel">
                                          <span>取消</span>
                                        </a>
                                        <a class="sns_vcard_change_btn">
                                          <span>确定</span>
                                        </a>
                                       </p>
                              	 	</div>
                                  </div>
                              </li>
                              <li id="snsim_system_config_content" class="sns_setting_win_body_item snsim_tab_content" style="border-radius: 0 7px 0 0;">
                                    <div class="sns_sys_config" onclick="SNSSkinPlugin.getInstance().changeSkin('dark')"><div style="padding-top: 200px;padding-left: 150px;">夜间模式</div></div>
                              </li>
                              <li id="snsim_change_pasword_content" class="sns_setting_win_body_item snsim_tab_content">
                                    <div class="sns_psw_change_container">
                                       <table summary="个人资料" cellspacing="0" cellpadding="0">
                                          <tbody>
                                             <tr>
                                               <th><span class="rq" title="必填">*</span>原密码:</th>
                                               <td><input type="password" name="oldpassword" id="oldpassword" class="sns_psw_change_input"></td>
                                               <td><span class="info">8-12位数字字母组合</span></td>
                                             </tr>
                                             <tr>
                                                <th><span class="rq" title="必填">*</span>新密码:</th>
                                                <td><input type="password" name="newpassword" id="newpassword" class="sns_psw_change_input"></td>
                                                <td><span class="info">8-12位数字字母组合</span></td>
                                             </tr>
                                             <tr>
                                                <th><span class="rq" title="必填">*</span> 确认新密码:</th>
                                                <td><input type="password" name="newpassword2" id="newpassword2" class="sns_psw_change_input"></td>
                                                <td><span class="info">8-12位数字字母组合</span></td>
                                             </tr>
                                             <tr>
                                                <th></th>
                                                <td>
                                                  <span class="sns_psw_change_error_msg"></span>
                                                </td>
                                                <td></td>
                                             </tr>
                                          </tbody>
                                       </table>
                                       <div class="sns_psw_change_btn_div">
                                       	  <div class="sns_psw_change_bottom">
                                       	  	<p style="float: right;padding-top: 10px;text-align: center;margin-right: -1px;">
	                                          <a class="sns_psw_change_btn_cancel">
	                                            <span>取消</span>
	                                          </a>
	                                          <a class="sns_psw_change_btn">
	                                            <span>确定</span>
	                                          </a>
	                                        </p>
	                                      </div>
                                        </div>
                                    </div>
                              </li>
                        </ul>
                  </div>
                  <div class="snsim_settings_bottom">
                  </div>
            </div>

            <div id="snsim_chat_window" class="snsim_chat_window  snsim_chat_con" style="display: none;border-radius: 6px;">
                  <div class="snsim_chat_wrap clearfix">
                        <a class=" sns_curchat_pic" href="javascript:void(0);">
                          <span class="head_pic">
                             <img id="show_chatroom_members" class="snsim_current_roster_photo curchat_pic"  src="${ctx}/trd/res/skin/default/icons/noavatar_big.gif"/>
                            
                            <!-- <img id="show_chatroom_members" class="snsim_current_roster_photo curchat_pic"  src="${ctx}/images/ip/menu/add.png"/> -->
                          </span>
                          
                        </a>
                        <div class="snsim_win_mutiperson" style="visibility: visible;border-radius: 6px 0 0 6px;">
                              <div class="snsim_chat_lf" style="border-radius: 0 0 0 6px;">
                                    <div class="snsim_chat_friend_box">
                                          <ul id="snsim_chat_window_tab_head" class="snsim_chat_window_tab_head snsim_chat_friend_list">
                                                <!-- chatWindowLeft -->
                                          </ul>
                                    </div>
                                    <a class="snsim_scroll_top"></a>
                                    <a class="snsim_scroll_bottom"></a>
                              </div>
                        </div>
                        <!--右半边-->
                        <div class="snsim_chat_rt" style="position: relative;">
                        	<div style="position: absolute;margin-top: 37px;">
                        	  <div id="udnProcessBar" style="position: relative;background: #fff;z-index: 9;width: 346px;max-height: 107px;padding-left: 10px;overflow: hidden;"></div>
                        	</div>
                            <!--右边的头-->
                            <div id="snsim_chat_rt_title" class="snsim_tit2" style="border-radius: 0 6px 0 0;">
                                  <div class="snsim_titin" style="margin: 15px 5px 0 0 ;">
                                        <!-- chatTitle -->
                                        <!-- <span id="show_chatroom_members"></span> -->
                                        <!--聊天窗口顶部好友名称 -->
                                        <div class="snsim_tit2_rt" style="float: right; height: 16px;">
                                              <a id="snsim_chat_windown_mini_btn" title="最小化" class="rt_icon snsim_icon_minY" href="javascript:void(0);"></a>
                                              <a id="snsim_chat_windown_close_btn" title="关闭" class="rt_icon snsim_icon_close1"></a>
                                        </div>
                                        <div class="sns_curchat_title"></div>
                                  </div>
                            </div>

                            <div class="snsim_chat_up">
                                  <div style="width: 300px;" class="snsim_chat_tips snsim_tips01 hide">
                                        <a class="snsim_icon_tipsclose" onclick="return false;" href="javascript:void(0);" hidefocus="true"></a>
                                        <span class="tips_icon icon_warnS"></span>
                                        <span></span>
                                  </div>
                                  <div class="snsim_chat_list">
                                        <div id="snsim_chat_window_tab_content" class="snsim_chat_window_tab_content snsim_chat_dialogue">
                                              <!-- chatWindowChatMain -->
                                        </div>
                                  </div>
                            </div>
                            <div id="snsim_chat_sendbox" class="snsim_chat_sendbox">
                                  <div id="snsim_chat_sendbox_toolbar" class="sendbox_toolbar sendbox_bar clearfix">
                                        <div class="sendbox_ac">
                                              <span class="sendbox_kind">
                                                    <a id="fontBtn" class="snsim_iconsend_font" title="字体"></a>
                                                    <a id="expressionBtn" class="snsim_iconsend_face" href="javascript:void(0);" title="表情"></a>
                                                    <a id="auploadImage" class="snsim_iconsend_img" title="图片">
                                                    	<input id="image_upload_input" style="visibility:hidden; height:1px;width:1px;" accept="image/*" type="file">
                                                    </a>
                                                    <a id="upload" class="snsim_upload_file" title="附件">
                                                    	<input id="file_upload_input" style="visibility:hidden; height:1px;width:1px;" type="file">
                                                    </a>
                                                    <a id="captureBtn" style="" class="snsim_iconsend_copysc" title="截图" href="javascript:f_capture()">
                                                          <iframe src="" id="setupFrame" class="hide"></iframe>
                                                    </a>
                                                    <a id="transToMUC" class="snsim_trans_to_muc" title="转为群聊"></a>
                                                    <a id="history_message" class="snsim_histroy_msg"><span class="snsim_icon_shield" title="聊天记录"></span>聊天记录</a>
                                              </span>
                                        </div>
                                        <div class="layer_title hide">
                                              <span>发送截屏</span>
                                        </div>
                                  </div>
                                  <div class="sendbox_box clearfix">
                                        <div id="snsim_sendbox_content" class="snsim_sendbox_content sendbox_area" contenteditable="true"></div>
                                  </div>
                                  <div class="sendbox_btn clearfix" style="border-radius: 0 0 6px 0;">
                                        <div class="sendbox_btn_l">
                                           <p class="sendbox_link">
                                               <span class="link_txt"> </span>
                                           </p>
                                        </div>
                                        <div class="sendbox_btn_r">
                                           <div class="snsim_tips_char">
                                             <span class="spetxt"></span>
                                             <div style="margin-left: 14px; color: #bfbfbf;">您还可以输入<span id="snsim_sendbox_available_num" style="color: red;">500</span>字</div>
                                           </div>
                                           <div class="btn">
                                             <a id="snsim_message_send_btn" class="snsim_btn_sub">
                                                <span style="float:left;margin-left:35px;">发送</span>
                                             </a>
                                           </div>
                                        </div>
                                  </div>
                            </div>
                            <div id="snsim_confirm_bg"></div>
                            <div class="snsim_confirm_box hide">
                                  <div class="snsim_confirm_con">
                                        <div class="snsim_confirm_info">
                                              <p class="snsim_confirm_p">
                                                    <span class="snsim_cfmicon_stat icon_askS"></span>
                                                    <span class="txt" node-type="snsim_confirm_txt"></span>
                                              </p>
                                              <p class="snsim_confirm_btn">
                                                    <a class="snsim_btn_a" onclick="return false;" href="javascript:void(0);">
                                                          <span>确认</span>
                                                    </a>
                                                    <a class="snsim_btn_b hide" onclick="return false;" href="javascript:void(0);">
                                                          <span>取消</span>
                                                    </a>
                                              </p>
                                        </div>
                                  </div>
                            </div>
                        </div>
                  </div>
            </div>
      </div>
      <div id="snsim_chat_window_mini" class="snsim_chat_window_mini snsim_win_minD snsim_min_chat"></div>
      <!-- 选择邀请好友 -->
      <div node-type="selRostersToInvite" class="W_layer selRostersToInvite">
            <ul node-type="selRostersToInvite" class="list_content_li">
            </ul>
            <p class="snsim_confirm_btn">
                  <a class="snsim_btn_a" node-type="selRostersToInvite">
                        <span>确认</span>
                  </a>
                  <a class="snsim_btn_b" node-type="cancelInvitation">
                        <span>取消</span>
                  </a>
            </p>
      </div>
</div>
<!-- 半透明遮罩层，防止鼠标拖动窗口时会选中元素 -->
<div id="snsim_coverlayer" class="snsim_center" style="display: none;"></div>

<!-- 图片预览 -->
<table id="snsim_img_preview_window" class="snsim_center snsim_img_preview">
   <tr>
     <td>
       <div class="snsim_img_container">
         <span class="snsim_img_preview_menu">
               <a title="关闭" class="snsim_img_preview_close"></a>
         </span>
         <img src="" style="cursor: pointer;max-width: 800px;">
       </div>
     </td>
   </tr>
</table>
<!-- 在线状态 -->
<div id="snsim_layer_presence_panel" class="snsim_float_window snsim_layer_presence_panel">
      <ul class="setting_list">
            <li title="切换为在线状态">
                  <a class="setting_cho" presence="chat">
                        <em class="snsim_chat"></em>
                        <span class="setting_txt">在线</span>
                  </a>
            </li>
            <li title="切换为隐身状态">
                  <a class="setting_cho" presence="unavailable">
                        <em class="snsim_unavailable"></em>
                        <span class="setting_txt">隐身</span>
                  </a>
            </li>
            <li title="切换为离开状态">
                  <a class="setting_cho" presence="away">
                        <em class="snsim_away"></em>
                        <span class="setting_txt">离开</span>
                  </a>
            </li>
            <li title="切换为忙碌状态">
                  <a class="setting_cho" presence="dnd">
                        <em class="snsim_dnd"></em>
                        <span class="setting_txt">忙碌</span>
                  </a>
            </li>
            <li title="注销登陆">
                  <a class="setting_cho" onclick="SNSApplication.getInstance().logout()" style="padding-left: 10px; padding-right: 6px;">
                        <em class="snsim_drop"></em>
                        <span class="setting_txt">注销</span>
                  </a>
            </li>
      </ul>
</div>

<!-- 好友操作 -->
<div id="snsim_roster_operation_panel" class="snsim_float_window snsim_roster_operation_panel snsim_layer">
      <ul class="setting_list">
            <li id="snsim_move_roster_btn" title="移动好友">
                  <a class="setting_cho">
                        <span class="setting_txt">移动好友</span>
                  </a>
            </li>
            <li id="snsim_copy_roster_btn" title="复制好友">
                  <a class="setting_cho">
                        <span class="setting_txt">复制好友</span>
                  </a>
            </li>
            <li id="snsim_delete_roster_btn" title="删除好友">
                  <a class="setting_cho">
                        <span class="setting_txt">删除好友</span>
                  </a>
            </li>
            <li id="snsim_modify_roster_name_btn" title="修改备注"  onclick="setTimeout(function(){jQuery('input[name=\'prompt_box_input\']').focus();},0);">
                  <a class="setting_cho">
                        <span class="setting_txt">修改备注</span>
                  </a>
            </li>
            <li id="snsim_create_group_btn" title="新建分组" onclick="setTimeout(function(){jQuery('input[name=\'prompt_box_input\']').focus();},0);">
                  <a class="setting_cho">
                        <span class="setting_txt">新建分组</span>
                  </a>
            </li>
      </ul>
</div>

<!-- 移动,复制-可用组 -->
<div id="snsim_roster_operation_available_panel" class="snsim_float_window  snsim_layer">
      <ul class="setting_list">
      
      </ul>
</div>

<!-- 消息提醒面板 -->
<div id="snsim_unread_message_panel" class="snsim_float_window snsim_unread_message_panel">
      <div style="max-height: 264px;overflow: hidden;"><ul></ul></div>
      <div class="bottom">
      	<span id="ignore_all">忽略全部</span>
      	<span id="check_all">查看全部</span>
      </div>
      <!-- <div class="arrow"></div> -->
</div>

<!-- 群成员列表面板 -->
<div id="chatroom_member_list" class="snsim_float_window chatroom_member_list">
    <div class="chatroom_info">
    	<img src="">
    	<span class="chatroom_name"></span>
        <a title="关闭" class="close_btn"></a>
    </div>
    <div class="chatroom_menubar">
    	<div class="members_title">群成员</div>
    	<span class="quit">退出群</span>
    	<span id="chatroom_settings" class="settings">修改资料</span>
    </div>
	<div class="chatroom_operation">
		<span class="chatroom_operation_item _destory">解散群</span>
		<span class="chatroom_operation_item _quit">退群</span>
		<span class="chatroom_operation_item _rename">改备注</span>
		<span class="chatroom_operation_item _chatroom_settings">群资料</span>
	</div>
	<div class="member_list_title">
		<span style="margin-left: 8%;" class="title_item">成员</span>
		<span style="margin-left: 21%;" class="title_item">基本信息</span>
		<span style="margin-left: 43%;" class="title_item">操作</span>
	</div>
	<div class="member_list_content">
		<ul>
		</ul>
	</div>
</div>

<!-- 群资料修改 -->
<div id="chatroom_settings_window" class="snsim_chatroom_settings_window">
       <div class="chatroom_settings_container">
      		<div class="top_container">
      			<a title="关闭" class="chatroom_settings_close_btn"></a>
      		</div>
       		<div class="chatroom_settings_content">
	       		<div class="body_container">
		       		<div class="chatroom_name">
		       			<span style="font-size: 12px;">群名称</span>
		       			<input type="text" class="chatroom_name_input" maxlength="20">
		       		</div>
	       			<div class="head_icon">
	       				<span style="font-size: 12px;vertical-align: top;">头像</span>
	       				<img class="chatroom_head_icon" src="/internet-platform-web/trd/res/skin/default/icons/normal_pic_50x50.png">
	       				<a id="chatroom_change_head_icon" class="avatar_upload_btn" title="更改头像">更改头像
						</a>
                        <input id="room_avatar_upload_input" style="visibility:hidden; height:1px;width:1px;" accept="image/*" type="file">
						       				
	       				<span class="instruction">仅支持JPG、JPEG、PNG文件且小于5M</span>
		       		</div>
		       		<div style="margin-top: 7px; display: none;">
		       			<span style="vertical-align: top;margin-right: 10px;">群描述</span>
		       			<textarea class="chatroom_desc_input" rows="5"></textarea>
		       		</div>
	       		</div>
	       	</div>
	       	<div id="avatar_upload_progress_container" class="hide"></div>
	       	<div id="snsim_chatroom_avatar_uploader_preview" class="chatroom_avatar_uploader_preview">
	       		<div class="avatar_default_preview"></div>
	       		<div id="bgDiv" class="bgDiv hide">
				       	<div id="dragDiv" class="dragDiv">
				          <div id="rRightDown" class="rRightDown"> </div>
				          <div id="rLeftDown" class="rLeftDown"> </div>
				          <div id="rRightUp" class="rRightUp"> </div>
				          <div id="rLeftUp" class="rLeftUp"> </div>
				        </div>
		      	</div>
	       	</div>
            <div class="bottom_container">
	            <p class="snsim_confirm_btn">
	                  <a class="snsim_btn_b">
	                        <span>返回</span>
	                  </a>
	                  <a class="snsim_btn_a">
	                        <span>保存</span>
	                  </a>
	            </p>
       		</div>
       </div>
</div>

<!-- 服务器搜索 -->
<div id="snsim_multi_search_window" class="snsim_float_window multi_search_window snsim_tab_content_container" style="border:1px solid #ccc;">
      <div>
            <span class="multi_search_window_menu">
            	<span class="search_head_icon"></span>
            	<span style="vertical-align: top;line-height: 37px;">查找</span>
                <a title="关闭" class="multi_search_window_close"></a>
            </span>
            <ul class="multi_search_tab snsim_tab_head_container clearfix">
                  <li id="snsim_roster_search_tab_head" class="snsim_tab_head cur" onclick="setTimeout(function(){jQuery('#search_roster').focus();},0);">
                        <span class="tab_head_icon roster_search_head_icon"></span>
                        <a>找人</a>
                  </li>
                  <li id="snsim_chatroom_search_tab_head" class="snsim_tab_head" onclick="setTimeout(function(){jQuery('#search_group').focus();},0);">
                        <span class="tab_head_icon chatroom_search_head_icon"></span>
                        <a>找群</a>
                  </li>
                  <!-- <li id="snsim_pulic_account_search_tab_head" class="snsim_tab_head">
                        <span class="tab_head_icon pubaccount_search_head_icon"></span>
                        <a>公共号</a>
                  </li> -->
            </ul>
      </div>
      <div id="snsim_roster_search_tab_content" class="snsim_tab_content cur">
      	<div>
	  		<span style="margin-left: 20px;">关键词:</span>
	        <input type="text" placeholder="查找联系人" class="multi_search_input" id="search_roster">
	        <button class="multi_search_btn">查找</button>
      	</div>
        <div class="search_result_head">
        	<span class="member_name">姓名</span>
        	<span class="member_info">账号</span>
        	<span class="member_opt">操作</span>
        </div>
        <div class="multi_search_result">
           <ul id="snsim_roster_search_tab_container" class="snsim_roster_search_tab_container"></ul>
        </div>
      </div>
      <div id="snsim_chatroom_search_tab_content" class="snsim_tab_content">
   		<span style="margin-left: 20px;">关键词:</span>
        <input type="text" placeholder="查找群" class="multi_search_input" id="search_group">
        <button class="multi_search_btn">查找</button>
        <div class="search_result_head">
			<span class="member_name">名称</span>
          	<!-- <span class="member_info">基本信息</span> -->
          	<span class="member_opt" style="margin-left: 295px;">操作</span>
        </div>
        <div class="multi_search_result">
           <ul id="snsim_chatroom_search_tab_container" class="snsim_chatroom_search_tab_container"></ul>
        </div>
      </div>
      <div id="snsim_public_account_search_tab_content" class="snsim_tab_content">
      	<span style="margin-left: 20px;">关键词:</span>
        <input type="text" placeholder="查找公共号" class="multi_search_input">
        <button class="multi_search_btn">查找</button>
        <div class="search_result_head">
			<span class="member_name">成员</span>
        	<span class="member_info">基本信息</span>
        	<span class="member_opt">操作</span>
        </div>
        <div class="multi_search_result">
         	<ul id="snsim_public_account_search_tab_container" class="snsim_public_account_search_tab_container"></ul>
        </div>
      </div>
</div>
<div id="snsim_mini_chatwindow" class="snsim_mini_chatwindow hide"></div>

<!-- 群邀请 -->
<div id="snsim_invite_window" class="invite_window snsim_float_window">
      <div class="sns_invite_head">
            <img id="sns_invite_chatroom_head_icon" />
            <span style="margin: 13px; float: left;color:#fff;">选择联系人</span>
	        <span class="invite_window_menu">
	            <a title="关闭" class="invite_window_close"></a>
	        </span>
      </div>
      <div id="snsim_invite_body" class="snsim_tab_content_container">
      	<div style="height: 360px;">
	      	<div id="snsim_invite_from_search" style="width: 131px; float: left;">
	            <input type="text" placeholder="" id="snsim_invite_search_input" class="invite_window_input">
				
	            <div id="invite_roster_list_box" class="invite_roster_list_box hide">
	            	<div style="border-bottom: 1px solid #ddd;height: 8px;width: 135px;margin: 6px 0px 0px 6px;">
		            	<span class="sns_invite_search_result">搜索结果</span>
	            	</div>
	                <div>
	                	<ul id="snsim_invite_search_result_list"></ul>
	                </div>
	            </div>
	      	</div><br>
	      	<div id="snsim_invite_tab_content_container">
	      		<div class="snsim_tab">
			      	<ul class="snsim_tab_head_container tab_list clearfix">
				      	<li id="snsim_tab_head_invite_from_roster" title="好友" class="snsim_tab_head snsim_tab_head_invite_from_roster cur">
					      	<a><span class="snsim_icon_invite_roster_tab"></span></a>
				      	</li>
				      	<li id="snsim_tab_head_invite_from_org" title="组织" class="snsim_tab_head snsim_tab_head_invite_from_org">
					      	<a><span class="snsim_icon_invite_org_tab"></span></a>
				      	</li>
			      	</ul>
	      		</div>
	      		<div id="snsim_tab_content_invite_from_roster" class="snsim_tab_content snsim_tab_content_invite_from_roster cur">
					<div id="snsim_invite_roster_List" class="snsInviteRosterBody"></div>
				</div>
		      	<div id="snsim_tab_content_invite_from_org" class="snsim_tab_content snsim_tab_content_invite_from_org">
					<div class="snsim_list_con">
						<div class="zTreeDemoBackground left">
							<ul id="invite_from_org_tree" class="ztree"></ul>
						</div>
					</div>
				</div>
	      	</div>
      	</div>
      	<div id="snsim_selected_container" class="snsim_selected_container">
      		<span style="margin-left: 20px;">已选人员(<span class="selected_num">0</span>)</span>
      		<div class="selected_list_container">
      			<ul class="selected_list">
      			</ul>
      		</div>
      	</div>
      </div>
      <div id="snsim_invite_footer" class="invite_rosters_footer">
            <a class="submit_btn">确认</a>
            <a class="cancel_btn">取消</a>
      </div>
</div>


<!-- 对话框 -->
	<div id="snsim_dialog" class="snsim_confirm_info">
	      <p class="snsim_confirm_p">
	            <span class="icon_alert"></span>
	            <span class="txt" style="margin-left: 13px;"></span>
	      </p>
	      <p class="snsim_confirm_p">
	            <input name="prompt_box_input" maxlength="20" type="text" style="outline: none;border: 1px solid #ddd;height: 23px;width: 155px;padding-left: 10px;"/>
	      </p>
	      <p class="snsim_confirm_btn" style="border-top: 1px solid #ddd;padding-top: 8px;background: #f7fafa;padding-bottom: 8px;">
	            <a class="snsim_btn_a" style="border: 1px solid #ddd;width: 67px;height: 18px;padding-top: 2px;border-radius: 7px;">
	                  <span>确认</span>
	            </a>
	            <a class="snsim_btn_b" style="border: 1px solid #ddd;width: 67px;height: 18px;padding-top: 2px;border-radius: 7px;">
	                  <span>取消</span>
	            </a>
	      </p>
	</div>

<!-- 登录冲突提醒 -->
	<div id="sns_conflict_alert" style="display: none; width: 100%; position: absolute; top: 0;z-index: 1001;">
		<div style="background: #DEAD63; text-align: center; padding: 5px 0; font-size: 14px;">
			<span>您已在别处登录, 点击<a style="color: #B23838;" id="sns_conflict_reconnect">重新连接</a></span>
		</div>
	</div>




