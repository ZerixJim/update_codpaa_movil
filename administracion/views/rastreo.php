<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 6/08/14
 * Time: 13:13
 */


ob_start();

session_start();


?>





<div id="rastreo" style="height:100%">


    <!--Rastreo -->

    <?php if ($_SESSION['permiso'] >= 1) { ?>

        <div title="Rastreo" style=" height:98%; padding: 5px">


            <div id="cc" class="easyui-layout" style="width:100%;height:100%;">



                <div data-options="region:'center'" style="background:#eee; position: relative;"
                     toolbar="#toolsMapa">

                    <div id="toolsMapa">


                        <!-- <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-map" plain="true" onClick="borrarMarkadores()">borrar Markadores</a>-->

                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-map" plain="true"
                           onClick="rastreoDia()">Rastreo</a>
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-map" plain="true"
                           onClick="rastreoDiaDet()">Detalle Rastreo</a>
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
                           onclick="$('#dgrastreo').datagrid('reload');">Actualizar Usuarios</a>

                    </div>

                    <div id="Mapa-rastreo"></div>





                    <div id="rastreo-prom">

                        <div class="closeable-div">

                            <div class="header-card">

                                <img src="../imagenes/user.png" alt="">

                                <span id="nombre-promotor">Nombre promotor</span>

                                <img id="icon" src="../imagenes/ic_keyboard_arrow_down_black_24dp_1x.png" alt="" class="rotate-180">

                            </div>


                            <div class="content">

                                <div class="lista-input">

                                    <ul>
                                        <li>

                                            <input id="dateBox" type="text" required style="width: 40%;">
                                            <input id="nombreEstado" class="easyui-combobox"  style="width: 45%;" name="nombreEstado" prompt="Estado">
                                        </li>


                                        <li>
                                            <input type="text" id="promotor-rastreo" class="easyui-combobox" prompt="promotor" required style="width: 70%;">


                                            <img id="action-visitas" src="../imagenes/icons/ic_arrow_forward_black_24dp_1x.png">



                                        </li>
                                    </ul>


                                </div>

                                <!--<div class="indicator-content">

                                    <div>
                                        <span class="text">En rango</span>
                                        <span style="background-color: #0b93d5;" class="color"></span></div>
                                    <div>
                                        <span class="text">Fuera de rango</span>
                                        <span style="background-color: #00CB49;" class="color"></span>
                                    </div>


                                </div>-->


                                <div id="ruta-entradas" style="display: none;" class="list-input">



                                </div>

                            </div>



                        </div>



                    </div>




                </div>

            </div>


        </div>

        <div id="detRastreo"></div>

    <?php } else {
    } ?>

    <!-- Fin RASTREO-->


</div>


<script>


    let mapa;

    $('#promotor-rastreo').combobox({

        url: '../php/rastreo/get_prom_ras.php',

        valueField: 'idCelular',
        textField: 'nombre',
        onSelect: function (record) {


            loadRastreo(record.nombre, record.idCelular);

        },

        onLoadSuccess: function () {
            initialize();
        },

        onUnselect: function(){
            //console.log('unselect');
        }


    });


    $('#dateBox').datebox({

        value: new Date().toString('dd-mm-yyyy'),

        formatter: myformatter,

        parser: myparser

    });


    function rastreoDiaDet() {

        /* let distancia = distanciaEntreDosPuntos(vis.x,vis.y,vis.latitud, vis.longitud);

         let prettyDistance = distancia >= 1 ? distancia.toFixed(2) + 'km' : (distancia * 1000.0).toFixed(2) + 'm';*/

        let row = $('#promotor-rastreo').combobox('getValue');

        let fecha = $('#dateBox').datebox('getValue');


        $('#detRastreo').window({

            modal: false,

            href: '../php/rastreo/detalle_rastreo.php?idPromotor=' + row + '&Fecha=' + fecha,

            title: 'Detalle Rastreo',

            closable: true,

            width: 950,

            height: 550,

            resizable: true,
            border:'thin'

        });

    }


    function loadRastreo(nombre, idPromtor){

        //console.log(record);

        $('#nombre-promotor').text(nombre);


        if (nombre.length !== 0 && idPromtor.length !== 0) {
            //console.log(rowData.nombre);

            let setFecha = $('#dateBox').datebox('getValue');

            OnRastreo = true;

            RastreoName = nombre;

            $.ajax({

                url: '../php/rastreo/getVisitas.php',

                data: {id: idPromtor, fecha: setFecha},

                type: 'GET',

                dataType: 'json',

                beforeSend: function () {

                    $("<img src='../imagenes/loaders/gears-2.gif' " +

                        " id='loading-imagen' class='loadin-image' />").appendTo("#Mapa-rastreo")

                },

                success: function (json) {

                    //console.log(json);

                    if (json.length > 0) {

                        iniciarMapa(json);


                    } else {



                        //console.log(markersMap);
                        if (markersMap.length > 0){


                            let tamanio = markersMap.length;

                            for (let i = 0; i < tamanio; i++) {
                                markersMap[i].setMap(null);
                            }




                            markersMap = [];

                            mapa.setZoom(5);

                            mapa.setCenter(new google.maps.LatLng(22.35006135229113, -101.55759218749995));
                        }






                        $('#ruta-entradas').text('No existen registros de este Promotor en la fecha: ' + setFecha)

                        $('#ruta-entradas').slideDown('fast');

                        setTimeout(function () {

                            $('#ruta-entradas').slideUp('fast');

                        }, 2000);



                        if (mapa === undefined || mapa === null) initialize();


                        markersMap = [];


                        mapa.setZoom(5);

                        mapa.setCenter(new google.maps.LatLng(22.35006135229113, -101.55759218749995));

                        //console.log('no hay registros');

                    }


                },

                error: function (jqXHR, status, error) {


                    $("#loading-imagen").remove();

                },

                complete: function (jqXHR, status) {

                    $("#loading-imagen").remove();



                }

            });


        } else {
            $.messager.alert('Rastreo', 'falta informacion para completar la busqueda', 'info');
        }

    }

    $('#nombreEstado').combobox({

        valueField: 'id',

        textField: 'nombre',

        url: '../php/get_estado.php',

        required: false,
        icons: [{
            iconCls: 'icon-remove',
            handler: function (e) {
                let c = $(e.data.target);
                let opts = c.combobox('options');
                $.map(c.combobox('getData'), function (row) {
                    c.combobox('unselect', row[opts.valueField])
                });


                $('#promotor-rastreo').combobox('clear').combobox('reload', {

                    url: '../php/rastreo/get_prom_ras.php',

                    valueField: 'idCelular',
                    textField: 'nombre',

                    onLoadSuccess: function () {
                        initialize();
                    }

                });

            }
        }],

        onSelect: function (record) {

            //console.log('entro');

            searchRas(record.id);
        },
        onUnselect:function (record) {

            //console.log('unselect');


        }

    });

    $('.header-card').find('#icon').click(function () {

        let p = $(this).parent();
        p.parent().find('.content').slideToggle('fast');

        p.find('#icon').toggleClass('rotate-180');
    });

    $('#action-visitas').click(function(){


        let idPromo = $('#promotor-rastreo').combobox('getValue');
        let nombre = $('#promotor-rastreo').combobox('getText');


        //console.log(idPromo);

        loadRastreo(nombre, idPromo);

    });



</script>