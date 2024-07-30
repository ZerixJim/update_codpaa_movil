<?

include_once('../../connexion/bdManager.php');

$idMaterial=$_REQUEST['idMat'];


$sql = "update materiales set estatus='0' where id_material='$idMaterial'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>