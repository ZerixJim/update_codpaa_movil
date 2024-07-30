<? 

include_once('../../connexion/DataBase.php');
$manager = DataBase::getInstance();

	$c_marcas = "SELECT idMarca as id , concat(m.nombre,'-',mt.tipo) as text FROM Marca m 
	inner join marca_tipos mt on (m.tipo=mt.idTipom) order by nombre";
	$r_marcas =$manager->ejecutarConsulta($c_marcas);
	$marcas=array();
	$i=0;
	$marcas[$i]['id']='0';
	$marcas[$i]['text']='Marcas';
	$marcas[$i]['state']='closed';
	$j=0;
	while($a_marcas = mysqli_fetch_array($r_marcas)) //aqui
		{ 
			$marcas[$i]['children'][$j]['id']=$a_marcas['id'];
			
			$marcas[$i]['children'][$j]['text']=$a_marcas['text'];	
		
			
			$j++;
	
	}
	$marcas_lista=json_encode($marcas,JSON_PARTIAL_OUTPUT_ON_ERROR);
	
	echo $marcas_lista;

       
     
 
               
        
