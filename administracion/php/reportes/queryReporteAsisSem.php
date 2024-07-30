<?php

ob_start();

session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');

    $idSupervisor = $_REQUEST['Supervisor'];

    // $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $SemanaAsis = $_REQUEST['SemanaAsis'];

    $idProm = $_REQUEST['idProm'];

    $id_marca = $_REQUEST['idMarca'];

    $id_tipoT = $_REQUEST['idTipoTie'];


    $filtro = "";
    if ($idSupervisor != "") {
        $filtro = " and p.Supervisor='" . $idSupervisor . "'";
    }
    if ($idProm != "") {
        $filtro = " and p.idCelular='" . $idProm . "'";
    }



    $n_estado = count($id_estado);
    $k = 0;
    if ($n_estado > 0) {
        foreach ($id_estado as $estados) {
            if ($estados != "all") {
                if ($k == 0) {
                    $filtro .= " and (p.idEstado='" . $estados . "' ";
                } else {
                    $filtro .= " or p.idEstado='" . $estados . "'";
                }
                $k++;
            }
        }
        if ($k != 0) {
            $filtro .= ")";
        }
    }


    $n_marc = count($id_marca);
    $k = 0;
    if ($n_marc > 0) {
        foreach ($id_marca as $marcas) {
            if ($k == 0) {
                $filtro .= " and (mp.idMarca='" . $marcas . "' ";
            } else {
                $filtro .= " or mp.idMarca='" . $marcas . "'";
            }
            $k++;
        }
        $filtro .= ")";
    }

    $manager = DataBase::getInstance();


    $sql = "SELECT tv.idTienda, mt.grupo, tf.grupo, tf.cadena, mt.sucursal, p.idCelular idPromotor, p.nombre, DATE(tv.fecha_captura) ,CONCAT(MIN(TIME(tv.fecha_captura)),'-', MAX(TIME(tv.fecha_captura)))entrada  FROM tiendasVisitadas tv

	LEFT JOIN maestroTiendas mt ON (mt.idTienda = tv.idTienda)
	LEFT JOIN municipios m ON (m.id = mt.id_municipio)
	LEFT JOIN estados e ON (e.id = m.estado_id)
	
	LEFT JOIN tiendas_formatos tf ON (tf.idFormato = mt.idFormato)
	LEFT JOIN Promotores p ON (p.idCelular = tv.idCelular)
	
	
	WHERE DATE(tv.fecha_captura) BETWEEN '2021-02-01' AND CURDATE() AND tv.idCelular IN (274, 2991, 3003)
	
	GROUP BY tv.idTienda, DATE(tv.fecha_captura), tv.idCelular
	
	ORDER BY tv.idCelular,tv.fecha_captura ";




} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



