<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];


    $sql = "";

    $manager = DataBase::getInstance();
    if ($_SESSION['id_perfil'] == 1 || $_SESSION['id_perfil'] == 10) {

        $sql = "SELECT p.idCelular,p.nombre
		FROM Promotores p
		order by p.idCelular ";

    } else {

        $sql = "SELECT p.idCelular,
		p.nombre
		FROM Promotores AS p    
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=p.idCelular)
		WHERE p.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		
		GROUP BY p.nombre
		 ORDER BY p.nombre ";

    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {array_push($result, $row);}

    $result = json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


    echo $result;
} else {
    echo 'no has iniciado sesion';
}


