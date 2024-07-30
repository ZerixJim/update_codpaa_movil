<?php

/**
 * Created by Dreamweaver.
 * User: Christian
 * Date: 16/02/16
 * Time: 12:00
 */


ob_start();

session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    include_once('../connexion/DataBase.php');

    $manager = DataBase::getInstance();

    $fecha = date('Y-m-d');

    $result = array();


    for($w = 0 ; $w <= 3 ; $w++ ){


        $semana =  date('W', strtotime($fecha . ' -' . ( $w )
            . ' weeks monday this week'));
        $anio = date('Y', strtotime($fecha . ' -' . ( $w )
            . ' weeks monday this week'));
        $from = date('d', strtotime($fecha . ' -' . ( $w )
            . ' weeks monday this week'));

        $to = date('d', strtotime($fecha . ' -' . ( $w )
            . ' weeks sunday this week'));

        $month = date('m', strtotime($fecha . ' -' . ( $w )
            . ' weeks sunday this week'));


        $label = "$semana - $anio";

        $mes = $manager->getMonthByNumberSpanish($month);

        $text = "Semana $semana del $from al $to de $mes del $anio";


        array_push($result,
            array("label"=> $label,
                "anio"=> $anio,
                "semana" => $semana,
                "text" => $text
            ));
    }


    echo json_encode($result, JSON_PRETTY_PRINT);

} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

