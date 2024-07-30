<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 22/08/2018
 * Time: 04:58 PM
 */


session_start();
if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    ?>


    <div id="ruteo" class="content-padding">

        <a href="#" class="easyui-linkbutton" onclick="getGeoPoints()">iniciar</a>
        <a href="#" class="easyui-linkbutton" onclick="findingStores()">Buscar Tiendas</a>
        <a href="#" class="easyui-linkbutton" onclick="exportar()">Export</a>


        <br/>

        <table id="ruteo2" style="width: 800px;height: 500px;"></table>


        <div id="router-map" style="width: 90%;height: 90%;margin: 0 auto;"></div>

        <div id="container"></div>


    </div>



    <script>

        let mapaRuteo;


        let requestArray2 = [], renderArray2 = [];

        let directionsService2 = new google.maps.DirectionsService();


        //var requestArray = [];


        function exportar(){

            let rows = $('#ruteo2').datagrid('getRows');

            exportarExcel(rows);



        }


        function startRouterMap(geopuntos) {

            //console.log(geopuntos);

            /*requestArray = [];
            markersChecks = [];*/


            let centerLocation;

            let mapOptions = {

                zoom: 5,

                center: new google.maps.LatLng(22.35006135229113, -101.55759218749995),

                mapTypeId: google.maps.MapTypeId.ROADMAP

            };


            mapaRuteo = new google.maps.Map(document.getElementById('router-map'), mapOptions);


            let total = geopuntos.length;

            for (let i = 0; i < total; i++) {


                let geo = geopuntos[i];

                let startLocation = new google.maps.LatLng(geo.x, geo.y);

                //console.log(myLatLon);


                let marker = new google.maps.Marker({
                    position: startLocation,
                    title: geo.grupo + ' ' + geo.sucursal + ' ' + geo.idTienda,
                    map: mapaRuteo,
                    icon: '../../images/formatos/' + geo.idFormato + '.png'
                });

                if (geo.tiendas.length > 0) {

                    let waypoinst = [];
                    let total2 = geo.tiendas.length;

                    let tiendasCercanas = geo.tiendas;

                    waypoinst.push({
                        location: startLocation,
                        stopover: true
                    });


                    for (let j = 0; j < total2; j++) {

                        let entPoint = new google.maps.LatLng(tiendasCercanas[j].x, tiendasCercanas[j].y);


                        //console.log(geo.x + ' ' + tiendasCercanas.x );
                        waypoinst.push({
                            location: entPoint,
                            stopover: true
                        });


                    }

                    setTimeout(function () {

                        console.log('paso');

                    }, 1000);

                    generateRequests2(waypoinst);


                }


            }


            /*var point;

            var ent_ini = 0, ent_fin = 0;

            var ent_points = [];

            var ruta = '../imagenes/rastreo/';

            var listadoTiendas = $('<ul>');*/

///*******Recorre los puntos registrados
            /*for (var i = 0; i < visitas.length; i++) {

                var vis = visitas[i];

                if (vis.tipo === 'E') {

                    point = ruta + 'entrada_mark.png';

                } else if (vis.tipo === 'S') {

                    point = ruta + 'salida_mark.png';

                }


                var titulo = vis.hora + " " + vis.sucursal;

                var sucur = vis.sucursal;

                //!***********************
                var msj = '<div id="content">' +
                    '<h3 id="firstHeading" class="firstHeading">Sucursal</h3>' +
                    '<div id="bodyContent">' +
                    '<p>' + vis.grupo + '</p>' +
                    '<p>' + vis.sucursal + '</p>' +
                    '</div>' +
                    '</div>';


                var myLatLngTiendas = new google.maps.LatLng(vis.x, vis.y);


                var myLatLng = new google.maps.LatLng(vis.latitud, vis.longitud);

                if ( i == 0){


                    centerLocation = myLatLng;
                }

                //!*****Si el registro es una entrada
                if (vis.tipo === 'E') {
                    //!****Si es la primera
                    if (ent_ini === 0) {
                        ent_ini = myLatLng;

                        ent_points.push({
                            location: myLatLng,
                            stopover: true
                        });

                    } else {
                        ent_points.push({
                            location: myLatLng,
                            stopover: true
                        });


                        ent_fin = myLatLng;
                    }
                }



                markersMap.push(markerGeneral);
                markersChecks.push(markerGeneral);



                var tipoCheck = vis.tipo === 'E' ? 'Entrada' : 'Salida';

                var distancia = distanciaEntreDosPuntos(vis.x,vis.y,vis.latitud, vis.longitud);

                var prettyDistance = distancia >= 1 ? distancia.toFixed(2) + 'km' : (distancia * 1000.0).toFixed(2) + 'm';

                var dateTime = vis.fecha_captura.split(' ');



                var color = distancia >= 0.5 ? '#f46542': '#1c9cff';

                var row = $('<li>').css('color', color );

                var imgPoint = $('<img>').attr('src', point).css('margin', '5px').css({'width':'15px'});

                row.html('<img src="../images/formatos/'+ vis.idFormato +'.png">' + vis.sucursal).append(imgPoint);



                var spanDistancia = $('<span>').css('color', '#8d8d8d').html(' a ' + prettyDistance + ' ' + dateTime[1]);


                row.append(spanDistancia);


                listadoTiendas.append(row);


                var msj2 = '<div id="content">' +
                    '<h3 id="firstHeading" class="firstHeading">' + tipoCheck + '</h3>' +
                    '<div id="bodyContent">' +
                    '<p>' + vis.hora + '</p>' +
                    '<p>' + vis.sucursal + '</p>' +
                    '<p>' +  prettyDistance +'</p>' +
                    '</div>' +
                    '</div>';

                messageStore(markerGeneral, msj2);


                if (myLatLng !== null) {



                    /!* var circle = new google.maps.Circle({

                         center: myLatLngTiendas,
                         strokeColor: color,
                         radius: 500,
                         fillColor: color,
                         fillOpacity: 0.15,
                         map: mapa

                     });*!/




                }



                /!*google.maps.event.addListener(markerGeneral, 'click', function() {

                 mapa.setZoom(16);

                 mapa.setCenter(markerGeneral.getPosition());

                 });*!/

            }*/ ///*****Final del recorrido de Puntos

            /*mapa.setCenter(centerLocation);
            mapa.setZoom(14);*/


            /*$('#ruta-entradas').html(listadoTiendas);
            $('#ruta-entradas').slideDown('fast');

            $('#ruta-entradas').find('ul').find('li').click(function(){

                var index = $(this).index();
                var vis = visitas[index];

                var latLon = new google.maps.LatLng(vis.latitud, vis.longitud);

                mapa.panTo(latLon);

                var zoom = mapa.getZoom();
                if(zoom !== 14){

                    mapa.setZoom(14);
                }

                var markerAnimation = markersChecks[index];


                markerAnimation.setAnimation(google.maps.Animation.BOUNCE);

                setTimeout(function () {
                    markerAnimation.setAnimation(null);
                }, 2000);




                //console.log(vis.idTienda);


            });*/


            /*
            if (ent_fin !== 0) {
                ///!****Saca el ultimo de la ruta
                //ent_points.pop();
                //!****dibuja la ruta completa con los diferentes puntos
                //drawRoute(ent_ini,ent_fin,ent_points);

                generateRequests(ent_points);

            }*/
        }

        function generateRequests2(waypoints) {


            // Somewhere to store the wayoints
            let waypts = waypoints;

            // 'start' and 'finish' will be the routes origin and destination
            let start, finish;

            // Grab the first waypoint for the 'start' location
            start = (waypts.shift()).location;
            // Grab the last waypoint for use as a 'finish' location
            finish = waypts.pop();
            if (finish === undefined) {
                // Unless there was no finish location for some reason?
                finish = start;
            } else {
                finish = finish.location;
            }

            // Let's create the Google Maps request object
            let request = {
                origin: start,
                destination: finish,
                waypoints: waypts,
                travelMode: google.maps.TravelMode.WALKING
            };

            // and save it in our requestArray
            requestArray2.push({"request": request});


            processRequests2();
        }

        function processRequests2() {


            let i = 0;

            // Counter to track request submission and process one at a time;

            let total = requestArray2.length;

            while (i < total) {

                directionsService2.route(requestArray2[i].request, directionResults);
                let color;


                if (i % 2 === 0) {

                    color = colourArray[0];

                } else {

                    color = colourArray[1];
                }

                renderArray2[i] = new google.maps.DirectionsRenderer();
                renderArray2[i].setMap(mapaRuteo);

                // Some unique options from the colorArray so we can see the routes
                renderArray2[i].setOptions({
                    preserveViewport: true,
                    suppressInfoWindows: true,
                    polylineOptions: {
                        strokeWeight: 10,
                        strokeOpacity: 0.5,
                        strokeColor: color
                    }/*,
                    markerOptions: {
                        icon: {
                            path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
                            scale: 3,
                            strokeColor: color
                        }
                    }*/
                });

                function directionResults(result, status) {
                    if (status === google.maps.DirectionsStatus.OK) {

                        //console.log('valor de i=' + i);

                        // Create a unique DirectionsRenderer 'i'

                        // Use this new renderer with the result
                        renderArray2[i].setDirections(result);
                        // and start the next request

                    }

                }

                i++;

            }


        }


        function getGeoPoints() {

            $.ajax({

                beforeSend: function (xhr) {

                    xhr.setRequestHeader('Authorization', 'asfdsafsdffdfddf');


                    $("<img src='../../imagenes/loaders/gears-2.gif' " +

                        " id='loading-imagen' class='loadin-image' />").appendTo("#ruteo");

                },
                method: 'get',
                url: 'http://apitest.vanguardiagps.com/v2/ruteo/19',
                success: function (data) {


                    startRouterMap(data);


                    $("#loading-imagen").remove();


                },
                error: function (param1, param2, param3) {


                    let data = JSON.parse(param1.responseText);

                    $.messager.alert('Aviso', data.mensaje, 'error');

                    $("#loading-imagen").remove();


                }


            });


        }


        function findingStores() {




            $.ajax({
                url:'ruteo/direcciones2.json',
                success:(data) => {

                    geoCoder(data);

                }
            });


        }


        async function geoCoder(data) {

            const KEY = "AIzaSyCDb3AWOIzMkm-_tdCezft9_ygxrgZ3__0";

            let len = data.length;

            for (let i = 0; i <= len ; i++) {

                let url = `https://maps.googleapis.com/maps/api/geocode/json?address=${data[i].direccion}&key=${KEY}`;
                await fetch(url)
                    .then(response => response.json())

                    .then(data2 => {
                        let geo = data2.results[0].geometry.location;

                        console.log(data2);

                        data[i].lat =  geo.lat;
                        data[i].lon = geo.lng;

                        //document.getElementById("container").innerHTML += JSON.stringify(data[i]) + ",";

                        $('#ruteo2').datagrid('appendRow', data[i]);



                    })
                    .catch(err => console.warn(err.message))

            }


        }


        $('#ruteo2').datagrid({
            columns : [[

                {field:'economico', title:'Id Economico'},
                {field:'sucursal', title:'Sucursal'},
                {field:'lat', title:'Latitud'},
                {field:'lon', title:'Longitud'},
                {field:'direccion', title:'Addresssss'}

            ]],


        });



    </script>


    <?php

} else {


    echo "Acceso denegado";


}

?>