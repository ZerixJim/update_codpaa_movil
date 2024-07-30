<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/bdManager.php');
    include_once('../../php/seguridad.php');

    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];

    if (isset($_REQUEST['buscar'])) {
        $_SESSION['buscarUser'] = $_REQUEST['buscar'];
    } else if ($_REQUEST['buscar'] == "*") {
        $_SESSION['buscarUser'] = "*";
    }
    $filtro = "";
    if ($_SESSION['buscarUser'] != "*" and isset($_SESSION['buscarUser'])) {
        $filtro = "  where (u.nombre like '%" . $_SESSION['buscarUser'] . "%' OR u.user like '%" . $_SESSION['buscarUser'] . "%')";
    }

    $manager = new bdManager();
    $sql = "select u.idUsuario,u.nombre,u.user,p.perfil, u.estatus from usuarios u 
		inner join usuarios_perfiles p on (u.id_perfil=p.id_perfil)   " . $filtro . " order by u.idUsuario";


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

