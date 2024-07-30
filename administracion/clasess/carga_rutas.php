<?php

set_time_limit(0);

//date_default_timezone_set('Europe/London');


/**  Set Include path to point at the PHPExcel Classes folder  **/
set_include_path(get_include_path() . PATH_SEPARATOR . './PHPExcel/Classes/');

/**  Include PHPExcel_IOFactory  **/
include 'PHPExcel/Classes/PHPExcel/IOFactory.php';

include_once('../connexion/DataBase.php');

$manager = DataBase::getInstance();


//$inputFileType = 'Excel5';
$inputFileType = 'Excel2007';
//	$inputFileType = 'Excel2003XML';
//	$inputFileType = 'OOCalc';
//	$inputFileType = 'Gnumeric';
$inputFileName = './PHPExcel/ruta_prueba.xls';

/**  Create a new Reader of the type defined in $inputFileType  **/
$objReader = PHPExcel_IOFactory::createReader($inputFileType);


if ($_FILES['file_rutas']["error"] > 0) {

    $_FILES['file_rutas']['error'];

} else {
    $inputFileName = $_FILES['file_rutas']['tmp_name'];

    //move_uploaded_file($_FILES['file_rutas']['tmp_name'],"PHPExcel/" . $_FILES['file_rutas']['name']);
}

//todo compatibilidad

$objPHPExcel = $objReader->load($inputFileName);

//	Do some processing here

$sheetData = $objPHPExcel->getActiveSheet()->toArray(null, true, true, true);

$arreglo_rutas = array();


foreach ($sheetData as $datos_excel) {

    if($datos_excel['A'] != null && is_numeric($datos_excel['A'])  && $datos_excel['B'] != null){
        $query_prom = "Select * from Promotores where idCelular='" . $datos_excel['A'] . "'";
        $res_prom = $manager->ejecutarConsulta($query_prom);
        $dat_prom = mysqli_fetch_array($res_prom);

        $query_tien = "Select * from maestroTiendas where idTienda=" . $datos_excel['B'];
        $res_tien = $manager->ejecutarConsulta($query_tien);

        if ($res_tien){

            $dat_tien = mysqli_fetch_array($res_tien);
            $datos_excel['tienda'] = $dat_tien['grupo'] . " " . $dat_tien['sucursal'];
        }

        $datos_excel['idTipo'] = $datos_excel['K'];

        array_push($arreglo_rutas, $datos_excel);
    }

}

echo '<script>';
echo 'console.log('.$arreglo_rutas.');';
echo '</script>';

echo json_encode($arreglo_rutas, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);



