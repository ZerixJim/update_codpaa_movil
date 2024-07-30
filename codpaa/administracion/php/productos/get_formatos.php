<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 27/02/2017
 * Time: 12:15 PM
 */
ob_start();
session_start();



if (isset($_SESSION['idUser']) && isset($_POST['solicitud']) && isset($_POST['idProducto'])) {

    include_once('../../connexion/DataBase.php');


    $sql = "";

    $manager = DataBase::getInstance();
    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5') {

        if ($_POST['solicitud'] == 'asignados'){

            $sql = "select tf.idFormato, tf.grupo,tf.cadena from tiendas_formatos as tf

          where tf.idFormato in (select pf.id_formato from producto_formato as pf where pf.id_producto='".$_POST['idProducto']."')
          order by tf.grupo asc
          ";

        }else if($_POST['solicitud'] == 'disponibles'){
            $sql = "select tf.idFormato, tf.grupo,tf.cadena from tiendas_formatos as tf

          where tf.idFormato not in (select pf.id_formato from producto_formato as pf where pf.id_producto='".$_POST['idProducto']."')
          order by tf.grupo asc
          ";
        }


    }


    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);
} else {

    echo 'no has iniciado sesion';

}