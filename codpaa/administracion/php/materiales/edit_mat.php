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
$idMaterial=$_REQUEST['idMaterial'];

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$manager = new bdManager();

$query_mat="Select * from materiales where id_material='".$idMaterial."'";

$resul_mat=$manager->ejecutarConsulta($query_mat);

$datos_mat=mysqli_fetch_array($resul_mat);


?>
<div align="center" >
<form id="fm_editMat" method="post"  enctype="multipart/form-data">

                    <table width="100%" border="1" cellspacing=0 cellpadding=2 >
                        <tr >

                            <div class="fitem">

                                <td align="right" height="35"><label><strong>Material:</strong></label></td>

                                <td align="center">
                                <input type="hidden" name="id_matE" id="id_matE" value="<? echo $idMaterial;?>">
                                <input name="nombre_matE" id="nombre_matE" class="easyui-textbox" style=" width:200px" required  data-options="value:'<? echo trim($datos_mat['material']);?>'"></td>

                            </div>
						</tr>
                        <tr>
                            <div class="fitem">

                                <td align="right" height="35"><label><strong>Unidad:</strong></label></td>

                                <td align="center"><input name="unidadME" id="unidadME" class="easyui-textbox"  data-options="value:'<? echo $datos_mat['unidad'];?>'" required></td>

                            </div>

                        </tr>
                        <tr>

                             <div class="fitem">

                                <td align="right" height="35"><label><strong>Solicitud Max:</strong></label></td>

                                <td align="center"><input name="solicitud_maxE" id="solicitud_maxE" class="easyui-textbox"  data-options="value:'<? echo $datos_mat['solicitud_max'];?>'" ></td>

                            </div>

                        </tr>
                        
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