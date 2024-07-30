<?php
session_start();
include('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');
$bd = new bdManager;

$idUsuario = $_SESSION['idUser'];
$idFoto = $_REQUEST['idphotoC2'];

$fotos=explode(',',$idFoto);
 
$idExhibicion = $_REQUEST['exhibicionE'];

$n_fotos=count($fotos);

if($n_fotos>0)
{
	$j=0;
	foreach ($fotos as $idFotos)
	{		
		$sql="update photoCatalogo as pc set id_exhibicion=$idExhibicion Where pc.idphotoCatalogo='".$idFotos."'";
		
		$result = $bd->ejecutarConsulta($sql);
		
		if($result)
		{
			$j++;
			}
	}

}

if ($j>0){

    echo 1;

} else {

    echo 0;

}