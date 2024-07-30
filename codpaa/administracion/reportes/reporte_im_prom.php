<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**

 * Created by Dreamweaver.

 * User: Christian

 * Date: 30/07/14

 * Time: 16:57

 */

include_once('../connexion/bdManager.php');
include_once('../php/seguridad.php');
?>


<div id="ImProm" class="easyui-layout" style="height: 800px; width:100%;">



    <div data-options="region:'north',title:'Filtros',split:true,collapsible:false" style="width:100%; height:160px;">



            <form id="filtrosReporte" method="post" >



                <table cellpadding="6"  width="100%" id="filtros_fren">
					<tbody>
                    <tr>

                     <td><label for="fechaInicio">Fecha inicio</label></td>

                        <td>
                        <input id="fechaInicio" class="easyui-datebox"  data-options="formatter:myformatter,parser:myparser" required></td>

                    
 						<td><label for="fechaFin">Fecha fin</label></td>

                        <td><input id="fechaFin" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser" required>
                        </td>


						<td><label for="MarcaIm">Marca</label></td>

                        <td><input id="MarcaIm" name="MarcaIm"></td>

					</tr>
                    <tr>
                    <td><label for="tipoProd">Tipo de Producto</label></td>

                     <td><input id="tipoProd" name="tipoProd" class="easyui-combobox" data-options="valueField:'nombreTipo',textField:'nombreTipo',required:false,multiple:true"></td>
                        
                     <td><label for="Formatos">Formato</label></td>

                        <td><input id="Formatos" class="easyui-combobox" name="Formatos"  ></td>
                        
                    </tr>
   
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteImProm()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRImProm()">Limpiar</a>

            </div>

            <script  type="text/javascript">


				$('#Formatos').combobox({

                    valueField:'idFormato',

                    textField:'cadena',
					
					multiple:true,

                    url:'../php/get_formato.php',

                    required:false

                });
				

                $('#nombreEstado').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_estado.php',

                    required:false

                });
				
				 $('#MarcaIm').combobox({

                    valueField:'idMarca',

                    textField:'nombre',

                    url:'../php/getMarca.php',

                    required:true,
					
					
                    onSelect:function(record){

					  var url = '../php/getTipoProd.php?idMarca='+record.idMarca;

                        $('#tipoProd').combobox('clear');

                        $('#tipoProd').combobox('reload',url);
					
                    }


                });
			

            </script>







        </div>


    <!-- end north -->



    <!-- start center -->

    <div id="repor_impcenter" data-options="region:'center',title:'Datos',split:true,collapsible:false" style="width: auto;height: 100%;" toolbar="#toolsFren" >
	<div id="toolsFren">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true" onClick="exportarImProm()">Exportar</a>
        </div>

 		<div id="ventanaImProm"></div>

    </div>

    <!-- end center -->


    <div id="dlgImProm" style="width: 350px;height: 150px;"></div>
</div>



