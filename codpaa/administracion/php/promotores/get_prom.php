<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];

    if (isset($_REQUEST['buscarProm'])) {
        $_SESSION['buscarProm'] = $_REQUEST['buscarProm'];
    } else if ($_REQUEST['buscarProm'] == "*") {
        $_SESSION['buscarProm'] = "*";
    }
    $filtro = "";
    if ($_SESSION['buscarProm'] != "*" and isset($_SESSION['buscarProm'])) {

        $filtro = " and (P.idCelular='" . $_SESSION['buscarProm'] . "' OR S.nombreSupervisor like '%" . $_SESSION['buscarProm'] .
            "%' OR P.nombre like '%" . $_SESSION['buscarProm'] . "%' OR Es.nombre like '%" . $_SESSION['buscarProm'] . "%')";

        if($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '10'){
            $filtro = " where P.idCelular='" . $_SESSION['buscarProm'] . "' OR S.nombreSupervisor like '%" . $_SESSION['buscarProm'] .
                "%' OR P.nombre like '%" . $_SESSION['buscarProm'] . "%' OR Es.nombre like '%" . $_SESSION['buscarProm'] . "%'";
        }
    }


    $sql = "";

    $manager = DataBase::getInstance();
    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '10') {

        $sql = "SELECT P.idCelular, concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor, pr.idRuta AS Ruta,
		P.nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status, concat(i.start_date,' - ', i.end_date) inca
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)  
		LEFT JOIN incapacidad i ON (i.id_promotor=P.idCelular AND ( CURDATE() BETWEEN i.start_date AND i.end_date) )
		LEFT JOIN promotor_ruta pr ON (pr.idPromotor = P.idCelular)
		" . $filtro . "
		order by P.idCelular asc";

    } else if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '4' || $_SESSION['id_perfil'] == '11' || $_SESSION['id_perfil'] == '13') {

        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		P.nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status ,concat(i.start_date,' - ', i.end_date) inca
		FROM Promotores AS P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores AS S ON P.Supervisor=S.idSupervisores
		LEFT JOIN incapacidad i ON (i.id_promotor=P.idCelular AND ( CURDATE() BETWEEN i.start_date AND i.end_date) )
		
		    
		INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular)
		WHERE P.status='a' AND M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		" . $filtro . "
		GROUP BY P.nombre
		 ORDER BY P.nombre ASC";

    } else if ($_SESSION['id_perfil'] == '8') {
        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		P.nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)
		WHERE P.status='a' AND P.Supervisor = (SELECT idSupervisor FROM usuarios WHERE idUsuario='" . $idUsuario . "') " . $filtro . "";

    } else if ($_SESSION['id_perfil'] == '6') {
        $sql = "SELECT P.idCelular,concat(S.nombreSupervisor,' ',S.apellidoSupervisor) AS nombreSupervisor,
		P.nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status
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
		P.nombre,P.usuario,P.password,P.numero_celular,Es.nombre AS Estadomx, P.status
		FROM Promotores P
		LEFT JOIN estados Es ON (Es.id=P.idEstado)
		LEFT JOIN Supervisores S ON (P.Supervisor=S.idSupervisores)
		WHERE P.status='a' AND
		P.Supervisor IN (SELECT idSupervisores FROM Supervisores WHERE
		idGerente=(SELECT idGerente FROM usuarios WHERE idUsuario='" . $idUsuario . "')) " . $filtro . "";

    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {array_push($result, $row);}

    $result = json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


    echo $result;
} else {
    echo 'no has iniciado sesion';
}


