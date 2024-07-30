<?

include_once('../../connexion/bdManager.php');

$perfil = $_REQUEST['idPerfil'];
$menu = $_REQUEST['idMenu'];


$sql = "delete from permisos_perfil where id_perfil='$perfil' and id_menu='$menu'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>