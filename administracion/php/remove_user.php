<?
include_once('../connexion/bdManager.php');
$id = intval($_REQUEST['id']);



$sql = "delete from Promotores where idCelular=$id";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);
if ($result){
	echo json_encode(array('success'=>true));
} else {
	echo json_encode(array('msg'=>'Ocurrio algun error'));
}

?>