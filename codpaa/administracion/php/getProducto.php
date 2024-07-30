<?



ob_start();

session_start();







include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

    $idMarca = $_GET['idMarca'];

    $idUsuario = $_SESSION['idUser'];


    $manager = new bdManager();

    $sql = "SELECT idProducto,concat(nombre,' ',presentacion) as nombre FROM Producto 
	where idMarca='".$idMarca."' order by nombre asc ";


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



