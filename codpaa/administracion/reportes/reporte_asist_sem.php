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


<div id="asistenciaSem" class="easyui-layout" style="height: 100%; width:100%;">


    <div data-options="region:'north',title:'Busqueda',split:true,collapsible:true" style="width:100%; height:140px;">


        <form id="filtrosReporte" method="post">


            <table cellpadding="5" id="filtros">
                <tbody>
                <tr>

                    <td>

                        <input id="SemanaAsis" class="easyui-combobox" prompt="Semana" required/>
                    </td>

                    <td>

                        <input id="MarcaAsiSem" name="MarcaAsiSem" prompt="Marca"/>
                    </td>

                    <td>

                        <input id="idTipoTieS" class="easyui-combobox" name="idTipoTieS" prompt="Tipo Tienda"/>
                    </td>

                    <!-- <td>
                    <label for="Formatos">Formato</label><br>
                    <input id="Formatos" class="easyui-combobox" name="Formatos" ></td>-->

                    <td>

                        <input id="nombreEstS" class="easyui-combobox" name="nombreEstS" prompt="Estado"/>
                    </td>

                    <td>

                        <input name="busca_promAse" id="busca_promAse" prompt="Promotor" style="padding: 3px;
                        border: 1px none #cccccc;border-bottom-style: solid;" placeholder="Promtor"/>

                    </td>


                    <td>
                        <input id="idSuperSem" class="easyui-combobox" name="idSuperSem" prompt="Supervisor"/>
                    </td>


                </tr>

                </tbody>
            </table>

        </form>


        <div style="text-align: center;">

            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteAsisSem()">Aceptar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRAsisSem()">Limpiar</a>

        </div>


        <script type="text/javascript">

            function crearReporteAsisSem() {


                var idSupervisor = $('#idSuperSem').combobox('getValue');

                var promotor = $('#busca_promAse').val();
                var id_promo;
                if (promotor !== "") {
                    id_promo = promotor.split(' - ');
                }
                else {
                    id_promo = "";
                }
                //var idFormato = $('#Formatos').combobox('getValue');

                var idEstado = $('#nombreEstS').combobox('getValues');

                var n_estado = idEstado.length;

                var SemanaAsis = $('#SemanaAsis').combobox('getValue');

                var idMarca = $('#MarcaAsiSem').combobox('getValues');

                var n_marca = idMarca.length;


                var idTipoTie = $('#idTipoTieS').combobox('getValue');

                if (n_estado > 0 && SemanaAsis !== "" && n_marca > 0) {
                    $.ajax({
                        beforeSend: function () {
                            $("#repor_Semcenter").html('');
                            $("<img src='../imagenes/loaders/gears.gif' " +
                                "id='loading-imagen' class='loadin-image' />").appendTo("#repor_Semcenter");
                        },
                        complete: function () {
                            $("#loading-imagen").remove();
                        },
                        type: 'GET',
                        url: '../php/reportes/queryReporteAsisSem.php',
                        data: {
                            Supervisor: idSupervisor,
                            idMarca: idMarca,
                            idEstado: idEstado,
                            SemanaAsis: SemanaAsis,
                            idTipoTie: idTipoTie,
                            idProm: id_promo[0]
                        },

                        error: function (jqXHR, textStatus, error) {

                            $.messager.alert('Reportes', "error: " + jqXHR.responseText, 'error');
                        }
                    }).done(function (data) {
                        $("#repor_Semcenter").html(data);
                    });
                }
                else {

                    $.messager.alert('Reportes', "Faltan datos para generar el reporte", 'error');
                }

            }

            function cleanRAsisSem() {

                $("#repor_Semcenter").html('');

            }


            /*
            $('#Formatos').combobox({

                valueField:'idFormato',

                textField:'cadena',

                url:'../php/get_formato.php',

                required:false

            });*/


            $('#nombreEstS').combobox({

                valueField: 'id',

                textField: 'nombre',

                url: '../php/get_estado.php',

                required: true,

                multiple: true

            });

            $('#MarcaAsiSem').combobox({

                valueField: 'idMarca',

                textField: 'nombre',
                groupField: 'cliente',

                url: '../php/getMarca.php',

                required: true,

                multiple: true,
                icons: [{
                    iconCls: 'icon-add',
                    handler: function (e) {

                        var c = $(e.data.target);
                        var opts = c.combobox('options');
                        $.map(c.combobox('getData'), function (row) {
                            c.combobox('select', row[opts.valueField])
                        });

                    }
                }, {
                    iconCls: 'icon-remove',
                    handler: function (e) {
                        var c = $(e.data.target);
                        var opts = c.combobox('options');
                        $.map(c.combobox('getData'), function (row) {
                            c.combobox('unselect', row[opts.valueField])
                        });
                    }
                }],

                filter: function(q, row){
                var opts = $(this).combobox('options');


                //fitro para la busqueda por grupo o textfield

                if (row[opts.groupField] != null){

                    return row[opts.groupField].toLowerCase().indexOf(q.toLowerCase()) >= 0
                        || row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;

                } else {

                    return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) >= 0;
                }


            }


            });

            $('#idTipoTieS').combobox({

                valueField: 'idTipo',

                textField: 'nombre',

                url: '../php/getTiendaTipo.php',

                required: false


            });


            $('#idSuperSem').combobox({

                valueField: 'idSupervisores',

                textField: 'nombreSupervisor',

                url: '../php/get_supervisor.php',

                required: false

            });

            $('#SemanaAsis').combobox({

                valueField: 'idSupervision',

                textField: 'semana_lab',

                url: '../php/get_semanaLab2.php'

            });


            $('#busca_promAse').autocomplete({

                serviceUrl: '../php/search_prom.php',
                minChars: 2,
                zIndex: 9999,
                onSelect: function () {


                }

            });


        </script>


    </div>


    <!-- end north -->


    <!-- start center -->

    <div id="repor_Semcenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false"
         style="width: auto;height: 100%;">


    </div>

    <!-- end center -->


    <div id="dialogoASem" style="width: 400px;height: 200px;"></div>

</div>



