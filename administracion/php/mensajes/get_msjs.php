<?
ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $tipo = $_SESSION['tipo'];
    $idUsuario = $_SESSION['idUser'];

    if (isset($_REQUEST['buscarMsj'])) {
        $_SESSION['buscarMsj'] = $_REQUEST['buscarMsj'];
    } else if ($_REQUEST['buscarMsj'] == "*") {
        $_SESSION['buscarMsj'] = "*";
    }
    if (isset($_REQUEST['filtroE'])) {
        $_SESSION['filtroE'] = $_REQUEST['filtroE'];
    }
    $filtro = "";

    if ($_SESSION['filtroE'] == "" || !isset($_SESSION['filtroE'])) {
        $filtro = " and ms.estatus>=0";
    } else {
        $filtro = " and ms.estatus='" . $_SESSION['filtroE'] . "'";
    }
    if ($_SESSION['buscarMsj'] != "*" and isset($_SESSION['buscarMsj'])) {

        $filtro = "  and (ms.asunto like '%" . $_SESSION['buscarMsj'] . "%' OR m.nombre like '%" . $_SESSION['buscarMsj'] . "%') ";
    }

    $manager = DataBase::getInstance();

    //mensajes se depliengan tanto para adminitrador como para cuenta_clave
    if ($_SESSION['id_perfil'] == '1' || $_SESSION['id_perfil'] == '5') {

        $sql = "select ms.*,m.nombre as marca from mensajes ms
		inner join Marca m on  (ms.id_marca=m.idMarca) where ms.id_user = $idUsuario 
		" . $filtro . "";

    }

    $rs = $manager->ejecutarConsulta($sql);
    $result = array();
    while ($row = mysqli_fetch_object($rs)) {

        switch ($row->estatus) {
            case 0:
                $row->estatus = "CANCELADO";
                break;
            case 1:
                $row->estatus = "ACTIVO";
                break;
            case 2:
                $row->estatus = "ENVIADO";
                break;
        }
        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);
} else {
    echo 'no has iniciado sesion';
    header('refresh:2,../index.php');
}

