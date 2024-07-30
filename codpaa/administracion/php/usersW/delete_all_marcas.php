<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 15/02/2017
 * Time: 01:17 PM
 */

include_once('../../connexion/bdManager.php');

$usuario = $_REQUEST['idUsuario'];
$marcas = $_REQUEST['marcas'];

$base = new bdManager();

$delets = array();

foreach($marcas as $marca){

    $sql = "delete from usuariosMarcaAsignada where idUsuario='$usuario' and idMarca='$marca'";


    if($base->ejecutarConsulta($sql)){
        array_push($delets, array('idMarca'=> $marca));
    }

}


if (count($delets) > 0){

    echo json_encode(array('success'=> true, 'marcas'=> $delets), JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);

}