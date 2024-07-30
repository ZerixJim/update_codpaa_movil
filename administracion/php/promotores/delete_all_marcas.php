<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 13/02/2017
 * Time: 01:24 PM
 */

include_once('../../connexion/bdManager.php');

$promotor = $_REQUEST['idPromotor'];
$marcas = $_REQUEST['marcas'];

$base = new bdManager();

foreach($marcas as $marca){

    $sql = "delete from marcaAsignadaPromotor where idPromotor='$promotor' and idMarca='$marca'";

    echo $base->ejecutarConsulta($sql);
}




