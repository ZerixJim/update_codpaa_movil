<?

include_once('../../connexion/bdManager.php');

$idMensaje=$_REQUEST['idMensj'];


$sql = "update mensajes set estatus='0' where id_mensaje='$idMensaje'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>