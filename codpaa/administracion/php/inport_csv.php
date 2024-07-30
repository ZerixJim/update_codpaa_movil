<?php


$csvfile = $_FILES['csv']['name'];
$csvtmp = $_FILES['csv']['tmp_name'];



if (!file_exists($csvfile)) {
    echo json_encode(array('msg'=>'El archivo no existe'));  
}else{
	echo json_encode(array('success'=>true));
}



$size = filesize($csvfile);

if (!$size) {
        echo json_encode(array('msg'=>'Archivo vacio'));
        
}







?>