function initialize() {

    // Multiple Markers
    var markers = [
        ['Memorial Park, Madison', 40.756945,-74.397612],
        ['Drew University, 36 Madison Ave', 40.762926,-74.422159],
        ['Brooklake Country Club, 139 Brooklake Rd', 40.765461,-74.378471]
    ];
                        
    // Info Window Content
    var infoWindowContent = [
        ['<div class="info_content">' +
        '<h3>London Eye</h3>' +
        '<p>The London Eye is a giant Ferris wheel situated on the banks of the River Thames. The entire structure is 135 metres (443 ft) tall and the wheel has a diameter of 120 metres (394 ft).</p>' +        '</div>'],
        ['<div class="info_content">' +
        '<h3>Palace of Westminster</h3>' +
        '<p>The Palace of Westminster is the meeting place of the House of Commons and the House of Lords, the two houses of the Parliament of the United Kingdom. Commonly known as the Houses of Parliament after its tenants.</p>' +
        '</div>'],
        ['<div class="info_content">' +
        '<h3>Westminster Bridge</h3>' +
        '<p>The Palace of Westminster is the meeting place of the House of Commons and the House of Lords, the two houses of the Parliament of the United Kingdom. Commonly known as the Houses of Parliament after its tenants.</p>' +
        '</div>']
    ];

    // Multiple Pointers with Different Colors
    var mapPointers = [
        ['images/map-marker.png'],
        ['images/map-marker2.png'],
        ['images/map-marker3.png']
    ];



    var map;
    var bounds = new google.maps.LatLngBounds();

    var current_item = jQuery("#map-canvas-multipointer");
    var map_address = current_item.data('address');
    var map_latlng = current_item.data('latlng');
    var map_zoom = current_item.data('zoom');
    var map_style = current_item.data('mapstyle');
    var map_title = current_item.data('title');
    var map_descr = jQuery(current_item.data("popupstring-id")).html();
    var map_pointer = current_item.data('marker');

    var mapOptions = {
        scrollwheel: false,
        scaleControl: true,
        disableDefaultUI: true,
        panControl: true,
        zoomControl: true, //zoom
        mapTypeControl: true,
        streetViewControl: true,
        overviewMapControl: true,
        styles: THEMEMASCOT_googlemap_styles[map_style ? map_style : 'default'],
        mapTypeId: 'roadmap'
    };
                    
    // Display a map on the page
    map = new google.maps.Map(current_item.get(0), mapOptions);
    map.setTilt(45);

    // Display multiple markers on a map
    var infoWindow = new google.maps.InfoWindow(), marker, i;
    
    // Loop through our array of markers & place each one on the map  
    for( i = 0; i < markers.length; i++ ) {
        var position = new google.maps.LatLng(markers[i][1], markers[i][2]);
        bounds.extend(position);
        marker = new google.maps.Marker({
            position: position,
            map: map,
            title: markers[i][0],
            icon: mapPointers[i][0]
        });

        // Allow each marker to have an info window    
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infoWindow.setContent(infoWindowContent[i][0]);
                infoWindow.open(map, marker);
            }
        })(marker, i));

        // Automatically center the map fitting all markers on the screen
        map.fitBounds(bounds);
    }

    // Override our map zoom level once our fitBounds function runs (Make sure it only runs once)
    var boundsListener = google.maps.event.addListener((map), 'bounds_changed', function(event) {
        this.setZoom(map_zoom);
        google.maps.event.removeListener(boundsListener);
    });
    
}

