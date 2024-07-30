<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 11/02/2017
 * Time: 10:56 AM
 */

include_once('../../connexion/DataBase.php');
$manager = DataBase::getInstance();

$idPromotor = $_POST['idPromotor'];

$c_marcas = "SELECT concat(m.nombre,' - ',mt.tipo) AS text,pm.idMarca AS id,
    if(c.razonsocial is NULL, 'Sin cliente', c.razonsocial) as cliente  FROM Marca m
	RIGHT JOIN  marcaAsignadaPromotor pm ON (m.idMarca=pm.idMarca)
	LEFT JOIN marca_tipos mt ON (m.tipo=mt.idTipom)
	LEFT JOIN ClientesMarcas as cm on cm.idMarca=m.idMarca
	LEFT JOIN Clientes as c on c.idCliente=cm.idCliente

	WHERE m.estatus = 1 
	AND pm.idPromotor=" . $idPromotor . " ORDER BY m.nombre";

$r_marcas = $manager->ejecutarConsulta($c_marcas);

$marcas_asig = array();


while ($a_marcas = mysqli_fetch_array($r_marcas)) {

    array_push($marcas_asig, array('idMarca'=>$a_marcas['id'], 'nombre' => $a_marcas['text'], 'cliente' => $a_marcas['cliente'] ));

}

header('Content-Type: application/json; charset=utf-8');

echo json_encode($marcas_asig, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);





