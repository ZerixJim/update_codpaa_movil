<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**
 * Created by PhpStorm.
 * User: Christian
 * Date: 30/07/14
 * Time: 16:57
 */

include_once('../connexion/bdManager.php');
include_once('../php/seguridad.php');
?>


<div id="mayoreo_compartido" class="easyui-layout" style="height: 100%; width:100%;">


    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:200px; height:auto;">


        <form id="filtrosReporteMC" method="post">


            <table cellpadding="6" width="100%" id="filtros_mc">
                <tbody>
                <tr>
                    <td>
                        <label for="MarcaMayC">Marca</label><br>
                        <input id="MarcaMayC" name="MarcaMayC"></td>

                </tr>
                <tr>
                    <td>

                        <label for="MesLab">Mes</label><br>
                        <input id="MesLab" class="easyui-combobox" name="MesLab" style=" width:100px;"
                               data-options="valueField:'mesMay',textField:'mesMay',required:true,multiple:false">
                    </td>

                </tr>

                <tr>
                    <td>
                        <label for="tipo_cons">Tipo de Consulta</label><br>
                        <select id="tipo_cons" class="easyui-combobox" name="tipo_cons" style=" width:140px;"
                                data-options="required:true,multiple:false">
                            <option value="1">Todos</option>
                            <option value="2">1er Sem-Ventas</option>
                            <option value="3">2da Sem-InteligenciaM</option>
                            <option value="4">3er Sem-Fotos</option>
                            <option value="5">4ta Sem-Inventarios</option>
                        </select>
                    </td>

                </tr>

                <tr style="display:;" id="div_est">
                    <td>
                        <label for="EstadoMC">Estado</label><br>
                        <input id="EstadoMC" class="easyui-combobox" name="EstadoMC"></td>

                </tr>
                <tr style="display:none;" id="div_reg">
                    <td><label for="RegionMx">Region</label><br>
                        <input id="RegionMx" class="easyui-combobox" name="RegionMx" style=" width:150px;"
                               data-options="panelHeight:'auto'"></td>
                </tr>

                <tr>
                    <td>

                        <label for="GrupoM">Grupo</label><br>
                        <input id="GrupoM" class="easyui-combobox" name="GrupoM">
                    </td>

                </tr>
                <tr>
                    <td>
                        <label for="NumEco">Numero Economico</label><br>
                        <input id="NumEco" class="easyui-combobox" name="NumEco" style=" width:100px;"
                               data-options="valueField:'numeroEconomico',textField:'numeroEconomico',required:false,multiple:false">
                    </td>

                </tr>
                <tr>
                    <td>
                        <label for="NoVisitadas">No Visitadas</label>
                        <input type="checkbox" name="NoVisitadas" id="NoVisitadas" value="1"/>
                    </td>
                </tr>


                </tbody>
            </table>

        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteMayComp()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanMayComp()">Limpiar</a>

        </div>

        <script type="text/javascript">
            function mes_pic(values) {



                var url = '../php/get_mesLab.php?idMarca=' + values;

                $('#MesLab').combobox('clear');

                $('#MesLab').combobox('reload', url);
            }

            function num_eco(mes) {
                var idMarca = $('#MarcaMayC').combobox('getValues');



                if (mes != null) {
                    var fecha = mes.split('-');

                    var url = '../php/get_numEco.php?idMarca=' + idMarca + '&Mes=' + fecha[0] + '&Anio=' + fecha[1];

                    $('#NumEco').combobox('clear');

                    $('#NumEco').combobox('reload', url);
                }


            }

            function exportarMayC() {

                $.ajax({
                    url:'../clasess/create_excel_by_json.php',
                    success:function (data) {

                        window.location = data;
                    },
                    error:function (param1, param2, param3) {
                        console.log(param2);
                    }
                });

            }


            $('#EstadoMC').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: false

            });

            $('#MarcaMayC').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',

                required: true,

                multiple: true,

                onSelect: function (record) {



                },
                onChange:function(newValue, oldValue){

                    if (newValue.length > 0){

                        mes_pic(newValue);
                    }





                }


            });
            $('#MesLab').combobox({
                onSelect: function (record) {

                    num_eco(record.mesMay);
                }
            });
            $('#RegionMx').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_region.php',

                required: false


            });

            $('#GrupoM').combobox({

                valueField: 'grupo',

                textField: 'grupo',

                url: '../php/get_grupomc.php',

                required: false

            });

            function crearDialogoFoto2(idfoto) {

                // creamos el dialogo de imagen
                $('#ventanaFotoMc').window({

                    modal: false,
                    href: '../php/fotos/marcoimagen.php?idfoto=' + idfoto,
                    title: 'Detalle Foto',
                    closable: true,
                    width: 1300,
                    height: 630,
                    resizable: true

                });

            }

            //////*************************** Funciones de imagenes

            function zoom(id) {
                // Instantiate EasyZoom plugin
                //var $easyzoom = $(id).easyZoom();

                // Get the instance API
                //var api = $easyzoom.data('easyZoom');

                var rotate = $(id).attr("id");
                console.log(rotate);
                if (document.getElementById('acercar').checked == true) {
                    $(id).animate({width: 1000}, {
                        step: function (now, fx) {
                            $(this).width('950px');
                        },
                        duration: 'slow'
                    }, 'linear');

                }
                else if (document.getElementById('rotar-right').checked == true) {
                    $(id).animate({borderSpacing: 90}, {
                        step: function (now, fx) {


                            $(this).css('-webkit-transform', 'rotate(' + now + 'deg)');
                            $(this).css('-moz-transform', 'rotate(' + now + 'deg)');
                            $(this).css('transform', 'rotate(' + now + 'deg)');
                            $(this).width('950px');
                            $(this).css('margin-bottom', '0');
                        },
                        duration: 'slow'
                    }, 'linear');

                }
                else if (document.getElementById('rotar-left').checked == true) {
                    $(id).animate({borderSpacing: -90}, {
                        step: function (now, fx) {

                            $(this).css('-webkit-transform', 'rotate(' + now + 'deg)');
                            $(this).css('-moz-transform', 'rotate(' + now + 'deg)');
                            $(this).css('transform', 'rotate(' + now + 'deg)');
                            $(this).width('950px');
                            $(this).css('margin-bottom', '0');
                        },
                        duration: 'slow'
                    }, 'linear');

                }

            }
            function check_out(id) {

                document.getElementById('rotar-right').checked = false;

                document.getElementById('acercar').checked = false;

                document.getElementById('rotar-left').checked = false;

                document.getElementById(id).checked = true;

            }

            function rotation(direct, idfoto, imagen) {
                $('#ventanaFotoMc').window('refresh', '../php/fotos/marcoimagen.php?rotation=' + direct + '&imagen=' + imagen + '&idfoto=' + idfoto);
            }

            function reload(idfoto, imagen) {
                $('#ventanaFotoMc').window('refresh', '../php/fotos/marcoimagen.php?imagen=' + imagen + '&idfoto=' + idfoto);
            }


        </script>

    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true" style="width: auto;height: 100%; position: relative;"
         toolbar="#toolsMC">
        <div id="toolsMC">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               onClick="exportarMayC()">Exportar</a>
        </div>

        <div id="ventanaMayC"></div>

    </div>

    <!-- end center -->
    <div id="detVtaPMay"></div>
    <div id="detVtaOMay"></div>
    <div id="detInvMAy"></div>
    <div id="detImMay"></div>
    <div id="detFotMay"></div>
    <div id="ventanaFotoMc"></div>

    <div id="dlgMayC" style="width: 400px;height: 200px;"></div>
</div>



