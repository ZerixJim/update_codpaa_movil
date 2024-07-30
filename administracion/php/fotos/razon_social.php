<?php

ob_start();

session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


    include_once('../../connexion/bdManager.php');

    $idMarca = $_GET['idMarca'];


    $manager = new bdManager();

    $sql = "SELECT id,razon_social  FROM razon_social ";

    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while($row = mysqli_fetch_object($rs)){

        array_push($result, $row);

    }

    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_PRETTY_PRINT);


}else{

    echo 'no has iniciado sesion';

    http_response_code(422);

}



