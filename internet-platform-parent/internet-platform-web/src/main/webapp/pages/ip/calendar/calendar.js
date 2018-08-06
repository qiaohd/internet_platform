define([ 'jquery', 'knockout','text!./calendar.html'],
	function($, ko, template) {
		var viewModel = {
			data : ko.observable({})
		};
		var init = function(){
			
		};
		
		return {
			'model' : viewModel,
			'template' : template,
			'init' : init
		};
	}
);
