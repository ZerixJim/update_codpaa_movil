<?php
session_start();



include_once('../connexion/DataBase.php');
$manager = DataBase::getInstance();

$query = $_REQUEST["query"];

$query_prom = "SELECT * FROM Promotores 
	WHERE (idCelular LIKE '%" . $query . "%' OR nombre LIKE '%" . $query . "%' OR usuario LIKE '%" . $query . "%') AND status='a'
 	ORDER BY nombre";

$result_prom = $manager->ejecutarConsulta($query_prom);

$rows = array();


while ($arreglo2 = mysqli_fetch_array($result_prom)) {



    $nombre = $arreglo2["idCelular"] . " - " . $arreglo2["nombre"];

    array_push($rows, array('value'=>$nombre, 'data'=>array('idCelular'=> $arreglo2['idCelular'])) );



}


echo json_encode(array('query'=>$query, 'suggestions'=> $rows), JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);



