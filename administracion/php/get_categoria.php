<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 14/08/14

 * Time: 13:03

 */



ob_start();

session_start();





if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){



    include_once('../connexion/DataBase.php');

    $idCel = $_GET['id'];

    $fecha = $_GET['fecha'];


    $manager = DataBase::getInstance();

    $sql = "SELECT id_categoria, categoria 
            FROM categorias_productos";




    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while($row = mysqli_fetch_object($rs)){

        array_push($result, $row);

    }



    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);



}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

