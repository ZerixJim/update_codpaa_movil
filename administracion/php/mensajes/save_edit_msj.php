<?

include_once('../../connexion/DataBase.php');


$titulo = $_REQUEST['titulo_msjE'];
$idMarca = $_REQUEST['MarcaMsjE'];
$asunto = $_REQUEST['asunto_msjE'];
$mensaje = $_REQUEST['contenido_msjE'];
$idMensaje=$_REQUEST['id_msjdE'];
$idTipoPromo = $_REQUEST['tipo-promo'];


$sql = "update mensajes set titulo='$titulo',id_marca='$idMarca',asunto='$asunto',mensaje='$mensaje', id_tipo_promotor = $idTipoPromo 
where id_mensaje='".$idMensaje."'";
$base = DataBase::getInstance();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
