<?
ob_start();
session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


    include_once('../../connexion/ConexionPDO.php');

	$idCel = $_GET['id'];
	$fecha = $_GET['fecha'];


	$pdo = ConexionPDO::getInstance()->getDB();

    $fecha = date("Y-m-d", strtotime($fecha));


	$command = "select Distinct tv.idTienda, mT.sucursal, mT.x, mT.y, tv.idCelular,tv.fecha, 
	tv.hora, tv.latitud, tv.longitud, tv.tipo, Promotores.nombre,f.grupo, mT.idFormato, tv.fecha_captura  
	from tiendasVisitadas tv
	left join Promotores on tv.idCelular=Promotores.idCelular 
	left join maestroTiendas As mT on mT.idTienda=tv.idTienda 
	left join tiendas_formatos f on (f.idFormato=mT.idFormato)
	where tv.idCelular=:idPromo and date(tv.fecha_captura) = :fecha order by tv.fecha_captura asc";

	$sentense = $pdo->prepare($command);
	$sentense->bindParam(':idPromo', $idCel, PDO::PARAM_INT);
	$sentense->bindParam(':fecha', $fecha, PDO::PARAM_STR);


	$sentense->execute();

	$result = $sentense->fetchAll(PDO::FETCH_ASSOC);


    echo json_encode($result,JSON_PRETTY_PRINT);
	
}else{
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

