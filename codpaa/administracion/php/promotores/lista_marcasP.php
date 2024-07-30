<?
session_start();
include_once('../../connexion/DataBase.php');
$manager = DataBase::getInstance();

$idUsuario = $_SESSION['idUser'];
$idPromotor = $_POST['idPromotor'];


if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil'] == '3' || $_SESSION['id_perfil'] == '10' || $_SESSION['id_perfil'] == '5') {
    $c_marcas = "SELECT m.idMarca AS id , concat(m.nombre,' - ',mt.tipo) AS text,
        if(c.razonsocial is NULL, 'Sin cliente', c.razonsocial) as cliente
        FROM Marca as m
		INNER JOIN marca_tipos mt ON (m.tipo=mt.idTipom)

		LEFT JOIN ClientesMarcas as cm ON cm.idMarca=m.idMarca
		LEFT JOIN Clientes as c on c.idCliente=cm.idCliente

		WHERE m.estatus = 1
		AND m.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
		and m.idMarca not in(select mA.idMarca from marcaAsignadaPromotor as mA where mA.idMarca=m.idMarca AND mA.idPromotor=".$idPromotor.")

		ORDER BY nombre";
} else {
    $c_marcas = "SELECT m.idMarca AS id , concat(m.nombre,' - ',mt.tipo) AS text,
        if(c.razonsocial is NULL, 'Sin cliente', c.razonsocial) as cliente FROM Marca m
		LEFT JOIN marca_tipos mt ON (m.tipo=mt.idTipom)
		LEFT JOIN ClientesMarcas as cm ON cm.idMarca=m.idMarca
		LEFT JOIN Clientes as c on c.idCliente=cm.idCliente
        WHERE m.estatus = 1 
        AND m.idMarca not in (select mA.idMarca from marcaAsignadaPromotor as mA where (mA.idMarca=m.idMarca AND mA.idPromotor=".$idPromotor.")  )
		ORDER BY nombre";


}
$r_marcas = $manager->ejecutarConsulta($c_marcas);
$marcas = array();


while ($a_marcas = mysqli_fetch_array($r_marcas)) {

    array_push($marcas, array('idMarca'=> $a_marcas['id'], 'nombre'=> $a_marcas['text'], 'cliente' => $a_marcas['cliente'] ));

}


header('Content-Type: application/json; charset=utf-8');

echo json_encode(array('marcasDisponibles'=>$marcas), JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);



     
 
               
        
