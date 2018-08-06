require(['jquery', 'knockout', 'bootstrap', 'uui', 'director','Jcorp'], function($, ko) {
	var viewModel = {
		data : ko.observable({}),
		hirerName: ko.observable("")
	};
	ko.applyBindings(viewModel);
	$(".nav-tabs li").click(function(){
		$(this).addClass("active").siblings().removeClass("active");
	});
});