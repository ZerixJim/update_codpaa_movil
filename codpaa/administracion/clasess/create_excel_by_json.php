<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 08/09/2017
 * Time: 01:54 PM
 */

set_include_path(get_include_path() . PATH_SEPARATOR . './PHPExcel/Classes/');

/** PHPExcel */
include 'PHPExcel.php';

/** PHPExcel_Writer_Excel2007 */
include 'PHPExcel/Writer/Excel2007.php';

$objPhpExcel = new PHPExcel();

//$array = array("test"=> "hola", "test2"=> "tesdtsfkl jslkaf laj");

$array = json_decode($_POST["json"], true);



//properties
$objPhpExcel->getProperties()->setCreated("Codpaa");
$objPhpExcel->getProperties()->setLastModifiedBy("Codpaa");
$objPhpExcel->getProperties()->setTitle("Modulo");


if (count($array) > 0){

    //inicializamos la columna en A
    $column = 'A';
    $row = 2;


    //Headers of json
    $headers = array_keys($array[0]);

    //var_dump($array);

    foreach ($headers as $head){

        $objPhpExcel->getActiveSheet()->setCellValue($column . 1, $head);

        $column++;

    }


    $column = 'A';

    foreach ($array as $value){

        $headers = array_keys($value);

        foreach ($headers as $head ){

            $objPhpExcel->getActiveSheet()->setCellValue($column . $row, $value[$head]);

            $column++;

        }


        $row++;
        $column = 'A';

    }


    $objWrite = new PHPExcel_Writer_Excel2007($objPhpExcel);
    $file = "file". rand(1,999) . ".xlsx";

    $objWrite->save("../archivos/". $file);



    if (file_exists("../archivos/".$file)){

        echo "../archivos/". $file;
        http_response_code(200);
    }else{
        echo "archivo no encotrado";
        http_response_code(404);
    }


}else{

    echo "parametros faltantes ";
    http_response_code(422);

}


