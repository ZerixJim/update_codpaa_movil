var url;
var inde;
var markerGeneral;
var markerTiendas;
var markerRas;
var mapa;



function newUser(){
	$('#dlg').dialog('open').dialog('setTitle','Nuevo Promotor');
	$('#fm').form('clear');
	url = 'php/save_user.php';
}

function newSuper(){
	$('#dlgSuper').dialog('open').dialog('setTitle','Nuevo Supervisor');
	$('#fmSuper').form('clear');
	url = 'php/save_super.php';
}

function importCSV(){
	$('#dlgImport').dialog('open').dialog('setTitle','Importar CSV');
}
	
function editUser(){
	var row = $('#dg').datagrid('getSelected');
		
	if (row){
		$('#dlg').dialog('open').dialog('setTitle','Editar promotor');
			
		$('#fm').form('load',row);
		url = 'php/edit_promo.php?id='+row.idCelular;
			
	}
}
	
function editSuper(){
	var row = $('#dgSuper').datagrid('getSelected');
		
	if (row){
		$('#dlgSuper').dialog('open').dialog('setTitle','Editar Supervisor');
			
		$('#fmSuper').form('load',row);
		url = 'php/edit_super.php?id='+row.idSupervisores;
			
	}
}
	
function saveUser(){
	$('#fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.success){
				$('#dlg').dialog('close');		
				$('#dg').datagrid('reload');	
			} else {
				$.messager.show({
					title: 'Error',
					msg: result.msg
				});
			}
		}
	});
}
	
	
	
	
function removeUser(){
	var row = $('#dg').datagrid('getSelected');
	if (row){
		$.messager.confirm('Confirmacion','¿Estas seguro que quieres eliminar a '+row.nombre+'?',function(r){
			if (r){
				$.post('php/remove_user.php',{id:row.idCelular},function(result){
					if (result.success){
						$('#dg').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({	// show error message
							title: 'Error',
							msg: result.msg
						});
					}
				},'json');
			}
		});
	}
}
    
$(function(){
	$('#dgrastreo').datagrid({
		onClickRow: function(rowIndex, rowData){
			
			var setFecha = $('#chDate').datebox('getValue');
			
			$.ajax({
				url: 'php/getVisitas.php',
				data: {id: rowData.idCelular,fecha: setFecha},
				type: 'GET',
				dataType: 'json',
				success: function(json){
					if(json.length > 0){
						iniciarMapa(json);
					}else{
						
						if(markerGeneral != null){
							markerGeneral.setMap(null);
							
							mapa.setZoom(5);
							mapa.setCenter(new google.maps.LatLng(22.35006135229113,-101.55759218749995));
						}
						
						alert("No existen registros de este Promotor en la fecha: "+setFecha);
						console.log('no hay registros');
					}
					
				},
				error: function(jqXHR,status,error){
					console.log("error, getVisitas");
					
				},
				complete: function(jqXHR, status){
					console.log("peticion getVisitas terminada");
				}
			});
			

			
		}
	});
	
	$('#chDate').datebox({
		
		value: fecha,
		formatter : function(date){
			var d = date.getDate();
			var m = date.getMonth()+1;
			var y = date.getFullYear(); 
			
			if(d < 10){
				d = "0" + d;
			}
			
			if(m < 10){
				m = "0" + m;
			}
			
        	return d+'-'+m+'-'+y;
    	},
		fecha: function(){
			var d = date.getDate();
			var m = date.getMonth()+1;
			var y = date.getFullYear(); 
        	return d+'-'+m+'-'+y;
		}
	
	});

	
	
});

function fecha(){
	
	var formattedDate = new Date();
	var d = formattedDate.getDate();
	var m =  formattedDate.getMonth();
	m += 1;  // JavaScript months are 0-11
	var y = formattedDate.getFullYear();
			
	if(d < 10){
		d = "0" + d;
	}
			
	if(m < 10){
		m = "0" + m;
	}
			

	return d+"-"+m+"-"+y;
}

function initialize() {
    var mapOptions = {
        center: new google.maps.LatLng(20.66895389774733 , -103.40225729761116),
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP		
    };
		
    var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);
	var summa = 'imagenes/tiendas/summa.gif';
	var marker = new google.maps.Marker({
        position: new google.maps.LatLng(20.66895389774733 , -103.40225729761116),
        map: map,
		icon: summa
    });

		
}


