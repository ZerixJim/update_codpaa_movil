<?

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$idSolicituM = $_REQUEST['idSolicitudM'];
$estatus = $_REQUEST['estatus'];


$sql = "update materiales_solicitud set estatus='$estatus' where id_mat_solicitud='".$idSolicituM."'";

$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>