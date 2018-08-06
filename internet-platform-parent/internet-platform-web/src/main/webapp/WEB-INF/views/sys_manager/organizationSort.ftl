<#include "../header.ftl">
    <link href="${ctx}/trd/components/components.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/changeTree.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/system/organizationSort/orgSort.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/css/ip/resetCommon.css" rel="stylesheet" type="text/css">
    <script src="${ctx}/js/ip/sys_manager/organizationSort/organizationSort.js" type="text/javascript"></script>
    <script>
        window.$ctx = '${ctx}';
    </script>
    <span id="hirerId" style="display:none">${hirerId}</span>
    <div class="system-container">
        <div class="system-main">
            <!--左侧菜单栏开始-->
            <div class="system-left">
                <div class="tabbable tabs-left" style="min-height: 500px;">
                    <div style="width:100%;display: inline-block;solid red 1px;float:left;padding-top: 5px;overflow:auto;">
                    <div class="ztree"
                         u-meta='{"id":"departmentTree","data":"dataTable1","type":"tree","idField":"coId","pidField":"parentCoId","nameField":"coCodeAndName","setting":"departmentTreeSetting"}'>
                    </div>
                    <div class="ztree"
                         u-meta='{"id":"dutyTree","data":"roleDataTable","type":"tree","idField":"roleId","pidField":"parentRoleId","nameField":"roleName","setting":"dutyTreeSetting"}'>
                    </div>
                    <div class="ztree"
                         u-meta='{"id":"userTree","data":"roleDataTable","type":"tree","idField":"roleId","pidField":"parentRoleId","nameField":"roleName","setting":"userTreeSetting"}'>
                    </div>
                </div>
                </div>
            </div>
            <!--右侧内容区开始-->
            <div class="system-right" style="height: 100%;" >
                <div class="duty-info">
                    <ul id="sort-tabs" class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active"><a href="#department" id="home-tab" role="tab"
                                                                  data-toggle="tab" aria-controls="department"
                                                                  aria-expanded="false"
                                                                  data-bind="click: getDepartmentInit">部门排序</a></li>
                        <li role="presentation" class=""><a href="#duty" role="tab" id="duty-tab"
                                                            data-toggle="tab" aria-controls="duty"
                                                            aria-expanded="true" data-bind="click: getDuty">职务排序</a>
                        </li>
                        <li role="presentation" class=""><a href="#user" role="tab" id="user-tab"
                                                            data-toggle="tab" aria-controls="user"
                                                            aria-expanded="true" data-bind="click: getUserInit">用户排序</a>
                        </li>
                    </ul>
                    <div id="myTabContent" class="tab-content">
                        <div role="tabpanel" class="tab-pane fade active in" id="department"
                             aria-labelledby="department-tab">
                            <input id="selected-node-name-department" type="hidden" value="">
                            <span id="selected-node-department" style="display: none;"></span>
                            <div id="choice-sort-mode-department">
                                <button class="drag-sort-btn drag-sort-btn-common" data-bind="click: setDepartmentSort">拖动排序</button>
                                <button class="letters-sort-btn drag-sort-btn-common" data-bind="click: sortByLetterJumpDepartment">按首字母排序</button>
                            </div>
                            <table id="department-table">
                                <thead>
                                <tr>
                                    <th class="department-num">部门编号</th>
                                    <th class="department-name">部门名称</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div role="tabpanel" class="tab-pane fade" id="duty"
                             aria-labelledby="duty-tab">
                            <input id="selected-node-name-duty" type="hidden" value="">
                            <span id="selected-node-duty" style="display: none;"></span>
                            <div id="choice-sort-mode-duty">
                                <button class="drag-sort-btn drag-sort-btn-common" data-bind="click: setDutySort">拖动排序</button>
                                <button class="letters-sort-btn drag-sort-btn-common" data-bind="click: sortByLetterJumpDuty">按首字母排序</button>
                            </div>
                            <table id="duty-table">
                                <thead>
                                <tr>
                                    <th class="department-num">职务编号</th>
                                    <th class="department-name">职务名称</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div role="tabpanel" class="tab-pane fade" id="user"
                             aria-labelledby="user-tab">
                            <input id="selected-node-name-user" type="hidden" value="">
                            <span id="selected-node-user" style="display: none;"></span>
                            <div id="choice-sort-mode-user">
                                <button class="drag-sort-btn drag-sort-btn-common" data-bind="click: setUserSort">拖动排序</button>
                                <button class="letters-sort-btn drag-sort-btn-common" data-bind="click: sortByLetterJumpUser">按首字母排序</button>
                            </div>
                            <table  id="user-table">
                                <thead>
                                <tr>
                                    <th class="user-num">人员编号</th>
                                    <th class="user-name">姓名</th>
                                    <th class="user-account">账号</th>
                                    <th class="user-duty-name">职务名称</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!--右侧内容区结束-->
            <div class="clearfix"></div>

            <div class="modal fade" id="sort-jump-window" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content jump-content-delete">
                        <div class="jump-header">
                            <p class="jump-header-title">拖动排序</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click: sortCloseWindow;">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body">
                            <!--<div class="sotabjump-window">-->
                            <!--<ul></ul>-->
                            <!--</div>-->
                            <table class="sotabjump-window">
                                <tbody></tbody>
                            </table>
                        </div>
                        <div class="jump-footer">
                            <div class="jump-footer-btn-right fr">
                                <button class="jump-btn-save" data-bind="click: sortSaveWindow">确定</button>
                                <button class="jump-btn-cancel" data-bind="click: sortCloseWindow">
                                    取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="sure-jump-window" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content jump-content-delete">
                        <div class="jump-header">
                            <p class="jump-header-title">按首字母排序</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click: sortByFirstLetterJumpClose;">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body">
                            <div id="sort-by-letter-content">
                                <p><i class="sure-notice"></i>确定要按照首字母排序吗？</p>
                            </div>
                        </div>
                        <div class="jump-footer">
                            <div class="jump-footer-btn-right fr">
                                <button class="jump-btn-save" data-bind="click: sortByFirstLetterJumpSure">确定</button>
                                <button class="jump-btn-cancel" data-bind="click: sortByFirstLetterJumpClose">
                                    取消
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="error-jump-window" tabindex="-1" role="dialog" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="jump-content jump-content-delete">
                        <div class="jump-header">
                            <p class="jump-header-title">系统提示</p>
                            <a class="jump-header-close" href="javascript:void(0);"
                               data-bind="click: errorCloseWindow;">
                                <img src="../images/ip/sys/close.png" width="12" height="13">
                            </a>
                        </div>
                        <div class="jump-body">
                            <p id="error-content"></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#include "../footer.ftl">