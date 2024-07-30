<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 28/02/2017
 * Time: 01:00 PM
 */
ob_start();
session_start();


if (isset($_SESSION['idUser']) && isset($_POST['idProducto'])) {

    include_once('../../connexion/bdManager.php');

    $formatos = $_POST['formatos'];
    $producto = $_POST['idProducto'];

    $db = new bdManager();

    $inserted = array();

    foreach($formatos as $formato){

        $sql = "insert into producto_formato(id_producto, id_formato) VALUES ($producto, $formato)";


        if($db->ejecutarConsulta($sql)){
            array_push($inserted, array('idFormato'=>$formato));
        }

    }

    if(count($inserted) > 0){

        echo json_encode(array('success'=> true, 'idProducto'=> $producto , 'inserted' => $inserted ), JSON_PRETTY_PRINT | JSON_NUMERIC_CHECK);

    }



}

