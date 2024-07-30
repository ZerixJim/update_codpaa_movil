<?

include_once('../../connexion/bdManager.php');

$perfil = $_REQUEST['idPerfil'];
$menu = $_REQUEST['idMenu'];


$sql = "insert into permisos_perfil (id_menu,id_perfil,estatus) values ('$menu','$perfil','1')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>