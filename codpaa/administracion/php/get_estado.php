<?php

/**
 * Created by PhpStorm.
 * User: Gustavo
 * Date: 14/08/14
 * Time: 13:03
 */


ob_start();

session_start();


include_once('../connexion/DataBase.php');


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


    $manager = DataBase::getInstance();

    $sql = "SELECT UPPER(nombre) AS nombre,id FROM estados  ORDER BY nombre ASC ";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    /*$ini = array('nombre' => 'TODOS', 'id' => 'all');
    array_push($result, $ini);*/

    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);

    }


    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}





