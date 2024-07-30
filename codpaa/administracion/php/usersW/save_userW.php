<?php

include_once('../../connexion/DataBase.php');


$perfil = $_REQUEST['Perfil'];
$nombre = $_REQUEST['nombre'];
$usuario = $_REQUEST['usuario'];
$pass = $_REQUEST['password'];
$idSupervisor = $_REQUEST['supervisor'];
$idCliente = $_REQUEST['cliente'];
$idGerente = $_REQUEST['gerente'];
$email = $_REQUEST['email'];


if ($perfil == '1' || $perfil == '2' || $perfil == '10') {
    $permiso = '3';
    $tipo_usuario = 'ALL';
} else if ($perfil == '3' || $perfil == '5' || $perfil == '6' || $perfil == '9' || $perfil == '4' || $perfil == '8') {
    $permiso = '2';
    $tipo_usuario = '';
} else {
    $permiso = '1';
}

$sql = "insert into usuarios (id_perfil,nombre,user,pass,idPermiso,tipo_usuario,idCliente,idSupervisor,email,idGerente)
values('$perfil','$nombre','$usuario',md5('$pass'),'$permiso','$tipo_usuario','$idCliente','$idSupervisor','$email','$idGerente')";
$base = DataBase::getInstance();
$result = $base->ejecutarConsulta($sql);


if ($result) {
    echo '1';
} else {
    echo '0';
}