function setMarkers(map, loc, imagen) {

	
  	for (var i = 0; i < loc.length; i++) {
    	var localiza = loc[i];
    	var myLatLng = new google.maps.LatLng(localiza.latitud, localiza.longitud);
    	var markerGeneral = new google.maps.Marker({
        	position: myLatLng,
        	map: map,
       	 	icon: imagen,
        	title: localiza.hora,
        	zIndex: localiza.idTiendasVisitadas
    	});
  	}
}



function addMarkadorRas(rastreo){
	for (var i = 0; i < rastreo.length; i++){
		var ras = rastreo[i];
		
		var titulo = ras.hora;
		
		var myLatLng = new google.maps.LatLng(ras.latitud, ras.longitud);
		if(myLatLng != null){
			markerRas = new google.maps.Marker({
				position: myLatLng,
				map: mapa,
				icon: 'imagenes/rastreo/gps.png',
				title: titulo
        	
			});
		}
    	

  	}
}

function rastreoDia(){
	var row = $('#dgrastreo').datagrid('getSelected');
	var setFechaR = $('#chDate').datebox('getValue');
	$.ajax({
		
		url: 'php/get_rastreo.php',
		data: {id: row.idCelular,fecha: setFechaR},
		type: 'GET',
		dataType: 'json',
		success: function(json){
			
			if(json.length > 0){
				addMarkadorRas(json);
			}else{
				console.log('no hay registros rastreo');
			}
					
		},
		error: function(jqXHR,status,error){
			console.log("error, getRastreo");
					
		},
		complete: function(jqXHR, status){
			console.log("peticion getRastreo terminada");
		}
	});
	
}

function addMarkador(tiendas){
	
	for (var i = 0; i < tiendas.length; i++){
    	var tien = tiendas[i];
		
		var titulo = tien.grupo+" "+tien.sucursal;
		
    	var myLatLng = new google.maps.LatLng(tien.x, tien.y);
		if(myLatLng != null){
			markerTiendas = new google.maps.Marker({
        		position: myLatLng,
        		map: mapa,
        		icon: 'imagenes/rastreo/tienda.png',
        		title: titulo
        	
    		});
		}
    	

  	}
	
}


