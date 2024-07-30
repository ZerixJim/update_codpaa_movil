<?

include_once('../connexion/bdManager.php');

$supervisor = $_REQUEST['nombreSupervisor'];
$nombre = $_REQUEST['nombre'];
$usuario = $_REQUEST['usuario'];
$pass = $_REQUEST['password'];


$sql = "insert into Promotores(Supervisor,nombre,usuario,password) values($supervisor,'$nombre','$usuario','$pass')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('msg'=>'Ocurrio algun problema'));
}
?>