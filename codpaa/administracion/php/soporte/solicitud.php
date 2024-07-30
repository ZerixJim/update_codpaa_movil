<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 24/04/2017
 * Time: 11:02 AM
 */
ob_start();
session_start();







if(isset($_POST['title']) && isset($_POST['description']) &&  isset($_POST['fecha'] ) && isset($_SESSION['idUser'])){

    include_once('../../connexion/DataBase.php');

    function generateRandomString($length = 5) {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }


    $dataBase = DataBase::getInstance();


    $sql = "SELECT IF ( COUNT(*) >= st.solicitudMax, false, true) AS permitido FROM solicitud_codpaa AS sc
	        LEFT JOIN solicitud_codpaa_tipo AS st ON st.idTipo=sc.tipo

            WHERE tipo = '" . $_POST['tipo'] . "' AND idUsuario='" . $_SESSION['idUser'] .  "' AND MONTH(NOW())= MONTH(createdAt) AND YEAR(NOW()) = YEAR(createdAt)
            GROUP BY idUsuario, MONTH(createdAt), YEAR(createdAt);";

    $request = $dataBase->ejecutarConsulta($sql);

    $past = mysqli_fetch_array($request);

    $count = mysqli_num_rows($request);


    if($past['permitido'] == true || $count == 0){


        $sql = "Insert into solicitud_codpaa(idUsuario, titulo, descripcion, fechaPropuesta, tipo)
            VALUES ('" . $_SESSION['idUser'] . "','". $_POST['title'] ."', '" . $_POST['description'] . "', '" . $_POST['fecha'] .  "', '". $_POST['tipo'] ."')";


        $id = $dataBase->getIdRow($sql);
        if($id > 0){



            $count =  count($_FILES['file']['name']);





            if($count > 0){



                $targetDir = "../../archivos/". $_SESSION['idUser'] . "/";

                if (!is_dir($targetDir)){

                    mkdir($targetDir, 0755);


                }

                $insert = array();
                for ($i = 0; $i < $count; $i++) {


                    $nameFile =  basename($_FILES['file']['name'][$i]);

                    $nameFileSplit = explode('.', $nameFile);

                    $name = $nameFileSplit[0] . generateRandomString();

                    $type =  $nameFileSplit[count($nameFileSplit ) - 1];

                    $nameFile = $name . "." . $type;


                    $dir = $_SESSION['idUser'] . "/" .$nameFile;


                    $targetFile = $targetDir . $nameFile ;


                    if(! file_exists($targetFile)){


                        if(move_uploaded_file($_FILES['file']['tmp_name'][$i], $targetFile)){

                            $sql = "insert into solicitud_codpaa_files(idSolicitud, archivo) VALUES ('". $id ."', '" . $dir . "')";

                            if($dataBase->ejecutarConsulta($sql)){

                                array_push($insert, array('name'=>$_FILES['file']['name'][$i]));

                            }
                        }


                    }
                }


                if (count($insert) > 0){
                    echo json_encode(array('success'=> true, 'files'=>$insert, 'title'=>$_REQUEST['title']));
                    exit();
                }




            }


            echo json_encode(array('success'=> true, 'title'=>$_REQUEST['title']));
        }



    }else{
        echo "superaste el maximo de solicitudes por mes";
    }



}else{

    echo "datos no recibidos";

}




