<?php
ob_start();
session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	$idCel = $_REQUEST['id'];
	$fecha = $_REQUEST['fecha'];

	$fecha = date('d-m-Y',strtotime($fecha));

    include_once('../connexion/ConexionPDO.php');


	$manager = ConexionPDO::getInstance()->getDB();
	$sql = "select Distinct idCelular, fecha, hora, latitud, longitud from rastreo 
	where idCelular= :idPromo and fecha= :fecha order by hora desc limit 80";

	$sentense = $manager->prepare($sql);
	$sentense->bindParam(':idPromo', $idCel, PDO::PARAM_INT);
	$sentense->bindParam(':fecha', $fecha);

	$sentense->execute();

	echo json_encode($sentense->fetchAll());

}else{
	http_response_code(422);
}




