<?
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	
	if($_SESSION['permiso'] <= 3){
		
		include_once('../connexion/bdManager.php');
		$manager = new bdManager();
		
		$sql = 'select idSupervisores,nombreSupervisor,apellidoSupervisor,status from Supervisores order by idSupervisores asc';
	
	
		$rs = $manager->ejecutarConsulta($sql);
    	$result = array();  
    	while($row = mysqli_fetch_object($rs)){  
        	array_push($result, $row);  
    	}  
      
    	echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR); 
		 
	}else{
		echo 'no tienes los suficientes permisos';
		header('refresh:2,../index.php');
	}

	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}


?>