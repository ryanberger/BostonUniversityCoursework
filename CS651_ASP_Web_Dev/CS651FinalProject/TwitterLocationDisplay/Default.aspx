<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head runat="server">
    <title>Twitter Location Aggregator</title>
    <style type="text/css">
        #map_canvas
        {
            width: 100%;
            height: 500px;
        }
    </style>

    <script src="Resources/json_parse.js" type="text/javascript"></script>

    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAA5AlDqT7PH6Uf3_0anPBLlBT2yXp_ZAY8_ufC3CFXhHIE1NvwkxR2Yxhaq3J3vRY8c840lKLCZFXxew&sensor=false"
        type="text/javascript"></script>

    <script type="text/javascript">
        // XMLHttpRequest
        var ajaxRequest;

        // Google Map
        var map;

        // Array of markers and array of display text
        var gmarkers = [];
        var markerText = [];
        var index = -1;

        // initAJAX
        function initAjax() {
            try {
                ajaxRequest = new XMLHttpRequest();
                getCoordinates();
            }
            catch (Error) {
                // IE 4 to IE 6
                ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
                getCoordinates();
            }
        }
        // input
        function getCoordinates() {
            initializeMap();
            var location = document.getElementById("customLocation").value;
            
            if (location == "") {
                location = "Default";
            }
            
            var theURL = "Default.aspx?Location=" + location;
            ajaxRequest.open("GET", theURL);
            ajaxRequest.onreadystatechange = fillMap;
            ajaxRequest.send();
        }

        function initializeMap() {
            if (GBrowserIsCompatible()) {
                map = new GMap2(document.getElementById("map_canvas"));
                map.setMapType(G_PHYSICAL_MAP);
                map.setCenter(new GLatLng(25.4419, -30.1419), 3);
                map.setUIToDefault();
            }
        }

        function fillMap() {
            
            if (ajaxRequest.readyState == 4) {
                var strJson = ajaxRequest.responseText;
                strJson = strJson.substring(strJson.indexOf('['), strJson.indexOf(']') + 1);
                
                if (strJson != "") {
                    
                    var result = json_parse(strJson);

                    for (var i = 0; i < 10; i++) {
                        var displayText = "<b>" + result[i].Name + "</b>" +
                            "<br /><a href=\"http://search.twitter.com/search?q=" + result[i].Query1 + "\" target=\"_blank\">" + result[i].Trend1 + "</a>" +
                            "<br /><a href=\"http://search.twitter.com/search?q=" + result[i].Query2 + "\" target=\"_blank\">" + result[i].Trend2 + "</a>" +
                            "<br /><a href=\"http://search.twitter.com/search?q=" + result[i].Query3 + "\" target=\"_blank\">" + result[i].Trend3 + "</a>";
                        map.addOverlay(createMarker(new GLatLng(parseFloat(result[i].Latitude), parseFloat(result[i].Longitude)), i, displayText));
                    }
                }
            }
        }

        // Create a base icon for all of our markers that specifies the
        // shadow, icon dimensions, etc.
        var baseIcon = new GIcon(G_DEFAULT_ICON);
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(20, 34);
        baseIcon.shadowSize = new GSize(37, 34);
        baseIcon.iconAnchor = new GPoint(9, 34);
        baseIcon.infoWindowAnchor = new GPoint(9, 2);

        // Creates a marker whose info window displays the letter corresponding
        // to the given index.
        function createMarker(point, index, displayText) {
            // Create a lettered icon for this point using our icon class
            var letter = String.fromCharCode("A".charCodeAt(0) + index);
            var letteredIcon = new GIcon(baseIcon);
            letteredIcon.image = "http://www.google.com/mapfiles/marker" + letter + ".png";

            // Set up our GMarkerOptions object
            markerOptions = { icon: letteredIcon };
            var marker = new GMarker(point, markerOptions);

            GEvent.addListener(marker, "click", function() {
                marker.openInfoWindowHtml(displayText);
            });
            gmarkers[index] = marker;
            markerText[index] = displayText;
            return marker;
        }

        function nextMarker() {
            index++;
            if (index > 9) {
                index = 0;
            }
            gmarkers[index].openInfoWindowHtml(markerText[index]);
        }

        function prevMarker() {
            index--;
            if (index < 0) {
                index = 9;
            }
            gmarkers[index].openInfoWindowHtml(markerText[index]);
        }
    </script>

</head>
<body onunload="GUnload();">
    <div>
        Enter in desired location for trends (leave blank for top 10 places in the world):
        <input type="text" id="customLocation" value="" />
        <input type="button" id="setLocation" value="Set Location" onclick="initAjax();" />
        <input type="button" id="prevMarker" value="Previous marker" onclick="prevMarker();" />
        <input type="button" id="nextMarker" value="Next marker" onclick="nextMarker();" />
    </div>
    <div id="map_canvas" />
</body>
</html>