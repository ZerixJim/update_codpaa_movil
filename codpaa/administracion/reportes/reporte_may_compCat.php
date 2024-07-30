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


<div id="mayoreo_comp_cat" class="easyui-layout" style="height: 100%; width:100%;">


    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:200px; height:auto;">


        <form id="filtrosReporteMCCat" method="post">


            <table cellpadding="6" width="100%" id="filtros_mccat">
                <tbody>
                <tr>

                    <td>

                        <label for="MesLabC">Mes</label><br>
                        <input id="MesLabC" class="easyui-combobox" name="MesLabC" style=" width:100px;">
                    </td>

                </tr>
                <tr>
                    <td>
                        <label for="FormatosMCC">Formato</label><br>
                        <input id="FormatosMCC" class="easyui-combobox" name="FormatosMCC"></td>
                </tr>
                <tr>
                    <td>

                        <label for="GrupoM">Grupo</label><br>
                        <input id="GrupoM" class="easyui-combobox" name="GrupoM">
                    </td>

                </tr>

                <tr>
                    <td>
                        <label for="EstadoMCC">Estado</label><br>
                        <input id="EstadoMCC" class="easyui-combobox" name="EstadoMCC"></td>

                </tr>
                <tr>
                    <td><label for="RegionMx">Region</label><br>
                        <input id="RegionMx" class="easyui-combobox" name="RegionMx" style=" width:150px;"
                               data-options="panelHeight:'auto'"></td>
                </tr>


                </tbody>
            </table>

        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteMayComCat()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanMayComCat()">Limpiar</a>

        </div>

        <script type="text/javascript">


            $('#FormatosMCC').combobox({

                valueField: 'cadena',

                textField: 'cadena',

                url: '../php/get_cadenamc.php',

                required: false

            });
            $('#GrupoM').combobox({

                valueField: 'grupo',

                textField: 'grupo',

                url: '../php/get_grupomc.php',

                required: false

            });

            $('#EstadoMCC').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: false

            });
            $('#RegionMx').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_region.php',

                required: false


            });

            $('#MesLabC').combobox({

                valueField: 'mesMay',

                textField: 'mesMay',

                url: '../php/get_mesLab.php',

                required: true

            });


        </script>

    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true" style="width: auto;height: 100%;"
         toolbar="#toolsMCCat">
        <div id="toolsMCCat">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               onClick="exportarMayCCat()">Exportar</a>
        </div>

        <div id="ventanaMayCCat"></div>

    </div>

    <!-- end center -->


    <div id="dlgMayCCat" style="width: 400px;height: 200px;"></div>
</div>



