define([ 'jquery', 'knockout','text!./sys.html'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		var init = function(){
			
		};
		
		viewModel.get_username_cookie = function(cookie_name){
			
			var userName=$.cookie(cookie_name);
		};
		
		viewModel.close_window_edit = function(){
			var show_content = document.getElementById("input_pwd");
		    show_content.style.display='none';
		    
		};
		
		viewModel.input_pwd_sys = function(){
			
			var pwd=$("#password").val();
			var userName=viewModel.get_username_cookie("username");
			var info={"pwd":pwd,"userName":userName};
			$.ajax({
				url: 'repeat_pwd_page',
                type: 'POST',
                data:info,
                success: function(data) {
                	if("false"==data){
                       alert("请重新登录");
                	}else if("wrong_pwd"==data){
                	   alert("密码有误");
                	 }else{
                	    window.location="sys_manager/sysInfo?username="+data+"&pwd="+pwd;
                	}
                }
			});
		};
		
		viewModel.get_username_cookie = function(name, value, options) {
		    if (typeof value != 'undefined') { // name and value given, set cookie
		        options = options || {};
		        if (value === null) {
		            value = '';
		            options = $.extend({}, options); // clone object since it's unexpected behavior if the expired property were changed
		            options.expires = -1;
		        }
		        var expires = '';
		        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
		            var date;
		            if (typeof options.expires == 'number') {
		                date = new Date();
		                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
		            } else {
		                date = options.expires;
		            }
		            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
		        }
		        // NOTE Needed to parenthesize options.path and options.domain
		        // in the following expressions, otherwise they evaluate to undefined
		        // in the packed version for some reason...
		        var path = options.path ? '; path=' + (options.path) : '';
		        var domain = options.domain ? '; domain=' + (options.domain) : '';
		        var secure = options.secure ? '; secure' : '';
		        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
		    } else { // only name given, get cookie
		        var cookieValue = null;
		            var cookies = document.cookie.split(';');
		            for (var i = 0; i < cookies.length; i++) {
		                var cookie = jQuery.trim(cookies[i]);
		                // Does this cookie string begin with the name we want?
		                if (cookie.substring(0, name.length + 1) == (name + '=')) {
		                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
		                    break;
		                }
		            }
		        return cookieValue;
		    }
         };
		
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
