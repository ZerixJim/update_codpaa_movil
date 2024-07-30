<?php
ob_start();
session_start();
if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
    include_once('../connexion/DataBase.php');
    
    $manager = DataBase :: getInstance();
    $sql = "SELECT idTienda,canal
    FROM maestroTiendas
    GROUP BY canal";

    $rs = $manager -> ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)){
        array_push($result,$row);
    }

    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);
}else{
    echo 'No has iniciado sesion';
    header('refresh:2,../index.php');
}