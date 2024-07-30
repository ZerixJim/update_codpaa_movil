<?

include_once('../../connexion/bdManager.php');

$cliente = $_REQUEST['idCliente'];
$marca = $_REQUEST['idMarca'];


$sql = "delete from ClientesMarcas where idCliente='$cliente' and idMarca='$marca'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>