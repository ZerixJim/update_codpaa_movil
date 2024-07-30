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

?>


<div id="Frentes" class="easyui-layout" data-options="fit:true">


    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:25%;">


        <form id="filtrosReporte" method="post">


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
                    <label for="MarcaFr">Marca</label>
                    <input id="MarcaFr" name="MarcaFr">
                </li>
               <!-- <li>
                    <label for="nombreProd">Producto</label>
                    <input id="nombreProd" name="nombreProd" class="easyui-combobox">
                </li>
                <li>
                    <label for="Formatos">Formato</label>
                    <input id="Formatos" class="easyui-combobox" name="Formatos">
                </li>
                <li>
                    <label for="nombreEstado">Estado</label>
                    <input id="nombreEstado" class="easyui-combobox" name="nombreEstado">
                </li>-->

                <li>
                    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="createDataGridFrentes()">Aceptar</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRFren()">Limpiar</a>
                </li>
            </ul>



        </form>



        <script type="text/javascript" src="../script/datagrid-groupview.js"></script>

        <script type="text/javascript">


            $('#Formatos').combobox({

                valueField: 'idFormato',

                textField: 'cadena',

                multiple: true,

                url: '../php/get_formato.php',

                required: false

            });


            $('#nombreEstado').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: false

            });

            $('#MarcaFr').tagbox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',
                prompt : 'selecciona marca',
                limitToList: true,
                hasDownArrow: true,
                tagFormatter: function(value,row){
                    var opts = $(this).tagbox('options');
                    return row ? row[opts.textField] : value;
                },
                groupField : 'cliente',


                onChange: function (newValue, oldValue) {


                    var idMarcas = $(this).tagbox('getValues');


                    if (idMarcas.length > 0) {

                        $('#nombreProd').combobox({

                            valueField: 'idProducto',
                            textField: 'nombre',
                            url: '../php/productos/get_productos.php',
                            queryParams: {idMarca: idMarcas},
                            multiple: true,
                            icons: [{
                                iconCls: 'icon-remove',
                                handler: function (e) {
                                    var c = $(e.data.target);
                                    var opts = c.combobox('options');
                                    $.map(c.combobox('getData'), function (row) {
                                        c.combobox('unselect', row[opts.valueField])
                                    });
                                }
                            }]

                        });

                    } else {

                        $('#nombreProd').combobox('clear');


                    }


                }


            });


            function createDataGridFrentes() {

                var idFormato = $('#Formatos').combobox('getValues');

                var idEstado = $('#nombreEstado').combobox('getValue');

                var fechaIni = $('#fechaInicio').combobox('getValue');

                var fechaFin = $('#fechaFin').combobox('getValue');

                var idMarca = $('#MarcaFr').tagbox('getValues');
                idMarca = idMarca.length > 0 ? idMarca : '';

                var idProd = $('#nombreProd').combobox('getValues');

                idProd = idProd.length > 0 ? idProd : '';


                $('#frentes-datagrid').datagrid({

                    url: '../php/reportes/queryReporteFren.php',

                    fitColumns: 'true',
                    width: '100%',

                    view:groupview,
                    groupField:'producto',

                    onBeforeLoad: function (param) {

                        return $('#filtrosReporte').form('validate');

                    },
                    groupFormatter:function(value,rows){

                        var tiendas = 0;


                        for (var i= 0; i < rows.length ; i++){

                            if (rows[i].frentes == 0){
                                tiendas++;
                            }

                        }

                        return value + ' - En ' + rows.length + ' tiendas, en las cuales ' + tiendas + ' no tienen frentes ' ;
                    },

                    remoteSort: false,



                    rowStyler: function (index, row) {

                        if (row.frentes == 0){


                            return 'background-color:#ff977a;';
                        }


                    },
                    onLoadSuccess: function () {

                        var gcount = $(this).datagrid('options').view.groups.length;
                        for(var i=0; i<gcount; i++){
                            $(this).datagrid('collapseGroup', i);
                        }

                    },

                    queryParams: {
                        idFormato: idFormato,
                        idMarca: idMarca,
                        idProd: idProd,
                        idEstado: idEstado,
                        Desde: fechaIni,
                        Hasta: fechaFin
                    }


                });


            }


            function exportarFren() {

                var datos = $('#frentes-datagrid').datagrid('getRows');

                //console.log(datos);


                if (datos.length > 0) {

                    exportarExcel(datos);


                } else {
                    $.messager.alert('Reportes', 'No hay informacion, no se puede exportar');
                }
            }


        </script>


    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true,collapsible:false, fit:true"
         style="width: auto;height: 100%;" toolbar="#toolsFren">
        <div id="toolsFren">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
               onClick="exportarFren()">Exportar</a>
        </div>

        <div id="ventanaFren" style="height: 95%;">


            <table id="frentes-datagrid" class="easyui-datagrid" style="width: 100%;" fit="true">

                <thead>
                <tr>
                    <th data-options="field:'idTienda', sortable:true">ID-tienda</th>
                    <th data-options="field:'idMarca', sortable:true">ID-Marca</th>
                    <th data-options="field:'grupo'">grupo</th>
                    <th data-options="field:'sucursal', sortable:true">sucursal</th>
                    <th data-options="field:'numeroEconomico'">n.economico</th>
                    <th data-options="field:'formato', sortable:true">formato</th>
                    <th data-options="field:'cadenaf', sortable:true">cadenaf</th>
                    <th data-options="field:'estadoMex', sortable:true">estado-mex</th>
                    <th data-options="field:'frentes'">frentes</th>
                    <th data-options="field:'idProducto'">ID-producto</th>

                    <th data-options="field:'fecha_captura', sortable:true">fecha</th>

                </tr>
                </thead>

            </table>


        </div>

    </div>

    <!-- end center -->

</div>

