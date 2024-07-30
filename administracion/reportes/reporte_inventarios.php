<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**
 * Created by PhpStorm.
 * User: Christian
 * Date: 30/07/14
 * Time: 16:57
 */


?>


<div id="inventarios" class="easyui-layout" style="width:100%;height: 100%">


    <div data-options="region:'west',title:'Filtros',split:true,collapsible:false" style="width:200px; height:auto;">


        <form id="filtrosReporteInv" method="post">


            <table cellpadding="6" width="100%" id="filtros_inv">
                <tbody>
                <tr>

                    <td>
                        <label for="fechaInicio">Fecha inicio</label><br>
                        <input id="fechaInicio" class="easyui-datebox"
                               data-options="formatter:myformatter,parser:myparser" required></td>
                </tr>
                <tr>

                    <td>
                        <label for="fechaFin">Fecha fin</label> <br>
                        <input id="fechaFin" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser"
                               required>
                    </td>

                </tr>
                <tr>
                    <td>
                        <label for="MarcaInv">Marca</label><br>
                        <input id="MarcaInv" name="MarcaInv"></td>

                </tr>

                <tr>

                    <td>
                        <label for="nombreEstado">Estado</label><br>
                        <input id="nombreEstado" class="easyui-combobox" name="nombreEstado"></td>

                </tr>
              <!-- <tr>

                    <td>
                        <label for="idTipoTie">Tipo Tienda</label><br>
                        <input id="idTipoTie" class="easyui-combobox" name="idTipoTie"></td>

                </tr>
              <!--  <tr>
                    <td>
                        <label for="Formatos">Formato</label><br>
                        <input id="Formatos" class="easyui-combobox" name="Formatos"></td>
                </tr>-->



                </tbody>
            </table>

        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteInvent()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanForm()">Limpiar</a>

        </div>

        <script type="text/javascript">


            function crearReporteInvent() {

            //    var idFormato = $('#Formatos').combobox('getValue');

                var idEstado = $('#nombreEstado').combobox('getValue');

                var fechaIni = $('#fechaInicio').combobox('getValue');

                var fechaFin = $('#fechaFin').combobox('getValue');

                var idMarca = $('#MarcaInv').combobox('getValues');



                idMarca = idMarca.length > 0 ? idMarca : '';




             //  var idTipoTie = $('#idTipoTie').combobox('getValue');





                if ( fechaIni !== "" && fechaFin !== "" && idMarca !== "") {


                    $('#datagrid-inventario').datagrid({

                        url: '../php/reportes/queryReporteInvent.php',
                        queryParams: {
                         //   idFormato: idFormato,
                            idMarca: idMarca,
                            idEstado: idEstado,
                            Desde: fechaIni,
                            Hasta: fechaFin
                      //   idTipoTie: idTipoTie
                        },

                        view: bufferview,

                        columns : [[

                            {field:'fecha_captura', title: 'Fecha captura' },
                            {field:'idTienda', title: 'IdTienda' },
                            {field:'nombre', title: 'Nombre' },
                            {field:'cadenaf', title: 'Cadena' },
                          //  {field:'idMarca', title: 'idMarca' },
                         //   {field:'grupo', title: 'Grupo' },
                            {field:'sucursal', title: 'Sucursal' },
                         //   {field:'numeroEconomico', title: 'numeroEconomico' },
                            {field:'estadoMex', title: 'Estado' },
                           // {field:'idProducto', title: 'IdProducto' },
                            {field:'producto', title: 'Producto y Presentacion' },
                            {field:'cantidadFisico', title: 'Fisico' },
                            {field:'cantidadSistema', title: 'Sistema' },
                         //   {field:'idPromotor', title: 'IdPromotor' },
                            {field:'tipo', title: 'Tipo' },


                        ]],


                        onBeforeLoad: function (param) {

                            return $('#filtrosReporteInv').form('validate');

                        },

                        onLoadError : function () {

                        }




                    });


                }
                else {
                    $.messager.alert('Reportes', "Faltan datos para generar el reporte", 'warning');
                }
            }




            $('#Formatos').combobox({

                valueField: 'idFormato',

                textField: 'cadena',

                url: '../php/get_formato.php',

                required: false

            });


            $('#nombreEstado').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: false

            });

            $('#MarcaInv').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',

                multiple : true,
                groupField: 'cliente',

                required: true


            });

            $('#idTipoTie').combobox({

                valueField: 'idTipo',

                textField: 'nombre',

                url: '../php/getTiendaTipo.php',

                required: false


            });


            function exportarInventario(){

                var datos = $('#datagrid-inventario').datagrid('getRows');

                //console.log(datos);


                if (datos.length > 0) {

                    exportarExcel(datos);


                } else {
                    $.messager.alert('Reportes', 'No hay informacion, no se puede exportar');
                }



            }


            ///******************************************************************


        </script>

    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true, fit:true"
         toolbar="#toolsInvent">
        <div id="toolsInvent">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               onClick="exportarInventario()">Exportar</a>
        </div>

        <table id="datagrid-inventario" class="easyui-datagrid" fit="true">


        </table>

    </div>

    <!-- end center -->

</div>
