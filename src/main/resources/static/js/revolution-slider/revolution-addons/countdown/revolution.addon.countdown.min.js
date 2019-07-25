
// countdown.js jQuery Engine MADE BY HILIOS
// https://github.com/hilios/jQuery.countdown
!function(t){"use strict";"function"==typeof define&&define.amd?define(["jquery"],t):t(jQuery)}(function(t){"use strict";function e(t){if(t instanceof Date)return t;if(String(t).match(o))return String(t).match(/^[0-9]*$/)&&(t=Number(t)),String(t).match(/\-/)&&(t=String(t).replace(/\-/g,"/")),new Date(t);throw new Error("Couldn't cast `"+t+"` to a date object.")}function s(t){var e=t.toString().replace(/([.?*+^$[\]\\(){}|-])/g,"\\$1");return new RegExp(e)}function n(t){return function(e){var n=e.match(/%(-|!)?[A-Z]{1}(:[^;]+;)?/gi);if(n)for(var a=0,o=n.length;o>a;++a){var r=n[a].match(/%(-|!)?([a-zA-Z]{1})(:[^;]+;)?/),l=s(r[0]),c=r[1]||"",u=r[3]||"",f=null;r=r[2],h.hasOwnProperty(r)&&(f=h[r],f=Number(t[f])),null!==f&&("!"===c&&(f=i(u,f)),""===c&&10>f&&(f="0"+f.toString()),e=e.replace(l,f.toString()))}return e=e.replace(/%%/,"%")}}function i(t,e){var s="s",n="";return t&&(t=t.replace(/(:|;|\s)/gi,"").split(/\,/),1===t.length?s=t[0]:(n=t[0],s=t[1])),1===Math.abs(e)?n:s}var a=[],o=[],r={precision:100,elapse:!1};o.push(/^[0-9]*$/.source),o.push(/([0-9]{1,2}\/){2}[0-9]{4}( [0-9]{1,2}(:[0-9]{2}){2})?/.source),o.push(/[0-9]{4}([\/\-][0-9]{1,2}){2}( [0-9]{1,2}(:[0-9]{2}){2})?/.source),o=new RegExp(o.join("|"));var h={Y:"years",m:"months",n:"daysToMonth",w:"weeks",d:"daysToWeek",D:"totalDays",H:"hours",M:"minutes",S:"seconds"},l=function(e,s,n){this.el=e,this.$el=t(e),this.interval=null,this.offset={},this.options=t.extend({},r),this.instanceNumber=a.length,a.push(this),this.$el.data("countdown-instance",this.instanceNumber),n&&("function"==typeof n?(this.$el.on("update.countdown",n),this.$el.on("stoped.countdown",n),this.$el.on("finish.countdown",n)):this.options=t.extend({},r,n)),this.setFinalDate(s),this.start()};t.extend(l.prototype,{start:function(){null!==this.interval&&clearInterval(this.interval);var t=this;this.update(),this.interval=setInterval(function(){t.update.call(t)},this.options.precision)},stop:function(){clearInterval(this.interval),this.interval=null,this.dispatchEvent("stoped")},toggle:function(){this.interval?this.stop():this.start()},pause:function(){this.stop()},resume:function(){this.start()},remove:function(){this.stop.call(this),a[this.instanceNumber]=null,delete this.$el.data().countdownInstance},setFinalDate:function(t){this.finalDate=e(t)},update:function(){if(0===this.$el.closest("html").length)return void this.remove();var e,s=void 0!==t._data(this.el,"events"),n=new Date;e=this.finalDate.getTime()-n.getTime(),e=Math.ceil(e/1e3),e=!this.options.elapse&&0>e?0:Math.abs(e),this.totalSecsLeft!==e&&s&&(this.totalSecsLeft=e,this.elapsed=n>=this.finalDate,this.offset={seconds:this.totalSecsLeft%60,minutes:Math.floor(this.totalSecsLeft/60)%60,hours:Math.floor(this.totalSecsLeft/60/60)%24,days:Math.floor(this.totalSecsLeft/60/60/24)%7,daysToWeek:Math.floor(this.totalSecsLeft/60/60/24)%7,daysToMonth:Math.floor(this.totalSecsLeft/60/60/24%30.4368),totalDays:Math.floor(this.totalSecsLeft/60/60/24),weeks:Math.floor(this.totalSecsLeft/60/60/24/7),months:Math.floor(this.totalSecsLeft/60/60/24/30.4368),years:Math.abs(this.finalDate.getFullYear()-n.getFullYear())},this.options.elapse||0!==this.totalSecsLeft?this.dispatchEvent("update"):(this.stop(),this.dispatchEvent("finish")))},dispatchEvent:function(e){var s=t.Event(e+".countdown");s.finalDate=this.finalDate,s.elapsed=this.elapsed,s.offset=t.extend({},this.offset),s.strftime=n(this.offset),this.$el.trigger(s)}}),t.fn.countdown=function(){var e=Array.prototype.slice.call(arguments,0);return this.each(function(){var s=t(this).data("countdown-instance");if(void 0!==s){var n=a[s],i=e[0];l.prototype.hasOwnProperty(i)?n[i].apply(n,e.slice(1)):null===String(i).match(/^[$A-Z_][0-9A-Z_$]*$/i)?(n.setFinalDate.call(n,i),n.start()):t.error("Method %s does not exist on jQuery.countdown".replace(/\%s/gi,i))}else new l(this,e[0],e[1])})}});


