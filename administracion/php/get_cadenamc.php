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

    $sql = "SELECT mt.cadena FROM maestroTiendas mt 
	inner join cod_tienda_marca_promotor cod on (mt.idTienda=cod.idTienda)
	 where idTipoTienda='2'  group by cadena order by cadena";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();
	
	$ini=array('grupo'=>'TODOS');
	
	 array_push($result, $ini);

    while($row = mysqli_fetch_object($rs)){
		
		$row->grupo=utf8_encode($row->grupo);

        array_push($result, $row);

    }



    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);



}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

