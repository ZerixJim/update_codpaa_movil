<?

include_once('../../connexion/bdManager.php');

$usuario = $_REQUEST['idUsuario'];
$menu = $_REQUEST['idMenu'];


$sql = "insert into permisos_clientes (id_menu,id_usuario,estatus) values ('$menu','$usuario','1')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>