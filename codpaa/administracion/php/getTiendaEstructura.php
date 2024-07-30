<?
ob_start();
session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

    include_once('../connexion/DataBase.php');

	$idUser = $_SESSION['idUser'];


	$manager = DataBase::getInstance();
	$sql = "SELECT et.id_estructura id, nombre 
	FROM estructuras_tienda et
	
	LEFT JOIN usuario_estructura ue ON (ue.id_estructura = et.id_estructura)
	
	WHERE ue.id_usuario = $idUser";
	
	$rs = $manager->ejecutarConsulta($sql);
    $result = array();  
    while($row = mysqli_fetch_object($rs)){  
        array_push($result, $row);  
    }  
      
    echo json_encode($result,JSON_PRETTY_PRINT);
	
}else{
	echo 'no has iniciado sesion';

}


