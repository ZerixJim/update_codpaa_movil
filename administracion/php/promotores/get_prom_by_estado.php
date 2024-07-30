<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $idUsuario = $_SESSION['idUser'];
    $idEstado = $_REQUEST['idEstado'];


    $filtro = "";


    if (!empty($idEstado)){

        $filtro .= " and Es.id=". $idEstado;


    }



    $manager = DataBase::getInstance();
    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '10') {

        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)  where P.status = 'a' " . $filtro . "
		order by P.idCelular asc";

    } else if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '5') {

        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores AS P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		WHERE P.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";

    } else if ($_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '8') {
        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)
		WHERE P.status='a' AND P.Supervisor = (SELECT idSupervisor FROM usuarios WHERE idUsuario='" . $idUsuario . "') " . $filtro . "";

    } else if ($_SESSION['id_perfil'] == '6') {
        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores AS P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		WHERE P.status='a' AND
		M.idMarca IN (SELECT idMarca FROM ClientesMarcas WHERE idCliente = (SELECT idCliente FROM usuarios WHERE idUsuario='" . $idUsuario . "'))
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";
    } else if ($_SESSION['id_perfil'] == '9') {
        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)
		WHERE P.status='a' AND
		P.Supervisor IN (SELECT idSupervisores FROM Supervisores WHERE
		idGerente=(SELECT idGerente FROM usuarios WHERE idUsuario='" . $idUsuario . "')) " . $filtro . "";

    } else if ($_SESSION['id_perfil'] == '13'){


        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores AS P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		WHERE P.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";


    } else if($_SESSION['id_perfil'] == '4'){


        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		concat(P.nombre, ' ', P.idCelular) nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx
		FROM Promotores AS P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		WHERE P.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";


    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }


    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);



} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}


