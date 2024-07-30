<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 30/07/14

 * Time: 16:57

 */

include_once('../connexion/bdManager.php');
include_once('../php/seguridad.php');
?>

 <script type="application/javascript" language="javascript">
		$(function(){
			
			// Clona la fila oculta que tiene los campos base, y la agrega al final de la tabla
			$("#agregar").on('click', function(){
				
				$("#filtros tbody tr:last").clone().removeClass('fila-base').appendTo("#filtros tbody");
				
			});
		 
			// Evento que selecciona la fila y la elimina 
			$(document).on("click","#eliminar",function(){
				var parent = $(this).parents().get(0);
				
				$(parent).remove();
				
				
				
			});
		});

		</script>

<div id="fotos" class="easyui-layout" style="height: 800px; width:100%;">



    <div data-options="region:'north',title:'Busqueda',split:true,collapsible:false" style="width:100%; height:400px;">



            <form id="filtrosReporte" method="post" >



                <table cellpadding="5" id="filtros">
					<tbody>
                    <tr>

                        <td><label for="Mes">Mes</label></td>

                        <td><input id="Mes"  class="easyui-combobox" name="Mes"></td>

                    

                        <td><label for="Anio">Año</label></td>

                        <td><input id="Anio"  class="easyui-combobox" name="Anio"></td>

                    

                        <td><label for="nombreEstado">Estado</label></td>

                        <td><input id="nombreEstado" class="easyui-combobox" name="nombreEstado" ></td>
						</tr>
                        <tr>
                        <td><label for="nombreTienda">Tienda</label></td>

                        <td><input id="nombreTienda" class="easyui-combobox" name="nombreTienda" ></td>

                    </tr>
                    <tr>
                    <td colspan="2">
                    <input type="button" id="agregar" value="Agregar Regla" class="easyui-linkbutton"/>
                    </td>
                    </tr>
					<tr>
                        <th>
                         Marca
                        </th>
                        <th>
                         Fotos
                        </th>
                        <th>
                         Frentes
                        </th>
                        <th>
                         Inventarios
                        </th>
                        <th>
                         Inteligencia M
                        </th>
                        <th>
                        </th>
                    </tr>
					<tr class="fila-base">
                            <td>
                            <? 
							$manager = new bdManager();
							$sql = "SELECT ma.idMarca,ma.nombre FROM Marca as ma inner join usuariosMarcaAsignada as ua on ma.idMarca=ua.idMarca 
	where ua.idUsuario=".$idUsuario." order by idMarca asc ";?>
                            <select name="nombreMarca[]" id="nombreMarca" required>
                            <option value="0">----------</option>
							<? 
						   $rs = $manager->ejecutarConsulta($sql);
							
							while($marcas_a=mysqli_fetch_array($rs))
							{ ?>
							<option value="<? echo $marcas_a['idMarca'];?>"><? echo $marcas_a['nombre'];?></option>	
							<?	}
							?>
                            </select>
                            
                            </td>
                            <td>
                            <select name="fotos_c[]" id="fotos_c" >
                            <option value="0">---</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            </select>
                            X
                            <select name="fotos_t[]" id="fotos_t" >
                            <option value="0">---</option>
                            <option value="SEM">SEM</option>
                            <option value="QUI">QUI</option>
                            </select>
                            X
                            <select name="fotos_l[]" id="fotos_l" >
                            <option value="0">---</option>
                            <option value="SUC">SUC</option>
                            </select>	
                            </td>
                       
                            <td>
                            <select name="frentes_c[]" id="frentes_c" >
                            <option value="0">---</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            </select>
                            X
                            <select name="frentes_t[]" id="frentes_t" >
                            <option value="0">---</option>
                            <option value="SEM">SEM</option>
                            <option value="QUI">QUI</option>
                            </select>
                            X
                            <select name="frentes_l[]" id="frentes_l" >
                            <option value="0">---</option>
                            <option value="SUC">SUC</option>
                            </select>
                            </td>
                            <td>
                            <select name="invent_c[]" id="invent_c" >
                            <option value="0">---</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            </select>
                            X
                            <select name="invent_t[]" id="invent_t" >
                            <option value="0">---</option>
                            <option value="SEM">SEM</option>
                            <option value="QUI">QUI</option>
                            </select>
                            X
                            <select name="invent_l[]" id="invent_l">
                            <option value="0">---</option>
                            <option value="SUC">SUC</option>
                            </select>
                            </td>
                        
                            <td>
                            <select name="im_c[]" id="im_c">
                            <option value="0">---</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            </select>
                            X
                            <select name="im_t[]" id="im_t" >
                            <option value="0">---</option>
                            <option value="SEM">SEM</option>
                            <option value="QUI">QUI</option>
                            </select>
                            X
                            <select name="im_l[]" id="im_l" >
                            <option value="0">---</option>
                            <option value="FOR">FOR</option>
                            </select>
                            </td>
                            <td id="eliminar" class="easyui-linkbutton">Eliminar</td> 
                     </tr>
                    
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteBono()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanForm()">Limpiar</a>

            </div>



            <script  type="text/javascript">


				
                $('#Mes').combobox({

                    valueField:'Mes',

                    textField:'Mes',

                    url:'../php/getMes.php',

                    required:true

                });
				
				$('#Anio').combobox({

                    valueField:'Anio',

                    textField:'Anio',

                    url:'../php/getAnio.php',

                    required:true

                });
				$('#nombreTienda').combobox({

                    valueField:'IdTienda',

                    textField:'nombre',

                    url:'../php/getTiendasEstado.php',

                    required:false

                });
				

                $('#nombreEstado').combobox({

                    valueField:'id',

                    textField:'nombre',

                    url:'../php/get_estado.php',

                    required:false,

                    onSelect:function(record){

                        var url = '../php/getTiendasEstado.php?Estado='+record.id;

                        $('#nombreTienda').combobox('clear');



                        $('#nombreTienda').combobox('reload',url);

                    }

                });
			

            </script>







        </div>


    <!-- end north -->





    <!-- start center -->

    <div id="reportecenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false" style="width: auto;height: 100%;" toolbar="#toolsFoto">

        <div id="toolsFoto">



            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-pdf" plain="true" onClick="exportarPdf()">Exportar</a>





        </div>




    </div>

    <!-- end center -->



    <div id="ventanaFoto"></div>

    <div id="dialogoLink" style="width: 400px;height: 200px;"></div>

</div>



