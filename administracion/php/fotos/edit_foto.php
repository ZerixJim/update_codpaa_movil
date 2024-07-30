<?php
session_start();
include('../../connexion/DataBase.php');

$bd = DataBase::getInstance();

$idUsuario = $_SESSION['idUser'];
$idFoto = $_REQUEST['idphotoCatalogo'];

$fotos = explode(',', $idFoto);

$idMarca = $_REQUEST['MarcaFotE'];

$n_fotos = count($fotos);

if ($n_fotos > 0) {
    $j = 0;
    foreach ($fotos as $idFotos) {
        $sql_mar = "SELECT id_marca FROM photoCatalogo WHERE idphotoCatalogo='" . $idFotos . "'";
        $rs_mar = $bd->ejecutarConsulta($sql_mar);
        $dat_mar = mysqli_fetch_array($rs_mar);

        $sql_edit = "INSERT INTO photoEdit (id_photoEdit,idMarca,idUsuario,idMarca_ant,fecha,idphotoCatalogo) 
		VALUES ('','" . $idMarca . "','" . $idUsuario . "','" . $dat_mar['id_marca'] . "',now(),'" . $idFotos . "')";

        $bd->ejecutarConsulta($sql_edit);

        $sql = "update photoCatalogo as pc set id_marca=$idMarca Where pc.idphotoCatalogo='" . $idFotos . "'";

        $result = $bd->ejecutarConsulta($sql);

        if ($result) {
            $j++;
        }
    }

}

if ($j > 0) {

    echo 1;

} else {

    echo 0;

}