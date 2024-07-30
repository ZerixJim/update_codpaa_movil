<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 25/04/2017
 * Time: 11:01 AM
 */
ob_start();
session_start();

include_once('../../connexion/DataBase.php');


if(isset($_SESSION['idUser'])){

    $data = DataBase::getInstance();

    $sql = "select sT.idTipo as id, sT.descripcion as text from solicitud_codpaa_tipo as sT";

    $response = $data->ejecutarConsulta($sql);

    $json = array();

    while($row= mysqli_fetch_object($response)){

        array_push($json, $row);

    }


    echo json_encode($json, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);


}else{
    echo "acceso denegado";
}