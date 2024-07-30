<?

include_once('../../connexion/DataBase.php');
$base = DataBase::getInstance();

$idCliente = $_REQUEST['idCliente'];

$razonsocial = $_REQUEST['razonsocial'];
$nombre_c = $_REQUEST['nombre_c'];
$calle = $_REQUEST['calle'];
$no_int = $_REQUEST['no_int'];
$no_ext = $_REQUEST['no_ext'];
$colonia = $_REQUEST['colonia'];
$cp = $_REQUEST['cp'];
$idEstado = $_REQUEST['idEstado'];
$idMunicipio = $_REQUEST['idMunicipio'];
$telefono = $_REQUEST['telefono'];
$rfc = $_REQUEST['rfc'];
$alias = $_REQUEST['alias'];


$sql = "update Clientes set razonsocial='$razonsocial',
nombre_contacto='$nombre_c', calle='$calle',no_ext='$no_ext',
no_int='$no_int',colonia='$colonia',cp='$cp',idEstado='$idEstado',
idMunicipio='$idMunicipio',telefono='$telefono',rfc='$rfc', alias='$alias' 
where idCliente='$idCliente'";

$result = $base->ejecutarConsulta($sql);


if ($result) {
    echo '1';
} else {
    echo '0';
}
