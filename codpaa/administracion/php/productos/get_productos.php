<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 20/07/2017
 * Time: 12:31 PM
 */

ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_POST['idMarca'])) {

    include_once('../../connexion/DataBase.php');

    $idMarca = $_POST['idMarca'];


    $manager = DataBase::getInstance();


    $k = 0;

    $marcas = "";
    $n_marc = count($idMarca);

    foreach ($idMarca as $marca) {

        if (is_numeric($marca)){
            $marcas.= $marca;

            if ($k != $n_marc - 1){

                $marcas .= ",";
            }
        }

        $k++;
    }


    $sql = "select p.idProducto, concat(p.nombre, ' ', p.presentacion ) as nombre from Producto as p 
                where   
                p.estatus = 1 and p.idMarca in (". $marcas .")  
                order by p.idMarca, p.nombre ";




    $rs = $manager->ejecutarConsulta($sql);
    $result = array();


    while ($row = mysqli_fetch_object($rs)) {

        $row->nombre = htmlentities($row->nombre);
        array_push($result, $row);
    }

    echo json_encode($result, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT);
} else {
    echo 'acceso denegado';

}