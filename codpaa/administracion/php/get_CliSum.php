<?php

/**

 * Created by Dream.

 * User: Christian M
 
 * Date: 07/10/15

 * Time: 13:03

 */



ob_start();

session_start();

include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

	$idMarca = $_REQUEST['idMarca'];
	$idEstado = $_REQUEST['idEstado'];

    $manager = new bdManager();

   $filtro="";
	if(isset($idEstado) && $idEstado!='' && $idEstado!='all')
	{
		$filtro.="and mT.idEstado='".$idEstado."'";
		}
	if($idMarca==75)
	{
		$sql="select mT.numeroEconomico as id_summa,mT.grupo as cli_sum
	from maestroTiendas mT 
	left join cod_tienda_marca_promotor cod on (mT.idTienda=cod.idTienda)
	where  cod.idMarca='".$idMarca."' ".$filtro."
	group by mT.numeroEconomico
	order by mT.numeroEconomico asc";
		}
	else
	{
		$sql = "select mT.numeroEconomico as id_summa,mT.grupo as cli_sum
	from maestroTiendas mT 
	left join photoCatalogo fot on (fot.id_tienda=mT.idTienda)
	left join frentesCharola fc on (fc.idTienda=mT.idTienda)
	where  fc.idMarca='".$idMarca."' and fot.id_marca='".$idMarca."' ".$filtro."
	group by mT.numeroEconomico
	order by mT.numeroEconomico asc";
	
		}


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();
	
	$ini=array('cli_sum'=>'TODOS', 'id_summa'=>'all');
	
	 array_push($result, $ini);

    while($row = mysqli_fetch_object($rs)){
		
		$row->cli_sum=utf8_encode($row->cli_sum);

        array_push($result, $row);

    }



    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);



}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

