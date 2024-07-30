<?php

/**

 * Created by PhpStorm.

 * User: Gustavo

 * Date: 14/08/14

 * Time: 13:03

 */



ob_start();

session_start();







include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


    $manager = new bdManager();

    $sql = "SELECT idExhibicion,nombre FROM tipoExhibicion WHERE status='1' order by idExhibicion asc ";







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

