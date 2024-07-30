<?php
include_once('../connexion/DataBase.php');

$manager = DataBase::getInstance();


$sql = "SELECT idCliente,razonsocial

    FROM Clientes ORDER BY razonsocial ASC";



$rs = $manager->ejecutarConsulta($sql);
$result = array();
while ($row = mysqli_fetch_object($rs)) {

    $row->razonsocial = utf8_encode($row->razonsocial);
    array_push($result, $row);
}

echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);
