<?

ob_start();
session_start();



include_once('../connexion/bdManager.php');

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	$idCel = $_GET['id'];
	$fecha = $_GET['fecha'];



	$manager = new bdManager();
	$sql = "SELECT distinct m.grupo, m.sucursal, m.x, m.y FROM maestroTiendas As m inner join tiendasVisitadas as t on m.idTienda=t.idTienda where t.fecha='".$fecha."' and t.idCelular=".$idCel;
	
	
	
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






?>