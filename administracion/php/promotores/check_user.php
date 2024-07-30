<?php
session_start();
include_once('../../connexion/bdManager.php');
$manager = new bdManager();

$user=$_REQUEST["user_ch"];

$query_prom="select * from Promotores 
	where usuario = '".$user."' and idCelular is not NULL";

	$result_prom = $manager->ejecutarConsulta($query_prom);

	$dato_prom=mysqli_fetch_array($result_prom);
	
	echo $dato_prom['usuario'];

?>
