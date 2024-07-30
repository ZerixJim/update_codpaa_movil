<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 10/03/2017
 * Time: 10:31 AM
 */

ob_start();
session_start();

if (isset($_SESSION['id_perfil']) && isset($_POST['selected'])) {


    include_once "../../connexion/DataBase.php";

    $db = DataBase::getInstance();

    $fotos = $_POST['selected'];


    $inserted = array();
    foreach( $fotos as $foto){

        $sql = "update photoCatalogo set evento=1 WHERE idphotoCatalogo=". $foto['idphotoCatalogo'];


        if($db->ejecutarConsulta($sql)){
            array_push($inserted, array('idFoto' => $foto['idphotoCatalogo']));
        }



    }

    if(count($inserted) > 0 ){
        echo json_encode(array('success'=> true, 'inserted'=> $inserted),
            JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT | JSON_PARTIAL_OUTPUT_ON_ERROR);
    }


}