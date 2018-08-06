<#include "../header.ftl">
<link href="${ctx}/trd/uui/css/u.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/trd/uui/css/grid.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/css/ip/system/sys_organization/sys_organization.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/ip/changeTree.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/ip/resetCommon.css" rel="stylesheet" type="text/css">
<script src="${ctx}/js/ip/sys_manager/sys_manager.js" type="text/javascript"></script>
<script src="${ctx}/js/ip/sys_manager/organization/organization.js"></script>
<script>
	window.$ctx = '${ctx}';
</script>

<div id="organization-container" class="organization-container clearfix">
	<span id="hirerId" style="display:none">${hirerId}</span>
	       <#if userInfo??>
			  <span id="userInfo" class="hide">${userInfo}</span>
			<#else>
			</#if>
			<#if result??>
  				<span id="resultExc" class="hide">${result}</span>
			<#else>
			</#if>
			<span id="showuserId" class="hide"></span>
			<div id="organizaton-left" class="organizaton-left">
				<div id="organizaton-left-radios" class="organizaton-left-radios">
				<input id="getStoporStart" class="hide" value="1"/>
					<span><input type="radio" value="0" id="stop" class="stop" name="isStart"/>已停用</span>
					<span><input type="radio" value="1" id="start" class="strat" name="isStart" checked/>启用</span>
				</div>
				<div id="left-tree" class="organizaton-left-tree clearfix">
					<div style="width:100%;height:520px;display: inline-block;solid red 1px;float:left;padding-top: 5px;overflow:auto;">
						<div class="ztree"
							 u-meta='{"id":"tree1","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"treeSetting"}'>
						</div>
			        </div>
				</div>
			</div>
			<div id="organizaton-right" class="organizaton-right">

				<div class="organizaton-right-btns clearfix">
				    <div class="clearfix btns-container" >
				        <div style="display:none;" id="test">
							<div id="progressbar"><div class="progress-label">加载...</div></div>
						</div>

					    <ul class="all-btn">
					        <li><a href="javascript:void(0);" data-bind="click:adddept">添加单位</a></li>
					        <li><a id="all-btn-edit" class="btn-spe" href="javascript:void(0);" data-bind="click:editordept">编辑单位</a></li>
					        <li><a href="javascript:void(0);" data-bind="click:addstaff">添加人员</a></li>
					        <li><a id="importfilebtn" href="javascript:void(0);">批量导入</a></li>
							<!--<div class="fl" id="separation"><a id="dep-info-set" href="javascript:void(0);" data-bind="click: depInfoSet"> 单位信息项设置</a></div>-->
							<li><a id="dep-info-set" href="javascript:void(0);" data-bind="click: depInfoSet"> 单位信息项设置</a></li>
							<!-- 
					    	<li><a style="width:96px;" id="init-data" href="javascript:void(0);">初始化群组</a></li>
							-->
					    	<li><a style="width:96px;" id="one-data" href="javascript:void(0);">工作流同步</a></li>
					    </ul>
                    </div>
                    <div class="sys-nav-search">
		                <input id="sys-nav-search-text" class="sys-nav-search-text" type="text" name="" placeholder="姓名/手机/邮箱">
		                <button id="sys-nav-search-btn" class="" data-bind=""></button>
                    </div>
				</div>
		<input class="hide" id="query-coId" type="text"/>
		<div id="demo-datagrid" class="demo-datagrid" style="">
            <div class="table-container">
                <table class="u-table">
                    <thead>
                        <tr>
                            <!--<th>-->
                                <!--<label class="u-checkbox only-style u-checkbox-floatl" data-bind="click: dataTableUser.toggleAllSelect.bind(dataTableUser), css:{'is-checked': dataTableUser.allSelected()}">-->
                                    <!--<input id="checkInput" type="checkbox" class="u-checkbox-input">-->
                                    <!--<span class="u-checkbox-label"></span>-->
                                <!--</label>-->
                            <!--</th>-->
                            <th  class="userNameWidth">姓名</th>
                            <th class="userSexWidth">性别</th>
                            <th class="coNameWidth">单位</th>
                            <th class="dutyWidth">职务</th>
                            <th class="loginNameWidth">登录账号</th>
                            <th class="userEmailWidth">Email</th>
                            <th class="cellphoneNoWidth">手机</th>
                            <th class="doneWidth">操作</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach:{data:dataTableUser.rows(),as:'row', afterAdd: menuEvents.afterAdd}" >
                        <tr data-bind="css: { 'is-selected' : row.selected() } ,attr:{'id': $index()}">
                            <!--<td class="checkbox1">-->
                                <!--<label class="u-checkbox only-style u-checkbox-floatl" data-bind="click: row.multiSelect, css:{'is-checked': row.selected()}">-->
                                    <!--<input type="checkbox" class="u-checkbox-input">-->
                                    <!--<span class="u-checkbox-label"></span>-->
                                <!--</label>-->
                            <!--</td>-->
                            <td class="userNameWidth" data-bind="text: row.ref('userName'),attr:{'title':row.ref('userName')}"></td>
                            <td class="userSexWidth" data-bind="text: row.ref('userSex'),attr:{'title':row.ref('userSex')}"></td>
                            <td class="coNameWidth" data-bind="text: row.ref('coName'),attr:{'title':row.ref('coName')}"></td>
                            <td class="dutyWidth" data-bind="text: row.ref('roleName'),attr:{'title':row.ref('roleName')}"></td>
                            <td class="loginNameWidth" data-bind="text: row.ref('loginName'),attr:{'title':row.ref('loginName')}"></td>
                            <td class="userEmailWidth" data-bind="text: row.ref('userEmail'),attr:{'title':row.ref('userEmail')}"></td>
                            <td class="cellphoneNoWidth" data-bind="text: row.ref('cellphoneNo'),attr:{'title':row.ref('cellphoneNo')}"></td>
                            <td class="doneWidth" data-bind="attr:{'id':row.ref('userId')}">
                                <a data-bind="click:$parent.menuEvents.beforeEdit.bind($data,  $index())" href="javascript:void(0);" >[编辑]</a>
                                <a class="0" data-bind="click:$parent.menuEvents.viewRowStop.bind($data, $index())" href="javascript:;" >[停用]</a><!--data-bind="click:$parent.menuEvents.viewRow.bind($data, $index())"-->
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
			<div id='pagination' class='u-pagination' style="float: right;" u-meta='{"type":"pagination","data":"dataTableUser","pageList":[10,20,50],"sizeChange":"sizeChangeFun","pageChange":"pageChangeFun"}'></div>
        </div>

		<!--end-->

        <!-- 批量处理  -->
        <!--start-->
        <div style="display:none;" id="batch-import-content">
		<p class="_p"><a href="${ctx}/organization/organInfo"> &gt;&gt;组织机构 &gt;&gt;</a>批量导入
			<a href="" class="back"><img src="${ctx}/images/ip/sys/back.png" />返回</a>
		</p>
		<!--第一步-->
		<div id="stepOne" style="display:none;">
			<div class="batch-import-progress">
				<div class="batch-line"></div>
				<div class="batch-line"></div>
				<div class="batch-1 batch-icon-1">
					上传文件
				</div>
				<div class="batch-2 batch-icon-2">
					执行导入
				</div>
				<div class="batch-3 batch-icon-2">
					完成
				</div>

			</div>
			<div class="step-batch-import">
				<div class="step batch-import-first" id="batch-import-first">1</div>
				<div class="descript batch-import-step">
					<p>填写导入员工的信息<span>（请按模板格式要求填写）</span></p>
					<!--<a href="${ctx}/upload/template/用户批量导入模板.xlsx"><img src="${ctx}/images/ip/sys/download.png" />下载模板</a>-->
					<a href="${ctx}/organization/templateDownLoad?filename=用户批量导入模板.xlsx"><img src="${ctx}/images/ip/sys/download.png" />下载模板</a>
				</div>
			</div>
			<div class="step-batch-import">
				<div class="step batch-import-second">2</div>
				<div class="descript batch-import-step">
					<p>上传填好的员工信息表（仅支持xls/xlsx格式，且文件大小不能超过2M）</p>
					<form id="excelFileUpload" action="" method="post" enctype="multipart/form-data">
					    <#if result??>
					    <span id="fileNameRet">${fileName}</span>
  				        <input type="file" id="uploadfile" name="excelFile" class="upload-file-btn" value="上传附件"/>
					  <#else>
					     <!--<a id="excelUpload"><img src="${ctx}/images/ip/sys/attachments.png" />上传附件</a>-->
						 <input type="file" id="uploadfile" name="excelFile" class="upload-file-btn" value="上传附件"/>
					   </#if>

					</form>
				</div>
			</div>
			<button type="button" class="file-upload-btn" id="file-upload-btn" >上传</button><!--data-bind="click:fileUplode"-->
		</div>
		<!--第二步     -->
		<div id="stepTwo" style="display:none;">
			<div class="batch-import-progress">
				<div class="batch-line batch-bgcolor"></div>
				<div class="batch-line"></div>
				<div class="batch-1 batch-icon-1">
					上传文件
				</div>
				<div class="batch-2 batch-icon-1">
					执行导入
				</div>
				<div class="batch-3 batch-icon-2">
					完成
				</div>
			</div>
			<div class="batch-dr">
				<div class="batch-dr-1">本次可导入（<span id="validstaff">0</span>）人</div>
				<div class="batch-dr-2">本次不可导入（<span id="invalidstaff">0</span>）人</div>
			</div>
			<div class="batch-dr">
				<button id="fileuploadNext" type="button" class="file-down-btn" >下一步</button>
				<button style="background-color: rgba(243, 172, 0, 0.87);" id="returnOne" type="button" class="file-down-btn" >重新上传</button>
			</div>
			<div class="batch-message">
				不可导入的员工
			</div>
			<div id="batch-table" class="batch-table">
				<div>
					<table>
						<tr>
							<th>行号</th>
							<th>错误原因</th>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!--第三步     -->
		<div id="stepSuc" style="display: none;">
			<div class="batch-import-progress">
				<div class="batch-line batch-bgcolor"></div>
				<div class="batch-line batch-bgcolor"></div>
				<div class="batch-1 batch-icon-1">
					上传文件
				</div>
				<div class="batch-2 batch-icon-1">
					执行导入
				</div>
				<div class="batch-3 batch-icon-1">
					完成
				</div>
			</div>
			<div class="batch-success">
				<img src="${ctx}/images/ip/sys/upload-success.png" />
				<p>成功导入<span id="filerealNum">0</span>人！初始密码：12345678</p>
			</div>
			<a id="fileuploadSec" class="file-upload-btn" >完成</a>

		</div>
	</div>
        <!--end-->
                </div>
          </div>
	</div>
	</div>
		<!--  下面是 添加人员的弹出框  -->
		<input type="hidden" id="tree1name"/>
		<input type="hidden" id="staffDepttree1"/>
		   	<div class="modal fade" id="dialog-add-staff" tabindex="-1" role="dialog" aria-hidden="true">
        		<div class="modal-dialog">
        		    <div id="sys-jump-staff">
                   		<div class="sys-jump-header">
                        <span id="staffTit">添加人员</span>
                        	<a class="close-jump-window fr" id="close-jump-add-staff-window" href="#" data-bind="click:close_jump_add_staff_window"><img src="${ctx}/images/ip/sys/close.png"></a>
                    	</div>
                    <div id="sys-jump-content">
                        <div class="sys-jump-content-tabs">
                          <!-- Nav tabs -->
						<ul class="nav nav-tabs " role="tablist">
						    <li role="presentation" class="active"><a href="#add-staff-normal" aria-controls="home" role="tab" data-toggle="tab">常规</a></li>
						    <li role="presentation"><a href="#add-staff-other" aria-controls="add-staff-other" role="tab" data-toggle="tab">其他</a></li>
						    <li role="presentation"><a href="#part-time-job" aria-controls="part-time-job" role="tab" data-toggle="tab">兼职</a></li>
						</ul>
						  <!-- Tab panes -->
						<div class="tab-content">
						    <div role="tabpanel" class="tab-pane active" id="add-staff-normal">
						    	<form class="normal-form">
						    		<p><label><span class="star">&lowast;</span>登录账号：</label>
						    			<input id="def-staff-login" type="text" name="" maxlength="12" placeholder="请输入账号" data-bind="value:staffAccount, event:{blur:checkStaffAccount}"/>
						    			<span class="error-tips" data-bind="visible:staffAccountErrorTip">不能为空！</span>
						    			<span id="checkError" class="error-tips"></span>
						    			<span id="hirerNo" class="staffCommon" style="visibility:hidden">@${hirerNo}</span></p>
			 	                    <p id="p-pwd"><label><span class="star">&lowast;</span>密码：</label>
			 	                    	<span id="def-staff-pwd" class="staffCommon">12345678</span>
			 	                    	<input id="login-staff-pwd" class="staff-pwd staffCommon" type="password" name="" placeholder="登陆后系统会提示修改密码"/>
			 	                    	<a href="javascript:void(0)" class="save-update-pwd staffCommon" id="save-update-pwd" data-bind="click:update_pwd_save">保存</a>
			 	                    	<a href="javascript:void(0)" class="cancel-update-pwd staffCommon" id="cancel-update-pwd" data-bind="click:cancel_update_pwd">取消</a>
			 	                    	<span style="color:red;" id="passwordWran"></span>
			 	                    	<a href="javascript:void(0)" class="update-pwd-a staffCommon" id="update-pwd-a">修改密码</a></p>
			 	                    <p>
			 	                    <label><span class="star">&lowast;</span>姓名：</label>
			 	                    	<input id="def-staff-name" type="text" name="" placeholder="请输入姓名" maxlength="20" data-bind="value:staffName, event:{blur:checkStaffName}"/>
			 	                    	<span class="error-tips" data-bind="visible:staffNameErrorTip">不能为空！</span>

			 	                    </p>
			 	                    <p>
			 	                        <label><span class="star">&lowast;</span>姓别：</label>
			 	                    	<span id="sexboy" style="display:inline-block; margin-left:10px;">
			 	                    		<input type="radio" name="staff-sex" value="1" checked="checked" id="man"/>男
			 	                    	</span>
			 	                    	<span id="sexgirl" style="display:inline-block;">
			 	                    		<input type="radio" name="staff-sex" value="0"/>女
			 	                    	</span>
			 	                    </p>
						    	    <p><label><span class="star">&lowast;</span>所属单位：</label>
						    	    	<input id="staffDept" class="hide"/>
						    	    	<input id="staffDeptId" class="hide"/>
						    	    	<input id="staffDeptId-hide" class="hide"/>
						    	    	<input id="staff-own-dept-hide" class="hide"/>
						    	    	<input id="staff-own-dept" class="staff-own-dept" type="text" name="" value="请选择" disabled/>
						    	    	<span class="show-parent-dept-tree" id="select-staff-own-dept" data-bind="click:select_staff_own_dept">...</span>
						    	   		<span id="staff-own-dept-warn" class="error-tips" style="bottom:5px;left:300px;"></span>
						    	    </p>
						    	    <p>
						    	    	<label>职务：</label>
						    	    	<select id="staff-own-job"></select>
						    	    </p>
						    	    <p><label><span class="star">&lowast;</span>手机：</label>
						    	    	<input id="staff-own-tel" maxlength="11" type="text" name="" placeholder="可作为登录账号使用" data-bind="event:{blur:checkTelString}"/>
						    	    	<label style="color:red; width:210px; padding-left:6px;text-align: left; font-size:12px;" id="labelTelString"></label>
						    	    </p>
						    	    <p><label>Email：</label>
						    	    	<input id="staff-own-email" maxlength="30" type="text" name="" placeholder="绑定后可作为登录账号使用" data-bind="event:{blur:checkEmailString}"/>
						    	    	<label style="color:red; width:210px;padding-left:6px;" id="labelEmailString"></label>
						    	    </p>
						    	    <p>
						    	    	<label>座机/分机</label>
						    	    	<input id="zuoji-phone" maxlength="14" type="text" name="" placeholder="请输入座机号" class="zuoji-phone"/>
						    	    	<input id="fenji-phone" maxlength="45" type="text" name="" placeholder="分机号码" class="fenji-phone"/>

						    	    </p>
						    	</form>
						    </div>
						    <div role="tabpanel" class="tab-pane" id="add-staff-other">
						    	<form class="normal-form">
						    		<p>
						    			<label>工号：</label>
						    			<input id="staff-own-employeeNo" maxlength="20" type="text" name="" placeholder="请输入账号"/></p>
			 	                    <p>
			 	                    	<label>原籍：</label>
			 	                    	<input id="staff-own-nativePlace" maxlength="40" type="text" name="" placeholder="请输入原籍"/></p>
			 	                    <p>
			 	                    	<label>毕业学校：</label>
			 	                    	<input id="staff-own-GRADUATE_SCHOOL" maxlength="20" type="text" name="" placeholder="请输入毕业学校"/></p>
			 	                    <p>
			 	                    	<label>专业：</label>
			 	                    	<input id="staff-own-major" type="text" maxlength="30" name="" placeholder="请输入所学专业"/></p>
			 	                    <p>
			 	                    	<label>毕业时间：</label>
			 	                    	<span class="u-datepicker date">
										    <input id="staff-own-graduatioinTime" class="u-input" type="text" placeholder="请选择毕业时间">
										</span>
			 	                    </p>
			 	                    <p><label>学历：</label><select id="staff-own-education"></select></p><!--<option>无</option>-->
			 	                    <p>
			 	                    	<label class="descript">备注：</label>
			 	                    	<textarea id="staff-own-remark" maxlength="50" name="" class="" placeholder="在这里可以添加备注"></textarea></p>
						    	</form>
						    </div>
						    <div role="tabpanel" class="tab-pane" id="part-time-job">
						    	<p>
							    	<span>现任单位：<span id="nowDept"></span></span>
							    	<span>现任职务：<span id="nowDuty"></span></span>
							    	<a href="javascript:void(0)" id="add-part-time-job" data-bind="click:add_part_time_job">添加兼职</a>
						    	</p>
						    	<div id="part-time-container">
							    	<table>
							    		<thead>
							    			<tr>
							    				<td width="270px" class="no-border-left">兼职单位</td>
							    				<td width="180px">兼职职务</td>
							    				<td class="no-border-right">操作</td>
							    				<input class="hide" id="isPartTime" value="1"/>
							    			</tr>
							    		</thead>
							    		<tbody>
							    		</tbody>
							    	</table>
						    	</div>
						    	<div class="select-parent-dept-tree" id="select-part-time-dept-tree" data-bind="visible:showPartTimedeptTree">
									<div class="select-parent-dept-tree-title">
									    <a class="close-parent-tree-select" id="close-select-part-job" href="#" data-bind="click:close_select_part_job"><img src="${ctx}/images/ip/sys/close2.png"></a>
									</div>
									<div class="ztree-container">
									    <div style="width:100%;display: inline-block;solid red 1px;float:left;padding-top: 5px;">
											<div class="ztree"
												 u-meta='{"id":"tree2","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"treeSetting"}'>
											</div>
									    </div>
								    </div>
									 <div class="sys-jump-footer">
						                <div class="sys-jump-btn fr">
						                    <button id="" class="sys-jump-btn-save" type="submit" data-bind="click:addStaffbtnSave">保存</button>
						                    <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_select_part_job">取消</button>
						                 </div>
				                     </div>
                               </div>
						    </div>
						</div>
                         </div>
                    </div>
                    <div class="sys-jump-footer">
                        <div class="sys-jump-btn fr">
                            <button id="editsavebtn" class="sys-jump-btn-save" type="submit"  data-bind="click:addstaffSave">保存</button>
                            <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_jump_add_staff_window">取消</button>
                        </div>
                    </div>
                    <div class="select-parent-dept-tree" id="sleect-staff-own-dept-tree" data-bind="visible:showStaffOwndeptTree">
						<div class="select-parent-dept-tree-title">
						    <a class="close-parent-tree-select" id="close-staff-own-tree-select" href="#" data-bind="click:close_staff_own_tree_select"><img src="${ctx}/images/ip/sys/close2.png"></a>
						</div>
						<div class="ztree-container">
						    <div style="width:100%;display: inline-block;solid red 1px;float:left;padding-top: 5px;overflow:auto;">
								<div class="ztree"
									 u-meta='{"id":"tree3","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"treeSetting"}'>
								</div>
						    </div>
					    </div>
						 <div class="sys-jump-footer">
			                <div class="sys-jump-btn fr">
			                    <button id="" class="sys-jump-btn-save" type="submit" data-bind="click:save_staff_own_tree_select">保存</button>
			                    <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_staff_own_tree_select">取消</button>
			                 </div>
			            </div>
                   </div>
                </div>
        	</div>
        </div>
        <!-- 下面是添加单位的dialog -->
        	<div class="modal fade" id="dialog-add-dept" tabindex="-1" role="dialog" aria-hidden="true">
        		<div class="modal-dialog">
        		    <div class="sys-jump-dept">
                   		<div class="sys-jump-header">添加单位(可拖动排序)
                        	<a class="close-jump-window fr" id="close-jump-add-dept-window" href="#" data-bind="click:close_jump_add_dept_window"><img src="${ctx}/images/ip/sys/close.png"></a>
                    	</div>
                    <div id="sys-jump-content" class="normal-form">
                    	<!--<form class="normal-form">-->
                    		<input class="hide" id="adddeptParentcoId"/>
                    		<input class="hide" id="editdeptcoId"/>
						    <input class="hide" id="adddeptlevelnumcoId"/>
						    <input type="hidden" id="tree1coName"/>
						    <input type="hidden" id="tree4coId"/>
						    <input type="hidden" id="tree4coName"/>
							<p>
								<label><span class="star">&lowast;</span>父单位名称：</label>
								<input type="text" class="edit-parent-dept" name="" id="adddeptParentcode" disabled />
								<span class="show-parent-dept-tree" id="show-parent-dept-tree" data-bind="click:show_parent_dept_tree">...</span>
								<span id="adddeptParentcodeError" class="error-tips hide" >请选择父节点</span>
							</p>
							<p>
								<label><span class="star">&lowast;</span>单位编号：</label>
								<input id="addNewDeptCode" maxlength="30" type="text" name="" data-bind="value:addNewDeptCode,event: {blur: checkaddNewDeptCode}"/>
								<span class="error-tips" data-bind="visible:addDeptCodeErrorTip" >必填数字且符合编码规范！</span>
							</p>
							<p>
								<label><span class="star">&lowast;</span>单位名称：</label>
								<input id="adddeptcode" maxlength="120" type="text" name="" data-bind="value:addDeptName,event: {blur: checkDeptName}"/>
								<span class="error-tips" data-bind="visible:addDeptNameErrorTip" >不能为空！</span>
							</p>
							<!--<p>-->
								<!--<label class="descript">单位描述：</label>-->
								<!--<textarea id="adddeptDesc" maxlength="300" name="" class=""></textarea>-->
							<!--</p>-->
							<table id = "dep-drag-sort-depAdd" class="sotabjump-window">
								<tbody></tbody>
							</table>
							<p style="height: 145px">
								<label class="descript">单位描述：</label>
								<textarea id="adddeptDesc" maxlength="300" name="" class=""></textarea>
							</p>
						<!--</form>-->
                    </div>
                    <div class="sys-jump-footer">
                        <div class="sys-jump-btn fr">
                            <button id="sys-jump-btn-save" class="sys-jump-btn-save" type="submit" data-bind="click:adddeptSave" >保存</button>
                            <button id="sys-jump-btn-cancel" class="sys-jump-btn-cancel" type="button" data-bind="click:close_jump_add_dept_window">取消</button>
                        </div>
                    </div>

                    <div class="select-parent-dept-tree" id="select-parent-dept-tree" data-bind="visible:showAddParDeptTree">
						<div class="select-parent-dept-tree-title">
						    <a class="close-parent-tree-select" id="close-parent-dept-tree-select" href="#" data-bind="click:close_parent_dept_tree_select"><img src="${ctx}/images/ip/sys/close2.png"></a>
						</div>
						<div class="ztree-container">
						    <div style="width:100%;display: inline-block;solid red 1px;float:left;padding-top: 5px;overflow:auto;">
								<div class="ztree"
									 u-meta='{"id":"tree4","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"treeSetting"}'>
								</div>
						    </div>
					    </div>
						 <div class="sys-jump-footer">
			                <div class="sys-jump-btn fr">
			                    <button id="" class="sys-jump-btn-save" type="submit" data-bind="click:close_add_parent_dept_tree_select">保存</button>
			                    <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_parent_dept_tree_select">取消</button>
			                 </div>
			            </div>
        </div>
                </div>

        	</div>
        </div>
        <!-- 父级单位菜单树 -->

        <!--编辑单位  -->

        <div class="modal fade" id="dialog-editor-dept" tabindex="-1" role="dialog" aria-hidden="true">
        		<div class="modal-dialog">
        		    <div class="sys-jump-dept">
                   		<div class="sys-jump-header">编辑单位
                        	<a class="close-jump-window fr" href="#" data-bind="click:close_editor_dept_window"><img src="${ctx}/images/ip/sys/close.png"></a>
                    	</div>
                    <div id="sys-jump-content">
                    	<form class="normal-form">
						    	<p><label>父单位名称：</label>
						    		<input class="edit-parent-dept" id="edit-parent-dept" disabled type="text" name=""/>
						    		<input class="hide" id="editparentId"/>
						    		<input type="hidden" id="addparentIdtree5"/>
						    		<input class="hide" id="addparentId"/>
						    		<input class="hide" id="checkparentId"/>
						    		<span class="show-parent-dept-tree" id="select-editor-parent-dept-tree" data-bind="click:select_editor_parent_dept">...</span>
						    	</p>
						    	<p><label><span class="star">&lowast;</span>单位编号：</label>
			 	                	<input id="editnewdeptcode" maxlength="30" type="text" name="" data-bind="value:editorDeptCode, event:{blur:checkEditorDeptCode}"/>
			 	                	<span class="error-tips" data-bind="visible:editorDeptCodeErrorTip">不能为空且为数字！</span>
			 	                </p>
			 	                <p><label><span class="star">&lowast;</span>单位名称：</label>
			 	                	<input id="editdeptcode" maxlength="120" type="text" name="" data-bind="value:editorDeptName, event:{blur:checkEditorDeptName}"/>
			 	                	<span class="error-tips" data-bind="visible:editorDeptNameErrorTip">不能为空！</span>
			 	                </p>
								<table id = "dep-drag-sort-depEdit" class="sotabjump-window">
									<tbody></tbody>
								</table>
			 	                <p style="height: 145px">
			 	                	<label class="descript">单位描述：</label>
			 	                	<textarea id="editor-dept-desc" maxlength="300" name="" class=""></textarea>
								</p>
						</form>
                    </div>
                    <div class="sys-jump-footer">
                    	<button id="sys-jump-btn-delete" class="sys-jump-btn-delete" type="button" data-bind="click:deldeptwran">删除单位</button>
                        <div class="sys-jump-btn fr">
	                            <button id="" class="sys-jump-btn-save" type="submit" data-bind="click:editor_save_dept_window" >保存</button>
	                            <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_editor_dept_window">取消</button>
                            </div>
                    </div>
                </div>
                <div class="select-parent-dept-tree" id="editor-parent-dept-tree" data-bind="visible:showEditorParDeptTree">
					<div class="select-parent-dept-tree-title">
				    	<a class="close-parent-tree-select" id="" href="#" data-bind="click:close_editor_dept_tree"><img src="${ctx}/images/ip/sys/close2.png"></a>
					</div>
					<div class="ztree-container">
					    <div style="width:100%;display: inline-block;solid red 1px;float:left;padding-top: 5px;overflow:auto;">
							<div class="ztree"
								 u-meta='{"id":"tree5","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"treeSetting"}'>
							</div>
						</div>
					</div>
					<div class="sys-jump-footer">
					    <div class="sys-jump-btn fr">
						    <button id="" class="sys-jump-btn-save" type="submit"  data-bind="click:close_add_editor_dept_tree">保存</button>
						    <button id="" class="sys-jump-btn-cancel" type="button" data-bind="click:close_editor_dept_tree">取消</button>
					    </div>
				    </div>
               </div>
        	  </div>
        </div>
        <div class="modal fade" id="error_add_group" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">

            </div>
        </div>
        <div class="modal fade" id="sure_add_group" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
            	<div id="jump-content" class="jump-content jump-content-organ">
				    <div class="jump-header">
				        <p class="jump-header-title" >系统提示</p>
				        <a class="jump-header-close" href="javascript:void(0);" data-bind="click:jump_btn_close">
				        	<img src="../images/ip/menu/close.png" width="12" height="13">
				        </a>
				    </div>
				    <div class="jump-body">
				    	<div id="error_add_group-warn" class="jump-body-icon">
				        	<img src="../images/ip/menu/errorNotice.png" /><span>确定要删除吗？</span>
				        </div>
					</div>
				    <div class="jump-footer">
				        <div class="jump-footer-btn-right fr">
				            <button id="jump-btn-save" class="jump-btn-save" data-bind="click:jump_btn_close">确定</button>
				            <button id="jump-btn-cancel" class="jump-btn-cancel" data-bind="click:jump_btn_close">取消</button>
				        </div>
				    </div>
				</div>
            </div>
        </div>
		<!--<div id="save-success-new" class="save-success-new"><img src="../images/ip/menu/success_p.png" height="" width=""/>保存成功!</div>
		<div id="save-success-staff" class="save-success-new save-success-staff">
			<p style="margin-bottom: 0;line-height: 35px;"><img src="../images/ip/menu/success_p.png" height="" width=""/>保存成功!</p>
			<p style="margin-bottom: 0;line-height: 3px;">用户密码被重置为<span id="pwdText"></span></p>
		</div>
		<div id="delete-success-new" class="save-success-new"><img src="../images/ip/menu/success_p.png" height="" width=""/>删除成功!</div>-->
		<div class="modal fade" id="error_add_group" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">

            </div>
        </div>
        <div class="modal fade" id="sure_del_group" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog">
            	<div id="jump-del-content" class="jump-content jump-content-organ">
				    <div class="jump-header">
				        <p class="jump-header-title" >系统提示</p>
				        <a class="jump-header-close" href="javascript:void(0);" data-bind="click:jump_del_btn_close">
				        	<img src="../images/ip/menu/close.png" width="12" height="13">
				        </a>
				    </div>
				    <div class="jump-body">
				    	<div id="error_del_group-warn" class="jump-body-icon">
				        	<img src="../images/ip/menu/errorNotice.png" /><span>确定要删除吗？</span>
				        </div>
					</div>
				    <div class="jump-footer">
				        <div class="jump-footer-btn-right fr">
				            <button id="jump-del-btn-save" class="jump-btn-save" data-bind="click:deldeptInfo">确定</button>
				            <button id="jump-del-btn-cancel" class="jump-btn-cancel" data-bind="click:jump_del_btn_close">取消</button>
				        </div>
				    </div>
				</div>
            </div>
        </div>
	<!--单位信息项设置-->
	<div class="modal fade" id="dep-info-set-window" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div id="jump-set-content" class="jump-content jump-content-organ">
				<div class="jump-header">
					<p class="jump-header-title">单位信息项设置</p>
					<a class="jump-header-close" href="javascript:void(0);" data-bind="click:jumpSetClose">
						<img src="../images/ip/menu/close.png" width="12" height="13">
					</a>
				</div>
				<div class="jump-body">
					<div type="text"
						 u-meta='{"id":"dep-info-grid","data":"depInfoData","type":"grid","editable":true,"autoExpand":false}'>
						<div options='{"field":"columnName","dataType":"String","title":"数据库字段","editType":"string","width": 200}'></div>
						<div options='{"field":"columnComments","dataType":"String","title":"字段显示名称","editType":"string","width": 200}'></div>
						<div options='{"field":"javaField","visible":"false","dataType":"String","title":"字段显示名称","editType":"string","width": 200}'></div>
						<div options='{"field":"showType","dataType":"String","title":"展示形式","editOptions":{"id":"show-type","type":"combo","datasource":"showType"},"editType":"combo","renderTy":"comboRender","afterEType":"showTypeChange","width": 200}'></div>
						<div options='{"field":"dictType","dataType":"String","title":"字典类型","editType":"string","width": 200}'></div>
						<div options='{"field":"isUse","dataType":"String","title":"是否启用","editType":"checkbox","renderType":"booleanRender","width": 100}'></div>
					</div>
				</div>
				<div class="jump-footer">
					<div class="jump-footer-btn-right fr">
						<button class="jump-btn-save" data-bind="click: saveDeptInfo">保存</button>
						<button class="jump-btn-cancel" data-bind="click: jumpSetClose">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
<script>
		var excelRet=$("#resultExc").html();
        		if(excelRet=="true"){

        		}else if(excelRet=="false"){
        			alert("请重新上传");
        		}
</script>

<#include "../footer.ftl">
