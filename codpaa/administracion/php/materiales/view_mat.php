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
$idMaterial=$_REQUEST['idMaterial'];

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$manager = new bdManager();

$query_mat="select * from materiales where id_material='".$idMaterial."'";

$resul_mat=$manager->ejecutarConsulta($query_mat);

$datos_mat=mysqli_fetch_array($resul_mat);

?>
<div align="center">
<form id="fm_view" method="post" novalidate>

                    <table width="100%">
                   
                     <tr>
                     
                    <td>
                    <table width="100%">
                        <tr>

                            <div class="fitem">

                                <td height="40px"><label><h3 class="colorVang">MATERIAL</h3></label>
								<? echo $datos_mat['material'];?></td>

                            </div>
						</tr>
                        <tr>
                            <div class="fitem">

                                <td height="40px"><label><h3 class="colorVang">UNIDAD</h3></label>
								<? echo $datos_mat['unidad'];?></td>

                            </div>

                        </tr>
                        <tr>

                            <div class="fitem">

                                <td height="40px"><label><h3 class="colorVang"> SOLICITUD MAX</h3></label>
								<? echo $datos_mat['solicitud_max'];?></td>

                            </div>
						</tr>
                        
                       </table>
                    </td>    
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