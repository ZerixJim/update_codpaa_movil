<? 
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
</head>

<body>
<? 
$id_msj=$_REQUEST['idMensaje'];

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$manager = new bdManager();

$query_msj="select ms.*,M.nombre as Marca from mensajes ms 
		inner join Marca M on 
		(M.idMarca=ms.id_marca) 
		where id_mensaje='".$id_msj."'";

$resul_msj=$manager->ejecutarConsulta($query_msj);

$datos_msj=mysqli_fetch_array($resul_msj);

?>
<div id="msjs_data" class="easyui-accordion" style="width:100%;height:100%;">
<div align="center" title="Mensaje" data-options="iconCls:'icon-edit'">
<form id="fm_viewMsj" method="post" novalidate>

                     <table width="100%">
                        <tr>

                            <div class="fitem">

                                <td height="40px"><label><h3 class="colorVang">Marca</h3></label>
                                <input type="hidden" id="idMsj" name="idMsj" value="<? echo $id_msj;?>"/>
								<strong><? echo $datos_msj['Marca'];?></strong>
                                </td>

                            </div>
						<div class="fitem">

                                <td height="40px"><label><h3 class="colorVang">Titulo</h3></label>
                                <input name="titulo_msjE" id="titulo_msjE" class="easyui-textbox" required  data-options="value:'<? echo trim($datos_msj['titulo']);?>'" readonly>
                                </td>

                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td colspan="2" height="40px"><label><h3 class="colorVang"> Asunto</h3></label>
                                <input name="asunto_msjE" id="asunto_msjE" class="easyui-textbox"  data-options="value:'<? echo $datos_msj['asunto'];?>'" readonly>
                                </td>

                            </div>
					</tr>
                    <tr>
                            <div class="fitem">

                                <td colspan="2" height="40px"><label><h3 class="colorVang">Contenido</h3></label>
                                <input name="contenido_msjE" id="contenido_msjE" class="easyui-textbox"  style=" width:450px; height:150px;" data-options="value:'<? echo $datos_msj['mensaje'];?>',multiline:true" readonly>
                                </td>

                            </div>

                        </tr>
                       
                       </table>
                

         </form>
     </div> <!-- Termina primer div del acordeon -->
      <div align="center" title="Acuses" data-options="iconCls:'icon-ok'">
        	
        	<div id="tool_acuses" align="center" >
            
                 <label for="TipoAcuse" class="card-face__title2">Elegir:</label><br>
                 <select id="TipoAcuse" class="easyui-combobox" name="TipoAcuse" style="width:100px;">
                    <option value=""></option>
                    <option value="1">Visto</option>
                    <option value="2">No Visto</option>
                 </select>
            </div>
            <div id="msjs_acuses" style=" width:70%">
            	 
            </div>
            
        </div><!-- Termina el segundo div del acordeon-->
   </div>   
   <script type="text/javascript" language="javascript">
   function ver_acuses()
		{
			var tipoAcu=$('#TipoAcuse').combobox('getValue');
			var idMsj=$('#idMsj').val();
			
			 $.ajax({
			beforeSend:function() { 
			 $("#msjs_acuses").html('');
				 $("<img src='../imagenes/loading.gif'  style='position:relative;top:0px;left:50px;z-index:2000' id='loading-imagen' />").appendTo("#mensajes");
			 },
			 complete:function() {
				   $("#loading-imagen").remove();
			 },
			  type: 'POST',	 
			 url: '../php/mensajes/queryAcuses.php',
			 data: { tipoAcu: tipoAcu,idMsj: idMsj},
			 
			 error: function(jqXHR, textStatus, error) {
		
					$.messager.alert('Mensajes',"error: " + jqXHR.responseText,'error');
				  }
			 }).done(function(data)
			 {
				 $("#msjs_acuses").html(data);
				 
				 
			 });
			
		}
   $(document).ready(function(e) {
            
						
			$('#TipoAcuse').combobox({
					
					onSelect:function(record){

					 ver_acuses();
                    }
					
				});
				
				 var $on = 'section';
				$($on).css({
				  'background':'none',
				  'border':'none',
				  'box-shadow':'none'
				});
			
        });
   </script>      
</body>
</html>

<? 
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>