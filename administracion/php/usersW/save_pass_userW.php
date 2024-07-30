<?
session_start();
include_once('../../connexion/bdManager.php');
$base = new bdManager();

$password = $_REQUEST['password'];
$idUsuario=$_SESSION['idUser'];


$sql = "update usuarios  set pass=md5('$password') where idUsuario='$idUsuario'";

$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>