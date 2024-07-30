<?
session_start();

include_once('../../connexion/DataBase.php');

$titulo = $_REQUEST['titulo'];
$idMarca = $_REQUEST['idMarca'];
$asunto = $_REQUEST['asunto'];
$mensaje = $_REQUEST['mensaje'];
$idTipo = $_REQUEST['idTipoPromotor'];

$idUser = $_SESSION['idUser'];

$sql = "insert into mensajes (titulo,id_marca,asunto,mensaje,fecha, id_tipo_promotor, id_user) 
values('$titulo','$idMarca','$asunto','$mensaje',now(), $idTipo, $idUser )";
$base = DataBase::getInstance();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
