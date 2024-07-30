<?

ob_start();

session_start();


include_once('../../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){





    $idMarca = $_GET['idMarca'];

	$Estado=$_GET['Estado'];
	
	$idTipo=$_GET['idTipo'];
	
	$id_region=$_GET['idRegion'];
	
	$desde=$_GET['Desde'];
	
	$hasta=$_GET['Hasta'];
	
	$fecha_a=explode('-',$desde);
	
	$fecha_b=explode('-',$hasta);
	
	$fechaInicio2=$fecha_a[2]."-".$fecha_a[1]."-".$fecha_a[0];
	
	$fechaFin2=$fecha_b[2]."-".$fecha_b[1]."-".$fecha_b[0];
	
	
    $manager = new bdManager();

    $sql = "SELECT concat(s.nombreSupervisor,' ',s.apellidoSupervisor,' fotos(',count(idphotoCatalogo),')') as nombre, s.idSupervisores as idSupervisor

            FROM photoCatalogo pc
			
			inner join Promotores p on (p.idCelular=pc.id_promotor)
			inner join Supervisores s on (p.Supervisor=s.idSupervisores)

            where ";
			
	$id_marcas=explode(',',$idMarca);
	$n_marc=count($id_marcas);
	$k=0;
	if($n_marc>0)
	{
		foreach($id_marcas as $marcas)
		{
		   if($k==0)
		  {
			$sql.=" (pc.id_marca='".$marcas."'";
		  }
		else
		{
			$sql.=" or pc.id_marca='".$marcas."'";
			}
			$k++;
	    }
		$sql.=")";
	}			

	if($id_region!="" && $id_region!="all")
	{
		
		$query_est="select * from estados where id_region='".$id_region."'";
		$rs_est=$manager->ejecutarConsulta($query_est);
		$n_est=0;
	    while($dat_est=mysqli_fetch_array($rs_est))
		{
		 	if($n_est==0)
			{
				$sql.=" and (p.idEstado='".$dat_est['id']."'";
				}  		
			else
			{
				$sql.=" or p.idEstado='".$dat_est['id']."'";
				}
			$n_est++;
		}
		
		$sql.=")";
	}

	if(isset($desde) && !empty($_GET['Desde']) && isset($hasta) && !empty($_GET['Hasta']) ){
	
			//$sql.=" and (STR_TO_DATE(pc.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) ";
			$sql.=" and DATE(pc.fecha_captura) BETWEEN '".$fechaInicio2."' and '".$fechaFin2."'";
		}


	if($Estado!="")
	{
		$sql.=" and p.idEstado='".$Estado."'";
		}

	if($idTipo!="")
	{
		$sql.=" and p.idtipoPromotor='".$idTipo."'";
		}

   $sql.=" group by s.idSupervisores";



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













