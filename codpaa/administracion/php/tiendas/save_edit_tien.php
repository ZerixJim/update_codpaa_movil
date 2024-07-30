<?
include_once('../../connexion/DataBase.php');


$sucursal = $_REQUEST['sucursal'];
$numeroEconomico = $_REQUEST['numeroEconomico'];
$direccion = $_REQUEST['direccion'];
$colonia = $_REQUEST['colonia'];
$idEstado = $_REQUEST['idEstado'];
$cp = $_REQUEST['cp'];
$idMunicipio = $_REQUEST['idMunicipio'];
$telefono = $_REQUEST['telefono'];
$latitud = $_REQUEST['latitud'];
$longitud = $_REQUEST['longitud'];
$idTipoTien = $_REQUEST['idTipoTien'];
$idTienda = $_REQUEST['idTienda'];
$idFormato = $_REQUEST['idFormato'];

$idRazon = $_REQUEST['razonSocial'];

$base = DataBase::getInstance();

$municipio_q = "Select nombre from municipios where id='" . $idMunicipio . "'";
$municipio_r = $base->ejecutarConsulta($municipio_q);
$municipio_d = mysqli_fetch_array($municipio_r);

$sql = "update maestroTiendas 
        set sucursal='$sucursal',numeroEconomico='$numeroEconomico',direccion='$direccion',colonia='$colonia',cp='$cp',idEstado='$idEstado',
        telefono='$telefono',x='$latitud',y='$longitud',idTipoTienda='$idTipoTien',idFormato='$idFormato', id_municipio= $idMunicipio ,
        lat = '$latitud', lon = '$longitud' , razonsocial = '$idRazon' 
        
        where idTienda='" . $idTienda . "'";

$result = $base->ejecutarConsulta($sql);


if ($result) {
    echo '1';
} else {
    echo '0';
}
