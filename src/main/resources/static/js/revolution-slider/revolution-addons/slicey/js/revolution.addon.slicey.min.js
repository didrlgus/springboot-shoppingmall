/********************************************
 * REVOLUTION 5.0+ EXTENSION - SLICED
 * @version: 1.0 (15.02.2017)
 * @requires jquery.themepunch.revolution.js
 * @author ThemePunch
*********************************************/

(function($) {

	var _R = jQuery.fn;
		
	///////////////////////////////////////////
	// 	EXTENDED FUNCTIONS AVAILABLE GLOBAL  //
	///////////////////////////////////////////
	jQuery.extend(true,_R, {
		
		revSliderSlicey: function() {
			
			return this.each(function() {	
				jQuery(this).on('revolution.slide.onloaded',function() {
					init(this.opt);
				});			
			});
		}		
	});

	//////////////////////////////////////////
	//	-	INITIALISATION OF WHITEBOARD 	-	//
	//////////////////////////////////////////
	var init = function(opt) {	
		opt.slidecobjects = new Array();
		
		// PREPARING THE LAYERS 
		opt.c.find('.tp-slicey').each(function(){
			 var sb = jQuery(this),
				 li = sb.closest('.tp-revslider-slidesli'),
				 di = li.find('.defaultimg').first().clone(),
				 shd = li.find('.slotholder').data(),
				 _ = sb.data();
			


			 _.slicey_offset_start = 1;
			 _.slicey_offset_end = _.slicey_offset===undefined ? 1 : 1 + parseInt(_.slicey_offset, 10) * 0.01;
			 
			 _.slicey_blurstart = _.slicey_blurstart===undefined || _.slicey_blurstart==="inherit" ? shd.blurstart : _.slicey_blurstart;
			 _.slicey_blurend = _.slicey_blurend===undefined || _.slicey_blurend==="inherit" ? shd.blurend : _.slicey_blurend;


			
			 di.removeClass("tp-bgimg").removeClass("defaultimg").addClass("slicedbox_defmig");	     	
			 
			 var wp = jQuery('<div class="slicedbox_wrapper" data-slicey_offset_start="' + 
			          _.slicey_offset_start+'" data-slicey_offset_end="'+_.slicey_offset_end+'" data-global_duration="' + 
					  shd.duration/1000+'" data-global_ease="'+shd.ease+'" data-slicey_blurstart="'+_.slicey_blurstart+'" data-slicey_blurend="'+_.slicey_blurend+'" data-global_scalestart="' + 
					  (shd.scalestart/100)+'" data-global_scaleend="' + 
					  (shd.scaleend/100)+'" style="width:100%;height:100%;position:absolute;overflow:hidden;box-shadow:' + 
					  li.data('slicey_shadow')+'"></div>');
					  
			 
			 wp.append(di);
			 sb.append(wp);	  	     	     
			 var tc = wp.closest('.tp-caption');
			 punchgs.TweenLite.set(tc,{background:"transparent", transformStyle:"flat", perspective:"1000px", force3D:"true", transformOrigin:"50% 50%"});	     
			 opt.slidecobjects.push({caption:tc,li_index:li.data('index')});
			 punchgs.TweenLite.set(di,{opacity:1});
				 
		});

		// UPDATE LAYER SIZES IF SLIDE CHANGE (NEED TO DO -> Only Layer Reset on Current Layers in Slide !!)
		opt.c.on('revolution.slide.onafterswap',function(event,obj) {
			var ind = obj.currentslide.data('index');

			for (var i in opt.slidecobjects) {		
				var l = opt.slidecobjects[i].caption,
					ls = l.data();
				if (ind===opt.slidecobjects[i].li_index)				
					updateSlicedBox(l,ls,opt);
			}
		});

		// ON LAYER ENTERSTAGE START ANIMATION ON LAYER
		opt.c.on('revolution.layeraction',function(event,obj) {	
			if (obj.eventtype==="enterstage") {			
				updateSlicedBox(obj.layer,obj.layersettings,opt);			
				animateSlicedBox(obj.layer,obj.layersettings,0);
			}
		});


		// RECALCULATE SIZE OF ELEMENTS ON RESIZE
		jQuery(window).resize(function() {
			clearTimeout(opt.sliced_resize_timer);
			opt.sliced_resize_timer = setTimeout(function() {
			for (var i in opt.slidecobjects) {	
				var l = opt.slidecobjects[i].caption,
					ls = opt.slidecobjects[i].caption.data(),
					ali = opt.c.find('.active-revslide');

				if (ali.length===0 || ali.data('index')===opt.slidecobjects[i].li_index) {
					updateSlicedBox(l,ls,opt);	
					animateSlicedBox(l,ls,"update");	
				}
			}
			
			},50)
		})
	};

	// UPDATE THE SLICEBOX SIZES AND CONTENT
	var updateSlicedBox = function(l,_,opt) {
		_.slicedbox_wrapper = _.slicedbox_wrapper == undefined ? l.find('.slicedbox_wrapper') : _.slicedbox_wrapper;			
		if (_.slicedbox_wrapper.length>0) {		
			_.slicedbox_defmig = _.slicedbox_defmig == undefined ? l.find('.slicedbox_defmig') : _.slicedbox_defmig;		
			_.origin_offset = {
				sx : (opt.conw/2 - _.calcx),
				sy : (opt.conh/2 - _.calcy),
				x : (opt.conw/2 - (_.calcx+(_.eow/2))),
				y : (opt.conh/2 - (_.calcy+(_.eoh/2)))
			}
			punchgs.TweenLite.set(_.slicedbox_defmig,{opacity:1,left:(0-_.calcx)+"px" , top:(0-_.calcy)+"px", width:opt.conw, height:opt.conh, position:"absolute"});
		}
	}

	// ANIMATE, RESET PROGRESSED ANIMATION ON LAYER
	var animateSlicedBox = function(l,ls,prog) {		
		if (ls.slicedbox_wrapper.length>0) {			
			var	_ = ls.slicedbox_wrapper.data();
			if (prog===undefined) prog=0;
			if (prog==="update" && _.slicedanimation!==undefined) prog = _.slicedanimation.progress();
			_.slicedanimation = new punchgs.TimelineLite();				
			_.scalestart = _.global_scalestart * _.slicey_offset_start;
			_.scaleend = _.global_scaleend * _.slicey_offset_end;
			
			/*_.x = {
					start: ((ls.eow / (ls.eow * _.scalestart)) * ls.origin_offset.x) - ls.origin_offset.x ,  //(ls.origin_offset.x - (ls.origin_offset.x*_.scalestart))/2 ,
					end: ((ls.eow / (ls.eow * _.scaleend)) * ls.origin_offset.x) - ls.origin_offset.x  //(ls.origin_offset.x - (ls.origin_offset.x*_.scaleend))/2
			};

			_.y = {
					start: ((ls.eoh / (ls.eoh * _.scalestart)) * ls.origin_offset.y) - ls.origin_offset.y ,  //(ls.origin_offset.y - (ls.origin_offset.y*_.scalestart))/2 ,
					end: ((ls.eoh / (ls.eoh * _.scaleend)) * ls.origin_offset.y) - ls.origin_offset.y  //(ls.origin_offset.y - (ls.origin_offset.y*_.scaleend))/2
			};


					
			_.slicedanimation.add(punchgs.TweenLite.fromTo(ls.slicedbox_wrapper,_.global_duration,
											{	z:0,
												x:_.x.start,
												y:_.y.start,
												transformOrigin:"50% 50%"
											},
											{	force3D:"auto",
												x:_.x.end,
												y:_.y.end,
												z:(_.scaleend)*100,
												ease:_.global_ease
											}),0);
			*/
			_.slicedanimation.add(punchgs.TweenLite.fromTo(ls.slicedbox_wrapper,_.global_duration,
												{transformOrigin:(ls.origin_offset.sx+"px "+ls.origin_offset.sy+"px"),scale:(_.global_scalestart*_.slicey_offset_start)},
												{force3D:"auto", scale:(_.global_scaleend*_.slicey_offset_end),ease:_.global_ease}),0);

			
			// ADD BLUR EFFECT ON THE ELEMENTS
			if (_.slicey_blurstart!==undefined && _.slicey_blurend!==undefined &&  (_.slicey_blurstart!==0 || _.slicey_blurend!==0)) {
				_.blurElement = {a:_.slicey_blurstart};
				_.blurElementEnd = {a:_.slicey_blurend, ease:_.global_ease};
				_.blurAnimation = new punchgs.TweenLite(_.blurElement, _.global_duration, _.blurElementEnd);


				_.blurAnimation.eventCallback("onUpdate", function(_,ls) {									
					punchgs.TweenLite.set(ls.slicedbox_wrapper,{position:"absolute",msFilter:'blur('+_.blurElement.a+'px)',filter:'blur('+_.blurElement.a+'px)',webkitFilter:'blur('+_.blurElement.a+'px)'});
				},[_,ls]);
				_.slicedanimation.add(_.blurAnimation,0);			
			}

			_.slicedanimation.progress(prog)
			_.slicedanimation.play()
		}
	}


})(jQuery);