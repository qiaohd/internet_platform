define(['jquery', 'knockout', 'text!./tenantset.html', 'css!./tenantStyle.css'],
    function ($, ko, template) {
		var flagTel=false;
		var flagEmail=false;
        var viewModel = {
        		hirerNameCheck:function(){
        			var hirerNametrim=$.trim($("#hirerName").val());
        			if(hirerNametrim.length<=2){
        				document.getElementById('labelHirerName').innerText="不少于3个汉字";
        				document.getElementById('hirerName').focus();
        			}else if(hirerNametrim.length>=30){
        				document.getElementById('labelHirerName').innerText="最多30个汉字";
        			}else{
        				document.getElementById('labelHirerName').innerText="";
        			}
        		},
        		hirerNameShortCheck:function(){
        			var hirerNameShorttrim = $.trim($("#hirerNameShort").val());
        			if(hirerNameShorttrim==""){
        				document.getElementById('labelNameshort').innerText="不能为空";
        				document.getElementById('hirerNameShort').focus();
        			}else if(hirerNameShorttrim.length>=20){
        				document.getElementById('labelNameshort').innerText="最多20个汉字";
        			}
        			
        			else{
        				document.getElementById('labelNameshort').innerText="";
        			}
        		},
        		hirerLinkNameCheck:function(){
        			var hirerLinkNametrim=$.trim($("#hirerLinkName").val());
        			if(hirerLinkNametrim==""){
        				document.getElementById('labelLinkName').innerText="不能为空";
        				document.getElementById('hirerLinkName').focus();
        			}else if(hirerLinkNametrim.length>=20){
        				document.getElementById('labelLinkName').innerText="最多20个汉字";
        			}
        			else{
        				document.getElementById('labelLinkName').innerText="";
        			}
        		},
        		//0315
        		checkPhoString:function(){
        			var isPhone = /^([0-9]{3,4})?[0-9]{7,8}$/;
        			var pho_value=$.trim(document.getElementById("phoneNo").value);
        			if(pho_value!=""){
        				if(isPhone.test(pho_value)){
            				document.getElementById('labelPhoString').innerText="";
            			}else{
            				document.getElementById('labelPhoString').innerText="格式不正确，如:01088888888";
            			}
        			}else{
        				document.getElementById('labelPhoString').innerText="";
        			}
        			
        		},
        		//15000000000
        		checkTelString:function(){
        			var ctx = $("#ctx").val();
        			var isTel=/^((\+?86)|(\(\+86\)))?(13[0123456789][0-9]{8}|15[012356789][0-9]{8}|17[01568][0-9]{8}|18[012356789][0-9]{8}|147[0-9]{8}|145[0-9]{8}|149[0-9]{8})$/;		
        			var tel_value=$.trim(document.getElementById("cellphoneNo").value);
        				if(isTel.test(tel_value)){
        					$.ajax({
        						url:ctx + "/sysmanager/hirercfg/phoneCheck",
        						type: "GET",
        			            dataType: "json",
        			            async: false,
        			            data:{
        			            	"phone":tel_value,  
        					        "hirerId":$("#hirerId").text()
        			            },
        			            success: function (data) {
        			            	if(data.result=="success"){
        			            		document.getElementById('labelTelString').innerText="";
        			            		flagTel=true;
        			            		return;
        			            	}else if(data.result=="PH_two"){
        			            		document.getElementById('labelTelString').innerText="该手机号已存在";
        			            		$("#labelTelString").css("text-align","left");
        			            		flagTel=false;
        			            		return;
        			            	}
        			            }
        					});
            			}else{
            				document.getElementById('labelTelString').innerText="格式不正确";
            				flagTel=false;
		            		return;
            			}
        		},
        		postcodeCheck:function(){
                	//053000
                	var ss=$.trim(document.getElementById('postcode').value);
                    var re= /^[0-9][0-9]{5}$/;
                    
                    if(ss!=""){
                    	 if(re.test(ss)){
                    		 document.getElementById('label5').innerText="";
                    	 }else{
                             document.getElementById('label5').innerText="格式不正确！";
                           }
                    }else{
                    	document.getElementById('label5').innerText="";
                    }
                },
                //http://
                urlstringCheck:function(){
                	var urlString = $.trim(document.getElementById("website").value);
                	var  regExp = /(http[s]?|ftp):\/\/[^\/\.]+?\..+\w$/i;
                	if(urlString!=""){
                		if(regExp.test(urlString)){
                			document.getElementById('labelurlString').innerText="";
                		}else{
                            document.getElementById('labelurlString').innerText="请输入正确网址，如：https://...";
                        }
                	}else{
                    	document.getElementById('labelurlString').innerText="";
                    }
                },
              //@email:
                emailstringCheck:function(){ 
                	var ctx = $("#ctx").val();
                	var emailString = $.trim(document.getElementById("email").value);
                	var  myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
                	if(emailString!=""){
                		if(myreg.test(emailString)){
                			$.ajax({
                				url:ctx + "/sysmanager/hirercfg/emailCheck",
                				type: "GET",
                	            dataType: "json",
                	            async: false,
                	            data:{
                	            	"userEmail":emailString,
                	            	"hirerId":$("#hirerId").text()
                	            },
                	            success: function (data) {
                	            	if(data.result=="success"){
                	            		document.getElementById('labelemailString').innerText="";
                	            		flagEmail=true;
                	            		return;
                	            	}else if(data.result=="EM_two"){
                	            		document.getElementById('labelemailString').innerText="该邮箱已存在";
                	            		$("#labelemailString").css("text-align","left");
                	            		flagEmail=false;
                	            		return;
                	            	}
                	            }
                			});
                		}else{
                            document.getElementById('labelemailString').innerText="请输入正确邮箱，如：123@163.com";
                            flagEmail=false;
                        	return;
                        }
                	}else{
                    	document.getElementById('labelemailString').innerText="";
                    	flagEmail=true;
                    	return;
                    }
                },
            data: ko.observable({}),
            hirerNo: ko.observable(""),
            hirerName: ko.observable(""),
            hirerNameShort: ko.observable(""),
            hirerLinkName: ko.observable(""),
            duty: ko.observable(""),
            phoneNo: ko.observable(""),
            cellphoneNo: ko.observable(""),
            email: ko.observable(""),
            fax: ko.observable(""),
            hirerType: ko.observable(""),
            region: ko.observable(""),
            address: ko.observable(""),
            postcode: ko.observable(""),
            website: ko.observable(""),
            hirerSexMan: ko.observable(true),
            hirerSexWomen: ko.observable(false),
            hirerNameInValidMessage: ko.observable(false),
            HirerInfoSave: function () {
            	var hirer = $("#hirer-type option:selected").text();
            	if(hirer=="请选择"){
            		hirer="";
            	}
            	var ss=$.trim(document.getElementById('postcode').value);
                var re= /^[0-9][0-9]{5}$/;
                var urlString = $.trim(document.getElementById("website").value);
                var regExp = /(http[s]?|ftp):\/\/[^\/\.]+?\..+\w$/i;
             	var isPhone = /^([0-9]{3,4})?[0-9]{7,8}$/;
    			var pho_value=$.trim(document.getElementById("phoneNo").value);
    			var isTel=/^((\+?86)|(\(\+86\)))?(13[012356789][0-9]{8}|15[012356789][0-9]{8}|18[02356789][0-9]{8}|147[0-9]{8}|1349[0-9]{7})$/;		
    			var tel_value=$.trim(document.getElementById("cellphoneNo").value);
    			var emailString = $.trim(document.getElementById("email").value);
            	var  myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            	viewModel.emailstringCheck();
            	viewModel.checkTelString();
            	if ($.trim(viewModel.hirerNameShort()).length < 1) {
                    $("#hirerNameShort").focus();
                } else if ($.trim(viewModel.hirerLinkName()).length < 1) {
                    $("#hirerLinkName").focus();
                }else if(!myreg.test(emailString) && emailString!=""){
                	document.getElementById('labelemailString').innerText="请输入正确邮箱，如：123@163.com";
                }else if((re.test(ss) || ss=="") &&(regExp.test(urlString) || urlString=="")&&(flagEmail==true || emailString=="")&&(pho_value=="" || isPhone.test(pho_value))&& flagTel==true && $("#hirerName").val().length>=3 && $("#hirerNameShort").val()!="" && $("#hirerLinkName").val()!=""){
                	document.getElementById('labelemailString').innerText="";
                	var setValue = {
                        "hirerId": $.trim($("#hirerId").text()),
                        "hirerNo": $.trim($("#hirerNo").text()),
                        "sex": $("#sexall input:checked").val(),
                        "hirerName": $.trim($("#hirerName").val()),
                        "password": $.trim($("#password").text()),
                        "hirerShortName": $.trim($("#hirerNameShort").val()),
                        "linkman": $.trim($("#hirerLinkName").val()),
                        "duty": $.trim($("#duty").val()),
                        "phoneNo": pho_value,
                        "cellphoneNo": tel_value,
                        "email": emailString,
                        "fax": $.trim($("#fax").val()),
                        "hirerType": hirer,
                        "region": $("#province option:selected").val() + "," + $("#city option:selected").val() + "," + $("#county option:selected").val(),
                        "address": $.trim($("#address").val()),
                        "postcode": ss,
                        "website": urlString
                    };
                	var ctx = $("#ctx").val();
                    $.ajax({
                        url:ctx + "/sysmanager/hirercfg/saveHirerInfo",
                        type: "POST",
                        data: setValue,
                        success: function (data) {
                            if (data.result) {
                            	$("#dialog-add-dept").modal('hide');
        		    			$("#save-success-new").css("display","block");
        		    			$("#save-success-text").text("保存成功！");
        	            		$("#save-success-new").fadeOut(4000);
								$(window).scrollTop(0);
								$("#hirerName").focus();
                            } else {
                            	$("#sure_add_group").modal();
        		    			$("#error_add_group-warn").html("<img src='../images/ip/menu/errorNotice.png' /><span>"+ data.reason +"</span> ");
        		    			var show_error = document.getElementById("jump-content");
        						show_error.style.display='block';
                                //alert("保存失败!");
                            }
                        }
                    });
                }
            }
        };
        /*
         * 界面初始化
         */
        var hirerId = $("#hirerId").text();
        var ctx = $("#ctx").val();
        var time = new Date();
        $.ajax({
            url: ctx + "/sysmanager/hirercfg/getHirerById?hirerId=" + hirerId,
            type: "GET",
            dataType: "json",
            data:{time: time.getTime()},
            success: function (data) {
                viewModel.hirerNo(data.hirerBaseInfo.hirerNo);
                viewModel.hirerName(data.hirerBaseInfo.hirerName);
                viewModel.hirerNameShort(data.hirerBaseInfo.hirerShortName);
                viewModel.hirerLinkName(data.hirerBaseInfo.linkman);
                viewModel.duty(data.hirerBaseInfo.duty);
                viewModel.phoneNo(data.hirerBaseInfo.phoneNo);
                viewModel.cellphoneNo(data.hirerBaseInfo.cellphoneNo);
                viewModel.email(data.hirerBaseInfo.email);
                viewModel.fax(data.hirerBaseInfo.fax);
                var proCode=data.regionCodeAndName.proCode;
                var cityCode=data.regionCodeAndName.cityCode;
                var countryCode=data.regionCodeAndName.countryCode;
                viewModel.address(data.hirerBaseInfo.address);
                viewModel.postcode(data.hirerBaseInfo.postcode);
                viewModel.website(data.hirerBaseInfo.website);
                $("#hirer-type").append("<option value=''>请选择</option>");
                for (var i = 0; i < data.enumTypeInfo.length; i++) {
                	if(data.hirerBaseInfo.hirerType==data.enumTypeInfo[i].detailInfo){
                		$("#hirer-type").append("<option selected='selected'>"+data.enumTypeInfo[i].detailInfo+"</option>");
                	}else{
                		$("#hirer-type").append("<option>" + data.enumTypeInfo[i].detailInfo + "</option>");
                	}
                }
                
                $("#password").text(data.hirerBaseInfo.password);
                if (data.hirerBaseInfo.sex == "1") {
                    viewModel.hirerSexMan("1");
                } else {
                    viewModel.hirerSexWomen("2");
                }
                for (var i = 0; i < data.provinceInfo.length; i++) {
                    if(data.regionCodeAndName.proCode==data.provinceInfo[i].theCode){
                    	$("#province").append("<option selected='selected' value='" + data.provinceInfo[i].theCode + "'>" + data.provinceInfo[i].theName + "</option>")
                    }else{
                    	 $("#province").append("<option value='" + data.provinceInfo[i].theCode + "'>" + data.provinceInfo[i].theName + "</option>")
                    }
                }
               if(proCode!=null){
            	   getCity(proCode,cityCode);
               }
               if(cityCode!=null){
            	   getCounty(cityCode,countryCode);
               }
                /*
                 * 市级初始化
                 */
                $("#province").on("change", function () {
                    var proCode = $('#province').val();
                    getCity(proCode,"");
                    $("#county").empty();
                    $("#county").append("<option value='000000'>请选择区县</option>");
                });
                /*
                 * 县级初始化
                 */
                $("#city").on("change", function () {
                    var cityCode = $("#city").val();
                    getCounty(cityCode,countryCode);
                });
            }
        });
        
        var getCity = function(proCode,cityCode){
        	var ctx = $("#ctx").val();
        	 $.ajax({
             	url: ctx + "/sysmanager/hirercfg/getcityInfo?proCode=" + proCode,
             	type: "GET",
             	dataType: "json",
             	success: function (data) {
             		$("#city").empty();
             		$("#city").append("<option value='000000'>请选择市</option>");
             		for (var i = 0; i < data.length; i++) {
             			if(cityCode==data[i].theCode){
             				$("#city").append("<option selected='selected' value='" + data[i].theCode + "'>" + data[i].theName + "</option>");
             			}else{
             				$("#city").append("<option value='" + data[i].theCode + "'>" + data[i].theName + "</option>");
             			}
             		}
             	}
             });
        };
        
        var getCounty = function(cityCode,countryCode){
        	var ctx = $("#ctx").val();
        	 $.ajax({
                 url: ctx + "/sysmanager/hirercfg/getcountyInfo?cityCode=" + cityCode,
                 type: "GET",
                 dataType: "json",
                 success: function (data) {
                     //$("#county option").remove();
                     $("#county").empty();
                     //$("#county").html("");
                     $("#county").append("<option value='000000'>请选择区县</option>");
                     for (var i = 0; i < data.length; i++) {
                     	if(countryCode==data[i].theCode){
                     		$("#county").append("<option selected='selected' value='" + data[i].theCode + "'>" + data[i].theName + "</option>");
                     	}else{
                     		$("#county").append("<option value='" + data[i].theCode + "'>" + data[i].theName + "</option>");
                     	}
                     }

                 }
             });
        };
        viewModel.jump_btn_close=function(){
    		$("#sure_add_group").modal("hide");
    		var show_error = document.getElementById("jump-content");
    		show_error.style.display='none';
    		$("#dialog-editor-dept").modal("hide");
    		viewModel.showEditorParDeptTree(false);
    	};
        var init = function () {

        };
        return {
            'model': viewModel,
            'template': template,
            'init': init
        };
    }
);