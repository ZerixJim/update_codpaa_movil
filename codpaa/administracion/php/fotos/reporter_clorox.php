<?php

/*ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);*/

require_once 'Fotos.php';
require_once '../../clasess/pptCreatorClorox.php';
require_once '../../connexion/ConexionPDO.php';
require_once '../../clasess/Sample_Header3.php';

require_once '../../complemento/phpmailer/src/PHPMailer.php';


use fotos\Fotos;
use classes\PptCreator;
use PHPMailer\PHPMailer\PHPMailer;


$grupo = $_REQUEST['grupo'];
$marca = 249;


$date = date('Y-m-d');

$dateNumber = date('w', strtotime($date));


if (isset($_REQUEST['date'])) {

    $date = $_REQUEST['date'];

} else {

    if ($dateNumber == 1) {

        $date = date('Y-m-d', strtotime(date($date) . ' -2 day'));

    } else {

        $date = date('Y-m-d', strtotime(date($date) . ' -1 day'));
    }


}


$datos = Fotos::queryFotos($marca, $grupo, $date);


//echo json_encode($datos);

$arra = PptCreator::createPpt($date, $marca, $datos, $grupo);


if ($arra['success']) {


    require_once '../../complemento/phpmailer/src/Exception.php';
    require_once '../../complemento/phpmailer/src/SMTP.php';


    $para = Fotos::getMails($grupo);
    $titulo = 'Reporte Clorox';

    $fecha = $date;

    $ruta = $arra['ruta'];


    $mensaje = "<html lang='es'>

                    <body style='background-color: #f3f3f3;font-family: Arial, Courier, monospace;font-size: 13px;'>
                    
                        <div style = 'background-color: rgb(252, 252, 252);width: 70%;margin: 0 auto;' >
                            <div
                                style = 'background: rgb(54, 54, 54);text-align: center;padding: 5px;box-sizing: border-box;color: rgb(236, 236, 236);' >
                                <h2 > Vanguardia </h2 >
                            </div >
                    
                            <div style = 'padding: 25px;box-sizing: border-box;' >
                                <strong > Buen Día </strong >
                                <p style = 'font-size: 1em;' > Este correo fue generado de manera automatica mediante la plataforma Codpaa Web,
                                    favor de no responder a este
                                    correo .</p ><br >
                                <p > se ha generado el reporte </p >
                                <p ><strong ><a href = 'https://$ruta'
                                            style = 'background-color: rgb(119, 163, 243);text-decoration: none;padding: 7px;box-sizing: border-box;color: rgb(247, 247, 247);' > Descargar
                                            Aquí </a ></strong ></p > ------
                                <p style = 'font-size: 0.9em;color: #7a7a7a;' > el link de descarga solo estara disponible en el siguiente
                                    periodo de 48hrs .</p >
                                <p style = 'font-size: 0.9em;color: rgb(184, 184, 184);' > https://$ruta</p>
                                <p style = 'font-size: 0.7em;' > Fecha <span > $fecha </span ></p >
                    
                            </div >
                    
                        </div >
                    
                        <footer style = 'text-align: center;color: rgb(179, 179, 179);' >
                    
                            <p > Power by Codpaa web &copy; </p >
                    
                        </footer >
                    
                    
                    </body >
                    
                    </html > ";



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

        foreach ($para as $item) {

            $mail->AddAddress($item);
        }

        $mail->From = 'no-reply@plataformavanguardia.com';
        $mail->FromName = 'Codpaa Web Reporter ';

        $mail->AddReplyTo('no-reply@plataformavanguardia.com', 'Codpaa Web');
        $mail->Subject = $titulo . 'Grupo ' . $grupo;
        //$mail->Body = $body;
        $mail->MsgHTML($mensaje);
        $mail->AltBody = "";
        $mail->Timeout = 10;
        $mail->CharSet = 'UTF-8';


        echo json_encode(array("status" => $mail->Send(), "title" => $titulo, "error"=> $mail->ErrorInfo), JSON_PRETTY_PRINT);

    }catch (Exception $e){

        echo json_encode(array("status"=>0, "error"=>$e));

    }


}






