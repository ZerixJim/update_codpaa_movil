<?
session_start();
include_once('../../connexion/DataBase.php');
$base = DataBase::getInstance();

$email = $_REQUEST['email'];
$idUsuario=$_SESSION['idUser'];


$sql = "update usuarios  set email='$email' where idUsuario='$idUsuario'";

$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
