<?

include_once('../../connexion/bdManager.php');

$idCliente=$_REQUEST['idCliente'];


$sql = "update Clientes set estatus='0' where idCliente='$idCliente'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>