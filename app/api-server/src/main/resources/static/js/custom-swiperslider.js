(function($) {
  "use strict";
  
  $(document).ready(function() {
	  thememascot_swiper_slider_init();
  });  
  
  /* ---------------------------------------------------------------------- */
  /* ----------------------------- Revolution Slider -----------------------*/
  /* ---------------------------------------------------------------------- */
  function thememascot_swiper_slider_init() {
	//swiper photo gallery with thumbnail
    var photogalleryTop = new Swiper('.swiper-photogallery-top', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        spaceBetween: 10
    });
    var photogalleryThumbs = new Swiper('.swiper-photogallery-thumbs', {
        spaceBetween: 10,
        centeredSlides: true,
        slidesPerView: 'auto',
        touchRatio: 0.2,
		    keyboardControl: true,
        slideToClickedSlide: true
    });
    photogalleryTop.params.control = photogalleryThumbs;
    photogalleryThumbs.params.control = photogalleryTop;
	
	
	//swiper fullscreen slider with text
    var swiperFullwidthSlider = new Swiper('.swiper-fullscreen-slider', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
		keyboardControl: true,
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        parallax: true,
        speed: 600
    });
	
  //swiper fullscreen slider Lazyload image
    var swiperLazyLoad = new Swiper('.swiper-lazyload-slider', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        pagination: '.swiper-pagination',
        paginationClickable: true,
        // Disable preloading of all images
        preloadImages: false,
        // Enable lazy loading
        lazyLoading: true
    });
	
  }
})(jQuery);