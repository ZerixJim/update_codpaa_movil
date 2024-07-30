<?
ob_start();
session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	$idMarca = $_GET['idMarca'];
	$idEstado = $_GET['idEstado'];
	$CliSum = $_GET['CliSum'];
	$manager = new bdManager();
	$filtro="";
	if(isset($idEstado) && $idEstado!='' && $idEstado!='all')
	{
		$filtro.=" and mT.idEstado='".$idEstado."'";
		}
	if(isset($CliSum) && $CliSum!='' && $CliSum!='all')
	{
		$filtro.=" and mT.numeroEconomico='".$CliSum."'";	
		}	
	if($idMarca==75)
	{
		$sql="select mT.idTienda,mT.direccion, mT.sucursal, mT.x, mT.y
		from maestroTiendas mT 
		left join cod_tienda_marca_promotor cod on (mT.idTienda=cod.idTienda)
		where  cod.idMarca='".$idMarca."' ".$filtro."
		group by mT.idTienda
		order by mT.idTienda asc";
		}
	else
	{
		$year=date('Y');
		
		$sql = "select mT.idTienda,mT.direccion, mT.sucursal, mT.x, mT.y
		from maestroTiendas mT 
		left join photoCatalogo fot on (fot.id_tienda=mT.idTienda)
		left join frentesCharola fc on (fc.idTienda=mT.idTienda)
		where  fc.idMarca='".$idMarca."' and fot.id_marca='".$idMarca."' ".$filtro."
		and (fot.ano='".$year."' or fc.fecha LIKE '".$year."')
		group by mT.idTienda
		order by mT.idTienda asc";
	
		}
	
	$rs = $manager->ejecutarConsulta($sql);
    $result = array();  
    while($row = mysqli_fetch_object($rs)){  
		
		$sql_fot="Select imagen,fecha from photoCatalogo 
		where id_tienda='".$row->idTienda."' and id_marca='".$idMarca."' 
		order by idphotoCatalogo desc limit 1";
		$rs_fot=$manager->ejecutarConsulta($sql_fot);
		$dato_foto = mysqli_fetch_array($rs_fot);

		$row->imagen=$dato_foto['imagen'];
		$row->fecha=$dato_foto['fecha'];
		
        array_push($result, $row);  
    }
	
   
    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR); 
	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>