<?php
session_start();
include_once('../connexion/DataBase.php');
$manager = DataBase::getInstance();

$query = $_REQUEST["query"];

$query_us = "SELECT * FROM usuarios WHERE (idUsuario LIKE '%" . $query . "%' 
    OR nombre LIKE '%" . $query . "%' OR user LIKE '%" . $query . "%') 
    AND estatus=1 
    ORDER BY nombre";

$result_us = $manager->ejecutarConsulta($query_us);

$rows = array();


while ($arreglo2 = mysqli_fetch_array($result_us)) {


    array_push($rows, array('value' => $arreglo2["nombre"], 'data' => array('idUsuario' => $arreglo2["idUsuario"])));

}

echo json_encode(array('query' => $query, 'suggestions' => $rows));


