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


<div id="tiendas_visitadas" class="easyui-layout" style="height: 800px; width:100%;">



    <div data-options="region:'west',title:'Filtros',split:true,collapsible:false" style="width:200px; height:auto;">



            <form id="filtrosReporteTV" method="post" >



                <table cellpadding="6"  width="100%" id="filtros_tv">
					<tbody>
                    <tr>

                        <td>
                        <label for="fechaInicio">Fecha inicio</label><br>
                        <input id="fechaInicio" class="easyui-datebox"  data-options="formatter:myformatter,parser:myparser" required></td>
						</tr>
                        <tr>

                        <td>
                        <label for="fechaFin">Fecha fin</label> <br>
                        <input id="fechaFin" class="easyui-datebox" data-options="formatter:myformatter,parser:myparser" required>
                        </td>

						</tr>
                        <tr>
                        <td>
                        <label for="MarcaVis">Marca</label><br>
                        <input id="MarcaVis" name="MarcaVis"></td>

					</tr>
                    <tr> 

                        <td>
                        <label for="idTipoTie">Tipo Tienda</label><br>
                        <input id="idTipoTie" class="easyui-combobox" name="idTipoTie" ></td>
                        
                    </tr>
                    <tr>
                        <td>
                        <label for="Formatos">Formato</label><br>
                        <input id="Formatos" class="easyui-combobox" name="Formatos" ></td>
                       </tr>
                       <tr> 

                        <td>
                        <label for="nombreEstado">Estado</label><br>
                        <input id="nombreEstado" class="easyui-combobox" name="nombreEstado" ></td>
                        
                    </tr>
                    
   
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteTiendV()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRTiendV()">Limpiar</a>

            </div>

            <script  type="text/javascript">


				$('#Formatos').combobox({

                    valueField:'idFormato',

                    textField:'cadena',

                    url:'../php/get_formato.php',

                    required:false

                });
				

                $('#nombreEstado').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_estado.php',

                    required:false

                });
				
				 $('#MarcaVis').combobox({

                    valueField:'idMarca',

                    textField:'nombre',

                    url:'../php/getMarca.php',

                    required:true,
					
					multiple:true


                });
				
				$('#idTipoTie').combobox({

                    valueField:'idTipo',

                    textField:'nombre',

                    url:'../php/getTiendaTipo.php',

                    required:false


                });
			

            </script>

        </div>


    <!-- end north -->



    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true" style="width: auto;height: 100%;" toolbar="#toolsTV" >
	<div id="toolsTV">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true" onClick="exportarTiendV()">Exportar</a>
        </div>

 		<div id="ventanaTiendV"></div>

    </div>

    <!-- end center -->


    <div id="dlgTiendV" style="width: 400px;height: 200px;"></div>
</div>



