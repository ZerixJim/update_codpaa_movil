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


<div id="mayoreo_comp_cob" class="easyui-layout" style="height: 100%; width:100%;">



    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:200px; height:auto;">



            <form id="filtrosReporteMCCob" method="post" >



                <table cellpadding="6"  width="100%" id="filtros_mccob">
					<tbody>
                    <tr>

                        <td>
                       
                       <label for="MesLabCob" >Mes</label><br>
	                   <input id="MesLabCob" class="easyui-combobox" name="MesLabCob" style=" width:100px;" >
                        </td>

						</tr>
                    <tr>
                        <td>
                        <label for="FormatosMCCob">Formato</label><br>
                        <input id="FormatosMCCob" class="easyui-combobox" name="FormatosMCCob" ></td>
                     </tr>
                     <tr>
                        <td>
                       
                       <label for="GrupoMCob" >Grupo</label><br>
	                   <input id="GrupoMCob" class="easyui-combobox" name="GrupoMCob">
                        </td>

					</tr>
                       
                     <tr> 
                        <td>
                        <label for="EstadoMCCob">Estado</label><br>
                        <input id="EstadoMCCob" class="easyui-combobox" name="EstadoMCCob" ></td>
                        
                    </tr>
                    <tr>
                        <td><label for="RegionMxCob">Region</label><br>
                        <input id="RegionMxCob" class="easyui-combobox" name="RegionMxCob" style=" width:150px;" data-options="panelHeight:'auto'"></td>
                    </tr>
                    
   
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteMayComCob()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanMayComCob()">Limpiar</a>

            </div>

            <script  type="text/javascript">
			
			

				$('#FormatosMCCob').combobox({

                    valueField:'cadena',

                    textField:'cadena',

                    url:'../php/get_cadenamc.php',

                    required:false

                });
				$('#GrupoMCob').combobox({

                    valueField:'grupo',

                    textField:'grupo',

                    url:'../php/get_grupomc.php',

                    required:false

                });

                $('#EstadoMCCob').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_estado.php',

                    required:false

                });
				$('#RegionMxCob').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_region.php',

                    required:false

                   

                });
				
				 $('#MesLabCob').combobox({

                    valueField:'mesMay',

                    textField:'mesMay',

                    url: '../php/get_mesLab.php',

                    required:true
					
                });
				
								
            </script>

        </div>


    <!-- end north -->



    <!-- start center -->

    <div id="repor_icenter" data-options="region:'center',title:'Datos',split:true" style="width: auto;height: 100%;" toolbar="#toolsMCCob" >
	<div id="toolsMCCob">

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-excel" plain="true" onClick="exportarMayCCob()">Exportar</a>

        </div>

 		<div id="ventanaMayCCob"></div>

    </div>

    <!-- end center -->


    <div id="dlgMayCCob" style="width: 400px;height: 200px;"></div>
</div>



