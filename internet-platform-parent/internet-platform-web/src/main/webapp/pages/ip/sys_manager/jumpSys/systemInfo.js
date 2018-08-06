$(function(){
	//键盘回车执行登录
		$(document).keydown(function(e){
			var evt = window.event ? window.event : e ;
			if(evt.keyCode === 13){
				$("#btn_ok").focus();
				input_pwd_sys();
			}
		});
});
function input_pwd_sys(){
	var pwd=$("#password").val();
	var info={"pwd":pwd};
	$.ajax({
		url: '../repeat_pwd_page',
		async: false,
        type: 'POST',
        data:info,
        datatype:'text',
        success: function(data) {
        	if("false"==data){
               alert("请重新登录");
        	}else if("wrong_pwd"==data){
        	   alert("密码有误");
        	 }else{
        	    window.location=basePath+"/tenant/tenantInfo?HirerId="+data;
        	}
        }
	});
}

function btn_close() {
	window.close();
}
