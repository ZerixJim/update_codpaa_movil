<?

ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/bdManager.php');
    include_once('../../php/seguridad.php');

    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];

    if (isset($_REQUEST['buscarMat'])) {
        $_SESSION['buscarMat'] = $_REQUEST['buscarMat'];
    } else if ($_REQUEST['buscarMat'] == "*") {
        $_SESSION['buscarMat'] = "*";
    }
    $filtro = "";
    if ($_SESSION['buscarMat'] != "*" and isset($_SESSION['buscarMat'])) {

        $filtro = "  and (material like '%" . $_SESSION['buscarMat'] . "%' OR unidad like '%" . $_SESSION['buscarMat'] . "%') ";
    }

    $manager = new bdManager();
    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '11') {

        $sql = "select * from materiales
		where estatus='1' " . $filtro . "";

    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }
    header('Content-Type: application/json');
    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}


