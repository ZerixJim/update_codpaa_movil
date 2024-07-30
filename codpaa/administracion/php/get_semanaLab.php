<?php

/**

 * Created by Dreamweaver.

 * User: Christian

 * Date: 16/02/16

 * Time: 12:00

 */


ob_start();

session_start();

include_once('../connexion/DataBase.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

	$id_prom=$_REQUEST['id_prom'];

    $manager = DataBase::getInstance();

    $sql = "SELECT concat(semana,' - ',anio) as semana_lab,idSupervision from supervisionRutas where
    idPromotor='".$id_prom."' Group by anio,semana ORDER BY anio desc,semana desc limit 10";


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

