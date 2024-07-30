<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');

    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];
    $filtro = "";
    $sql = "";


    if (!empty($_REQUEST['idEstado']) and isset($_REQUEST['idEstado'])) {

        $filtro .= "  and P.idEstado='" . $_REQUEST['idEstado'] . "'";
    }


    $manager = DataBase::getInstance();

    if ($_SESSION['id_perfil'] == "1") {

        $sql = "SELECT P.idCelular,S.nombreSupervisor,P.nombre,e.abrev AS ciudad FROM Promotores P
		LEFT JOIN Supervisores S ON P.Supervisor=S.idSupervisores 
		LEFT JOIN estados e ON (P.idEstado=e.id)
		WHERE P.status='a' " . $filtro . "";

    } else if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '4' || $_SESSION['id_perfil'] == '11' || $_SESSION['id_perfil'] == '13') {
        $sql = "SELECT P.idCelular,Su.nombreSupervisor,P.nombre,e.abrev AS ciudad FROM Promotores AS P 
		LEFT JOIN Supervisores AS Su ON P.Supervisor=Su.idSupervisores 
		INNER JOIN marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		LEFT JOIN estados e ON (P.idEstado=e.id)
		 WHERE P.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "' )
		  " . $filtro . " 
		 GROUP BY P.nombre
		 ORDER BY P.nombre ASC";

    } else if ($_SESSION['id_perfil'] == "3" || $_SESSION['id_perfil'] == '8') {

        /*supervisores y vendedor */

        $sql = "SELECT P.idCelular,Su.nombreSupervisor,P.nombre,e.abrev AS ciudad FROM Promotores AS P 
		LEFT JOIN Supervisores AS Su ON P.Supervisor=Su.idSupervisores 
		LEFT JOIN estados e ON (P.idEstado=e.id)
		 WHERE P.status='a' AND P.Supervisor= (SELECT idSupervisor FROM usuarios WHERE idUsuario='" . $idUsuario . "') 
		  " . $filtro . "
		 GROUP BY P.nombre
		 ORDER BY P.nombre ASC";

    } else if ($_SESSION['id_perfil'] == "6") {

        $sql = "SELECT P.idCelular,S.nombreSupervisor,P.nombre,e.abrev AS ciudad FROM Promotores AS P 
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores 
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular) 
		LEFT JOIN estados e ON (P.idEstado=e.id)
		WHERE P.status='a' AND 
		M.idMarca IN (SELECT idMarca FROM ClientesMarcas WHERE idCliente = (SELECT idCliente FROM usuarios WHERE idUsuario='" . $idUsuario . "'))
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";

    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_PRETTY_PRINT);
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

