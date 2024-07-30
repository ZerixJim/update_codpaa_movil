<? 
ob_start();
session_start();

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
 
</head>

<body>
<? 
$idUsuario = $_SESSION['idUser'];

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$manager = new bdManager();

$filtro="";
		if(isset($_REQUEST['Busq']))
		{
			
			$filtro=" and (m.idTienda='".$_REQUEST['Busq']."' OR grupo like '%".$_REQUEST['Busq']."%' OR sucursal like '%".$_REQUEST['Busq']."%' OR municipio='%".$_REQUEST['Busq']."%')";
			}
$sql = '';

	if($_SESSION['id_perfil'] == '5' || $_SESSION['id_perfil'] == '2'){
		$sql = "SELECT m.idTienda, grupo, numeroEconomico, sucursal, municipio, UPPER(estados.nombre) as nombre,x,y,concat(m.direccion,' colonia:',m.colonia) as direcc
		FROM maestroTiendas m 
		inner join estados on (m.idEstado=estados.id)
		inner join cod_tienda_marca_promotor cod on (cod.idTienda=m.idTienda)
		where m.idFormato!='0' and cod.idMarca IN (select idMarca from usuariosMarcaAsignada where idUsuario='".$idUsuario."')
		".$filtro." 
		group by m.idTienda
		order by m.idTienda asc";
		
	}
	else if($_SESSION['id_perfil'] != '6'){
		$sql = "SELECT m.idTienda, grupo, numeroEconomico, sucursal, municipio, UPPER(estados.nombre) as nombre,x,y,concat(m.direccion,' colonia:',m.colonia) as direcc
		FROM maestroTiendas m
		inner join estados on (m.idEstado=estados.id)
		where m.idFormato!='0'
		".$filtro." order by idTienda asc";
		
	}else if($_SESSION['id_perfil'] == '6'){
		
		$sql="SELECT m.idTienda, grupo, numeroEconomico, sucursal, municipio, UPPER(estados.nombre) as nombre,x,y,concat(m.direccion,' colonia:',m.colonia) as direcc
		FROM maestroTiendas m
		inner join estados on (m.idEstado=estados.id)
		inner join cod_tienda_marca_promotor cod on (cod.idTienda=m.idTienda)
		where m.idFormato!='0' and 
		cod.idMarca IN (select idMarca from ClientesMarcas where idCliente=(select idCliente from usuarios where idUsuario='".$idUsuario."'))
		".$filtro." 
		group by m.idTienda
		order by m.idTienda asc";
	}

$rs = $manager->ejecutarConsulta($sql);
    $result = array();  
    while($row = mysqli_fetch_object($rs)){  
	
		$row->nombre=utf8_encode($row->nombre);
		
        array_push($result, $row);  
    } 

	$resul_puntos= json_encode($result,JSON_PARTIAL_OUTPUT_ON_ERROR);  
	
	echo "<script>cargaMapaTnd(".$resul_puntos.");</script>";
?>
<div align="center">

                
              <div id="Mapa_tiendas" style="width:800px;height:600px;"></div>
     </div>          
<script language="javascript" type="application/javascript">

 
</script>
</body>
</html>

<? 
}else{	
	echo 'no has iniciado sesion';
	header('refresh:2,../index.php');
}

?>