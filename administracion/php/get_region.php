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

    $sql = "SELECT UPPER(descripcion) AS nombre,id_nielsen AS id FROM estados_nielsen AS en  ORDER BY en.descripcion ASC ";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();


    while ($row = mysqli_fetch_object($rs)) {


        array_push($result, $row);

    }


    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


} else {

    echo 'no has iniciado sesion';


}

