<?php



include_once('../../connexion/ConexionPDO.php');


$pdo = \v2\data\ConexionPDO::getInstance()->getDB();





$query_tien = "SELECT * FROM maestroTiendas mt 
	WHERE mt.idTipoTienda = 2 AND mt.idEstado IN ( 7, 23, 27, 30, 31 )";


$sentense = $pdo->prepare($query_tien);


$sentense->execute();


echo json_encode($sentense->fetchAll(PDO::FETCH_ASSOC));