<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    $id_tienda = $_REQUEST['idTienda'];

    include_once('../../connexion/DataBase.php');


    $manager = DataBase::getInstance();

    $query_tien = "SELECT mt.*,UPPER(e.nombre) AS nombreEstado, GROUP_CONCAT(mar.`nombre` ORDER BY mar.`nombre` SEPARATOR ' / ') marcas
            FROM maestroTiendas mt
            LEFT JOIN estados e ON (mt.idEstado=e.id) 

            left join tienda_marca tm on (tm.id_tienda = mt.idTienda)
            left join Marca mar on (mar.idMarca = tm.id_marca) 
            
            WHERE  idTienda='" . $id_tienda . "'  group by mt.idTienda ";

    $resul_tien = $manager->ejecutarConsulta($query_tien);

    $datos_tien = mysqli_fetch_array($resul_tien);

    //todo change to permission system



    ?>
    <div style="width:100%;height:100%; padding: 20px;box-sizing: border-box;">

        <div style="display: flex;align-items: stretch;">

            <div id="Mapa-tienda"></div>


            <div >

                <ul class="lista-style">
                    <li>

                        <?= $datos_tien['sucursal'] ?>

                    </li>
                    <li>
                        <?= $datos_tien['grupo'] ?>
                    </li>
                    <li>
                        <?= $datos_tien['direccion'] ?>
                    </li>
                    <li>
                        <?= $datos_tien['cp'] ?>
                    </li>
                    <li>
                        <?= $datos_tien['colonia'] ?>
                    </li>
                    <li>
                        <?= $datos_tien['nombreEstado'] ?>
                    </li>

                    <li>
                        <?= $datos_tien['municipio'] ?>
                    </li>

                    <li>

                        <?= $datos_tien['telefono'] ?>

                    </li>

                    <li>
                        <span id="latitud"><?= $datos_tien['x'];?></span>,

                    </li>

                    <li>
                        <span id="longitud"><?= $datos_tien['y'];?></span>



                        <?php
                        if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5'): ?>
                        <a class="easyui-linkbutton" iconcls="icon-edit" onclick="editTien()">Edit</a>

                        <? endif; ?>

                    </li>

                    <li>


                        <label for="marcas">Marcas</label>
                        <div id="marcas"><?= $datos_tien['marcas']  ?></div>



                    </li>


                </ul>




            </div>






        </div>




    </div>

    <script>
        $(document).ready(function () {

            let latit = <?= $datos_tien['x'];?>;
            let longi = <?= $datos_tien['y'];?>;

            let myLatlng = new google.maps.LatLng(latit, longi);

            let mapOptions = {
                zoom: 15,
                center: myLatlng,
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
            let map = new google.maps.Map(document.getElementById('Mapa-tienda'), mapOptions);

            let marker = new google.maps.Marker({
                position: myLatlng,
                map: map,
                animation: google.maps.Animation.DROP,
                title: 'Localizacion Tienda'
            });


            marker.addListener('dragend', function (args) {

                let position = args.latLng;

                $('#latitud').val(position.lat());
                $('#longitud').val(position.lng());


            });

            //geoCoder();

            $('#edit-marker').on('click', function () {

                editMarker();


            });

            $('#edit-save').on('click', function () {

                stopEdit();


            });






            function geoCoder(){


                let geocoder = new google.maps.Geocoder();

                geocoder.geocode({


                    "location": myLatlng

                }, function (result, status) {


                    //console.log(result[0]);

                    let geo = result[0].address_components;
                    console.log(result[0].address_components);


                    geo.forEach(part =>{

                        if (part.types.includes("locality")){

                            console.log(part.long_name);



                        }



                    })




                   /* map.setCenter(result[0].geometry.location);

                    let mark = new google.maps.Marker({
                        map: map,
                        position: result[0].geometry.location
                    });*/

                });

            }


            function stopEdit() {

                if (marker != null){

                    marker.setDraggable(false);
                    marker.setLabel('');
                }


            }



            function editMarker() {

                if (marker != null){

                    marker.setDraggable(true);
                    marker.setLabel('editar');


                }

            }


            $('#latitud').on('input', function () {


                markerChangePosition()


            });

            $('#longitud').on('input', function () {

                markerChangePosition()

            });



            function markerChangePosition() {
                let lat = $('#latitud').val();
                let lon = $('#longitud').val();


                let position = new google.maps.LatLng(lat, lon);

                marker.setPosition(position);

            }


        });

    </script>


    <?
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

?>