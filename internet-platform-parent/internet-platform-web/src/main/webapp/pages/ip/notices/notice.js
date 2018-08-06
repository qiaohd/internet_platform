define([ 'jquery', 'knockout','text!./notice.html'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		
		var init = function(){
			viewModel.selChild();
		};
		
		var zNodes2;
		var treeNodes;
		var ztree;
		viewModel.selChild=function(){
			
			var input_notice=$("#notice_input").val();
			var json_menuid={"input_notice":input_notice};
			
			var setting = {
					view: {
						dblClickExpand: true,
						showLine: true,
						selectedMulti: true
					},
					data: {
						simpleData: {
							enable:true,
							idKey: "id",
							pIdKey: "pId",
							rootPId: ""
						}
					}
				};
			 
			$.ajax({
				url: 'menuShow/selectChildMenu',
                type: 'GET',
                data:json_menuid,
                success: function(data) {
                	 zNodes2=data;
                	 var t = $("#tree");
         			ztree = $.fn.zTree.init(t, setting, zNodes2);
         			
                }
			});
			
		};
		
		
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
