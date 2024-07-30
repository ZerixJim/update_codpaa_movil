<?


ob_start();

session_start();





if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../connexion/DataBase.php');

    $idCel = $_GET['id'];

    $fecha = $_GET['fecha'];

    $idUsuario = $_SESSION['idUser'];


    $manager = DataBase::getInstance();


    if ($_SESSION['id_perfil'] == 1 || $_SESSION['id_perfil'] == 12){


        $sql = "SELECT ma.idMarca,ma.nombre AS nombre, c.razonsocial as cliente, ma.idMarca, ma.logo_img logo FROM Marca AS ma
            
            left join ClientesMarcas as cm on cm.idMarca=ma.idMarca
            left join Clientes as c on c.idCliente=cm.idCliente
            WHERE ma.estatus = 1
	        ORDER BY c.razonsocial, ma.nombre ASC ";
    }else{

        $sql = "SELECT ma.idMarca,ma.nombre AS nombre, c.razonsocial as cliente, ma.idMarca, ma.logo_img logo FROM Marca AS ma
            INNER JOIN usuariosMarcaAsignada AS ua ON ma.idMarca=ua.idMarca
            left join ClientesMarcas as cm on cm.idMarca=ma.idMarca
            left join Clientes as c on c.idCliente=cm.idCliente
	        WHERE ma.estatus = 1 AND ua.idUsuario='". $idUsuario ."' ORDER BY c.razonsocial, ma.nombre ASC ";
    }





    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while($row = mysqli_fetch_assoc($rs)){

        array_push($result, $row);

    }


    echo json_encode($result, JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);


} else {

    echo 'no has iniciado sesion';
    http_response_code(422);

    header('refresh:2,../index.php');

}



