<?
include_once('../connexion/DataBase.php');

$manager = DataBase::getInstance();
$rs = $manager->ejecutarConsulta('SELECT idSupervisores,concat(nombreSupervisor," ",apellidoSupervisor) AS nombreSupervisor 
	FROM Supervisores ORDER BY nombreSupervisor ASC');
$result = array();
while ($row = mysqli_fetch_object($rs)) {

    array_push($result, $row);
}

echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);

