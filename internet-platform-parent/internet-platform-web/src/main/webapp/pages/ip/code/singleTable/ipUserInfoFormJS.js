define([ 'jquery', 'knockout','text!./ipUserInfoForm.html','bootstrap', 'uui', 'director', 'tree', 'grid','dragJW','css!./ipUserInfoList.css'], function($, ko,template) {
		
		var viewModel = {
            data : ko.observable({})
        };
		
		viewModel. getData = function() {
		    $.ajax({
		        url:$ctx+'/gen/genTable/getColumnProperties',
		        type: 'GET',
		        dataType: 'json',
		        data:{
		           'id':"",
		           'tableName':'ip_user_info'
		        },
		        success: function (data) {
		            console.log(data);
		            viewModel.editPage(data);
		            viewModel.getEnumnSel(data);
		        }
		     })
		}
		
		
		
		viewModel.getEnumnSel= function(data) {
		
		for(var i = 0; i <data.genTable.columnList.length; i++){
		         var dicType="";
		         var dicName=data.genTable.columnList[i].columnComments;
              if(data.genTable.columnList[i].showType == "select"){
            	  var x=i;
                $.ajax({
			        url:$ctx+'/gen/genTable/getColumnSel',
			        type: 'GET',
			        dataType: 'json',
			        data:{
			           'dicType':dicType,
			           'dicName':dicName
			        },
			        success: function (getEnumnSeldata) {
	                        $("#"+data.genTable.columnList[x].javaField).append("<option>请选择</option>");
	            	        $("#"+data.genTable.columnList[x].javaField + "option:selected").text(getEnumnSeldata.detailInfo);
			            	for (var j = 0; j < getEnumnSeldata.length; j++) {
			                    $("#"+data.genTable.columnList[x].javaField).append("<option>" + getEnumnSeldata[j].detailInfo + "</option>");
			                }
			        }
              });
            }
		  }
		}
		
		viewModel. editPage = function(data){
		    
		      
		    var html = "";
		    var n = 12;
		        n = n/2;
		    console.log($(".area-type"));
		    for(var i = 0; i <data.genTable.columnList.length; i++){
              if(data.genTable.columnList[i].isInsert == "Y"){
              console.log(data.genTable.columnList[i].showType);
            switch (data.genTable.columnList[i].showType)
            {
                case "input":
                    html_text = "<div class='col-md-"+ n +"'><div class='common'><label>" + data.genTable.columnList[i].columnComments + ":</label><input type='text' class='u-form-control focus'></div></div>";
                    break;
                case "textarea":
                    html_text = "<div class='col-md-"+ n +"'><div class='common' id='area-type'><label>" + data.genTable.columnList[i].columnComments + ":</label><textarea maxlength='100' class='u-form-control'></textarea></div></div>";
                    break;
                case "select":
                    html_text = "<div class='col-md-"+ n +"'><div class='common'><label>" + data.genTable.columnList[i].columnComments + ":</label><select id='"+data.genTable.columnList[i].javaField+"' class='u-form-control' readonly='readonly'></select></div></div>";
                    break;
                case "radiobox":
                    html_text = "<div class='col-md-"+ n +"'><div class='common'><label>" + data.genTable.columnList[i].columnComments + ":</label><input type='radio' class='u-radio-button'></div></div>";
                    break;
                case "checkbox":
                    html_text = "<div class='col-md-"+ n +"'><div class='common'><label>" + data.genTable.columnList[i].columnComments + ":</label><label class='u-checkbox w-64'><input type='checkbox' class='u-checkbox-input'></label></div></div>";
                    break;
                case "dateselect":
                    html_text = "<div class='col-md-"+ n +"'><div class='common'><label>" + data.genTable.columnList[i].columnComments + ":</label><div class='u-datepicker'><input class='u-input' type='text'></div></div></div>";
                    break;
             }
             html = html + html_text;
          }
      }
		    $("#code-area").append(html);
		    if(data.layout == "1列") $("#area-type").css("margin-bottom","35px");
		
		}
		
		
		var init = function(){
            ko.cleanNode($('.content')[0]);
            app = u.createApp(
                {
                    el:'.content',
                    model: viewModel
                }
            );
            viewModel. getData();
            
        };

        return {
            'model' : viewModel,
            'template' : template,
            'init' : init
        };
	});