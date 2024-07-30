<?
include_once('../connexion/DataBase.php');

$manager = DataBase::getInstance();
$idMarca = $_GET['idMarca'];

$Estados = $_GET['Estado'];
$filtro = "";

$id_estado = explode(',', $Estados);

$n_estado = count($id_estado);
$k = 0;
if ($n_estado > 0) {
    foreach ($id_estado as $estados) {
        if ($estados != "all") {
            if ($k == 0) {
                $filtro .= " and (p.idEstado='" . $estados . "' ";
            } else {
                $filtro .= " or p.idEstado='" . $estados . "'";
            }
            $k++;
        }
    }
    if ($k != 0) {
        $filtro .= ")";
    }
}


$filtro_marc = "";
$id_marcas = explode(',', $idMarca);
$n_marc = count($id_marcas);
$k = 0;
if ($n_marc > 0) {
    foreach ($id_marcas as $marcas) {
        if ($k == 0) {
            $filtro_marc .= "'" . $marcas . "'";
        } else {
            $filtro_marc .= ",'" . $marcas . "'";
        }
        $k++;
    }
    $filtro_marc .= "";
}
$query_sup = "SELECT s.idSupervisores AS idSupervisor,concat(nombreSupervisor,' ',apellidoSupervisor) AS nombreSupervisor FROM Supervisores s 
				INNER JOIN Promotores p ON (s.idSupervisores=p.Supervisor)
				INNER JOIN marcaAsignadaPromotor mp ON (p.idCelular=mp.idPromotor)
				WHERE mp.idMarca IN (" . $filtro_marc . ") AND s.`status`=1  " . $filtro . " 
				GROUP BY idSupervisores  
				ORDER BY nombreSupervisor ASC";

$rs = $manager->ejecutarConsulta($query_sup);



$result = array();
while ($row = mysqli_fetch_object($rs)) {

    array_push($result, $row);
}

echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);
