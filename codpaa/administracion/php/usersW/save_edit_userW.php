<?

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$base = new bdManager();

$perfil = $_REQUEST['Perfil'];
$nombre = $_REQUEST['nombre'];
$usuario = $_REQUEST['usuario'];
$pass = $_REQUEST['password'];
$idUsuario=$_REQUEST['idUsuario'];
$idSupervisor=$_REQUEST['supervisor'];
$idCliente=$_REQUEST['cliente'];
$idGerente=$_REQUEST['gerente'];
$email=$_REQUEST['email'];



if($perfil=='1' || $perfil=='2' || $perfil=='10')
{
	$permiso='3';
	$tipo_usuario='ALL';
	}
else if ( $perfil=='3' || $perfil=='5' || $perfil=='6' || $perfil=='9' || $perfil=='8')
{
	$permiso='2';
	$tipo_usuario='';
	}
else
{
	$permiso='1';
	}


$sql_pass="select * from usuarios where idUsuario='$idUsuario' and pass='$pass'";
$result_pass=$base->ejecutarConsulta($sql_pass);
$n_result=mysqli_num_rows($result_pass);

$contra="";
if($n_result!=1)
{
	$contra=",pass=md5('$pass')";
	}

$sql = "update usuarios  set id_perfil='$perfil',nombre='$nombre',user='$usuario',idPermiso='$permiso',tipo_usuario='$tipo_usuario',idCliente='$idCliente',idSupervisor='$idSupervisor',email='$email' ".$contra.",idGerente='$idGerente'
where idUsuario='$idUsuario'";

$result = $base->ejecutarConsulta($sql);


if ($result){
	echo '1';
} else {
	echo '0';
}
?>