<?

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

require '../complemento/phpmailer/src/PHPMailer.php';
require '../complemento/phpmailer/src/Exception.php';
require '../complemento/phpmailer/src/SMTP.php';


include_once('../connexion/DataBase.php');

$base = DataBase::getInstance();

$usuario_pass = $_REQUEST['usuario_pass'];


$sql = "select email,idUsuario from usuarios where user='$usuario_pass'";

$result = $base->ejecutarConsulta($sql);


if ($result) {

    $datos = mysqli_fetch_array($result);

    if ($datos['email'] != "") {
        $abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        $pass = "";
        for ($i = 0; $i < 10; $i++) {
            $pass .= substr($abc, rand(0, 62), 1);
        }

        $sql_change = "UPDATE usuarios SET pass=MD5('" . $pass . "') WHERE idUsuario='" . $datos['idUsuario'] . "'";

        $rs_change = $base->ejecutarConsulta($sql_change);

        if ($rs_change) {
            $para = $datos['email'];
            $titulo = 'Recuperacion de Contraseña';
            $mensaje = '
			<html>
			<head>
			  <title>CodPaa Web</title>
			</head>
			<body>
			  <p>Buen Dia:</p>
			  <p>Este correo se envió  de manera automatica mediante la plataforma Codpaa Web<br>
			  enseguida se muestra tu nueva contraseña para ingresar al sistema. <br>
			  Password: ' . $pass . '<br>
			  Esperemos no tengas ningun problema para poder iniciar sesión , en dado que esto ocurra <br>
			  favor de comunicarte con tu Vanguardia al area de Sistemas.  <br>
			  Saludos
			  </p>
			</body>
			</html>
			';


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
                $mail->addAddress($para);     // Add a recipient
                //$mail->addAddress('gustavoibarra1989@gmail.com');

                // Content
                $mail->isHTML(true);                                  // Set email format to HTML
                $mail->Subject = 'pass recover';
                $mail->Body    = $mensaje;
                $mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

                if ($mail->send())
                    echo '1';
                else
                    echo '0';
            } catch (Exception $e) {
                echo "0";
            }

        } else {
            echo '0';
        }
    } else {
        echo '2';
    }
} else {
    echo '0';
}
