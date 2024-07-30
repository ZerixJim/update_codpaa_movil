<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 15/05/2018
 * Time: 10:35 AM
 */

session_start();


?>


<div id="inteligencia-mercado" class="easyui-layout" style="width:100%;height: 100%">


    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:250px; height:auto;">


        <form id="filtros-reporte-im" method="post" class="form-style">


            <ul>
                <li>

                    <label for="fechaInicio">Fecha inicio</label>
                    <input id="fechaInicio" class="easyui-datebox"
                           data-options="formatter:myformatter,parser:myparser" required>

                </li>
                <li>
                    <label for="fechaFin">Fecha fin</label>
                    <input id="fechaFin" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser"
                           required>
                </li>
                <li>

                    <label for="marca-inteligencia">Marca</label>
                    <input id="marca-inteligencia" name="marca-inteligencia">

                </li>
                <li>
                    <label for="idTipoTie">Tipo Tienda</label>
                    <input id="idTipoTie" class="easyui-combobox" name="idTipoTie">
                </li>
                <li>
                    <label for="Formatos">Formato</label>
                    <input id="Formatos" class="easyui-combobox" name="Formatos">

                </li>
                <li>
                    <label for="nombreEstado">Estado</label>
                    <input id="nombreEstado" class="easyui-combobox" name="nombreEstado">

                </li>
            </ul>



        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteInteligencia()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="">Limpiar</a>

        </div>

        <script type="text/javascript" src="../../script/datagrid-groupview.js"></script>

        <script type="text/javascript">


            function crearReporteInteligencia() {

                var idFormato = $('#Formatos').combobox('getValue');

                var idEstado = $('#nombreEstado').combobox('getValue');

                var fechaIni = $('#fechaInicio').combobox('getValue');

                var fechaFin = $('#fechaFin').combobox('getValue');

                var idMarca = $('#marca-inteligencia').combobox('getValues').length > 0 ?
                    $('#marca-inteligencia').combobox('getValues') : '';

                var idTipoTie = $('#idTipoTie').combobox('getValue');





                if ( fechaIni !== "" && fechaFin !== "" && idMarca !== "") {


                    $('#datagrid-inventario').datagrid({

                        url: '../php/reportes/query_reporte_inteligencia.php',
                        queryParams: {
                            idFormato: idFormato,
                            idMarca: idMarca,
                            idEstado: idEstado,
                            Desde: fechaIni,
                            Hasta: fechaFin,
                            idTipoTie: idTipoTie
                        },

                        remoteSort: false,

                        view:groupview,
                        groupField:'producto',

                        groupFormatter:function(value,rows){

                            var tiendas = 0;



                            return value ;
                        },

                        columns : [[

                            {field: 'idTienda', title: 'Id tienda', sortable: true},
                            {field: 'grupo', title: 'Grupo', sortable: true},
                            {field: 'sucursal', title: 'Sucursal', sortable: true},
                            {field: 'formato', title: 'Formato', sortable: true},
                            {field: 'cadenaf', title: 'Cadena formato', sortable: true},
                            {field: 'estadoMex', title: 'Estado', sortable: true},
                            {field: 'idProducto', title: 'id producto', sortable: true},
                            {field: 'producto', title: 'Nombre', sortable: true},
                            {field: 'fecha_captura', title: 'Fecha', sortable: true},
                            {field: 'precioNormal', title: '$ Normal', sortable: true},
                            {field: 'precioOferta', title: '$ Oferta', sortable: true},
                            {field: 'precio_caja', title: '$ Caja', sortable: true},
                            {field: 'oferta', title: 'Oferta', sortable: true}

                        ]],


                        onBeforeLoad: function (param) {

                            return $('#filtros-reporte-im').form('validate');

                        },

                        onLoadError : function () {

                        },

                        onLoadSuccess: function () {

                            var gcount = $(this).datagrid('options').view.groups.length;
                            for(var i=0; i<gcount; i++){
                                $(this).datagrid('collapseGroup', i);
                            }

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

            $('#marca-inteligencia').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                multiple:true,

                groupField: 'cliente',

                url: '../php/getMarca.php',

                required: true


            });

            $('#idTipoTie').combobox({

                valueField: 'idTipo',

                textField: 'nombre',

                url: '../php/getTiendaTipo.php',

                required: false


            });


            function exportarInteligencia(){

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
               onClick="exportarInteligencia()">Exportar</a>
        </div>

        <table id="datagrid-inventario" class="easyui-datagrid" fit="true">


        </table>

    </div>







</div>



