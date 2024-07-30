<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 14/08/14
 * Time: 13:03
 */


session_start();


include_once('../connexion/DataBase.php');


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


    $manager = DataBase::getInstance();

    $sql = "SELECT nombre AS nombre, idRegion AS id FROM regiones_summa rs ORDER BY rs.nombre ASC ";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();


    while ($row = mysqli_fetch_object($rs)) {


        array_push($result, $row);

    }


    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


} else {

    echo 'no has iniciado sesion';


}

