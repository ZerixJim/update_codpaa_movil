<?
include_once('../../connexion/DataBase.php');


$manager = DataBase::getInstance();

$c_marcas = "SELECT CONCAT(m.nombre,' - ',mt.tipo) nombre, um.idMarca id,
    IF(c.razonsocial IS NULL, 'Sin cliente', c.razonsocial) cliente, c.alias 
    FROM Marca m
	LEFT JOIN usuariosMarcaAsignada um ON (m.idMarca=um.idMarca) 
	LEFT JOIN marca_tipos mt ON (m.tipo=mt.idTipom)
	
	INNER JOIN ClientesMarcas cm ON (cm.idMarca=m.idMarca)
	INNER JOIN Clientes c ON (c.idCliente=cm.idCliente)
	
	
	WHERE m.estatus = 1 
	AND um.idUsuario='" . $_POST['idUsuario'] . "' 
	ORDER BY m.nombre";

$r_marcas = $manager->ejecutarConsulta($c_marcas);

$marcas_asig = array();


while ($a_marcas = mysqli_fetch_array($r_marcas)) {

    array_push($marcas_asig, array('idMarca'=> $a_marcas['id'],
        'nombre'=> $a_marcas['nombre'], 'cliente'=>$a_marcas['cliente'],
        'alias'=> $a_marcas['alias']));

}


echo json_encode(array('marcas'=> $marcas_asig), JSON_PARTIAL_OUTPUT_ON_ERROR
    | JSON_NUMERIC_CHECK);

  