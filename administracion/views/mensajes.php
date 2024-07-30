<?php
ob_start();

session_start();


?>


<div id="mensajes" class="content-padding">

    <? if ($_SESSION['permiso'] >= 2) { ?>


        <h3 style="padding: 15px;">Mensajes</h3>

        <div title="Mensajes" >


            <table id="dg_msjs" class="easyui-datagrid" url="../php/mensajes/get_msjs.php" toolbar="#toolbarMsjs"
                   fitColumns="true" singleSelect="true" idField="id_mensaje" style="max-height: 600px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow">

                <thead>

                <tr>

                    <th field="id_mensaje" width="auto" sortable="true">idMensaje</th>

                    <th data-options="field:'marca'" width="auto">Marca</th>

                    <th data-options="field:'titulo'" width="auto">Titulo</th>

                    <th data-options="field:'asunto'" width="auto">Asunto</th>

                    <th data-options="field:'fecha'" width="auto">Fecha</th>

                    <th field="estatus" width="auto">Estatus</th>

                </tr>

                </thead>
            </table>

            <!-- toolbar mensajes-->
            <div id="toolbarMsjs">
                <? if ($_SESSION['permiso'] >= 3) { ?>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="newMsj()">Nuevo Mensaje</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="removeMsj()">Cancelar Mensaje</a>


                    <!-- dialogo Mensajes-->

                <? }
                if ($_SESSION['permiso'] >= 2) {
                    ?>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="editMsj()">Editar Mensaje</a>

                <? } ?>
                <? if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5') { ?>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-sendMsj" plain="true"
                       onclick="enviarMsj()">Enviar Mensaje</a>
                <? } ?>
                Estatus :
                <select id="estatus_msj" class="easyui-combobox" panelHeight="auto" style="width:100px" data-options="valueField:'id', textField:'text'">

                    <option value="1">Activo</option>

                    <option value="0">Cancelado</option>

                    <option value="2">Enviado</option>
                </select>
                <input id="search_msjs" style="width:100px" type="text" onKeyPress="msjsEnter(event);">
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchMsjs();">Buscar</a>
            </div>
            <div id="dlg_msjs" title="Infomacion del Mensaje" class="easyui-dialog" data-options="modal:true"
                 style="width:600px;height:500px;padding:10px 20px" closed="true" buttons="#dlg-butMsj">


                <form id="fm_msjnew" method="post" novalidate class="form-style">

                    <ul>
                        <li>
                            <label>Marca</label>
                            <input class="easyui-combobox" name="MarcaMsj" id="MarcaMsj"
                                   data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarca.php',required:true"
                                   required>
                        </li>

                        <li>

                            <label for="id-tipo-promo" >Tipo promtor</label>
                            <select id="id-tipo-promo" class="easyui-combobox" prompt="Tipo" style="width: 120px;" required>

                                <option value="1">Autoservicio</option>
                                <option value="2">Mayoreo</option>

                            </select>

                        </li>
                        <li>
                            <label>Titulo:</label>
                            <input name="titulo_msj" id="titulo_msj" class="easyui-validatebox"
                                   style=" width:100px" data-option="required:true" required>
                        </li>
                        <li>
                            <label>Asunto:</label>
                            <input name="asunto_msj" id="asunto_msj" class="easyui-validatebox"
                                   data-option="required:true" style=" width:180px" required>
                        </li>
                        <li>
                            <label>Contenido:</label>
                            <input maxlength="500" name="contenido_msj" id="contenido_msj"
                                   class="easyui-textbox" style=" width:200px; height:100px;" data-options="required:true,multiline:true">

                        </li>

                    </ul>


                </form>


            </div>


            <div id="dlg-butMsj">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMsj()">Guardar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogMsjs()">Cancelar</a>

            </div>

            <div id="msj_view" style="padding:10px 20px;display:none;">
            </div>
            <div id="msj_edit" style="padding:10px 20px;display:none;">
            </div>
            <div id="msj_remove" style="padding:10px 20px;display:none;">
                <span><br><br>&nbsp;&nbsp;Estas seguro de cancelar el Mensaje?</span>
            </div>
            <div id="dlg_sendMsj" style="padding:10px 20px; display:none;">
                <span><br><br>&nbsp;&nbsp;Estas seguro de enviar el Mensaje?</span>
            </div>


        </div>
        <script>


            function searchMsjs(idEstatus) {
                let buscarmsj = $('#search_msjs').val();
                let filtroE = idEstatus;

                //console.log(filtroE);


                if (buscarmsj === "") {
                    buscarmsj = '*';
                }

                if (buscarmsj !== "" || filtroE !== "") {
                    $.ajax({
                        type: 'POST',
                        url: '../php/mensajes/get_msjs.php',
                        data: {buscarMsj: buscarmsj, filtroE: filtroE},

                        success: function (data) {
                            $('#dg_msjs').datagrid({
                                data: jQuery.parseJSON(data)
                            });

                        },

                        error: function (jqXHR, textStatus, error) {

                            alert("error: " + jqXHR.responseText + " " + textStatus);
                        }
                    });
                }
                else {
                    $('#dg_msjs').datagrid('reload');
                }

            }


            function hideDialogMsjs() {
                $('#dlg_msjs').dialog('close');
                $('#msj_view').hide();
                $('#msj_edit').hide();
                $('#msj_remove').hide();
            }
            function onDblClickRow(index) {

                var idMensaje = $('#dg_msjs').datagrid('getRows')[index]['id_mensaje'];
                $('#msj_view').show();

                $('#msj_view').dialog({
                    title: 'Datos Mensaje',
                    href: '../php/mensajes/view_msj.php?idMensaje=' + idMensaje,
                    width: 700,
                    height: 520,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#msj_view').dialog('close');
                        }
                    }]
                });

            }
            function editMsj() {
                var row = $('#dg_msjs').datagrid('getSelected');
                var idMsj = row.id_mensaje;
                $('#msj_edit').show();

                $('#msj_edit').dialog({
                    title: 'Editar Mensaje',
                    href: '../php/mensajes/edit_msj.php?idMensaje=' + idMsj,
                    width: 600,
                    height: 500,
                    center:true,
                    modal:true,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            editMsje();
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#msj_edit').dialog('close');
                        }
                    }]
                });
            }
            function removeMsj() {
                var row = $('#dg_msjs').datagrid('getSelected');
                var idMsj = row.id_mensaje;

                document.getElementById('msj_remove').style.display = '';
                $('#msj_remove').dialog({
                    title: 'Eliminar Mensaje',
                    width: 400,
                    height: 180,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            removeMsje(idMsj);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#msj_remove').dialog('close');
                        }
                    }]
                });
            }
            $('#estatus_msj').combobox({

                onSelect: function (record) {
                    searchMsjs(record.id);
                    //console.log(record.id);
                }
            });


            function enviarMsj() {

                $('#dlg_sendMsj').show();

                $('#dlg_sendMsj').dialog({
                    title: 'Enviar Solicitud',
                    width: 450,
                    height: 180,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            save_EnvioMsj();
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#dlg_sendMsj').dialog('close');
                        }
                    }]
                });
            }

            $('#dg_msjs').datagrid({

                onSelect: function (index, row) {
                    if (row.estatus != "ACTIVO") {
                        $(this).datagrid('unselectRow', index);
                    }
                }

            });
        </script>

    <? } else {
    } ?>

    <!-- FIN MATERIALES-->

</div>

