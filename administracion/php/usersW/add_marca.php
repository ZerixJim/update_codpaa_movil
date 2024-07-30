<?

include_once('../../connexion/bdManager.php');

$usuario = $_REQUEST['idUsuario'];
$marcas = $_REQUEST['marcas'];

$base = new bdManager();

$inseteds = array();


foreach($marcas as $marca){

	$sql = "insert into usuariosMarcaAsignada (idUsuario,idMarca) values ('$usuario','$marca')";

	if ($base->ejecutarConsulta($sql)){

		array_push($inseteds, array('idMarca'=> $marca));

	}

}

if(count($inseteds) > 0){

	echo json_encode(array('success'=> true, 'marcas'=> $inseteds), JSON_NUMERIC_CHECK | JSON_PARTIAL_OUTPUT_ON_ERROR);

}
