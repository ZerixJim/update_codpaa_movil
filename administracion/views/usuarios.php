<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 29/07/14
 * Time: 16:35
 */

ob_start();

session_start();


?>


<div id="usuarios" class="content-padding">

    <? if ($_SESSION['permiso'] >= 2) { ?>





        <div style="display: flex;justify-content: space-between;padding: 15px;">

            <div></div>

            <div>

                <input id="search_btm" style="width:150px; border: 1px solid #ccc;padding: 10px;" type="text" onKeyPress="userWEnter(event);" placeholder="filtro" >
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchUserW();"
                   style="padding: 5px;background-color: #0D98FB;color: #FFFFFF;">Buscar</a>

            </div>
        </div>



        <h3>Usuarios</h3>
        <div>


            <table id="dg" class="easyui-datagrid" url="../php/usersW/get_usuarios.php" toolbar="#toolbarUs"
                   fitColumns="true" singleSelect="true" idField="idUsuario" sortName="idUsuario" sortOrder="asc"
                   style="width: 100%;height:600px;min-height: 550px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow">

                <thead>

                <tr>

                    <th data-options="field:'idUsuario'" width="auto" sortable="true">idUsuario</th>

                    <th data-options="field:'nombre'" width="auto">Nombre</th>

                    <th data-options="field:'user'" width="auto">Usuario</th>

                    <th data-options="field:'perfil'" width="auto">Perfil</th>

                    <th data-options="field:'estatus'" width="5" formatter="statusFormatter"></th>


                </tr>

                </thead>


            </table>

            <!-- toolbar promotores-->

            <? if ($_SESSION['permiso'] >= 3) { ?>

                <div id="toolbarUs">

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="newUserW();">Nuevo Usuario</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="removeW();">Remover Usuario</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
                       onclick="$('#dg').datagrid('reload');">Refrescar tabla</a>



                </div>

                <!-- dialogo Promotores-->

            <? } else {
            } ?>


            <!--Dialogo crear nuevo usuario-->
            <div id="dlg_user" title="Infomacion del Usuario" class="easyui-dialog"
                 style="width:400px;height:280px;padding:10px 20px" closed="true" buttons="#dlg-buttons">


                <form id="fm_usnew" method="post" novalidate>

                    <table>

                        <tr>

                            <div class="fitem">

                                <td><label>Nombre </label></td>

                                <td><input name="nombre" id="nombre" class="easyui-validatebox" style=" width:250px"
                                           data-option="required:true" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Perfil </label></td>

                                <td><input class="easyui-combobox" name="Perfil" id="Perfil"
                                           data-options="valueField:'id_perfil',textField:'perfil',url:'../php/get_perfil.php',required:true"
                                           required></td>


                            </div>

                        </tr>

                        <tr style="display:none;" id="div_sup">

                            <div class="fitem">

                                <td><label>SUPERVISOR </label></td>

                                <td><input class="easyui-combobox" name="idSupervisor" id="idSupervisor"
                                           data-options="valueField:'idSupervisores',textField:'nombreSupervisor',url:'../php/get_supervisor.php'"
                                           style=" width:150px"></td>


                            </div>

                        </tr>


                        <tr style="display:none;" id="div_cli">

                            <div class="fitem">

                                <td><label>CLIENTE </label></td>

                                <td><input class="easyui-combobox" name="idCliente" id="idCliente"
                                           data-options="valueField:'idCliente',textField:'razonsocial',url:'../php/get_cliente.php'"
                                           style=" width:150px"></td>


                            </div>

                        </tr>
                        <tr style="display:none;" id="div_ger">

                            <div class="fitem">

                                <td><label>GERENTE </label></td>

                                <td><input class="easyui-combobox" name="idGerente" id="idGerente"
                                           data-options="valueField:'idGerente',textField:'nombre',url:'../php/get_gerente.php'"
                                           style=" width:150px"></td>


                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td><label>Usuario:</label></td>

                                <td><input name="usuario" id="usuario" class="easyui-validatebox"
                                           data-option="required:true" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Contraseña:</label></td>

                                <td><input name="password" id="password" class="easyui-validatebox"
                                           data-option="required:true" required></td>

                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td><label>Email:</label></td>

                                <td><input name="email" id="email" class="easyui-validatebox"
                                           data-options="required:false,validType:'email'"></td>

                            </div>

                        </tr>

                    </table>

                </form>

            </div>
            <div id="dlg_edit" style="padding:10px 20px;display:none;">
            </div>
            <div id="dlg_remove" style="padding:10px 20px;display:none;">
                <span>Estas seguro de eliminar el Usuario?</span>
            </div>
            <div id="dlg-buttons">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
                   onclick="saveUserW()">Guardar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogUs()">Cancelar</a>

            </div>


        </div>

        <script>
            function hideDialogUs() {
                $('#dlg_user').dialog('close');
                $('#dlg_edit').hide();
                $('#dlg_remove').hide();

            }


            $('#Perfil').combobox({

                onSelect: function (record) {

                    let url;

                    if (record.id_perfil == 3 || record.id_perfil == 8) {
                        document.getElementById('div_sup').style.display = '';
                        url = '../php/get_supervisor.php';

                        var comboSuper = $('#idSupervisor');

                        comboSuper.combobox('clear');
                        comboSuper.combobox('reload', url);

                        document.getElementById('div_cli').style.display = 'none';
                        document.getElementById('div_ger').style.display = 'none';
                    }

                    else if (record.id_perfil == 6) {

                        var comboClient = $('#idCliente');

                        document.getElementById('div_cli').style.display = '';
                        url = '../php/get_cliente.php';
                        comboClient.combobox('clear');
                        comboClient.combobox('reload', url);
                        document.getElementById('div_sup').style.display = 'none';
                        document.getElementById('div_ger').style.display = 'none';



                    }
                    else if (record.id_perfil == 9) {
                        document.getElementById('div_ger').style.display = '';
                        url = '../php/get_gerente.php';
                        $('#dGerente').combobox('clear');
                        $('#idGerente').combobox('reload', url);
                        document.getElementById('div_sup').style.display = 'none';
                        document.getElementById('div_cli').style.display = 'none';
                    }
                }

            });


            function statusFormatter(index, row) {

                let color, content;

                if (row.estatus > 0 ) {

                    color = '#a0ff9c';
                    content = 'Activo'

                }else{

                    color = '#ff777e';
                    content = 'Desactivados';
                }

                return `<span style="color: #FFFFFF; border-radius: 5px; padding: 5px 2px; font-weight: bold; text-shadow: #717171 0 0 5px;
                                background-color: ${color};"> ${content} </span>`;
            }


            function onDblClickRow(index) {

                let dlgEdit  = $('#dlg_edit');

                let idUsuario = $('#dg').datagrid('getRows')[index]['idUsuario'];


                dlgEdit.show();

                dlgEdit.dialog({
                    title: 'Editar Usuario',
                    href: '../php/usersW/edit_userW.php?idUsuario=' + idUsuario,
                    width: 400,
                    height: 280,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            editUserW(idUsuario);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#dlg_edit').dialog('close');
                        }
                    }]
                });

            }

            function removeW() {
                let row = $('#dg').datagrid('getSelected');
                let idUsuario = row.idUsuario;

                document.getElementById('dlg_remove').style.display = '';
                $('#dlg_remove').dialog({
                    title: 'Eliminar Usuario',
                    width: 400,
                    height: 180,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            removeUserW(idUsuario);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#dlg_remove').dialog('close');
                        }
                    }]
                });
            }
        </script>

    <? } ?>

    <!-- FIN PROMOTORES-->

</div>

