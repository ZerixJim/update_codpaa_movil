<?

include_once('../../connexion/bdManager.php');

$promotor = $_REQUEST['idPromotor'];
$marcas = $_REQUEST['marcas'];

$base = new bdManager();

foreach($marcas as $marca){

    $sql = "delete from marcaAsignadaPromotor where idPromotor='$promotor' and idMarca='$marca'";

    echo $base->ejecutarConsulta($sql);
}









