<?php

session_start();
include '../connexion/DataBase.php';

$nombre=$_POST['nombre'];
$cliente=$_POST['cliente'];
$cliente=(int)$cliente;

if(isset($nombre) && isset($cliente) && isset($_SESSION['idUser'])){

    //include_once('../connexion/DataBase.php');
    $dataBase = DataBase::getInstance();

    $sql = "INSERT into tipoExhibicion(nombre)
        VALUES ('$nombre')";

    $request = $dataBase->ejecutarConsulta($sql);


    $sql = "SELECT * FROM tipoExhibicion
            WHERE nombre='$nombre'";

    $res = $dataBase->ejecutarConsulta($sql);
    
    $row = $res->fetch_array(MYSQLI_NUM);
    $idEx=$row[0];
    $idEx=(int)$idEx;
    
    $sql = "INSERT into cliente_tipo_exhibicion(id_cliente, idExhibicion)
    VALUES ($cliente, $idEx)";

    $a = $dataBase->ejecutarConsulta($sql);
    
    if($request && $res && $a){
        echo json_encode(array('success'=> true, 'nombre'=>$_REQUEST['nombre']));
    }
}else{

    echo "datos no recibidos";

}

?>


