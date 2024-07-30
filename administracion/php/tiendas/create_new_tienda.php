<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 04/06/2018
 * Time: 05:30 PM
 */


session_start();




if (isset($_SESSION["usuario"])){


    include_once "../../connexion/DataBase.php";
    
    echo "<script>";
    echo "console.log($_SESSION);";
    echo "</script>";

    $grupo = $_REQUEST['grupoN'];
    $nEconomico = $_REQUEST['economicoN'];
    $sucursal = $_REQUEST['sucursalN'];
    $direccion = $_REQUEST['direccionN'];
    $colonia = $_REQUEST['coloniaN'];
    $municipio = $_REQUEST['idMunicipioN'];
    $estado = $_REQUEST['idEstadoN'];
    $idRazonSocial = $_REQUEST['razon-social'];
    $canal = $_REQUEST['canalTiendaN'];


    $telefono = $_REQUEST['telefonoN'];

    $x = $_REQUEST['latitudN'];
    $y = $_REQUEST['longitudN'];

    $tipoTienda = $_REQUEST['idTipoTieN'];

    $formato = $_REQUEST['idFormatoN'];

    $cp = $_REQUEST['cpN'];




    $manager = DataBase::getInstance();


    $sql = "insert into maestroTiendas(grupo, numeroEconomico,sucursal, direccion, colonia, id_municipio, 
             x, y, telefono, idFormato, idEstado, idTipoTienda, cp, lat, lon, razonsocial, canal) 
            VALUES ('$grupo','$nEconomico','$sucursal','$direccion', '$colonia', '$municipio', '$x', '$y', '$telefono', '$formato',
            '$estado','$tipoTienda','$cp', '$x', '$y', '$idRazonSocial', '$canal')";


    if ($manager->ejecutarConsulta($sql)){


        echo "registrado :)";
        echo "<script>";
        echo "console.log('registrado');";
        echo "console.log($sucursal);";
        echo "</script>";

        http_response_code(201);

    }
    else{
        echo "<script>";
        echo "console.log('NO registrado');";
        echo "</script>";
    }


}else{


    http_response_code(422);

    echo "acceso denegado";
}