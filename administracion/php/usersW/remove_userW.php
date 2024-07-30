<?

include_once('../../connexion/bdManager.php');

$idUsuario = $_REQUEST['idUsuario'];

if ($perfil == '1') {
    $permiso = '3';
    $tipo_usuario = 'ALL';
} else {
    $permiso = '1';
    $tipo_usuario = '';
}

$sql = "update usuarios  set estatus='0'
where idUsuario='$idUsuario'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result) {
    echo '1';
} else {
    echo '0';
}
