<?

include_once('../../connexion/DataBase.php');
$manager = DataBase::getInstance();

$c_marcas = "SELECT m.idMarca AS id , CONCAT(m.nombre,' - ',mt.tipo) nombre  
   ,IF(c.razonsocial IS NULL, 'Sin cliente', c.razonsocial) cliente, c.alias 
     
  FROM Marca AS m
	INNER JOIN marca_tipos mt ON (m.tipo=mt.idTipom)
	
	INNER JOIN ClientesMarcas cm ON (cm.idMarca=m.idMarca)
	INNER JOIN Clientes c ON (c.idCliente=cm.idCliente)
	
	AND m.estatus = 1
	AND m.idMarca NOT IN(SELECT mA.idMarca 
	  FROM usuariosMarcaAsignada AS mA 
	  WHERE mA.idMarca=m.idMarca AND mA.idUsuario=" . $_POST['idUsuario'] . ") 

	ORDER BY nombre";
$r_marcas = $manager->ejecutarConsulta($c_marcas);
$marcas = array();

while ($a_marcas = mysqli_fetch_array($r_marcas)) {

    array_push($marcas, array('idMarca' => $a_marcas['id'], 'nombre' => $a_marcas['nombre'],
        'cliente'=>$a_marcas['cliente'], 'alias'=> $a_marcas['alias']));
}

echo json_encode(array('marcas' => $marcas), JSON_PARTIAL_OUTPUT_ON_ERROR |
    JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);

