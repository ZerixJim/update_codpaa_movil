<?php
session_start();
include_once('../php/seguridad.php');
/**

 * Created by Dreamweaver.

 * User: Christian
 * modified: Gustavo Ibarra

 * Date: 22/03/16

 * Time: 12:13

 */



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

 ?>

    <div title="Solicitud de Materiales" id="solicitudM" closable="true" style="height:100%;">

       <table id="dgSolicitudM" class="easyui-datagrid" toolbar="#toolbarSolicitudM"  fitColumns="true" idField="idCelular"
			   style="height:100%;" >

            <thead>

            <tr>

				<th field="idCelular" width="8" sortable="true">idPromotor</th>

				<th field="promotor" width="20" sortable="true">Promotor</th>

				<th field="fecha" width="8" sortable="true">fecha</th>


            </tr>

            </thead>

        </table>


		<div id="toolbarSolicitudM">


			<!--<select class="easyui-combobox" panelHeight="auto" style="width:100px;" id="estatus_fil" prompt="Estatus">
				<option value="">Todos</option>
				<option value="0">Cancelado</option>
				<option value="1">Activo</option>
				<option value="2">Enviado</option>
				<option value="3">Recibido</option>
			</select>-->
			<input id="search_solicM" name="search_solicM" style="width:100px;padding: 5px 5px; margin: 8px 8px; border: 1px solid #ccc;
			border-radius: 4px;box-sizing: border-box;" type="text"
				   onKeyPress="solicitudMEnter(event);" placeholder="Filtro..">
			<a href="#" class="easyui-linkbutton" iconCls="icon-search" onClick="searchSolicitudM();">Buscar</a>

		</div>

		<div id="dlg_viewSM" style="padding:0px 5px; display:none;">
		</div>
		<div id="dlg_sendSM" style="padding:10px 20px;" class="easyui-dialog" closed="true">
		</div>
               

    </div>
	<script language="javascript">


		var dataGridM = $('#dgSolicitudM');

		dataGridM.datagrid({

			view:detailview,
			singleSelect:false,
			checkOnSelect:false,
			url:'../php/materiales/querySolicitudM.php',
			autoRowHeight:false,
			pageSize:50,method: 'get',
			/*onDblClickRow: onDblClickRow,*/
			remoteSort:false,

			detailFormatter:function(index, row){

				return '<div class="ddv" style="padding:2px"><table class="ddsubM"></table></div>';

			},

			onExpandRow:function(index, row){

				var count = dataGridM.datagrid('getRows').length;
				/*other option is expandRow*/
				for (var i=0; i< count ; i++ ){

					if(i != index)
						$(this).datagrid('collapseRow', i);


				}

				var ddvr = $(this).datagrid('getRowDetail', index).find('table.ddsubM');
				ddvr.datagrid({

					url:'../php/materiales/get_materiales_id.php',
					queryParams:{
						idPromotor:row.idCelular,
						mes:row.mes
					},
					fitColumns:true,
					rownumbers:true,
					height:'200px',
					columns:[[
						{field:'idSolicitud', title:'ID Solicitud', width:300},
						{field:'idMaterial', title:'ID Material', width:300},
						{field:'material', title:'Material', width:300},
						{field:'cantidad', title:'Cantidad', width:300},
						{field:'estatus', title:'Estatus', width:300}
					]],
					onResize:function(){

					},
					loadMsg:'Cargando..',

					toolbar:[{
						iconCls: 'icon-send',
						text:'enviar solicitud',
						handler: function(){

							var selections = ddvr.datagrid('getSelections');
							if (selections.length > 0){

								enviarSol(selections);

								/*console.log(selections);*/
							}else {

								$.messager.alert('Aviso', 'no seleccionaste solicitud');

							}
						}
					}],


					onLoadSuccess:function(){
						setTimeout(function(){
							dataGridM.datagrid('fixDetailRowHeight',index);
						},0);
					},

					onSelect: function (index, row) {
						if (row.estatus != "ACTIVO") {
							$(this).datagrid('unselectRow', index);
						}
					}

				});

				$(this).datagrid('fixDetailRowHeight', index);
			}



		});


		function hideDialogSM() {
			$('#dlg_viewSM').hide();
			$('#dlg_sendSM').hide();
		}
		function onDblClickRow(index) {

			var idSolicitudM = $('#dgSolicitudM').datagrid('getRows')[index]['id_mat_solicitud'];
			var Estatus = $('#dgSolicitudM').datagrid('getRows')[index]['estatus'];

			$('#dlg_viewSM').show();
			$('#dlg_viewSM').dialog({
				title: 'Datos Solicitud',
				href: '../php/materiales/view_solicitudM.php?idSolicitudM=' + idSolicitudM,
				width: 600,
				height: 350,
				buttons: [{
					text: 'Ok',
					iconCls: 'icon-ok',
					handler: function () {

						if (Estatus == "RECIBIDO") {
							$('#dlg_viewSM').dialog('close');
						}
						else {
							save_editSM(idSolicitudM);
						}
					}
				}, {
					text: 'Cancel',
					handler: function () {
						$('#dlg_viewSM').dialog('close');
						hideDialogSM();
					}
				}]
			});

		}
		$('#estatus_fil').combobox({

			onSelect: function (record) {
				searchSolicitudM();
			}
		});


		function enviarSol(object) {



			var guia = $('#num_guiaSM').val();

			var dialogo = $('#dlg_sendSM');
			dialogo.dialog({
				title: 'Enviar Solicitud',
				href: '../php/materiales/enviar_solM.php',
				width: 450,
				height: 180,
				buttons: [{
					text: 'Ok',
					iconCls: 'icon-ok',
					handler: function () {

						var validate = $('#fm_sendSM').form('validate');

						if(validate){

							dialogo.dialog('close');
							saveSolicitud(object, guia);
						}
					}
				}, {
					text: 'Cancel',
					handler: function () {
						$('#dlg_sendSM').dialog('close');
					}
				}]
			}).dialog('open').dialog('center');





		}


		function saveSolicitud(data, guia){


			$.ajax({

				type: 'POST',
				dataType:'json',

				url: '../php/materiales/envia_solicitudM.php',


				data: {data: data, num_guia: guia},

				success:function(data){

					$('.ddsubM').datagrid('reload');

				},

				complete: function () {
				},

				error: function (jqx, status, thrown) {

					console.log('ajax loading error...' + thrown);

					return false;

				}

			});
		}



	</script>


<? 



}else{

    echo "valor= ".$_SESSION['usuario'];

}



?>







