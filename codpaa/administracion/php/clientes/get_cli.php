<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    if (isset($_REQUEST['buscarCli'])) {
        $_SESSION['buscarCli'] = $_REQUEST['buscarCli'];
    } else if ($_REQUEST['buscarCli'] == "*") {
        $_SESSION['buscarCli'] = "*";
    }
    $filtro = "";
    if ($_SESSION['buscarCli'] != "*" and isset($_SESSION['buscarCli'])) {

        $filtro = "  and (razonsocial like '%" . $_SESSION['buscarCli'] . "%' 
        OR rfc like '%" . $_SESSION['buscarCli'] . "%' OR alias like '%" . $_SESSION['buscarCli'] .  "%')";
    }

    $manager = DataBase::getInstance();

    $sql = "SELECT *,concat(calle,' ',no_ext,' ',no_int, ' ',colonia,' ',cp) AS direccion FROM Clientes WHERE estatus='1' " . $filtro . " ORDER BY razonsocial";


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

