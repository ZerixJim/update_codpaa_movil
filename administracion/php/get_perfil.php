<?
include_once('../connexion/DataBase.php');

$manager = DataBase::getInstance();


$sql = "SELECT id_perfil,perfil FROM usuarios_perfiles ORDER BY id_perfil ASC";

$rs = $manager->ejecutarConsulta($sql);
$result = array();
while ($row = mysqli_fetch_object($rs)) {
    array_push($result, $row);
}

echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


