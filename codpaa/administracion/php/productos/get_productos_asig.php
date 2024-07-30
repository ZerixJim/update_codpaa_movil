<?php

ob_start();
session_start();



/**
 * Created by PhpStorm.
 * User: grim
 * Date: 24/02/2017
 * Time: 05:01 PM
 */


if(isset($_SESSION['idUser']) && isset($_POST['idProducto'])){

    include_once('../../connexion/bdManager.php');

    $db = new bdManager();


    $sql = "SELECT concat(tf.grupo,' ',tf.cadena) AS formato, 'Formato' as tipo
        FROM Producto AS p
		INNER JOIN  producto_formato AS pf ON pf.id_producto=p.idProducto
		LEFT JOIN  tiendas_formatos AS tf ON tf.idFormato=pf.id_formato


        WHERE p.idProducto = '".$_POST['idProducto']."'
        UNION

        SELECT concat(mt.grupo,' ', mt.sucursal) AS tienda, 'Tienda' as tipo
        FROM Producto AS p
        INNER JOIN  producto_tienda AS pt ON pt.id_producto=p.idProducto
        LEFT JOIN maestroTiendas AS mt ON mt.idTienda=pt.id_tienda

        WHERE p.idProducto = '".$_POST['idProducto']."'";

    $response = $db->ejecutarConsulta($sql);
    $array = array();
    while($row = mysqli_fetch_array($response)){

        array_push($array, array('formato' => $row['formato'], 'tipo'=> $row['tipo']));


    }


    echo json_encode($array, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_PRETTY_PRINT);

}

