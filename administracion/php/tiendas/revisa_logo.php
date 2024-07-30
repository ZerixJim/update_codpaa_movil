<? 
ob_start();
session_start();

$imagen=$_REQUEST['Marca'];

$revisa_logo=0;
if(file_exists('../../imagenes/logos/'.$imagen.'.png'))
{
	$revisa_logo=1;
}
else{
	$revisa_logo=0;
}
echo $revisa_logo;
?>