<?php

/**
 * Created by Dream.
 * User: Christian M
 * Date: 6/08/14
 * Time: 13:13
 */


ob_start();

session_start();
include_once('../php/seguridad.php');

?>


<script>


    var mapa;


    $(function () {


    });


</script>


<div id="ctes_tiendas">


    <!--Rastreo -->

    <? if ($_SESSION['permiso'] >= 1) { ?>

        <div title="TIENDAS">


            <div id="cc" class="easyui-layout" style="width:100%;height:700px;">


                <div data-options="region:'north',title:'Promotores',split:true"
                     style="width:100%; height:120px; padding: 5px;">


                    <div class="easyui-panel" title="Detalle">
                        &nbsp;&nbsp;
                        <label for="dateBox">Marca:</label>
                        <input id="nombreMarcaCT" name="nombreMarcaCT"></td>

                        &nbsp;&nbsp;

                        <label for="nombreEstadoCT">Estado</label>

                        <input id="nombreEstadoCT" class="easyui-combobox" name="nombreEstadoCT">


                        &nbsp;&nbsp;

                        <label for="nombreClsumCT">Cliente Sum</label>

                        <input id="nombreClsumCT" class="easyui-combobox" name="nombreClsumCT"
                               data-options="valueField:'id_summa',textField:'cli_sum',required:false">

                        <script type="text/javascript">
                            ///////////////////////////////////////////////////////
                            function cargaMapaCT(tiendas_m) {
                                var MarcaName = $('#nombreMarcaCT').combobox('getText');
                                var idMarca = $('#nombreMarcaCT').combobox('getValue');

                                var Marca = idMarca.split('-');
                                var Logo = Marca[1];
                                var directionsDisplay1 = new google.maps.DirectionsRenderer();

                                var mapOptions = {
                                    zoom: 5,

                                    center: new google.maps.LatLng(22.35006135229113, -101.55759218749995),

                                    mapTypeId: google.maps.MapTypeId.ROADMAP
                                }

                                var map_ruta = new google.maps.Map(document.getElementById('Mapa-ctes-tiendas'), mapOptions);

                                directionsDisplay1.setMap(map_ruta);
                                for (var i = 0; i < tiendas_m.length; i++) {

                                    var tien_m = tiendas_m[i];

                                    var sucur = tien_m.sucursal;

                                    //***********************
                                    var msj = '<div id="content">' +
                                        '<h2 id="firstHeading" class="firstHeading">Sucursal ' + tien_m.idTienda + '</h2>' +
                                        '<div id="bodyContent">' +
                                        '<p>' + tien_m.sucursal + '</p>' +
                                        '<p>' + tien_m.direccion + '</p>';
                                    if (tien_m.fecha != null) {
                                        msj += '<p><strong>Ultima Foto</strong></p>' +
                                            '<p><strong>Fecha: </strong>' + tien_m.fecha + '</p>' +
                                            '<p><a target="_blank" href="http://plataformavanguardia.net/' + tien_m.imagen + '" width="200">Ver Imagen</a></p>' +
                                            '<p><img src="http://plataformavanguardia.net/' + tien_m.imagen + '" width="100"/></p>';
                                    }
                                    msj += '</div>' +
                                        '</div>';

                                    var image = "";

                                    if (Logo == 1) {
                                        image = MarcaName;
                                    } else {
                                        image = "tienda";
                                    }

                                    var myLatLngTiendas = new google.maps.LatLng(tien_m.x, tien_m.y);

                                    markerTiendas = new google.maps.Marker({

                                        position: myLatLngTiendas,

                                        map: map_ruta,

                                        icon: '../imagenes/logos_maps/' + image + '.png',

                                        title: sucur

                                    });
                                    messageStore(markerTiendas, msj);

                                }

                            }

                            ////////////////////////////////////////////////////////
                            function datos_tiendas() {
                                var idMarca = $('#nombreMarcaCT').combobox('getValue');

                                var Marca = idMarca.split('-');
                                var idEstado = $('#nombreEstadoCT').combobox('getValue');

                                var CliSum = $('#nombreClsumCT').combobox('getValue');

                                $.ajax({


                                    url: '../php/tiendas/getTiendasCtes.php',

                                    data: {idMarca: Marca[0], idEstado: idEstado, CliSum: CliSum},

                                    type: 'GET',

                                    dataType: 'json',

                                    beforeSend: function () {

                                        $("<img src='../imagenes/loading.gif' style='position:relative;" +

                                            "top:0px;left:25%;z-index:2000' id='loading-imagen' />").appendTo("#Mapa-ctes-tiendas")

                                    },

                                    success: function (json) {

                                        if (json.length > 0) {

                                            cargaMapaCT(json);


                                        } else {

                                            $.messager.alert('Tiendas Clientes', "No existen Tiendas para esos Parametros", 'info');


                                        }


                                    },

                                    error: function (jqXHR, status, error) {
                                        $.messager.alert('Tiendas Clientes', "Ocurrio un Error", 'error');

                                    },

                                    complete: function (jqXHR, status) {

                                        $("#loading-imagen").remove();

                                    }

                                });
                            }

                            //////////////////////////////////////////////////////////////////////////
                            //*********************Carga de Clientes para el filtro
                            function clientes_sum() {
                                var idMarca = $('#nombreMarcaCT').combobox('getValue');
                                var Marca = idMarca.split('-');
                                var idEstado = $('#nombreEstadoCT').combobox('getValue');

                                var url = '../php/get_CliSum.php?idMarca=' + Marca[0] + '&idEstado=' + idEstado;

                                $('#nombreClsumCT').combobox('clear');

                                $('#nombreClsumCT').combobox('reload', url);

                                $('#nombreClsumCT').combobox({
                                    onSelect: function (record) {
                                        datos_tiendas();
                                    }
                                });

                            }

                            //***********Carga de datos para los filtros
                            $('#nombreEstadoCT').combobox({

                                valueField: 'id',

                                textField: 'nombre',

                                url: '../php/get_estado.php',

                                required: false,

                                onSelect: function (record) {

                                    clientes_sum();
                                    datos_tiendas();
                                }
                            });

                            $('#nombreMarcaCT').combobox({

                                valueField: 'idMarca',

                                textField: 'nombre',

                                url: '../php/getMarcaLogo.php',

                                required: true,

                                onSelect: function (record) {

                                    clientes_sum();
                                    datos_tiendas();

                                }
                            });
                            //*******************************************************************
                            $(document).ready(function (e) {

                                //var directionsDisplay1 = new google.maps.DirectionsRenderer();

                                var mapOptions = {
                                    zoom: 5,

                                    center: new google.maps.LatLng(22.35006135229113, -101.55759218749995),

                                    mapTypeId: google.maps.MapTypeId.ROADMAP
                                }
                                var map_ruta = new google.maps.Map(document.getElementById('Mapa-ctes-tiendas'), mapOptions);

                                //			directionsDisplay1.setMap(map_ruta);
                            });

                        </script>

                    </div>

                </div>

                <div data-options="region:'center',title:'Ubicacion'" style="padding:5px;background:#eee;">


                    <div id="Mapa-ctes-tiendas"></div>


                </div>

            </div>


        </div>

    <? }  ?>

    <!-- Fin RASTREO-->


</div>