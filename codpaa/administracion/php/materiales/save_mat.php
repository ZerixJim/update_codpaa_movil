<?

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$material = $_REQUEST['material'];
$unidadM= $_REQUEST['unidadM'];
$solicitudM = $_REQUEST['solicitudM'];

$sql = "insert into materiales (material,unidad,solicitud_max,estatus) 
values('$material','$unidadM','$solicitudM','1')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>