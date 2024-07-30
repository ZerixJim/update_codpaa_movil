<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map-canvas { width: 500px; height: 500px; }
    </style>
      <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBjwBcplUTpmueueRZnWmnrmDd6ErF39pU"
              type="text/javascript"></script>
      <script type="text/javascript">
	
	
    
      function initialize() {
		  
        	var mapOptions = {
          		center: new google.maps.LatLng(20.66895389774733 , -103.40225729761116),
          		zoom: 17,
          		mapTypeId: google.maps.MapTypeId.ROADMAP,
				size:new GSize(900,248)
        	};
		
        	var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);
			
			var marker = new google.maps.Marker({
            	position: new google.maps.LatLng(20.66895389774733 , -103.40225729761116),
            	map: map
      		});
			
			

		
      }
	  
      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
  </head>
  <body>
    <div id="map-canvas"/>
  </body>
</html>