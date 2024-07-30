<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 29/07/14

 * Time: 16:35

 */

ob_start();

session_start();
include_once('../php/seguridad.php');


?>



<div id="promotores">

    <? if($_SESSION['permiso'] >=2){?>

        <div title="Promotores" style="padding:10px;">



            <table id="dg" class="easyui-datagrid" url="../php/usersW/get_usuarios.php" toolbar="#toolbar1" fitColumns="true" singleSelect="true" idField="idUsuario" sortName="idUsuario" sortOrder="asc" style="height:500px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow">

                <thead>

                <tr>

                    <th data-options="field:'idUsuario'" width="auto" sortable="true">idUsuario</th>

                    <th data-options="field:'nombre'" width="auto">Nombre</th>

                    <th data-options="field:'user'"  width="auto">Usuario</th>

                    <th data-options="field:'perfil'" width="auto">Perfil</th>

                </tr>

                </thead>



            </table>

            <!-- toolbar promotores-->

            <? if($_SESSION['permiso'] >= 3){  ?>

                <div id="toolbar1">

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newUserW();">Nuevo Usuario</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeW();">Remover Usuario</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:$('#dg').datagrid('reload');">Refrescar tabla</a>
                    
                     <input id="search_btm" style="width:100px" type="text" onKeyPress="userWEnter(event);">
                     <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchUserW();">Buscar</a>

                </div>

                <!-- dialogo Promotores-->

            <? }else{} ?>

            <div id="dlg_user" title="Infomacion del Usuario" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true" buttons="#dlg-buttons">


                <form id="fm" method="post" novalidate>

                    <table>

                        <tr>

                            <div class="fitem">

                                <td><label>Nombre:</label></td>

                                <td><input name="nombre" id="nombre" class="easyui-validatebox" style=" width:250px" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Perfil:</label></td>

                                <td><input class="easyui-combobox" name="Perfil" id="Perfil" data-options="valueField:'id_perfil',textField:'perfil',url:'../php/get_perfil.php'" required></td>



                            </div>

                        </tr>
                     
                         <tr style="display:none;" id="div_sup">

                            <div class="fitem" >

                                <td><label>SUPERVISOR:</label></td>

                                <td><input class="easyui-combobox" name="idSupervisor" id="idSupervisor" data-options="valueField:'idSupervisores',textField:'nombreSupervisor',url:'../php/get_supervisor.php'" style=" width:150px"></td>



                            </div>

                        </tr>
                   
                     
                         <tr style="display:none;" id="div_cli">

                            <div class="fitem" >

                                <td><label>CLIENTE:</label></td>

                                <td><input class="easyui-combobox" name="idCliente" id="idCliente" data-options="valueField:'idCliente',textField:'razonsocial',url:'../php/get_cliente.php'" style=" width:150px"></td>



                            </div>

                        </tr>
                     
                        <tr>

                            <div class="fitem">

                                <td><label>Usuario:</label></td>

                                <td><input name="usuario" id="usuario" class="easyui-validatebox" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Contraseña:</label></td>

                                <td><input name="password" id="password" class="easyui-validatebox" required></td>

                            </div>

                        </tr>
                         <tr>

                            <div class="fitem">

                                <td><label>Email:</label></td>

                                <td><input name="email" id="email" class="easyui-validatebox" data-options="required:false,validType:'email'"></td>

                            </div>

                        </tr>
 
                    </table>

                </form>

            </div>
			<div id="dlg_edit" style="padding:10px 20px;" >
            </div>
            <div id="dlg_remove" style="padding:10px 20px;display:none;" >
            <span>Estas seguro de eliminar el Usuario?</span>
            </div>
            <div id="dlg-buttons">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUserW()">Guardar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_user').dialog('close')">Cancelar</a>

            </div>





        </div>

<script language="javascript">
 $('#Perfil').combobox({

                    onSelect:function(record){

					if(record.id_perfil==3)
					{
						document.getElementById('div_sup').style.display='';
						var url = '../php/get_supervisor.php';
						$('#idSupervisor').combobox('clear');
						 $('#idSupervisor').combobox('reload',url);
						 
						 document.getElementById('div_cli').style.display='none';
						}
					
					else if(record.id_perfil==6)
					{
						document.getElementById('div_cli').style.display='';
						var url = '../php/get_cliente.php';
						$('#idCliente').combobox('clear');
						 $('#idCliente').combobox('reload',url);
						 document.getElementById('div_sup').style.display='none';
						}
 					}

               });


function onDblClickRow(index){
	
	var idUsuario=$('#dg').datagrid('getRows')[index]['idUsuario'];
	
	 $('#dlg_edit').dialog({
		 title: 'Editar Usuario',
		 href: '../php/usersW/edit_userW.php?idUsuario='+idUsuario,
		 width: 400,
		 height: 280,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						editUserW(idUsuario);
					}
				},{
					text:'Cancel',
					handler:function(){
					$('#dlg_edit').dialog('close');	
					}
				}]
		 });
	
		}
		
function removeW()
{
	var row = $('#dg').datagrid('getSelected');
	var idUsuario = row.idUsuario;
	
	document.getElementById('dlg_remove').style.display='';
	$('#dlg_remove').dialog({
		 title: 'Eliminar Usuario',
		 width: 400,
		 height: 180,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						removeUserW(idUsuario);
					}
				},{
					text:'Cancel',
					handler:function(){
					$('#dlg_remove').dialog('close');	
					}
				}]
		 });
	}
</script>
        
    <? }else{} ?>

    <!-- FIN PROMOTORES-->

</div>

