<?php

/**
 * Created by Dreamweaver.
 * User: Christian
 * Date: 16/02/16
 * Time: 12:00
 */


ob_start();

session_start();

include_once('../connexion/DataBase.php');


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    $idMarca = $_REQUEST['idMarca'];
    $id_marcas = explode(',', $idMarca);
    $n_marc = count($id_marcas);
    $k = 0;
    $filtro = "";
    if ($n_marc > 0 && isset($_REQUEST['idMarca'])) {
        foreach ($id_marcas as $marcas) {
            if ($k == 0) {
                $filtro .= "and (cod.idMarca='" . $marcas . "'";
            } else {
                $filtro .= " or cod.idMarca='" . $marcas . "'";
            }
            $k++;
        }
        $filtro .= ")";
    }
    $manager = DataBase::getInstance();

    $sql = "SELECT concat(mes,'-',anio) as mesMay FROM `cod_tienda_marca_promotor` cod
            inner JOIN maestroTiendas mt on (cod.idTienda=mt.idTienda)
            where mt.idTipoTienda=2 and anio>=2016 ". $filtro ." GROUP BY cod.anio,cod.mes";


    $rs = $manager->ejecutarConsulta($sql);

    $result = array();

    while ($row = mysqli_fetch_object($rs)) {

        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR);


} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}

