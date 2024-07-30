<?php
session_start();
include_once('../connexion/DataBase.php');
$manager = DataBase::getInstance();

$query = $_REQUEST["query"];

$query_us = "SELECT * FROM Clientes WHERE razonsocial LIKE '%" . $query . "%' OR rfc LIKE '%" . $query . "%' ORDER BY razonsocial";

$result_us = $manager->ejecutarConsulta($query_us);

$rows = array();


while ($arreglo2 = mysqli_fetch_array($result_us)) {

    $nombre = $arreglo2["idCliente"] . " - " . $arreglo2["razonsocial"];

    array_push($rows, array('value'=> $nombre, 'data'=> $arreglo2['idCliente']));

}


echo json_encode(array('query'=> $query, 'suggestions'=> $rows), JSON_NUMERIC_CHECK | JSON_PARTIAL_OUTPUT_ON_ERROR);


