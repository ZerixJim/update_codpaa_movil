<?



ob_start();

session_start();







include_once('../connexion/bdManager.php');



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){



    $estado = $_GET['Estado'];





    $manager = new bdManager();

    $sql = "SELECT m.idTienda,concat(m.grupo,' ', m.sucursal) as nombre

            FROM maestroTiendas as m";



    if($estado!=""){

        $sql .=" where m.estado='".$estado."'";

    }



    $sql.=" group by m.idTienda";



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













