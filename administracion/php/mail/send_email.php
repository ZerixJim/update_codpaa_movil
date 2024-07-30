<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 16/10/2017
 * Time: 10:56 AM
 */

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;




if (isset($_REQUEST["to"])) {

    require '../../complemento/phpmailer/src/PHPMailer.php';
    require '../../complemento/phpmailer/src/Exception.php';
    require '../../complemento/phpmailer/src/SMTP.php';

    $para = $_REQUEST["to"];
    $titulo = $_REQUEST['title'];
    $estatus = $_REQUEST["status"];
    $fecha = $_REQUEST["fecha"];
    $comentario = $_REQUEST["comentario"];

    $mensaje = "<html lang='es'>
			<head>
			  <title>Soporte Codpaa Web</title>
			</head>
			<body style='background-color: #f5f5f5; text-align: center;'>
			
			    <div style='width: 60%;background-color: #ffffff;margin: 10px auto;'> 
			        
			        <div style='background-color: #3c3c3c; color: #f3f3f3;text-align: center;padding: 5px; box-sizing: border-box;'>
			            <h1>CODPAA <img src='https://test.plataformavanguardia.net/imagenes/favicon.png' alt='logo'> </h1>
                    </div>
			    
			        <div style='padding: 15px;box-sizing: border-box;'>
			            <p>Buen Día:</p>
                        <p>Este correo fue enviado de manera automatica mediante la plataforma Codpaa Web, favor de no responder a este correo</p><br>
                        <p>El Estatus de tu solicitud a cambiado a <span> $estatus </span> </p><br>
                        <p>Fecha <span> $fecha </span></p>
                  
                        <p>Comentario:  $comentario </p>
                    </div>
			        
			 
                </div>
			 
			  
			  <footer>
			      <span style='color: #bababa;margin: 0 auto;'>power by Codpaa &copy; plataform </span>
              </footer>
			  
			</body>
			</html>";



    $mail = new PHPMailer();


    try{

        $mail->isSMTP();                                            // Send using SMTP
        $mail->Host       = 'box.mailsrv1.site';                    // Set the SMTP server to send through
        $mail->SMTPAuth   = true;                                   // Enable SMTP authentication

        $mail->Username   = 'no-reply@plataformavanguardia.com';                     // SMTP username
        $mail->Password   = 'YqS8BNx4dNMw';                               // SMTP password
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;         // Enable TLS encryption; `PHPMailer::ENCRYPTION_SMTPS` also accepted


        $mail->Port       = 587;                                    // TCP port to connect to
        //$Password_mail;
        $mail->IsHTML (true);
        $mail->AddAddress($para);
        $mail->From = 'no-reply@plataformavanguardia.com';
        $mail->FromName = 'Codpaa Web';

        $mail->AddReplyTo('no-reply@plataformavanguardia.com', 'Codpaa Web');
        $mail->Subject = $titulo;
        //$mail->Body = $body;
        $mail->MsgHTML($mensaje);
        $mail->AltBody = "";
        $mail->Timeout = 10;
        $mail->CharSet = 'UTF-8';



        echo json_encode(array("status" => $mail->Send(), "title" => $titulo), JSON_PRETTY_PRINT);

    }catch (Exception $e){

        echo json_encode(array("status"=>0, "error"=>$e));

    }



} else {

    http_response_code(422);
    echo "parametros faltantes";


}

