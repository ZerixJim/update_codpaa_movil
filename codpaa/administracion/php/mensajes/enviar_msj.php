<?


if (isset($_REQUEST['tipo'])) {


    include_once('../../connexion/DataBase.php');
    include_once('../../complemento/gcm.php');

    $tipo = $_REQUEST['tipo'];

    $gcm = new GCM();

    if ($tipo == 'mensaje') {

        $idMensaje = $_REQUEST['idMensaje'];

        $manager = DataBase::getInstance();

        $sql_msj = "SELECT ms.*,m.nombre AS marca, ms.id_tipo_promotor FROM mensajes ms
        INNER JOIN Marca m ON (ms.id_marca=m.idMarca)
        WHERE id_mensaje='" . $idMensaje . "'";

        $rs_msj = $manager->ejecutarConsulta($sql_msj);

        $dat_msj = mysqli_fetch_array($rs_msj);

        $sql_ids = "SELECT token_gcm,idPromotor FROM Promotores p
          INNER JOIN marcaAsignadaPromotor mp ON (p.idCelular=mp.idPromotor)
          WHERE mp.idMarca='" . $dat_msj['id_marca'] . "' AND token_gcm!=''  AND p.idtipoPromotor = " . $dat_msj['id_tipo_promotor'];



        $rs_ids = $manager->ejecutarConsulta($sql_ids);

        $registration_ids = array();

        // preparing gcm registration ids array
        while ($dat_ids = mysqli_fetch_array($rs_ids)) {
            array_push($registration_ids, $dat_ids['token_gcm']);
        }



        $data = array();

        $data['tipo'] = $tipo;


        $data['asunto'] = $dat_msj['asunto'];

        $data['message'] = $dat_msj['titulo'];

        $data['content'] = $dat_msj['mensaje'];

        $data['id_mensaje'] = $dat_msj['id_mensaje'];

        $data['fecha'] = date('Y-m-d H:i:s');

        //$topic=trim(str_replace(' ','',$dat_msj['marca']));;

        //$result_send=$gcm->sendToTopic($topic,$data);


        $result_send = $gcm->sendMultiple($registration_ids, $data);


        if ($result_send) {
            /*echo $result_send;*/
            $sql = "UPDATE mensajes SET estatus='2'
	                WHERE id_mensaje='" . $dat_msj['id_mensaje'] . "'";
            $base = DataBase::getInstance();
            $result = $base->ejecutarConsulta($sql);

            echo '1';
        } else {
            echo '0';
        }

    } else if ($tipo == 'baja') {

        

    } else if ($tipo == 'alta') {

    } else if ($tipo == 'rastreo') {


    }


}


