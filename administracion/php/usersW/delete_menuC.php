<?

include_once('../../connexion/bdManager.php');

$usuario = $_REQUEST['idUsuario'];
$menu = $_REQUEST['idMenu'];


$sql = "delete from permisos_clientes where id_usuario='$usuario' and id_menu='$menu'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>