<?

include_once('../../connexion/bdManager.php');

$razonsocial = $_REQUEST['razonsocial'];
$nombre_c=$_REQUEST['nombre_c'];
$calle = $_REQUEST['calle'];
$no_int = $_REQUEST['no_int'];
$no_ext = $_REQUEST['no_ext'];
$colonia = $_REQUEST['colonia'];
$cp = $_REQUEST['cp'];
$idEstado = $_REQUEST['idEstado'];
$idMunicipio = $_REQUEST['idMunicipio'];
$telefono = $_REQUEST['telefono'];
$rfc = $_REQUEST['rfc'];


$sql = "insert into Clientes (razonsocial, nombre_contacto,calle,no_ext,no_int,colonia,cp,idEstado,idMunicipio,telefono,rfc) 
values('$razonsocial', '$nombre_c','$calle','$no_ext','$no_int','$colonia','$cp','$idEstado','$idMunicipio','$telefono','$rfc')";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>