function tp_countdown(api,targetdate,slidechanges) {


	var currentd,currenth,currentm,currents;

	function animateAndUpdate(o,nt,ot) {
	   if (ot==undefined) {    
	     o.html(nt);
	   } else {      
	      if (o.css("opacity")>0) {
	      punchgs.TweenLite.fromTo(o,0.45,
	  		{autoAlpha:1,rotationY:0,scale:1},
	  		{autoAlpha:0,rotationY:-180,scale:0.5,ease:punchgs.Back.easeIn,onComplete:function() { o.html(nt);} });

	  punchgs.TweenLite.fromTo(o,0.45,
	  		{autoAlpha:0,rotationY:180,scale:0.5},
	  		{autoAlpha:1,rotationY:0,scale:1,ease:punchgs.Back.easeOut,delay:0.5 });
	      } else {
	         o.html(nt);
	      }
	   }
	  return nt;
	}

	function countprocess(event) {

	  var newd = event.strftime('%D'),
	      newh = event.strftime('%H'),
	      newm = event.strftime('%M'),
	      news = event.strftime('%S');
	  if (newd != currentd) currentd = animateAndUpdate(jQuery('#t_days'),newd,currentd);
	  if (newh != currenth) currenth = animateAndUpdate(jQuery('#t_hours'),newh,currenth);
	  if (newm != currentm) currentm = animateAndUpdate(jQuery('#t_minutes'),newm,currentm);
	  if (news != currents) currents = animateAndUpdate(jQuery('#t_seconds'),news,currents);
	  
	  jQuery.each(slidechanges,function(i,obj) {
	    var dr = obj.days==undefined || obj.days>=newd,
	        hr = obj.hours==undefined || obj.hours>=newh,
	        mr = obj.minutes==undefined || obj.minutes>=newm,
	        sr = obj.seconds==undefined || obj.seconds>=news;
	      if (dr && hr && mr && sr && !obj.changedown) {
	         api.revshowslide(obj.slide);
	         obj.changedown = true;
	      }
	  })
	}

	jQuery('#skipahead').click(function(){

	  var quickjump = 15000,
	  	  smalloffset = new Date().getTime() + quickjump;
	 	  api.countdown(smalloffset,countprocess);
	});
	
	api.countdown(targetdate, countprocess);	
}