<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 29/07/14
 * Time: 16:35
 */

ob_start();

session_start();


if ($_SESSION['permiso'] >= 2):

    ?>


    <div title="Clientes" class="content-padding">


        <div style="display: flex;justify-content: space-between;">
            <div></div>

            <div style="display: flex;">

                <input id="search_cli" style="width:150px" class="easyui-textbox" onKeyPress="cliEnter(event);">
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchCli();">Buscar</a>
            </div>

        </div>



        <h3>Clientes</h3>

        <table id="dg" class="easyui-datagrid" url="../php/clientes/get_cli.php" toolbar="#toolbarC"
               fitColumns="true" singleSelect="true" idField="idCliente" sortName="idCliente" sortOrder="asc"
               style="min-height: 500px;max-height: 600px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow,onClickRow:btnsEnabled, remoteSort:false, fit:true">

            <thead>

            <tr>

                <th field="idCliente" width="100" sortable="true" sorter="mysort">idCliente</th>

                <th data-options="field:'razonsocial'" width="100">Cliente</th>

                <th field="rfc" width="100">RFC</th>

                <th field="direccion" width="100">Direccion</th>

                <th field="telefono" width="100">Telefono</th>

                <th field="alias" width="100">Alias</th>


            </tr>

            </thead>


        </table>

        <!-- toolbar promotores-->

        <? if ($_SESSION['permiso'] >= 3) { ?>

            <div id="toolbarC">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                   onclick="newCli()">Nuevo Cliente</a>

                <a href="javascript:void(0)" id="btn-cliente-editar" style="display: none" class="easyui-linkbutton"
                   iconCls="icon-edit" plain="true"
                   onclick="editCli()">Editar Cliente</a>

                <a href="javascript:void(0)" id="btn-cliente-remover" style="display: none" class="easyui-linkbutton"
                   iconCls="icon-remove" plain="true"
                   onclick="removeCli()">Remover Cliente</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
                   onclick="javascript:$('#dg').datagrid('reload');">Refrescar tabla</a>


            </div>

            <!-- dialogo Promotores-->

        <? } else {
        } ?>

        <div id="dlg_ncli" title="Infomacion del Cliente" class="easyui-dialog"
             style="width:600px;height:600px; padding-left:5px;"
             closed="true" buttons="#dlgcli-buttons">


            <form id="fm_clinew" class="form-style" method="post" novalidate>

                <ul>
                    <li>
                        <label for="razonsocial">Cliente</label>
                        <input id="razonsocial" name="razonsocial" class="easyui-validatebox"
                               style=" width:250px" data-option="required:true" required>
                    </li>
                    <li>
                        <label for="nombre_c">Nombre Contrato</label>
                        <input id="nombre_c" name="nombre_c" class="easyui-validatebox" style=" width:250px"
                               data-option="required:true" required>
                    </li>
                    <li>

                        <label for="calle">Calle</label>
                        <input class="easyui-validatebox" id="calle" name="calle">
                    </li>
                    <li>
                        <label for="no_ext">No. Ext</label>
                        <input class="easyui-validatebox" id="no_ext" name="no_ext" size="10">
                    </li>
                    <li>
                        <label for="no_int">No. Int</label>
                        <input class="easyui-validatebox" id="no_int" name="no_int" size="10">
                    </li>
                    <li>
                        <label for="colonia">Colonia</label>
                        <input class="easyui-validatebox" id="colonia" name="colonia">
                    </li>
                    <li>
                        <label for="cp">CP</label>
                        <input class="easyui-validatebox" id="cp" name="cp" size="10">
                    </li>
                    <li>
                        <label for="idEstado">Estado</label>
                        <input id="idEstado" class="easyui-combobox" name="idEstado">
                    </li>
                    <li>
                        <label for="idMunicipio">Municipio</label>
                        <input id="idMunicipio" class="easyui-combobox"
                               data-options="valueField:'id',textField:'municipio',required:false"
                               name="idMunicipio">
                    </li>
                    <li>
                        <label for="rfc">RFC</label>
                        <input name="rfc" id="rfc" class="easyui-validatebox" data-option="required:true"
                               required>
                    </li>
                    <li>
                        <label for="telefono">Telefono</label>
                        <input name="telefono" id="telefono" class="easyui-validatebox">
                    </li>
                </ul>


            </form>

        </div>

        <div id="dlg_edit" style="padding:10px 20px;display:none;">
        </div>

        <div id="dlg_remove" style="padding:10px 20px;display:none;">
            <span><br><br>&nbsp;&nbsp;Estas seguro de eliminar el Cliente?</span>
        </div>

        <div id="dlgcli-buttons">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveCli()">Guardar</a>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogCl()">Cancelar</a>

        </div>

        <div id="dlg_view" style="padding:10px 20px;display:none;">
        </div>


    </div>
    <script>
        function hideDialogCl() {
            $('#dlg_ncli').dialog('close');
            $('#dlg_view').hide();
            $('#dlg_edit').hide();
            $('#dlg_remove').hide();
        }

        $('#idEstado').combobox({

            valueField: 'id',

            textField: 'nombre',

            url: '../php/get_estado.php',

            required: false,

            onSelect: function (record) {

                var url = '../php/get_municipios.php?idEstado=' + record.id;

                $('#idMunicipio').combobox('clear');


                $('#idMunicipio').combobox('reload', url);

            }

        });

        function onDblClickRow(index) {

            var idCliente = $('#dg').datagrid('getRows')[index]['idCliente'];


            $('#dlg_view').show();
            $('#dlg_view').dialog({
                title: 'Datos Cliente',
                href: '../php/clientes/view_cli.php?idCliente=' + idCliente,
                width: 380,
                height: 300,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $('#dlg_view').dialog('close');
                    }
                }]
            });

        }

        function mysort(a, b) {
            a = parseInt(a);
            b = parseInt(b);
            return (a > b ? 1 : -1);
        }

        function removeCli() {
            var row = $('#dg').datagrid('getSelected');


            if (row !== null) {

                var idCliente = row.idCliente;

                document.getElementById('dlg_remove').style.display = '';
                $('#dlg_remove').dialog({
                    title: 'Eliminar Cliente',
                    width: 400,
                    height: 180,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            removeClien(idCliente);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#dlg_remove').dialog('close');
                        }
                    }]
                });

            } else {
                alert("selecciona un item");
            }

        }

        function editCli() {
            var row = $('#dg').datagrid('getSelected');
            var idCliente = row.idCliente;

            $('#dlg_edit').show();
            $('#dlg_edit').dialog({
                title: 'Datos Cliente',
                href: '../php/clientes/edit_cli.php?idCliente=' + idCliente,
                width: 500,
                height: 400,
                buttons: [{
                    text: 'Ok',
                    iconCls: 'icon-ok',
                    handler: function () {
                        editClien(idCliente);
                    }
                }, {
                    text: 'Cancel',
                    handler: function () {
                        $('#dlg_edit').dialog('close');
                    }
                }]
            });


        }

        function btnsEnabled() {
            var row = $('#dg').datagrid('getSelected');
            if (row) {
                $('#btn-cliente-editar').css("display", "inline-block");
                $('#btn-cliente-remover').css("display", "inline-block");
            } else {
                $('#btn-cliente-editar').css("display", "none");
                $('#btn-cliente-remover').css("display", "none");
            }
        }
    </script>


<? endif;
?>