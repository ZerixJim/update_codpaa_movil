<?php
ob_start();

session_start();


?>



<div id="comentarios" class="content-padding">

    <? if ($_SESSION['permiso'] >= 2) { ?>

        <h3>Comentarios</h3>

        <div title="Comentarios">


            <table id="dg_comnts" class="easyui-datagrid" toolbar="#toolbarComnts" fitColumns="true" singleSelect="true"
                   idField="id_mensaje" style="min-height: 550px; max-height: 600px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow, remoteSort:false"  nowrap="false">

                <thead>

                <tr>

                    <th data-options="field:'idTienda'" width="50">idTienda</th>

                    <th data-options="field:'economico'" width="50">N.economico</th>

                    <th data-options="field:'Tienda'" width="100" sortable="true">Tienda</th>

                    <th data-options="field:'fecha'" width="50">Fecha</th>

                    <th field="idPromotor" width="50">idPromotor</th>

                    <th field="Promotor" sortable="true" width="80">Promotor</th>

                    <th data-options="field:'marca'" sortable="true" width="50">Marca</th>

                    <th data-options="field:'comentario'" width="100">Comentario</th>


                </tr>

                </thead>


            </table>

            <!-- toolbar mensajes-->
            <div id="toolbarComnts" style="padding: 15px;">

                <input id="fechaInicio" class="easyui-datebox"
                       data-options="formatter:myformatter,parser:myparser,required:true" prompt="fecha inicio">


                <input id="fechaFin" class="easyui-datebox"
                       data-options="formatter:myformatter,parser:myparser,required:true" prompt="fecha fin">

                <input id="marca-comentario" class="easyui-combobox" name="marca-comentario" prompt="Marca">

                <input type="text" id="tipoTienda" class="easyui-combobox" prompt="tipo tienda">

                <input id="promotor-comentario" style="width:100px" class="easyui-textbox" prompt="promotor">


                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchComnts();">Buscar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true"
                   onClick="cFC()">Exportar</a>
            </div>

            <div id="comnt_view" style="padding:10px 20px;display:none;">
                <div id="dlgComnts" style="width: 350px;height: 150px; display:none;"></div>
            </div>

        </div>
        <script language="javascript">


            $('#tipoTienda').combobox({

                valueField: 'idTipo',

                textField: 'nombre',

                url: '../php/getTiendaTipo.php'


            });



            function onDblClickRow(index) {

                let idComnt = $('#dg_comnts').datagrid('getRows')[index]['idcomentarioTienda'];

                let comentView = $('#comnt_view');

                comentView.show();

                comentView.dialog({
                    title: 'Datos Comentario',
                    href: '../php/comentarios/view_comnt.php?idComnt=' + idComnt,
                    width: 500,
                    height: 320,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#comnt_view').dialog('close');
                        }
                    }]
                });

            }

            function searchComnts() {

                let buscarcomnt = $('#promotor-comentario').val();
                let fechaIni = $('#fechaInicio').datebox('getValue');


                let idMarca = $('#marca-comentario').combobox('getValues').length > 0 ? $('#marca-comentario').combobox('getValues') : '';

                //console.log(idMarca);

                let fechaFin = $('#fechaFin').datebox('getValue');

                let idTipoTienda = $('#tipoTienda').combobox('getValue');

                if (buscarcomnt === "") {
                    buscarcomnt = '*';
                }

                if (buscarcomnt !== "" || (fechaIni !== "" && fechaFin !== "")) {
                    $('#dg_comnts').datagrid({


                        method: 'POST',
                        url: '../php/comentarios/get_comnts.php',
                        queryParams: {
                            buscarComnt: buscarcomnt,
                            fechaIni: fechaIni,
                            fechaFin: fechaFin,
                            tipoTienda: idTipoTienda,
                            idMarca:idMarca
                        },

                        onLoadSuccess: function (data) {


                        },

                        onError: function (jqXHR, textStatus, error) {

                            alert("error: " + jqXHR.responseText + " " + textStatus);
                        }
                    });
                }
                else {
                    $('#dg_comnts').datagrid('reload');
                }

            }



            /*
            *
            * create excel from comentarios
            *
            */

            function cFC(){
                let comentarios = $('#dg_comnts').datagrid('getRows');

                if (comentarios.length > 0){

                    //console.log(comentarios);

                    exportarExcel(comentarios);
                } else {
                    alert("no hay comentarios ");
                }
            }


            $('#marca-comentario').combobox({

                valueField: 'idMarca',

                textField: 'nombre',

                url: '../php/getMarca.php',


                multiple: true,
                groupField: 'cliente',

                formatter: function (row) {

                    return '<span ' +
                        'style="background: url(http://codpaa.plataformavanguardia.net/images/marcas/' + row.idMarca + '.gif) no-repeat left;' +
                        'background-size: 35px auto; padding-left: 40px; color: #333333; ">' +
                        row.nombre + '</span>';
                },

                icons: [{
                    iconCls: 'icon-add',
                    handler: function (e) {

                        let c = $(e.data.target);
                        let opts = c.combobox('options');
                        $.map(c.combobox('getData'), function (row) {
                            c.combobox('select', row[opts.valueField])
                        });

                    }
                }, {
                    iconCls: 'icon-remove',
                    handler: function (e) {
                        let c = $(e.data.target);
                        let opts = c.combobox('options');
                        $.map(c.combobox('getData'), function (row) {
                            c.combobox('unselect', row[opts.valueField])
                        });
                    }
                }]







            });

        </script>

    <? } else {
    } ?>

    <!-- FIN MATERIALES-->

</div>