function iniciarMapa(visitas) {
	console.log("script" +  visitas.length);
  	var mapOptions = {
    	zoom: 5,
		center: new google.maps.LatLng(22.35006135229113,-101.55759218749995),
    	mapTypeId: google.maps.MapTypeId.ROADMAP,
		styles: [
			{ elementType: "geometry", stylers: [{ color: "#242f3e" }] },
			{ elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
			{ elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
			{
				featureType: "administrative.locality",
				elementType: "labels.text.fill",
				stylers: [{ color: "#d59563" }]
			},
			{
				featureType: "poi",
				elementType: "labels.text.fill",
				stylers: [{ color: "#d59563" }]
			},
			{
				featureType: "poi.park",
				elementType: "geometry",
				stylers: [{ color: "#263c3f" }]
			},
			{
				featureType: "poi.park",
				elementType: "labels.text.fill",
				stylers: [{ color: "#6b9a76" }]
			},
			{
				featureType: "road",
				elementType: "geometry",
				stylers: [{ color: "#38414e" }]
			},
			{
				featureType: "road",
				elementType: "geometry.stroke",
				stylers: [{ color: "#212a37" }]
			},
			{
				featureType: "road",
				elementType: "labels.text.fill",
				stylers: [{ color: "#9ca5b3" }]
			},
			{
				featureType: "road.highway",
				elementType: "geometry",
				stylers: [{ color: "#746855" }]
			},
			{
				featureType: "road.highway",
				elementType: "geometry.stroke",
				stylers: [{ color: "#1f2835" }]
			},
			{
				featureType: "road.highway",
				elementType: "labels.text.fill",
				stylers: [{ color: "#f3d19c" }]
			},
			{
				featureType: "transit",
				elementType: "geometry",
				stylers: [{ color: "#2f3948" }]
			},
			{
				featureType: "transit.station",
				elementType: "labels.text.fill",
				stylers: [{ color: "#d59563" }]
			},
			{
				featureType: "water",
				elementType: "geometry",
				stylers: [{ color: "#17263c" }]
			},
			{
				featureType: "water",
				elementType: "labels.text.fill",
				stylers: [{ color: "#515c6d" }]
			},
			{
				featureType: "water",
				elementType: "labels.text.stroke",
				stylers: [{ color: "#17263c" }]
			}
		]
 	};
  	mapa = new google.maps.Map(document.getElementById("Mapa-rastreo"),mapOptions);
								
	
	var point;
	var ruta = 'imagenes/rastreo/';
	

    for (var i = 0; i < visitas.length; i++){
    	var vis = visitas[i];
		if(vis.tipo == 'E'){
			point =  ruta + 'entrada.png';
		}else if(vis.tipo == 'S'){
			point = ruta + 'salida.png';
		}
		
		var titulo = vis.hora+" "+vis.sucursal;
		var sucur = vis.sucursal;

		var myLatLngTiendas = new google.maps.LatLng(vis.x, vis.y);
        var myLatLng = new google.maps.LatLng(vis.latitud, vis.longitud);
		if(myLatLng != null){

			/*if(vis.x!=null && vis.y!=null)
			{
				sucur = sucur +"distancia: "+distancia(vis.x,vis.y,vis.latitud,vis.longitud);
			}*/
			markerTiendas = new google.maps.Marker({
        		position: myLatLngTiendas,
        		map: mapa,
        		icon: 'imagenes/rastreo/tienda.png',
        		title: sucur
        	
    		});
		}



    	markerGeneral = new google.maps.Marker({
        	position: myLatLng,
        	map: mapa,
        	icon: point,
        	title: titulo
        	
    	});
		
		google.maps.event.addListener(markerGeneral, 'click', function() {
    		mapa.setZoom(16);
    		mapa.setCenter(markerGeneral.getPosition());
  		});
		

  	}
	
	/*google.maps.event.addListener(mapa, 'center_changed', function() {
    	// 3 seconds after the center of the map has changed, pan back to the
    	// marker.
    	window.setTimeout(function() {
      		mapa.panTo(marker.getPosition());
    	}, 3000);
  	});*/
	
	
	
  	
}


function makerPosition(){
	
	var row = $('#dgTiendas').datagrid('getSelected');
	
	var mapOptions = {
        center: new google.maps.LatLng(row.x , row.y),
        zoom: 17,
        mapTypeId: google.maps.MapTypeId.ROADMAP		
    };
	
	var map = new google.maps.Map(document.getElementById("map-canvas"),mapOptions);
	
	
	var imagen;
	var ruta = 'imagenes/tiendas/';
	var titulo = row.cadena +' - '+ row.sucursal;
	if(row.cadena == 'CHEDRAUI'){
		imagen = ruta+'chedaui.gif';
	}else if(row.cadena == 'FARMACIAS GUADALAJARA'){
		imagen = ruta+'farmg.gif';
	}else if(row.cadena == 'CASA LEY'){
		imagen = ruta+'ley.gif';
	}
			
	var marker = new google.maps.Marker({
		position: new google.maps.LatLng(row.x , row.y),
		map: map,
		icon: imagen,
		title: titulo
		
	});
	
	
}



function mapa(){
	$('#map-canvas').toggle('show',initialize);
}


function ImpotarCsvTiendas(){
	$('#fmCsvTiendas').form('submit',{
		url: 'php/inport_csv.php'
		
	});
}



function borrarMarkadores(){
	if(markerGeneral != null){
		markerGeneral.setMap(null);
		mapa.setZoom(5);
		mapa.setCenter(new google.maps.LatLng(22.35006135229113,-101.55759218749995));
		
	}
	
}

function distancia(lat1,lon1,lat2,lon2){
	var R = 6378;
	var dLat = (lat2-lat1).toRad();
	var dLon = (lon2-lon1).toRad();
	var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
			Math.cos(lat1.toRad()) * Math.cos(lat2.toRad()) *
			Math.sin(dLon/2) * Math.sin(dLon/2);
	var c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
	var d = R * c;

	return d;
}







