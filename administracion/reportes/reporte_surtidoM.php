<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**

 * Created by PhpStorm.

 * User: Christian

 * Date: 30/07/14

 * Time: 16:57

 */

include_once('../connexion/bdManager.php');
include_once('../php/seguridad.php');
?>


<div id="surtido_muebles" class="easyui-layout" style="height: 800px; width:100%;">



    <div data-options="region:'north',title:'Filtros',split:true,collapsible:false" style="width:100%; height:180px;">



            <form id="filtrosReporte" method="post" >



                <table cellpadding="6"  width="100%" id="filtros_surt">
					<tbody>
                    <tr>

                     <td><label for="fechaInicio">Fecha inicio</label></td>

                        <td>
                        <input id="fechaInicio" class="easyui-datebox"  data-options="formatter:myformatter,parser:myparser" required></td>

                    
 						<td><label for="fechaFin">Fecha fin</label></td>

                        <td><input id="fechaFin" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser" required>
                        </td>
						 

                        <td>
                        <label for="idTipoTie">Tipo Tienda</label></td>
                        <td>
                        <input id="idTipoTie" class="easyui-combobox" name="idTipoTie" ></td>
                        
                  

						

					</tr>
                    <tr>
                    <td><label for="MarcaSurt">Marca</label></td>

                        <td><input id="MarcaSurt" name="MarcaSurt"></td>
                        
                    <td><label for="ProdSurt">Producto</label></td>

                     <td><input id="ProdSurt" name="ProdSurt" class="easyui-combobox" data-options="valueField:'idProducto',textField:'nombre',required:true,multiple:true"></td>
                        
                    <td><label for="nombreEstado">Estado</label></td>

                        <td><input id="nombreEstado" class="easyui-combobox" name="nombreEstado" ></td>
                    </tr>
   
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteSurt()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRSurt()">Limpiar</a>

            </div>

            <script  type="text/javascript">

			

					$('#idTipoTie').combobox({

                    valueField:'idTipo',

                    textField:'nombre',

                    url:'../php/getTiendaTipo.php',

                    required:true


                });

                    $('#nombreEstado').combobox({

                        valueField:'id',

                        textField:'nombre',

                        url:'../php/get_estado.php',

                        required:false

                    });

				 $('#MarcaSurt').combobox({

                    valueField:'idMarca',

                    textField:'nombre',

                    url:'../php/getMarca.php',

                    required:true,
					
					
                    onSelect:function(record){

					  var url = '../php/getProducto.php?idMarca='+record.idMarca;

                        $('#ProdSurt').combobox('clear');

                        $('#ProdSurt').combobox('reload',url);
					
                    }


                });
			

            </script>







        </div>


    <!-- end north -->



    <!-- start center -->

    <div id="repor_smcenter" data-options="region:'center',title:'Datos',split:true,collapsible:false" style="width: auto;height: 100%;" toolbar="#toolsSurt" >
	<div id="toolsSurt">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true" onClick="exportarSurt();">Exportar</a>
        </div>

 		<div id="ventanaSurt"></div>

    </div>

    <!-- end center -->


    <div id="dlgSurt" style="width: 350px;height: 150px;"></div>
</div>



