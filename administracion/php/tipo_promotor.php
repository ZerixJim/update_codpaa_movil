<?php

ob_start();

session_start();







include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){





    $idUser = $_SESSION['idUser'];





    $manager = new bdManager();

    $sql = "SELECT tu.idTipoUsuario,tu.nombreTipo FROM codpaa.tipoUsuario as tu

inner join codpaa.usuarioTipoAsignado as uta on tu.idTipoUsuario=uta.idTipoUsuario where uta.idUsuario=".$idUser." ";

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