var THEMEMASCOT_googlemap_init_obj = {};
var THEMEMASCOT_GEOCODE_ERROR = "Error";
// Google map Styles
var THEMEMASCOT_googlemap_styles = {
    'default': [],
    'style1':  [{"featureType":"administrative","elementType":"labels.text.fill","stylers":[{"color":"#444444"}]},{"featureType":"landscape","elementType":"all","stylers":[{"color":"#f2f2f2"}]},{"featureType":"landscape.man_made","elementType":"geometry.fill","stylers":[{"color":"#eeeeee"}]},{"featureType":"landscape.natural.landcover","elementType":"geometry.fill","stylers":[{"color":"#dddddd"}]},{"featureType":"landscape.natural.terrain","elementType":"geometry.fill","stylers":[{"color":"#dddddd"}]},{"featureType":"poi","elementType":"all","stylers":[{"visibility":"off"}]},{"featureType":"road","elementType":"all","stylers":[{"saturation":-100},{"lightness":45}]},{"featureType":"road.highway","elementType":"all","stylers":[{"visibility":"simplified"}]},{"featureType":"road.arterial","elementType":"labels.icon","stylers":[{"visibility":"off"}]},{"featureType":"transit","elementType":"all","stylers":[{"visibility":"off"}]},{"featureType":"water","elementType":"all","stylers":[{"color":"#46bcec"},{"visibility":"on"}]},{"featureType":"water","elementType":"geometry.fill","stylers":[{"color":"#00BBD1"}]},{"featureType":"water","elementType":"labels.text.fill","stylers":[{"color":"#979797"}]},{"featureType":"water","elementType":"labels.text.stroke","stylers":[{"weight":"0.01"}]}],
                            
    'style2': [{"featureType": "landscape","stylers": [{"hue": "#007FFF"},{"saturation": 100},{"lightness": 156},{"gamma": 1}]},{"featureType": "road.highway","stylers": [{"hue": "#FF7000"},{"saturation": -83.6},{"lightness": 48.80000000000001},{"gamma": 1}]},{"featureType": "road.arterial","stylers": [{"hue": "#FF7000"},{"saturation": -81.08108108108107},{"lightness": -6.8392156862745},{"gamma": 1}]},{"featureType": "road.local","stylers": [{"hue": "#FF9A00"},{"saturation": 7.692307692307736},{"lightness": 21.411764705882348},{"gamma": 1}]},{"featureType": "water","stylers": [{"hue": "#0093FF"},{"saturation": 16.39999999999999},{"lightness": -6.400000000000006},{"gamma": 1}]},{"featureType": "poi","stylers": [{"hue": "#00FF60"},{"saturation": 17},{"lightness": 44.599999999999994},{"gamma": 1}]}],

    'style3': [{stylers: [{ hue: "#00ffe6" },{ saturation: -20 }]},{featureType: "road",elementType: "geometry",stylers: [{ lightness: 100 },{ visibility: "simplified" }]},{featureType: "road",elementType: "labels",stylers: [{ visibility: "off" }]}],

    'style4': [{"stylers": [{ "saturation": -100 }]}],

    'style5': [{"featureType": "landscape","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 20.4705882352941 },{ "gamma": 1 }]},{"featureType": "road.highway","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 25.59999999999998 },{ "gamma": 1 }]},{"featureType": "road.arterial","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": -22 },{ "gamma": 1 }]},{"featureType": "road.local","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 21.411764705882348 },{ "gamma": 1 }]},{"featureType": "water","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 21.411764705882348 },{ "gamma": 1 }]},{"featureType": "poi","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 4.941176470588232 },{ "gamma": 1 }]}],

    'style6': [{"featureType": "landscape","stylers": [{ "hue": "#FF0300"  },{ "saturation": -100 },{ "lightness": 20.4705882352941 },{ "gamma": 1 }]},{"featureType": "road.highway","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 25.59999999999998 },{ "gamma": 1 }]},{"featureType": "road.arterial","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": -22 },{ "gamma": 1 }]},{"featureType": "road.local","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 21.411764705882348 },{ "gamma": 1 }]},{"featureType": "water","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 21.411764705882348 },{ "gamma": 1 }]},{"featureType": "poi","stylers": [{ "hue": "#FF0300" },{ "saturation": -100 },{ "lightness": 4.941176470588232 },{ "gamma": 1 }]}],

    'style7': [{"featureType":"landscape","stylers":[{ "invert_lightness": true },{"saturation":-100},{"lightness":65},{"visibility":"on"}]},{"featureType":"poi","stylers":[{"saturation":-100},{"lightness":51},{"visibility":"simplified"}]},{"featureType":"road.highway","stylers":[{"saturation":-100},{"visibility":"simplified"}]},{"featureType":"road.arterial","stylers":[{"saturation":-100},{"lightness":30},{"visibility":"on"}]},{"featureType":"road.local","stylers":[{"saturation":-100},{"lightness":40},{"visibility":"on"}]},{"featureType":"transit","stylers":[{"saturation":-100},{"visibility":"simplified"}]},{"featureType":"administrative.province","stylers":[{"visibility":"off"}]},{"featureType":"water","elementType":"labels","stylers":[{"visibility":"on"},{"lightness":-25},{"saturation":-100}]},{"featureType":"water","elementType":"geometry","stylers":[{"hue":"#ffff00"},{"lightness":-25},{"saturation":-97}]}],
                
    'style8':  [{"featureType": "landscape","stylers": [{"hue": "#FFA800"},{"saturation": 17.799999999999997},{"lightness": 152.20000000000002},{"gamma": 1}]},{"featureType": "road.highway","stylers": [{"hue": "#007FFF"},{"saturation": -77.41935483870967},{"lightness": 47.19999999999999},{"gamma": 1}]},{"featureType": "road.arterial","stylers": [{"hue": "#FBFF00"},{"saturation": -78},{"lightness": 39.19999999999999},{"gamma": 1}]},{"featureType": "road.local","stylers": [{"hue": "#00FFFD"},{"saturation": 0},{"lightness": 0},{"gamma": 1}]},{"featureType": "water","stylers": [{"hue": "#007FFF"},{"saturation": -77.41935483870967},{"lightness": -14.599999999999994},{"gamma": 1}]},{"featureType": "poi","stylers": [{"hue": "#007FFF"},{"saturation": -77.41935483870967},{"lightness": 42.79999999999998},{"gamma": 1}]}],

    'style9': [{"featureType": "water","elementType": "geometry.fill","stylers": [{"color": "#A3C6FE"}]}, {"featureType": "transit","stylers": [{"color": "#b3C6FE"}, {"visibility": "off"}]}, {"featureType": "road.highway","elementType": "geometry.stroke","stylers": [{"visibility": "on"}, {"color": "#EBCE7B"}]}, {"featureType": "road.highway","elementType": "geometry.fill","stylers": [{"color": "#ffffff"}]}, {"featureType": "road.local","elementType": "geometry.fill","stylers": [{"visibility": "on"}, {"color": "#ffffff"}, {"weight": 1.8}]}, {"featureType": "road.local","elementType": "geometry.stroke","stylers": [{"color": "#d7d7d7"}]}, {"featureType": "poi","elementType": "geometry.fill","stylers": [{"visibility": "on"}, {"color": "#ebebeb"}]}, {"featureType": "administrative","elementType": "geometry","stylers": [{"color": "#a7a7a7"}]}, {"featureType": "road.arterial","elementType": "geometry.fill","stylers": [{"color": "#ffffff"}]}, {"featureType": "road.arterial","elementType": "geometry.fill","stylers": [{"color": "#ffffff"}]}, {"featureType": "landscape","elementType": "geometry.fill","stylers": [{"visibility": "on"}, {"color": "#E9E5DC"}]}, {"featureType": "road","elementType": "labels.text.fill","stylers": [{"color": "#696969"}]}, {"featureType": "administrative","elementType": "labels.text.fill","stylers": [{"visibility": "on"}, {"color": "#737373"}]}, {"featureType": "poi","elementType": "labels.icon","stylers": [{"visibility": "off"}]}, {"featureType": "poi","elementType": "labels","stylers": [{"visibility": "off"}]}, {"featureType": "road.arterial","elementType": "geometry.stroke","stylers": [{"color": "#d6d6d6"}]}, {"featureType": "road","elementType": "labels.icon","stylers": [{"visibility": "off"}]}, {"featureType": "poi","elementType": "geometry.fill","stylers": [{"color": "#dadada"}]}],

    'dark': [{"featureType": "landscape","stylers": [{"invert_lightness": true}, {"saturation": -100}, {"lightness": 65}, {"visibility": "on"}]}, {"featureType": "poi","stylers": [{"saturation": -100}, {"lightness": 51}, {"visibility": "simplified"}]}, {"featureType": "road.highway","stylers": [{"saturation": -100}, {"visibility": "simplified"}]}, {"featureType": "road.arterial","stylers": [{"saturation": -100}, {"lightness": 30}, {"visibility": "on"}]}, {"featureType": "road.local","stylers": [{"saturation": -100}, {"lightness": 40}, {"visibility": "on"}]}, {"featureType": "transit","stylers": [{"saturation": -100}, {"visibility": "simplified"}]}, {"featureType": "administrative.province","stylers": [{"visibility": "off"}]}, {"featureType": "water","elementType": "labels","stylers": [{"visibility": "on"}, {"lightness": -25}, {"saturation": -100}]}, {"featureType": "water","elementType": "geometry","stylers": [{"hue": "#ffff00"}, {"lightness": -25}, {"saturation": -97}]}],
    'greyscale1': [{"stylers": [{"saturation": -100}]}],
    'greyscale2': [{"featureType": "landscape","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": 20.4705882352941}, {"gamma": 1}]}, {"featureType": "road.highway","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": 25.59999999999998}, {"gamma": 1}]}, {"featureType": "road.arterial","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": -22}, {"gamma": 1}]}, {"featureType": "road.local","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": 21.411764705882348}, {"gamma": 1}]}, {"featureType": "water","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": 21.411764705882348}, {"gamma": 1}]}, {"featureType": "poi","stylers": [{"hue": "#FF0300"}, {"saturation": -100}, {"lightness": 4.941176470588232}, {"gamma": 1}]}]
};

jQuery(function($) {
    "use strict";
    initialize();
});