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



<div id="productos">

    <? if($_SESSION['permiso'] >=2){?>

        <div title="Productos" style="padding:10px;">



            <table id="dg_prod" class="easyui-datagrid" url="../php/productos/get_prod.php" toolbar="#toolbarP" fitColumns="true" singleSelect="true" idField="idProducto" sortName="idProducto" sortOrder="asc" style="height:500px;" data-options="method: 'get',
				onDblClickRow: onDblClickRow">

                <thead>

                <tr>

                    <th field="idProducto" width="auto" sortable="true">idProducto</th>

                    <th  data-options="field:'nombre'" width="auto">Nombre</th>
                    
                    <th field="Marca" width="auto">Marca</th>

                    <th field="tipo" width="auto">Tipo</th>

                </tr>

                </thead>



            </table>

            <!-- toolbar promotores-->

            <? if($_SESSION['permiso'] >= 3){  ?>

                <div id="toolbarP">

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newProd()">Nuevo Producto</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editProd()">Editar Producto</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeProd()">Remover Producto</a>

                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-reload" plain="true" onclick="javascript:$('#dg_prod').datagrid('reload');">Refrescar tabla</a>

					 <input id="search_prod" style="width:100px" type="text" onKeyPress="prodEnter(event);">
                     <a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchProd();">Buscar</a>
                </div>

                <!-- dialogo Promotores-->

            <? }else{} ?>

            <div id="dlg_prod" title="Infomacion del Producto" class="easyui-dialog" style="width:400px;height:280px;padding:10px 20px" closed="true" buttons="#dlg-buttons">


                <form id="fm" method="post" novalidate>

                    <table>

                        <tr>

                            <div class="fitem">

                                <td><label>Nombre:</label></td>

                                <td><input name="nombre_prod" id="nombre_prod" class="easyui-validatebox" style=" width:180px" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">
                            
                                <td><label>Presentacion:</label></td>

                                <td><input name="presentacion" id="presentacion" class="easyui-validatebox"  required></td>


                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td><label>Marca:</label></td>

                                
                                <td><input class="easyui-combobox" name="Marca" id="Marca" data-options="valueField:'idMarca',textField:'nombre',url:'../php/getMarca.php'" required></td>

                            </div>

                        </tr>

                        <tr>

                            <div class="fitem">

                                <td><label>Codigo de Barras:</label></td>

                                <td><input name="codigo" id="codigo" class="easyui-validatebox"></td>

                            </div>

                        </tr>
                        
                        <tr>

                            <div class="fitem">

                                <td><label>Tipo Producto:</label></td>

                                <td><input name="tipo_prod" id="tipo_prod" class="easyui-validatebox" ></td>

                            </div>

                        </tr>
                        
                         <tr>

                            <div class="fitem">

                                <td><label>Modelo:</label></td>

                                <td><input name="modelo_prod" id="modelo_prod" class="easyui-validatebox" ></td>

                            </div>

                        </tr>

                    </table>

                </form>





            </div>
            


            <div id="dlg-buttons">

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveProd()">Guardar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_prod').dialog('close')">Cancelar</a>

            </div>
            
            <div id="prod_view" style="padding:10px 20px;" >
              </div>
          <div id="prod_edit" style="padding:10px 20px;" >
          </div>
          <div id="prod_remove" style="display:none;" >
            <span><br><br>&nbsp;&nbsp;Estas seguro de eliminar el Producto?</span>
            </div>





        </div>
<script language="javascript">

function onDblClickRow(index){
	
	var idProducto=$('#dg_prod').datagrid('getRows')[index]['idProducto'];
	
	 $('#prod_view').dialog({
		 title: 'Datos Producto',
		 href: '../php/productos/view_prod.php?idProducto='+idProducto,
		 width: 400,
		 height: 250,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						$('#prod_view').dialog('close');	
					}
				}]
		 });
	
		}
function editProd()
{
	var row = $('#dg_prod').datagrid('getSelected');
	var idProd = row.idProducto;
	
	$('#prod_edit').dialog({
		 title: 'Editar Producto',
		 href: '../php/productos/edit_prod.php?idProducto='+idProd,
		 width: 500,
		 height: 600,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						editProduc(idProd);
					}
				},{
					text:'Cancel',
					handler:function(){
					$('#prod_edit').dialog('close');	
					}
				}]
		 });
	}
function removeProd()
{
	var row = $('#dg_prod').datagrid('getSelected');
	var idProd = row.idProducto;
	
	document.getElementById('prod_remove').style.display='';
	$('#prod_remove').dialog({
		 title: 'Eliminar Producto',
		 width: 400,
		 height: 180,
		buttons: [{
					text:'Ok',
					iconCls:'icon-ok',
					handler:function(){
						removeProduc(idProd);
					}
				},{
					text:'Cancel',
					handler:function(){
					$('#prod_remove').dialog('close');	
					}
				}]
		 });
	}
	</script>
        
    <? }else{} ?>

    <!-- FIN PROMOTORES-->







</div>

