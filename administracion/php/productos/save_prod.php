<?php
session_start();


if (isset($_SESSION["usuario"])){

    include_once('../../connexion/ConexionPDO.php');


    $nombre = $_REQUEST['nombre'];
    $idMarca = $_REQUEST['idMarca'];
    $presentacion = $_REQUEST['presentacion'];
    $codigo = $_REQUEST['codigo'];
    $tipo = $_REQUEST['tipo_prod'];
    $modelo = $_REQUEST['modelo_prod'];


    $base = ConexionPDO::getInstance()->getDB();


    $codigo = trim($codigo);

    //new product exists
    $sql = "select * from Producto p where trim(p.codigoBarras) = :codigo";
    $sentense = $base->prepare($sql);
    $sentense->bindParam(':codigo', $codigo);

    $sentense->execute();
    if($sentense->rowCount() > 0){
        http_response_code(304);

        echo "0";
        exit();
    }



    //create new product

    $sql = "insert into Producto (nombre,idMarca,presentacion,codigoBarras,tipo,modelo) 
            values(:name,:idBrand,:presentation,:barcode,:type,:model)";


    $sentense = $base->prepare($sql);

    $sentense->bindParam(':name', $nombre);
    $sentense->bindParam(':idBrand', $idMarca, PDO::PARAM_INT);
    $sentense->bindParam(':presentation', $presentacion);
    $sentense->bindParam(':barcode', $codigo);
    $sentense->bindParam(':type', $tipo);
    $sentense->bindParam(':model', $modelo);

    $result = $sentense->execute();

    if ($result){

        echo '1';
    } else {

        echo '0';
    }

}else{

    echo "Access Denied";
    http_response_code(421);

}
