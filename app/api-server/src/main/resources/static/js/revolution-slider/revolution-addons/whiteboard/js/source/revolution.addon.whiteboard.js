/********************************************
 * REVOLUTION 5.0+ EXTENSION - WHITEBOARD
 * @version: 2.0 (20.10.2016)
 * @requires jquery.themepunch.revolution.js
 * @author ThemePunch
*********************************************/



(function($) {

var _R = jQuery.fn;
	
///////////////////////////////////////////
// 	EXTENDED FUNCTIONS AVAILABLE GLOBAL  //
///////////////////////////////////////////
jQuery.extend(true,_R, {
	rsWhiteBoard: function(el) {
		return this.each(function() {	
			var opt = this.opt;
			
			jQuery(this).on('revolution.slide.onloaded',function() {
				init(opt);
			});			
		});
	}		
});

//////////////////////////////////////////
//	-	INITIALISATION OF WHITEBOARD 	-	//
//////////////////////////////////////////
var init = function(opt) {
		
	// Merge of Defaults										
	o = opt.whiteboard;

	if (o===undefined) return false;
	
	// Define Markups
	var _css_thehand = "position:absolute;z-index:1000;top:0px;left:0px;",
		_css_hand_inner ="position:absolute;z-index:1000;top:0px;left:0px;",
		_css_hand_scale ="position:absolute;z-index:1000;top:0px;left:0px;",
		_css_hand_image = "position:absolute; top:0px;left:0px;background-size:contain;background-repeat:no-repeat;background-position:center center;";
		//opacity:0.2;border:1px dotted rgba(255,0,0,0.2);
	
	if (o.movehand!=undefined) o.movehand.markup = '<div class="wb-move-hand wb-thehand" style="'+_css_thehand+'"><div class="wb-hand-inner" style="'+_css_hand_inner+';width:'+o.movehand.width+'px;height:'+o.movehand.height+'px"><div class="wb-hand-scale" ><div class="wb-hand-image" style="'+_css_hand_image+'width:'+o.movehand.width+'px;height:'+o.movehand.height+'px;background-image:url('+o.movehand.src+');"></div></div></div>';
	if (o.writehand!=undefined)  o.writehand.markup = '<div class="wb-draw-hand wb-thehand" style="'+_css_thehand+'"><div class="wb-hand-inner" style="'+_css_hand_inner+'"><div class="wb-hand-scale" style="'+_css_hand_scale+'"><div class="wb-hand-image" style="'+_css_hand_image+'width:'+o.writehand.width+'px;height:'+o.writehand.height+'px;background-image:url('+o.writehand.src+');"></div></div></div>';
	
	
	jQuery(window).resize(function() {

		clearTimeout(opt.whiteboard_resize_timer);
		opt.whiteboard_resize_timer = setTimeout(function() {
			jQuery('.wb-thehand').each(function() {
				var h = jQuery(this).find('.wb-hand-scale');				
				punchgs.TweenLite.set(h,{scale:opt.bw})
			})
		},50)
	})
	// Listen on Layer Actions
	opt.c.on('revolution.slide.onbeforeswap',function(event,obj) {

		opt.c.find('.wb-thehand').remove();
	});
	opt.c.on('revolution.layeraction',function(event,obj) {			
		
		var obj_wb = obj.layersettings.whiteboard;

		if (obj_wb != undefined) {
			if (obj_wb.configured!=true) {
				if (obj_wb.hand_function=="write" || obj_wb.hand_function=="draw") {
					obj_wb.jitter_distance = obj_wb.jitter_distance!=undefined ? parseInt(obj_wb.jitter_distance,0)/100 : parseInt(o.writehand.jittering.distance,0)/100;
					obj_wb.jitter_distance_horizontal = obj_wb.jitter_distance_horizontal!=undefined ? parseInt(obj_wb.jitter_distance_horizontal,0)/100 : parseInt(o.writehand.jittering.distance_horizontal,0)/100;
					obj_wb.jitter_offset = obj_wb.jitter_offset!=undefined ? parseInt(obj_wb.jitter_offset,0)/100 : parseInt(o.writehand.jittering.offset,0)/100;
					obj_wb.jitter_offset_horizontal = obj_wb.jitter_offset_horizontal!=undefined ? parseInt(obj_wb.jitter_offset_horizontal,0)/100 : parseInt(o.writehand.jittering.offset_horizontal,0)/100;
					obj_wb.hand_type = obj_wb.hand_type || o.writehand.handtype;
				}
				if (obj_wb.hand_function=="move") {					
					obj_wb.hand_type = obj_wb.hand_type || o.movehand.handtype;
				}
				obj_wb.configured = true;
			}

			//obj.layer.css({border:"1px dashed rgba(0,0,0,0.3)"})
			
			var _d = obj.layer.data(),
				hand = jQuery(_d._pw).find('.wb-thehand');

			if (hand.length>0) {
				var wb = hand.data('wb');				
			}
			else {
				var wb = jQuery.extend(true,{},obj.layersettings.whiteboard);				
			}
			

			// ATTACH HAND TO THE RIGHT ELEMENT
			if (obj.eventtype=="enterstage") {
				if (jQuery(obj.layer).is(':visible')===false) return;
				if (!obj.layer.hasClass("handadded")) {
					obj.layer.addClass("handadded");
						obj.layersettings.handEffect="on";												
				 		if (wb.handadded!=true) 
							attachHandTo(opt,obj,wb);							
						animateHand(obj,wb);
				}								
			}		
			if (obj.eventtype=="enteredstage") {
				if (obj.layer.hasClass("handadded") && !obj.layer.hasClass("handremoved")) {				
					obj.layer.addClass("handremoved");
					obj.layersettings.handEffect="off";
					if (moveBetweenStages(opt,obj,wb)==false) {						
						moveoutHand(opt,obj,wb);
					}
				}				
			}

			if (obj.eventtype=="leavestage") {
				obj.layer.removeClass("handadded");
				obj.layer.removeClass("handremoved");
			}
		}
	});
	
}
/*********************************************************
	- 	Look For Next Drawn/Written Layer in Slide - 
*********************************************************/

var lookForNextLayer = function(obj) {
	var retobj = {},
			_d = obj.layer.data();

	retobj.obj = "";
	retobj.startat=9999999;

	
	jQuery(_d._li).find('.tp-caption').each(function() {
		var c = jQuery(this),
			_ = c.data();
		
		if (_.active!=true && _.whiteboard!=undefined && _.whiteboard["hand_function"]!="move") {
			if (parseInt(_.frames[0].delay,0)<retobj.startat) {
				retobj.obj = c;
				retobj.startat = parseInt(_.frames[0].delay,0);
			}
		}
	});

	return retobj;

}


/**************************************
	-	Move Between Stages -
****************************************/
var moveBetweenStages = function(opt,obj,wb) {
	var ret = false,
		_ = obj.layer.data(),
		hand = jQuery(_._pw).find('.wb-thehand');
			
	if (_.whiteboard.goto_next_layer==="on" && _.whiteboard.hand_function!="move" && opt.c.find('.wb-between-stations').length==0) {
		// IF HAND HAS TO MOVE TO NEXT POSSIBLE ITEM

		var nextobj = lookForNextLayer(obj);			
		if (nextobj!=undefined && nextobj.obj.length>0) {
			var hi = hand.find('.wb-hand-inner'),	
				le = _.timeline!=undefined && _.timeline._labels!=undefined &&  _.timeline._labels.frame_0_end!=undefined ? _.timeline._labels.frame_0_end : 0,
				s = nextobj.startat/1000 - le,
				pos = jQuery(_._pw).position(),
				posnew = nextobj.obj.data('_pw').position(),
				wasinstaticlayer = hand.closest('.tp-static-layers').length>0 ? true : false;


			
			
			hand.appendTo(opt.ul);
			hand.addClass("wb-between-stations")
			if (wasinstaticlayer) 
				hand.css({zIndex:200});		
			else
				hand.css({zIndex:100});		
			wb.handEffect = "off";

			
			punchgs.TweenLite.fromTo(hand,s,{top:pos.top, left:pos.left},{top:posnew.top,left:posnew.left});
			punchgs.TweenLite.to(hi,s,{x:0,y:0,onComplete:function() {	hand.remove(); }});
			ret = true;
		} 
	}
	return ret;
}



/*************************************
	- 	Draw - Jitter Hand - 
**************************************/

var rotateHand = function(hand_inner, obj, _) {
	if (_.handEffect=="off" || _.handadded!=true) return;

	
	var _d = obj.layer.data(),
		elheight = _.maxwave || _d.eoh,
		 ang = parseInt(_.hand_angle,0) || 10;
		 ro = _.hand_function=="write" || _.hand_function=="draw" ? Math.random()*ang - (ang/2) : 0;		
	_.rotatespeed = _.rotatespeed || 0.05;
	
	
	_.rotating_anim = punchgs.TweenLite.to(hand_inner,_.rotatespeed,{rotationZ:ro,ease:punchgs.Power3.easeOut,onComplete:function(){rotateHand(hand_inner,obj, _)}});
}

var jitterHand = function(hand_inner, obj, _) {	
	if (_.handEffect=="off" || _.handadded!=true) return;	
	var 	_d = obj.layer.data();

	if (_.jitter_direction == "horizontal") {

		var	elwidth = _.maxwave || _d.eow*_.jitter_distance_horizontal,
			eloffset = _d.eow*_.jitter_offset_horizontal;	
					
		if (elwidth == 0) return;
		_.current_x_offset = _.current_x_offset || 0;
		_.lastwave =  Math.random()*elwidth+eloffset;
		_.jitterspeed = _.jitterspeed || 0.05;			
		_.jittering_anim = punchgs.TweenLite.to(hand_inner,_.jitterspeed,{x:_.lastwave,ease:punchgs.Power3.easeOut,onComplete:function(){jitterHand(hand_inner,obj,_)}});
	
	} else {
		var	elheight = _.maxwave || _d.eoh;
		
		if (elheight == 0) return;
		_.current_y_offset = _.current_y_offset || 0;
		_.lastwave = _.lastwave == _.current_y_offset + (elheight*_.jitter_offset)? Math.random()*(elheight*_.jitter_distance) + _.current_y_offset + (elheight*_.jitter_offset): _.current_y_offset + (elheight*_.jitter_offset);
		_.jitterspeed = _.jitterspeed || 0.05;	
			
		_.jittering_anim = punchgs.TweenLite.to(hand_inner,_.jitterspeed,{y:_.lastwave,ease:punchgs.Power3.easeOut,onComplete:function(){jitterHand(hand_inner,obj,_)}});
	}
}

/*************************************
	- 	ATTACH HAND TO THE LAYER  - 
**************************************/
var attachHandTo = function(opt,obj,wb) {
	
	// SET DEFAULTS
	var o = wb.hand_function=="move" ? opt.whiteboard.movehand : opt.whiteboard.writehand,				
		element = o.markup,
		_d = obj.layer.data();

	
	wb.hand_full_rotation = wb.hand_full_rotation || 0;
	wb.hand_origin = o.transform.transformX+"px "+o.transform.transformY+"px";
	wb.hand_scale =  wb.hand_type=="right" ? 1 : -1;
	wb.hand_x_offset = parseInt(wb.hand_x_offset,0) || 0;
	wb.hand_y_offset = parseInt(wb.hand_y_offset,0) || 0;

	// ADD THE HAND TO THE LAYER
	jQuery(element).appendTo(jQuery(_d._pw)); 
	wb.handadded = true;	

	var hand = jQuery(_d._pw).find('.wb-thehand'),
		hand_inner = hand.find('.wb-hand-inner'),
		hand_image = hand.find('.wb-hand-image');

	// PREPARE HAND TRANSFORMS		
	punchgs.TweenLite.set(hand_image,{scaleX:wb.hand_scale, rotation:wb.hand_full_rotation,transformOrigin:wb.hand_origin, x:0-o.transform.transformX + wb.hand_x_offset, y:0-o.transform.transformY + wb.hand_y_offset});
	punchgs.TweenLite.set(hand.find('.wb-hand-scale'),{scale:opt.bw,transformOrigin:"0% 0%"})
}

var animateHand = function(obj,wb) {
	var _d = obj.layer.data(),
		hand = jQuery(_d._pw).find('.wb-thehand'),
		hand_inner = hand.find('.wb-hand-inner'),
		hand_image = hand.find('.wb-hand-image'),
		tween = punchgs.TweenLite.getTweensOf(obj.layer);
		
		
	
	// SET LOOP ANIMATION
	switch (wb.hand_function) {
		case "write":
		case "draw":
			
			var s = _d.frames[obj.frame_index].speed/1000;
			
			// IF IT IS A TEXT, WRITE TEXT
			if (_d.splittext!= undefined && _d.splittext!="none") {					
				
				wb.tweens = obj.layersettings.timeline.getChildren(true,true,false);

				
				jQuery.each(wb.tweens,function(i,tw) {	

					tw.eventCallback("onStart",function(hand_inner,obj,wb) {
						var el = jQuery(this.target),
							w=el.width(),
							h=el.height();
										
						if 	(el!==undefined && el.html() !==undefined && el.html().length>0 && el.html().charCodeAt(0)!=9 && el.html().charCodeAt(0)!=10) {										
							var pos = el.position(),
								pa = el.parent(),
								papa = pa.parent(),								
								ro = wb.hand_function=="write" ? Math.random()*10 -5 : 0,
								speed = this._duration,
								x = pos.left,
								y = pos.top;

							
							if (pa.hasClass("tp-splitted")) {
								x = pa.position().left + x;
								y = pa.position().top + y;
							}
							if (papa.hasClass("tp-splitted")) {
								x = papa.position().left + x;	
								y = papa.position().top + y;
							}
														
							wb.rotatespeed = wb.hand_angle_repeat !== undefined ? (parseFloat(speed)/parseFloat(wb.hand_angle_repeat)) : speed>1 ? speed/6 : speed>0.5 ? speed/6 : speed / 3;
							wb.jitterspeed = wb.jitter_repeat !== undefined ? (parseFloat(speed)/parseFloat(wb.jitter_repeat)) : speed>1 ? speed/6 : speed>0.5 ? speed/6 : speed / 3;

							if (wb.current_y_offset != y) speed = 0.1;

							wb.current_y_offset = y || 0;
							wb.maxwave = h;			
							if (i<wb.tweens.length-1)				
								punchgs.TweenLite.to(hand_inner,speed,{x:x+w});
							else 
								punchgs.TweenLite.to(hand_inner,speed,{x:x+w,onComplete:function() {									
									wb.handEffect="off";
									try{
										wb.jittering_anim.kill(false);
									} catch(e) {}
								}});
						} else {	
							
							wb.current_y_offset = 0;
							wb.maxwave = h;							
							wb.handEffect="off";
							try{
								wb.jittering_anim.kill(false);
							} catch(e) {}
						}

						
					},[hand_inner,obj,wb]);
										
				})


			 } else {			 	
				var dishor = (_d.eow*wb.jitter_distance_horizontal),
					offhor = (_d.eow*wb.jitter_offset_horizontal),
					disver = (_d.eoh*wb.jitter_distance),
					offver = (_d.eoh*wb.jitter_offset);
					
					wb.rotatespeed = wb.hand_angle_repeat !== undefined ? (parseFloat(s)/parseFloat(wb.hand_angle_repeat)) : s>1 ? s/6 : s>0.5 ? s/6 : s / 3;
					wb.jitterspeed = wb.jitter_repeat !== undefined ? (parseFloat(s)/parseFloat(wb.jitter_repeat)) : s>1 ? s/6 : s>0.5 ? s/6 : s / 3;
					

			 	if (wb.hand_direction=="right_to_left")	{
			 		var xf = _d.eow-offhor,
			 			xt = xf - dishor;

			 		punchgs.TweenLite.fromTo(hand_inner,s,{x:xf},{x:xt , ease:obj.layersettings.frames[0].ease,onComplete:function() {
				 		wb.handEffect="off";
				 	}});			 	
				}
			 	else
			 	if (wb.hand_direction=="top_to_bottom") {
			 		var yf = offver,
			 			yt = yf + disver;	

			 		punchgs.TweenLite.fromTo(hand_inner,s,{y:yf},{y:yt, ease:obj.layersettings.frames[0].ease,onComplete:function() {
				 		wb.handEffect="off";
				 	}});
				 	wb.jitter_direction = "horizontal";
			 	} else
			 	if (wb.hand_direction=="bottom_to_top") {
			 		var yf = _d.eoh-offver,
			 			yt = yf - disver;
			 		punchgs.TweenLite.fromTo(hand_inner,s,{y:yf},{y:yt, ease:obj.layersettings.frames[0].ease,onComplete:function() {
				 		wb.handEffect="off";
				 		
				 	}});			 
				 	wb.jitter_direction = "horizontal";	
			 	} else { 	
			 		var xf = offhor,
			 			xt = xf + dishor;			 		
				 	punchgs.TweenLite.fromTo(hand_inner,s,{x:xf},{x:xt, ease:obj.layersettings.frames[0].ease,onComplete:function() {
				 		wb.handEffect="off";
				 	}});
				 }				 	
			 }
					
			jitterHand(hand_inner, obj,wb);
			rotateHand(hand_inner, obj,wb);
			
			
		break;

		case "move":
			// PUT HANDS WRAPPER IN POSITION

			hand.data('outspeed',obj.layersettings.frames[obj.layersettings.frames.length-1].speed/1000);
			hand.data('outease',obj.layersettings.frames[obj.layersettings.frames.length-1].ease);

			tween[0].eventCallback("onUpdate",function(obj) {
				var style = this.target.style,
					trans = this.target._gsTransform,
					pos = {};

				// CALCULATE POSITION OF LAYER
				pos.x = trans.x;
				pos.y = trans.y;
				
										
				// SAVE ORIGINAL POSITION
				if (hand.data('pos')===undefined) hand.data('pos',pos);
			  	wb.hand_inner_animation = punchgs.TweenLite.set(hand,{x:pos.x,y:pos.y});
			},[obj]);
		break;
	}
	hand.data('wb',wb);
}

/*************************************
	- 	MOVE HAND AWAY FROM LAYER  - 
**************************************/
var moveoutHand = function(opt,obj,wb) {		
	
	var _d = obj.layer.data(),
		hand = jQuery(_d._pw).find('.wb-thehand'),
		pos = hand.data('pos') || {x:0, y:0},
		tl = hand.position() || {top:0,left:0},
		sp = _d.frames[obj.layersettings.frames.length-1].speed/1000 || 2,
		ea = _d.frames[obj.layersettings.frames.length-1].ease,
		alp = pos.x==0 && pos.y==0 ? 0 : 1;

	sp = sp*0.5;

	

	if (wb.hand_function!="move") {
		var handoffset = jQuery(_d._pw).position(),
			slideroffset = opt.c.offset();
		
		tl.left = wb.hand_type=="right" ? opt.c.width() -  handoffset.left + _d.eow :  0  - handoffset.left-_d.eow;
		tl.top = opt.c.height();		

	}

	punchgs.TweenLite.to(hand,sp,{top:tl.top, left:tl.left,autoAlpha:alp,x:pos.x, y:pos.y,ease:ea,onComplete:function() {

		hand.remove();
		wb.handadded = false;
	}});

}


})(jQuery);