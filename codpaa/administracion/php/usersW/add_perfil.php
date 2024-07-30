<?

include_once('../../connexion/bdManager.php');

$new_perfil = $_REQUEST['newPerfil'];


$sql = "insert into usuarios_perfiles (perfil,estatus) values ('$new_perfil','1')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>