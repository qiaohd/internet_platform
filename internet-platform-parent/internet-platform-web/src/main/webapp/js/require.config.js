require.config({
	baseUrl: path,
	waitSeconds: 0,
	paths: {
		text: "trd/requirejs/text",
		css: "trd/requirejs/css",
		jquery: "trd/jquery/jquery-1.12.3.min",
		bootstrap: 'trd/bootstrap/js/bootstrap',
		Jcorp:"trd/Jcorp-0.9.12/js/jquery.Jcrop",
		knockout: "trd/knockout/knockout-3.2.0.debug",
		uui: "trd/uui/js/u",
		wizard:"trd/jquery-bootstrap-wizard/jquery.bootstrap.wizard",
		director:"trd/director/director",
		'jquery.file.upload' : "trd/juqery-file-upload/9.9.2/js/jquery.fileupload",
		'jquery.ui.widget':"trd/jquery-ui/jquery.ui.widget",
		'jquery.iframe.transport':"trd/jquery-iframe-transport/jquery.iframe-transport",
		polyFill: "trd/uui/js/u-polyfill",
		tree: "trd/uui/js/u-tree",
		grid: "trd/uui/js/u-grid",
		jqTreeTable:"trd/treeTable/jquery.treeTable",
		jqUi:"trd/jquery-ui-1.11.4/jquery-ui",
		dragJW:"trd/dragJumpWindow",
		select2:"trd/select2/select2",
		colorPicker:"trd/jQuery-colorpicker/jquery.colorpicker",
		lodopFuncs:"trd/lodop/LodopFuncs",
		wangEditor:"trd/wangEditor/dist/js/wangEditor",
		datatimepicker:"trd/bootstrap-datetimepicker/js/bootstrap-datetimepicker",
	    layer:"trd/layer-v2.2/layer",
	    supportPl: "js/ip/common/supportPlaceholder",
	    jqueryForm: "trd/jquery-form/jquery.form",
	    ajaxHook: "trd/ajaxHook"
	},
	shim: {
		'uui':{
			deps: ["jquery","bootstrap","polyFill", "css!trd/uui/css/font-awesome.min.css"]
		},
		'bootstrap': {
			deps: ["jquery"]
		},
		'jquery.file.upload':{
			deps: ["jquery","jquery.ui.widget","jquery.iframe.transport","css!trd/juqery-file-upload/9.9.2/css/jquery.fileupload.css"]
		},
		'tree': {
			deps: ["uui"]
		},
		'grid': {
			deps: ["uui"]
		},
		'jqUi': {
			deps: ["jquery","css!trd/jquery-ui-1.11.4/jquery-ui.min.css"]
		},
		'select2':{
			deps: ["jquery", "css!trd/select2/select2.css"]
		},
		'Jcorp':{
			deps: ["jquery", "css!trd/Jcorp-0.9.12/css/jquery.Jcrop.css"]
		},
		'colorPicker':{
			deps: ["jquery"]
		},
		'layer':{
			deps: ["jquery", "css!trd/layer-v2.2/skin/layer.css"]
		},
		'jqueryForm':{
			deps: ["jquery"]
		}
	}
});