<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
ob_start();
session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) :

    $x = $_REQUEST['latitud'];
    $y = $_REQUEST['longitud'];
    $formato = $_REQUEST['formato'];
    $distancia = .05;

    include_once('../../connexion/ConexionPDO.php');
    $pdo = ConexionPDO::getInstance() -> getDB();

    $cmd = "SELECT * FROM maestroTiendas mt WHERE distancia(mt.x,mt.y,:x,:y) <= :distancia AND mt.idFormato = :formato";

    $sentencia = $pdo->prepare($cmd);

    $sentencia -> bindParam(':x',$x,PDO::PARAM_STR);
    $sentencia -> bindParam(':y',$y,PDO::PARAM_STR);
    $sentencia -> bindParam(':distancia',$distancia,PDO::PARAM_STR);
    $sentencia -> bindParam(':formato',$formato,PDO::PARAM_STR);

    $sentencia ->execute();
    $array = $sentencia -> fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($array);



endif;
