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


<div id="mayoreo_compRes" class="easyui-layout" style="height: 100%; width:100%;">



    <div data-options="region:'west',title:'Filtros',split:true,collapsible:true" style="width:200px; height:auto;">



            <form id="filtrosReporteMC" method="post" >



                <table cellpadding="6"  width="100%" id="filtros_mc">
					<tbody>
                        <tr>
                        <td>
                        <label for="MarcaMayCR">Marca</label><br>
                        <input id="MarcaMayCR" name="MarcaMayCR" ></td>

					</tr>
                     <tr>
                        <td>
                       
                       <label for="MesLabR" >Mes</label><br>
	                   <input id="MesLabR" class="easyui-combobox" name="MesLab" style=" width:100px;" data-options="valueField:'mesMay',textField:'mesMay',required:true,multiple:false">
                        </td>

					</tr>
                        
                    <tr style="display:;" id="div_est"> 
                        <td>
                        <label for="EstadoMCR">Estado</label><br>
                        <input id="EstadoMCR" class="easyui-combobox" name="EstadoMCR" ></td>
                        
                    </tr>
                    <tr style="display:none;" id="div_reg">
                        <td><label for="RegionMxR">Region</label><br>
                        <input id="RegionMxR" class="easyui-combobox" name="RegionMxR" style=" width:150px;" data-options="panelHeight:'auto'"></td>
                    </tr>
                    
                    <tr>
                        <td>
                       
                       <label for="GrupoMR" >Grupo</label><br>
	                   <input id="GrupoMR" class="easyui-combobox" name="GrupoMR">
                        </td>

					</tr>
                   
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteMayCompRes()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanMayCompRes()">Limpiar</a>

            </div>

            <script  type="text/javascript">
			function mes_pic()
			{
					var idMarca = new Array();
					idMarca=$('#MarcaMayCR').combobox('getValues');
					
					var url = '../php/get_mesLab.php?idMarca='+idMarca;

                     $('#MesLabR').combobox('clear');

                     $('#MesLabR').combobox('reload',url);
			}
			
			
				 $('#EstadoMCR').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_estado.php',

                    required:false

                });
				
				 $('#MarcaMayCR').combobox({

                    valueField:'idMarca',

                    textField:'nombre',

                    url:'../php/getMarca.php',

                    required:true,
					
					multiple:true,
					
					 onSelect:function(record){
						 
						 mes_pic();
					if(record.idMarca=='1' || record.idMarca=='77' || record.idMarca=='78' || record.idMarca=='79' || record.idMarca=='80' || record.idMarca=='81')
					{
						 document.getElementById('div_est').style.display='none';
						 document.getElementById('div_reg').style.display='';
						 
						 
						}
					else
					{
						 document.getElementById('div_est').style.display='';
						 document.getElementById('div_reg').style.display='none';
						}
						 }


                });
				
				$('#RegionMxR').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_region.php',

                    required:false

                   

                });
				
				$('#GrupoMR').combobox({

                    valueField:'grupo',

                    textField:'grupo',

                    url:'../php/get_grupomc.php',

                    required:false

                });
function visitasGraph(tot_vis)
 {
	var recorre;
		var datos= [
          ['Task', 'Hours per Day'],
          ['Work',     11],
          ['Eat',      2],
          ['Commute',  2],
          ['Watch TV', 2],
          ['Sleep',    7]
        ];
		
		if(tot_vis)
		{
			console.log(tot_vis);
						
			var datos2= new Array();
			
			data = new google.visualization.DataTable();
			
			data.addColumn('string', 'Tipo');
     		 data.addColumn('number', 'Total');
			while(tot_vis.length>0)
			{
				recorre=tot_vis.pop();
				data.addRow([recorre.Tipo,Number(recorre.Total)]);	
				}
			
			console.log(datos);
			}
		else
		{
			data=google.visualization.arrayToDataTable(datos);	
			}
		
		  var options = {
          title: 'Asistencia a Tiendas',
          is3D: true,
        };

        var chart = new google.visualization.PieChart(document.getElementById('asistenciaGra'));
        chart.draw(data, options); 
	 }
	
            </script>

        </div>


    <!-- end north -->



    <!-- start center -->

    <div id="repor_mcrcenter" data-options="region:'center',title:'Datos',split:true" style="width: auto;height: 100%;" >
	

 		<div id="ventanaMCRes"></div>

    </div>

    <!-- end center -->
	    
</div>



