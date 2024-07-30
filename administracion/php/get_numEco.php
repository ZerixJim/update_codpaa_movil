<?php

/**

 * Created by Dreamweaver.

 * User: Christian

 * Date: 16/02/16

 * Time: 12:00

 */


ob_start();

session_start();

include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

	$idMarca=$_REQUEST['idMarca'];
	$Mes=$_REQUEST['Mes'];
	$Anio=$_REQUEST['Anio'];
	
	$id_marcas=explode(',',$idMarca);
	$n_marc=count($id_marcas); 
	$k=0;
	$filtro="";
	if($n_marc>0 && isset($_REQUEST['idMarca']))
	{
		foreach($id_marcas as $marcas)
		{
		   if($k==0)
		  {
			$filtro.="'".$marcas."'";
		  }
		else
		{
			$filtro.=" ,'".$marcas."'";
			}
			$k++;
	    }
		$filtro.="";
	}
    $manager = new bdManager();

	$sql_num="Select DISTINCT mT.numeroEconomico from cod_tienda_marca_promotor ctm  
		inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
		where mT.idTipoTienda='2' and mes='".$Mes."' and anio='".$Anio."' and idMarca IN (".$filtro.")
		GROUP BY mT.numeroEconomico
		order by mT.numeroEconomico ASC";
		
     $rs = $manager->ejecutarConsulta($sql_num);

    $result = array();

    while($row = mysqli_fetch_object($rs)){

		array_push($result, $row);
	}

    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);


}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

