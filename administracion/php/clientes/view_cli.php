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
$id_cliente=$_REQUEST['idCliente'];

include_once('../../connexion/bdManager.php');
$manager = new bdManager();

$query_cli="Select * from Clientes where idCliente='".$id_cliente."'";

$resul_cli=$manager->ejecutarConsulta($query_cli);

$datos_cli=mysqli_fetch_array($resul_cli);


?>
<div align="left" style="margin-top:30px; margin-left:10px;" >
	<form id="fm_view" method="post" novalidate>

    <table>
      <tr>

            <div class="fitem">

                <td><label><strong>CLIENTE:</strong></label></td>

                <td><? echo $datos_cli['razonsocial'];?></td>

            </div>
        </tr>
        <tr>

            <div class="fitem">

                <td><label><strong>NOMBRE CONTACTO:</strong></label></td>

                <td><? echo $datos_cli['nombre_contacto'];?></td>

            </div>
        </tr>
        <tr>
            <div class="fitem">

                <td><label><strong>RFC:</strong></label></td>

                <td><? echo $datos_cli['rfc'];?></td>

            </div>

        </tr>
        <tr>

            <div class="fitem">

                <td><label><strong>DIRECCION:</strong></label></td>

                <td><? echo $datos_cli['calle']." ".$datos_cli['no_ext']." ".$datos_cli['no_int'];?></td>

            </div>
        </tr>
        <tr>
            <div class="fitem">

                <td><label><strong>COLONIA:</strong></label></td>

                <td><? echo $datos_cli['colonia'];?></td>

            </div>

        </tr>
        <tr>

            <div class="fitem">

                <td><label><strong>CP:</strong></label></td>

                <td><? echo $datos_cli['cp'];?></td>

            </div>
        </tr>
        <tr>
            <div class="fitem">

                <td><label><strong>ESTADO:</strong></label></td>

                <td><? 
				$query_esta="select * from estados where id='".$datos_cli['idEstado']."'";
				$rs_esta=$manager->ejecutarConsulta($query_esta);
				$datos_esta=mysqli_fetch_array($rs_esta);
				
				echo $datos_esta['nombre'];?></td>

            </div>

        </tr>
        <tr>

            <div class="fitem">

                <td><label><strong>MUNICIPIO:</strong></label></td>

                <td><? 
				$query_mun="select * from municipios where id='".$datos_cli['idMunicipio']."'";
				$rs_mun=$manager->ejecutarConsulta($query_mun);
				$datos_mun=mysqli_fetch_array($rs_mun);
				
				
				echo $datos_mun['nombre'];?></td>

            </div>

        </tr>
         <tr>

            <div class="fitem">

                <td><label><strong>TELEFONO:</strong></label></td>

                <td><? echo $datos_cli['telefono'];?></td>

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