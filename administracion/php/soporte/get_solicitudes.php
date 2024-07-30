<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 26/04/2017
 * Time: 05:16 PM
 */

ob_start();
session_start();

if (isset($_SESSION['idUser'])) {


    include_once('../../connexion/DataBase.php');


    $db = DataBase::getInstance();

    $sql = "";


    if ($_SESSION['id_perfil'] == 1) {

        $sql = "SELECT s.idSolicitud, s.titulo, s.descripcion,s.fechaPropuesta, s.createdAt, s.fechaAprobada,
            COUNT(sf.archivo) AS archivos, s.estatus, st.descripcion as tipo, u.nombre, s.comentario
            , UNIX_TIMESTAMP(s.`createdAt`) t_start, UNIX_TIMESTAMP(concat(s.`fechaPropuesta`,' ','18:30:00')) t_end, UNIX_TIMESTAMP(now()) t_now 
              FROM solicitud_codpaa AS s
              LEFT JOIN solicitud_codpaa_files AS sf ON sf.idSolicitud=s.idSolicitud
              LEFT JOIN solicitud_codpaa_tipo as st ON st.idTipo=s.tipo
              LEFT JOIN usuarios as u on u.idUsuario=s.idUsuario 
              where year(s.createdAt) = year(curdate())  or s.estatus not in (2,0) 
            
       GROUP BY s.idSolicitud order by s.createdAt DESC ";

    } else {
        $sql = "SELECT s.idSolicitud, s.titulo, s.descripcion,s.fechaPropuesta, s.createdAt, s.fechaAprobada,
            COUNT(sf.archivo) AS archivos, s.estatus, st.descripcion as tipo, u.nombre, s.comentario,
            UNIX_TIMESTAMP(s.`createdAt`) t_start, UNIX_TIMESTAMP(concat(s.`fechaPropuesta`,' ','18:30:00')) t_end, UNIX_TIMESTAMP(now()) t_now 
      FROM solicitud_codpaa AS s
      LEFT JOIN solicitud_codpaa_files AS sf ON sf.idSolicitud=s.idSolicitud
      LEFT JOIN solicitud_codpaa_tipo as st ON st.idTipo=s.tipo
      LEFT JOIN usuarios as u on u.idUsuario=s.idUsuario
      WHERE s.idUsuario='" . $_SESSION['idUser'] . "' 
      GROUP BY s.idSolicitud  
      order by s.createdAt DESC ";
    }


    $response = $db->ejecutarConsulta($sql);

    $json = array();

    while ($row = mysqli_fetch_object($response)) {
        array_push($json, $row);
    }

    echo json_encode($json, JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_NUMERIC_CHECK);


} else {
    echo "accesso denegado";
}