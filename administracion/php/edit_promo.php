<?
	include('../connexion/bdManager.php');
    $id = intval($_REQUEST['id']);  
    $nombre = $_REQUEST['nombre']; 
	$supervisor = $_REQUEST['nombreSupervisor'];
    $usuario = $_REQUEST['usuario'];  
    $pass = $_REQUEST['password'];  
     
    if($supervisor == "" || $supervisor == 0){
		$sql="update Promotores set nombre='$nombre',usuario='$usuario', password='$pass' where idCelular=$id"; 
	}else{
		$sql="update Promotores set Supervisor=$supervisor, nombre='$nombre',usuario='$usuario', password='$pass' where idCelular=$id";  
	}
      
    
	$bd = new bdManager;
	$result = $bd->ejecutarConsulta($sql);
      
    if ($result){
		echo json_encode(array('success'=>true));
	} else {
		echo json_encode(array('msg'=>'Error al actualizar.'));
	}


?>