<?php
ob_start();

session_start();


?>


<div id="materiales" style="height:100%;">

    <? if ($_SESSION['permiso'] >= 2) { ?>

        <div title="Materiales" style="height:100%;">


            <table id="dg_mat" class="easyui-datagrid" url="../php/materiales/get_mat.php" toolbar="#toolbarP"
                   fitColumns="true" singleSelect="true" idField="id_material" sortName="id_material" sortOrder="asc"
                   style="height:100%;" data-options="method: 'get',
				onDblClickRow: onDblClickRow,remoteSort:false">

                <thead>

                <tr>

                    <th field="id_material" width="auto" sortable="true" sorter="mysort">idMaterial</th>

                    <th data-options="field:'material'" width="auto" sortable="true">Material</th>

                    <th data-options="field:'unidad'" width="auto" sortable="true">Unidad</th>

                    <th field="solicitud_max" width="auto" sortable="true" sorter="mysort">Solicitud Max</th>

                </tr>

                </thead>


            </table>

            <!-- toolbar promotores-->
            <div id="toolbarP" style="padding: 5px">
                <? if ($_SESSION['permiso'] >= 3) { ?>



                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="removeMat()">Remover Material</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="editMat()">Editar Material</a>


                    <!-- dialogo Promotores-->

                <? }
                   if($_SESSION['permiso'] >= 2){

                       ?>
                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                          onclick="newMat()">Nuevo Material</a>
                        <?


                   }
                ?>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true"
                   onclick="javascript:$('#dg_mat').datagrid('reload');">Refrescar tabla</a>
                <input id="search_mat" style="width:100px; border: 1px solid #ccc; padding: 5px 2px;" type="text"
                       onKeyPress="matEnter(event);" placeholder="filtro">
                <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchMat();">Buscar</a>
            </div>
            <div id="dlg_mat" title="Infomacion del Material" class="easyui-dialog"
                 style="width:400px;height:250px;padding:10px 20px" closed="true" buttons="#dlg-butMa">


                <form id="fm_matnew" method="post" novalidate>

                    <table>

                        <tr>

                            <div class="fitem">

                                <td><label>Material:</label></td>

                                <td><input name="nombre_mat" id="nombre_mat" class="easyui-validatebox"
                                           style=" width:180px" data-option="required:true" ; required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Unidad:</label></td>

                                <td><input name="unidadM" id="unidadM" class="easyui-validatebox"
                                           data-option="required:true" style=" width:100px" required></td>


                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td><label>Solicitud Max:</label></td>

                                <td><input name="solicitud_max" id="solicitud_max" class="easyui-validatebox"
                                           style=" width:50px"></td>

                            </div>

                        </tr>

                    </table>

                </form>


            </div>


            <div id="dlg-butMa">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMat()">Guardar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="hideDialogMat()">Cancelar</a>

            </div>

            <div id="mat_view" style="padding:10px 20px;display:none;">
            </div>
            <div id="mat_edit" style="padding:10px 20px;display:none;">
            </div>
            <div id="mat_remove" style="padding:10px 20px;display:none;">
                <span><br><br>&nbsp;&nbsp;Estas seguro de eliminar el Material?</span>
            </div>


        </div>
        <script language="javascript">
            function hideDialogMat() {
                $('#dlg_mat').dialog('close');
                $('#mat_view').hide();
                $('#mat_edit').hide();
                $('#mat_remove').hide();
            }
            function onDblClickRow(index) {

                var idMaterial = $('#dg_mat').datagrid('getRows')[index]['id_material'];
                $('#mat_view').show();

                $('#mat_view').dialog({
                    title: 'Datos Material',
                    href: '../php/materiales/view_mat.php?idMaterial=' + idMaterial,
                    width: 450,
                    height: 250,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $('#mat_view').dialog('close');
                        }
                    }]
                });

            }
            function mysort(a, b) {
                a = parseInt(a);
                b = parseInt(b);
                return (a > b ? 1 : -1);
            }
            function editMat() {
                var row = $('#dg_mat').datagrid('getSelected');
                var idMat = row.id_material;
                $('#mat_edit').show();

                $('#mat_edit').dialog({
                    title: 'Editar Material',
                    href: '../php/materiales/edit_mat.php?idMaterial=' + idMat,
                    width: 450,
                    height: 250,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            editMate();
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#mat_edit').dialog('close');
                        }
                    }]
                });
            }
            function removeMat() {
                var row = $('#dg_mat').datagrid('getSelected');
                var idMat = row.id_material;

                document.getElementById('mat_remove').style.display = '';
                $('#mat_remove').dialog({
                    title: 'Eliminar Material',
                    width: 400,
                    height: 180,
                    buttons: [{
                        text: 'Ok',
                        iconCls: 'icon-ok',
                        handler: function () {
                            removeMate(idMat);
                        }
                    }, {
                        text: 'Cancel',
                        handler: function () {
                            $('#mat_remove').dialog('close');
                        }
                    }]
                });
            }
        </script>

    <? } else {
    } ?>

    <!-- FIN MATERIALES-->

</div>

