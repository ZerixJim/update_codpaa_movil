<?php

include_once('../getInfo.php');

if(isset($_POST['idTien']) && isset($_POST['fecha']) && isset($_POST['idProducto']) && isset($_POST['cajas'])){
	$obtenerDatos = new getDatos();
	$obtenerDatos->insertarInventarioNew($_POST['idTien'],$_POST['fecha'],$_POST['idProducto'],$_POST['cajas']);
	
	
}else{
	echo "{\"mensaje\":\"error al recibir datos\"}";
}






?>