<?


include_once('../../php/seguridad.php');


ob_start();
session_start();

if(isset($_SESSION['idUser'])){

	include_once('../../connexion/DataBase.php');

	$nombre = $_REQUEST['nombre'];
	$super = $_REQUEST['supervisor'];
	$tipo_promotor = $_REQUEST['tipo_promotor'];
	$id_estado = $_REQUEST['id_estado'];
	$usuario = $_REQUEST['usuario'];
	$pass = $_REQUEST['pass'];
	$no_cel = $_REQUEST['no_cel'];
	$imei = $_REQUEST['imei'];
	$email = $_REQUEST['email'];
	$no_nomina=$_REQUEST['no_nomina'];
	$talla_p=$_REQUEST['talla_p'];
	$nombre_emer=$_REQUEST['nombre_emer'];
	$tel_emer=$_REQUEST['tel_emer'];
	$cel_vang=$_REQUEST['cel_vang'];
	$emailVang=$_REQUEST['emailVang'];
	$rfc=$_REQUEST['rfc'];
	$curp=$_REQUEST['curp'];
	$imss=$_REQUEST['imss'];
	$fechaImss = $_REQUEST['fechaAltaImss'];


	$sql = "insert into Promotores (nombre,Supervisor,idtipoPromotor,idEstado,usuario,password,numero_celular,imei,fechaAlta,
					ultimaModificacion,no_nomina,email,talla_playera,nombre_emer,tel_emer,cel_vanguardia,email_vang,rfc,curp,imss, fechaAltaIMSS)
					values('$nombre','$super','$tipo_promotor','$id_estado','$usuario','$pass','$no_cel','$imei',CURDATE(),NOW(),'$no_nomina',
					'$email','$talla_p','$nombre_emer','$tel_emer','$cel_vang','$emailVang','$rfc','$curp','$imss', '$fechaImss')";
	$base = DataBase::getInstance();
	$result = $base->ejecutarConsulta($sql);


	if ($result){
		echo '1';
	} else {
		echo '0';
	}



}else{

    echo 'Access Denid';
}


