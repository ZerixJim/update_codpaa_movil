<?php
session_start();
include_once('../connexion/DataBase.php');
$manager = DataBase::getInstance();

$query = $_REQUEST["query"];

$query_us = "SELECT * FROM usuarios WHERE (idUsuario LIKE '%" . $query . "%' OR nombre LIKE '%" . $query . "%' OR user LIKE '%" . $query . "%')
            AND id_perfil='6' ORDER BY nombre";

$result_us = $manager->ejecutarConsulta($query_us);

$rows = array();

while ($arreglo2 = mysqli_fetch_array($result_us)) {

    $nombre = $arreglo2["idUsuario"] . " - " . $arreglo2["nombre"];

    array_push($rows, array('value'=> $nombre, 'data' => array('idPromotor'=> $arreglo2["idUsuario"])));

}


echo json_encode(array('query'=>$query, 'suggestions'=> $rows), JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);



