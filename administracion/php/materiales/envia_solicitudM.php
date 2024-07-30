<?

include_once('../../connexion/bdManager.php');


$array_sol = $_POST['data'];


$num_guia = $_POST['num_guia'];



$base = new bdManager();


$inserted = array();

foreach ($array_sol as $solicitud) {

    $sql = "update materiales_solicitud set guia='$num_guia',fecha_envio=now(),estatus='2'
    where id_mat_solicitud='" . $solicitud['idSolicitud'] . "'";


    if($base->ejecutarConsulta($sql)){
        array_push($inserted, $solicitud);
    }

}


if(count($inserted) > 0){
   echo json_encode(array('success'=>true, 'inserted'=>$inserted), JSON_NUMERIC_CHECK | JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_PRETTY_PRINT);
}