var THEMEMASCOT = {};

(function($) {
    "use strict";

    /* ---------------------------------------------------------------------- */
    /* -------------------------- Declare Variables ------------------------- */
    /* ---------------------------------------------------------------------- */
    var $document = $(document);
    var $document_body = $(document.body);
    var $window = $(window);
    var $html = $('html');
    var $body = $('body');
    var $wrapper = $('#wrapper');
    var $header = $('#header');
    var $footer = $('#footer');
    var $sections = $('section');
    var $portfolio_gallery = $(".gallery-isotope");
    var portfolio_filter = ".portfolio-filter a";
    var $portfolio_filter_first_child = $(".portfolio-filter a:eq(0)");
    var $portfolio_flex_slider = $(".portfolio-slider");


    THEMEMASCOT.isMobile = {
        Android: function() {
            return navigator.userAgent.match(/Android/i);
        },
        BlackBerry: function() {
            return navigator.userAgent.match(/BlackBerry/i);
        },
        iOS: function() {
            return navigator.userAgent.match(/iPhone|iPad|iPod/i);
        },
        Opera: function() {
            return navigator.userAgent.match(/Opera Mini/i);
        },
        Windows: function() {
            return navigator.userAgent.match(/IEMobile/i);
        },
        any: function() {
            return (THEMEMASCOT.isMobile.Android() || THEMEMASCOT.isMobile.BlackBerry() || THEMEMASCOT.isMobile.iOS() || THEMEMASCOT.isMobile.Opera() || THEMEMASCOT.isMobile.Windows());
        }
    };

    THEMEMASCOT.isRTL = {
        check: function() {
            if( $( "html" ).attr("dir") === "rtl" ) {
                return true;
            } else {
                return false;
            }
        }
    };

    THEMEMASCOT.urlParameter = {
        get: function(sParam) {
            var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;

            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        }
    };


    THEMEMASCOT.bmiCalculator = {
        magic: function(bmi) {
            var output = '';
            var info = '';
            if (bmi) {
                if (bmi < 15) {
                    info = "very severely underweight";
                }
                if ((bmi >= 15)&&(bmi < 16)) {
                    info = "severely underweight";
                }
                if ((bmi >= 16)&&(bmi < 18.5)) {
                    info = "underweight";
                }
                if ((bmi >= 18.5)&&(bmi < 25)) {
                    info = "normal";
                }
                if ((bmi >= 25)&&(bmi < 30)) {
                    info = "overweight";
                }
                if ((bmi >= 30)&&(bmi < 35)) {
                    info = "moderately obese";
                }
                if ((bmi >= 35)&&(bmi <= 40)) {
                    info = "severely obese";
                }
                if (bmi >40) {
                    info = "very severely obese";
                }
                output = "Your BMI is <span>"  + bmi + "</span><br />" + 
                                                              "You are <span>"  + info + "</span>.";
            } else {
                output = "You broke it!";
            };
            return output;
        },
        
        calculateStandard: function (bmi_form) {
            var weight_lbs = bmi_form.find('input[name="bmi_standard_weight_lbs"]').val();
            var height_ft = bmi_form.find('input[name="bmi_standard_height_ft"]').val();
            var height_in = bmi_form.find('input[name="bmi_standard_height_in"]').val();
            var age = bmi_form.find('input[name="bmi_standard_age"]').val();
            var gender = bmi_form.find('radio[name="bmi_standard_gender"]').val();

            var total_height_inc = ( parseInt(height_ft, 10) * 12 ) + parseInt(height_in, 10);
            var bmi = ( parseFloat(weight_lbs) / (total_height_inc * total_height_inc) ) * 703;
            var output = THEMEMASCOT.bmiCalculator.magic(bmi);

            bmi_form.find('#bmi_standard_calculator_form_result').html(output).fadeIn('slow');
        },
        
        calculateMetric: function (bmi_form) {
            var weight_kg = bmi_form.find('input[name="bmi_metric_weight_kg"]').val();
            var height_cm = bmi_form.find('input[name="bmi_metric_height_cm"]').val();
            var age = bmi_form.find('input[name="bmi_standard_age"]').val();
            var gender = bmi_form.find('radio[name="bmi_standard_gender"]').val();

            var total_weight_kg = parseFloat(weight_kg) ;
            var total_height_m = parseFloat(height_cm) * 0.01;
            var bmi = ( total_weight_kg / (total_height_m * total_height_m) );
            var output = THEMEMASCOT.bmiCalculator.magic(bmi);

            bmi_form.find('#bmi_metric_calculator_form_result').html(output).fadeIn('slow');
        },
        
        init: function () {
            var bmi_Standard_Form = $('#form_bmi_standard_calculator');
            bmi_Standard_Form.on('submit', function(e) {
                e.preventDefault();
                THEMEMASCOT.bmiCalculator.calculateStandard(bmi_Standard_Form);
                return false;
            });

            var bmi_Metric_Form = $('#form_bmi_metric_calculator');
            bmi_Metric_Form.on('submit', function(e) {
                e.preventDefault();
                THEMEMASCOT.bmiCalculator.calculateMetric(bmi_Metric_Form);
                return false;
            });
        }

    };

    THEMEMASCOT.initialize = {

        init: function() {
            THEMEMASCOT.bmiCalculator.init();
            THEMEMASCOT.initialize.TM_fixedFooter();
            THEMEMASCOT.initialize.TM_datePicker();
            THEMEMASCOT.initialize.TM_ddslick();
            THEMEMASCOT.initialize.TM_sliderRange();
            THEMEMASCOT.initialize.TM_loadBSParentModal();
            THEMEMASCOT.initialize.TM_demoSwitcher();
            THEMEMASCOT.initialize.TM_platformDetect();
            THEMEMASCOT.initialize.TM_onLoadPopupPromoBox();
            THEMEMASCOT.initialize.TM_customDataAttributes();
            THEMEMASCOT.initialize.TM_parallaxBgInit();
            THEMEMASCOT.initialize.TM_resizeFullscreen();
            THEMEMASCOT.initialize.TM_prettyPhoto_lightbox();
            THEMEMASCOT.initialize.TM_nivolightbox();
            THEMEMASCOT.initialize.TM_fitVids();
            THEMEMASCOT.initialize.TM_YTPlayer();
            THEMEMASCOT.initialize.TM_equalHeightDivs();
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Fixed Footer  ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_fixedFooter: function() {
            var $fixed_footer = $('.fixed-footer');

            if( $fixed_footer.length > 0 ){
                $('body.has-fixed-footer .main-content').css('margin-bottom', $fixed_footer.height());
            }
        },
        
        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Date Picker  -------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_datePicker: function() {
            $( ".date-picker" ).datepicker();
            $( ".time-picker" ).timepicker();
            $( ".datetime-picker" ).datetimepicker();
        },

        /* ---------------------------------------------------------------------- */
        /* -------------------------------- ddslick  ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_ddslick: function() {
            var $ddslick = $("select.ddslick");
            if( $ddslick.length > 0 ) {
                $ddslick.each(function(){
                    var name =  $(this).attr('name');
                    var id = $(this).attr('id');
                    $("#"+id).ddslick({
                        width: '100%',
                        imagePosition: "left",
                        onSelected: function(selectedData){
                            $("#"+id+ " .dd-selected-value").prop ('name', name);
                         }  
                    });
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- slider range  -------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_sliderRange: function() {
            var $slider_range = $(".slider-range");
            if( $slider_range.length > 0 ) {
                $slider_range.each(function(){
                    var id = $(this).attr('id');
                    var target_id = $(this).data('target');
                    $( "#" + target_id ).slider({
                      range: "max",
                      min: 2001,
                      max: 2016,
                      value: 2010,
                      slide: function( event, ui ) {
                        $( "#" + id ).val( ui.value );
                      }
                    });
                    $( "#" + id ).val( $( "#" + target_id ).slider( "value" ) );
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------ Bootstrap Parent Modal  --------------------- */
        /* ---------------------------------------------------------------------- */
        TM_loadBSParentModal: function() {
            var ajaxLoadContent = true;
            if( ajaxLoadContent ) {
                $.ajax({
                    url: "ajax-load/bootstrap-parent-modal.html",
                    success: function (data) { $body.append(data); },
                    dataType: 'html'
                });
            }
        },
        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Demo Switcher  ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_demoSwitcher: function() {
            var showSwitcher = true;
            var $style_switcher = $('#style-switcher');
            if( !$style_switcher.length && showSwitcher ) {
                $.ajax({
                    url: "color-switcher/style-switcher.html",
                    success: function (data) { $body.append(data); },
                    dataType: 'html'
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Preloader  ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_preLoaderClickDisable: function() {
            var $preloader = $('#preloader');
            $preloader.children('#disable-preloader').on('click', function(e) {
                $preloader.fadeOut();
                return false;
            });
        },

        TM_preLoaderOnLoad: function() {
            var $preloader = $('#preloader');
            if( $preloader.length > 0 ) {
                $preloader.delay(200).fadeOut('slow');
            }
        },


        /* ---------------------------------------------------------------------- */
        /* ------------------------------- Platform detect  --------------------- */
        /* ---------------------------------------------------------------------- */
        TM_platformDetect: function() {
            if (THEMEMASCOT.isMobile.any()) {
                $html.addClass("mobile");
            } else {
                $html.addClass("no-mobile");
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Popup Promo Box  ---------------------- */
        /* ---------------------------------------------------------------------- */
        TM_onLoadPopupPromoBox: function() {
            var $modal = $('.on-pageload-popup-promobox');
            if( $modal.length > 0 ) {
                $modal.each( function(){
                    var $current_item       = $(this);
                    var target              = $current_item.data('target');
                    var timeout             = $current_item.data('timeout');

                    var delay               = $current_item.data('delay');
                    delay = ( !delay ) ? 2500 : Number(delay) + 2500;

                    if( $current_item.hasClass('cookie-enabled') ) {
                        var elementCookie = $.cookie( target );
                        if ( !!elementCookie && elementCookie === 'enabled' ){
                            return true;
                        }
                    } else {
                        $.removeCookie( target );
                    }

                    var t_enablepopup = setTimeout(function() {
                        $.magnificPopup.open({
                            items: { src: target },
                            type: 'inline',
                            mainClass: 'mfp-no-margins mfp-fade',
                            closeBtnInside: false,
                            fixedContentPos: true,
                            removalDelay: 500,
                            callbacks: {
                                afterClose: function() {
                                    if( $current_item.hasClass('cookie-enabled') ) {
                                        $.cookie( target, 'enabled' );
                                    }
                                }
                            }
                        }, 0);
                    }, Number(delay) );

                    if( timeout !== '' ) {
                        var t_closepopup = setTimeout(function() {
                            $.magnificPopup.close();
                        }, Number(delay) + Number(timeout) );
                    }
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Hash Forwarding  ---------------------- */
        /* ---------------------------------------------------------------------- */
        TM_hashForwarding: function() {
            if (window.location.hash) {
                var hash_offset = $(window.location.hash).offset().top;
                $("html, body").animate({
                    scrollTop: hash_offset
                });
            }
        },


        /* ---------------------------------------------------------------------- */
        /* ----------------------- Background image, color ---------------------- */
        /* ---------------------------------------------------------------------- */
        TM_customDataAttributes: function() {
            $('[data-bg-color]').each(function() {
                $(this).css("cssText", "background: " + $(this).data("bg-color") + " !important;");
            });
            $('[data-bg-img]').each(function() {
                $(this).css('background-image', 'url(' + $(this).data("bg-img") + ')');
            });
            $('[data-text-color]').each(function() {
                $(this).css('color', $(this).data("text-color"));
            });
            $('[data-font-size]').each(function() {
                $(this).css('font-size', $(this).data("font-size"));
            });
            $('[data-height]').each(function() {
                $(this).css('height', $(this).data("height"));
            });
            $('[data-width]').each(function() {
                $(this).css('width', $(this).data("width"));
            });
            $('[data-border]').each(function() {
                $(this).css('border', $(this).data("border"));
            });
            $('[data-margin-top]').each(function() {
                $(this).css('margin-top', $(this).data("margin-top"));
            });
            $('[data-margin-right]').each(function() {
                $(this).css('margin-right', $(this).data("margin-right"));
            });
            $('[data-margin-bottom]').each(function() {
                $(this).css('margin-bottom', $(this).data("margin-bottom"));
            });
            $('[data-margin-left]').each(function() {
                $(this).css('margin-left', $(this).data("margin-left"));
            });
        },



        /* ---------------------------------------------------------------------- */
        /* -------------------------- Background Parallax ----------------------- */
        /* ---------------------------------------------------------------------- */
        TM_parallaxBgInit: function() {
            if (!THEMEMASCOT.isMobile.any() && $window.width() >= 800 ) {
                $('.parallax').each(function() {
                    var data_parallax_ratio = ( $(this).data("parallax-ratio") === undefined ) ? '0.5': $(this).data("parallax-ratio");
                    $(this).parallax("50%", data_parallax_ratio);
                });
            } else {
                $('.parallax').addClass("mobile-parallax");
            }
        },

        /* ---------------------------------------------------------------------- */
        /* --------------------------- Home Resize Fullscreen ------------------- */
        /* ---------------------------------------------------------------------- */
        TM_resizeFullscreen: function() {
            var windowHeight = $window.height();
            $('.fullscreen, .revslider-fullscreen').height(windowHeight);
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- Magnific Popup ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_magnificPopup_lightbox: function() {
            
            var $image_popup_lightbox = $('.image-popup-lightbox');
            if( $image_popup_lightbox.length > 0 ) {
                $image_popup_lightbox.magnificPopup({
                    type: 'image',
                    closeOnContentClick: true,
                    closeBtnInside: false,
                    fixedContentPos: true,
                    mainClass: 'mfp-no-margins mfp-fade', // class to remove default margin from left and right side
                    image: {
                        verticalFit: true
                    }
                });
            }

            var $image_popup_vertical_fit = $('.image-popup-vertical-fit');
            if( $image_popup_vertical_fit.length > 0 ) {
                $image_popup_vertical_fit.magnificPopup({
                    type: 'image',
                    closeOnContentClick: true,
                    mainClass: 'mfp-img-mobile',
                    image: {
                        verticalFit: true
                    }
                });
            }

            var $image_popup_fit_width = $('.image-popup-fit-width');
            if( $image_popup_fit_width.length > 0 ) {
                $image_popup_fit_width.magnificPopup({
                    type: 'image',
                    closeOnContentClick: true,
                    image: {
                        verticalFit: false
                    }
                });
            }

            var $image_popup_no_margins = $('.image-popup-no-margins');
            if( $image_popup_no_margins.length > 0 ) {
                $image_popup_no_margins.magnificPopup({
                    type: 'image',
                    closeOnContentClick: true,
                    closeBtnInside: false,
                    fixedContentPos: true,
                    mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
                    image: {
                        verticalFit: true
                    },
                    zoom: {
                        enabled: true,
                        duration: 300 // don't foget to change the duration also in CSS
                    }
                });
            }

            var $popup_gallery = $('.popup-gallery');
            if( $popup_gallery.length > 0 ) {
                $popup_gallery.magnificPopup({
                    delegate: 'a',
                    type: 'image',
                    tLoading: 'Loading image #%curr%...',
                    mainClass: 'mfp-img-mobile',
                    gallery: {
                        enabled: true,
                        navigateByImgClick: true,
                        preload: [0,1] // Will preload 0 - before current, and 1 after the current image
                    },
                    image: {
                        tError: '<a href="%url%">The image #%curr%</a> could not be loaded.',
                        titleSrc: function(item) {
                            return item.el.attr('title') + '<small>by Marsel Van Oosten</small>';
                        }
                    }
                });
            }

            var $zoom_gallery = $('.zoom-gallery');
            if( $zoom_gallery.length > 0 ) {
                $zoom_gallery.magnificPopup({
                    delegate: 'a',
                    type: 'image',
                    closeOnContentClick: false,
                    closeBtnInside: false,
                    mainClass: 'mfp-with-zoom mfp-img-mobile',
                    image: {
                        verticalFit: true,
                        titleSrc: function(item) {
                            return item.el.attr('title') + ' &middot; <a class="image-source-link" href="#" target="_blank"></a>';
                        }
                    },
                    gallery: {
                        enabled: true
                    },
                    zoom: {
                        enabled: true,
                        duration: 300, // don't foget to change the duration also in CSS
                        opener: function(element) {
                            return element.find('img');
                        }
                    }
                    
                });
            }
            
            var $popup_yt_vimeo_gmap = $('.popup-youtube, .popup-vimeo, .popup-gmaps');
            if( $popup_yt_vimeo_gmap.length > 0 ) {
                $popup_yt_vimeo_gmap.magnificPopup({
                    disableOn: 700,
                    type: 'iframe',
                    mainClass: 'mfp-fade',
                    removalDelay: 160,
                    preloader: false,

                    fixedContentPos: false
                });
            }

            var $popup_with_zoom_anim = $('.popup-with-zoom-anim');
            if( $popup_with_zoom_anim.length > 0 ) {
                $popup_with_zoom_anim.magnificPopup({
                    type: 'inline',

                    fixedContentPos: false,
                    fixedBgPos: true,

                    overflowY: 'auto',

                    closeBtnInside: true,
                    preloader: false,

                    midClick: true,
                    removalDelay: 300,
                    mainClass: 'my-mfp-zoom-in'
                });
            }

            var $popup_with_move_anim = $('.popup-with-move-anim');
            if( $popup_with_move_anim.length > 0 ) {
                $popup_with_move_anim.magnificPopup({
                    type: 'inline',

                    fixedContentPos: false,
                    fixedBgPos: true,

                    overflowY: 'auto',

                    closeBtnInside: true,
                    preloader: false,

                    midClick: true,
                    removalDelay: 300,
                    mainClass: 'my-mfp-slide-bottom'
                });
            }
            
            var $ajaxload_popup = $('.ajaxload-popup');
            if( $ajaxload_popup.length > 0 ) {
                $ajaxload_popup.magnificPopup({
                  type: 'ajax',
                  alignTop: true,
                  overflowY: 'scroll', // as we know that popup content is tall we set scroll overflow by default to avoid jump
                  callbacks: {
                    parseAjax: function(mfpResponse) {
                      THEMEMASCOT.initialize.TM_datePicker();
                      THEMEMASCOT.initialize.TM_sliderRange();
                      THEMEMASCOT.initialize.TM_ddslick();
                    }
                  }
                });
            }

            var $form_ajax_load = $('.form-ajax-load');
            if( $form_ajax_load.length > 0 ) {
                $form_ajax_load.magnificPopup({
                  type: 'ajax'
                });
            }
            
            var $popup_with_form = $('.popup-with-form');
            if( $popup_with_form.length > 0 ) {
                $popup_with_form.magnificPopup({
                    type: 'inline',
                    preloader: false,
                    focus: '#name',

                    mainClass: 'mfp-no-margins mfp-fade',
                    closeBtnInside: false,
                    fixedContentPos: true,

                    // When elemened is focused, some mobile browsers in some cases zoom in
                    // It looks not nice, so we disable it:
                    callbacks: {
                      beforeOpen: function() {
                        if($window.width() < 700) {
                          this.st.focus = false;
                        } else {
                          this.st.focus = '#name';
                        }
                      }
                    }
                });
            }

            var $mfpLightboxAjax = $('[data-lightbox="ajax"]');
            if( $mfpLightboxAjax.length > 0 ) {
                $mfpLightboxAjax.magnificPopup({
                    type: 'ajax',
                    closeBtnInside: false,
                    callbacks: {
                        ajaxContentAdded: function(mfpResponse) {
                        },
                        open: function() {
                        },
                        close: function() {
                        }
                    }
                });
            }

            //lightbox image
            var $mfpLightboxImage = $('[data-lightbox="image"]');
            if( $mfpLightboxImage.length > 0 ) {
                $mfpLightboxImage.magnificPopup({
                    type: 'image',
                    closeOnContentClick: true,
                    closeBtnInside: false,
                    fixedContentPos: true,
                    mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
                    image: {
                        verticalFit: true
                    }
                });
            }

            //lightbox gallery
            var $mfpLightboxGallery = $('[data-lightbox="gallery"]');
            if( $mfpLightboxGallery.length > 0 ) {
                $mfpLightboxGallery.each(function() {
                    var element = $(this);
                    element.magnificPopup({
                        delegate: 'a[data-lightbox="gallery-item"]',
                        type: 'image',
                        closeOnContentClick: true,
                        closeBtnInside: false,
                        fixedContentPos: true,
                        mainClass: 'mfp-no-margins mfp-with-zoom', // class to remove default margin from left and right side
                        image: {
                            verticalFit: true
                        },
                        gallery: {
                            enabled: true,
                            navigateByImgClick: true,
                            preload: [0,1] // Will preload 0 - before current, and 1 after the current image
                        },
                        zoom: {
                          enabled: true,
                          duration: 300, // don't foget to change the duration also in CSS
                          opener: function(element) {
                            return element.find('img');
                          }
                        }

                    });
                });
            }

            //lightbox iframe
            var $mfpLightboxIframe = $('[data-lightbox="iframe"]');
            if( $mfpLightboxIframe.length > 0 ) {
                $mfpLightboxIframe.magnificPopup({
                    disableOn: 600,
                    type: 'iframe',
                    removalDelay: 160,
                    preloader: false,
                    fixedContentPos: false
                });
            }

            //lightbox inline
            var $mfpLightboxInline = $('[data-lightbox="inline"]');
            if( $mfpLightboxInline.length > 0 ) {
                $mfpLightboxInline.magnificPopup({
                    type: 'inline',
                    mainClass: 'mfp-no-margins mfp-zoom-in',
                    closeBtnInside: false,
                    fixedContentPos: true
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- lightbox popup ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_prettyPhoto_lightbox: function() {
            //prettyPhoto lightbox
            var $pretty_photo_lightbox = $("a[data-rel^='prettyPhoto']");
            if( $pretty_photo_lightbox.length > 0 ) {
                $pretty_photo_lightbox.prettyPhoto({
                    hook: 'data-rel',
                    animation_speed:'normal',
                    theme:'light_square',
                    slideshow:3000, 
                    autoplay_slideshow: false,
                    social_tools: false
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Nivo Lightbox ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_nivolightbox: function() {
            var $nivo_lightbox = $('a[data-lightbox-gallery]');
            if( $nivo_lightbox.length > 0 ) {
                $nivo_lightbox.nivoLightbox({
                    effect: 'fadeScale',
                    afterShowLightbox: function(){
                        var $nivo_iframe = $('.nivo-lightbox-content > iframe');
                        if( $nivo_iframe.length > 0 ) {
                            var src = $nivo_iframe.attr('src');
                            $nivo_iframe.attr('src', src + '?autoplay=1');
                        }
                    }
                });
            }
        },



        /* ---------------------------------------------------------------------- */
        /* ---------------------------- Wow initialize  ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_wow: function() {
            var wow = new WOW({
                mobile: false // trigger animations on mobile devices (default is true)
            });
            wow.init();
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- Fit Vids ------------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_fitVids: function() {
            $body.fitVids();
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- YT Player for Video -------------------- */
        /* ---------------------------------------------------------------------- */
        TM_YTPlayer: function() {
            var $ytube_player = $(".player");
            if( $ytube_player.length > 0 ) {
                $ytube_player.mb_YTPlayer();
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ---------------------------- equalHeights ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_equalHeightDivs: function() {
            /* equal heigh */
            var $equal_height = $('.equal-height');
            if( $equal_height.length > 0 ) {
                $equal_height.children('div').css('min-height', 'auto');
                $equal_height.equalHeights();
            }

            /* equal heigh inner div */
            var $equal_height_inner = $('.equal-height-inner');
            if( $equal_height_inner.length > 0 ) {
                $equal_height_inner.children('div').css('min-height', 'auto');
                $equal_height_inner.children('div').children('div').css('min-height', 'auto');
                $equal_height_inner.equalHeights();
                $equal_height_inner.children('div').each(function() {
                    $(this).children('div').css('min-height', $(this).css('min-height'));
                });
            }

            /* pricing-table equal heigh*/
            var $equal_height_pricing_table = $('.equal-height-pricing-table');
            if( $equal_height_pricing_table.length > 0 ) {
                $equal_height_pricing_table.children('div').css('min-height', 'auto');
                $equal_height_pricing_table.children('div').children('div').css('min-height', 'auto');
                $equal_height_pricing_table.equalHeights();
                $equal_height_pricing_table.children('div').each(function() {
                    $(this).children('div').css('min-height', $(this).css('min-height'));
                });
            }
        }

    };


    THEMEMASCOT.header = {

        init: function() {

            var t = setTimeout(function() {
                THEMEMASCOT.header.TM_fullscreenMenu();
                THEMEMASCOT.header.TM_sidePanelReveal();
                THEMEMASCOT.header.TM_scroolToTopOnClick();
                THEMEMASCOT.header.TM_scrollToFixed();
                THEMEMASCOT.header.TM_sticky();
                THEMEMASCOT.header.TM_topnavAnimate();
                THEMEMASCOT.header.TM_scrolltoTarget();
                THEMEMASCOT.header.TM_navLocalScorll();
                THEMEMASCOT.header.TM_menuCollapseOnClick();
                THEMEMASCOT.header.TM_homeParallaxFadeEffect();
                THEMEMASCOT.header.TM_topsearch_toggle();
            }, 0);

        },


        /* ---------------------------------------------------------------------- */
        /* ------------------------- menufullpage ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_fullscreenMenu: function() {
            var $menufullpage = $('.menu-full-page .fullpage-nav-toggle');
            if( $menufullpage.length > 0 ) {
                $menufullpage.menufullpage();
            }
        },


        /* ---------------------------------------------------------------------- */
        /* ------------------------- Side Push Panel ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_sidePanelReveal: function() {
            $('.side-panel-trigger').on('click', function(e) {
                $body.toggleClass("side-panel-open");
                if ( THEMEMASCOT.isMobile.any() ) {
                    $body.toggleClass("overflow-hidden");
                }
                return false;
            });

            $('.has-side-panel .body-overlay').on('click', function(e) {
                $body.toggleClass("side-panel-open");
                return false;
            });

            //sitebar tree
            $('.side-panel-nav .nav .tree-toggler').on('click', function(e) {
                $(this).parent().children('ul.tree').toggle(300);
            });
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------- scrollToTop  ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_scroolToTop: function() {
            if ($window.scrollTop() > 600) {
                $('.scrollToTop').fadeIn();
            } else {
                $('.scrollToTop').fadeOut();
            }
        },

        TM_scroolToTopOnClick: function() {
            $document_body.on('click', '.scrollToTop', function(e) {
                $('html, body').animate({
                    scrollTop: 0
                }, 800);
                return false;
            });
        },


        /* ---------------------------------------------------------------------------- */
        /* --------------------------- One Page Nav close on click -------------------- */
        /* ---------------------------------------------------------------------------- */
        TM_menuCollapseOnClick: function() {
            $document.on('click', '.onepage-nav a', function(e) {
                $('.showhide').trigger('click');
                //return false;
            });
        },

        /* ---------------------------------------------------------------------- */
        /* ----------- Active Menu Item on Reaching Different Sections ---------- */
        /* ---------------------------------------------------------------------- */
        TM_activateMenuItemOnReach: function() {
            var $onepage_nav = $('.onepage-nav');
            var cur_pos = $window.scrollTop() + 2;
            var nav_height = $onepage_nav.outerHeight();
            $sections.each(function() {
                var top = $(this).offset().top - nav_height - 80,
                    bottom = top + $(this).outerHeight();

                if (cur_pos >= top && cur_pos <= bottom) {
                    $onepage_nav.find('a').parent().removeClass('current').removeClass('active');
                    $sections.removeClass('current').removeClass('active');

                    //$(this).addClass('current').addClass('active');
                    $onepage_nav.find('a[href="#' + $(this).attr('id') + '"]').parent().addClass('current').addClass('active');
                }
            });
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------- on click scrool to target with smoothness -------- */
        /* ---------------------------------------------------------------------- */
        TM_scrolltoTarget: function() {
            //jQuery for page scrolling feature - requires jQuery Easing plugin
            $('.smooth-scroll-to-target, .fullscreen-onepage-nav a').on('click', function(e) {
                e.preventDefault();

                var $anchor = $(this);
                
                var $hearder_top = $('.header .header-nav');
                var hearder_top_offset = 0;
                if ($hearder_top[0]){
                    hearder_top_offset = $hearder_top.outerHeight(true);
                } else {
                    hearder_top_offset = 0;
                }

                //for vertical nav, offset 0
                if ($body.hasClass("vertical-nav")){
                    hearder_top_offset = 0;
                }

                var top = $($anchor.attr('href')).offset().top - hearder_top_offset;
                $('html, body').stop().animate({
                    scrollTop: top
                }, 1500, 'easeInOutExpo');

            });
        },

        /* ---------------------------------------------------------------------- */
        /* -------------------------- Scroll navigation ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_navLocalScorll: function() {
            var data_offset = -60;
            var $local_scroll = $("#menuzord .menuzord-menu, #menuzord-right .menuzord-menu");
            if( $local_scroll.length > 0 ) {
                $local_scroll.localScroll({
                    target: "body",
                    duration: 800,
                    offset: data_offset,
                    easing: "easeInOutExpo"
                });
            }

            var $local_scroll_other = $("#menuzord-side-panel .menuzord-menu, #menuzord-verticalnav .menuzord-menu, #fullpage-nav");
            if( $local_scroll_other.length > 0 ) {
                $local_scroll_other.localScroll({
                    target: "body",
                    duration: 800,
                    offset: 0,
                    easing: "easeInOutExpo"
                });
            }
        },

        /* ---------------------------------------------------------------------------- */
        /* --------------------------- collapsed menu close on click ------------------ */
        /* ---------------------------------------------------------------------------- */
        TM_scrollToFixed: function() {
            $('.navbar-scrolltofixed').scrollToFixed({zIndex: 999});
            $('.scrolltofixed').scrollToFixed({
                marginTop: $('.header .header-nav').outerHeight(true) + 10,
                limit: function() {
                    var limit = $('#footer').offset().top - $(this).outerHeight(true);
                    return limit;
                }
            });
            $('#sidebar').scrollToFixed({
                marginTop: $('.header .header-nav').outerHeight() + 20,
                limit: function() {
                    var limit = $('#footer').offset().top - $('#sidebar').outerHeight() - 20;
                    return limit;
                }
            });
        },

        /* ---------------------------------------------------------------------------- */
        /* --------------------------------- Sticky ----------------------------------- */
        /* ---------------------------------------------------------------------------- */
        TM_sticky: function() {
            $(".navbar-sticky").sticky({topSpacing:0});
        },

        /* ---------------------------------------------------------------------- */
        /* --------------------------- Waypoint Top Nav Sticky ------------------ */
        /* ---------------------------------------------------------------------- */
        TM_topnavAnimate: function() {
            /*if ($window.scrollTop() > (50)) {
                $(".navbar-sticky-animated").removeClass("animated-active");
            } else {
                $(".navbar-sticky-animated").addClass("animated-active");
            }

            if ($window.scrollTop() > (50)) {
                $(".navbar-sticky-animated .header-nav-wrapper .container, .navbar-sticky-animated .header-nav-wrapper .container-fluid").removeClass("add-padding");
            } else {
                $(".navbar-sticky-animated .header-nav-wrapper .container, .navbar-sticky-animated .header-nav-wrapper .container-fluid").addClass("add-padding");
            }*/
        },

        /* ----------------------------------------------------------------------------- */
        /* --------------------------- Menuzord - Responsive Megamenu ------------------ */
        /* ----------------------------------------------------------------------------- */
        TM_menuzord: function() {

            var $menuzord = $("#menuzord");
            if( $menuzord.length > 0 ) {
                $menuzord.menuzord({
                    align: "left",
                    effect: "slide",
                    animation: "none",
                    indicatorFirstLevel: "<i class='fa fa-angle-down'></i>",
                    indicatorSecondLevel: "<i class='fa fa-angle-right'></i>"
                });
            }

            var $menuzord_right = $("#menuzord-right");
            if( $menuzord_right.length > 0 ) {
                $menuzord_right.menuzord({
                    align: "right",
                    effect: "slide",
                    animation: "none",
                    indicatorFirstLevel: "<i class='fa fa-angle-down'></i>",
                    indicatorSecondLevel: "<i class='fa fa-angle-right'></i>"
                });
            }

            var $menuzord_side_panel = $("#menuzord-side-panel");
            if( $menuzord_side_panel.length > 0 ) {
                $menuzord_side_panel.menuzord({
                    align: "right",
                    effect: "slide",
                    animation: "none",
                    indicatorFirstLevel: "",
                    indicatorSecondLevel: "<i class='fa fa-angle-right'></i>"
                });
            }
            
            var $menuzord_vertical_nav = $("#menuzord-verticalnav");
            if( $menuzord_vertical_nav.length > 0 ) {
                $menuzord_vertical_nav.menuzord({
                    align: "right",
                    effect: "slide",
                    animation: "none",
                    indicatorFirstLevel: "<i class='fa fa-angle-down'></i>",
                    indicatorSecondLevel: "<i class='fa fa-angle-right'></i>"
                });
            }

        },

        /* ---------------------------------------------------------------------- */
        /* ---------------- home section on scroll parallax & fade -------------- */
        /* ---------------------------------------------------------------------- */
        TM_homeParallaxFadeEffect: function() {
            if ($window.width() >= 1200) {
                var scrolled = $window.scrollTop();
                $('.content-fade-effect .home-content .home-text').css('padding-top', (scrolled * 0.0610) + '%').css('opacity', 1 - (scrolled * 0.00120));
            }
        },

        /* ---------------------------------------------------------------------- */
        /* --------------------------- Top search toggle  ----------------------- */
        /* ---------------------------------------------------------------------- */
        TM_topsearch_toggle: function() {
            $document_body.on('click', '#top-search-toggle', function(e) {
                e.preventDefault();
                $('.search-form-wrapper.toggle').toggleClass('active');
                return false;
            });
        }

    };

    THEMEMASCOT.widget = {

        init: function() {

            var t = setTimeout(function() {
                THEMEMASCOT.widget.TM_shopClickEvents();
                THEMEMASCOT.widget.TM_fcCalender();
                THEMEMASCOT.widget.TM_verticalTimeline();
                THEMEMASCOT.widget.TM_verticalMasonryTimeline();
                THEMEMASCOT.widget.TM_masonryIsotop();
                THEMEMASCOT.widget.TM_pieChart();
                THEMEMASCOT.widget.TM_progressBar();
                THEMEMASCOT.widget.TM_funfact();
                THEMEMASCOT.widget.TM_odometerFunfact();
                THEMEMASCOT.widget.TM_instagramFeed();
                THEMEMASCOT.widget.TM_jflickrfeed();
                THEMEMASCOT.widget.TM_accordion_toggles();
                THEMEMASCOT.widget.TM_tooltip();
                //THEMEMASCOT.widget.TM_countDownTimer();
            }, 0);

        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Shop Plus Minus ----------------------- */
        /* ---------------------------------------------------------------------- */
        TM_shopClickEvents: function() {
            $document_body.on('click', '.quantity .plus', function(e) {
                var currentVal = parseInt($(this).parent().children(".qty").val(), 10);
                if (!isNaN(currentVal)) {
                    $(this).parent().children(".qty").val(currentVal + 1);
                }
                return false;
            });

            $document_body.on('click', '.quantity .minus', function(e) {
                var currentVal = parseInt($(this).parent().children(".qty").val(), 10);
                if (!isNaN(currentVal) && currentVal > 0) {
                    $(this).parent().children(".qty").val(currentVal - 1);
                }
                return false;
            });

            $document_body.on('click', '#checkbox-ship-to-different-address', function(e) {
                $("#checkout-shipping-address").toggle(this.checked);
            });
        },


        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Event Calendar ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_fcCalender: function() {
            if (typeof calendarEvents !== "undefined" ) {
                var $full_event_calendar = $('#full-event-calendar');
                if( $full_event_calendar.length > 0 ) {
                    $full_event_calendar.fullCalendar({
                        header: {
                            left: 'prev,next today',
                            center: 'title',
                            right: 'month,agendaWeek,agendaDay'
                        },
                        defaultDate: '2018-01-12',
                        selectable: true,
                        selectHelper: true,
                        select: function(start, end) {
                            var title = prompt('Event Title:');
                            var eventData;
                            if (title) {
                                eventData = {
                                    title: title,
                                    start: start,
                                    end: end
                                };
                                $('#calendar').fullCalendar('renderEvent', eventData, true); // stick? = true
                            }
                            $('#calendar').fullCalendar('unselect');
                        },
                        editable: true,
                        eventLimit: true, // allow "more" link when too many events
                        events: calendarEvents
                    });
                }
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------ Timeline Block ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_verticalTimeline: function() {
            var timelineBlocks = $('.cd-timeline-block'),
              offset = 0.8;

            if( timelineBlocks.length > 0 ) {
                //hide timeline blocks which are outside the viewport
                hideBlocks(timelineBlocks, offset);
                //on scolling, show/animate timeline blocks when enter the viewport
                $window.on('scroll', function(){
                  (!window.requestAnimationFrame)  ? setTimeout(function(){ showBlocks(timelineBlocks, offset); }, 100) : window.requestAnimationFrame(function(){ showBlocks(timelineBlocks, offset); });
                });
            }

            function hideBlocks(blocks, offset) {
              blocks.each(function(){
                ( $(this).offset().top > $window.scrollTop()+$window.height()*offset ) && $(this).find('.cd-timeline-img, .cd-timeline-content').addClass('is-hidden');
              });
            }

            function showBlocks(blocks, offset) {
              blocks.each(function(){
                ( $(this).offset().top <= $window.scrollTop()+$window.height()*offset && $(this).find('.cd-timeline-img').hasClass('is-hidden') ) && $(this).find('.cd-timeline-img, .cd-timeline-content').removeClass('is-hidden').addClass('bounce-in');
              });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------- Vertical Masonry Timeline -------------------- */
        /* ---------------------------------------------------------------------- */
        TM_verticalMasonryTimeline: function() {
            var $masonry_timeline = $('.vertical-masonry-timeline');
            if( $masonry_timeline.length > 0 ) {
                $masonry_timeline.isotope({
                    itemSelector : '.each-masonry-item',
                    sortBy: 'original-order',
                    layoutMode: 'masonry',
                    resizable: false
                });
            }

            //=====> Timeline Positions
            function  timeline_on_left_and_right(){
                $masonry_timeline.children('.each-masonry-item').each(function(index, element) {
                    var last_child = $(this);
                    var prev_last  = $(this).prev();
                    var last_child_offset = parseInt(last_child.css('top'), 10);
                    var prev_last_offset  = parseInt(prev_last.css('top'), 10);
                    var offset_icon       = last_child_offset - prev_last_offset;
                    
                    var go_top_to = 0;
                    if(offset_icon){
                        if ( offset_icon <= 87 ){
                            go_top_to = 87 - offset_icon;
                            last_child.find('.timeline-post-format').animate({
                                top: go_top_to
                            }, 300);
                        }
                    }
                    
                    if( $(this).position().left === 0 ){
                        $(this).removeClass('item-right');
                        $(this).addClass('item-left');
                    }else{
                        $(this).removeClass('item-left');
                        $(this).addClass('item-right');
                    }
                });
            }

            if( $masonry_timeline.length > 0 ) {
                timeline_on_left_and_right();
                
                $window.resize(function() {
                    timeline_on_left_and_right();
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- Masonry Isotope ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_masonryIsotop: function() {
            //isotope firsttime loading
            if( $portfolio_gallery.length > 0 ) {
                $portfolio_gallery.imagesLoaded(function(){
                    if ($portfolio_gallery.hasClass("masonry")){
                        $portfolio_gallery.isotope({
                            itemSelector: '.gallery-item',
                            layoutMode: "masonry",
                            masonry: {
                                columnWidth: '.gallery-item-sizer'
                            },
                            filter: "*"
                        });
                    } else{
                        $portfolio_gallery.isotope({
                            itemSelector: '.gallery-item',
                            layoutMode: "fitRows",
                            filter: "*"
                        });
                    }
                });
            }
            
            //isotope filter
            $document_body.on('click', portfolio_filter, function(e) {
                $(portfolio_filter).removeClass("active");
                $(this).addClass("active");
                var fselector = $(this).data('filter');
                if ($portfolio_gallery.hasClass("masonry")){
                    $portfolio_gallery.isotope({
                        itemSelector: '.gallery-item',
                        layoutMode: "masonry",
                        masonry: {
                            columnWidth: '.gallery-item-sizer'
                        },
                        filter: fselector
                    });
                } else{
                    $portfolio_gallery.isotope({
                        itemSelector: '.gallery-item',
                        layoutMode: "fitRows",
                        filter: fselector
                    });
                }
                return false;
            });
            
            THEMEMASCOT.slider.TM_flexslider();

        },

        TM_portfolioFlexSliderGalleryPopUpInit: function() {
            var $flexSliders = $portfolio_gallery.find('.slides');
            if( $flexSliders.length > 0 ) {
                $flexSliders.each(function () {
                    var _items = $(this).find("li > a");
                    var items = [];
                    for (var i = 0; i < _items.length; i++) {
                        items.push({src: $(_items[i]).attr("href"), title: $(_items[i]).attr("title")});
                    }
                    $(this).parent().parent().parent().find(".icons-holder").magnificPopup({
                        items: items,
                        type: 'image',
                        gallery: {
                            enabled: true
                        }
                    });
                });
            }
        },

        TM_isotopeGridRearrange: function() {
            if ($portfolio_gallery.hasClass("masonry")){
                $portfolio_gallery.isotope({
                    itemSelector: '.gallery-item',
                    layoutMode: "masonry"
                });
            } else{
                $portfolio_gallery.isotope({
                    itemSelector: '.gallery-item',
                    layoutMode: "fitRows"
                });
            }
        },

        TM_isotopeGridShuffle: function() {
            $portfolio_gallery.isotope('shuffle');
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- CountDown ------------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_countDownTimer: function() {
            var $clock = $('#clock-count-down');
            var endingdate = $clock.data("endingdate");
            if( $clock.length > 0 ) {
                $clock.countdown(endingdate, function(event) {
                    var countdown_text = '' +
                        '<ul class="countdown-timer">' +
                        '<li>%D <span>Days</span></li>' +
                        '<li>%H <span>Hours</span></li>' +
                        '<li>%M <span>Minutes</span></li>' +
                        '<li>%S <span>Seconds</span></li>' +
                        '</ul>';
                    $(this).html(event.strftime(countdown_text));
                });
            }
        },

        
        /* ---------------------------------------------------------------------- */
        /* ----------------------- pie chart / circle skill bar ----------------- */
        /* ---------------------------------------------------------------------- */
        TM_pieChart: function() {
            var $piechart = $('.piechart');
            if( $piechart.length > 0 ) {
                $piechart.appear();
                $document_body.on('appear', '.piechart', function() {
                    var current_item = $(this);
                    if (!current_item.hasClass('appeared')) {


                        var barcolor = ( current_item.data("barcolor") === undefined ) ? '#ef1e25': current_item.data("barcolor");
                        var trackcolor = ( current_item.data("trackcolor") === undefined ) ? '#f9f9f9': current_item.data("trackcolor");
                        var scalecolor = ( current_item.data("scalecolor") === undefined ) ? '#dfe0e0': current_item.data("scalecolor");
                        var linewidth = ( current_item.data("linewidth") === undefined ) ? '3': current_item.data("linewidth");
                        var size = ( current_item.data("size") === undefined ) ? '110': current_item.data("size");


                        current_item.css("width", size).css("height", size);
                        current_item.children('.percent').css("line-height", size+'px');
                        current_item.easyPieChart({
                            animate: 3000,
                            barColor: barcolor,
                            trackColor: trackcolor,
                            scaleColor: scalecolor,
                            easing: 'easeOutBounce',
                            lineWidth: linewidth,
                            size: size,
                            lineCap: 'square',
                            onStep: function(from, to, percent) {
                                $(this.el).find('span').text(Math.round(percent));
                            }
                        });
                        current_item.addClass('appeared');
                    }
                });
            }
        },
        
        /* ---------------------------------------------------------------------- */
        /* ------------------- progress bar / horizontal skill bar -------------- */
        /* ---------------------------------------------------------------------- */
        TM_progressBar: function() {
            var $progress_bar = $('.progress-bar');
            if( $progress_bar.length > 0 ) {
                $progress_bar.appear();
                $document_body.on('appear', '.progress-bar', function() {
                    var current_item = $(this);
                    if (!current_item.hasClass('appeared')) {
                        var percent = current_item.data('percent');
                        var unit = ( current_item.data("unit") === undefined ) ? "%" : current_item.data("unit");
                        var barcolor = current_item.data('barcolor');
                        current_item.append('<span class="percent">' + percent + unit + '</span>').css('background-color', barcolor).css('width', percent + '%').addClass('appeared');
                    }
                    
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------ Funfact Number Counter ---------------------- */
        /* ---------------------------------------------------------------------- */
        TM_funfact: function() {
            var $animate_number = $('.animate-number');
            if( $animate_number.length > 0 ) {
                $animate_number.appear();
                $document_body.on('appear', '.animate-number', function() {
                    $animate_number.each(function() {
                        var current_item = $(this);
                        if (!current_item.hasClass('appeared')) {
                            current_item.animateNumbers(current_item.attr("data-value"), true, parseInt(current_item.attr("data-animation-duration"), 10)).addClass('appeared');
                        }
                    });
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------- Odometer Funfact Number Counter ------------------ */
        /* ---------------------------------------------------------------------- */
        TM_odometerFunfact: function() {
            var $animate_number = $('.odometer-animate-number');
            if( $animate_number.length > 0 ) {
                $animate_number.appear();
                $document_body.on('appear', '.odometer-animate-number', function() {
                    $animate_number.each(function(){
                      var current_item = $(this);
                      var v = current_item.data('value');
                      var o = new Odometer({
                          el: this,
                          value: 1,
                          theme: ( current_item.data("theme") === undefined ) ? 'minimal': current_item.data("theme")
                      });
                      o.render();
                        setTimeout(function(){
                        o.update(v);
                        }, 500);
                    });
                   /* $animate_number.each(function() {
                        var current_item = $(this);
                        var v = current_item.attr("data-value");
                        if (!current_item.hasClass('appeared')) {
                           var starsOdometer = new Odometer({ el: this, theme: 'minimal', value: '0' });
                           starsOdometer.render();
                           starsOdometer.update(v);
                        }
                        setTimeout(function(){
                        o.update(v++);
                        }, 500);
                    });*/
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ----------------------------- Instagram Feed ---------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_instagramFeed: function() {
            var $instagram_feed = $('.instagram-feed');
            if( $instagram_feed.length > 0 ) {
                $instagram_feed.each(function() {
                    var current_div = $(this);
                    var instagramFeed = new Instafeed({
                        target: current_div.attr('id'),
                        get: 'user',
                        userId: current_div.data('userid'),
                        accessToken: current_div.data('accesstoken'),
                        resolution: current_div.data('resolution'),
                        limit: current_div.data('limit'),
                        template: '<div class="item"><figure><img src="{{image}}" /><a href="{{link}}" class="link-out" target="_blank"><i class="fa fa-link"></i></a></figure></div>',
                        after: function() {
                        }
                    });
                    instagramFeed.run();
                });
            }

            var $instagram_feed_carousel = $('.instagram-feed-carousel');
            if( $instagram_feed_carousel.length > 0 ) {
                $instagram_feed_carousel.each(function() {
                    var current_div = $(this);
                    var instagramFeed = new Instafeed({
                        target: current_div.attr('id'),
                        get: 'user',
                        userId: current_div.data('userid'),
                        accessToken: current_div.data('accesstoken'),
                        resolution: current_div.data('resolution'),
                        limit: current_div.data('limit'),
                        template: '<div class="item"><figure><img src="{{image}}" /><a href="{{link}}" class="link-out" target="_blank"><i class="fa fa-link"></i></a></figure></div>',
                        after: function() {
                            if(!current_div.hasClass("owl-carousel")){
                                current_div.addClass("owl-carousel owl-theme");
                            }
                            current_div.owlCarousel({
                                rtl: THEMEMASCOT.isRTL.check(),
                                autoplay: true,
                                autoplayTimeout: 4000,
                                loop: true,
                                margin: 15,
                                dots: true,
                                nav: false,
                                responsive: {
                                    0: {
                                        items: 2
                                    },
                                    768: {
                                        items: 2
                                    },
                                    1000: {
                                        items: 2
                                    }
                                }
                            });
                        }
                    });
                    instagramFeed.run();
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ---------------------------- Flickr Feed ----------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_jflickrfeed: function() {
            var $jflickrfeed = $(".flickr-widget .flickr-feed, .jflickrfeed");
            if( $jflickrfeed.length > 0 ) {
                $jflickrfeed.each(function() {
                    var current_div = $(this);
                    current_div.jflickrfeed({
                        limit: 9,
                        qstrings: {
                            id: current_div.data('userid')
                        },
                        itemTemplate: '<a href="{{link}}" title="{{title}}" target="_blank"><img src="{{image_m}}" alt="{{title}}">  </a>'
                    });
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------- accordion & toggles ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_accordion_toggles: function() {
            var $panel_group_collapse = $('.panel-group .collapse');
            $panel_group_collapse.on("show.bs.collapse", function(e) {
                $(this).closest(".panel-group").find("[href='#" + $(this).attr("id") + "']").addClass("active");
            });
            $panel_group_collapse.on("hide.bs.collapse", function(e) {
                $(this).closest(".panel-group").find("[href='#" + $(this).attr("id") + "']").removeClass("active");
            });
        },

        /* ---------------------------------------------------------------------- */
        /* ------------------------------- tooltip  ----------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_tooltip: function() {
            var $tooltip = $('[data-toggle="tooltip"]');
            if( $tooltip.length > 0 ) {
                $tooltip.tooltip();
            }
        },

        /* ---------------------------------------------------------------------- */
        /* ---------------------------- Twitter Feed  --------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_twittie: function() {
            var $twitter_feed = $('.twitter-feed');
            var $twitter_feed_carousel = $('.twitter-feed-carousel');
            
            if( $twitter_feed.length > 0 ) {
                $twitter_feed.twittie({
                    username: $twitter_feed.data('username'),
                    dateFormat: '%b. %d, %Y',
                    template: '{{tweet}} <div class="date">{{date}}</div>',
                    count: ( $twitter_feed.data("count") === undefined ) ? 4: $twitter_feed.data("count"),
                    loadingText: 'Loading!'
                });
            }

            if( $twitter_feed_carousel.length > 0 ) {
                $twitter_feed_carousel.twittie({
                    username: $twitter_feed_carousel.data('username'),
                    dateFormat: '%b. %d, %Y',
                    template: '{{tweet}} <div class="date">{{date}}</div>',
                    count: ( $twitter_feed_carousel.data("count") === undefined ) ? 4: $twitter_feed_carousel.data("count"),
                    loadingText: 'Loading!'
                }, function() {
                    var $this_carousel = $twitter_feed_carousel.find('ul');
                    if(!$this_carousel.hasClass("owl-carousel")){
                        $this_carousel.addClass("owl-carousel owl-theme");
                    }
                    var data_dots = ( $this_carousel.data("dots") === undefined ) ? false: $this_carousel.data("dots");
                    var data_nav = ( $this_carousel.data("nav")=== undefined ) ? false: $this_carousel.data("nav");
                    var data_duration = ( $this_carousel.data("duration") === undefined ) ? 4000: $this_carousel.data("duration");

                    $this_carousel.owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        responsive: {
                            0: {
                                items: 1
                            },
                            768: {
                                items: 1
                            },
                            1000: {
                                items: 1
                            }
                        }
                    });
                });
            }
        }
    };

    THEMEMASCOT.slider = {

        init: function() {

            var t = setTimeout(function() {
                THEMEMASCOT.slider.TM_typedAnimation();
                THEMEMASCOT.slider.TM_flexslider();
                THEMEMASCOT.slider.TM_owlCarousel();
                THEMEMASCOT.slider.TM_maximageSlider();
                THEMEMASCOT.slider.TM_bxslider();
                THEMEMASCOT.slider.TM_beforeAfterSlider();
            }, 0);

        },


        /* ---------------------------------------------------------------------- */
        /* -------------------------- Typed Text Carousel  ---------------------- */
        /* ---------------------------------------------------------------------- */
        TM_typedAnimation: function() {
            var $typed_text_carousel = $('.typed-text-carousel');
            if ( $typed_text_carousel.length > 0 ) {
                $typed_text_carousel.each(function() {
                    var string_1 = $(this).find('span:first-child').text();
                    var string_2 = $(this).find('span:nth-child(2)').text();
                    var string_3 = $(this).find('span:nth-child(3)').text();
                    var str = '';
                    var $this = $(this);
                    if (!string_2.trim() || !string_3.trim()) {
                        str = [string_1];
                    }
                    if (!string_3.trim() && string_2.length) {
                        str = [string_1, string_2];
                    }
                    if (string_1.length && string_2.length && string_3.length) {
                        str = [string_1, string_2, string_3];
                    }
                    var speed = $(this).data('speed');
                    var back_delay = $(this).data('back_delay');
                    var loop = $(this).data('loop');
                    $(this).typed({
                        strings: str,
                        typeSpeed: speed,
                        backSpeed: 0,
                        backDelay: back_delay,
                        cursorChar: "|",
                        loop: loop,
                        contentType: 'text',
                        loopCount: false
                    });
                });
            }
        },


        /* ---------------------------------------------------------------------- */
        /* -------------------------------- flexslider  ------------------------- */
        /* ---------------------------------------------------------------------- */
        TM_flexslider: function() {
            var $each_flex_slider = $('.flexslider-wrapper').find('.flexslider');
            if ( $each_flex_slider.length > 0 ) {
                THEMEMASCOT.widget.TM_portfolioFlexSliderGalleryPopUpInit();
                $each_flex_slider.each(function() {
                    var $flex_slider = $(this);
                    var data_direction = ( $flex_slider.parent().data("direction") === undefined ) ? 'horizontal': $flex_slider.parent().data("direction");
                    var data_controlNav = ( $flex_slider.parent().data("controlnav") === undefined ) ? true: $flex_slider.parent().data("controlnav");
                    var data_directionnav = ( $flex_slider.parent().data("directionnav") === undefined ) ? true: $flex_slider.parent().data("directionnav");
                    $flex_slider.flexslider({
                        rtl: THEMEMASCOT.isRTL.check(),
                        selector: ".slides > li",
                        animation: "slide",
                        easing: "swing",
                        direction: data_direction,
                        slideshow: true,
                        slideshowSpeed: 7000,
                        animationSpeed: 600,
                        pauseOnHover: false,
                        controlNav: data_controlNav,
                        directionNav: data_directionnav,
                        start: function(slider){
                            imagesLoaded($portfolio_gallery, function(){
                                setTimeout(function(){
                                    $portfolio_filter_first_child.trigger("click");
                                },500);
                            });
                            THEMEMASCOT.initialize.TM_magnificPopup_lightbox();
                            THEMEMASCOT.initialize.TM_prettyPhoto_lightbox();
                            THEMEMASCOT.initialize.TM_nivolightbox();
                        },
                        after: function(){
                        }
                    });
                });
            }
        },

        /* ---------------------------------------------------------------------- */
        /* -------------------------------- Owl Carousel  ----------------------- */
        /* ---------------------------------------------------------------------- */
        TM_owlCarousel: function() {
            var $owl_carousel_1col = $('.owl-carousel-1col, .text-carousel, .image-carousel, .fullwidth-carousel');
            if ( $owl_carousel_1col.length > 0 ) {
                if(!$owl_carousel_1col.hasClass("owl-carousel")){
                    $owl_carousel_1col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_1col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav") === undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 1,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ]
                    });
                });
            }

            var $owl_carousel_2col = $('.owl-carousel-2col');
            if ( $owl_carousel_2col.length > 0 ) {
                if(!$owl_carousel_2col.hasClass("owl-carousel")){
                    $owl_carousel_2col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_2col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 2,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            480: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 1,
                                center: false
                            },
                            750: {
                                items: 2,
                                center: false
                            },
                            960: {
                                items: 2
                            },
                            1170: {
                                items: 2
                            },
                            1300: {
                                items: 2
                            }
                        }
                    });
                });
            }

            var $owl_carousel_3col = $('.owl-carousel-3col');
            if ( $owl_carousel_3col.length > 0 ) {
                if(!$owl_carousel_3col.hasClass("owl-carousel")){
                    $owl_carousel_3col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_3col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 3,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            480: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 1,
                                center: false
                            },
                            750: {
                                items: 2,
                                center: false
                            },
                            960: {
                                items: 2
                            },
                            1170: {
                                items: 3
                            },
                            1300: {
                                items: 3
                            }
                        }
                    });
                });
            }
            

            var $owl_carousel_4col = $('.owl-carousel-4col');
            if ( $owl_carousel_4col.length > 0 ) {
                if(!$owl_carousel_4col.hasClass("owl-carousel")){
                    $owl_carousel_4col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_4col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 4,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: true
                            },
                            480: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 3,
                                center: false
                            },
                            750: {
                                items: 3,
                                center: false
                            },
                            960: {
                                items: 3
                            },
                            1170: {
                                items: 4
                            },
                            1300: {
                                items: 4
                            }
                        }
                    });
                });
            }

            var $owl_carousel_5col = $('.owl-carousel-5col');
            if ( $owl_carousel_5col.length > 0 ) {
                if(!$owl_carousel_5col.hasClass("owl-carousel")){
                    $owl_carousel_5col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_5col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 5,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            480: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 2,
                                center: false
                            },
                            750: {
                                items: 3,
                                center: false
                            },
                            960: {
                                items: 4
                            },
                            1170: {
                                items: 5
                            },
                            1300: {
                                items: 5
                            }
                        }
                    });
                });
            }

            var $owl_carousel_6col = $('.owl-carousel-6col');
            if ( $owl_carousel_6col.length > 0 ) {
                if(!$owl_carousel_6col.hasClass("owl-carousel")){
                    $owl_carousel_6col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_6col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 6,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            480: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 2,
                                center: false
                            },
                            750: {
                                items: 3,
                                center: false
                            },
                            960: {
                                items: 4
                            },
                            1170: {
                                items: 6
                            },
                            1300: {
                                items: 6
                            }
                        }
                    });
                });
            }

            var $owl_carousel_7col = $('.owl-carousel-7col');
            if ( $owl_carousel_7col.length > 0 ) {
                if(!$owl_carousel_7col.hasClass("owl-carousel")){
                    $owl_carousel_7col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_7col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 7,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 2,
                                center: false
                            },
                            750: {
                                items: 3,
                                center: false
                            },
                            960: {
                                items: 4
                            },
                            1170: {
                                items: 7
                            },
                            1300: {
                                items: 7
                            }
                        }
                    });
                });
            }

            var $owl_carousel_8col = $('.owl-carousel-8col');
            if ( $owl_carousel_8col.length > 0 ) {
                if(!$owl_carousel_8col.hasClass("owl-carousel")){
                    $owl_carousel_8col.addClass("owl-carousel owl-theme");
                }
                $owl_carousel_8col.each(function() {
                    var data_dots = ( $(this).data("dots") === undefined ) ? false: $(this).data("dots");
                    var data_nav = ( $(this).data("nav")=== undefined ) ? false: $(this).data("nav");
                    var data_duration = ( $(this).data("duration") === undefined ) ? 4000: $(this).data("duration");
                    $(this).owlCarousel({
                        rtl: THEMEMASCOT.isRTL.check(),
                        autoplay: true,
                        autoplayTimeout: data_duration,
                        loop: true,
                        items: 8,
                        margin: 15,
                        dots: data_dots,
                        nav: data_nav,
                        navText: [
                            '<i class="fa fa-chevron-left"></i>',
                            '<i class="fa fa-chevron-right"></i>'
                        ],
                        responsive: {
                            0: {
                                items: 1,
                                center: false
                            },
                            600: {
                                items: 2,
                                center: false
                            },
                            750: {
                                items: 3,
                                center: false
                            },
                            960: {
                                items: 5
                            },
                            1170: {
                                items: 8
                            },
                            1300: {
                                items: 8
                            }
                        }
                    });
                });
            }
            
        },


        /* ---------------------------------------------------------------------- */
        /* ---------- maximage Fullscreen Parallax Background Slider  ----------- */
        /* ---------------------------------------------------------------------- */
        TM_maximageSlider: function() {
            var $maximage_slider = $('#maximage');
            if( $maximage_slider.length > 0 ) {
                $maximage_slider.each(function() {
                    $(this).maximage({
                        cycleOptions: {
                            fx: 'fade',
                            speed: 1500,
                            prev: '.img-prev',
                            next: '.img-next'
                        }
                    });
                });
            }
        },


        /* ---------------------------------------------------------------------- */
        /* ----------------------------- BxSlider  ------------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_bxslider: function() {
            var $bxslider = $('.bxslider');
            if( $bxslider.length > 0 ) {
                $bxslider.each(function() {
                    var $this = $(this);
                    $this.bxSlider({
                        mode: 'vertical',
                        minSlides: ( $this.data("minslides") === undefined ) ? 2: $this.data("minslides"),
                        slideMargin: 20,
                        pager: false,
                        prevText: '<i class="fa fa-angle-left"></i>',
                        nextText: '<i class="fa fa-angle-right"></i>'
                    });
                });
            }
        },


        /* ---------------------------------------------------------------------- */
        /* ------------------------ Before After Slider  ------------------------ */
        /* ---------------------------------------------------------------------- */
        TM_beforeAfterSlider: function() {
            var $before_after_slider = $('.twentytwenty-container');
            if( $before_after_slider.length > 0 ) {
                $before_after_slider.each(function() {
                    var $this = $(this);
                    var data_offset_pct = ( $this.data("offset-percent") === undefined ) ? 0.5: $this.data("offset-percent");
                    var data_orientation = ( $this.data("orientation") === undefined ) ? 'horizontal': $this.data("orientation");
                    var data_before_label = ( $this.data("before-label") === undefined ) ? 'Before': $this.data("before-label");
                    var data_after_label = ( $this.data("after-label") === undefined ) ? 'After': $this.data("after-label");
                    var data_no_overlay = ( $this.data("no-overlay") === undefined ) ? true: $this.data("no-overlay");
                    $this.twentytwenty({
                        default_offset_pct: data_offset_pct, // How much of the before image is visible when the page loads
                        orientation: data_orientation, // Orientation of the before and after images ('horizontal' or 'vertical')
                        before_label: data_before_label, // Set a custom before label
                        after_label: data_after_label, // Set a custom after label
                        no_overlay: data_no_overlay //Do not show the overlay with before and after
                    });
                });
            }
        }
    };


    /* ---------------------------------------------------------------------- */
    /* ---------- document ready, window load, scroll and resize ------------ */
    /* ---------------------------------------------------------------------- */
    //document ready
    THEMEMASCOT.documentOnReady = {
        init: function() {
            THEMEMASCOT.initialize.init();
            THEMEMASCOT.header.init();
            THEMEMASCOT.slider.init();
            THEMEMASCOT.widget.init();
            THEMEMASCOT.windowOnscroll.init();
        }
    };

    //window on load
    THEMEMASCOT.windowOnLoad = {
        init: function() {
            var t = setTimeout(function() {
                THEMEMASCOT.initialize.TM_wow();
                THEMEMASCOT.widget.TM_twittie();
                THEMEMASCOT.initialize.TM_magnificPopup_lightbox();
                THEMEMASCOT.initialize.TM_preLoaderOnLoad();
                THEMEMASCOT.initialize.TM_hashForwarding();
                THEMEMASCOT.initialize.TM_parallaxBgInit();
            }, 0);
            $window.trigger("scroll");
            $window.trigger("resize");
        }
    };

    //window on scroll
    THEMEMASCOT.windowOnscroll = {
        init: function() {
            $window.on( 'scroll', function(){
                THEMEMASCOT.header.TM_scroolToTop();
                THEMEMASCOT.header.TM_activateMenuItemOnReach();
                THEMEMASCOT.header.TM_topnavAnimate();
            });
        }
    };

    //window on resize
    THEMEMASCOT.windowOnResize = {
        init: function() {
            var t = setTimeout(function() {
                THEMEMASCOT.initialize.TM_equalHeightDivs();
                THEMEMASCOT.initialize.TM_resizeFullscreen();
            }, 400);
        }
    };


    THEMEMASCOT.header.TM_menuzord();

    /* ---------------------------------------------------------------------- */
    /* ---------------------------- Call Functions -------------------------- */
    /* ---------------------------------------------------------------------- */
    $document.ready(
        THEMEMASCOT.documentOnReady.init
    );
    $window.on('load',
        THEMEMASCOT.windowOnLoad.init
    );
    $window.on('resize', 
        THEMEMASCOT.windowOnResize.init
    );

    //call function before document ready
    THEMEMASCOT.initialize.TM_preLoaderClickDisable();

})(jQuery);