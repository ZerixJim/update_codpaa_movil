<?

include_once('../../connexion/bdManager.php');

$promotor = $_REQUEST['idPromotor'];
$selects = $_REQUEST['selects'];

$base = new bdManager();


foreach($selects as $item){



	$sql = "insert into marcaAsignadaPromotor (idPromotor,idMarca) values ($promotor,$item)";

	echo $base->ejecutarConsulta($sql);

}



