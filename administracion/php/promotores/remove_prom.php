<?

include_once('../../connexion/DataBase.php');
include_once ('../../complemento/gcm.php');

$idPromotor=$_REQUEST['idPromo'];
$fechaBaja=$_REQUEST['fechaBaja'];



$fechaBaja = date('Y-m-d', strtotime($_REQUEST['fechaBaja']));


$sql = "update Promotores set status='b',fechaBaja='$fechaBaja' where idCelular='$idPromotor'";
$base = DataBase::getInstance();

$result = $base->ejecutarConsulta($sql);

$gcm = new GCM();

$manager = DataBase::getInstance();

$data = array();

$data['tipo'] = "baja";
$data['idPromotor'] = $idPromotor;
$data['status'] = "b";


$sql = "select token_gcm from Promotores where idCelular=".$idPromotor;


$re = $manager->ejecutarConsulta($sql);

$token = mysqli_fetch_array($re);

$respues = $gcm->send($token['token_gcm'], $data);



if ($result){
	echo '1';
} else {
	echo '0';
}
