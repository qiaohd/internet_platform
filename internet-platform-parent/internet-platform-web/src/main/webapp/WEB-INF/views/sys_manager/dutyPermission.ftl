<#include "../header.ftl">
    <link href="${ctx}/trd/components/components.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/changeTree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/system/duty_permission/duty_permission.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/resetCommon.css" rel="stylesheet" type="text/css">
    <script src="${ctx}/js/ip/sys_manager/sys_manager.js" type="text/javascript"></script>
    <script src="${ctx}/js/ip/sys_manager/duty_permission/duty_permission.js" type="text/javascript"></script>
    <script>
    	window.$ctx = '${ctx}';
    </script>
    <input id="ctx" type="hidden" value="${ctx}">
    <div class="system-container">
        <span id="hirerId" style="display:none">${hirerId}</span>
        <div class="system-main">
            <!--左侧菜单栏开始-->
            <div class="system-left">
                <div class="tabbable tabs-left" style="min-height: 500px;">
                <input id="current-duty" type="hidden" class="0">
                    <ul class="nav nav-tabs duty-user"></ul>
                </div>
            </div>
            <!--右侧内容区开始-->
            <div class="system-right" style="height: 100%;">
                <div class="content">
                    <div class="duty-permission-container">
                        <div class="clearfix">
                            <ul class="all-btn">
                                <li class="left-menu"><a href="#/newDuty/newDuty">新建职务</a></li>
                                <li class="left-menu"><a href="#/editDuty/editDuty">编辑职务</a></li>
                                <li><a href="javascript:void(0);" data-bind="click:delete_position_open;">删除职务</a></li>
                                <li><a href="javascript:void(0);" data-bind="click:add_staff_choice_open">添加人员</a></li>
                            </ul>
                        </div>
                        <div class="duty">
                            <p>职务名称:<span id="duty-name" data-bind="text: dutyName"></span></p>

                            <p>职务描述:<span id="duty-disc" data-bind="text: dutyDisc"></span></p>
                        </div>
                        <div class="duty-info">
                            <ul id="myTabs" class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#home" id="home-tab" role="tab"
                                                                          data-toggle="tab" aria-controls="home"
                                                                          aria-expanded="false"
                                                                          data-bind="click: getDuty">查看权限</a></li>
                                <li role="presentation" class=""><a href="#profile" role="tab" id="profile-tab"
                                                                    data-toggle="tab" aria-controls="profile"
                                                                    aria-expanded="true" data-bind="click: getStaff">查看人员</a>
                                </li>
                            </ul>
                            <div id="myTabContent" class="tab-content">
                                <div role="tabpanel" class="tab-pane fade active in" id="home"
                                     aria-labelledby="home-tab">
                                    <div class="ztree"
                                         u-meta='{"id":"tree1","data":"menuDataTable","type":"tree","idField":"menuId","pidField":"parentMenuId","nameField":"menuName","setting":"treeSetting"}'>
                                    </div>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="profile" style="margin-top: 10px;" aria-labelledby="profile-tab">
                                    <div type="text"
                                         u-meta='{"id":"grid-staff","data":"dataTableStaff","type":"grid","editable":false,"columnMenu": false,"sortable":false,"showNumCol":true}'>
                                        <div options='{"field":"staffNumber","dataType":"String","title":"编号","editType":"string","width": 100}'></div>
                                        <div options='{"field":"userName","dataType":"String","title":"姓名","editType":"string","width": 150}'></div>
                                        <div options='{"field":"roleName","dataType":"String","title":"职务名称","editType":"string"}'></div>
                                        <div options='{"field":"coName","dataType":"String","title":"所在部门","editType":"string"}'></div>
                                    </div>
                                    <div id='pagination' class='u-pagination' style="float: right;" u-meta='{"type":"pagination","data":"dataTableStaff","pageList":[10,20,50],"sizeChange":"sizeChangeFun","pageChange":"pageChangeFun"}'></div>
                                </div>
                            </div>
                        </div>
                        <!--<div class="duty-operation-function">-->
                        <!--<a class="duty-operation-btn" href="#/editDuty/editDuty">编辑</a>-->
                        <!--<button id="duty-delete-btn" class="duty-operation-btn" data-bind="click:delete_position_open;">删除</button>-->
                        <!--</div>-->
                    </div>
                </div>
            </div>
            <!--右侧内容区结束-->
            <div class="clearfix"></div>
            <div class="modal fade" id="dialog_add_group" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content choice-staff">
                        <div class="jump-header">
                            <p class="jump-header-title">添加人员</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click:add_staff_choice_close">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body clearfix">
                            <div class="jump-body-content">
                                <p><span class="add-staff-conmmon">职务名称：</span><span id="jump-duty-name"
                                                                                     data-bind="text: dutyNameAddStaff">财务一处处长</span>
                                </p>
                            </div>
                            <div class="fl">
                                <div class="duty-search clearfix">
                                    <input id="duty-search-text" class="fl" type="text"
                                           placeholder="请输入员工名称">
                                    <a id="duty-search-btn" class="fl" href="#"
                                       data-bind="click: searchStaff"><img id="duty-search-pic"
                                                                           src="../images/ip/sys/searchIcon.png"
                                                                           style="width:28px; height:28px;"></a>
                                </div>
                                <div class="duty-staff duty-staff-department">
                                    <div class="choice-staff-tittle">选员工</div>
                                    <div class="duty-staff-all">
                                        <div class="ztree"
                                             u-meta='{"id":"tree-choice-staff","data":"companyDataTable","type":"tree","idField":"staffId","pidField":"staffPid","nameField":"staffName","setting":"treeChoiceStaffSetting"}'>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="fr">
                                <div class="duty-staff duty-staff-choiced">
                                    <div class="choice-staff-tittle clearfix">
                                        <a id="clear-staff-all" class="fr" href="#"
                                           data-bind="click: clearAllChoicedStaff">清空</a>
                                    </div>
                                    <div class="duty-staff-all duty-li-x">
                                        <ul id="choiced-staff"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="jump-footer">
                            <div class="jump-footer-btn-left fl">
                                <span>被添加的人员将被修改【主职务】，请谨慎操作！</span>
                            </div>
                            <div class="jump-footer-btn-right fr">
                                <button class="jump-btn-save" data-bind="click: saveChoicedStaff">确定
                                </button>
                                <button class="jump-btn-cancel" data-bind="click:add_staff_choice_close">
                                    取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="dialog_delete_group" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content jump-content-delete">
                        <div class="jump-header">
                            <p class="jump-header-title">系统提示</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click: delete_position_close;">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body">
                            <div class="jump-body-delete">
                                <p>您确定要删除吗？</p>
                            </div>
                        </div>
                        <div class="jump-footer">
                            <div class="jump-footer-btn-right fr">
                                <button class="jump-btn-save" data-bind="click: delete_duty">确定</button>
                                <button class="jump-btn-cancel" data-bind="click: delete_position_close;">
                                    取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="show_error" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content jump-content-delete">
                        <div class="jump-header">
                            <p class="jump-header-title">系统提示</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click: close_error;">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body">
                            <div id="show-error-info" class="jump-body-delete">
                                <p></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="save-success-new-duty" class="save-success-new"><img src="../images/ip/menu/success_p.png" height="" width=""/>保存成功!</div>
    </div>
    <#include "../footer.ftl">