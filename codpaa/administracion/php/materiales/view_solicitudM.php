<? 
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
?>
<html>
<head>
  
</head>

<body >
<? 
$idSolicitudM=$_REQUEST['idSolicitudM'];

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$manager = new bdManager();

$query_sol="Select  ms.*,m.material,p.nombre as promotor,concat('(',mt.idTienda,')-',mt.sucursal) as tienda
		FROM materiales_solicitud ms 
		inner join Promotores p on (p.idCelular=ms.id_promotor)
		inner join materiales m on (m.id_material=ms.id_material) 
		inner join maestroTiendas mt on (mt.idTienda=ms.idTienda)
		where id_mat_solicitud='".$idSolicitudM."'";

$resul_sol=$manager->ejecutarConsulta($query_sol);

$datos_sol=mysqli_fetch_array($resul_sol);


?>
<div align="center" >
<form id="fm_editSolM" method="post"  enctype="multipart/form-data">

                    <table width="100%" border="0" cellspacing=0 cellpadding=2 style="text-align: justify;">
                        <tr >

                            <div class="fitem">

                                <td align="right" height="35"><label><strong>Promotor:</strong></label></td>

                                <td align="center">
                                <input type="hidden" name="id_solicitudME" id="id_solicitudME" value="<? echo $idSolicitudM;?>">
                                <input name="promotorSE" id="promotorSE" class="easyui-textbox" readonly required  data-options="value:'<? echo trim($datos_sol['promotor']);?>'"></td>

                            </div>
						</tr>
                          <tr>
                            <div class="fitem">

                                <td align="right" height="35"><label><strong>Tienda:</strong></label></td>

                                <td align="center"><input name="tiendaSE" id="tiendaSE" class="easyui-textbox"  readonly data-options="value:'<? echo $datos_sol['tienda'];?>'"></td>

                            </div>

                        </tr>
                        <tr>
                            <div class="fitem">

                                <td align="right" height="35"><label><strong>Material:</strong></label></td>

                                <td align="center"><input name="materialSE" id="materialSE" class="easyui-textbox"  readonly data-options="value:'<? echo $datos_sol['material'];?>'"></td>

                            </div>

                        </tr>
                        <tr>

                             <div class="fitem">

                                <td align="right" height="35"><label><strong>Cantidad:</strong></label></td>

                                <td align="center"><input name="cantidadSE" id="cantidadSE" class="easyui-textbox" readonly data-options="value:'<? echo $datos_sol['cantidad'];?>'" ></td>

                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td align="right"><label>Fecha:</label></td>

                                
                                <td align="center"><input name="fechaSE" id="fechaSE" class="easyui-textbox" readonly data-options="value:'<? echo $datos_sol['fecha'];?>'" required></td>

                            </div>
                        </tr>
                        <tr>

                             <div class="fitem">

                                <td align="right" height="35"><label><strong>Estatus:</strong></label></td>
                                
                                <td align="center">
                                <select class="easyui-combobox" panelHeight="auto" style="width:100px" id="estatusSE">
                                    <option value="0" <? if($datos_sol['estatus']=='0'){ echo "selected";}?>>Cancelado</option>
                                    <option value="1" <? if($datos_sol['estatus']=='1'){ echo "selected";}?>>Activo</option>
                                    <option value="2" <? if($datos_sol['estatus']=='2'){ echo "selected";}?>>Enviado</option>
                                    <option value="3" <? if($datos_sol['estatus']=='3'){ echo "selected";}?>>Recibido</option>
                                </select>
                                </td>

                            </div>
						</tr>
                        <? if($datos_sol['estatus']>1)
						{?>
                        <tr>

                             <div class="fitem">

                                <td align="right" height="35"><label><strong>Fecha Envio:</strong></label></td>

                                <td align="center"><input name="fecha_envioSE" id="fecha_envioSE" class="easyui-textbox" data-options="value:'<? echo $datos_sol['fecha_envio'];?>'" readonly></td>

                            </div>
                        </tr>
                        <tr>

                             <div class="fitem">

                                <td align="right" height="35"><label><strong>Guia:</strong></label></td>

                                <td align="center"><input name="guiaSE" id="guiaSE" class="easyui-textbox"  data-options="value:'<? echo $datos_sol['guia'];?>'" readonly></td>

                            </div>
                        </tr>
                        <? }?>
                    </table>

                </form>
     </div> 

</body>
</html>

<? 
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>