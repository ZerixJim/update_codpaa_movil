<?



ob_start();

session_start();


include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){

    $idCel = $_GET['id'];

    $fecha = $_GET['fecha'];

    $idUsuario = $_SESSION['idUser'];







    $manager = new bdManager();

     $sql = "SELECT DISTINCT SUBSTRING(fecha,-4,4) as Anio FROM tiendasVisitadas group by Anio order by Anio asc ";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while($row = mysqli_fetch_object($rs)){
		
		$fecha=explode("-",$row->Anio);
		//$row->Anio=$fecha[2];

        array_push($result, $row);

    }


    echo json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);



}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



