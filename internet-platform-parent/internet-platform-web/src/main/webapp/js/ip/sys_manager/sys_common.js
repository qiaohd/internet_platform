require(['jquery', 'ajaxHook'], function($,aH) {
//    hookAjax({
//        //拦截回调
//        onreadystatechange:function(xhr){
//            // console.log("onreadystatechange called: %O",xhr);
//        },
//        onload:function(xhr){
//            // console.log("onload called: %O",xhr);
//        },
//        //拦截函数
//        open:function(arg){
//            // console.log("open called: method:%s,url:%s,async:%s",arg[0],arg[1],arg[2]);
//            function getCookie(c_name) {
//                if (document.cookie.length>0) {
//                    c_start=document.cookie.indexOf(c_name + "=");
//                    if (c_start!=-1) {
//                        c_start=c_start + c_name.length+1
//                        c_end=document.cookie.indexOf(";",c_start)
//                        if (c_end==-1) c_end=document.cookie.length
//                        return unescape(document.cookie.substring(c_start,c_end))
//                    }
//                }
//                return ""
//            }
//            var u_usercode = getCookie('u_usercode');
//            var old_u_usercode = $("#old_u_usercode").text();
//            if(u_usercode != old_u_usercode){
//                alert("用户信息不匹配，请重新打开！");
//                return true;
//            }
//        }
//    })
});
