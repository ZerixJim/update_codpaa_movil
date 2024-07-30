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

	$id_estado=$_REQUEST['idEstado'];

    $manager = DataBase::getInstance();

    $sql = "SELECT UPPER(nombre) as municipio,id FROM municipios where estado_id='".$id_estado."'  order by nombre asc ";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while($row = mysqli_fetch_object($rs)){

		
        array_push($result, $row);
		

    }

    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);


}else{

    http_response_code(422);
    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

