<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 01/03/2017
 * Time: 11:01 AM
 */

ob_start();
session_start();



if(isset($_SESSION['idUser']) && isset($_POST['idPromotor'])){


    include_once('../../connexion/bdManager.php');

    $bd = new bdManager();

    $sql = "select ms.id_mat_solicitud as solicitud , ms.id_material, m.material, ms.cantidad, ms.estatus
    from materiales_solicitud as ms
	left join materiales as m on m.id_material=ms.id_material
    where month(ms.fecha) ='".$_POST['mes']."' and ms.id_promotor='".$_POST['idPromotor']."' order by m.material";


    $request = $bd->ejecutarConsulta($sql);

    $array = array();
    while($row = mysqli_fetch_array($request)){


        $status = "";
        switch ($row['estatus']) {
            case 0:
                $status = "CANCELADO";
                break;
            case 1:
                $status = "ACTIVO";
                break;
            case 2:
                $status = "ENVIADO";
                break;
            case 3:
                $status = "RECIBIDO";
                break;
        }

        array_push($array, array('idMaterial'=> $row['id_material'],
                                    'material'=>$row['material'],
                                    'idSolicitud'=> $row['solicitud'],
                                    'cantidad'=>$row['cantidad'],
                                    'estatus'=> $status
            ));

    }


    echo json_encode($array, JSON_NUMERIC_CHECK | JSON_NUMERIC_CHECK | JSON_PARTIAL_OUTPUT_ON_ERROR);

}