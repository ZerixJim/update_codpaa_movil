<?



ob_start();

session_start();







include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

    $idCel = $_GET['id'];

    $fecha = $_GET['fecha'];

    $idUsuario = $_SESSION['idUser'];


    $manager = new bdManager();

    $sql = "SELECT concat(ma.idMarca,'-',logo_img) as idMarca,ma.nombre FROM Marca as ma inner join usuariosMarcaAsignada as ua on ma.idMarca=ua.idMarca 
	where ua.idUsuario=".$idUsuario." order by ma.nombre asc ";


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



