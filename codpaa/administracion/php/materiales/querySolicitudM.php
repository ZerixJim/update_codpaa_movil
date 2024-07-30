<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/bdManager.php');
    include_once('../../php/seguridad.php');

    $manager = new bdManager();

    $idUsuario = $_SESSION['idUser'];

    if (isset($_REQUEST['buscarSolicitudM'])) {
        $_SESSION['buscarSolicitudM'] = $_REQUEST['buscarSolicitudM'];
    } else if ($_REQUEST['buscarSolicitudM'] == "*") {
        $_SESSION['buscarSolicitudM'] = "*";
    }

    if (isset($_REQUEST['filtroE'])) {
        $_SESSION['filtroE'] = $_REQUEST['filtroE'];
    }
    $filtro = "";

    if ($_SESSION['filtroE'] == "" || !isset($_SESSION['filtroE'])) {
        $filtro = " where ms.estatus>=0";
    } else {
        $filtro = " where ms.estatus='" . $_SESSION['filtroE'] . "'";
    }

    if ($_SESSION['buscarSolicitudM'] != "*" and isset($_SESSION['buscarSolicitudM'])) {

        $filtro .= " and (ms.id_mat_solicitud='" . $_SESSION['buscarTienda'] . "' OR p.nombre like '%" . $_SESSION['buscarSolicitudM'] . "%' 
					OR m.material like '%" . $_SESSION['buscarSolicitudM'] . "%' OR ms.fecha='" . $_SESSION['buscarSolicitudM'] . "')";
    }

    $sql = '';

    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '11') {

        $sql = "SELECT p.idCelular, date(ms.fecha) as fecha , month(ms.fecha) as mes,
          p.nombre AS promotor
		FROM materiales_solicitud ms
		INNER JOIN Promotores p ON (p.idCelular=ms.id_promotor)
		INNER JOIN materiales m ON (m.id_material=ms.id_material)
		INNER JOIN maestroTiendas mt ON (mt.idTienda=ms.idTienda)

        ".$filtro."
		group by month(ms.fecha), ms.id_promotor

		ORDER BY ms.fecha desc, p.nombre ASC";

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


