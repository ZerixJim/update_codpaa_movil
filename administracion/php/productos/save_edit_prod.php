<?

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

$nombre = $_REQUEST['nombre_prodE'];
$idMarca = $_REQUEST['MarcaE'];
$presentacion = $_REQUEST['presentacionE'];
$codigo = $_REQUEST['codigoE'];
$tipo = $_REQUEST['tipo_prodE'];
$modelo = $_REQUEST['modelo_prodE'];
$idProducto=$_REQUEST['id_prodE'];

if(is_uploaded_file($_FILES['imagen_prodE']['tmp_name']))
{
 	$file_path = "../../images/productos/".$idMarca."/";
 
	 if(!is_dir($file_path)){

        mkdir($file_path);

    }
		
	$nom_arch=pathinfo($_FILES['imagen_prodE']['name']);	
	
	
		$error_img=0;
	if(!move_uploaded_file($_FILES['imagen_prodE']['tmp_name'], $file_path.$idProducto.".".$nom_arch['extension']))
	{
		$error_img=1;
		
		}
			 
}


$sql = "update Producto set nombre='$nombre',presentacion='$presentacion',idMarca='$idMarca',codigoBarras='$codigo',tipo='$tipo',modelo='$modelo'
where idProducto='".$idProducto."'";
$base = new bdManager();
$result = $base->ejecutarConsulta($sql);


if ($result && $error_img==0){
	echo '1';
} else {
	echo '0';
}
?>