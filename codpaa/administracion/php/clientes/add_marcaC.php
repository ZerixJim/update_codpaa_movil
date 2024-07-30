<?

include_once('../../connexion/bdManager.php');

$cliente = $_REQUEST['idCliente'];
$marca = $_REQUEST['idMarca'];


$sql = "insert into ClientesMarcas (idCliente,idMarca) values ('$cliente','$marca')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>