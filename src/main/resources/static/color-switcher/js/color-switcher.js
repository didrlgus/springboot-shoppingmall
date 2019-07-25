/*-----------------------------------------------------------------------------------
 /* Styles Switcher
 -----------------------------------------------------------------------------------*/

window.console = window.console || (function() {
    var c = {};
    c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile = c.clear = c.exception = c.trace = c.assert = function() {
    };
    return c;
})();

function makeTimeStamp() {
    return timestamp = Date.now().toString();
}

//rebuild owl
function switcherRebuildOwlCarousel() {
    $('.owl-carousel').each(function() {
        $(this).trigger('destroy.owl.carousel');
        // After destory, the markup is still not the same with the initial.
        // The differences are:
        //   1. The initial content was wrapped by a 'div.owl-stage-outer';
        //   2. The '.owl-carousel' itself has an '.owl-loaded' class attached;
        //   We have to remove that before the new initialization.
        $(this).html($(this).find('.owl-stage-outer').html()).removeClass('owl-loaded');
    });
    THEMEMASCOT.slider.TM_owlCarousel();
}


jQuery(document).ready(function($) {
	
	var colors = '';
	var server_url = 'http://demo.thememascot.com/lara/demo/css/colors/'; 
    var theme_skin_folder_url = 'css/colors/'; 
    var menuzord_skins_folder_url = 'css/menuzord-skins/'; 


    //initially if rtl add switcher rtl:
    if( $( "html" ).attr("dir") == "rtl" ) {
       $('<link href="color-switcher/css/color-switcher-rtl.css" rel="stylesheet" type="text/css" id="link-switcher-rtl">').appendTo('body');
        // Style switcher (ACTIVE)
        $('#style-switcher').animate({
            right: '-270px'
        });
    } else {
        // Style switcher (ACTIVE)
        $('#style-switcher').animate({
            left: '-270px'
        });
    }

    $('#style-switcher .switcher-toggle').on('click', function(e) {
        e.preventDefault();
        var div = $('#style-switcher');
        if ($(this).hasClass('closed')) {
            if( $( "html" ).attr("dir") == "rtl" ) {
                $('#style-switcher').animate({
                    right: '0px'
                });
            } else {
                $('#style-switcher').animate({
                    left: '0px'
                });
            }

            // open switcher and add class open
            $(this).addClass('open');
            $(this).removeClass('closed');

        } else {
            if( $( "html" ).attr("dir") == "rtl" ) {
                $('#style-switcher').animate({
                    right: '-270px'
                });
            } else {
                $('#style-switcher').animate({
                    left: '-270px'
                });
            }
            // close switcher and add closed
            $(this).addClass('closed');
            $(this).removeClass('open');
        }
    })
	
        
    //1. Theme Skins Color change (ACTIVE):
    $('#switcher-theme-custom-color li a').on('click', function(e) {
        if($("#link-colors-style").length == 0) {
            $('<link href="" rel="stylesheet" type="text/css" id="link-colors-style">').appendTo('body');
        }
        var color_code = $(this).attr('class');
        $(this).parent().parent().find('li').removeClass('active');
        $("#link-colors-style").attr("href", theme_skin_folder_url + color_code +".css?"+ makeTimeStamp() );
        $(this).parent().addClass('active');
        //$.cookie("layout_color", server_url + color_code +".css");
        return false;
    }); 
        
    //2. Menu Skins Color change (ACTIVE):
    $('#switcher-menu-custom-color li a').on('click', function(e) {
        var color_code = $(this).attr('class');
        var splitted = color_code.split('-');
        $('header .menuzord').attr('class', '').addClass('menuzord menuzord-responsive').addClass(splitted[2]);
        return false;
    });


    //3. Menu Style change (ACTIVE):
    $('#switcher-menu-style').on('change', function (e) {
        var optionSelected = $("option:selected", this);
        var valueSelected = this.value;

        if($("#menuzord-menu-skins").length == 0) {
            $('<link href="" rel="stylesheet" type="text/css" id="menuzord-menu-skins">').appendTo('body');
        }
        $("#menuzord-menu-skins").attr("href", menuzord_skins_folder_url + valueSelected +".css?"+ makeTimeStamp() );

    });
    
    
    //4. Layout Mode (ACTIVE):  
    $('#switcher-layout-mode a').on('click', function(e) {
        $(this).parent().find('a').removeClass('btn-primary').addClass('btn-dark');
        var layout_mode = $(this).removeClass('btn-dark').addClass('btn-primary').data('layout-mode');
        if( layout_mode == "wide" ) {
            $('body').removeClass('boxed-layout').removeClass('pt-40').removeClass('pb-40');
        } else if( layout_mode == "boxed" ) {
            $('body').addClass('boxed-layout bg-gray-light pt-40 pb-40 p-sm-0');
            THEMEMASCOT.widget.TM_isotopeGridRearrange();
        }

        $('.navbar-scrolltofixed').trigger('resize');
        switcherRebuildOwlCarousel();
        return false;
    });
    
    //auto bg image
    $('#switcher-theme-custom-color .color').each(function() {
        $(this).css('background-color', $(this).data("color"));
    });
    
    //auto bg image
    $('.switcher-style-list a[data-bg]').each(function() {
        $(this).css('background-image', 'url(' + $(this).data("bg") + ')');
    });

    //5. Background Pattern change (ACTIVE):
    $('#switcher-boxed-layout-bg-pattern a').on('click', function(e) {
        $('body').css('background-image', 'url(' + $(this).data('bg')+ ')').removeClass('bg-img-cover');
        return false;
    });

    //6. Background Image change (ACTIVE):  
    $('#switcher-boxed-layout-bg-img a').on('click', function(e) {
        $('body').css('background-image', 'url(' + $(this).data('bg')+ ')').addClass('bg-img-cover');
        return false;
    });

    //6. Background Image change (ACTIVE):  
    $('#switcher-boxed-layout-bg-color a').on('click', function(e) {
        var color_code = $(this).attr('class');
        $("body").removeClass (function (index, css) {
            return (css.match (/(^|\s)bg-\S+/g) || []).join(' ');
        });
        $('body').css('background-image', 'none').removeClass(color_code).addClass(color_code);
        return false;
    });

    //7. Body Font-Family (ACTIVE):  
    $('#switcher-body-fontfamily').on('change', function (e) {
        var optionSelected = $("option:selected", this);
        var valueSelected = this.value;
        var splttedval = valueSelected.split('|')
        $('body').css('font-family', splttedval[0]);
        
        $( "#link-body-font-family" ).remove();
        $('<link href="'+splttedval[1]+'" rel="stylesheet" type="text/css" id="link-body-font-family">').appendTo('body');
    });

    //7. Heading Font-Family (ACTIVE):  
    $('#switcher-heading-fontfamily').on('change', function (e) {
        var optionSelected = $("option:selected", this);
        var valueSelected = this.value;
        var splttedval = valueSelected.split('|')
        $('h1,h2,h3,h4,h5,h6').css('font-family', splttedval[0]);
        
        $( "#link-heading-font-family" ).remove();
        $('<link href="'+splttedval[1]+'" rel="stylesheet" type="text/css" id="link-heading-font-family">').appendTo('body');
    });

    //8. Background Color Dark/Light (ACTIVE):  
    $('#switcher-bgcolor a').on('click', function(e) {
        $(this).parent().find('a').removeClass('btn-primary').addClass('btn-dark');
        var bgcolor = $(this).removeClass('btn-dark').addClass('btn-primary').data('bgcolor-type');
        if( bgcolor == "dark" ) {
            $('body').addClass('dark');
            $( "#link-style-main-dark" ).remove();
            $('<link href="css/style-main-dark.css?'+ makeTimeStamp() +'" rel="stylesheet" type="text/css" id="link-style-main-dark">').appendTo('body');
            $('#header .menuzord-brand img').attr('src','images/logo-wide-white.png');
        } else if( bgcolor == "light" ) {
            $('body').removeClass('dark');
            $('#header .menuzord-brand img').attr('src','images/gym/logo-wide.png');
        }
        return false;
    });

    //9. Direction change (ACTIVE):  
    $('#switcher-rtl a').on('click', function(e) {
        $(this).parent().find('a').removeClass('btn-primary').addClass('btn-dark');
        var direction_type = $(this).removeClass('btn-dark').addClass('btn-primary').data('direction-type');
        if( direction_type == "rtl" ) {
            $('html').attr('dir', 'rtl').removeClass('ltr').addClass('rtl');
            $( "#link-bootstrap-rtl" ).remove();
            $( "#link-style-main-rtl" ).remove();
            $( "#link-style-main-rtl-extra" ).remove();
            $( "#link-switcher-rtl" ).remove();
            $('<link href="css/bootstrap-rtl.min.css" rel="stylesheet" type="text/css" id="link-bootstrap-rtl">').appendTo('body');
            $('<link href="css/style-main-rtl.css?'+ makeTimeStamp() +'" rel="stylesheet" type="text/css" id="link-style-main-rtl">').appendTo('body');
            $('<link href="css/style-main-rtl-extra.css?'+ makeTimeStamp() +'" rel="stylesheet" type="text/css" id="link-style-main-rtl-extra">').appendTo('body');
            $('<link href="color-switcher/css/color-switcher-rtl.css" rel="stylesheet" type="text/css" id="link-switcher-rtl">').appendTo('body');
            
            switcherRebuildOwlCarousel();

        } else if( direction_type == "ltr" ) {
            $('html').attr('dir', 'ltr').removeClass('rtl').addClass('ltr');
            $( "#link-bootstrap-rtl" ).remove();
            $( "#link-style-main-rtl" ).remove();
            $( "#link-style-main-rtl-extra" ).remove();
            $( "#link-switcher-rtl" ).remove();
        }
        return false;
    });
            

    //10. Direction change (ACTIVE):  
    $('#switcher-reset').on('click', function(e) {
        
        return false;
    });

	//button-reset:	
    $('#button-reset2 a').on('click', function(e) {
        $('body').css('background', '#fff');
        $("#link-colors-style").attr("href", theme_skin_folder_url + "yellow.css");
        //$.cookie('layout_color',  server_url + 'yellow.css');
		
		//menu reset:
        $('#wrapper').removeClass();
        $('#wrapper').addClass('container');
        $('.menu-position li a').parent().parent().find('a').removeClass('active');
        $('#menu-left-bottom').addClass('active');
        //$.cookie('layout_menuPosition', '');
		
		//container-height reset:
        $('#container-height-fixed').trigger('click');
    });
});
