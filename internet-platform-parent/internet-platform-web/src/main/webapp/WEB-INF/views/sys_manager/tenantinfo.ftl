<#include "../header.ftl">

<script src="${ctx}/js/ip/sys_manager/tenantInfo/tenantinfo.js"></script>
<script src="${ctx}/js/ip/sys_manager/sys_manager.js" type="text/javascript"></script>	
        <input id="ctx" type="hidden" value="${ctx}">
		<div class="system-container">
            <span id="hirerId" style="display:none">${hirerId}</span>
            <#if ishowFlag??>
			  <span id="ishowFlag" style="display:none">${ishowFlag}</span>
			  <span id="uploadPicInfo" style="display:none">${uploadPicInfo}</span>
			<#else>
			</#if>
            
			<div class="system-main">
				<!--左侧菜单栏开始-->
				<div class="system-left">
					<div class="tabbable tabs-left" style="min-height: 500px;">
						<ul class="nav nav-tabs " >
							
							<#if ishowFlag??>
						      <li id="hirer-set" class="left-menu">
						   <#else>
                              <li id="hirer-set" class="left-menu active"> 						   
						   </#if>
								<a href="#/tenantinfo/tenantset/tenantset">租户设置
								<span class="glyphicon glyphicon-chevron-right"></span>
								</a>
							</li>
							
							<#if ishowFlag??>
						      <li id="hirer-logo" class="left-menu active">
						   <#else>
                              <li id="hirer-logo" class="left-menu"> 						   
						   </#if>
								<a href="#/tenantinfo/tenantlogo/tenantsetlogo">租户LOGO
								<span class="glyphicon glyphicon-chevron-right"></span>
								</a>
							</li>
							
						</ul>
					</div>
				</div>
				<!--右侧内容区开始-->
				<div class="system-right" style="height: 100%;">
					<div class="content">
					</div>
				</div>
				<!--右侧内容区结束-->
				<div class="clearfix"></div>
			</div>
		</div>
		<script>
		    var ishowFlag=$("#ishowFlag").text();
		    if(ishowFlag!=null && ishowFlag!="" && ishowFlag!=undefined){
			    window.onload = $("#hirer-logo").click();
				$(function(){
					window.location = $("#hirer-logo a").attr("href");
				})	
		    }else{
			      window.onload = $("#hirer-set").click();
				$(function(){
					window.location = $("#hirer-set a").attr("href");
				})	
		    }
				
		</script>
<#include "../footer.ftl"> 