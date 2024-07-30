<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 09/05/2017
 * Time: 01:25 PM
 */

ob_start();
session_start();


use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;




if (isset($_SESSION['idUser'])) {

    require '../../complemento/phpmailer/src/PHPMailer.php';
    require '../../complemento/phpmailer/src/Exception.php';
    require '../../complemento/phpmailer/src/SMTP.php';

    include_once('../../connexion/DataBase.php');


    $idSolicitud = $_REQUEST['idSolicitud'];
    $titulo = $_REQUEST['titulo'];
    $descripcion = $_REQUEST['descripcion'];
    $estatus = $_REQUEST['estatus'];
    $comentario = $_REQUEST['comentario'];
    $fechaAprobada = $_REQUEST['fechaAprobada'];


    $db = DataBase::getInstance();
    $sql = "update solicitud_codpaa set titulo='" . $titulo . "',
            descripcion='" . $descripcion . "', estatus='". $estatus ."', comentario='" . $comentario . "',
            fechaAprobada='" .  $fechaAprobada . "'
            where idSolicitud=" . $idSolicitud;

    if($db->ejecutarConsulta($sql)){


        $sql = "SELECT sc.titulo, sc.comentario, sc.estatus, u.email, sc.fechaAprobada fecha  
                FROM solicitud_codpaa sc
                
                LEFT JOIN usuarios u ON (u.`idUsuario` = sc.`idUsuario`)
                
                
                WHERE sc.idSolicitud = $idSolicitud";


        $row = $db->ejecutarConsulta($sql);


        $row = $row->fetch_array();

        /*
         *
         * sending mail
         *
         */


        if ($estatus ==  0 || $estatus == 2){


            if($estatus == 0) $estatus = 'Cancelado';
            else if($estatus == 2) $estatus = 'Terminado';




            $mensaje = "<html lang='es'>
                            <head>
                              <title>Soporte Codpaa Web</title>
                            </head>
                            <body style='background-color: #f5f5f5; text-align: center;'>
                            
                                <div style='width: 70%;background-color: #ffffff;margin: 10px auto;border-radius: 10px;overflow: hidden;'> 
                                    
                                    <div style='background-color: #3c3c3c; color: #f3f3f3;text-align: center;padding: 5px; box-sizing: border-box;'>
                                        <h1 style='margin: 0;'>CODPAA</h1>
                                    </div>
                                
                                    <div style='padding: 15px;box-sizing: border-box;'>
                                      <p><strong>Buen Día:</strong></p>
                                      <p>Este correo fue enviado de manera automática mediante la plataforma Codpaa Web, favor de no responder a este correo</p><br>
                                      <p>El Estatus de tu solicitud  con ID[#$idSolicitud] $titulo ,  a cambiado a <strong> $estatus </strong> </p><br>
                                      <p>Fecha <span> $fechaAprobada </span></p>
                                      
                                      <p>Comentario:  $comentario </p>
                                    </div>
                                    
                             
                                </div>
                             
                              
                              <footer>
                                  <span style='color: #bababa;margin: 0 auto;'>power by Codpaa &copy; platform</span>
                              </footer>
                              
                            </body>
                       </html>";



            $mail = new PHPMailer();


            try {
                //Server settings
                //$mail->SMTPDebug = SMTP::DEBUG_SERVER;// Enable verbose debug output

                $mail->isSMTP();                                            // Send using SMTP
                $mail->Host       = 'box.mailsrv1.site';                    // Set the SMTP server to send through
                $mail->SMTPAuth   = true;                                   // Enable SMTP authentication

                $mail->Username   = 'no-reply@plataformavanguardia.com';                     // SMTP username
                $mail->Password   = 'YqS8BNx4dNMw';                               // SMTP password
                $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;         // Enable TLS encryption; `PHPMailer::ENCRYPTION_SMTPS` also accepted


                $mail->Port       = 587;                                    // TCP port to connect to

                //Recipients
                $mail->setFrom('no-reply@plataformavanguardia.com', 'Codpaa Web');
                $mail->addAddress($row['email']);     // Add a recipient


                if (!empty($_FILES['file'])){

                    $count = count($_FILES['file']['name']);


                    for ($i = 0; $i < $count; $i++) {

                        $mail->addAttachment($_FILES['file']['tmp_name'][$i], $_FILES['file']['name'][$i]);

                    }


                }



                // Content
                $mail->isHTML(true);                                  // Set email format to HTML
                $mail->Subject = $titulo;
                $mail->msgHTML($mensaje);
                $mail->AltBody = "Correo enviado desde Codpaa Web(no contestar),  [$idSolicitud] $titulo,  el estatus cambio a $estatus  , en $fechaAprobada   $comentario";
                $mail->CharSet = 'UTF-8';

                $send = $mail->send();

                echo json_encode(array("status" => true, "id_status" => $estatus, "errorInfo" => $mail->ErrorInfo, "send"=>$send), JSON_PRETTY_PRINT);

            } catch (Exception $e) {

                echo json_encode(array("status"=>false, "error"=>$e));
            }


        }




    }else{

        echo json_encode(array("status" => false), JSON_PRETTY_PRINT);

    }


} else {
    echo "acceso denegado";
}