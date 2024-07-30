<?
ob_start();
session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

    include_once('../connexion/DataBase.php');
	$idCel = $_GET['id'];
	$fecha = $_GET['fecha'];



	$manager = DataBase::getInstance();
	$sql = "SELECT DISTINCT(canal) FROM maestroTiendas";
	
	
	
	$rs = $manager->ejecutarConsulta($sql);
    $result = array();  
    while($row = mysqli_fetch_object($rs)){  
        array_push($result, $row);  
    }  
      
    echo json_encode($result,JSON_PRETTY_PRINT);
	
}else{
	echo 'no has iniciado sesion';

}
