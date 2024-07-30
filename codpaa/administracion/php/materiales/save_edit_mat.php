<?

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$material = $_REQUEST['nombre_matE'];
$unidadM = $_REQUEST['unidadME'];
$solicitudM = $_REQUEST['solicitud_maxE'];

$idMaterial=$_REQUEST['id_matE'];


$sql = "update materiales set material='$material',unidad='$unidadM',solicitud_max='$solicitudM'
where id_material='".$idMaterial."'";

$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result && $error_img==0){
	echo '1';
} else {
	echo '0';
}
?>