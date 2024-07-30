<?php

include_once('../../connexion/bdManager.php');

$idProducto=$_REQUEST['idProd'];


$sql = "update Producto set estatus='0' where idProducto='$idProducto'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
