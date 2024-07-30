<?
include_once('../../connexion/bdManager.php');
$manager = new bdManager();

	$c_marcas = "SELECT concat(m.nombre,'-',mt.tipo) as text,cm.idMarca as id from Marca m 
	inner join ClientesMarcas cm on (m.idMarca=cm.idMarca)
	inner join marca_tipos mt on (m.tipo=mt.idTipom) 
	where cm.idCliente='".$_GET['idCliente']."'";
	
	$r_marcas=$manager->ejecutarConsulta($c_marcas);
	$i=0;
	$marcas_asig=array();
	$marcas_asig[$i]['id']='0';
	$marcas_asig[$i]['text']='Marcas Asignadas';
	$marcas_asig[$i]['state']='closed';
	
	$j=0;
	while($a_marcas = mysqli_fetch_array($r_marcas))//aqui 
	{
		$marcas_asig[$i]['children'][$j]['id']=$a_marcas['id'];
			
		$marcas_asig[$i]['children'][$j]['text']=$a_marcas['text'];	
	
		$j++;
	}
	if($j==0)
	{
		$marcas_asig="";
		}
	$marcas_asignadas=json_encode($marcas_asig,JSON_PARTIAL_OUTPUT_ON_ERROR);
	
	echo $marcas_asignadas;
?>
  