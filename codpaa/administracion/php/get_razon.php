<?php



ob_start();
session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../connexion/DataBase.php');


    $manager = DataBase::getInstance();

    $sql = "SELECT rs.razon_social AS nombre,rs.id FROM razon_social rs  ORDER BY rs.razon_social";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();



    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);

    }


    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}


