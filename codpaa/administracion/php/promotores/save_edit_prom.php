<?

include_once('../../connexion/DataBase.php');
include_once('../../php/seguridad.php');

ob_start();

session_start();

if (isset($_SESSION['idUser'])) {

    $nombre = $_REQUEST['nombre'];
    $super = $_REQUEST['supervisor'];
    $tipo_promotor = $_REQUEST['tipo_promotor'];
    $id_estado = $_REQUEST['id_estado'];
    $usuario = $_REQUEST['usuario'];
    $pass = $_REQUEST['pass'];
    $no_cel = $_REQUEST['no_cel'];
    $imei = $_REQUEST['imei'];
    $email = $_REQUEST['email'];
    $no_nomina = $_REQUEST['no_nomina'];
    $talla_p = $_REQUEST['talla_p'];
    $nombre_emer = $_REQUEST['nombre_emer'];
    $tel_emer = $_REQUEST['tel_emer'];
    $cel_vang = $_REQUEST['cel_vang'];
    $emailVang = $_REQUEST['emailVang'];
    $rfc = $_REQUEST['rfc'];
    $curp = $_REQUEST['curp'];
    $imss = $_REQUEST['imss'];
    $idPromotor = $_REQUEST['idPromotor'];
    $fechaAltaImss = $_REQUEST['fechaAltaImss'];


    $sql = "update Promotores set nombre='$nombre',Supervisor='$super',idtipoPromotor='$tipo_promotor',idEstado='$id_estado',
            usuario='$usuario',password='$pass',numero_celular='$no_cel',imei='$imei',no_nomina='$no_nomina',email='$email',
            talla_playera='$talla_p',nombre_emer='$nombre_emer',tel_emer='$tel_emer',cel_vanguardia='$cel_vang',
            email_vang='$emailVang',rfc='$rfc',curp='$curp',imss='$imss', fechaAltaIMSS='$fechaAltaImss'
            where idCelular='" . $idPromotor . "'";

    $base = DataBase::getInstance();
    $result = $base->ejecutarConsulta($sql);


    if ($result) {
        echo '1';
    } else {
        echo '0';
    }


}else{
    echo "acceso denegado";
}


