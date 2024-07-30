<?
ob_start();
session_start();


if(isset($_POST['usuario']) &&  isset($_POST['pass'])){

    include_once('../connexion/DataBase.php');
	
	$usuario = $_POST['usuario'];
	$contrasena = $_POST['pass'];
	
	if($usuario != "" && $contrasena != ""){
		$manager = DataBase::getInstance();
		$consulta = "SELECT * from usuarios where user='".$usuario."' and pass=md5('".$contrasena."') and estatus='1';";
		$resultado = $manager->ejecutarConsulta($consulta);
		if(mysqli_num_rows($resultado) > 0){
			$fila = mysqli_fetch_array($resultado);
			$_SESSION['idUser'] = $fila['idUsuario'];
			$_SESSION['usuario']= $fila['nombre'];
			$_SESSION['permiso'] = $fila['idPermiso'];
			$_SESSION['tipo'] = $fila['tipo_usuario'];
			$_SESSION['id_perfil'] = $fila['id_perfil'];
			$_SESSION["ultimoAcceso"]= date("Y-n-j H:i:s");


            $manager->ejecutarConsulta("insert into access_log(idUsuario,ip) 
                                            VALUES ('".$fila['idUsuario'] ."', '"
                                        . $_SERVER['REMOTE_ADDR'] . "')");

			
			echo "1";
			
		}else{
			echo "usuario  o Contraseña no Validos";
			
		}

		
	}else{
		echo "Debe ingresar sus datos";
	}
	
}else{
	echo "Datos inexistentes";
	
}


