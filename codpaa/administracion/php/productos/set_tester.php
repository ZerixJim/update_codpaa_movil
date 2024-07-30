<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 09/02/2017
 * Time: 11:51 AM
 */

ob_start();

session_start();

include_once('../../connexion/bdManager.php');

$productos = null;

if (isset($_REQUEST['productos'])){


    $productos = $_POST['productos'];

    $db = new bdManager();



    foreach($productos as $producto){

        var_dump($producto['idProducto']);

        if($producto['ck'] == 'Probador' ){
            $sql = "update Producto set tester=0 where idProducto = ". $producto['idProducto'];
        }else{
            $sql = "update Producto set tester=1 where idProducto = ". $producto['idProducto'];
        }


        $db->ejecutarConsulta($sql);
    }






}else{
    echo 'no hay solicitud';
}