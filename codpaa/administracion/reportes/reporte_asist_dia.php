<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 30/07/14
 * Time: 16:57
 */


?>


<div id="asistencia" class="easyui-layout" style="height: 100%; width:100%;">


    <div data-options="region:'west',title:'Busqueda',split:true,collapsible:true" style="width:23%;padding: 5px; background-color: #f5f5f5;">


        <form id="filtrosReporte" method="post" class="form-style">


            <ul>
                <li>
                    <label for="fechaDia">Fecha</label>
                    <input id="fechaDia" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser"
                           required>
                </li>
                <li>
                    <label for="MarcaAsisD">Marca</label>
                    <input id="MarcaAsisD" name="MarcaAsisD">
                </li>
                <li>
                    <label for="EstadoAD">Estado</label>
                    <input id="EstadoAD" class="easyui-combobox" name="EstadoAD">

                </li>
                <li>

                    <input name="busca_promAD" class="easyui-tagbox" id="busca_promAD" style="height:40px;">



                </li>
                <li>
                    <label for="SupervisorAD">Supervisor</label>
                    <input id="SupervisorAD" class="easyui-combobox" name="SupervisorAD">


                </li>
                <li>
                    <label for="horas">Tiempo Tienda</label>
                    <input type="checkbox" id="horas" name="horas"/>

                </li>

                <li>

                    <label for="fotosTom">Fotos Tomadas</label>
                    <input type="checkbox" id="fotosTom" name="fotosTom"/>
                </li>
            </ul>



        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteAsisD()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRAsisD()">Limpiar</a>

        </div>


        <script type="text/javascript">
            function search_super() {
                var idMarca = $('#MarcaAsisD').combobox('getValues');

                var idEstado = $('#EstadoAD').combobox('getValues');

                if (idMarca != "") {
                    var url = '../php/get_superMarca.php?idMarca=' + idMarca + '&Estado=' + idEstado;

                    $('#SupervisorAD').combobox('clear');

                    $('#SupervisorAD').combobox('reload', url);
                }

            }

            function cleanRAsisD() {

                $("#repor_ADcenter").html('');
                $("#filtrosReporte").form('clear');
            }

            function crearReporteAsisD() {


                var idSupervisor = $('#SupervisorAD').combobox('getValue');

                var promotor = $('#busca_promAD').tagbox('getValues');

                promotor = promotor.length > 0 ? promotor : '';

                var idEstado = $('#EstadoAD').combobox('getValues');

                var n_estado = idEstado.length;

                var fechaDia = $('#fechaDia').combobox('getValue');

                var horas = 0;

                if (document.getElementById('horas').checked) {
                    horas = 1;
                }

                var fotos_cp = 0;

                if (document.getElementById('fotosTom').checked) {
                    fotos_cp = 1;
                }

                var idMarca = $('#MarcaAsisD').combobox('getValues');

                var n_marca = idMarca.length;

                if (n_estado > 0 && fechaDia !== "" && n_marca > 0) {
                    $('#dg-visitas-dia').datagrid({

                        url: '../php/reportes/queryReporteAsisD.php',
                        queryParams: {
                            Supervisor: idSupervisor,
                            idMarca: idMarca,
                            idEstado: idEstado,
                            fechaDia: fechaDia,
                            horas: horas,
                            idProm: promotor,
                            FotosCp: fotos_cp
                        },

                        columns:[[
                            {field:'idPromotor', title: 'idPromotor'},
                            {field:'idTienda', title: 'idTienda'},
                            {field:'sucursal', title: 'Sucursal'},
                            {field:'estado', title: 'Estatado'},
                            {field:'fecha', title: 'Fecha'},
                            {field:'tiempo', title: 'tiempo'},
                            {field:'distancia', title: 'Distancia'}

                        ]],
                        fit:true,

                        view:groupview,
                        groupField:'nombre',

                        groupFormatter:function(value,rows){


                            var min = 1000 * 60;
                            var hours = min * 60;
                            var day = hours * 24;

                            var length = rows.length;
                            var first = new Date(rows[0].fecha_captura);
                            var last = new Date(rows[length - 1].fecha_captura);


                            return value + ' horas laboradas ' + ((last.getTime() - first.getTime()) / hours ).toFixed();
                        },
                        onLoadSuccess: function () {

                            var gcount = $(this).datagrid('options').view.groups.length;
                            for(var i=0; i<gcount; i++){
                                $(this).datagrid('collapseGroup', i);
                            }

                        },
                        groupStyler: function (value, rows) {

                            var min = 1000 * 60;
                            var hours = min * 60;
                            var day = hours * 24;

                            var length = rows.length;
                            var first = new Date(rows[0].fecha_captura);
                            var last = new Date(rows[length - 1].fecha_captura);


                            var horasTotales = ((last.getTime() - first.getTime()) / hours ).toFixed();


                            if (horasTotales <= 6 ){

                                return 'color:#ff8484;';

                            }else if(horasTotales <= 7) {

                                return 'color:#dce258;';

                            }else {

                                return 'color:#93e258;';

                            }


                        },

                        rowStyler: function (index, row) {


                            if (row.distancia > 1){


                                return 'color: #f76565;';

                            }


                        }


                    });
                }
                else {

                    $.messager.alert('Reportes', "Faltan datos para generar el reporte", 'error');
                }

            }

            $('#EstadoAD').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: true,

                multiple: true,

                onSelect: function (record) {
                    search_super();
                },
                onUnselect: function (record) {
                    search_super();
                }


            });

            $('#MarcaAsisD').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',

                required: true,

                multiple: true,

                onSelect: function (record) {
                    search_super();
                },
                onUnselect: function (record) {
                    search_super();
                }


            });


            $('#SupervisorAD').combobox({

                valueField: 'idSupervisor',

                textField: 'nombreSupervisor',

                required: false
            });


            //console.log($('#busca_promAD').val());


            $('#busca_promAD').tagbox({

                url : '../php/promotores/get_prom.php',
                valueField : 'idCelular',
                textField: 'nombre',
                prompt : 'selecciona promotor',
                limitToList: true,
                hasDownArrow: true,
                tagFormatter: function(value,row){
                    var opts = $(this).tagbox('options');
                    return row ? row[opts.textField] : value;
                }



            });


            /*$('#busca_promAD').autocomplete({

                serviceUrl: '../php/search_prom.php',
                minChars: 2,
                zIndex: 9999,
                onSelect: function () {


                }

            });*/




        </script>

    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_ADcenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
         style="width: auto;">

        <table class="easyui-datagrid" id="dg-visitas-dia"></table>


    </div>

    <!-- end center -->


    <div id="dialogoLink" style="width: 400px;height: 200px;"></div>

</div>