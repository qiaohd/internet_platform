require(['jquery', 'knockout', 'bootstrap', 'uui', 'director','jqTreeTable'], function($, ko) {
	window.ko = ko;
	window.addRouter = function(path, func) {
		var pos = path.indexOf('/:');
		var truePath = path;
		if (pos != -1)
			truePath = path.substring(0,pos);
		func = func || function() {
			var params = arguments;
			initPage('pages/ip/sys_manager' + truePath,params);
		};
		var tmparray = truePath.split("/");
		if(tmparray[1] in router.routes && tmparray[2] in router.routes[tmparray[1]] && tmparray[3] in router.routes[tmparray[1]][tmparray[2]]&& tmparray[4] in router.routes[tmparray[1]][tmparray[2]][tmparray[3]]){
			return;
		}else{
			router.on(path, func);
		}
	};

	window.router = Router();
	
	$(function(){
		$('.left-menu').find("a[href*='#']").each(function() {
			var path = this.hash.replace('#', '');
			addRouter(path);
		});
		window.router.init();
	});

	function initPage(p, id) {
		var module = p;
		requirejs.undef(module);
		require([module], function(module) {
			ko.cleanNode($('.content')[0]);
			$('.content').html('');
			$('.content').html(module.template);
			if(module.model){
				module.model.data.content = ko.observableArray([]);
				ko.applyBindings(module.model, $('.content')[0]);
				module.init(id);
			}else{
				module.init(id, $('.content')[0]);
			}
		});
	}

});