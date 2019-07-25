/**************************************************************************
 * tp-color-picker.js - Color Picker Plugin for Revolution Slider
 * @version: 1.0.1 (2.28.2017)
 * @author ThemePunch
**************************************************************************/

;window.RevColor = {
	
	defaultValue: '#ffffff',
	isColor: /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i,
	
	get: function(val) {
		
		if(!val) return 'transparent';
		return RevColor.process(val, true)[0];
		
	},
	
	parse: function(val, prop, returnColorType) {
		
		val = RevColor.process(val, true);
		var ar = [];
			
		if(!prop) ar[0] = val[0];	
		else ar[0] = prop + ': ' + val[0] + ';';
			
		if(returnColorType) ar[1] = val[1];
		return ar;
		
	},
	
	convert: function(color, opacity) {
		
		// falsey or non-string
		if(!color || typeof color !== 'string') return RevColor.defaultValue;
		
		// transparent
		if(color === 'transparent') return color;
		
		// gradients
		if(color.search(/\[\{/) !== -1 || color.search('gradient') !== -1) return RevColor.process(color, true)[0];
		
		// if no opacity value exists
		if(typeof opacity === 'undefined' || isNaN(opacity)) return RevColor.process(color, true)[0];
		
		// convert opacity from float to int
		opacity = parseFloat(opacity);
		if(opacity <= 1) opacity *= 100;
		
		// min/max opacity
		opacity = Math.max(Math.min(parseInt(opacity, 10), 100), 0);
		
		// transparent for 0 opacity
		if(opacity === 0) return 'transparent';
		
		try {
		
			if(color.search('#') !== -1 || color.length < 8) {
				
				if(!RevColor.isColor.test(color)) color = color.replace(/[^A-Za-z0-9#]/g, '');
				return RevColor.processRgba(RevColor.sanitizeHex(color), opacity);
				
			}
			
			else {
				
				color = RevColor.rgbValues(color, 3);
				return RevColor.rgbaString(color[0], color[1], color[2], opacity * 0.01);
				
			}
			
		}
		catch(e) {
			
			return RevColor.defaultValue;
			
		}
		
	},
	
	process: function(clr, processColor) {
		
		if(typeof clr !== 'string') {
			
			if(processColor) clr = RevColor.sanitizeGradient(clr);
			return [RevColor.processGradient(clr), 'gradient', clr];
			
		}
		else if(clr.trim() === 'transparent') {
			
			return ['transparent', 'transparent'];
			
		}
		else if(clr.search(/\[\{/) !== -1) {
			
			try {
				
				clr = JSON.parse(clr.replace(/\&/g, '"'));
				if(processColor) clr = RevColor.sanitizeGradient(clr); 
				return [RevColor.processGradient(clr), 'gradient', clr];
				
			}
			catch(e) {
				
				console.log('RevColor.process() failed to parse JSON string');
				return [
				
					'linear-gradient(0deg, rgba(255, 255, 255, 1) 0%, rgba(0, 0, 0, 1) 100%)', 
					'gradient', {
						
						'type': 'linear', 
						'angle': '0', 
						'colors': [
					
							{'r': '255', 'g': '255', 'b': '255', 'a': '1', 'position': '0', 'align': 'bottom'}, 
							{'r': '0', 'g': '0', 'b': '0', 'a': '1', 'position': '100', 'align': 'bottom'}
						
						]
					}
				];
			}
			
		}
		else if(clr.search('#') !== -1) {
			
			return [RevColor.sanitizeHex(clr), 'hex'];
			
		}
		else if(clr.search('rgba') !== -1) {
			
			return [clr.replace(/\s/g, ''), 'rgba'];
			
		}
		else {
			
			return [clr.replace(/\s/g, ''), 'rgb'];
			
		}
		
	},
	
	transparentRgba: function(val, processed) {
		
		if(!processed) {
		
			var tpe = RevColor.process(val)[1];
			if(tpe !== 'rgba') return false;
			
		}
		
		return RevColor.rgbValues(val, 4)[3] === '0';
		
	},
	
	rgbValues: function(values, num) {
		
		values = values.substring(values.indexOf('(') + 1, values.lastIndexOf(')')).split(',');
		if(values.length === 3 && num === 4) values[3] = '1';
		
		for(var i = 0; i < num; i++) values[i] = values[i].trim();
		return values;
		
	},
	
	rgbaString: function(r, g, b, a) {
		
		return 'rgba(' + r + ', ' + g + ', ' + b + ', ' + a + ')';
		
	},
	
	rgbToHex: function(clr) {
		
		var values = RevColor.rgbValues(clr, 3);
		return RevColor.getRgbToHex(values[0], values[1], values[2]);
		
	},
	
	rgbaToHex: function(clr) {
		
		var values = RevColor.rgbValues(clr, 4);
		return [RevColor.getRgbToHex(values[0], values[1], values[2]), values[3]];
		
	},
	
	getOpacity: function(val) {
		
		return parseInt(RevColor.rgbValues(val, 4)[3] * 100, 10) + '%';
		
	},
	
	getRgbToHex: function(r, g, b) {
		
		return '#' + ("0" + parseInt(r).toString(16)).slice(-2) + 
					 ("0" + parseInt(g).toString(16)).slice(-2) + 
					 ("0" + parseInt(b).toString(16)).slice(-2);
		
	},
	
	joinToRgba:function(val) {
		
		val = val.split('||');
		return RevColor.convert(val[0], val[1]);
		
	},
	
	processRgba: function(hex, opacity) {
		
		hex = hex.replace('#', '');
		
		var hasOpacity = typeof opacity !== 'undefined',
		rgb = hasOpacity ? 'rgba' : 'rgb',
		color = rgb + '(' + 
					parseInt(hex.substring(0, 2), 16) + ', ' + 
					parseInt(hex.substring(2, 4), 16) + ', ' + 
					parseInt(hex.substring(4, 6), 16);
		
		if(hasOpacity) color +=  ', ' + (parseInt(opacity, 10) * 0.01).toFixed(2).replace(/\.?0*$/, '') + ')';
		else color +=  ')';
		
		return color;
		
	},
	
	processGradient: function(obj) {

		var tpe = obj.type,
			begin = tpe + '-gradient(',
			middle = tpe === 'linear' ? obj.angle + 'deg, ' : 'ellipse at center, ',
			colors = obj.colors,
			len = colors.length,
			end = '',
			clr;
		
		for(var i = 0; i < len; i++) {
			
			if(i > 0) end += ', ';
			clr = colors[i];
			end += 'rgba(' + clr.r + ', ' + clr.g + ', ' + clr.b + ', ' + clr.a + ') ' + clr.position + '%';
			
		}
		
		return begin + middle + end + ')';
		
	},
	
	sanitizeHex: function(hex) {
	
		hex = hex.replace('#', '').trim();
		if(hex.length === 3) {
			
			var a = hex.charAt(0),
				b = hex.charAt(1),
				c = hex.charAt(2);
				
			hex = a + a + b + b + c + c;
			
		}
		
		return '#' + hex;
		
	},
	
	sanitizeGradient: function(obj) {

		var colors = obj.colors,
			len = colors.length,
			ar = [],
			prev;
			

		for(var i = 0; i < len; i++) {
			
			var cur = colors[i];
			delete cur.align;
			
			if(prev) {
				
				if(JSON.stringify(cur) !== JSON.stringify(prev)) {
					
					ar[ar.length] = cur;
					
				}
				
			}
			else {
				
				ar[ar.length] = cur;
				
			}
			
			prev = cur;
			
		}
		
		obj.colors = ar;
		return obj;
		
	}
	
};

(function($) {
	
	var doc,
		angle,
		inited,
		bodies,
		isFull,
		points,
		radial,
		hitTop,
		onEdit,
		onInit,
		onAjax,
		cPicker,
		prepped,
		reverse,
		gradBtn,
		gradHex,
		defMode,
		inFocus,
		defEdit,
		defAjax,
		colorBox,
		colorBtn,
		colorHex,
		colorLoc,
		gradIris,
		blankRow,
		curPoint,
		appended,
		gradCore,
		onCancel,
		defCancel,
		defChange,
		dragPoint,
		topPoints,
		botPoints,
		hitBottom,
		textareas,
		curSquare,
		curCorner,
		colorIris,
		langColor,
		gradInput,
		onReverse,
		wheelDown,
		editTitle,
		colorView,
		angleWheel,
		wheelPoint,
		gradOutput,
		colorClear,
		directions,
		pointIndex,
		opacityLoc,
		gradViewed,
		wheelActive,
		gradOpacity,
		groupPoints,
		pointerWrap,
		gradSection,
		colorDelete,
		curLanguage,
		customAdded,
		defWidgetId,
		selectedHex,
		currentInput,
		presetGroups,
		colorSection,
		colorOpacity,
		openingValue,
		openingColor,
		appendedHTML,
		currentColor,
		supressColor,
		supressWheel,
		supressCheck,
		gradientsOpen,
		isTransparent,
		mainContainer,
		opacityDelete,
		selectedColor,
		opacitySlider,
		supressOpacity,
		currentEditing,
		defaultClasses,
		changeCallback,
		gradientPreview = {};
		
	var hitWidth = 265,
		maxPoints = 20,
		centerWheel = 30,
		minGradRows = 6,
		minColorRows = 5,
		presetColumns = 6,
		deleteBuffer = 10,
		warningBuffer = 15,
		sliderHeight = 180,
		dragObj = {axis: "x", containment: "#rev-cpicker-point-wrap"};
		
	var lang = {
		
		'color': 'Color',
		'solid_color': 'Solid Color',
		'gradient_color': 'Gradient Color',
		'currently_editing': 'Currently Editing',
		'core_presets': 'Core Presets',
		'custom_presets': 'Custom Presets',
		'enter_a_name': 'Enter a Name',
		'save_a_new_preset': 'Save a new Preset',
		'save': 'Save',
		'color_hex_value': 'Color Hex Value',
		'opacity': 'Opacity',
		'clear': 'Clear',
		'location': 'Location',
		'delete': 'Delete',
		'horizontal': 'Horizontal',
		'vertical': 'Vertical',
		'radial': 'Radial',
		'enter_angle': 'Enter Angle',
		'reverse_gradient': 'Reverse Gradient',
		'delete_confirm': 'Remove/Delete this custom preset color?',
		'naming_error': 'Please enter a unique name for the new color preset'
		
	};
	
	var defColors = [
	
		'#FFFFFF', 
		'#000000', 
		'#FF3A2D', 
		'#007AFF', 
		'#4CD964', 
		'#FFCC00', 
		'#C7C7CC', 
		'#8E8E93', 
		'#FFD3E0', 
		'#34AADC', 
		'#E0F8D8', 
		'#FF9500'
		
	];
	
	var defGradients = [
				
		{'0': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:247,&g&:247,&b&:247,&a&:&1&,&position&:0,&align&:&top&},{&r&:247,&g&:247,&b&:247,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:215,&g&:215,&b&:215,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:215,&g&:215,&b&:215,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'1': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:74,&g&:74,&b&:74,&a&:&1&,&position&:0,&align&:&top&},{&r&:74,&g&:74,&b&:74,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:43,&g&:43,&b&:43,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:43,&g&:43,&b&:43,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'2': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:219,&g&:221,&b&:222,&a&:&1&,&position&:0,&align&:&top&},{&r&:219,&g&:221,&b&:222,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:137,&g&:140,&b&:144,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:137,&g&:140,&b&:144,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'3': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:26,&g&:214,&b&:253,&a&:&1&,&position&:0,&align&:&top&},{&r&:26,&g&:214,&b&:253,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:29,&g&:98,&b&:240,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:29,&g&:98,&b&:240,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'4': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:198,&g&:68,&b&:252,&a&:&1&,&position&:0,&align&:&top&},{&r&:198,&g&:68,&b&:252,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:88,&g&:86,&b&:214,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:88,&g&:86,&b&:214,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'5': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:255,&g&:94,&b&:58,&a&:&1&,&position&:0,&align&:&top&},{&r&:255,&g&:94,&b&:58,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:255,&g&:42,&b&:104,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:42,&b&:104,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'6': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:228,&g&:221,&b&:202,&a&:&1&,&position&:0,&align&:&top&},{&r&:228,&g&:221,&b&:202,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:214,&g&:206,&b&:195,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:214,&g&:206,&b&:195,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'7': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:255,&g&:219,&b&:76,&a&:&1&,&position&:0,&align&:&top&},{&r&:255,&g&:219,&b&:76,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:255,&g&:205,&b&:2,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:205,&b&:2,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'8': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:255,&g&:149,&b&:0,&a&:&1&,&position&:0,&align&:&top&},{&r&:255,&g&:149,&b&:0,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:255,&g&:94,&b&:58,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:94,&b&:58,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'9': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:82,&g&:237,&b&:199,&a&:&1&,&position&:0,&align&:&top&},{&r&:82,&g&:237,&b&:199,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:90,&g&:200,&b&:251,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:90,&g&:200,&b&:251,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'10': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:228,&g&:183,&b&:240,&a&:&1&,&position&:0,&align&:&top&},{&r&:228,&g&:183,&b&:240,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:200,&g&:110,&b&:223,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:200,&g&:110,&b&:223,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'11': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:135,&g&:252,&b&:112,&a&:&1&,&position&:0,&align&:&top&},{&r&:135,&g&:252,&b&:112,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:11,&g&:211,&b&:24,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:11,&g&:211,&b&:24,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'12': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:61,&g&:78,&b&:129,&a&:&1&,&position&:0,&align&:&top&},{&r&:61,&g&:78,&b&:129,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:87,&g&:83,&b&:201,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:110,&g&:127,&b&:243,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:110,&g&:127,&b&:243,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'13': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:35,&g&:21,&b&:87,&a&:&1&,&position&:0,&align&:&top&},{&r&:35,&g&:21,&b&:87,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:68,&g&:16,&b&:122,&a&:&1&,&position&:29,&align&:&bottom&},{&r&:255,&g&:19,&b&:97,&a&:&1&,&position&:67,&align&:&bottom&},{&r&:255,&g&:248,&b&:0,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:248,&b&:0,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'14': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:105,&g&:234,&b&:203,&a&:&1&,&position&:0,&align&:&top&},{&r&:105,&g&:234,&b&:203,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:234,&g&:204,&b&:248,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:102,&g&:84,&b&:241,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:102,&g&:84,&b&:241,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'15': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:255,&g&:5,&b&:124,&a&:&1&,&position&:0,&align&:&top&},{&r&:255,&g&:5,&b&:124,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:124,&g&:100,&b&:213,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:76,&g&:195,&b&:255,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:76,&g&:195,&b&:255,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'16': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:255,&g&:5,&b&:124,&a&:&1&,&position&:0,&align&:&top&},{&r&:255,&g&:5,&b&:124,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:141,&g&:11,&b&:147,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:50,&g&:21,&b&:117,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:50,&g&:21,&b&:117,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'17': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:164,&g&:69,&b&:178,&a&:&1&,&position&:0,&align&:&top&},{&r&:164,&g&:69,&b&:178,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:212,&g&:24,&b&:114,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:255,&g&:0,&b&:102,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:0,&b&:102,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'18': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:158,&g&:251,&b&:211,&a&:&1&,&position&:0,&align&:&top&},{&r&:158,&g&:251,&b&:211,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:87,&g&:233,&b&:242,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:69,&g&:212,&b&:251,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:69,&g&:212,&b&:251,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'19': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:172,&g&:50,&b&:228,&a&:&1&,&position&:0,&align&:&top&},{&r&:172,&g&:50,&b&:228,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:121,&g&:24,&b&:242,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:72,&g&:1,&b&:255,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:72,&g&:1,&b&:255,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'20': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:112,&g&:133,&b&:182,&a&:&1&,&position&:0,&align&:&top&},{&r&:112,&g&:133,&b&:182,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:135,&g&:167,&b&:217,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:222,&g&:243,&b&:248,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:222,&g&:243,&b&:248,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'21': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:34,&g&:225,&b&:255,&a&:&1&,&position&:0,&align&:&top&},{&r&:34,&g&:225,&b&:255,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:29,&g&:143,&b&:225,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:98,&g&:94,&b&:177,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:98,&g&:94,&b&:177,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'22': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:44,&g&:216,&b&:213,&a&:&1&,&position&:0,&align&:&top&},{&r&:44,&g&:216,&b&:213,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:107,&g&:141,&b&:214,&a&:&1&,&position&:50,&align&:&bottom&},{&r&:142,&g&:55,&b&:215,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:142,&g&:55,&b&:215,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'23': '{&type&:&linear&,&angle&:&160&,&colors&:[{&r&:44,&g&:216,&b&:213,&a&:&1&,&position&:0,&align&:&top&},{&r&:44,&g&:216,&b&:213,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:197,&g&:193,&b&:255,&a&:&1&,&position&:56,&align&:&bottom&},{&r&:255,&g&:186,&b&:195,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:186,&b&:195,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'24': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:191,&g&:217,&b&:254,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:191,&g&:217,&b&:254,&a&:&1&,&position&:0,&align&:&top&},{&r&:223,&g&:137,&b&:181,&a&:&1&,&position&:100,&align&:&top&},{&r&:223,&g&:137,&b&:181,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'25': '{&type&:&linear&,&angle&:&340&,&colors&:[{&r&:97,&g&:97,&b&:97,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:97,&g&:97,&b&:97,&a&:&1&,&position&:0,&align&:&top&},{&r&:155,&g&:197,&b&:195,&a&:&1&,&position&:100,&align&:&top&},{&r&:155,&g&:197,&b&:195,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'26': '{&type&:&linear&,&angle&:&90&,&colors&:[{&r&:36,&g&:57,&b&:73,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:36,&g&:57,&b&:73,&a&:&1&,&position&:0,&align&:&top&},{&r&:81,&g&:127,&b&:164,&a&:&1&,&position&:100,&align&:&top&},{&r&:81,&g&:127,&b&:164,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'27': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:234,&g&:205,&b&:163,&a&:&1&,&position&:0,&align&:&top&},{&r&:234,&g&:205,&b&:163,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:230,&g&:185,&b&:128,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:230,&g&:185,&b&:128,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'28': '{&type&:&linear&,&angle&:&45&,&colors&:[{&r&:238,&g&:156,&b&:167,&a&:&1&,&position&:0,&align&:&top&},{&r&:238,&g&:156,&b&:167,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:255,&g&:221,&b&:225,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:255,&g&:221,&b&:225,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'29': '{&type&:&linear&,&angle&:&340&,&colors&:[{&r&:247,&g&:148,&b&:164,&a&:&1&,&position&:0,&align&:&top&},{&r&:247,&g&:148,&b&:164,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:253,&g&:214,&b&:189,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:253,&g&:214,&b&:189,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'30': '{&type&:&linear&,&angle&:&45&,&colors&:[{&r&:135,&g&:77,&b&:162,&a&:&1&,&position&:0,&align&:&top&},{&r&:135,&g&:77,&b&:162,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:196,&g&:58,&b&:48,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:196,&g&:58,&b&:48,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'31': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:243,&g&:231,&b&:233,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:243,&g&:231,&b&:233,&a&:&1&,&position&:0,&align&:&top&},{&r&:218,&g&:212,&b&:236,&a&:&1&,&position&:100,&align&:&top&},{&r&:218,&g&:212,&b&:236,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'32': '{&type&:&linear&,&angle&:&320&,&colors&:[{&r&:43,&g&:88,&b&:118,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:43,&g&:88,&b&:118,&a&:&1&,&position&:0,&align&:&top&},{&r&:78,&g&:67,&b&:118,&a&:&1&,&position&:100,&align&:&top&},{&r&:78,&g&:67,&b&:118,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'33': '{&type&:&linear&,&angle&:&60&,&colors&:[{&r&:41,&g&:50,&b&:60,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:41,&g&:50,&b&:60,&a&:&1&,&position&:0,&align&:&top&},{&r&:72,&g&:85,&b&:99,&a&:&1&,&position&:100,&align&:&top&},{&r&:72,&g&:85,&b&:99,&a&:&1&,&position&:100,&align&:&bottom&}]}'},
		{'34': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:233,&g&:233,&b&:231,&a&:&1&,&position&:0,&align&:&top&},{&r&:233,&g&:233,&b&:231,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:239,&g&:238,&b&:236,&a&:&1&,&position&:25,&align&:&bottom&},{&r&:238,&g&:238,&b&:238,&a&:&1&,&position&:70,&align&:&bottom&},{&r&:213,&g&:212,&b&:208,&a&:&1&,&position&:100,&align&:&bottom&},{&r&:213,&g&:212,&b&:208,&a&:&1&,&position&:100,&align&:&top&}]}'},
		{'35': '{&type&:&linear&,&angle&:&180&,&colors&:[{&r&:251,&g&:200,&b&:212,&a&:&1&,&position&:0,&align&:&bottom&},{&r&:251,&g&:200,&b&:212,&a&:&1&,&position&:0,&align&:&top&},{&r&:151,&g&:149,&b&:240,&a&:&1&,&position&:100,&align&:&top&},{&r&:151,&g&:149,&b&:240,&a&:&1&,&position&:100,&align&:&bottom&}]}'}
	
	];
	
	var markup = 
	
	'<div id="rev-cpicker-back" class="rev-cpicker-close"></div>' + 
	
	'<div id="rev-cpicker">' + 
		
		'<div id="rev-cpicker-head">' + 
			
			'<div id="rev-cpicker-drag" class="rev-cpicker-draggable"></div>' + 
			'<span id="rev-cpicker-color-btn" class="rev-cpicker-main-btn" data-text="solid_color"></span>' + 
			'<span id="rev-cpicker-gradient-btn" class="rev-cpicker-main-btn" data-text="gradient_color"></span>' + 
			
			'<div id="rev-cpicker-editing" class="rev-cpicker-draggable">' + 
				'<span id="rev-cpicker-edit-title" data-text="currently_editing"></span>' + 
				'<span id="rev-cpicker-current-edit"></span>' + 
			'</div>' + 
			
			'<span id="rev-cpicker-exit" class="rev-cpicker-close"></span>' + 
		
		'</div>' + 
		
		'<div id="rev-cpicker-section-left" class="rev-cpicker-section">' + 
			
			'<div id="rev-cpicker-body">' + 
			
				'<div id="rev-cpicker-colors" class="rev-cpicker-type">' + 
					
					'<div class="rev-cpicker-column rev-cpicker-column-left">	' + 
						
						'<div class="rev-cpicker-column-inner-left">' + 
						
							'<div class="rev-cpicker-presets">' + 
							
								'<span id="rev-cpicker-colors-core-btn" class="rev-cpicker-preset-title rev-cpicker-preset-title-core selected">' + 
									'<span data-text="core_presets"></span> ' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-down"></span>' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-up"></span>' + 
								'</span>' + 
								
								'<span id="rev-cpicker-colors-custom-btn" class="rev-cpicker-preset-title rev-cpicker-preset-title-custom">' + 
									'<span data-text="custom_presets"></span> ' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-down"></span>' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-up"></span>' + 
								'</span>' + 
								
								'<div id="rev-cpicker-colors-core" class="rev-cpicker-presets-group"></div>' + 
								'<div id="rev-cpicker-colors-custom" class="rev-cpicker-presets-group rev-cpicker-presets-custom"></div>' + 
							
							'</div>' + 
							
							'<div class="rev-cpicker-iris">' + 
							
								'<input id="rev-cpicker-iris-color" class="rev-cpicker-iris-input" value="#ffffff" />' + 
								
								'<div id="rev-cpicker-scroller" class="iris-slider iris-strip">' +
									'<div id="rev-cpicker-scroll-bg"></div>' + 
									'<div id="rev-cpicker-scroll" class="iris-slider-offset"></div>' +
								'</div>' +
							
							'</div>' + 
							
						'</div>' + 
					
					'</div>' + 
					
					'<div class="rev-cpicker-column rev-cpicker-column-right">' + 
						
						'<div class="rev-cpicker-column-inner-right">' + 
						
							'<div>' + 
							
								'<span data-text="save_a_new_preset"></span>' + 
								
								'<div class="rev-cpicker-presets-save-as">' + 
								
									'<input type="text" class="rev-cpicker-preset-save" placeholder="" data-placeholder="enter_a_name" />' + 
									'<span class="rev-cpicker-btn rev-cpicker-save-preset-btn" data-alert="naming_error">' + 
										'<span class="rev-cpicker-save-icon"></span>' + 
										'<span class="rev-cpicker-preset-save-text" data-text="save"></span>' + 
									'</span>' + 
								
								'</div>' + 
							
							'</div>' + 
							
							'<div class="rev-cpicker-meta">' + 
							
								'<span data-text="color_hex_value"></span>' + 
								'<br>' + 
								'<input type="text" id="rev-cpicker-color-hex" class="rev-cpicker-input rev-cpicker-hex" value="#ffffff" />' + 
								'<br>' + 
								'<span data-text="opacity" class="rev-cpicker-hideable"></span>' +
								'<br>' + 
								'<input type="text" id="rev-cpicker-color-opacity" class="rev-cpicker-input rev-cpicker-opacity-input rev-cpicker-hideable" value="100%" />' + 
								'<span id="rev-cpciker-clear-hex" class="rev-cpicker-btn rev-cpicker-btn-small rev-cpciker-clear rev-cpicker-hideable" data-text="clear"></span>' + 
							
							'</div>' + 
							
						'</div>' + 
				
					'</div>' + 

				'</div>' + 
				
				'<div id="rev-cpicker-gradients" class="rev-cpicker-type">' + 
				
					'<div class="rev-cpicker-column rev-cpicker-column-left">	' + 
						
						'<div class="rev-cpicker-column-inner-left">' + 
						
							'<div class="rev-cpicker-presets">' + 
							
								'<span id="rev-cpicker-gradients-core-btn" class="rev-cpicker-preset-title rev-cpicker-preset-title-core selected">' + 
									'<span data-text="core_presets"></span> ' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-down"></span>' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-up"></span>' + 
								'</span>' + 
								
								'<span id="rev-cpicker-gradients-custom-btn" class="rev-cpicker-preset-title rev-cpicker-preset-title-custom">' + 
									'<span data-text="custom_presets"></span> ' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-down"></span>' + 
									'<span class="rev-cpicker-arrow rev-cpicker-arrow-up"></span>' + 
								'</span>' + 
								
								'<div id="rev-cpicker-gradients-core" class="rev-cpicker-presets-group"></div>' + 
								'<div id="rev-cpicker-gradients-custom" class="rev-cpicker-presets-group rev-cpicker-presets-custom"></div>' + 
							
							'</div>' + 
							
							'<div class="rev-cpicker-gradient-block">' + 
								
								'<div id="rev-cpicker-gradient-input" class="rev-cpicker-gradient-builder">' + 
									
									'<span id="rev-cpicker-hit-top" class="rev-cpicker-builder-hit"></span>' +  
									'<div id="rev-cpicker-point-wrap">' + 
										'<div id="rev-cpciker-point-container"></div>' + 
									'</div>' + 
									'<span id="rev-cpicker-hit-bottom" class="rev-cpicker-builder-hit"></span>' +  
								
								'</div>' + 
								
								'<div class="rev-cpicker-meta-row-wrap">' + 
								
									'<div class="rev-cpicker-meta-row">' + 
										
										'<div><label data-text="opacity"></label><input type="text" id="rev-cpicker-grad-opacity" class="rev-cpicker-point-input rev-cpicker-opacity-input" value="100%" disabled /></div>' + 
										'<div><label data-text="location"></label><input type="text" id="rev-cpicker-opacity-location" class="rev-cpicker-point-input rev-cpicker-point-location" value="100%" disabled /></div>' + 
										'<div><label>&nbsp;</label><span class="rev-cpicker-btn rev-cpicker-btn-small rev-cpicker-point-delete" id="rev-cpicker-opacity-delete" data-text="delete">{{delete}}</span></div>' + 
									
									'</div>' + 
									
									'<div class="rev-cpicker-meta-row">' + 
										
										'<div><label data-text="color"></label><span class="rev-cpicker-point-input" id="rev-cpicker-color-box"></span></div>' + 
										'<div><label data-text="location"></label><input type="text" id="rev-cpicker-color-location" class="rev-cpicker-point-input rev-cpicker-point-location" value="100%" disabled /></div>' + 
										'<div><label>&nbsp;</label><span class="rev-cpicker-btn rev-cpicker-btn-small rev-cpicker-point-input rev-cpicker-point-delete" id="rev-cpicker-color-delete" data-text="delete">{{delete}}</span></div>' + 
									
									'</div>' + 
									
								'</div>' + 
								
							'</div>' + 
							
						'</div>' + 
					
					'</div>' + 
					
					'<div class="rev-cpicker-column rev-cpicker-column-right">' + 
						
						'<div class="rev-cpicker-column-inner-right">' + 
						
							'<div>' + 
							
								'<span data-text="save_a_new_preset"></span>' + 
								
								'<div class="rev-cpicker-presets-save-as">' + 
								
									'<input type="text" class="rev-cpicker-preset-save" placeholder="" data-placeholder="enter_a_name" />' + 
									'<span class="rev-cpicker-btn rev-cpicker-save-preset-btn" data-alert="naming_error">' + 
										'<span class="rev-cpicker-save-icon"></span>' + 
										'<span class="rev-cpicker-preset-save-text" data-text="save"></span>' + 
									'</span>' + 
								
								'</div>' + 
							
							'</div>' + 
							
							'<div class="rev-cpicker-gradient-block">' + 
								
								'<div id="rev-cpicker-gradient-output" class="rev-cpicker-gradient-builder"></div>' + 
								
							'</div>' + 
							
							'<div class="rev-cpicker-meta-row-wrap">' + 
								
									'<div class="rev-cpicker-meta-row">' + 
										
										'<div><label>Orientation</label>' + 
										
											'<span id="rev-cpicker-orientation-horizontal" class="rev-cpicker-btn rev-cpicker-btn-small rev-cpicker-orientation" data-direction="90" data-text="horizontal"></span>' + 
											'<span id="rev-cpicker-orientation-vertical" class="rev-cpicker-btn rev-cpicker-btn-small rev-cpicker-orientation" data-direction="180" data-text="vertical"></span>' + 
											'<span id="rev-cpicker-orientation-radial" class="rev-cpicker-btn rev-cpicker-btn-small rev-cpicker-orientation" data-direction="radial" data-text="radial"></span>' + 
											
										'</div>' + 
									
									'</div>' + 
									
									'<div class="rev-cpicker-meta-row rev-cpicker-meta-row-push">' + 
										
										'<div>' + 
											'<label data-text="enter_angle"></label>' + 
											'<div id="rev-cpicker-angle-container">' + 
												'<input type="text" class="rev-cpicker-input" id="rev-cpicker-meta-angle" value="" />' + 
												'<div id="rev-cpicker-wheel">' + 
													'<div id="rev-cpicker-wheel-inner"><span id="rev-cpicker-wheel-point"></span></div>' + 
												'</div>' + 
											'</div>' + 
										'</div>' + 
										'<div><label data-text="reverse_gradient"></label><span id="rev-cpicker-meta-reverse"></span></div>' + 
									
									'</div>' + 
									
								'</div>' + 
							
						'</div>' + 
				
					'</div>' + 
				
				'</div>' + 
			
			'</div>' + 
		
		'</div>' + 
		
		'<span id="rev-cpicker-check"></span>' + 
		
		'<div id="rev-cpicker-section-right" class="rev-cpicker-section">' + 
		
			'<div class="rev-cpicker-iris">' + 
							
				'<input id="rev-cpicker-iris-gradient" class="rev-cpicker-iris-input" value="#ffffff" />' + 
			
			'</div>' + 
			
			'<div class="rev-cpicker-fields">' + 
			
				'<input type="text" id="rev-cpicker-gradient-hex" class="rev-cpicker-input rev-cpicker-hex" value="#ffffff" />' + 
				'<span id="rev-cpciker-clear-gradient" class="rev-cpicker-btn rev-cpicker-btn-small rev-cpciker-clear" data-text="clear"></span>' + 
				'<span id="rev-cpicker-check-gradient" class="rev-cpicker-btn"></span>' + 
				
			'</div>' + 
		
		'</div>' + 
		
		'<span id="rev-cpicker-remove-delete" data-text="delete_confirm"></span>' + 

	'</div>';
	
	function changeDegree(grad, deg) {
		
		grad = grad.split('(');
		
		var begin = grad[0];
		grad.shift();
		
		var end = grad.join('(').split(',');	
		end.shift();
		
		deg = typeof deg !== 'undefined' ? deg + 'deg,' : 'ellipse at center,';
		return begin + '(' + deg + end.join(',');
		
	}
	
	function gradientView(val) {

		return changeDegree(val.replace('radial-', 'linear-'), '90');
		
	}
	
	function replaceText() {
		
		this.innerHTML = curLanguage[getAttribute(this, 'data-text')];
		
	}
	
	function replaceHolder() {
		
		this.setAttribute('placeholder', curLanguage[getAttribute(this, 'data-placeholder')]);
		
	}
	
	function replaceAlert() {
		
		this.setAttribute('data-message', curLanguage[getAttribute(this, 'data-alert')]);
		
	}
	
	function getAttribute(el, attr) {
		
		return el.getAttribute(attr) || '';
		
	}
	
	function writeLanguage(language) {
		
		if(!language) language = {};
		if(typeof language === 'string') language = JSON.parse(language.replace(/\&/g, '"'));
		
		curLanguage = $.extend({}, lang, language);
		langColor = curLanguage.color;
		
		cPicker.find('*[data-placeholder]').each(replaceHolder);
		cPicker.find('*[data-alert]').each(replaceAlert);
		cPicker.find('*[data-text]').each(replaceText);
			
	}
	
	function newPreset(val, core, cls, grad) {
		
		var titl,
			obj,
			el;
		
		if(!$.isPlainObject(val)) {
			
			titl = val;
			
		}
		else {
			
			var angl,
				tpe;
			
			for(var prop in val) {
				
				if(!val.hasOwnProperty(prop)) continue;
				
				val = val[prop];
				if(typeof val === 'string') {
					
					val = RevColor.process(val);
					if(val[1] === 'gradient') {
					
						obj = val[2];
						angl = obj.angle;
						tpe = obj.type;
						
					}
					
					val = val[0];
					
				}
				else {
					
					angl = val.angle;
					tpe = val.type;
					
				}
				
				if(!isNaN(prop)) {
					
					titl = tpe !== 'radial' ? angl + '&deg;' : 'radial';
				
				}				
				else {
					
					titl = prop.replace(/_/g, ' ').replace(/\b\w/g, function(chr) {return chr.toUpperCase();});
					
				}
				
			}
			
		}
		
		if(val !== 'blank') {
			
			var datas;
			if($.isPlainObject(val)) {
				
				obj = val;
				datas = '';
				val = grad || RevColor.processGradient(val); 
			
			}
			
			var shell = '<span class="rev-cpicker-color tptip' + cls + '" data-title="' + titl + '" data-color="' + val + '">' + 
					    '<span class="rev-cpicker-preset-tile"></span>' + 
					    '<span class="rev-cpicker-preset-bg" style="background: ' + val + '"></span>';
			
			if(!core) shell += '<span class="rev-cpicker-delete"><span class="rev-cpicker-delete-icon"></span></span>';
			shell += '</span>';
			
			el = $(shell);
			if(obj) el.data('gradient', obj);
			return el[0];
				   
		}
		else {
			
			el = document.createElement('span');
			el.className = 'rev-cpicker-color blank';
			return el;
			
		}
		
	}
	
	function checkPreset() {
		
		var presetColor = getAttribute(this, 'data-color').toLowerCase(),
		toCheck = !supressCheck ? presetColor === openingColor.toLowerCase() : false;
		
		if(presetColor === selectedHex || toCheck) {
			
			var $this = $(this);
			
			$this.closest('.rev-cpicker-presets-group').find('.rev-cpicker-color.selected').removeClass('selected');
			selectedColor = $this;
			
			if(supressCheck && !colorView) setValue(selectedColor.data('gradient'), true);
			selectedColor.addClass('selected');

			return false;
			
		}
		
	}
	
	function writePresets(container, colors) {
		
		var frag = document.createDocumentFragment(),
			core = container.search('core') !== -1,
			cls = core ? '' : ' rev-picker-color-custom',
			len = colors.length,
			minRows = container.search('colors') !== -1 ? minColorRows : minGradRows,
			rows = Math.max(Math.ceil(len / presetColumns), minRows);
		
		for(var i = 0; i < rows; i++) {
		
			while(colors.length < ((i + 1) * presetColumns)) colors[colors.length] = 'blank';
		
		}
		
		len = colors.length;
		for(i = 0; i < len; i++) {

			frag.appendChild(newPreset(colors[i], core, cls));
			
		}
		
		return ['rev-cpicker-' + container, frag];
		
	}
	
	function onChange(gradient, color, trans) {
		
		if(!currentColor) return;
		if(!gradient) {
				
			var hex = color || colorHex.val(),
				opacity = typeof trans !== 'undefined' ? trans : colorOpacity.val();
			
			if(hex === 'transparent') color = 'transparent';
			else if(opacity === '100%') color = RevColor.sanitizeHex(hex);
			else color = RevColor.processRgba(hex, opacity);
			
		}
		
		var isTrans = color === 'transparent',
			val = !isTrans ? color : '';
		
		if(!gradient) colorBtn.data('state', color);
		else gradBtn.data('state', color);
		
		if(!isTrans) currentColor[0].style.background = val;
		else currentColor.css('background', val);
		
		if(onEdit) onEdit(currentInput, color);
		doc.trigger('revcolorpickerupdate', [currentInput, color]);
		
	}
	
	function setValue(val, fromPreset) {
		
		var obj = RevColor.process(val),
			type = obj[1],
			clr = obj[0];
		
		if(isFull) reverse.removeClass('checked');
		if(type !== 'gradient') {
			
			switch(type) {
				
				case 'hex':
					
					val = RevColor.sanitizeHex(clr);
					colorOpacity.val('100%');
					updateSlider(100);
				
				break;
				
				case 'rgba': 
					
					var values = RevColor.rgbaToHex(clr),
					opacity = parseInt(values[1] * 100, 10);
					val = values[0];
	
					colorOpacity.val(opacity + '%');
					updateSlider(opacity);
				
				break;
				
				case 'rgb':
					
					val = RevColor.rgbToHex(clr);
					colorOpacity.val('100%');
					updateSlider(100);
				
				break;
				
				default:
				
					colorClear.click();
					colorBtn.click();
				
				// end default
				
			}
			
			colorIris.val(val).change();
			if(!fromPreset) colorBtn.click();
			
		}
		else {
			
			if(isFull) {
				
				buildGradientInput(obj[2]);
				buildGradientOutput();
				
				if(!fromPreset) {
						
					gradViewed = true;
					gradBtn.click();
					
				}
					
			}
			else {
				
				colorIris.val(RevColor.defaultValue).change();
				colorBtn.click();
				
			}
			
		}
		
		return [clr, type];
		
	}
	
	function getSibling(align, x) {
		
		var locations = points.slice(),
			len = locations.length,
			pnt;
			
		locations.sort(sortPoints);
		while(len--) {
			
			pnt = locations[len];
			if(pnt.align === align && pnt.x < x) return pnt;
			
		}
		
		len = locations.length;
		for(var i = 0; i < len; i++) {
			
			pnt = locations[i];
			if(pnt.align === align && pnt.x > x) return pnt;
			
		}
		
	}
	
	function clonePoint(align, pos) {
		
		var sibling = getSibling(align, pos),
			clr = sibling.color,
			bg = getBgVal(clr, align, true),
			rgb = getRgbVal(clr, true);
		
		var pnt = newPoint(align, pos, rgb, bg);
		if(curPoint) curPoint.removeClass('active');
		
		curPoint = $(pnt).addClass('active').appendTo(pointerWrap).draggable(dragObj);
		curSquare = curPoint.children('.rev-cpicker-point-square')[0];
		curCorner = curPoint.children('.rev-cpicker-point-triangle')[0];
		groupPoints = pointerWrap.children();
		
		var grad = activatePoint(pos);
		buildGradientOutput(pnt);
		
		if(align === 'bottom') gradIris.val(grad[1]).change();
		
	}
	
	function activatePoint(pointX) {
		
		if(typeof pointX === 'undefined') pointX = points[pointIndex].x;
		
		var color = curPoint.attr('data-color'),
			bottom = curPoint.hasClass('rev-cpicker-point-bottom');
		
		if(bottom) {
				
			if(opacityDelete.hasClass('active')) {
				
				gradOpacity.attr('disabled', 'disabled');
				opacityLoc.attr('disabled', 'disabled');
				opacityDelete.removeClass('active');
			
			}
			
			color = RevColor.rgbaToHex(color)[0];
			colorBox.css('background', color);
			colorLoc.removeAttr('disabled').val(pointX + '%');
			
			if(cPicker.find('.rev-cpicker-point-bottom').length > 2) {	
				colorDelete.addClass('active');
			}
			
			cPicker.addClass('open');
			
		}
		else {
			
			if(colorDelete.hasClass('active')) {
			
				colorBox.css('background', '');
				colorLoc.attr('disabled', 'disabled');
				colorDelete.removeClass('active');
				
			}
			
			var opacity = RevColor.getOpacity(color);
			gradOpacity.attr('data-opacity', opacity).val(opacity).removeAttr('disabled');
			opacityLoc.val(pointX + '%').removeAttr('disabled');
			
			if(cPicker.find('.rev-cpicker-point-top').length > 2) {	
				opacityDelete.addClass('active');
			}
			
			cPicker.removeClass('open');
			
		}
		
		return [bottom, color];
		
	}
	
	function getBgVal(clr, align, cloned) {
		
		if(align === 'bottom') return 'rgb(' + clr.r + ',' + clr.g + ',' + clr.b + ')';
		var opacity = !cloned ? clr.a : '1';
		return 'rgba(0, 0, 0, ' + opacity + ')';
		
	}
	
	function getRgbVal(clr, cloned) {
		
		var opacity = !cloned ? clr.a : '1';
		return 'rgba(' + clr.r + ',' + clr.g + ',' + clr.b + ',' + opacity + ')';
		
	}
	
	function setPoints(colors) {
		
		var frag = document.createDocumentFragment(),
			len = colors.length,
			align,
			clr;	
			
		for(var i = 0; i < len; i++) {
			
			clr = colors[i];
			align = clr.align;
			frag.appendChild(
			
				newPoint(
				
					align, 
					clr.position, 
					getRgbVal(clr), 
					getBgVal(clr, align)
					
				)
				
			);
			
		}
		
		if(groupPoints) groupPoints.draggable('destroy');
		pointerWrap.empty().append(frag);
		groupPoints = pointerWrap.children().draggable(dragObj);
		
	}
	
	function newPoint(align, pos, val, bg) {
		
		var el = document.createElement('span');
		el.className = 'rev-cpicker-point rev-cpicker-point-' + align;
		
		if(typeof val === 'string') el.setAttribute('data-color', val);
		else $(el).data('gradient', val);
		
		el.setAttribute('data-location', pos);
		el.style.left = pos + '%';
		
		if(align === 'bottom') {
			
			el.innerHTML = 
			'<span class="rev-cpicker-point-triangle" style="border-bottom-color: ' + bg + '"></span>' + 
			'<span class="rev-cpicker-point-square" style="background: ' + bg  +'"></span>';
			
		}
		else {
		
			el.innerHTML = 
			'<span class="rev-cpicker-point-square" style="background: ' + bg + '"></span>' + 
			'<span class="rev-cpicker-point-triangle" style="border-top-color: ' + bg + '"></span>';
			
		}
		
		return el;
		
	}
	
	function getDegree(val) {
		
		if(!val || val === 'radial') val = '0';
		textareas.innerHTML = val + '&deg;';
		return textareas.value;
		
	}
	
	function deactivate() {
		
		if(curPoint) {
						
			curPoint.removeClass('active');
			curPoint = false;
			
		}
		
		colorLoc.attr('disabled', 'disabled');
		gradOpacity.attr('disabled', 'disabled');
		opacityLoc.attr('disabled', 'disabled');
		
		opacityDelete.removeClass('active');
		colorDelete.removeClass('active');
		
		colorBox.css('background', '');
		cPicker.removeClass('open');
		
	}
	
	function onClose(e, changed) {
		
		cPicker.removeClass('active is-basic').hide();
		bodies.removeClass('rev-colorpicker-open');
		mainContainer.css({left: '', top: ''});
		
		if(appended) {
			
			appended.remove();
			appended = false;
			
		}
		
		if(selectedColor) {
			
			if(selectedColor.hasClass('selected')) {
				
				if(changed) currentInput.data('hex', selectedColor.attr('data-color').toLowerCase());	
				selectedColor.removeClass('selected');
				
			}
			else {
				
				currentInput.removeData('hex');
				
			}
			
			selectedColor = false;
			
		}
		else {
			
			currentInput.removeData('hex');
			
		}
		
		if(!changed) {
			
			if(onCancel) onCancel();
			if(openingValue && openingValue !== 'transparent') {	
			
				currentColor[0].style.background = openingValue;
				
			}
			else {
				
				currentColor.css('background', '');
				
			}
			
			doc.trigger('revcolorpickerupdate', [currentInput, openingValue]);
			
		}
		
		currentColor = false;
		currentInput = false;
		
	}
	
	function checkGroup() {
		
		var $this = $(this),
			len = $this.children('.rev-cpicker-color').not('.blank').length;
			
		if(len > presetColumns) $('#' + this.id + '-btn').addClass('multiplerows');
		else $('#' + this.id + '-btn').removeClass('multiplerows');
		
		return len;
		
	}
	
	function checkRows() {
		
		var $this = $(this),
			minRows = this.id.search('colors') !== -1 ? minColorRows : minGradRows,
			colors = $this.children('.rev-cpicker-color'),
			len = colors.length,
			rows = Math.ceil(len / presetColumns),
			minItems = minRows * presetColumns,
			rowRemoved;	
		
		len += 1;	
		for(var i = 0; i < rows; i++) {
			
			var start = i * presetColumns,
				range = colors.slice(start, parseInt(start + presetColumns, 10) - 1);
			
			blankRow = true;
			range.each(checkRow);
			if(blankRow) {
				
				len -= presetColumns;
				if(len >= minItems) {
					
					range.remove();
					rowRemoved = true;
					
				}
				
			}
			
		}
		
		return rowRemoved;
		
	}
	
	function checkRow() {
				
		if(this.className.search('blank') === -1) {
			
			blankRow = false;
			return false;
			
		}
		
	}

	function buildGradientInput(obj) {
		
		var degree = obj.angle;
		if(obj.type === 'radial') degree = 'radial';
		
		directions.removeClass('selected');
		$('.rev-cpicker-orientation[data-direction="' + degree + '"]').addClass('selected');
		
		angle.val(getDegree(degree));
		updateWheel(degree);
		setPoints(obj.colors);
		
	}
	
	function buildGradientOutput(el, revrse, save) {
		
		onReverse = revrse;
		calculatePoints();
		onReverse = false;
		
		var elm,
			color,
			point,
			ar = [],
			len = points.length;
		
		for(var i = 0; i < len; i++) {
			
			point = points[i];
			color = point.color;
			ar[i] = color;
			
			elm = point.el;
			elm.setAttribute('data-color', RevColor.rgbaString(color.r, color.g, color.b, color.a));
			elm.setAttribute('data-opacity', color.a * 100);
			
			if(el && el === elm) pointIndex = i;
			
		}
		
		if(!radial.hasClass('selected')) {
			
			gradientPreview.type = 'linear';
			gradientPreview.angle = parseInt(angle.val(), 10).toString();
			
		}
		else {
			
			gradientPreview.type = 'radial';
			gradientPreview.angle = '0';
			
		}
		
		gradientPreview.colors = ar;
		if(selectedColor) selectedColor.removeClass('selected');
		
		var gradient = RevColor.processGradient(gradientPreview);
		onChange(true, gradient);
		
		if(!save) {
			
			gradInput.style.background = gradientView(gradient);
			gradOutput.style.background = gradient;
			
		}
		else {
		
			return [gradientPreview, gradient];
			
		}
		
	}
	
	function back(i, align) {
		
		if(i === 0) return false;
		
		var pnt;
		while(i--) {
				
			pnt = points[i];
			if(pnt.align !== align) return pnt;
			
		}
		
		return false;
		
	}
	
	function front(i, align, len) {
		
		if(i === len) return false;
		
		var pnt;
		while(i++ < len) {
				
			pnt = points[i];
			if(pnt.align !== align) return pnt;
			
		}
		
		return false;
		
	}
	
	function dist(px, bx, ex, bv, ev) {
		
		return Math.max(Math.min(Math.round(Math.abs((((px - bx) / (ex - bx)) * (ev - bv)) + bv)), 255), 0);
		
	}
	
	function distAlpha(px, bx, ex, bv, ev) {
		
		return Math.max(Math.min(Math.abs(parseFloat((((px - bx) / (ex - bx)) * (ev - bv)).toFixed(2)) + parseFloat(bv)), 1), 0);
		
	}
	
	function adjustAlpha(point, begin, end) {
		
		var val,
			beginAlpha = begin.alpha,
			endAlpha = end.alpha;
			
		if(beginAlpha !== endAlpha) {
			
			val = distAlpha(point.x, begin.x, end.x, beginAlpha, endAlpha).toFixed(2);
			
		}
		else {
			
			val = beginAlpha;
			
		}
		
		point.alpha = val;
		point.color.a = val;
		
	}
	
	function adjustColor(point, begin, end) {
		
		var p = point.color,
			bv = begin.color, 
			ev = end.color;
		
		if(begin !== end) {
					
			var px = point.x,
				bx = begin.x, 
				ex = end.x;
			
			p.r = dist(px, bx, ex, bv.r, ev.r);
			p.g = dist(px, bx, ex, bv.g, ev.g);
			p.b = dist(px, bx, ex, bv.b, ev.b);
			
		}
		else {
			
			p.r = bv.r;
			p.g = bv.g;
			p.b = bv.b;
			
		}
		
	}
	
	function calculatePoints() {
		
		points = [];
		topPoints = [];
		botPoints = [];
		
		groupPoints.each(getPoints);
		points.sort(sortPoints);
		
		var len = points.length,
			iLen = len - 1,
			point,
			begin,
			tpe,
			end;
		
		for(var i = 0; i < len; i++) {
			
			point = points[i];
			tpe = point.align;
			
			begin = back(i, tpe);
			if(begin === false) begin = front(i, tpe, iLen);
			
			end = front(i, tpe, iLen);
			if(end === false) end = back(i, tpe);
			
			if(tpe === 'bottom') adjustAlpha(point, begin, end);
			else adjustColor(point, begin, end);
			
		}
		
		points.sort(sortPoints);
		
	}
	
	function sortPoints(a, b) {
		
		return a.x < b.x ? -1 : a.x > b.x ? 1 : 0;
		
	}
	
	function getPoints(i) {
		
		var rgba = RevColor.rgbValues(getAttribute(this, 'data-color'), 4),
			align = this.className.search('bottom') !== -1 ? 'bottom' : 'top',
			alpha = rgba[3].replace(/\.?0*$/, '') || 0,
			pos = parseInt(this.style.left, 10);
		
		if(onReverse) {
	
			if(pos < 50) pos += (50 - pos) * 2;
			else pos -= (pos - 50) * 2;		
			
			this.style.left = pos + '%';
			this.setAttribute('data-location', pos);
			
		}
		
		points[i] = {
			
			el: this, 
			x: pos,
			alpha: alpha,
			align: align,
			color: {
				
				r: parseInt(rgba[0], 10),
				g: parseInt(rgba[1], 10),
				b: parseInt(rgba[2], 10),
				a: alpha,
				position: pos,
				align: align
				
			}
			
		};
		
		if(curPoint && curPoint[0] !== this) {
			
			if(align === 'bottom') botPoints[botPoints.length] = pos;
			else topPoints[topPoints.length] = pos;
			
		}
		
	}
	
	function updateWheel(val) {
		
		val = typeof val !== 'undefined' ? val : parseInt(angle.val(), 10);
		wheelPoint[0].style.transform = 'rotate(' + val + 'deg)';
		
	}
	
	function onAngleChange(e, dir, wheel) {
			
		var isWheel = typeof wheel !== 'undefined',
			val = isWheel ? wheel : parseInt(angle.val(), 10),
			changed,
			value;
		
		if(e && e.type === 'keyup') {
			
			changed = !isNaN(val) && val >= -360 && val <= 360;
			value = val;
			
		}
		else {
			
			var oValue = parseInt(angle.data('orig-value'), 10);
			
			if(!val) val = '0';
			if(isNaN(val) || val < -360 || val > 360) val = oValue;
			if(val !== oValue) {
				
				value = val;
				changed = true;
				angle.val(getDegree(val));
			
				if(!isWheel) {
				
					val = dir || val;
					directions.removeClass('selected');
					$('.rev-cpicker-orientation[data-direction="' + val + '"]').addClass('selected');
					
				}
				
			}
			
		}
		
		if(changed || dir) {
				
			if(value) updateWheel(value);
			buildGradientOutput();
			
		}
	
	}
	
	function onArrowClick() {
			
		var $this = $(this);
		
		if(this.className.search('down') !== -1) {
			
			$this.parent().addClass('active');
			$this.closest('.rev-cpicker-presets').addClass('active');
			$(this.id.replace('-btn', '')).addClass('active');
			gradientsOpen = cPicker.hasClass('gradient-view');
			
		}
		else {
			
			$this.parent().removeClass('active');
			$this.closest('.rev-cpicker-presets').removeClass('active');
			$(this.id.replace('-btn', '')).removeClass('active');
			gradientsOpen = false;
			
		}
		
	}
	
	function onDrag(e, ui) {
		
		var pos = parseInt((Math.round(ui.position.left) / (hitWidth - 2)).toFixed(2) * 100, 10);
		if(dragPoint === 'bottom') colorLoc.val(pos + '%').trigger('keyup');
		else opacityLoc.val(pos + '%').trigger('keyup');
		
	}
	
	function onDragStart() {
		
		var $this = $(this);
		dragPoint = $this.hasClass('rev-cpicker-point-bottom') ? 'bottom' : 'top';	
		$this.click();
		
	}
	
	function onDragStop() {
		
		if(dragPoint === 'bottom') colorLoc.trigger('focusout');
		else opacityLoc.trigger('focusout');
		
	}
	
	function updateSlider(opacity) {
		
		supressOpacity = true;
		opacitySlider.slider('value', Math.round((opacity * 0.01) * sliderHeight));
		supressOpacity = false;
		
	}
	
	function wheelMove(e) {
		
		var offset = angleWheel.offset(),
			posX = e.pageX - offset.left,
			posY = e.pageY - offset.top;
		
		if(isNaN(posX) || isNaN(posY)) return;
		
		var val = Math.atan2(posY - centerWheel, posX - centerWheel) * (180 / Math.PI) + 90;
		if(val < 0) val += 360;
		
		val = Math.max(0, Math.min(360, Math.round(val)));
		val = 5 * (Math.round(val / 5));
		
		supressWheel = true;
		onAngleChange(false, false, val);
		supressWheel = false;
		
	}
	
	function focusPatch(e) {
			
		e.stopImmediatePropagation();
		
	}
	
	function init() {
		
		if(!prepped) $.tpColorPicker();
		
		currentEditing = document.getElementById('rev-cpicker-current-edit');
		gradOutput = document.getElementById('rev-cpicker-gradient-output');
		gradInput = document.getElementById('rev-cpicker-gradient-input');
		editTitle = document.getElementById('rev-cpicker-edit-title');
		textareas = document.createElement('textarea');
		opacityDelete = $('#rev-cpicker-opacity-delete');
		pointerWrap = $('#rev-cpciker-point-container');
		opacityLoc = $('#rev-cpicker-opacity-location');
		presetGroups = $('.rev-cpicker-presets-group');
		colorOpacity = $('#rev-cpicker-color-opacity');
		radial = $('#rev-cpicker-orientation-radial');
		colorDelete = $('#rev-cpicker-color-delete');
		gradOpacity = $('#rev-cpicker-grad-opacity');
		colorLoc = $('#rev-cpicker-color-location');
		gradCore = $('#rev-cpicker-gradients-core');
		directions = $('.rev-cpicker-orientation');
		gradIris = $('#rev-cpicker-iris-gradient');
		wheelPoint = $('#rev-cpicker-wheel-point');
		gradSection = $('#rev-cpicker-gradients');
		colorIris = $('#rev-cpicker-iris-color');
		gradBtn = $('#rev-cpicker-gradient-btn');
		gradHex = $('#rev-cpicker-gradient-hex');
		colorClear = $('#rev-cpciker-clear-hex');
		reverse = $('#rev-cpicker-meta-reverse');
		hitBottom = $('#rev-cpicker-hit-bottom');
		opacitySlider = $('#rev-cpicker-scroll');
		colorSection = $('#rev-cpicker-colors');
		colorHex = $('#rev-cpicker-color-hex');
		colorBtn = $('#rev-cpicker-color-btn');
		colorBox = $('#rev-cpicker-color-box');
		angle = $('#rev-cpicker-meta-angle');
		angleWheel = $('#rev-cpicker-wheel');
		hitTop = $('#rev-cpicker-hit-top');
		mainContainer = $('#rev-cpicker');
		doc = $(document);
		
		dragObj.drag = onDrag;
		dragObj.stop = onDragStop;
		dragObj.start = onDragStart;
		
		colorBtn.data('state', colorSection.find('.rev-cpicker-color').eq(0).attr('data-color') || '#ffffff');
		gradBtn.data('state', gradSection.find('.rev-cpicker-color').eq(0).attr('data-color') || 'linear-gradient(0deg, rgba(255, 255, 255, 1) 0%, rgba(0, 0, 0, 1) 100%)');
		
		mainContainer.draggable({containment: 'window', handle: '.rev-cpicker-draggable', stop: function() {
		
			mainContainer.css('height', 'auto');
			
		}});
		
		presetGroups.perfectScrollbar({wheelPropagation: false, suppressScrollX: true});
		
		angleWheel.on('mousedown.revcpicker', function(e) {
			
			directions.removeClass('selected');
			wheelDown = true;
			wheelMove(e);
			
		}).on('mousemove.revcpicker', function(e) {
			
			if(!wheelDown) return;
			wheelMove(e);
			
		}).on('mouseleave.revcpicker mouseup.revcpicker', function() {
			
			wheelDown = false;
			
		});
		
		$('.rev-cpicker-main-btn').on('click.revcpicker', function() {
			
			var $this,
				state;
			
			colorView = this.id.search('gradient') === -1;
			if(currentColor) {
				
				$this = $(this);
				state = $this.data('state');
				
			}
			
			if(colorView) {
				
				if(currentColor) selectedHex = colorHex.val();
				cPicker.removeClass('gradient-view').addClass('color-view');
				
			}
			else {
				
				if(currentColor) selectedHex = state;
				
				cPicker.removeClass('color-view').addClass('gradient-view');
				if(!gradViewed) gradCore.children('.rev-cpicker-color').not('.blank').eq(0).click();
				
			}
			
			presetGroups.perfectScrollbar('update');
			if(state) {
					
				var isTrans = state === 'transparent',
					val = !isTrans ? state : '';
				
				if(!isTrans) currentColor[0].style.background = val;
				else currentColor.css('background', val);
				
				supressCheck = true;
				$('.rev-cpicker-color').not('.blank').each(checkPreset);
				supressCheck = false;
				
				doc.trigger('revcolorpickerupdate', [currentInput, state]);
				
			}
			
		});
		
		$('#rev-cpicker-check').on('click.revcipicker', function() {
			
			var ar,
				color,
				changed;
			
			if(cPicker.hasClass('color-view')) {
				
				var hex = colorHex.val(),
					opacity = colorOpacity.val();
				
				currentInput.removeData('gradient');
				if(hex === 'transparent') color = 'transparent';
				else if(opacity === '100%') color = RevColor.sanitizeHex(hex);
				else color = RevColor.processRgba(hex, opacity);
				ar = [currentInput, color, false];
				
			}
			else {
				
				deactivate();

				var grad = buildGradientOutput(false, false, true),
				obj = $.extend({}, grad[0]),
				clr = grad[1];
				
				currentInput.data('gradient', clr);
				color = JSON.stringify(obj).replace(/\"/g, '&');
				ar = [currentInput, clr, obj];
				
			}
			
			changed = ar[1] !== openingValue;
			if(changed) {
				
				currentInput.attr('data-color', ar[1]).val(color).change();
				doc.trigger('revcolorpicker', ar);
				if(changeCallback) changeCallback(ar[0], ar[1], ar[2]);
				
			}
			
			onClose(false, changed);
			
		});
		
		cPicker.on('click.revcpicker', function(e) {
			
			if(cPicker.hasClass('open')) {
				
				var targt = e.target,
					$this = $(targt),
					ids = targt.id,
					isTarget = targt.className.search('rev-cpicker-point') !== -1 || 
						       ids === 'rev-cpicker-section-right' || ids.search('hit') !== -1 || 
						       $this.closest('#rev-cpicker-section-right, #rev-cpicker-point-wrap').length;
				
				if(isTarget) {
					
					if($this.attr('type') === 'text') {
						
						isTarget = !$this.attr('disabled');
						
					}
					else if(ids === 'rev-cpicker-check-gradient') {
						
						isTarget = false;
						
					}
					
				}
				
				if(!isTarget) deactivate();
				
			}
			else if(wheelActive && /wheel|angle|reverse/.test(e.target.id) === false) {
				
				if(e.target.id.search('radial') === -1) {
					
					$('.rev-cpicker-orientation[data-direction="' + parseInt(angle.val()) + '"]').addClass('selected');
					
				}
				
				angleWheel.removeClass('active');
				wheelActive = false;
				
			}
			
		});
		
		$('.rev-cpicker-close').on('click.revcpicker', onClose);
		
		colorIris.wpColorPicker({palettes: false, width: 267, border: false, hide: false, change: function(e, ui) {
			
			var val = ui.color.toString();
			this.value = val;
			
			colorHex.val(val);
			if(!supressColor) {
				
				var opacity = colorOpacity.val();
				if(parseInt(opacity, 10) === 0) val = 'transparent';
				onChange(false, val, opacity);
				
				if(selectedColor) {
					
					selectedColor.removeClass('selected');
					selectedColor = false;
					
				}
				
			}
			
		}});
		
		gradIris.wpColorPicker({palettes: false, height: 250, border: false, hide: false, change: function(e, ui) {
			
			var val = ui.color.toString();
			this.value = val;
				
			gradHex.val(val);
			colorBox.css('background', val);
			curSquare.style.backgroundColor = val;
			curCorner.style.borderBottomColor = val;
			
			var rgba = RevColor.processRgba(val, 100),
				parsed = RevColor.rgbValues(rgba, 4),
				color = gradientPreview.colors[pointIndex];
			
			color.r = parsed[0];
			color.g = parsed[1];
			color.b = parsed[2];
			color.a = parsed[3];
			
			curPoint.attr('data-color', rgba);
			buildGradientOutput();
			
		}});
		
		opacitySlider.slider({orientation: 'vertical', max: sliderHeight, value: sliderHeight, 
		
			start: function() {
			
				isTransparent = colorHex.val() === 'transparent';
			
			}, 
			
			slide: function(e, ui) {
			
				if(!supressOpacity) {
				
					var opacity = parseInt((ui.value / sliderHeight).toFixed(2) * 100, 10),
						val;
					
					if(isTransparent) {
						
						val = opacity ? '#ffffff' : 'transparent';
						colorHex.val(val);
						
					}
					
					if(opacity === 0) val = 'transparent';
					onChange(false, val, opacity || 'transparent');
					colorOpacity.val(opacity + '%');
					
				}
				
			}
				
		});
		
		$('.rev-cpicker-point-location').on('keyup.revcpicker focusout.revcpicker', function(e) {
			
			if(!curPoint) return;
			
			var align = curPoint.hasClass('rev-cpicker-point-bottom') ? 'bottom' : 'top',
				locations = align === 'bottom' ? botPoints : topPoints,
				input = align === 'bottom' ? colorLoc : opacityLoc,
				point = input.val().replace('%', '') || '0',
				evt = e.type,
				dir;
			
			if(isNaN(point)) point = evt === 'keyup' ? '0' : curPoint.attr('data-location');
			point = Math.max(0, Math.min(100, parseInt(point, 10)));
			
			dir = point < 50 ? 1 : -1;
			
			while(locations.indexOf(point) !== -1) point += dir;
			
			if(evt === 'focusout') {
				
				input.val(point + '%');
				curPoint.attr('data-location', point);
				
			}
			
			curPoint.css('left', point + '%');
			buildGradientOutput();
			
		}).on('focusin.revcpicker', focusPatch);
		
		$('body').on('click.revcpicker', '.rev-cpicker-point', function() {
			
			pointerWrap.find('.rev-cpicker-point.active').removeClass('active');
			curPoint = $(this).addClass('active');
			
			curSquare = curPoint.children('.rev-cpicker-point-square')[0];
			curCorner = curPoint.children('.rev-cpicker-point-triangle')[0];
			
			buildGradientOutput(this);
			selectedColor = false;
			
			var grad = activatePoint();
			if(grad[0]) gradIris.val(grad[1]).change();
			
		}).on('mousedown.revcpicker', '.rev-cpicker-point', function(e) {
			
			curPoint = $(this).data('mousestart', e.pageY);
			
		}).on('mousemove.revcpicker', function(e) {
			
			if(!curPoint || !curPoint.data('mousestart')) return;
			
			var start = curPoint.data('mousestart'),
				posY = e.pageY;
				
			if(curPoint.hasClass('rev-cpicker-point-bottom')) {
				
				if(posY > start && posY - start > warningBuffer && colorDelete.hasClass('active')) {
					curPoint.addClass('warning');
				}
				else {
					curPoint.removeClass('warning');
				}
				
			}
			else {
				
				if(start > posY && start - posY > warningBuffer && opacityDelete.hasClass('active')) {
					curPoint.addClass('warning');
				}
				else {
					curPoint.removeClass('warning');
				}
				
			}
			
		}).on('mouseup.revcpicker', function(e) {
			
			if(!curPoint || !curPoint.data('mousestart')) return;
			
			var start = curPoint.data('mousestart'),
				end = e.pageY;
				
			curPoint.removeData('mousestart');
			if(curPoint.hasClass('rev-cpicker-point-bottom')) {
				
				if(end > start && end - start > deleteBuffer && colorDelete.hasClass('active')) {
					colorDelete.click();
				}
				else {
					curPoint.removeClass('warning');
				}
				
			}
			else {
				
				if(start > end && start - end > deleteBuffer && opacityDelete.hasClass('active')) {
					opacityDelete.click();
				}
				else {
					curPoint.removeClass('warning');
				}
				
			}
			
		}).on('change.revcpicker', '.rev-cpicker-component', function() {
			
			var $this = $(this),
				val = $this.data('gradient') || $this.val() || 'transparent';
			
			if(val === 'transparent' || RevColor.transparentRgba(val)) val = '';
			$this.data('tpcp').css('background', val);
			
		}).on('keypress.revcpicker', function(e) {
			
			if(cPicker.hasClass('active')) {
			
				var key = e.which;
				if(key == 27) {
					
					onClose();
					
				}
				else if(key == 13 && inFocus) {
					
					inFocus.blur();
					
				}
				
			}
			
		}).on('click.revcpicker', '.rev-cpicker-color:not(.blank)', function() {
			
			if(selectedColor) {
				
				if(selectedColor[0] === this && selectedColor.hasClass('selected')) return;
				selectedColor.removeClass('selected');
				
			} 
			
			selectedColor = $(this);
			
			var ids = selectedColor.parent()[0].id,
				tpe = ids.search('core') !== -1 ? 'core' : 'custom',
				view = ids.search('colors') !== -1 ? 'colors' : 'gradients',	
				btn = $('#rev-cpicker-' + view + '-' + tpe + '-btn');
			
			if(btn.hasClass('active')) btn.find('.rev-cpicker-arrow-up').click();
			if(cPicker.hasClass('color-view')) {
				
				var val = selectedColor.attr('data-color');
				
				supressColor = true;
				colorIris.val(val).change();
				if(colorHex.val() === 'transparent') colorHex.val(val.toLowerCase());
				supressColor = false;
				
				var opacity = colorOpacity.val();
				if(parseInt(opacity, 10) === 0) val = 'transparent';
				onChange(false, val, opacity);
				
			}
			else {
				
				hitTop.removeClass('full');
				hitBottom.removeClass('full');
				
				setValue(selectedColor.data('gradient'), true);
				reverse.removeClass('checked');
				gradCore.find('.rev-cpicker-color.selected').removeClass('selected');
				
			}
			
			selectedColor.addClass('selected');
			
		}).on('mouseover.revcpicker', '.rev-cpicker-color:not(.blank)', function() {
			
			if(gradientsOpen) gradOutput.style.background = getAttribute(this, 'data-color');
			
		}).on('mouseout.revcpicker', '.rev-cpicker-color:not(.blank)', function() {
			
			if(gradientsOpen) buildGradientOutput();
			
		}).on('click.revcpicker', '.rev-cpicker-delete', function() {
			
			if(!onAjax) {
			
				console.log('Ajax callback not defined');
				return;
				
			}
			
			if(window.confirm(document.getElementById('rev-cpicker-remove-delete').innerHTML)) {
				
				cPicker.addClass('onajax onajaxdelete');
				
				var $this = $(this),
					colr = $this.parent(),
					titl = colr.attr('data-title') || '';
					
				if(!titl) {
					
					console.log('Preset does not have a name/title');
					return;
					
				}

				var group = $this.closest('.rev-cpicker-presets-group'),
					ids = group[0].id,
					tpe = ids.search('colors') !== -1 ? 'colors' : 'gradients';
					
				doc.off('revcpicker_onajax_delete.revcpicker').on('revcpicker_onajax_delete.revcpicker', function(evt, err) {
					
					if(err) console.log(err);
					
					var group = $this.closest('.rev-cpicker-presets-group'),
						scroller = group.find('.ps-scrollbar-x-rail'),
						btn = $('#' + ids + '-btn');
						
					colr.remove();
					
					if(!checkRows.call(group[0])) {
						
						$('<span class="rev-cpicker-color blank"></span>').insertBefore(scroller);
						
					}
					else {
						
						group.perfectScrollbar('update');
						
					}
					
					if(checkGroup.call(group[0]) < presetColumns + 1) {

						$('<span class="rev-cpicker-color blank"></span>').insertBefore(scroller);
						if(btn.hasClass('active')) btn.children('.rev-cpicker-arrow-up').click();
						
					}
					
					cPicker.removeClass('onajax onajaxdelete');
					
				});
				
				titl = $.trim(titl.replace(/\W+/g, '_')).replace(/^\_|\_$/g, '').toLowerCase();
				onAjax('delete', titl, tpe, 'revcpicker_onajax_delete', currentInput);
				
			}
			
			return false;
			
		});
		
		$('.rev-cpicker-save-preset-btn').on('click.revcpicker', function() {
			
			if(!onAjax) {
			
				console.log('Ajax callback not defined');
				return;
				
			}
			
			var presetGroup,
				duplicateTitle,
				$this = $(this),
				input = $this.closest('.rev-cpicker-presets-save-as').find('.rev-cpicker-preset-save'),
				titl = input.val();
			
			if(!titl || !isNaN(titl)) {
				
				alert($this.attr('data-message'));
				return;
				
			}
			
			presetGroup = cPicker.hasClass('color-view') ? 'colors' : 'gradients';
			titl = $.trim(titl.replace(/\W+/g, '_')).replace(/^\_|\_$/g, '').toLowerCase();
			
			$('#rev-cpicker-' + presetGroup + '-custom').find('.rev-cpicker-color').not('.blank').each(function() {
				
				var atr = $.trim(getAttribute(this, 'data-title').replace(/\W+/g, '_')).replace(/^\_|\_$/g, '').toLowerCase();
				if(atr === titl) {
					
					alert($this.attr('data-message'));
					duplicateTitle = true;
					return false;
					
				}
				
			});
			
			if(duplicateTitle) return;
			cPicker.addClass('onajax onajaxsave');
			
			var newColorValue = {},
				color,
				grad;
				
			if(presetGroup === 'colors') {
				
				var hex = colorHex.val(),
					opacity = colorOpacity.val();
					
				if(hex === 'transparent') color = 'transparent';
				else if(opacity === '100%') color = RevColor.sanitizeHex(hex);
				else color = RevColor.processRgba(hex, opacity);
				
			}
			else {

				grad = gradOutput.style.background;
				color = $.extend({}, buildGradientOutput(false, false, true)[0]);
			
			}
			
			newColorValue[titl] = color;
			doc.off('revcpicker_onajax_save.revcpicker').on('revcpicker_onajax_save.revcpicker', function(evt, err) {
				
				if(err) {
					
					cPicker.removeClass('onajax onajaxsave');
					alert($this.attr('data-message'));
					return;
					
				}
					
				var pre = $(newPreset(newColorValue, false, ' rev-picker-color-custom', grad)),
					group = $('#rev-cpicker-' + presetGroup + '-custom'),
					box = group.find('.rev-cpicker-color.blank'),
					btn = $('#' + group[0].id + '-btn');
					
					
				if(box.length) pre.insertBefore(box.eq(0));
				else pre.insertBefore(group.find('.ps-scrollbar-x-rail'));
				
				$('#rev-cpicker-' + presetGroup + '-custom-btn').click();
				var len = checkGroup.call(group[0]);
				
				if(len > 6) {
					
					if(box.length) box.last().remove();
					btn.addClass('active').children('.rev-cpicker-arrow-down').click();
					group.perfectScrollbar('update');
					
				}

				pre.click();
				cPicker.removeClass('onajax onajaxsave');
				
			});
			
			onAjax('save', newColorValue, presetGroup, 'revcpicker_onajax_save', currentInput);
			
		});
		
		$('.rev-cpicker-preset-title').on('click.revcpicker', function() {
			
			var $this = $(this),
				par = $this.parent(),
				arrow = $this.hasClass('active') ? 'down' : 'up';
			
			onArrowClick.call($this.find('.rev-cpicker-arrow-' + arrow)[0]);
			
			par.find('.rev-cpicker-preset-title').removeClass('selected');
			$this.addClass('selected');
			
			par.find('.rev-cpicker-presets-group').hide();
			document.getElementById(this.id.replace('-btn', '')).style.display = 'block';
			
			presetGroups.perfectScrollbar('update');
			
		});
		
		colorClear.on('click.revcpicker', function() {
			
			colorOpacity.val('0%');
			updateSlider(0);
			colorIris.val(RevColor.defaultValue).change();
			colorHex.val('transparent');
			onChange(false, 'transparent');
			
		});
		
		cPicker.find('input[type="text"]').on('focusin.revcpicker', function() {
			
			inFocus = this;
			
		}).on('focusout.revcpicker', function() {
			
			inFocus = false;
			
		});
		
		$('.rev-cpicker-input').on('focusin.revcpicker', function() {
			
			var $this = $(this);
			$this.data('orig-value', $this.val());
			
		});
		
		$('.rev-cpicker-hex').on('focusout.revcpicker', function() {
			
			var $this, 
				oVal,
				val;
			
			if(this.id === 'rev-cpicker-color-hex') {
				
				val = colorHex.val();
				if(val) {
					
					val = RevColor.sanitizeHex(val);
					if(RevColor.isColor.test(val)) {
						
						colorHex.val(val);
						
					}
					else {
						
						$this = $(this);
						oVal = $this.data('orig-value');
						
						if(oVal) {
							
							val = oVal;
							colorHex.val(val);
							
						}
						else {
							
							colorClear.click();
							return;
							
						}
						
					}
					
				}
				else {	
				
					val = 'transparent';
					
				}
				
				colorIris.val(val).change();
				
			}
			else {
				
				val = gradHex.val() || RevColor.defaultValue;
				val = RevColor.sanitizeHex(val);
				
				if(!RevColor.isColor.test(val)) {
					
					$this = $(this);
					oVal = $this.data('orig-value');
					val = oVal ? oVal : RevColor.defaultValue;
					
				}
				
				gradHex.val(val);
				gradIris.val(val).change();
				
			}
			
		}).on('focusin.revcpicker', focusPatch);
		
		$('#rev-cpciker-clear-gradient').on('click.revcpicker', function() {
			
			gradIris.val(RevColor.defaultValue).change();
			
		});
		
		angle.on('keyup.revcpicker focusout.revcpicker', onAngleChange).on('focusin.revcpicker', function() {
			
			wheelActive = true;
			angleWheel.addClass('active');
			
		}).on('focusin.revcpicker', focusPatch);
			
		directions.on('click.revcpicker', function() {
			
			var $this = $(this),
				dir = $this.attr('data-direction');
			
			directions.removeClass('selected');
			$this.addClass('selected');
			
			if(dir !== 'radial') angle.removeAttr('disabled').val(getDegree(dir));
			else angle.attr('disabled', 'disabled');
			
			onAngleChange(false, dir);
			
		});
		
		$('.rev-cpicker-point-delete').on('click.revcpicker', function() {
			
			if(this.className.search('active') === -1) return;
			
			var align = curPoint.hasClass('rev-cpicker-point-bottom') ? 'bottom' : 'top',
				len = cPicker.find('.rev-cpicker-point-' + align).length;
				
			if(len > 2) {
				
				curPoint.draggable('destroy').remove();
				groupPoints = pointerWrap.children();
				
				cPicker.click();
				buildGradientOutput();

			}
			
			if(len <= maxPoints) {
				
				if(align === 'bottom') hitBottom.removeClass('full');
				else hitTop.removeClass('full');
				
			}
			
		});
		
		$('.rev-cpicker-preset-save').on('focusin.revcpicker', focusPatch);
		$('.rev-cpicker-opacity-input').on('keyup.revcpicker focusout.revcpicker', function(e) {
			
			var isColor = this.id.search('grad') === -1,
				$this = isColor ? colorOpacity : gradOpacity,
				opacity = $this.val().replace('%', ''),
				evt = e.type,
				clr;
			
			if(isNaN(opacity)) opacity = evt === 'keyup' ? '0' : $(this).data('orig-value');
			opacity = Math.max(0, Math.min(100, opacity));
			
			if(evt === 'focusout') {
				
				$this.val(opacity + '%');
				if(!isColor) curPoint.attr('data-opacity', opacity);
				
			}
			
			if(isColor) {
				
				var opaque = parseInt(opacity, 10),
					val = opaque !== 0 ? false : 'transparent';

				onChange(false, val, opacity);
				updateSlider(opacity);
			
			}
			else {
				
				var parsed = RevColor.rgbValues(curPoint.attr('data-color'), 3),
					color = gradientPreview.colors[pointIndex];
				
				opacity = (parseInt(opacity, 10) * 0.01).toFixed(2).replace(/\.?0*$/, '');
				
				color.r = parsed[0];
				color.g = parsed[1];
				color.b = parsed[2];
				color.a = opacity;
				
				clr = RevColor.rgbaString(color.r, color.g, color.b, opacity);
				curPoint.attr('data-color', clr);
				buildGradientOutput();
				
				clr = 'rgba(0, 0, 0, ' + opacity + ')';		
				curSquare.style.backgroundColor = clr;
				curCorner.style.borderTopColor = clr;
				
			}
			
		}).on('focusin.revcpicker', focusPatch);
		
		$('.rev-cpicker-builder-hit').on('click.revcpicker', function(e) {
			
			if(!points) calculatePoints();
			
			var hit = parseInt(((e.pageX - hitTop.offset().left) / hitWidth).toFixed(2) * 100, 10),
				align = this.id.search('bottom') !== -1 ? 'bottom' : 'top',
				locations = align === 'bottom' ? botPoints : topPoints,
				dir = hit < 50 ? 1 : -1;
			
			while(locations.indexOf(hit) !== -1) hit += dir;
			if(align === 'bottom') {
				
				if(cPicker.find('.rev-cpicker-point-bottom').length < maxPoints) {
					
					clonePoint(align, hit);
					selectedColor = false;
					
				}
				else {
					
					hitBottom.addClass('full');
					
				}
				
			}
			else {
				
				if(cPicker.find('.rev-cpicker-point-top').length < maxPoints) {
					
					clonePoint(align, hit);
					selectedColor = false;
					
				}
				else {
					
					hitTop.addClass('full');
					
				}
				
			}
		
		});
		
		reverse.on('click.revcpicker', function() {
			
			var rev = !reverse.hasClass('checked');
			
			if(rev) reverse.addClass('checked');
			else reverse.removeClass('checked');
			
			buildGradientOutput(false, true);
			
		});
		
		$('.rev-cpicker-arrow').on('click.revcpicker', onArrowClick);
		inited = true;
		
	}
	
	function addPresets(sets) {
		
		var settings = $.extend({}, sets),
			core = settings.core || {},
			custom = settings.custom,
			container,
			preset,
			colors,
			len,
			el;
		
		if(!customAdded || custom) {
			
			len = 4;
			customAdded = custom;
			custom = customAdded || {'colors': [], 'gradients': []};
			
		}
		else {
			
			len = 2;
			
		}
			
		if(!core.colors) core.colors = defColors;
		if(!core.gradients) core.gradients = defGradients;
		
		for(var i = 0; i < len; i++) {
			
			switch(i) {
				
				case 0:
				
					container = 'colors-core';
					colors = core.colors;
				
				break;
				
				case 1:
					
					container = 'gradients-core';
					colors = core.gradients;
				
				break;
				
				case 2:
				
					container = 'colors-custom';
					colors = custom.colors;
				
				break;
				
				case 3:
				
					container = 'gradients-custom';
					colors = custom.gradients;
				
				break;
				
			}

			preset = writePresets(container, colors.slice() || []);
			el = $('#' + preset[0]);
			el.find('.rev-cpicker-color').remove();
			el.prepend(preset[1]);
			
		}
		
	}
	
	$.tpColorPicker = function(settings) {
		
		if(!bodies) {
		
			bodies = $('body');
			cPicker = $('<div class="' + 'rev-cpicker-wrap color-view">' + markup + '</div>').appendTo(bodies);
			
		}
		
		if(!settings) settings = {};	
		if(settings.core) {
			
			if(settings.core.colors) defColors = settings.core.colors;
			if(settings.core.gradients) defGradients = settings.core.gradients;
			
		}
		
		addPresets(settings);
		
		if(!prepped) {
			
			writeLanguage(settings.language || lang);
			defMode = settings.mode || 'full';
			
		}
		else {
			
			presetGroups.perfectScrollbar('update');
			if(settings.mode) defMode = settings.mode;
			if(settings.language) writeLanguage(settings.language);
			
		}
		
		if(settings.init) onInit = settings.init;
		if(settings.onAjax) defAjax = settings.onAjax;
		if(settings.onEdit) defEdit = settings.onEdit;
		if(settings.change) defChange = settings.change;
		if(settings.cancel) defCancel = settings.cancel;
		if(settings.widgetId) defWidgetId = settings.widgetId;
		if(settings.defaultValue) RevColor.defaultValue = settings.defaultValue;
		if(settings.wrapClasses) defaultClasses = settings.wrapClasses;
		if(settings.appendedHtml) appendedHTML = settings.appendedHtml;
		
		prepped = true;
		
	};
	
	var ColorPicker = {
		
		refresh: function() {
			
			var $this = $(this);
			if($this.hasClass('rev-cpicker-component')) {
			
				var settings = $this.data('revcp') || {},
					val = $this.val() || settings.defaultValue || RevColor.defaultValue,
					colorValue = RevColor.process(val);
				
				val = colorValue[0];
				colorValue = colorValue[1] !== 'rgba' || !RevColor.transparentRgba(val, true) ? val : '';
				
				if(val !== 'transparent') $this.data('tpcp')[0].style.background = colorValue;
				else $this.data('tpcp').css('background', '');
				
				$this.attr('data-color', val).data('hex', val);
			
			}
		
		},
		
		destroy: function() {
			
			var $this = $(this).removeData();
			$this.closest('.rev-cpicker-master-wrap').removeData().remove();
			
		}
		
	};
	
	$.fn.tpColorPicker = function(settings) {
		
		if(settings && typeof settings === 'string') return this.each(ColorPicker[settings]);
		
		return this.each(function() {
			
			var $this = $(this);
			if($this.hasClass('rev-cpicker-component')) {
				
				$this.tpColorPicker('refresh');
				return;
				
			}
			
			var wrap = $('<span class="rev-colorpicker"></span>').data('revcolorinput', $this),
				box = $('<span class="rev-colorbox" />'),
				btn = $('<span class="rev-colorbtn" />'),
				cls = $this.attr('data-wrap-classes'),
				wrapper = $this.attr('data-wrapper'),
				ids = $this.attr('data-wrap-id'),
				txt = $this.attr('data-title'),
				skin = $this.attr('data-skin'),
				val = $this.val(),
				colorValue,
				defValue;
			
			wrap.insertBefore($this).append([box, btn, $this]);
			
			if(settings && $.isPlainObject(settings)) {
				
				if(!wrapper) wrapper = settings.wrapper;
				if(!cls) cls = settings.wrapClasses;
				if(!skin) skin = settings.skin;
				if(!ids) ids = settings.wrapId;
				if(!txt) txt = settings.title;
				
				defValue = settings.defaultValue;
				var sets = $this.data('revcp');
				
				if(sets) settings = $.extend({}, sets, settings);
				$this.data('revcp', settings);
				
			}
			
			if(!cls) cls = defaultClasses;
			if(cls) wrap.addClass(cls);
			if(ids) wrap.attr('id', ids);
			if(!val) {
				
				val = defValue || RevColor.defaultValue;
				$this.val(val);
				
			}
			
			colorValue = RevColor.process(val);
			val = colorValue[0];
			
			colorValue = colorValue[1] !== 'rgba' || !RevColor.transparentRgba(val, true) ? val : '';
			if(colorValue !== 'transparent') box[0].style.background = colorValue;
			
			btn[0].innerHTML = txt || langColor || lang.color;
			$this.attr({type: 'hidden', 'data-color': val}).data('tpcp', box).addClass('rev-cpicker-component');
			
			if(skin) wrap.addClass(skin);
			if(!wrapper) {
				
				wrap.addClass('rev-cpicker-master-wrap');
				
			}
			else {
				
				wrapper = $(wrapper).addClass('rev-cpicker-master-wrap');
				wrap.wrap(wrapper);
				
			}
			
			var initCallback = settings ? settings.init || onInit : false;
			if(initCallback) initCallback(wrap, $this, val, settings);
			
		});
		
	};
	
	$(function() {
	
		$('body').on('click.revcpicker', '.rev-colorpicker', function() {
				
			if(!inited) init();
			currentInput = $(this).data('revcolorinput');
			
			var widgetId = currentInput.attr('data-widget-id'),
				html = currentInput.attr('data-appended-html'),
				editing = currentInput.attr('data-editing'),
				data = currentInput.attr('data-colors'),
				mode = currentInput.attr('data-mode'),
				settings = currentInput.data('revcp'),
				lang = currentInput.attr('data-lang'),
				settingsGradients,
				customGradients,
				settingsColors,
				dataGradients,
				customColors,
				dataColors,
				presets,
				change,
				cancel,
				value,
				edit,
				ajax,
				val;

			if(data) {
				
				data = JSON.parse(data.replace(/\&/g, '"'));
				if(data.colors) dataColors = data.colors;
				if(data.gradients) dataGradients = data.gradients;
				
			}
			
			if(settings) {
				
				var colorSets = settings.colors;
				if(colorSets) {
					
					if(colorSets.core) {
					
						settingsColors = colorSets.core.colors;
						settingsGradients = colorSets.core.gradients;
					
					}
					
					if(colorSets.custom) {
						
						customColors = colorSets.custom.colors;
						customGradients = colorSets.custom.gradients;
						
					}
					
				}
				
				edit = settings.onEdit;
				ajax = settings.onAjax;
				change = settings.change;
				cancel = settings.cancel;
				
				if(!lang) lang = settings.lang;
				if(!mode) mode = settings.mode;
				if(!html) html = settings.appendedHtml;
				if(!editing) editing = settings.editing;
				if(!widgetId) widgetId = settings.widgetId;
				
			}
			
			if(settingsGradients || settingsColors || customGradients || customColors || dataGradients || dataColors) {
				
				presets = {};
				if(settingsGradients || settingsColors || dataGradients || dataColors) {
					
					presets.core = {
						
						colors: dataColors || settingsColors || defColors,
						gradients: dataGradients || settingsGradients || defGradients
						
					};
					
				}
				
				if(customGradients || customColors) {
					
					presets.custom = {
						
						colors: customColors || defColors,
						gradients: customGradients || defGradients
						
					};
					
				}
				
				addPresets(presets);
				
			}
			
			if(!widgetId) widgetId = defWidgetId;
			if(widgetId) cPicker[0].id = widgetId;
			
			if(!html) html = appendedHTML;
			if(html) appended = $(html).appendTo(mainContainer);
			
			if(lang) writeLanguage(lang);
			if(!mode) mode = defMode;
			
			if(!editing) {
				
				editing = '';
				editTitle.style.visibility = 'hidden';
				
			}
			else {
				
				editTitle.style.visibility = 'visible';
				
			}
			
			currentEditing.innerHTML = editing;
			
			if(mode === 'single' || mode === 'basic') {
				
				isFull = false;
				gradBtn.hide();
				colorBtn.show();
				if(mode === 'basic') cPicker.addClass('is-basic');
				
			}
			else {
				
				isFull = true;
				gradBtn.show();
				colorBtn.show();
				
			}
			
			val = currentInput.val() || currentInput.attr('data-color') || RevColor.defaultValue;
			if(val.split('||').length > 1) {
				
				val = RevColor.joinToRgba(val);
				currentInput.val(val);
				
			}
			
			value = setValue(val);
			openingValue = value[0];
			
			onEdit = edit || defEdit;
			onAjax = ajax || defAjax;
			onCancel = cancel || defCancel;
			changeCallback = change || defChange;
			
			if(value[1] !== 'gradient') colorBtn.data('state', openingValue);
			else gradBtn.data('state', openingValue);
			
			bodies.addClass('rev-colorpicker-open');
			currentColor = currentInput.data('tpcp');
			cPicker.data('revcpickerinput', currentInput).addClass('active').show();
			
			presetGroups.each(checkGroup).perfectScrollbar('update');
			openingColor = currentInput.attr('data-color');
			
			selectedHex = currentInput.data('hex');
			$('.rev-cpicker-color').not('.blank').each(checkPreset);
			
		});
		
	});
	
})(jQuery !== 'undefined' ? jQuery : false);











