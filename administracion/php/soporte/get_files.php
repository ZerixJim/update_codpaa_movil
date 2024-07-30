<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 4/05/17
 * Time: 10:55 AM
 */


ob_start();
session_start();

if (isset($_SESSION['idUser']) && isset($_GET['idSolicitud'])) {


    include_once('../../connexion/DataBase.php');
    $descripcion = $_REQUEST['descripcion'];
    $propuesta = $_REQUEST['fechaPropuesta'];
    $tipo = $_REQUEST['tipo'];
    $nombre = $_REQUEST['nombre'];
    $comentario = $_REQUEST['comentario'];
    $db = DataBase::getInstance();

    $sql = "SELECT * FROM solicitud_codpaa_files as sf where idSolicitud=" . $_GET['idSolicitud'];

    $response = $db->ejecutarConsulta($sql);

    $content = '<div style="padding: 3%"><ul class="modal-ul">
        <li>
            <h4>Descripcion:</h4>
            <p>' . $descripcion . '</p>
        </li>
       <hr class="separator">
        <li>
            <h4>Fecha Propuesta:</h4>
            <p>' . $propuesta . '</p>
        </li>
         <hr class="separator">
        <li>
            <h4 >Tipo:</h4>
            <p >' . $tipo . '</p>
        </li>
         <hr class="separator">
        <li>
            <h4 >Nombre:</h4>       
            <p >' . $nombre . '</p>
        </li>
         <hr class="separator">
        <li>
            <h4 >Comentario:</h4>
            <p >'.$comentario.'</p>
        </li>
         <hr class="separator">
        <li>
            <h4 style="text-align: center">Archivos</h4>
        </li>
        <li>
        ';

    while ($row = mysqli_fetch_array($response)) {

        $file = basename("../../archivos/" . $row['archivo']);
        $content .= '<a style ="width:100%;margin:.5%;display:inline-block;text-align: center" href="../../archivos/' . $row['archivo'] . '" 
        target="_blank" style="text-decoration:none;" download> ' . $file . ' </a>';
    }
    echo $content .= '</li></ul></div>';

} else {
    echo "accesso denegado";
}