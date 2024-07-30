<?php
session_start();
$idUsuario = $_SESSION['idUser'];
/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 30/07/14

 * Time: 16:57

 */



?>


<div id="asistencia" class="easyui-layout" style="height: 100%; width:100%;">



    <div data-options="region:'north',title:'Busqueda',split:true,collapsible:true"
         style="width:100%; height:140px;">



            <form id="filtrosReporte" method="post" >



                <table cellpadding="5" id="filtros">
					<tbody>
                    <tr>

                        <td>
                        <label for="SemanaAsist" class="card-face__title2">Semana</label>
                        <input id="SemanaAsist" class="easyui-combobox" name="SemanaAsist" style=" width:100px;" required>
                        </td>
						
						<td>
                        <label for="MarcaAsis" class="card-face__title2">Marca</label>
                        <input id="MarcaAsis" name="MarcaAsis"></td>

                        <td>
                        <label for="idTipoTie" class="card-face__title2">Tipo Tienda</label>
                        <input id="idTipoTie" class="easyui-combobox" name="idTipoTie" ></td>
                      
                        <td>
                        <label for="Formatos" class="card-face__title2">Formato</label>
                        <input id="Formatos" class="easyui-combobox" name="Formatos" ></td>
                     
                        <td>
                        <label for="nombreEstado" class="card-face__title2">Estado</label>
                        <input id="nombreEstado" class="easyui-combobox" name="nombreEstado" ></td>
                  

                        <td><label for="Supervisor" class="card-face__title2">Supervisor</label>
                        <input id="Supervisor"  class="easyui-combobox" name="Supervisor"></td>


                    </tr>
                    
				</tbody>
                </table>

            </form>



            <div style="text-align: center;">

                <a href="javascript:void(0)" class="easyui-linkbutton" onclick="crearReporteAsist()">Aceptar</a>

                <a href="javascript:void(0)" class="easyui-linkbutton" onClick="cleanRAsist()">Limpiar</a>

            </div>



            <script  type="text/javascript">



                function crearReporteAsist() {


                    let idSupervisor = $('#Supervisor').combobox('getValue');

                    let idFormato = $('#Formatos').combobox('getValue');

                    let idEstado = $('#nombreEstado').combobox('getValues');

                    let n_estado = idEstado.length;

                    let idSem = $('#SemanaAsist').combobox('getValue');

                    let idMarca = $('#MarcaAsis').combobox('getValues');

                    let n_marca = idMarca.length;

                    let idTipoTie = $('#idTipoTie').combobox('getValue');

                    if (n_estado > 0 && idSem !== "" && n_marca > 0) {
                        $.ajax({
                            beforeSend: function () {
                                $("#repor_dcenter").html('');
                                $("<img src='../imagenes/loading.gif'  style='position:relative;top:0;left:50px;z-index:2000' id='loading-imagen' />").appendTo("#repor_dcenter");
                            },
                            complete: function () {
                                $("#loading-imagen").remove();
                            },
                            type: 'POST',
                            url: '../php/reportes/queryReporteAsist.php',
                            data: {
                                Supervisor: idSupervisor,
                                idFormato: idFormato,
                                idMarca: idMarca,
                                idEstado: idEstado,
                                idSem: idSem,
                                idTipoTie: idTipoTie
                            },

                            error: function (jqXHR, textStatus, error) {

                                $.messager.alert('Reportes', "error: " + jqXHR.responseText, 'error');
                            }
                        }).done(function (data) {
                            $("#repor_dcenter").html(data);
                        });
                    }
                    else {

                        $.messager.alert('Reportes', "Faltan datos para generar el reporte", 'error');
                    }

                }
                function cleanRAsist() {

                    $("#repor_dcenter").html('');

                }

				
				
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

                    required:true,
					
					multiple:true

                });
				
				 $('#MarcaAsis').combobox({

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

				
                $('#Supervisor').combobox({

                    valueField:'idSupervisores',

                    textField:'nombreSupervisor',

                    url:'../php/get_supervisor.php',

                    required:false

                });
				
				$('#SemanaAsist').combobox({
					
					valueField:'label',

                    textField:'text',
					
					url:'../php/get_semanaLab2.php'
					
				});
				
			

            </script>







        </div>


    <!-- end north -->





    <!-- start center -->

    <div id="repor_dcenter" data-options="region:'center',title:'Resultado',split:true,collapsible:false" style="width: auto;height: 100%;" >

       



    </div>

    <!-- end center -->



    <div id="detFrentes"></div>
    <div id="detInvent"></div>
    <div id="detIm"></div>

    <div id="dialogoLink" style="width: 400px;height: 200px;"></div>

</div>



