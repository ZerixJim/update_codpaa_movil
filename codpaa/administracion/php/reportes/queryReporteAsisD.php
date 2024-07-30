<?php

/**
 * Created by DreamW.
 * User: Christian
 * Date: 11/12/14
 * Time: 13:59
 */


ob_start();

session_start();





if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');


    $idSupervisor = $_REQUEST['Supervisor'];

    $id_estado = $_REQUEST['idEstado'];

    $fechaDia = $_REQUEST['fechaDia'];

    $date = date("Y-m-d", strtotime($fechaDia));

    $id_marca = $_REQUEST['idMarca'];

    $horas = $_REQUEST['horas'];

    $idProm = $_REQUEST['idProm'];

    $fotos_cap = $_REQUEST['FotosCp'];

    $filtro = "";
    if ($idSupervisor != "") {
        $filtro = " and p.Supervisor='" . $idSupervisor . "'";
    }

    if (!empty($idProm)) {

        $countProm = count($idProm);

        $k = 0;
        $filtro = " and p.idCelular in (";

        foreach ($idProm as $idP){

            if (is_numeric($idP)){

                $filtro.= $idP;

                if ($k != $countProm - 1){
                    $filtro.= ",";

                }

            }
            $k++;
        }

        $filtro.= ")";

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
    $filtro_ph = "";
    if ($n_marc > 0) {
        foreach ($id_marca as $marcas) {
            if ($k == 0) {
                $filtro_ph .= " and (id_marca='" . $marcas . "' ";
                $filtro .= " and (mp.idMarca='" . $marcas . "' ";
            } else {
                $filtro_ph .= " or id_marca='" . $marcas . "'";
                $filtro .= " or mp.idMarca='" . $marcas . "'";
            }
            $k++;
        }
        $filtro_ph .= ")";
        $filtro .= ")";
    }

    $manager = DataBase::getInstance();

    //***************************************** Seleccionar Promotores*************************************************//

    $sql = "SELECT DISTINCT tv.idCelular AS idPromotor, tv.`idTienda` ,mt.sucursal, e.abrev estado,DATE(fecha_captura) fecha,
	MIN(TIME(tv.fecha_captura)) entrada,MAX(TIME(tv.fecha_captura)) salida,p.nombre,
	TIMEDIFF(MAX(TIME(tv.fecha_captura)), MIN(TIME(tv.fecha_captura))) tiempoTienda, fecha_captura, TIME(fecha_captura) tiempo
	
	,GROUP_CONCAT((ACOS(SIN(RADIANS(mt.x)) * SIN(RADIANS(tv.`latitud`)) + 
	COS(RADIANS(mt.x)) * COS(RADIANS(tv.`latitud`)) * 
	COS(RADIANS(mt.y) - RADIANS(tv.`longitud`))) * 6378))  distancia
	 
	FROM tiendasVisitadas tv
	
	LEFT JOIN maestroTiendas mt ON mt.idTienda=tv.idTienda 
	LEFT JOIN estados e ON mt.idEstado=e.id
	
	RIGHT JOIN marcaAsignadaPromotor mp on mp.idPromotor = tv.idCelular 
	
	LEFT JOIN Promotores p ON p.`idCelular`= tv.`idCelular`
	
	WHERE DATE(tv.fecha_captura)= '". $date . "' 
	
	" . $filtro .  "
  
	GROUP BY tv.idCelular, DATE(tv.fecha_captura), tv.idTienda, tv.tipo
	ORDER BY tv.idCelular ASC,tv.fecha_captura ASC";


    $response = $manager->ejecutarConsulta($sql);

    $json = array();
    ///// **********************************Ciclo promotores
    while ($visitas = mysqli_fetch_object($response)) {


        array_push($json, $visitas);

    }


    echo json_encode($json, JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT );



} else {

    echo 'no has iniciado sesion';
    http_response_code(422);

    header('refresh:2,../index.php');

}